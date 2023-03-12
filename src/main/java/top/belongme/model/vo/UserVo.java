package top.belongme.model.vo;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

/**
 * @Title: UserVo
 * @ProjectName Task-Manage-Back
 * @Description: TODO
 * @Author DengChao
 * @Date 2023/3/815:40
 */
@Data
public class UserVo {
    private String name;
    private String studentNumber;
}
