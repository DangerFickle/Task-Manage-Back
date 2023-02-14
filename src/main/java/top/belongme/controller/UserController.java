package top.belongme.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import top.belongme.exception.GlobalBusinessException;
import top.belongme.model.pojo.user.User;
import top.belongme.model.result.Result;
import top.belongme.model.vo.EmailVo;
import top.belongme.model.vo.ResetPasswordVo;
import top.belongme.model.vo.TaskDetailsQueryVo;
import top.belongme.service.UserService;
import top.belongme.utils.JwtUtil;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Objects;

/**
 * @Title: UserController
 * @ProjectName JobManage-Back
 * @Description: TODO
 * @Author DengChao
 * @Date 2023/2/718:31
 */
@RestController
@RequestMapping("/user")
public class UserController {
    @Resource
    private UserService userService;

    /**
     * TODO 获取用户信息
     *
     * @Author DengChao
     * @Date 2023/2/14 14:15
     */
    @PreAuthorize("hasAuthority('job:personal:select')")
    @GetMapping("/info")
    public Result getUserInfo(HttpServletRequest request) throws Exception {
        String token = request.getHeader("token");
        String userId = JwtUtil.parseJWT(token).getSubject();
        return userService.getUserInfo(Long.valueOf(userId));
    }

    /**
     * TODO 更新用户信息
     *
     * @Author DengChao
     * @Date 2023/2/14 14:33
     */
    @PreAuthorize("hasAuthority('job:personal:update')")
    @PutMapping("/resetPassword")
    public Result resetPassword(@Validated ResetPasswordVo resetPasswordVo, BindingResult result) {
        if (result != null && result.hasErrors()) {
            throw new GlobalBusinessException(800, Objects.requireNonNull(result.getFieldError()).getDefaultMessage());
        }
        return userService.resetPassword(resetPasswordVo);
    }

    @PreAuthorize("hasAuthority('job:personal:update')")
    @PutMapping("/updateEmail")
    public Result resetPassword(@Validated EmailVo emailVo, BindingResult result) {
        if (result != null && result.hasErrors()) {
            throw new GlobalBusinessException(800, Objects.requireNonNull(result.getFieldError()).getDefaultMessage());
        }
        return userService.updateEmail(emailVo);
    }

    /**
     * TODO 根据批次id获取未交用户列表
     *
     * @Author DengChao
     * @Date 2023/2/14 14:03
     */
    @PreAuthorize("hasAuthority('job:user:select')")
    @GetMapping("/noCommitUserList/{page}/{limit}")
    public Result getNoCommitUserList(@PathVariable Long page,
                                      @PathVariable Long limit,
                                      @Valid TaskDetailsQueryVo taskDetailsQueryVo,
                                      BindingResult result) {
        if (result != null && result.hasErrors()) {
            throw new GlobalBusinessException(800, Objects.requireNonNull(result.getFieldError()).getDefaultMessage());
        }
        //创建page对象
        Page<User> pageParam = new Page<>(page, limit);
        //调用service方法
        IPage<User> pageModel = userService.getNoCommitUserList(pageParam, taskDetailsQueryVo);
        return new Result<>(200, "请求成功", pageModel);
    }
}
