package top.belongme.model.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @Title: UserGroup
 * @ProjectName Task-Manage-Back
 * @Description: TODO
 * @Author DengChao
 * @Date 2023/12/1718:06
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserGroup {
    private String id;
    private String userId;
    private String groupId;
    private Date createTime;

    public UserGroup(String userId, String groupId) {
        this.userId = userId;
        this.groupId = groupId;
    }
}
