package top.belongme.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import top.belongme.model.result.Result;
import top.belongme.service.TaskService;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Title: TaskController
 * @ProjectName JobManage-Back
 * @Description: TODO
 * @Author DengChao
 * @Date 2023/2/1018:15
 */
@Slf4j
@RestController
@RequestMapping("/task")
public class TaskController {

    @Resource
    private TaskService taskService;

    /**
     * TODO 提交作业
     *
     * @Author DengChao
     * @Date 2023/2/11 18:49
     */
    @PreAuthorize("hasAuthority('job:task:insert')")
    @PostMapping(value = "/commitTask")
    public Result commitTask(@RequestParam("taskFile") MultipartFile uploadTaskFile, @RequestParam("belongBatchId") String belongBatchId) {
        return taskService.commitTask(uploadTaskFile, belongBatchId);
    }


    /**
     * TODO 取消提交作业
     *
     * @Author DengChao
     * @Date 2023/2/11 18:49
     */
    @PreAuthorize("hasAuthority('job:task:delete')")
    @DeleteMapping("/cancelCommit/{BatchId}")
    public Result cancelCommitTask(@PathVariable String BatchId) {
        return taskService.cancelCommitTask(BatchId);
    }


    /**
     * TODO 根据taskId下载指定作业文件
     *
     * @Author DengChao
     * @Date 2023/2/12 22:55
     */
    @PreAuthorize("hasAuthority('job:taskDetail:select')")
    @GetMapping("/downloadTask/{taskId}")
    public void getTaskFile(@PathVariable String taskId, HttpServletResponse response) throws IOException {
        taskService.getTaskFile(taskId, response);
    }

    /**
     * TODO 根据批次id下载整个批次文件夹的压缩包
     *
     * @Author DengChao
     * @Date 2023/2/13 20:50
     */
    @PreAuthorize("hasAuthority('job:taskDetail:select')")
    @GetMapping("/downloadBatch/{batchId}")
    public void getBatchFolder(@PathVariable String batchId, HttpServletResponse response) throws IOException {
        taskService.getBatchFile(batchId, response);
    }
}
