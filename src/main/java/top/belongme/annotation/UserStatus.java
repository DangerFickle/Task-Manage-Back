package top.belongme.annotation;

import java.lang.annotation.*;

/**
 * @Title: UserStatus
 * @ProjectName Task-Manage-Back
 * @Description: TODO
 * @Author DengChao
 * @Date 2023/11/1117:27
 */
@Documented
@Target(ElementType.METHOD) // 说明该注解只能放在方法上面
@Retention(RetentionPolicy.RUNTIME)
public @interface UserStatus {
}
