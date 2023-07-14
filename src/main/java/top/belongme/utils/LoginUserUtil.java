package top.belongme.utils;

import org.springframework.security.core.context.SecurityContextHolder;
import top.belongme.model.pojo.user.LoginUser;

/**
 * @Title: LoginUserUtil
 * @ProjectName Task-Manage-Back
 * @Description: TODO
 * @Author DengChao
 * @Date 2023/7/1414:09
 */
public class LoginUserUtil {
    public static LoginUser getCurrentLoginUser() {
        return (LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
