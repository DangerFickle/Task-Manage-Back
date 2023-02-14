package top.belongme.model.vo;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @Title: EmailVo
 * @ProjectName Task-Manage-Back
 * @Description: TODO
 * @Author DengChao
 * @Date 2023/2/1417:11
 */
@Data
public class EmailVo {
    @NotBlank(message = "密码不能为空")
    private String password;
    @NotBlank(message = "邮箱不能为空")
    private String email;
}
