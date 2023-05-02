package top.belongme.model.dto;

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
public class CourseDTO {
    private String id;
    private String courseName;
    private String description;
    private String creatorName;
    private String modifierName;
    private Integer status;
    private Date createTime;
    private Date updateTime;
}
