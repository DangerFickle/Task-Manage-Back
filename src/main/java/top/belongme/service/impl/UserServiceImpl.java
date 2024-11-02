package top.belongme.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import top.belongme.exception.GlobalBusinessException;
import top.belongme.mapper.*;
import top.belongme.model.pojo.Batch;
import top.belongme.model.pojo.Course;
import top.belongme.model.pojo.Role;
import top.belongme.model.pojo.task.Task;
import top.belongme.model.pojo.user.LoginUser;
import top.belongme.model.pojo.user.User;
import top.belongme.model.result.Result;
import top.belongme.model.dto.EmailDTO;
import top.belongme.model.dto.ResetPasswordDTO;
import top.belongme.model.dto.TaskDetailsQueryDTO;
import top.belongme.model.dto.UserDTO;
import top.belongme.service.CourseService;
import top.belongme.service.UserService;
import top.belongme.utils.LoginUserUtil;
import top.belongme.utils.RedisCache;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;
import java.util.Objects;

/**
 * @Title: UserServiceImpl
 * @ProjectName JobManage-Back
 * @Description: TODO
 * @Author DengChao
 * @Date 2023/2/718:32
 */
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    @Resource
    private RoleMapper roleMapper;
    @Resource
    private BatchMapper batchMapper;

    @Resource
    private TaskMapper taskMapper;

    @Resource
    RedisCache redisCache;
    @Resource
    private PasswordEncoder passwordEncoder;

    @Resource
    private UserMapper userMapper;

    @Resource
    private CourseService courseService;

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
    public IPage<User> getNotCommitUserList(Page<User> pageParam, TaskDetailsQueryDTO taskDetailsQueryDTO) {
        // 检查批次是否已被删除
        Batch batch = batchMapper.selectById(taskDetailsQueryDTO.getBelongBatchId());
        if (Objects.isNull(batch)) {
            throw new GlobalBusinessException(800, "该批次不存在");
        }
        log.info("获取未交人员列表成功");
        IPage<User> userIPage = baseMapper.getNotCommitUserList(pageParam, taskDetailsQueryDTO);
        userIPage.getRecords().forEach(user -> user.setHasEmail(Objects.nonNull(user.getEmail())));
        return userIPage;
    }

    /**
     * TODO 更新用户信息
     *
     * @Author DengChao
     * @Date 2023/2/14 14:36
     */
    @Override
    public Result resetPassword(ResetPasswordDTO resetPasswordDTO) {
        // 获取当前登陆的用户
        LoginUser loginUser = LoginUserUtil.getCurrentLoginUser();
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
        if (!passwordEncoder.matches(resetPasswordDTO.getOldPassword(), originPassword)) {
            throw new GlobalBusinessException(800, "旧密码错误");
        }
        // 加密新密码
        String newPassword = passwordEncoder.encode(resetPasswordDTO.getNewPassword());

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
            log.info("用户【{}】修改了密码", loginUser.getUser().getUsername());
            return new Result(200, "密码修改成功");
        } else {
            return new Result(800, "密码修改失败");
        }
    }

    @Override
    public Result updateEmail(EmailDTO emailDTO) {
        // 获取当前登陆的用户
        LoginUser loginUser = LoginUserUtil.getCurrentLoginUser();
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
        if (!passwordEncoder.matches(emailDTO.getPassword(), originPassword)) {
            throw new GlobalBusinessException(800, "密码错误，修改失败");
        }
        User user = new User();
        user.setId(userId);
        user.setEmail(emailDTO.getEmail());
        // 更新用户信息
        int update = baseMapper.updateById(user);
        if (update > 0) {
            log.info("用户【{}】修改了邮箱，新邮箱为【{}】", loginUser.getUser().getName(), user.getEmail());
            return new Result(200, "邮箱修改成功");
        } else {
            return new Result(800, "邮箱修改失败");
        }
    }

    @Override
    public IPage<User> selectPage(Page<User> pageParam, UserDTO userDTO) {
        QueryWrapper<User> qw = new QueryWrapper<>();
        qw
                .like("name", userDTO.getName())
                .like("student_number", userDTO.getStudentNumber())
                .and(i -> i.ne("role_id", "1"));
        return baseMapper.selectPage(pageParam, qw);
    }

    @Override
    public Result saveUser(User user) {
        // 检查用户是否已经存在
        QueryWrapper<User> qw = new QueryWrapper<User>()
                .eq("username", user.getStudentNumber())
                .or()
                .eq("student_number", user.getStudentNumber());
        User oldUser = baseMapper.selectOne(qw);
        if (Objects.nonNull(oldUser)) {
            throw new GlobalBusinessException(800, "该用户已存在");
        }
        user.setUsername(user.getStudentNumber());
        if (StringUtils.hasText(user.getPassword())) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        } else {
            user.setPassword(passwordEncoder.encode(user.getStudentNumber()));
        }
        int insert = baseMapper.insert(user);
        if (insert <= 0) {
            throw new GlobalBusinessException(800, "用户添加失败");
        }
        this.calculateCourseMaxGroupSize();
        LoginUser loginUser = LoginUserUtil.getCurrentLoginUser();
        log.info("管理员【{}】，添加了用户【{}】", loginUser.getUser().getName(), user.getName());
        return new Result(200, "用户添加成功");
    }

    @Override
    public Result updateUser(User user) {
        // 查询用户是否存在
        User oldUser = baseMapper.selectById(user.getId());
        if (Objects.isNull(oldUser)) {
            throw new GlobalBusinessException(800, "该用户不存在");
        }

        if (!Objects.equals(oldUser.getStudentNumber(), user.getStudentNumber())) {
            user.setUsername(user.getStudentNumber());
        } else {
            user.setStudentNumber(null);
        }

        if (StringUtils.hasText(user.getPassword())) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        } else {
            user.setPassword(oldUser.getPassword());
        }

        int update = baseMapper.updateById(user);
        if (update <= 0) {
            throw new GlobalBusinessException(800, "用户修改失败");
        }
        LoginUser loginUser = LoginUserUtil.getCurrentLoginUser();
        log.info("管理员【{}】，更新了用户【{}】的信息", loginUser.getUser().getName(), oldUser.getName());
        return new Result(200, "用户修改成功");
    }

    @Override
    public Result<User> getUserById(String userId) {
        User user = baseMapper.selectById(userId);
        return new Result<>(200, "请求成功", user);
    }

    @Override
    public Result deleteById(String userId) {
        // 查询用户是否存在
        User user = baseMapper.selectById(userId);
        if (Objects.isNull(user)) {
            throw new GlobalBusinessException(800, "该用户不存在");
        }
        // 检查是否是系统管理员
        if ("1".equals(user.getRoleId())) {
            throw new GlobalBusinessException(800, "无法删除系统管理员");
        }
        // 查询用户是否上传过作业
        QueryWrapper<Task> qw = new QueryWrapper<Task>()
                .eq("uploader_id", userId);
        Long tasks = taskMapper.selectCount(qw);
        if (tasks > 0) {
            throw new GlobalBusinessException(800, "该用户存在未删除的作业，无法删除");
        }
        // 删除用户
        int delete = baseMapper.deleteById(userId);
        if (delete <= 0) {
            throw new GlobalBusinessException(800, "用户删除失败");
        }
        this.calculateCourseMaxGroupSize();
        LoginUser loginUser = LoginUserUtil.getCurrentLoginUser();
        log.info("管理员【{}】删除了用户【{}】", loginUser.getUser().getName(), user.getName());
        return new Result(200, "用户删除成功");
    }

    @Override
    public Result switchStatus(String userId) {
        // 查询用户是否存在
        User user = baseMapper.selectById(userId);
        if (Objects.isNull(user)) {
            throw new GlobalBusinessException(800, "该用户不存在");
        }
        // 检查是否是系统管理员
        if (user.getRoleId().equals("1")) {
            throw new GlobalBusinessException(800, "无法禁用系统管理员");
        }
        user.setStatus(user.getStatus() == 1 ? 0 : 1);
        // 更新用户信息
        int update = baseMapper.updateById(user);
        if (update <= 0) {
            return new Result(800, "用户状态更新失败");
        }
        log.info("用户【{}】可用状态更新为【{}】成功", user.getName(), user.getStatus() == 1 ? "可用" : "禁用");
        return new Result(200, "用户状态更新成功");
    }

    /**
     * TODO 获取人员列表，除了自己群组的人员
     *
     * @Author DengChao
     * @Date 2024/10/27 14:18
     */
    @Override
    public IPage<User> getUserWithoutGroupMember(Page<User> pageParam, UserDTO userDTO) {
        return userMapper.getUserWithoutGroupMember(pageParam, userDTO);
    }

    @Override
    public Result saveUserBatch(List<User> userList) {
        if(userList.size() == 0) {
            throw new GlobalBusinessException(800, "文件为空");
        }
        List<String> userNameList = userList.stream().map(u -> u.getStudentNumber()).toList();

        long count = this.count(new QueryWrapper<User>().in("username", userNameList));
        if (count > 0) {
            throw new GlobalBusinessException(800, "文件中存在与现有用户重复的数据");
        }

        userList.forEach(user -> {
            user.setUsername(user.getStudentNumber());
            user.setPassword(passwordEncoder.encode(user.getStudentNumber()));
            user.setRoleId("3");
            user.setStatus(0);
        });
        boolean savedBatch = this.saveBatch(userList);
        if(!savedBatch) {
            throw new GlobalBusinessException(800, "添加失败");
        }
        this.calculateCourseMaxGroupSize();
        return Result.ok();
    }

    private boolean calculateCourseMaxGroupSize() {
        // 重新计算所有课程的群组最大数量
        BigDecimal userCount = BigDecimal.valueOf(this.count(new QueryWrapper<User>().ne("id", 1)));
        List<Course> courseList = courseService.list();
        courseList.forEach(course -> {
            // 计算该课程下群组的最大群组数量
            BigDecimal groupMaxMemberSize = BigDecimal.valueOf(course.getGroupMaxMemberSize());
            BigDecimal groupMaxSize = userCount.divide(groupMaxMemberSize,0, RoundingMode.CEILING);
            course.setGroupMaxSize(groupMaxSize.intValue());
        });
        return courseService.updateBatchById(courseList);
    }
}
