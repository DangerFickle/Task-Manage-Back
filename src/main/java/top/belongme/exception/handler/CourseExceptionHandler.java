package top.belongme.exception.handler;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import top.belongme.exception.GlobalBusinessException;
import top.belongme.model.result.Result;

/**
 * @Title: ExceptionHandler
 * @ProjectName JobManage-Back
 * @Description: TODO 全局异常处理，controller层抛出异常后，会被这里捕获并处理
 * @Author DengChao
 * @Date 2023/2/911:08
 */
@RestController
@ControllerAdvice
public class CourseExceptionHandler {
    @ExceptionHandler(GlobalBusinessException.class)
    public Result exceptionHandler(GlobalBusinessException e) {
        return new Result(e.getCode(), e.getMsg());
    }

}
