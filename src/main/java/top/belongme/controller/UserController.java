package top.belongme.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import top.belongme.annotation.UserObserve;
import top.belongme.beanconverter.UserConverter;
import top.belongme.exception.GlobalBusinessException;
import top.belongme.model.vo.UserVO;
import top.belongme.model.pojo.user.User;
import top.belongme.model.result.Result;
import top.belongme.model.dto.*;
import top.belongme.service.GroupService;
import top.belongme.service.UserService;
import top.belongme.utils.JwtUtil;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;
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

    @Resource
    private UserConverter userConverter;

    @Resource
    private GroupService groupService;



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
    public Result resetPassword(@Validated ResetPasswordDTO resetPasswordDTO, BindingResult result) {
        if (result != null && result.hasErrors()) {
            throw new GlobalBusinessException(800, Objects.requireNonNull(result.getFieldError()).getDefaultMessage());
        }
        return userService.resetPassword(resetPasswordDTO);
    }

    /**
     * TODO 修改邮箱
     *
     * @Author DengChao
     * @Date 2023/3/8 15:36
     */
    @PreAuthorize("hasAuthority('job:personal:update')")
    @PutMapping("/updateEmail")
    public Result updateEmail(@Validated EmailDTO emailDTO, BindingResult result) {
        if (result != null && result.hasErrors()) {
            throw new GlobalBusinessException(800, Objects.requireNonNull(result.getFieldError()).getDefaultMessage());
        }
        return userService.updateEmail(emailDTO);
    }

    /**
     * TODO 根据批次id获取未交用户列表
     *
     * @Author DengChao
     * @Date 2023/2/14 14:03
     */
    @PreAuthorize("hasAuthority('job:user:select')")
    @GetMapping("/noCommitUserList/{page}/{limit}")
    public Result<IPage<UserVO>> getNoCommitUserList(@PathVariable Long page,
                                                     @PathVariable Long limit,
                                                     @Valid TaskDetailsQueryDTO taskDetailsQueryDTO,
                                                     BindingResult result) {
        if (result != null && result.hasErrors()) {
            throw new GlobalBusinessException(800, Objects.requireNonNull(result.getFieldError()).getDefaultMessage());
        }
        //创建page对象
        Page<User> pageParam = new Page<>(page, limit);
        //调用service方法
        IPage<User> pageModel = userService.getNotCommitUserList(pageParam, taskDetailsQueryDTO);

        IPage<UserVO> userDTOIPage = userConverter.convertPage(pageModel);
        return new Result<>(200, "请求成功", userDTOIPage);
    }

    /**
     * TODO 根据获取用户列表
     *
     * @Author DengChao
     * @Date 2023/11/11 16:16
     */
    @PreAuthorize("hasAuthority('job:user:select')")
    @GetMapping("/listPage/{page}/{limit}")
    public Result<IPage<UserVO>> getUserList(@PathVariable Long page,
                                             @PathVariable Long limit,
                                             UserDTO userDTO) {
        //创建page对象
        Page<User> pageParam = new Page<>(page, limit);
        IPage<User> pageModel = userService.selectPage(pageParam, userDTO);
        IPage<UserVO> userDTOIPage = userConverter.convertPage(pageModel);
        return new Result<>(200, "请求成功", userDTOIPage);
    }

    /**
     * TODO 获取用户列表，排除指定群组已有的用户
     *
     * @Author DengChao
     * @Date 2024/10/22 21:48
     */
    @PreAuthorize("hasAuthority('job:user:select')")
    @GetMapping("/listGroupPage/{page}/{limit}")
    public Result<IPage<UserVO>> getUserListWithoutGroup(@PathVariable Long page,
                                                         @PathVariable Long limit,
                                                         UserDTO userDTO) {
        //创建page对象
        Page<User> pageParam = new Page<>(page, limit);
        IPage<User> pageModel = userService.getUserWithoutGroupMember(pageParam, userDTO);
        IPage<UserVO> userDTOIPage = userConverter.convertPage(pageModel);
        return new Result<>(200, "请求成功", userDTOIPage);
    }

    /**
     * TODO 添加用户
     *
     * @Author DengChao
     * @Date 2023/11/11 16:17
     */
    @PreAuthorize("hasAuthority('job:user:insert')")
    @PostMapping("/add")
    public Result addUser(@RequestBody @Valid User user, BindingResult result) {
        if (result != null && result.hasErrors()) {
            throw new GlobalBusinessException(800, Objects.requireNonNull(result.getFieldError()).getDefaultMessage());
        }
        return userService.saveUser(user);
    }

    /**
     * TODO 批量添加用户
     *
     * @Author DengChao
     * @Date 2024/10/27 17:59
     */
    @PreAuthorize("hasAuthority('job:user:insert')")
    @PostMapping("/addBatch")
    public Result addUserBatch(@RequestBody @Valid List<User> userList, BindingResult result) {
        if (result != null && result.hasErrors()) {
            throw new GlobalBusinessException(800, Objects.requireNonNull(result.getFieldError()).getDefaultMessage());
        }
        return userService.saveUserBatch(userList);
    }


    /**
     * TODO 更新用户
     *
     * @Author DengChao
     * @Date 2023/11/11 16:17
     */
    @UserObserve
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
    public Result<UserVO> getUserById(@PathVariable("id") String userId) {
        Result<User> result = userService.getUserById(userId);
        UserVO userVO = userConverter.convertPage(result.getData());
        return Result.ok(userVO);
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
    @UserObserve
    @PreAuthorize("hasAuthority('job:user:update')")
    @PutMapping("/switchStatus/{id}")
    public Result switchStatus(@PathVariable("id") String userId) {
        return userService.switchStatus(userId);
    }
}
