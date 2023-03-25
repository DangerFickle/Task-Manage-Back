package top.belongme.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import net.lingala.zip4j.ZipFile;
import org.apache.commons.io.FileUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;
import top.belongme.exception.GlobalBusinessException;
import top.belongme.mapper.BatchMapper;
import top.belongme.mapper.CourseMapper;
import top.belongme.mapper.TaskMapper;
import top.belongme.mapper.UserMapper;
import top.belongme.model.pojo.Batch;
import top.belongme.model.pojo.Course;
import top.belongme.model.pojo.Email;
import top.belongme.model.pojo.task.Task;
import top.belongme.model.pojo.user.LoginUser;
import top.belongme.model.pojo.user.User;
import top.belongme.model.result.Result;
import top.belongme.service.SendMailService;
import top.belongme.service.TaskService;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * @Title: TaskServiceImpl
 * @ProjectName JobManage-Back
 * @Description: TODO
 * @Author DengChao
 * @Date 2023/2/1018:18
 */
@Service
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class TaskServiceImpl extends ServiceImpl<TaskMapper, Task> implements TaskService {
    @Resource
    private BatchMapper batchMapper;

    @Resource
    private TaskMapper taskMapper;

    @Resource
    private CourseMapper courseMapper;

    @Resource
    private UserMapper userMapper;

    // 格林威治时间
    @Resource
    private Date GMTDate;

    @Resource
    private String filePathBySystem;

    @Resource
    private SendMailService sendMailService;

    @Resource
    private SimpleDateFormat simpleDateFormat;


    /**
     * TODO 提交作业
     *
     * @Author DengChao
     * @Date 2023/2/10 20:46
     */
    @Override
    public Result commitTask(MultipartFile uploadTaskFile, String belongBatchId) {
        // 限制提交大小为30MB
        if (uploadTaskFile.getSize() > 30 * 1024 * 1024) {
            throw new GlobalBusinessException(800, "作业文件不得超过30MB");
        }

        Batch belongBatch = batchMapper.selectById(belongBatchId);
        if (Objects.isNull(belongBatch)) {
            throw new GlobalBusinessException(800, "该批次不存在，提交失败");
        }

        // 判断所属批次是否为格林威治时间，如果是则说明该批次没有设置截止时间，为永不截止，直接做提交操作
        if (!Objects.equals(belongBatch.getEndTime(), GMTDate)) {
            // 判断当前批次是否已截止
            int compare = belongBatch.getEndTime().compareTo(new Date());
            if (compare <= 0) {
                throw new GlobalBusinessException(800, "该批次已经截止，无法提交");
            }
        }

        // 获取当前登陆用户
        LoginUser loginUser = (LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        // 在作业表中查询该批次下该用户是否已经提交过作业
        QueryWrapper<Task> taskQueryWrapper = new QueryWrapper<>();
        taskQueryWrapper.eq("belong_batch_id", belongBatchId);
        taskQueryWrapper.eq("uploader_id", loginUser.getUser().getId());
        Task oldTask = taskMapper.selectOne(taskQueryWrapper);
        if (Objects.nonNull(oldTask)) {
            throw new GlobalBusinessException(800, "该批次您已经提交过了");
        }

        // 将上传的文件重命名为当前登陆用户的学号+姓名
        // 获取提交的作业文件名
        String originalFileName = uploadTaskFile.getOriginalFilename();
        if (Objects.isNull(originalFileName)) {
            throw new GlobalBusinessException(800, "获取上传的文件名失败");
        }
        // 获取文件扩展名
        String fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
        // 根据学号和姓名拼接文件名，学号+姓名+文件扩展名
        String finalFileName = loginUser.getUser().getStudentNumber() + " " + loginUser.getUser().getName() + fileExtension;

        // 拼接作业文件路径，所属批次的文件夹路径 + 作业文件名
        String taskFilePath = belongBatch.getFolderPath() + File.separator + finalFileName;
        // 拼接提交的作业文件路径
        Task task = new Task();
        // 设置作业所属批次id
        task.setBelongBatchId(belongBatchId);
        // 设置作业文件路径
        task.setFilePath(taskFilePath);
        // 设置作业文件名
        task.setFileName(finalFileName);
        // 设置提交人id为当前登陆的用户id
        task.setUploaderId(loginUser.getUser().getId());
        int insert = baseMapper.insert(task);
        if (insert <= 0) {
            throw new GlobalBusinessException(800, "作业提交失败");
        }

        try {
            // 获取提交的作业文件输入流
            InputStream taskFileInputStream = uploadTaskFile.getInputStream();
            // 将作业文件输入流复制到所属批次文件夹
            FileUtils.copyInputStreamToFile(taskFileInputStream, new File(taskFilePath));
            // 获取批次所属课程
            Course course = courseMapper.selectById(belongBatch.getBelongCourseId());

            // 判断用户是否设置了邮箱，如果设置了则发送邮件通知
            if (Objects.nonNull(loginUser.getUser().getEmail())) {
                // 给用户发送一封通知邮件
                String commitDate = simpleDateFormat.format(new Date());
                // 邮件标题
                String subject = "作业提交成功通知";
                // 正文模板
                String emailText =
                        """
                                您的作业提交成功啦！
                                课程：%s
                                批次：%s
                                提交人：%s
                                提交时间：%s
                                """.formatted(course.getCourseName(), belongBatch.getBatchName(), loginUser.getUser().getName(), commitDate);

                Email email = new Email(loginUser.getUser().getEmail(), subject, emailText);
                sendMailService.sendSimpleMail(email);
            }

            // 打印日志
            log.info("【{}】作业提交成功，所属课程：{}，所属批次：{}", loginUser.getUser().getName(), course.getCourseName(), belongBatch.getBatchName());

            // 通知管理员
            this.noticeCommitDetail(course, belongBatch);

            return new Result(200, "作业提交成功");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * TODO 取消提交作业
     *
     * @Author DengChao
     * @Date 2023/2/11 19:08
     */
    @Override
    public Result cancelCommitTask(String batchId) {
        Batch belongBatch = batchMapper.selectById(batchId);
        // 判断所属批次是否存在
        if (Objects.isNull(belongBatch)) {
            throw new GlobalBusinessException(800, "该批次不存在");
        }

        // 判断所属批次是否已截止
        if (!belongBatch.getEndTime().equals(GMTDate)) {
            if (belongBatch.getEndTime().compareTo(new Date()) <= 0) {
                throw new GlobalBusinessException(800, "该批次已截止，无法取消提交");
            }
        }

        // 获取当前登陆的用户
        LoginUser loginUser = (LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        QueryWrapper<Task> taskQueryWrapper = new QueryWrapper<>();
        taskQueryWrapper.eq("belong_batch_id", batchId);
        taskQueryWrapper.eq("uploader_id", loginUser.getUser().getId());
        // 查询作业表中是否有该用户，该批次的作业
        Task task = taskMapper.selectOne(taskQueryWrapper);
        if (Objects.isNull(task)) {
            throw new GlobalBusinessException(800, "您还没有提交过该批次呢");
        }

        // 删除数据库中的记录
        int delete1 = taskMapper.deleteById(task.getId());
        if (delete1 <= 0) {
            throw new GlobalBusinessException(800, "取消提交失败");
        }

        // 删除作业文件
        File taskFile = new File(task.getFilePath());
        if (taskFile.exists()) {
            boolean delete = taskFile.delete();
            if (delete) {
                // 获取所属课程
                Course course = courseMapper.selectById(belongBatch.getBelongCourseId());
                // 打印日志
                log.info("【{}】取消提交成功，所属课程：{}，所属批次：{}", loginUser.getUser().getName(), course.getCourseName(), belongBatch.getBatchName());
                return new Result(200, "取消提交成功");
            } else {
                throw new GlobalBusinessException(800, "删除作业文件失败");
            }
        }
        throw new GlobalBusinessException(800, "该作业文件不存在");
    }

    @Override
    public void getTaskFile(String taskId, HttpServletResponse response) throws IOException {
        // 通过响应头通知前端异常信息
        response.setHeader("Access-Control-Expose-Headers", "exception");
        Task task = taskMapper.selectById(taskId);
        if (Objects.isNull(task)) {
            response.setHeader("exception", "task not exist");
            throw new GlobalBusinessException(800, "该作业不存在");
        }
        // 查询该作业所属的批次是否已截止，已截止才可以下载
        Batch batch = batchMapper.selectById(task.getBelongBatchId());
        if (batch.getEndTime().after(new Date())) {
            response.setHeader("exception", "task batch not end");
            throw new GlobalBusinessException(800, "所属批次还未截止，无法下载");
        }

        File taskFile = new File(task.getFilePath());
        if (!taskFile.exists()) {
            response.setHeader("exception", "task file not exist");
            throw new GlobalBusinessException(800, "该作业文件不存在");
        }
        //在vue的response中显示Content-Disposition
        response.setHeader("Access-Control-Expose-Headers", "Content-Disposition");
        // 设置在下载框默认显示的文件名
        String filename = URLEncoder.encode(task.getFileName(), StandardCharsets.UTF_8);
        // 解决编码后空格变加号的问题
        filename = filename.replace("+", "%20");
        response.setHeader("Content-Disposition", "attachment;filename=" + filename);
        response.setHeader("Content-Length", String.valueOf(taskFile.length()));

        response.setContentType("application/octet-stream;charset=UTF-8");
        InputStream inputStream = null;
        BufferedInputStream bufferedInputStream = null;
        BufferedOutputStream bufferedOutputStream = null;
        try {
            inputStream = Files.newInputStream(taskFile.toPath());
            bufferedInputStream = new BufferedInputStream(inputStream);
            bufferedOutputStream = new BufferedOutputStream(response.getOutputStream());
            // 将文件流写入到response的输出流中
            FileCopyUtils.copy(bufferedInputStream, bufferedOutputStream);
            // 获取当前登陆的用户
            LoginUser loginUser = (LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            // 获取所属课程
            Course course = courseMapper.selectById(batch.getBelongCourseId());
            // 获取作业的提交人
            User uploader = userMapper.selectById(task.getUploaderId());
            // 打印日志
            log.info("管理员【{}】下载了：【{}】的【{}】批次下，【{}】的作业", loginUser.getUser().getName(), course.getCourseName(), batch.getBatchName(), uploader.getName());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
            if (bufferedInputStream != null) {
                bufferedInputStream.close();
            }
            if (bufferedOutputStream != null) {
                bufferedOutputStream.flush();
                bufferedOutputStream.close();
            }
        }
    }

    @Override
    public void getBatchFile(String batchId, HttpServletResponse response) throws IOException {

        // 通过响应头通知前端异常信息
        response.setHeader("Access-Control-Expose-Headers", "exception");
        // 设置响应类型为文本
        Batch batch = batchMapper.selectById(batchId);
        if (Objects.isNull(batch)) {
            response.setHeader("exception", "batch not exist");
            throw new GlobalBusinessException(800, "该批次不存在");
        }

        if (batch.getEndTime().after(new Date()) || batch.getEndTime().equals(GMTDate)) {
            response.setHeader("exception", "batch not end");
            throw new GlobalBusinessException(800, "该批次还未截止，无法下载");
        }

        // 判断所属课程是否被禁用
        Course course = courseMapper.selectById(batch.getBelongCourseId());
        if (course.getStatus() == 0) {
            response.setHeader("exception", "course is disabled");
            throw new GlobalBusinessException(800, "所属课程已被禁用，无法下载");
        }

        // 判断批次下是否存在作业
        QueryWrapper<Task> taskQueryWrapper = new QueryWrapper<>();
        taskQueryWrapper.eq("belong_batch_id", batchId);
        long count = taskMapper.selectCount(taskQueryWrapper);
        if (count == 0) {
            response.setHeader("exception", "no task");
            throw new GlobalBusinessException(800, "该批次下还没有作业");
        }

        // 获取该批次的文件夹
        String batchFolderPath = batch.getFolderPath();
        File batchFolder = new File(batchFolderPath);
        if (!batchFolder.exists()) {
            response.setHeader("exception", "batch folder not exist");
            throw new GlobalBusinessException(800, "该批次文件夹不存在");
        }

        // 获取该批次文件夹下的的所有作业文件
        List<File> fileList = Arrays.asList(batchFolder.listFiles());
        // 以所属课程名 + 批次名作为zip文件名
        String fileName = course.getCourseName() + "---" + batch.getBatchName();
        // 拼接压缩包的路径
        String filePath = filePathBySystem + "temp_files" + File.separator + fileName + ".zip";
        // 压缩文件
        new ZipFile(filePath).addFiles(fileList);

        // 获取压缩后的zip文件
        File taskFilesZip = new File(filePath);
        if (!taskFilesZip.exists()) {
            throw new GlobalBusinessException(800, "压缩文件失败");
        }

        //在vue的response中显示Content-Disposition
        response.setHeader("Access-Control-Expose-Headers", "Content-Disposition");
        // 设置在下载框默认显示的文件名
        response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(taskFilesZip.getName(), "UTF-8"));
        response.setHeader("Content-Length", String.valueOf(taskFilesZip.length()));

        response.setContentType("application/octet-stream;charset=UTF-8");
        InputStream inputStream = null;
        BufferedInputStream bufferedInputStream = null;
        BufferedOutputStream bufferedOutputStream = null;
        try {
            inputStream = Files.newInputStream(taskFilesZip.toPath());
            bufferedInputStream = new BufferedInputStream(inputStream);
            bufferedOutputStream = new BufferedOutputStream(response.getOutputStream());
            // 将文件流写入到response的输出流中
            FileCopyUtils.copy(bufferedInputStream, bufferedOutputStream);

            // 获取当前登陆的用户
            LoginUser loginUser = (LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            // 打印日志
            log.info("管理员【{}】，下载了：【{}】下的【{}】批次下所有作业", loginUser.getUser().getName(), course.getCourseName(), batch.getBatchName());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
            if (bufferedInputStream != null) {
                bufferedInputStream.close();
            }
            if (bufferedOutputStream != null) {
                bufferedOutputStream.flush();
                bufferedOutputStream.close();
            }
            // 删除临时文件夹中的压缩包
            taskFilesZip.delete();
        }
    }

    private void noticeCommitDetail(Course course, Batch batch) {
        // 查询管理员，不包含系统管理员
        List<User> managerList = userMapper.selectList(new QueryWrapper<User>().eq("role_id", 2));

        StringBuilder sendTo = new StringBuilder();
        // 获取管理员的邮箱
        for (User user : managerList) {
            if (Objects.nonNull(user.getEmail())) {
                sendTo.append(user.getEmail()).append(",");
            }
        }

        // 如果收集管理员邮箱的StringBuilder长度为0，则不发送邮件
        if (sendTo.length() == 0) {
            return;
        }

        // 查询用户总数，不包含系统管理员
        Long userCount = userMapper.selectCount(new QueryWrapper<User>().ne("id", 1));

        // 查询已交人数
        QueryWrapper<Task> qw = new QueryWrapper<>();
        qw.eq("belong_batch_id", batch.getId());
        Long alreadyCount = taskMapper.selectCount(qw);

        if (alreadyCount == 20L || alreadyCount == 30L || alreadyCount == 40L || alreadyCount == 50L) {
            // 邮件通知正文
            String emailTemplate =
                    """
                            课程：%s
                            批次：%s
                            已交人数：%s
                            网站地址：https://task.belongme.top
                            """.formatted(course.getCourseName(), batch.getBatchName(), alreadyCount);
            //发送邮件通知管理员
            Email email = new Email(sendTo.toString(), "作业收集进度通知", emailTemplate);
            sendMailService.sendSimpleMail(email);
        } else if (alreadyCount.equals(userCount)) {
            // 邮件通知正文
            String emailTemplate =
                    """
                            课程：%s
                            批次：%s
                            作业已经收集完毕啦，快去下载吧！
                            网站地址：https://task.belongme.top
                            """.formatted(course.getCourseName(), batch.getBatchName());
            //发送邮件通知管理员
            Email email = new Email(sendTo.toString(), "作业收集进度通知", emailTemplate);
            sendMailService.sendSimpleMail(email);
        }

    }
}
