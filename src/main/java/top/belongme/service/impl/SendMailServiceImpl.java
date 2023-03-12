package top.belongme.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import top.belongme.exception.GlobalBusinessException;
import top.belongme.mapper.BatchMapper;
import top.belongme.mapper.CourseMapper;
import top.belongme.mapper.UserMapper;
import top.belongme.model.pojo.Batch;
import top.belongme.model.pojo.Course;
import top.belongme.model.pojo.Email;
import top.belongme.model.pojo.user.User;
import top.belongme.model.result.Result;
import top.belongme.model.vo.RemindVo;
import top.belongme.service.SendMailService;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.util.Date;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

@Service
@Slf4j
public class SendMailServiceImpl implements SendMailService {

    // 注入邮件工具类
    @Resource
    private JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String sendMailer;

    @Value("${spring.mail.nickname}")
    private String nickname;

    @Resource
    private BatchMapper batchMapper;

    @Resource
    private CourseMapper courseMapper;

    @Resource
    private UserMapper userMapper;


    private void checkMail(Email email) {
        Assert.notNull(email, "邮件请求不能为空");
        Assert.notNull(email.getSendTo(), "邮件收件人不能为空");
        Assert.notNull(email.getSubject(), "邮件主题不能为空");
        Assert.notNull(email.getText(), "邮件内容不能为空");
    }

    @Override
    public void sendSimpleMail(Email email) {
        Thread emailThread = new Thread(() -> {
            checkMail(email);
            SimpleMailMessage message = new SimpleMailMessage();
            //邮件发件人
            message.setFrom(nickname + '<' + sendMailer + '>');
            //邮件收件人 1或多个
            message.setTo(email.getSendTo().split(","));
            //邮件主题
            message.setSubject(email.getSubject());
            //邮件内容
            message.setText(email.getText());
            //邮件发送时间
            message.setSentDate(new Date());

            javaMailSender.send(message);
            log.info("发送邮件成功:{}->{}", sendMailer, email.getSendTo());
        });
        emailThread.setName("简单邮件发送线程-" + emailThread.getId());
        emailThread.start();
    }

    @Override
    public void sendHtmlMail(Email email) {
        Thread emainThread = new Thread(() -> {
            checkMail(email);
            MimeMessage message = javaMailSender.createMimeMessage();
            try {
                MimeMessageHelper helper = new MimeMessageHelper(message, true);
                //邮件发件人
                helper.setFrom(nickname + '<' + sendMailer + '>');
                //邮件收件人 1或多个
                helper.setTo(email.getSendTo().split(","));
                //邮件主题
                helper.setSubject(email.getSubject());
                //邮件内容
                helper.setText(email.getText(), true);
                //邮件发送时间
                helper.setSentDate(new Date());

                String filePath = email.getFilePath();
                if (StringUtils.hasText(filePath)) {
                    FileSystemResource file = new FileSystemResource(new File(filePath));
                    String fileName = filePath.substring(filePath.lastIndexOf(File.separator));
                    helper.addAttachment(fileName, file);
                }
                javaMailSender.send(message);
                log.info("发送邮件成功:{}->{}", sendMailer, email.getSendTo());
            } catch (MessagingException e) {
                log.error("发送邮件时发生异常！", e);
            }
        });
        emainThread.setName("HTML邮件发送线程-" + emainThread.getId());
        emainThread.start();
    }

    @Override
    public Result remindUser(RemindVo remindVo) {
        // 查询批次信息
        Batch batch = batchMapper.selectById(remindVo.getBatchId());
        if (Objects.isNull(batch)) {
            log.error("邮件提醒失败，因为批次不存在");
            throw new GlobalBusinessException(800, "批次不存在");
        }
        // 批次已经截止
        if (batch.getEndTime().after(new Date())) {
            log.error("邮件提醒失败，因为批次已截止");
            throw new GlobalBusinessException(800, "批次已截止");
        }
        // 查询课程信息
        Course course = courseMapper.selectById(batch.getBelongCourseId());
        if (Objects.nonNull(course)) {
            if (course.getStatus() == 0) {
                log.error("邮件提醒失败，因为所属课程已被禁用");
                throw new GlobalBusinessException(800, "所属课程已被禁用");
            }
        }
        // 查询用户信息
        User user = userMapper.selectById(remindVo.getUserId());
        if (Objects.nonNull(user)) {
            // 判断用户是否绑定过邮箱
            if (Objects.isNull(user.getEmail())) {
                log.error("邮件提醒失败，因为用户【{}】未绑定邮箱", user.getName());
                throw new GlobalBusinessException(800, "用户【%s】未绑定邮箱".formatted(user.getName()));
            }
        } else {
            log.error("邮件提醒失败，因为用户不存在");
            throw new GlobalBusinessException(800, "用户不存在");
        }

        String remindTemplate =
                """
                        学习委员提醒您交作业啦！
                        课程：%s
                        批次：%s
                        请登陆系统提交您的作业
                        网站地址：https://task.belongme.top
                        """.formatted(course.getCourseName(), batch.getBatchName());

        Email email = new Email(user.getEmail(), "作业提交提醒", remindTemplate);
        // 发送邮件
        this.sendSimpleMail(email);
        log.info("邮件提醒【{}】成功", user.getName());
        return new Result<>(200, "邮件提醒【%s】成功".formatted(user.getName()));
    }
}

