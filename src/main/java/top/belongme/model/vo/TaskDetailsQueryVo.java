package top.belongme.model.vo;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @Title: TaskQueryVo
 * @ProjectName JobManage-Back
 * @Description: TODO
 * @Author DengChao
 * @Date 2023/2/1021:00
 */
@Data
public class TaskDetailsQueryVo {
    // 学生姓名
    private String studentName;
    // 作业所属批次id
    @NotBlank(message = "所属批次id不能为空")
    private String belongBatchId;
    @NotBlank(message = "所属课程id不能为空")
    private String belongCourseId;
}
