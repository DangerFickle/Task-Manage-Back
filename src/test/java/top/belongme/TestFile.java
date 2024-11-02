package top.belongme;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import top.belongme.mapper.BatchMapper;
import top.belongme.model.pojo.Batch;

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

    @Resource
    BatchMapper batchMapper;

    @Test
    void testBatch() {
        Batch batch = batchMapper.selectById("1684167556118102017");
        System.out.println(batch);
    }
}
