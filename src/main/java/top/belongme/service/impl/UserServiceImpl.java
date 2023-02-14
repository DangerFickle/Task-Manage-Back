package top.belongme.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import top.belongme.exception.GlobalBusinessException;
import top.belongme.mapper.BatchMapper;
import top.belongme.mapper.RoleMapper;
import top.belongme.mapper.UserMapper;
import top.belongme.model.pojo.Batch;
import top.belongme.model.pojo.Role;
import top.belongme.model.pojo.user.LoginUser;
import top.belongme.model.pojo.user.User;
import top.belongme.model.result.Result;
import top.belongme.model.vo.EmailVo;
import top.belongme.model.vo.ResetPasswordVo;
import top.belongme.model.vo.TaskDetailsQueryVo;
import top.belongme.service.UserService;
import top.belongme.utils.RedisCache;

import javax.annotation.Resource;
import java.util.Objects;

/**
 * @Title: UserServiceImpl
 * @ProjectName JobManage-Back
 * @Description: TODO
 * @Author DengChao
 * @Date 2023/2/718:32
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    @Resource
    private RoleMapper roleMapper;
    @Resource
    private BatchMapper batchMapper;
    @Resource
    RedisCache redisCache;
    @Resource
    private PasswordEncoder passwordEncoder;

    @Override
    public Result<User> getUserInfo(Long userId) {
        User user = baseMapper.selectById(userId); // 根据用户id查询用户信息
        Role role = roleMapper.selectById(user.getRoleId()); // 根据用户的角色id查询角色信息
        user.setRoleName(role.getRoleCode()); // 将角色名称设置到用户对象中
        user.setPassword(null); // 将密码设置为null
        return new Result<>(200, "请求成功", user);
    }

    // 这里使用的还是作业详情的vo类TaskDetailsQueryVo，因为查询未交人员的查询条件是一样的
    @Override
    public IPage<User> getNoCommitUserList(Page<User> pageParam, TaskDetailsQueryVo taskDetailsQueryVo) {
        // 检查批次是否已被删除
        Batch batch = batchMapper.selectById(taskDetailsQueryVo.getBelongBatchId());
        if (Objects.isNull(batch)) {
            throw new GlobalBusinessException(800, "该批次不存在");
        }

        return baseMapper.getNoCommitUserList(pageParam, taskDetailsQueryVo);
    }

    /**
     * TODO 更新用户信息
     *
     * @Author DengChao
     * @Date 2023/2/14 14:36
     */
    @Override
    public Result resetPassword(ResetPasswordVo resetPasswordVo) {
        // 获取当前登陆的用户
        LoginUser loginUser = (LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        // 获取当前登陆用户的id
        String userId = loginUser.getUser().getId();
        // 查询该用户的信息
        User originUser = baseMapper.selectById(userId);
        if (Objects.isNull(originUser)) {
            throw new GlobalBusinessException(800, "该用户不存在");
        }
        // 获取用户的原始密码
        String originPassword = originUser.getPassword();
        // 比较传入的旧密码是否正确
        if (!passwordEncoder.matches(resetPasswordVo.getOldPassword(), originPassword)) {
            throw new GlobalBusinessException(800, "旧密码错误");
        }
        // 加密新密码
        String newPassword = passwordEncoder.encode(resetPasswordVo.getNewPassword());

        User user = new User();
        user.setId(userId);
        user.setPassword(newPassword);
        // 更新用户信息
        int update = baseMapper.updateById(user);
        if (update > 0) {
            // 使用户缓存失效
            boolean deleteCache = redisCache.deleteObject("login:" + userId);
            if (!deleteCache) {
                throw new GlobalBusinessException(800, "用户缓存清理失败，请重新登陆");
            }
            return new Result(200, "密码修改成功");
        } else {
            return new Result(800, "密码修改失败");
        }
    }

    @Override
    public Result updateEmail(EmailVo emailVo) {
        // 获取当前登陆的用户
        LoginUser loginUser = (LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        // 获取当前登陆用户的id
        String userId = loginUser.getUser().getId();
        // 查询该用户的信息
        User originUser = baseMapper.selectById(userId);
        if (Objects.isNull(originUser)) {
            throw new GlobalBusinessException(800, "该用户不存在");
        }
        // 获取用户的原始密码
        String originPassword = originUser.getPassword();
        // 比较传入的密码是否正确
        if (!passwordEncoder.matches(emailVo.getPassword(), originPassword)) {
            throw new GlobalBusinessException(800, "密码错误，修改失败");
        }
        User user = new User();
        user.setId(userId);
        user.setEmail(emailVo.getEmail());
        // 更新用户信息
        int update = baseMapper.updateById(user);
        if (update > 0) {
            return new Result(200, "邮箱修改成功");
        } else {
            return new Result(800, "邮箱修改失败");
        }
    }
}
