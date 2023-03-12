package top.belongme.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import top.belongme.mapper.RoleMapper;
import top.belongme.model.pojo.Role;
import top.belongme.service.RoleService;

/**
 * @Title: RoleServiceImpl
 * @ProjectName Task-Manage-Back
 * @Description: TODO
 * @Author DengChao
 * @Date 2023/3/816:52
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {
}
