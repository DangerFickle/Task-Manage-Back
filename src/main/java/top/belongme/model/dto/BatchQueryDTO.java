package top.belongme.model.dto;

import lombok.Data;

/**
 * @Title: BatchQueryVo
 * @ProjectName JobManage-Back
 * @Description: TODO
 * @Author DengChao
 * @Date 2023/2/916:40
 */
@Data
public class BatchQueryDTO {
    private String belongCourseId;
    private String batchName;
    private String batchType;
    private String description;
    private String creatorName;
    private String modifierName;
    private Integer isEnd;
    private Integer isCommit;
}
