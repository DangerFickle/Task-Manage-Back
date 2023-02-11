package top.belongme.service;

import top.belongme.model.result.Result;
import top.belongme.model.pojo.user.User;

/**
 * @Title: UserService
 * @ProjectName JobManage-Back
 * @Description: TODO
 * @Author DengChao
 * @Date 2023/2/718:32
 */
public interface UserService {
    Result<User> getUserInfo(Long userId);
}
