package top.belongme.model.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.Date;

/**
 * @Title: Batch
 * @ProjectName JobManage-Back
 * @Description: TODO
 * @Author DengChao
 * @Date 2023/2/915:29
 */
@Data
public class Batch {
    @TableId
    private String id;
    @NotBlank(message = "批次名称不能为空")
    private String batchName; // 批次名称
    @NotBlank(message = "批次描述不能为空")
    private String description; // 批次描述
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime; // 批次截至时间
    private String folderPath; // 批次文件夹路径
    @NotBlank(message = "所属课程不能为空")
    private String belongCourseId; // 所属课程id
    @TableField(exist = false)
    private String belongCourseName; // 所属课程名称
    private String creatorId; // 创建人id
    @TableField(exist = false)
    private String creatorName; // 创建人姓名
    private String modifierId; // 更新人id
    @TableField(exist = false)
    private String modifierName; // 更新人姓名
    @TableField(exist = false)
    private Integer isEnd; // 批次是否已截止
    @TableField(exist = false)
    private Integer isCommit; // 批次是否已提交
    @TableField(exist = false)
    private Long personCount; // 批次已交人数
    @TableField(exist = false)
    private Long totalCount; // 总人数
    private Date createTime;
    private Date updateTime;

}
