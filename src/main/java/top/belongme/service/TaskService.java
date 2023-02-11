package top.belongme.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;
import top.belongme.model.pojo.Task;
import top.belongme.model.result.Result;
import top.belongme.model.vo.TaskQueryVo;


/**
 * @Title: TaskService
 * @ProjectName JobManage-Back
 * @Description: TODO
 * @Author DengChao
 * @Date 2023/2/1018:17
 */
public interface TaskService extends IService<Task> {
    Result commitTask(MultipartFile uploadTaskFile, String belongBatchId);

    IPage<Task> selectPage(Page<Task> pageParam, TaskQueryVo taskQueryVo);

    Result cancelCommitTask(String taskIid);
}
