package top.belongme.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

/**
 * @Title: FileConfig
 * @ProjectName JobManage-Back
 * @Description: TODO
 * @Author DengChao
 * @Date 2023/2/815:41
 */
@Slf4j
@Configuration
public class FileConfig {

    @Value("${job-manage-settings.work-folder-name}")
    String workFolderName;

    @Bean("filePathBySystem")
    public String getFilePathBySystem() {
        // 初始化不同系统对应的文件路径 filePathBySystem， 要debug运行项目才能获取到
        String path = System.getProperty("java.class.path");
        int firstIndex = path.lastIndexOf(System.getProperty("path.separator")) + 1;
        int lastIndex = path.lastIndexOf(File.separator) + 1;
        path = path.substring(firstIndex, lastIndex);
        try {
            String filePathBySystem = URLDecoder.decode(path, "utf-8") + workFolderName + File.separator;
            log.info("系统运行的目录：{}", filePathBySystem);
            // 创建根据批次下载全部文件时的临时存放目录
            File tempFilesFolder = new File(filePathBySystem + "temp_files");
            if (!tempFilesFolder.exists()) {
                tempFilesFolder.mkdirs();
            }
            return filePathBySystem;
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("初始化不同系统对应的文件路径失败");
        }
    }

    @Bean
    public CommonsMultipartResolver commonsMultipartResolver() {
        return new CommonsMultipartResolver();
    }
}
