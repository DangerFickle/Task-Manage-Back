package top.belongme.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import top.belongme.exception.GlobalBusinessException;
import top.belongme.mapper.MenuMapper;
import top.belongme.mapper.UserMapper;
import top.belongme.model.pojo.Menu;
import top.belongme.model.pojo.user.LoginUser;
import top.belongme.model.pojo.user.User;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Resource
    private UserMapper userMapper;
    @Resource
    private MenuMapper menuMapper;

    /**
     * TODO 根据用户名查询用户信息
     *
     * @Author DengChao
     * @Date 2023/2/15 20:33
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //根据用户名查询用户信息
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, username);
        User user = userMapper.selectOne(wrapper);
        //如果查询不到数据就通过抛出异常来给出提示
        if (Objects.isNull(user)) {
            throw new GlobalBusinessException(401, "用户名或密码错误");
        }
        //TODO 根据用户查询权限信息 添加到LoginUser中
        // 获取该用户的角色所对应的所有菜单
        List<Menu> menuList = menuMapper.getMenuListByUserId(user.getId());
        // 从获取到的菜单对象中遍历出所有操作的权限编码
        List<String> permList = new ArrayList<>();
        menuList.forEach(menu -> {
            // type=2表示按钮级别
            if (menu.getType() == 2) {
                permList.add(menu.getPerms());
            }
        });
        //封装成UserDetails对象返回
        return new LoginUser(user, permList);
    }
}
