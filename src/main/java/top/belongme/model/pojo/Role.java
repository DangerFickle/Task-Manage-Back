package top.belongme.model.pojo;

import lombok.Data;

import java.util.Date;

/**
 * @Title: Role
 * @ProjectName JobManage-Back
 * @Description: TODO
 * @Author DengChao
 * @Date 2023/2/910:38
 */
@Data
public class Role {
    private String id;
    private String roleName;
    private String roleCode;
    private String description;
    private Date createTime;
    private Date updateTime;
    private Integer isDeleted;
}
