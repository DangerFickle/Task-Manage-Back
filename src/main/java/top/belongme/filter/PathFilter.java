package top.belongme.filter;

import com.alibaba.fastjson.JSONObject;
import top.belongme.model.result.Result;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @Title: PathFilter
 * @ProjectName Task-Manage-Back
 * @Description: TODO
 * @Author DengChao
 * @Date 2023/3/2512:56
 */
@WebFilter(urlPatterns = "/*")
public class PathFilter extends HttpFilter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        String requestURI = httpServletRequest.getRequestURI();
        if ("/actuator/shutdown".equals(requestURI)) {
            Result result = new Result(403, "非法访问！");
            response.setContentType("application/json;charset=utf-8");
            String jsonString = JSONObject.toJSONString(result);
            response.getWriter().write(jsonString);
            return;
        }
        chain.doFilter(request, response);
    }
}
