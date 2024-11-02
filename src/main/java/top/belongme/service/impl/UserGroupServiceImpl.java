package top.belongme.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import top.belongme.mapper.UserGroupMapper;
import top.belongme.model.pojo.UserGroup;
import top.belongme.service.UserGroupService;

/**
 * @Title: UserGroupServiceImpl
 * @ProjectName Task-Manage-Back
 * @Description: TODO
 * @Author DengChao
 * @Date 2023/12/1718:08
 */
@Service
public class UserGroupServiceImpl extends ServiceImpl<UserGroupMapper, UserGroup> implements UserGroupService {
}
