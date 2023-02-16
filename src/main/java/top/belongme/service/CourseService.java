package top.belongme.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import top.belongme.model.pojo.Course;
import top.belongme.model.result.Result;
import top.belongme.model.vo.CourseQueryVo;

import java.util.List;

/**
 * @Title: CourseService
 * @ProjectName JobManage-Back
 * @Description: TODO
 * @Author DengChao
 * @Date 2023/2/814:13
 */
public interface CourseService extends IService<Course> {
    IPage<Course> selectPage(Page<Course> pageParam, CourseQueryVo courseQueryVo);

    Result addCourseAndFolderPath(Course course);

    Result deleteCourse(String id);

    Result updateCourse(Course course);

    Result updateStatus(String id, Integer status);

}
