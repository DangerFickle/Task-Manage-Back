package top.belongme.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.io.FileUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import top.belongme.exception.GlobalBusinessException;
import top.belongme.mapper.BatchMapper;
import top.belongme.mapper.TaskMapper;
import top.belongme.model.pojo.Batch;
import top.belongme.model.pojo.Task;
import top.belongme.model.pojo.user.LoginUser;
import top.belongme.model.result.Result;
import top.belongme.model.vo.TaskQueryVo;
import top.belongme.service.TaskService;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
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
public class TaskServiceImpl extends ServiceImpl<TaskMapper, Task> implements TaskService {
    @Resource
    private BatchMapper batchMapper;

    @Resource
    private TaskMapper taskMapper;

    @Resource
    private Date GMTDate;

    @Override
    public IPage<Task> selectPage(Page<Task> pageParam, TaskQueryVo taskQueryVo) {
        return baseMapper.selectPage(pageParam, taskQueryVo);
    }

    /**
     * TODO 提交作业
     *
     * @Author DengChao
     * @Date 2023/2/10 20:46
     */
    @Override
    public Result commitTask(MultipartFile uploadTaskFile, String belongBatchId) {
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
        StringBuilder originalFileNameBuilder = new StringBuilder(originalFileName);
        // 获取文件扩展名
        String fileExtension = originalFileNameBuilder.substring(originalFileNameBuilder.lastIndexOf("."));
        // 根据学号和姓名拼接文件名
        StringBuilder finalFileName = new StringBuilder();
        finalFileName.append(loginUser.getUser().getStudentNumber())
                .append(" ")
                .append(loginUser.getUser().getName())
                .append(fileExtension);

        try {
            // 获取提交的作业文件输入流
            InputStream taskFileInputStream = uploadTaskFile.getInputStream();
            // 拼接作业文件路径，所属批次的文件夹路径 + 作业文件名
            String taskFilePath = belongBatch.getFolderPath() + File.separator + finalFileName;
            // 将作业文件输入流复制到所属批次文件夹
            FileUtils.copyInputStreamToFile(taskFileInputStream, new File(taskFilePath));
            // 拼接提交的作业文件路径
            Task task = new Task();
            // 设置作业所属批次id
            task.setBelongBatchId(belongBatchId);
            // 设置作业文件路径
            task.setFilePath(taskFilePath);
            // 设置作业文件名
            task.setFileName(finalFileName.toString());
            // 设置提交人id
            task.setUploaderId(loginUser.getUser().getId());
            baseMapper.insert(task);
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

        // 判断所属批次是否已截止
        Batch belongBatch = batchMapper.selectById(task.getBelongBatchId());
        if (!belongBatch.getEndTime().equals(GMTDate)) {
            if (belongBatch.getEndTime().compareTo(new Date()) <= 0) {
                throw new GlobalBusinessException(800, "该批次已截止，取消提交失败");
            }
        }

        // 判断当前登陆用户是否为该作业的提交人
        Task task1 = taskMapper.selectOne(new QueryWrapper<Task>().eq("id", task.getId()).eq("uploader_id", loginUser.getUser().getId()));
        if (Objects.isNull(task1)) {
            throw new GlobalBusinessException(800, "您不是该作业的提交人");
        }
        File taskFile = new File(task.getFilePath());
        if (taskFile.exists()) {
            boolean delete = taskFile.delete();
            if (delete) {
                taskMapper.deleteById(task.getId());
                return new Result(200, "取消提交成功");
            } else {
                throw new GlobalBusinessException(800, "删除作业文件失败");
            }
        }
        throw new GlobalBusinessException(800, "该作业文件不存在");
    }
}
