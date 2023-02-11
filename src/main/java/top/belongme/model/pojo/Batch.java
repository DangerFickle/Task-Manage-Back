package top.belongme.model.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

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
    private String id;
    private String batchName; // 批次名称
    private String description; // 批次描述
//    @JsonFormat(locale = "zh")
    private Date endTime; // 批次截至时间
    private String folderPath; // 批次文件夹路径
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
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", locale = "zh")
    private Date createTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", locale = "zh")
    private Date updateTime;

}
