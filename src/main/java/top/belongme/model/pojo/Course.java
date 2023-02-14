package top.belongme.model.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.Date;

/**
 * @Title: Course
 * @ProjectName JobManage-Back
 * @Description: TODO
 * @Author DengChao
 * @Date 2023/2/814:09
 */
@Data
public class Course {
    @TableId
    private String id;
    @NotBlank(message = "课程名不能为空")
    private String courseName;
    @TableField(exist = false)
    private String modifierName;
    private String modifierId; // 修改者id
    @TableField(exist = false)
    private String creatorName; // 创建者姓名
    private String folderPath;
    private String creatorId;
    @NotBlank(message = "课程描述不能为空")
    private String description;
    private Integer status;
    private Date createTime;
    private Date updateTime;
    private Integer isDeleted;
}
