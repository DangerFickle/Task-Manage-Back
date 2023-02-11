package top.belongme.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import top.belongme.model.pojo.Task;
import top.belongme.model.result.Result;
import top.belongme.model.vo.TaskQueryVo;
import top.belongme.service.TaskService;

import javax.annotation.Resource;

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
    public Result commitTask(@RequestParam("taskFile") MultipartFile uploadTaskFile, @RequestParam("belongBatchId") String belongBatchId){
        return taskService.commitTask(uploadTaskFile,belongBatchId);
    }


    @PreAuthorize("hasAuthority('job:task:select')")
    @GetMapping("/listPage/{page}/{limit}")
    public Result getBatchList(@PathVariable Long page,
                                   @PathVariable Long limit,
                                   TaskQueryVo taskQueryVo){
        //创建page对象
        Page<Task> pageParam = new Page<>(page, limit);
        //调用service方法
        IPage<Task> pageModel = taskService.selectPage(pageParam, taskQueryVo);
        return new Result<>(200, "请求成功", pageModel);
    }

    /**
     * TODO 取消提交作业
     *
     * @Author DengChao
     * @Date 2023/2/11 18:49
     */
    @PreAuthorize("hasAuthority('job:task:delete')")
    @DeleteMapping("/cancelCommit/{BatchId}")
    public Result cancelCommitTask(@PathVariable String BatchId){
        return taskService.cancelCommitTask(BatchId);
    }
}
