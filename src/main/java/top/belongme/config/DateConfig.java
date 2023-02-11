package top.belongme.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Title: DateConfig
 * @ProjectName JobManage-Back
 * @Description: TODO
 * @Author DengChao
 * @Date 2023/2/1115:12
 */
@Configuration
public class DateConfig {

    // 格林威治时间 1970-01-01 00:00:00，作为是否永不截止的比较元素
    @Bean("GMTDate")
    public Date getGMTDate() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            return dateFormat.parse("1970-01-01 00:00:00");
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }


}
