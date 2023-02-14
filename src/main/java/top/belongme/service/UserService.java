package top.belongme.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import top.belongme.model.pojo.task.TaskDetails;
import top.belongme.model.result.Result;
import top.belongme.model.pojo.user.User;
import top.belongme.model.vo.EmailVo;
import top.belongme.model.vo.ResetPasswordVo;
import top.belongme.model.vo.TaskDetailsQueryVo;

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
}
