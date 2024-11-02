package top.belongme.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import top.belongme.constant.Constants;
import top.belongme.exception.GlobalBusinessException;
import top.belongme.model.pojo.user.LoginUser;
import top.belongme.model.pojo.user.User;
import top.belongme.model.result.Result;
import top.belongme.service.UserService;
import top.belongme.utils.LoginUserUtil;
import top.belongme.utils.RedisCache;

import javax.annotation.Resource;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @Title: UserAspect
 * @ProjectName Task-Manage-Back
 * @Description: TODO
 * @Author DengChao
 * @Date 2023/11/1116:11
 */
@Aspect
@Component
public class UserAspect {

    @Resource
    private RedisCache redisCache;

    @Resource
    private UserService userService;

    @Resource
    private UserDetailsService userDetailsService;

    @Pointcut("@annotation(top.belongme.annotation.UserObserve)")
    public void userObserve() {
    }

    /**
     * TODO 用户信息被修改后更新redis缓存中的用户信息
     *
     * @Author DengChao
     * @Date 2023/11/11 17:27
     */
    @AfterReturning(
            value = "userObserve()",
            returning = "result")
    public void afterReturningMethod(JoinPoint joinPoint, Result result) {
        String methodName = joinPoint.getSignature().getName();
        Object obj = joinPoint.getArgs()[0];
        User user;
        String userName = "";
        String id;
        if (obj instanceof User) {
            user = (User) obj;
            user = userService.getById(user.getId());
            userName = user.getUsername();
        } else if (obj instanceof String) {
            id = (String) obj;
            user = userService.getById(id);
            userName = user.getUsername();
        }

        UserDetails userDetails = userDetailsService.loadUserByUsername(userName);
        LoginUser loginUser = (LoginUser) userDetails;
        // 更新Redis中的用户信息
        redisCache.setCacheObject("login:" + loginUser.getUser().getId(), loginUser,  30, TimeUnit.MINUTES);
        System.out.println("Logger-->返回通知，方法名：" + methodName + "，结果：" + result);
    }

    /**
     * TODO 在所有控制器执行之前检查当前用户是否被禁用
     *      禁用后禁止执行任何操作
     *
     * @Author DengChao
     * @Date 2023/11/11 17:28
     */
    @Before("execution(* top.belongme.controller.*.*(..)) && !execution(* top.belongme.controller.LoginController.*(..))")
    public void afterReturningMethod(JoinPoint joinPoint) {
        // 获取当前登陆的用户
        String userId = LoginUserUtil.getCurrentLoginUser().getUser().getId();
        // 检查用户状态
        User user = userService.getById(userId);
        if (Objects.equals(user.getStatus(), Constants.YES)) {
            throw new GlobalBusinessException(810, "您的账户已被禁用，请联系管理员");
        }
    }
}
