package top.belongme.service;

import top.belongme.model.result.Result;
import top.belongme.model.pojo.user.User;

/**
 * @Title: LoginService
 * @ProjectName JobManage-Back
 * @Description: TODO
 * @Author DengChao
 * @Date 2023/2/714:39
 */
public interface LoginService {
    User findUserById(Integer id);

    Result login(User user);

    Result logout();
}
