package top.belongme.model.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
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
public class Course {
    private String id;
    private String courseName;
    @TableField(exist = false)
    private String modifierName;
    private String modifierId; // 修改者id
    @TableField(exist = false)
    private String creatorName; // 创建者姓名
    private String folderPath;
    private String creatorId;
    private String description;
    private Integer status;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", locale = "zh")
    private Date createTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", locale = "zh")
    private Date updateTime;
    private Integer isDeleted;
}
