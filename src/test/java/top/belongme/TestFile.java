package top.belongme;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.annotation.Resource;


/**
 * @Title: TestFile
 * @ProjectName Task-Manage-Back
 * @Description: TODO
 * @Author DengChao
 * @Date 2023/2/1321:00
 */
@SpringBootTest
public class TestFile {
    @Resource
    PasswordEncoder passwordEncoder;


}
