package top.belongme.model.pojo.task;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import top.belongme.model.pojo.Batch;
import top.belongme.model.pojo.user.User;

import java.util.Date;

/**
 * @Title: Task
 * @ProjectName JobManage-Back
 * @Description: TODO
 * @Author DengChao
 * @Date 2023/2/1018:19
 */
@Data
public class Task {
    private String id;
    private String fileName;
    private String filePath;
    private String uploaderId;
    @TableField(exist = false)
    private User uploaderName;
    private String belongBatchId;
    @TableField(exist = false)
    private Batch belongBatch;
    private Date createTime;
    private Date updateTime;
}
