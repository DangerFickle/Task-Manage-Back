package top.belongme.model.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.Date;

/**
 * @Title: BatchDTO
 * @ProjectName Task-Manage-Back
 * @Description: TODO
 * @Author DengChao
 * @Date 2023/5/20:45
 */
@Data
public class BatchDTO {
    private String id;
    private String batchName; // 批次名称
    private String description; // 批次描述
    private String belongCourseId; // 所属课程id
    private String belongCourseName; // 所属课程名称
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime; // 批次截至时间
    private String creatorName; // 创建人姓名
    private String modifierName; // 更新人姓名
    private Integer isEnd; // 批次是否已截止
    private Integer isCommit; // 批次是否已提交
    private Long personCount; // 批次已交人数
    private Long totalCount; // 总人数
    private Long sizeOfDirectory; // 批次文件夹内的文件大小
    private Date createTime;
    private Date updateTime;
}
