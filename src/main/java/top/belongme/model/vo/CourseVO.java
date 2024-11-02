package top.belongme.model.vo;

import lombok.Data;

import java.util.Date;

/**
 * @Title: Course
 * @ProjectName JobManage-Back
 * @Description: TODO
 * @Author DengChao
 * @Date 2023/2/814:09
 */
@Data
public class CourseVO {
    private String id;
    private String courseName;
    private String description;
    private String creatorName;
    private String modifierName;
    private Integer groupMaxMemberSize; // 课程下群组的最大人数
    private Integer groupMaxSize; // 课程下群组的最大数量，创建课程时通过groupMaxMemberSize和全班人数属性计算
    private Integer status;
    private Date createTime;
    private Date updateTime;
}
