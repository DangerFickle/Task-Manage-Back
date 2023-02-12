package top.belongme.model.pojo.user;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import top.belongme.model.pojo.Menu;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

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
    private String name;
    private String studentNumber;
//    @TableField(exist = false)
//    private List<Menu> menuList;
    private Integer roleId;
    @TableField(exist = false)
    private String roleName;
    @Email(message = "邮箱格式错误")
    private String email;
    private String avatar;
    private Integer status;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", locale = "zh")
    private Date createTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", locale = "zh")
    private Date updateTime;
    @TableLogic
    private Integer isDeleted;
}
