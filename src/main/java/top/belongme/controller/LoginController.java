package top.belongme.controller;

import org.springframework.web.bind.annotation.*;
import top.belongme.model.result.Result;
import top.belongme.model.pojo.user.User;
import top.belongme.service.LoginService;

import javax.annotation.Resource;

/**
 * @Title: LoginController
 * @ProjectName JobManage-Back
 * @Description: TODO
 * @Author DengChao
 * @Date 2023/2/713:48
 */
@RestController
@RequestMapping("user")
public class LoginController {

    @Resource
    private LoginService loginService;

    @PostMapping("login")
    public Result login(@RequestBody User user){
        return loginService.login(user);
    }

    @GetMapping("logout")
    public Result logout(){
        return loginService.logout();
    }
}
