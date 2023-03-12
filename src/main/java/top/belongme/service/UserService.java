package top.belongme.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import top.belongme.model.pojo.user.User;
import top.belongme.model.result.Result;
import top.belongme.model.vo.EmailVo;
import top.belongme.model.vo.ResetPasswordVo;
import top.belongme.model.vo.TaskDetailsQueryVo;
import top.belongme.model.vo.UserVo;

/**
 * @Title: UserService
 * @ProjectName JobManage-Back
 * @Description: TODO
 * @Author DengChao
 * @Date 2023/2/718:32
 */
public interface UserService extends IService<User> {
    Result<User> getUserInfo(Long userId);

    IPage<User> getNoCommitUserList(Page<User> pageParam, TaskDetailsQueryVo taskDetailsQueryVo);

    Result resetPassword(ResetPasswordVo user);

    Result updateEmail(EmailVo emailVo);

    IPage<User> selectPage(Page<User> pageParam, UserVo userVo);

    Result saveUser(User user);

    Result updateUser(User user);

    Result<User> getUserById(String userId);

    Result deleteById(String userId);

    Result switchStatus(String userId);
}
