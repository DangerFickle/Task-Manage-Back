package top.belongme.model.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @Title: CourseQueryVo
 * @ProjectName JobManage-Back
 * @Description: TODO
 * @Author DengChao
 * @Date 2023/2/814:07
 */
@Data
public class CourseQueryDTO implements Serializable {
    private static final long serialVersionUID = 459348171343L;
    private String creatorName;
    private String courseName;
    private String description;
    private String modifierName;
}
