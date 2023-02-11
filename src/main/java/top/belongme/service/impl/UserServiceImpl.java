package top.belongme.service.impl;

import org.springframework.stereotype.Service;
import top.belongme.mapper.MenuMapper;
import top.belongme.mapper.RoleMapper;
import top.belongme.mapper.UserMapper;
import top.belongme.model.pojo.Menu;
import top.belongme.model.pojo.Role;
import top.belongme.model.result.Result;
import top.belongme.model.pojo.user.User;
import top.belongme.service.UserService;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @Title: UserServiceImpl
 * @ProjectName JobManage-Back
 * @Description: TODO
 * @Author DengChao
 * @Date 2023/2/718:32
 */
@Service
public class UserServiceImpl implements UserService {
    @Resource
    private UserMapper userMapper;
    @Resource
    private RoleMapper roleMapper;

    @Override
    public Result<User> getUserInfo(Long userId) {
        User user = userMapper.selectById(userId); // 根据用户id查询用户信息
        Role role = roleMapper.selectById(user.getRoleId()); // 根据用户的角色id查询角色信息
        user.setRoleName(role.getRoleCode()); // 将角色名称设置到用户对象中
        user.setPassword(null); // 将密码设置为null
        return new Result<>(200,"请求成功", user);
    }
}
