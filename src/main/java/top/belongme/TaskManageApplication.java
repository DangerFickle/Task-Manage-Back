package top.belongme;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
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
@ServletComponentScan("top.belongme.filter")
@EnableAspectJAutoProxy
public class TaskManageApplication {
    public static void main(String[] args) {
        SpringApplication.run(TaskManageApplication.class);
    }
}
