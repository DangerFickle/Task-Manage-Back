package top.belongme.model.vo;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.util.Date;

/**
 * @Title: UserDTO
 * @ProjectName Task-Manage-Back
 * @Description: TODO
 * @Author DengChao
 * @Date 2023/5/21:42
 */
@Data
public class UserVO {
    @TableId
    private String id;
    private String name;
    private String username;
    private String studentNumber;
//    private String email;
    private Boolean hasEmail;
    private String roleId;
    // 状态（1：正常 0：停用）
    private Integer status;
    private Date createTime;
    private Date updateTime;
}
