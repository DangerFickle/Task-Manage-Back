package top.belongme.model.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @Title: RemindVo
 * @ProjectName Task-Manage-Back
 * @Description: TODO
 * @Author DengChao
 * @Date 2023/2/2016:37
 */
@Data
public class RemindDTO {
    @NotBlank(message = "用户id不能为空")
    private String userId;

    @NotBlank(message = "所属批次id不能为空")
    private String batchId;
}
