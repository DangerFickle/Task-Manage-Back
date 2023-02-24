package top.belongme;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import top.belongme.model.pojo.Email;
import top.belongme.service.SendMailService;

import javax.annotation.Resource;

/**
 * @Title: TextEmailService
 * @ProjectName Task-Manage-Back
 * @Description: TODO
 * @Author DengChao
 * @Date 2023/2/2014:25
 */
@SpringBootTest
public class TextEmailService {

    @Resource
    SendMailService sendMailService;

    @Test
    void testSend() {
        Email mail = new Email("1975037337@qq.com", "测试邮件", "测试邮件");

        sendMailService.sendSimpleMail(mail);
    }
}
