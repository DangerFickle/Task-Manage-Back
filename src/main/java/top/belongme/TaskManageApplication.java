package top.belongme;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;



/**
 * @Title: top.belongme.JobManageApplication
 * @ProjectName JobManage-Back
 * @Description: TODO
 * @Author DengChao
 * @Date 2023/2/713:54
 */
@SpringBootApplication
@EnableGlobalMethodSecurity(prePostEnabled = true)
@MapperScan("top.belongme.mapper")
@ServletComponentScan("top.belongme.filter")
public class TaskManageApplication {
    public static void main(String[] args) {
        SpringApplication.run(TaskManageApplication.class);
    }
}
