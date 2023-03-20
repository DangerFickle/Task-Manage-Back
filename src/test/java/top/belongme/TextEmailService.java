package top.belongme;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import top.belongme.mapper.UserMapper;
import top.belongme.model.pojo.Email;
import top.belongme.model.pojo.user.User;
import top.belongme.service.SendMailService;

import javax.annotation.Resource;

/**
 * @Title: TextEmailService
 * @ProjectName Task-Manage-Back
 * @Description: TODO
 * @Author DengChao
 * @Date 2023/2/2014:25
 */
@SpringBootTest
public class TextEmailService {

    @Resource
    PasswordEncoder passwordEncoder;

    @Resource
    UserMapper userMapper;

    @Test
    void testSend() {
    }
}
