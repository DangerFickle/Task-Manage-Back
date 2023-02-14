package top.belongme.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import net.lingala.zip4j.exception.ZipException;
import org.springframework.web.multipart.MultipartFile;
import top.belongme.model.pojo.task.Task;
import top.belongme.model.result.Result;
import top.belongme.model.vo.TaskDetailsQueryVo;
import top.belongme.model.vo.TaskQueryVo;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;


/**
 * @Title: TaskService
 * @ProjectName JobManage-Back
 * @Description: TODO
 * @Author DengChao
 * @Date 2023/2/1018:17
 */
public interface TaskService extends IService<Task> {
    // 提交作业
    Result commitTask(MultipartFile uploadTaskFile, String belongBatchId);

    // 取消提交作业
    Result cancelCommitTask(String taskIid);

    void getTaskFile(String taskId, HttpServletResponse response) throws IOException;

    void getBatchFile(String batchId, HttpServletResponse response) throws IOException;
}
