package top.belongme.service.impl;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import top.belongme.exception.GlobalBusinessException;
import top.belongme.mapper.UserMapper;
import top.belongme.model.pojo.user.LoginUser;
import top.belongme.model.pojo.user.User;
import top.belongme.model.result.Result;
import top.belongme.service.LoginService;
import top.belongme.utils.JwtUtil;
import top.belongme.utils.RedisCache;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @Title: LoginServiceImpl
 * @ProjectName JobManage-Back
 * @Description: TODO
 * @Author DengChao
 * @Date 2023/2/714:40
 */
@Service
public class LoginServiceImpl implements LoginService {
    @Resource
    UserMapper userMapper;
    @Resource
    private AuthenticationManager authenticationManager;
    @Resource
    private RedisCache redisCache;

    @Override
    public User findUserById(Integer id) {
        return userMapper.selectById(id);
    }

    @Override
    public Result login(User user) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);
        if (Objects.isNull(authenticate)) {
            throw new GlobalBusinessException(800, "用户名或密码错误");
        }
        //使用userid生成token
        LoginUser loginUser = (LoginUser) authenticate.getPrincipal();

        String userId = loginUser.getUser().getId().toString();
        String jwt = JwtUtil.createJWT(userId);
        //authenticate存入redis，设置过期时间为半小时
        redisCache.setCacheObject("login:" + userId, loginUser,  30, TimeUnit.MINUTES);
        //把token响应给前端
        HashMap<String, String> map = new HashMap<>();
        map.put("token", jwt);

        return new Result(200, "登陆成功", map);
    }

    /**
     * 退出登录
     * 从SecurityContextHolder中取出当前请求的用户对象
     * 根据取出的用户对象删除redis中的用户信息
     *
     * @return
     */
    @Override
    public Result logout() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        String userid = loginUser.getUser().getId();
        redisCache.deleteObject("login:" + userid);
        return new Result(200, "退出成功");
    }
}
