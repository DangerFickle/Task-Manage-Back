package top.belongme.service.impl;

import com.alibaba.excel.EasyExcel;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Service;
import top.belongme.exception.GlobalBusinessException;
import top.belongme.mapper.BatchMapper;
import top.belongme.mapper.CourseMapper;
import top.belongme.mapper.UserMapper;
import top.belongme.model.excel.UserExcel;
import top.belongme.model.pojo.Batch;
import top.belongme.model.pojo.Course;
import top.belongme.model.result.Result;
import top.belongme.model.dto.TaskDetailsQueryDTO;
import top.belongme.service.ExcelService;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;

/**
 * @Title: ExcelServiceImpl
 * @ProjectName Task-Manage-Back
 * @Description: TODO
 * @Author DengChao
 * @Date 2023/3/2617:25
 */
@Slf4j
@Service
public class ExcelServiceImpl implements ExcelService {

    @Resource
    private UserMapper userMapper;

    @Resource
    private BatchMapper batchMapper;

    @Resource
    private CourseMapper courseMapper;

    @Resource
    private String filePathBySystem;


    @Override
    public Result getNotCommitExcel(HttpServletResponse response, TaskDetailsQueryDTO taskDetailsQueryDTO) throws Exception {
        Batch batch = batchMapper.selectById(taskDetailsQueryDTO.getBelongBatchId());
        if (Objects.nonNull(batch)) {
            List<UserExcel> notCommitUserList = userMapper.getNotCommitUserListExcel(taskDetailsQueryDTO);
            // 获取批次所属课程
            Course course = courseMapper.selectById(batch.getBelongCourseId());

            StringBuffer fileName = new StringBuffer(filePathBySystem);
            fileName.append("temp_files");
            fileName.append(File.separator);
            fileName.append(course.getCourseName());
            fileName.append("—");
            fileName.append(batch.getBatchName());
            fileName.append("—");
            fileName.append("未交名单.xlsx");

            File excelFile = new File(fileName.toString());

            EasyExcel.write(excelFile, UserExcel.class).sheet("未交名单").doWrite(notCommitUserList);

            //在vue的response中显示Content-Disposition
            response.setHeader("Access-Control-Expose-Headers", "Content-Disposition");
            // 设置在下载框默认显示的文件名
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(excelFile.getName(), StandardCharsets.UTF_8));
            response.setHeader("Content-Length", String.valueOf(excelFile.length()));
            response.setContentType("application/octet-stream;charset=UTF-8");

            FileUtils.copyFile(excelFile, response.getOutputStream());
            // 删除临时文件
            excelFile.delete();
            return new Result(200, "请求成功");
        } else {
            response.setHeader("Access-Control-Expose-Headers", "exception");
            response.setHeader("exception", URLEncoder.encode("批次不存在", StandardCharsets.UTF_8));
            throw new GlobalBusinessException(500, "批次不存在");
        }

    }
}
