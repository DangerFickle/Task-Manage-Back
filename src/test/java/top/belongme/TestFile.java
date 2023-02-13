package top.belongme;

import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import top.belongme.mapper.BatchMapper;
import top.belongme.model.pojo.Batch;

import javax.annotation.Resource;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

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
    private BatchMapper batchMapper;

}
