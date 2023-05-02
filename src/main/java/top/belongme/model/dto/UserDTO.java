package top.belongme.model.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.Date;

/**
 * @Title: UserDTO
 * @ProjectName Task-Manage-Back
 * @Description: TODO
 * @Author DengChao
 * @Date 2023/5/21:42
 */
@Data
public class UserDTO {
    @TableId
    private String id;
    private String name;
    private String username;
    private String studentNumber;
    private String email;
    private String roleId;
    // 状态（1：正常 0：停用）
    private Integer status;
    private Date createTime;
    private Date updateTime;
}
