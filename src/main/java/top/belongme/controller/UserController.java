package top.belongme.controller;

import io.jsonwebtoken.Claims;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import top.belongme.model.result.Result;
import top.belongme.service.UserService;
import top.belongme.utils.JwtUtil;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @Title: UserController
 * @ProjectName JobManage-Back
 * @Description: TODO
 * @Author DengChao
 * @Date 2023/2/718:31
 */
@RestController
public class UserController {
    @Resource
    private UserService userService;

    @GetMapping("/user/info")
    public Result getUserInfo(HttpServletRequest request) throws Exception {
        String token = request.getHeader("token");
        String userId = JwtUtil.parseJWT(token).getSubject();
        return userService.getUserInfo(Long.valueOf(userId));
    }
}
