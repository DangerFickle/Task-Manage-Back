package top.belongme.handler;

import com.alibaba.fastjson.JSON;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import top.belongme.utils.WebUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import top.belongme.model.result.Result;

@Component
public class AuthenticationEntryPointImpl implements AuthenticationEntryPoint {
    // 认证失败处理类，用于返回认证失败的信息给前端
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) {
        // 如果请求路径不是/user/login，还能来到这个方法执行
        // 说明是请求头不带token的其他接口，导致认证失败，返回错误信息，提示用户登陆
        if (!"/user/login".equals(request.getServletPath())) {
            Result result = new Result(808, "请先登陆");
            String json = JSON.toJSONString(result);
            WebUtils.renderString(response, json);
            return;
        }
        Result result = new Result(HttpStatus.UNAUTHORIZED.value(), "账号或密码错误，请重新登录");
        String json = JSON.toJSONString(result);
        WebUtils.renderString(response, json);
    }
}
