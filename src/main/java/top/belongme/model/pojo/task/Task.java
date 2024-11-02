package top.belongme.model.pojo.task;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
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
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Task {
    @TableId
    private String id;
    @TableField(exist = false)
    private String fileName;
    private String fileType;
    private String fileSha256;
    private String uploaderId;
    @TableField(exist = false)
    private User uploaderName;
    private String belongBatchId;
    @TableField(exist = false)
    private Batch belongBatch;
    private Date createTime;
    private Date updateTime;
}
