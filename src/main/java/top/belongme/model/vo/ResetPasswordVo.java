package top.belongme.model.vo;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @Title: ResetPasswordVo
 * @ProjectName Task-Manage-Back
 * @Description: TODO
 * @Author DengChao
 * @Date 2023/2/1415:53
 */
@Data
public class ResetPasswordVo {
    @NotBlank(message = "旧密码不能为空")
    private String oldPassword;
    @NotBlank(message = "新密码不能为空")
    private String newPassword;
}
