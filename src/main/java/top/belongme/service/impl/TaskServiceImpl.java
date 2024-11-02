package top.belongme.service.impl;

import com.alibaba.druid.util.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import net.lingala.zip4j.ZipFile;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import top.belongme.exception.GlobalBusinessException;
import top.belongme.mapper.*;
import top.belongme.model.pojo.Batch;
import top.belongme.model.pojo.Course;
import top.belongme.model.pojo.Email;
import top.belongme.model.pojo.Group;
import top.belongme.model.pojo.task.Task;
import top.belongme.model.pojo.user.LoginUser;
import top.belongme.model.pojo.user.User;
import top.belongme.model.result.Result;
import top.belongme.service.SendMailService;
import top.belongme.service.TaskService;
import top.belongme.utils.LoginUserUtil;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
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
    private GroupMapper groupMapper;

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
    private String fileSaveLocation;
    @Resource
    private SendMailService sendMailService;

    @Resource
    private SimpleDateFormat simpleDateFormat;

    @Value("${spring.mail.web-site}")
    private String webSite;

    /**
     * TODO 检查要上传的文件是否已经存在
     *
     * @Author DengChao
     * @Date 2023/10/29 15:53
     */
    @Override
    public Result checkFileExist(String fileSha256, String fileType) {
        // 拼接文件路径
        File file = new File(fileSaveLocation + fileSha256 + fileType);
        if (file.exists()) {
            return new Result(800, "文件已存在", true);
        } else {
            return new Result(800, "文件不存在", false);
        }
    }

    /**
     * TODO 提交作业
     *
     * @Author DengChao
     * @Date 2023/2/10 20:46
     */
    @Override
    public Result commitTask(MultipartFile uploadTaskFile,
                             String belongBatchId,
                             String fileSha256,
                             String taskType) {
        // 限制提交大小为50MB
        if (uploadTaskFile.getSize() > 50 * 1024 * 1024) {
            throw new GlobalBusinessException(800, "作业文件不得超过50MB");
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
                throw new GlobalBusinessException(800, "该批次已经截止");
            }
        }

        String uploaderId = LoginUserUtil.getCurrentLoginUserId();
        // 如果是群组作业则将uploaderId设置为groupId
        if ("group".equals(taskType)) {
            uploaderId = groupMapper.getGroupId(belongBatch.getBelongCourseId(), LoginUserUtil.getCurrentLoginUserId());
            if (StringUtils.isEmpty(uploaderId)) {
                throw new GlobalBusinessException(800, "您还未加入任何群组");
            }
        }
        // 获取当前登陆用户
        LoginUser loginUser = LoginUserUtil.getCurrentLoginUser();
        // 在作业表中查询该批次下该用户是否已经提交过作业
        QueryWrapper<Task> taskQueryWrapper = new QueryWrapper<>();
        taskQueryWrapper.eq("belong_batch_id", belongBatchId);
        taskQueryWrapper.eq("uploader_id", uploaderId);
        Long oldTask = taskMapper.selectCount(taskQueryWrapper);
        if (oldTask != 0) {
            throw new GlobalBusinessException(800, "该批次您已经提交过了");
        }

        // 将上传的文件重命名为当前登陆用户的学号+姓名
        // 获取提交的作业文件名
        String originalFileName = uploadTaskFile.getOriginalFilename();
        if (StringUtils.isEmpty(originalFileName)) {
            throw new GlobalBusinessException(800, "获取上传的文件名失败");
        }
        // 获取文件扩展名
        String fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));


        // 将作业信息写入数据库
        Task task = Task.builder()
                // 设置作业所属批次id
                .belongBatchId(belongBatchId)
                // 设置提交人id为当前登陆的用户id
                .uploaderId(uploaderId)
                .fileSha256(fileSha256)
                .fileType(fileExtension)
                .build();
        int insert = baseMapper.insert(task);
        if (insert <= 0) {
            throw new GlobalBusinessException(800, "作业提交失败");
        }

        try {
            // 获取提交的作业文件输入流
            InputStream taskFileInputStream = uploadTaskFile.getInputStream();
            // 拼接作业文件路径，所属批次的文件夹路径 + 作业文件名
            String taskFilePath = fileSaveLocation + fileSha256 + fileExtension;

            File taskFile = new File(taskFilePath);
            // 将作业文件输入流复制到文件夹
            FileUtils.copyInputStreamToFile(taskFileInputStream, taskFile);

            // 获取批次所属课程
            Course course = courseMapper.selectById(belongBatch.getBelongCourseId());
//            // 判断用户是否设置了邮箱，如果设置了则发送邮件通知
//            if (Objects.nonNull(loginUser.getUser().getEmail())) {
//                // 给用户发送一封通知邮件
//                String commitDate = simpleDateFormat.format(new Date());
//                // 邮件标题
//                String subject = "作业提交成功通知";
//                // 正文模板
//                String emailText = """
//                        您的个人作业提交成功啦！
//                        课程：%s
//                        批次：%s
//                        提交人：%s
//                        提交时间：%s
//                        """.formatted(course.getCourseName(), belongBatch.getBatchName(), loginUser.getUser().getName(), commitDate);
//
//                Email email = new Email(loginUser.getUser().getEmail(), subject, emailText);
//                sendMailService.sendSimpleMail(email);
//            }

            // 打印日志
            log.info("【{}】作业提交成功，所属课程：{}，所属批次：{}", loginUser.getUser().getName(), course.getCourseName(), belongBatch.getBatchName());

            // 通知管理员
            // this.noticeCommitDetail(course, belongBatch);
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

        String uploaderId = LoginUserUtil.getCurrentLoginUserId();
        if (belongBatch.getBatchType().equals("group")) {
            uploaderId = groupMapper.getGroupId(belongBatch.getBelongCourseId(), uploaderId);
        }

        Task taskParam = Task
                .builder()
                .belongBatchId(batchId)
                .uploaderId(uploaderId)
                .build();
        QueryWrapper<Task> qw = Wrappers.query(taskParam);
        // 查询作业表中是否有该用户，该批次的作业
        Task task = this.getOne(qw);
        if (Objects.isNull(task)) {
            throw new GlobalBusinessException(800, "您还没有提交过该批次呢");
        }

        // 检查该作业对应的文件是否还被其他用户引用
        QueryWrapper<Task> shaQW = new QueryWrapper<>();
        shaQW.eq("file_sha256", task.getFileSha256())
                .and(i -> i.ne("uploader_id", task.getUploaderId()));
        Long count = taskMapper.selectCount(shaQW);


        // 如果没有被引用，删除数据库中作业的记录
        int delete1 = taskMapper.deleteById(task.getId());
        if (delete1 <= 0) {
            throw new GlobalBusinessException(800, "取消提交失败");
        }

        // 如果没有被引用，则删除文件
        if (count == 0) {
            File file = new File(fileSaveLocation + task.getFileSha256() + task.getFileType());
            if (file.exists()) {
                file.delete();
            } else {
                throw new GlobalBusinessException(800, "文件不存在");
            }
        }


        // 获取所属课程
        Course course = courseMapper.selectById(belongBatch.getBelongCourseId());
        // 打印日志
        // 获取当前登陆的用户
        LoginUser loginUser = LoginUserUtil.getCurrentLoginUser();
        log.info("【{}】取消提交成功，所属课程：{}，所属批次：{}", loginUser.getUser().getName(), course.getCourseName(), belongBatch.getBatchName());
        return Result.ok("取消提交成功");
    }

    /**
     * TODO 管理员根据作业id，下载作业
     *
     * @Author DengChao
     * @Date 2023/10/29 15:08
     */
    @Override
    public void getTaskFile(String taskId, HttpServletResponse response) throws IOException {
        // 通过响应头通知前端异常信息
        response.setHeader("Access-Control-Expose-Headers", "exception");
        Task task = taskMapper.selectById(taskId);
        if (Objects.isNull(task)) {
            response.setHeader("exception", URLEncoder.encode("该作业不存在", StandardCharsets.UTF_8));
            throw new GlobalBusinessException(800, "该作业不存在");
        }

        // 获取当前登陆的用户
        LoginUser loginUser = LoginUserUtil.getCurrentLoginUser();

        // 查询该作业所属的批次是否已截止，已截止才可以下载
        Batch batch = batchMapper.selectById(task.getBelongBatchId());
        if (batch.getEndTime().after(new Date()) || batch.getEndTime().equals(GMTDate)) {
            response.setHeader("exception", URLEncoder.encode("所属批次还未截止", StandardCharsets.UTF_8));
            throw new GlobalBusinessException(800, "所属批次还未截止");
        }

        // 拼接文件真实路径
        String filePath = fileSaveLocation + task.getFileSha256() + task.getFileType();
        File taskFile = new File(filePath);
        if (!taskFile.exists()) {
            response.setHeader("exception", URLEncoder.encode("该作业文件不存在，文件系统中被人为删除了", StandardCharsets.UTF_8));
            throw new GlobalBusinessException(800, "该作业文件不存在，文件系统中被人为删除了");
        }
        //在vue的response中显示Content-Disposition
        response.setHeader("Access-Control-Expose-Headers", "Content-Disposition");

        // 获取所属课程
        Course course = courseMapper.selectById(batch.getBelongCourseId());

         // 如果是群组作业，则使用专属命名方式
        if ("group".equals(batch.getBatchType())) {
            Group group = groupMapper.selectById(task.getUploaderId());
            task.setFileName(course.getCourseName() + "-" + batch.getBatchName() + "-" + group.getName() + task.getFileType());
        } else {
            // 获取提交者
            User user = userMapper.selectById(task.getUploaderId());
            // 设置前端显示的文件名
            task.setFileName(user.getStudentNumber() + "-" + user.getName() + task.getFileType());
        }

        // 设置在下载框默认显示的文件名
        String filename = URLEncoder.encode(task.getFileName(), StandardCharsets.UTF_8);
        // 解决编码后空格变加号的问题
//        filename = filename.replace("+", "%20");
        response.setHeader("Content-Disposition", "attachment;filename=" + filename);
        response.setHeader("Content-Length", String.valueOf(taskFile.length()));
        response.setContentType("application/octet-stream;charset=UTF-8");

        // 将文件流写入到response的输出流中
        FileUtils.copyFile(taskFile, response.getOutputStream());


        // 获取作业的提交人
        User uploader = userMapper.selectById(task.getUploaderId());
        // 打印日志
        log.info("管理员【{}】下载了：【{}】的【{}】批次下，【{}】的作业", loginUser.getUser().getName(), course.getCourseName(), batch.getBatchName(), uploader.getName());
    }

    /**
     * TODO 用户根据批次id，下载作业
     *
     * @Author DengChao
     * @Date 2023/10/29 15:06
     */
    @Override
    public void getTaskFileByBelongBatchId(String belongBatchId, HttpServletResponse response) throws IOException {
        Batch batch = batchMapper.selectById(belongBatchId);
        if (Objects.isNull(batch)) {
            throw new GlobalBusinessException(800, "该批次不存在");
        }
        Course course = courseMapper.selectById(batch.getBelongCourseId());
        if (Objects.isNull(course)) {
            throw new GlobalBusinessException(800, "该课程不存在");
        }
        response.setHeader("Access-Control-Expose-Headers", "exception");
        // 获取当前登陆的用户
        LoginUser loginUser = LoginUserUtil.getCurrentLoginUser();
        // 根据批次id和uploaderId查询作业
        QueryWrapper<Task> taskQueryWrapper = new QueryWrapper<>();
        taskQueryWrapper.eq("belong_batch_id", belongBatchId);

        String uploaderId = loginUser.getUser().getId();
        if ("group".equals(batch.getBatchType())) {
            uploaderId = groupMapper.getGroupId(batch.getBelongCourseId(), uploaderId);
        }

        taskQueryWrapper.eq("uploader_id", uploaderId);
        Task task = taskMapper.selectOne(taskQueryWrapper);
        if (Objects.isNull(task)) {
            response.setHeader("exception", URLEncoder.encode("您还没有提交过该批次呢", StandardCharsets.UTF_8));
            throw new GlobalBusinessException(800, "您还没有提交过该批次呢");
        }

        // 设置前端显示的文件名
        if ("group".equals(batch.getBatchType())) {
            task.setFileName(course.getCourseName() + "-" + batch.getBatchName() + task.getFileType());
        } else {
            task.setFileName(loginUser.getUser().getStudentNumber() + "-" + loginUser.getUser().getName() + task.getFileType());
        }

        // 拼接文件真实路径
        String filePath = fileSaveLocation + task.getFileSha256() + task.getFileType();
        File taskFile = new File(filePath);
        // 设置在下载框默认显示的文件名
        String filename = URLEncoder.encode(task.getFileName(), StandardCharsets.UTF_8);
        // 解决编码后空格变加号的问题
//        filename = filename.replace("+", "%20");
        //在vue的response中显示Content-Disposition
        response.setHeader("Access-Control-Expose-Headers", "Content-Disposition");
        response.setHeader("Content-Disposition", "attachment;filename=" + filename);
        response.setHeader("Content-Length", String.valueOf(taskFile.length()));
        response.setContentType("application/octet-stream;charset=UTF-8");

        FileUtils.copyFile(taskFile, response.getOutputStream());

        log.info("用户【{}】，下载了【{}】课程中【{}】批次的作业", loginUser.getUser().getName(), course.getCourseName(), batch.getBatchName());
    }

    /**
     * TODO 批量下载一个批次下的所有作业文件
     *
     * @Author DengChao
     * @Date 2023/10/29 15:11
     */
    @Override
    public void getBatchFile(String batchId, HttpServletResponse response) throws IOException {
        // 通过响应头通知前端异常信息
        response.setHeader("Access-Control-Expose-Headers", "exception");
        // 设置响应类型为文本
        Batch batch = batchMapper.selectById(batchId);
        if (Objects.isNull(batch)) {
            response.setHeader("exception", URLEncoder.encode("该批次不存在", StandardCharsets.UTF_8));
            throw new GlobalBusinessException(800, "该批次不存在");
        }

        if (batch.getEndTime().after(new Date()) || batch.getEndTime().equals(GMTDate)) {
            response.setHeader("exception", URLEncoder.encode("该批次还未截止", StandardCharsets.UTF_8));
            throw new GlobalBusinessException(800, "该批次还未截止");
        }

        // 判断所属课程是否被禁用
        Course course = courseMapper.selectById(batch.getBelongCourseId());
        if (course.getStatus() == 0) {
            response.setHeader("exception", URLEncoder.encode("所属课程已被禁用", StandardCharsets.UTF_8));
            throw new GlobalBusinessException(800, "所属课程已被禁用");
        }

        // 判断批次下是否存在作业
        QueryWrapper<Task> taskQueryWrapper = new QueryWrapper<>();
        taskQueryWrapper.eq("belong_batch_id", batchId);
        long count = taskMapper.selectCount(taskQueryWrapper);
        if (count == 0) {
            response.setHeader("exception", URLEncoder.encode("该批次下还没有作业", StandardCharsets.UTF_8));
            throw new GlobalBusinessException(800, "该批次下还没有作业");
        }


        // 以所属课程名 + 批次名作为zip文件名
        String fileName = course.getCourseName() + "—" + batch.getBatchName();
        // 拼接压缩包的路径
        String filePath = filePathBySystem + "temp_files" + File.separator + fileName + ".zip";
        // 压缩文件
        ZipFile zipFile = new ZipFile(filePath);

        // 获取该批次下的所有作业记录
        List<Task> taskList = taskMapper.selectList(taskQueryWrapper);
        for (Task task : taskList) {
            // 根据作业的sha256和文件后缀构建所有文件
            String taskFilePath = fileSaveLocation + task.getFileSha256() + task.getFileType();
            zipFile.addFile(new File(taskFilePath));
            String oldFileName = task.getFileSha256() + task.getFileType();
            String newFileName = "没有对应的提交者" + task.getFileType();
            // 找到该task的提交者
            if ("personal".equals(batch.getBatchType())) {
                // 个人作业的文件名
                List<User> personalUploaders = taskMapper.getPersonalTaskUploaders(batchId);
                for (User user : personalUploaders) {
                    if (user.getId().equals(task.getUploaderId())) {
                        newFileName = user.getStudentNumber() + "-" + user.getName() + task.getFileType();
                    }
                }
            } else {
                // 群组作业的文件名
                List<Group> groupUploaders = taskMapper.getGroupTaskUploaders(batchId);
                for (Group group : groupUploaders) {
                    if (group.getId().equals(task.getUploaderId())) {
                        newFileName = group.getName() + task.getFileType();
                    }
                }
            }
            zipFile.renameFile(oldFileName, newFileName);
        }

        // 获取压缩后的zip文件
        File taskFilesZip = new File(filePath);
        if (!taskFilesZip.exists()) {
            throw new GlobalBusinessException(800, "压缩文件失败");
        }

        //在vue的response中显示Content-Disposition
        response.setHeader("Access-Control-Expose-Headers", "Content-Disposition");
        // 设置在下载框默认显示的文件名
        response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(taskFilesZip.getName(), StandardCharsets.UTF_8));
        response.setHeader("Content-Length", String.valueOf(taskFilesZip.length()));
        response.setContentType("application/octet-stream;charset=UTF-8");
        // 将压缩包写入到response的输出流中
        FileUtils.copyFile(taskFilesZip, response.getOutputStream());
        // 获取当前登陆的用户
        LoginUser loginUser = LoginUserUtil.getCurrentLoginUser();
        log.info("管理员【{}】，下载了：【{}】下的【{}】批次下所有作业", loginUser.getUser().getName(), course.getCourseName(), batch.getBatchName());
        // 删除临时文件夹中的压缩包
        taskFilesZip.delete();
    }


    @Override
    public Result secondTransmit(String belongBatchId, String fileSha256, String fileType) {
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
        LoginUser loginUser = LoginUserUtil.getCurrentLoginUser();
        String uploaderId = loginUser.getUser().getId();
        if ("group".equals(belongBatch.getBatchType())) {
            uploaderId = groupMapper.getGroupId(belongBatch.getBelongCourseId(), uploaderId);
            if (StringUtils.isEmpty(uploaderId)) {
                throw new GlobalBusinessException(800, "您还未加入任何群组");
            }
        }
        // 在作业表中查询该批次下该用户是否已经提交过作业
        QueryWrapper<Task> taskQueryWrapper = new QueryWrapper<>();
        taskQueryWrapper.eq("belong_batch_id", belongBatchId);
        taskQueryWrapper.eq("uploader_id", uploaderId);
        Task oldTask = taskMapper.selectOne(taskQueryWrapper);
        if (Objects.nonNull(oldTask)) {
            throw new GlobalBusinessException(800, "该批次您已经提交过了");
        }

        // 拼接提交的作业文件路径
        Task task = Task.builder()
                // 设置作业所属批次id
                .belongBatchId(belongBatchId)
                // 设置提交人id为当前登陆的用户id,或群组id
                .uploaderId(uploaderId)
                .fileSha256(fileSha256)
                .fileType(fileType)
                .build();
        int insert = baseMapper.insert(task);
        if (insert <= 0) {
            throw new GlobalBusinessException(800, "作业提交失败");
        }

        // 获取批次所属课程
        Course course = courseMapper.selectById(belongBatch.getBelongCourseId());
//        // 判断用户是否设置了邮箱，如果设置了则发送邮件通知
//        if (Objects.nonNull(loginUser.getUser().getEmail())) {
//            // 给用户发送一封通知邮件
//            String commitDate = simpleDateFormat.format(new Date());
//            // 邮件标题
//            String subject = "作业提交成功通知";
//            // 正文模板
//            String emailText = """
//                    您的作业提交成功啦！
//                    课程：%s
//                    批次：%s
//                    提交人：%s
//                    提交时间：%s
//                    """.formatted(course.getCourseName(), belongBatch.getBatchName(), loginUser.getUser().getName(), commitDate);
//
//            Email email = new Email(loginUser.getUser().getEmail(), subject, emailText);
//            sendMailService.sendSimpleMail(email);
//        }

        // 打印日志
        log.info("【{}】作业提交成功，所属课程：{}，所属批次：{}", loginUser.getUser().getName(), course.getCourseName(), belongBatch.getBatchName());

        // 通知管理员
        // this.noticeCommitDetail(course, belongBatch);
        return new Result(200, "作业提交成功");

    }


    /**
     * TODO 通知管理员批次的提交情况
     *
     * @Author DengChao
     * @Date 2023/3/26 22:26
     */
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

        // 计算百分比
        int percent = (int) (alreadyCount / userCount * 100);
        if (percent >= 90) {
            // 邮件通知正文
            String emailTemplate = """
                    课程：%s
                    批次：%s
                    已交人数：%s
                    网站地址：%s
                    """.formatted(course.getCourseName(), batch.getBatchName(), alreadyCount, webSite);
            //发送邮件通知管理员
            Email email = new Email(sendTo.toString(), "作业收集进度通知", emailTemplate);
            sendMailService.sendSimpleMail(email);
        } else if (alreadyCount.equals(userCount)) {
            // 邮件通知正文
            String emailTemplate = """
                    课程：%s
                    批次：%s
                    作业已经收集完毕啦，快去下载吧！
                    网站地址：%s
                    """.formatted(course.getCourseName(), batch.getBatchName(), webSite);
            //发送邮件通知管理员
            Email email = new Email(sendTo.toString(), "作业收集进度通知", emailTemplate);
            sendMailService.sendSimpleMail(email);
        }

    }
}
