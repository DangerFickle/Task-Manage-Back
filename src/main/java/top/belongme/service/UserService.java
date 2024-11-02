package top.belongme.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import top.belongme.model.pojo.user.User;
import top.belongme.model.result.Result;
import top.belongme.model.dto.EmailDTO;
import top.belongme.model.dto.ResetPasswordDTO;
import top.belongme.model.dto.TaskDetailsQueryDTO;
import top.belongme.model.dto.UserDTO;

import java.util.List;

/**
 * @Title: UserService
 * @ProjectName JobManage-Back
 * @Description: TODO
 * @Author DengChao
 * @Date 2023/2/718:32
 */
public interface UserService extends IService<User> {
    Result<User> getUserInfo(Long userId);

    IPage<User> getNotCommitUserList(Page<User> pageParam, TaskDetailsQueryDTO taskDetailsQueryDTO);

    Result resetPassword(ResetPasswordDTO user);

    Result updateEmail(EmailDTO emailDTO);

    IPage<User> selectPage(Page<User> pageParam, UserDTO userDTO);

    Result saveUser(User user);

    Result updateUser(User user);

    Result<User> getUserById(String userId);

    Result deleteById(String userId);

    Result switchStatus(String userId);

    IPage<User> getUserWithoutGroupMember(Page<User> pageParam, UserDTO userDTO);

    Result saveUserBatch(List<User> userList);
}
