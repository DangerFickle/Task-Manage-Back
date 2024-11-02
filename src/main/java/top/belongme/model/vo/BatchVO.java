package top.belongme.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * @Title: BatchDTO
 * @ProjectName Task-Manage-Back
 * @Description: TODO
 * @Author DengChao
 * @Date 2023/5/20:45
 */
@Data
public class BatchVO {
    private String id;
    private String batchName; // 批次名称
    private String batchType; // 批次类型
    private String description; // 批次描述
    private String belongCourseId; // 所属课程id
    private String belongCourseName; // 所属课程名称
    private Date endTime; // 批次截至时间
    private Integer isEnd; // 批次是否已截止
    private Integer isCommit; // 批次是否已提交
    private Long alreadyCount; // 批次已交人数
    private Long totalCount; // 总人数
    private Long sizeOfDirectory; // 批次文件夹内的文件大小
    private Date createTime;
    private Date updateTime;
}
