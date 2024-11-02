package top.belongme.filter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import top.belongme.model.pojo.user.LoginUser;
import top.belongme.model.result.Result;
import top.belongme.utils.JwtUtil;
import top.belongme.utils.RedisCache;
import top.belongme.utils.WebUtils;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

/**
 * @Title: JwtAuthenticationTokenFilter
 * @ProjectName JobManage-Back
 * @Description: TODO
 * @Author DengChao
 * @Date 2023/2/717:45
 */
@Component
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

    @Resource
    RedisCache redisCache;
    @Qualifier("redisTemplate")
    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        response.setHeader("Access-Control-Expose-Headers", "Is_Expired"); // 允许前端接收到响应头中的Is_Expired
        // 获取请求头中的token
        String token = request.getHeader("token");
        // 如果请求头中没有token，直接放行，因为没有向SecurityContext中存入认证信息，框架内部会自动让用户做认证
        if (!StringUtils.hasText(token) || "undefined".equals(token)) {
            filterChain.doFilter(request, response);
            return;
        }
        // 解析token，获取userId
        String userId;
        try {
            Claims claims = JwtUtil.parseJWT(token);
            userId = claims.getSubject();
        } catch (ExpiredJwtException e) {
            response.setHeader("Is_Expired", "expired"); // 用于前端接收类型为Blob时，判断用户登陆是否过期
            String invalidToken = JSON.toJSONString(new Result<>(809, "token已过期，请重新登陆"));
            WebUtils.renderString(response, invalidToken);
            return;
        } catch (MalformedJwtException e) {
            response.setHeader("Is_Expired", "expired"); // 用于前端接收类型为Blob时，判断用户登陆是否过期
            String invalidToken = JSON.toJSONString(new Result<>(810, "token非法"));
            WebUtils.renderString(response, invalidToken);
            return;
        } catch (Exception e) {
            e.printStackTrace();
            response.setHeader("Is_Expired", "expired"); // 用于前端接收类型为Blob时，判断用户登陆是否过期
            String invalidToken = JSON.toJSONString(new Result<>(807, "token未知错误"));
            WebUtils.renderString(response, invalidToken);
            return;
        }

        // 从redis中根据userId获取用户信息
        LoginUser loginUser = redisCache.getCacheObject("login:" + userId);
        // 如果redis中取不到用户信息，说明用户已经过期
        if (Objects.isNull(loginUser)) {
            response.setHeader("Is_Expired", "expired"); // 用于前端接收类型为Blob时，判断用户登陆是否过期
            String loginExpired = JSON.toJSONString(new Result<>(806, "缓存已过期"));
            WebUtils.renderString(response, loginExpired);
            return;
        }
        // 封装信息到Authentication，第一个参数为用户认证主体，第三个参数为用户具有的权限
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginUser, null, loginUser.getAuthorities());
        // 存入SecurityContext
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        filterChain.doFilter(request, response);
    }
}
