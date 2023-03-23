package top.belongme.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import top.belongme.exception.GlobalBusinessException;
import top.belongme.model.pojo.Course;
import top.belongme.model.pojo.user.User;
import top.belongme.model.result.Result;
import top.belongme.model.vo.*;
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
     * TODO 修改密码
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

    /**
     * TODO 修改邮箱
     *
     * @Author DengChao
     * @Date 2023/3/8 15:36
     */
    @PreAuthorize("hasAuthority('job:personal:update')")
    @PutMapping("/updateEmail")
    public Result updateEmail(@Validated EmailVo emailVo, BindingResult result) {
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
        IPage<User> pageModel = userService.getNotCommitUserList(pageParam, taskDetailsQueryVo);
        return new Result<>(200, "请求成功", pageModel);
    }

    @PreAuthorize("hasAuthority('job:user:update')")
    @GetMapping("/listPage/{page}/{limit}")
    public Result<IPage<User>> getUserList(@PathVariable Long page,
                                           @PathVariable Long limit,
                                           UserVo userVo) {
        //创建page对象
        Page<User> pageParam = new Page<>(page, limit);
        IPage<User> pageModel = userService.selectPage(pageParam, userVo);
        return new Result<>(200, "请求成功", pageModel);
    }

    @PreAuthorize("hasAuthority('job:user:insert')")
    @PostMapping("/add")
    public Result addUser(@RequestBody @Valid User user, BindingResult result) {
        if (result != null && result.hasErrors()) {
            throw new GlobalBusinessException(800, Objects.requireNonNull(result.getFieldError()).getDefaultMessage());
        }
        return userService.saveUser(user);
    }

    @PreAuthorize("hasAuthority('job:user:update')")
    @PutMapping("/update")
    public Result updateUser(@RequestBody @Valid User user, BindingResult result) {
        if (result != null && result.hasErrors()) {
            throw new GlobalBusinessException(800, Objects.requireNonNull(result.getFieldError()).getDefaultMessage());
        }
        return userService.updateUser(user);
    }

    /**
     * TODO 获取指定用户的信息
     *
     * @Author DengChao
     * @Date 2023/3/8 17:24
     */
    @PreAuthorize("hasAuthority('job:user:select')")
    @GetMapping("/getUserById/{id}")
    public Result<User> getUserById(@PathVariable("id") String userId) {
        return userService.getUserById(userId);
    }

    /**
     * TODO 删除指定用户
     *
     * @Author DengChao
     * @Date 2023/3/9 15:15
     */
    @PreAuthorize("hasAuthority('job:user:delete')")
    @DeleteMapping("/delete/{id}")
    public Result deleteById(@PathVariable("id") String userId) {
        return userService.deleteById(userId);
    }

    /**
     * TODO 切换用户的可用性
     *
     * @Author DengChao
     * @Date 2023/3/9 15:25
     */
    @PreAuthorize("hasAuthority('job:user:update')")
    @PutMapping("/switchStatus/{id}")
    public Result switchStatus(@PathVariable("id") String userId) {
        return userService.switchStatus(userId);
    }
}
