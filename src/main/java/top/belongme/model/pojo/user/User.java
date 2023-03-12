package top.belongme.model.pojo.user;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Date;

/**
 * @Title: User
 * @ProjectName JobManage-Back
 * @Description: TODO
 * @Author DengChao
 * @Date 2023/2/714:40
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Valid
public class User implements Serializable {
    private static final long serialVersionUID = -54579041104113736L;
    @TableId
    private String id;
    private String username;
    private String password;
    @NotBlank(message = "学号不能为空")
    private String name;
    @NotBlank(message = "学号不能为空")
    private String studentNumber;
    @NotBlank(message = "角色不能为空")
    private String roleId;
    @TableField(exist = false)
    private String roleName;
//    @Email(message = "邮箱格式错误")
    private String email;
    private String avatar;
    private Integer status;
    private Date createTime;
    private Date updateTime;

}
