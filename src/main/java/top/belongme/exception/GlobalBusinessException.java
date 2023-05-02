package top.belongme.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Title: CourseException
 * @ProjectName JobManage-Back
 * @Description: TODO
 * @Author DengChao
 * @Date 2023/2/911:03
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class GlobalBusinessException extends RuntimeException {
    private Integer code;
    private String msg;

    public GlobalBusinessException(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
