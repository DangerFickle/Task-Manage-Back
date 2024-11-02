package top.belongme.model.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.Data;

import java.util.Date;

/**
 * @Title: Group
 * @ProjectName Task-Manage-Back
 * @Description: TODO
 * @Author DengChao
 * @Date 2023/12/1714:48
 */
@Data
public class Group {
    private String id;
    private String leader; // 群主
    @TableField(exist = false)
    private String leaderName; // 群主名字
    private String name; // 群组名称
    private String belongCourse;
    private Date createTime;
    private Date updateTime;
    private Integer alreadyMember; // 群组已有成员数量
    @TableField(exist = false)
    private Integer groupMaxMemberSize; // 群组成员最大数量，不能超过这个数量
    @TableField(exist = false)
    private IPage<GroupMember> members; // 群组成员
}
