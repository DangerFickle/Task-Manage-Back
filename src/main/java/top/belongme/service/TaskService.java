package top.belongme.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;
import top.belongme.model.pojo.task.Task;
import top.belongme.model.result.Result;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;


/**
 * @Title: TaskService
 * @ProjectName JobManage-Back
 * @Description: TODO
 * @Author DengChao
 * @Date 2023/2/1018:17
 */
public interface TaskService extends IService<Task> {
    // 检查上传的文件是否已经存在
    Result checkFileExist(String fileSha256, String fileType);
    // 提交作业
    Result commitTask(MultipartFile uploadTaskFile, String belongBatchId, String fileSha256, String taskType);

    // 取消提交作业
    Result cancelCommitTask(String taskIid);

    void getTaskFile(String taskId, HttpServletResponse response) throws IOException;

    void getTaskFileByBelongBatchId(String belongBatchId, HttpServletResponse response) throws IOException;

    void getBatchFile(String batchId, HttpServletResponse response) throws IOException;

    Result secondTransmit(String belongBatchId, String fileSha256, String fileType);

}
