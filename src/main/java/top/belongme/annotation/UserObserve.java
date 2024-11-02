package top.belongme.annotation;

import java.lang.annotation.*;

/**
 * @Title: UserAspect
 * @ProjectName Task-Manage-Back
 * @Description: TODO
 * @Author DengChao
 * @Date 2023/11/1116:10
 */
@Documented
@Target(ElementType.METHOD) // 说明该注解只能放在方法上面
@Retention(RetentionPolicy.RUNTIME)
public @interface UserObserve {
}
