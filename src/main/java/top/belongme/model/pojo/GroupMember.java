package top.belongme.model.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.util.Date;

/**
 * @Title: GroupUser
 * @ProjectName Task-Manage-Back
 * @Description: TODO
 * @Author DengChao
 * @Date 2023/12/1715:29
 */
@Data
public class GroupMember {
    private String id;
    private String name;
    private String studentNumber;
    private String email;
    private Date create_time;
}
