package top.belongme.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import top.belongme.model.pojo.Course;
import top.belongme.model.result.Result;
import top.belongme.model.vo.CourseQueryVo;
import top.belongme.service.CourseService;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Title: CourseController
 * @ProjectName JobManage-Back
 * @Description: TODO
 * @Author DengChao
 * @Date 2023/2/814:05
 */
@RestController
@RequestMapping("course")
public class CourseController {

    @Resource
    CourseService courseService;

    /**
     * TODO 获取课程列表，分页查询 + 模糊查询
     *
     * @Author DengChao
     * @Date 2023/2/9 10:55
     */
    @PreAuthorize("hasAuthority('job:course:select')")
    @GetMapping("/listPage/{page}/{limit}")
    public Result<IPage<Course>> getCourseList(@PathVariable Long page,
                                               @PathVariable Long limit,
                                               CourseQueryVo courseQueryVo) {
        //创建page对象
        Page<Course> pageParam = new Page<>(page, limit);
        //调用service方法
        IPage<Course> pageModel = courseService.selectPage(pageParam, courseQueryVo);
        return new Result<>(200, "请求成功", pageModel);
    }

    /**
     * TODO 获取课程列表
     *
     * @Author DengChao
     * @Date 2023/2/9 12:24
     */
    @PreAuthorize("hasAuthority('job:course:select')")
    @GetMapping("/list")
    public Result<List<Course>> getCourseList() {
        List<Course> courseList = courseService.list();
        return new Result<>(200, "请求成功", courseList);
    }

    /**
     * TODO 获取课程列表，只要已启用的
     *
     * @Author DengChao
     * @Date 2023/2/9 12:24
     */
    @PreAuthorize("hasAuthority('job:course:select')")
    @GetMapping("/listOnlyEnabled")
    public Result<List<Course>> getCourseListOnlyEnabled() {
        List<Course> courseList = courseService.list(new QueryWrapper<Course>().eq("status", 1));
        return new Result<>(200, "请求成功", courseList);
    }

    /**
     * TODO 添加课程
     *
     * @Author DengChao
     * @Date 2023/2/9 10:54
     */
    @PreAuthorize("hasAuthority('job:course:insert')")
    @PostMapping("/add")
    public Result addCourse(@RequestBody Course course) {
        return courseService.addCourseAndFolderPath(course);
    }

    /**
     * TODO 根据id删除课程
     *
     * @Author DengChao
     * @Date 2023/2/9 10:54
     */
    @PreAuthorize("hasAuthority('job:course:delete')")
    @DeleteMapping("/delete/{id}")
    public Result deleteCourse(@PathVariable String id) {
        return courseService.deleteCourse(id);
    }

    /**
     * TODO 批量删除课程
     *
     * @Author DengChao
     * @Date 2023/2/9 10:54
     */
//    @PreAuthorize("hasAuthority('job:course:delete')")
//    @DeleteMapping("/deleteBatch")
    public Result deleteBatchByIds(@RequestBody List<String> ids) {
        return courseService.deleteBatchByIds(ids);
    }

    /**
     * TODO 更新课程信息
     *
     * @Author DengChao
     * @Date 2023/2/9 10:54
     */
    @PreAuthorize("hasAuthority('job:course:update')")
    @PutMapping("/update")
    public Result updateCourse(@RequestBody Course course) {
        return courseService.updateCourse(course);
    }

    /**
     * TODO 更新课程状态
     *
     * @Author DengChao
     * @Date 2023/2/9 10:54
     */
    @PreAuthorize("hasAuthority('job:course:update')")
    @PutMapping("/updateStatus/{courseId}/{status}")
    public Result updateStatus(@PathVariable String courseId,
                               @PathVariable Integer status) {
        return courseService.updateStatus(courseId, status);
    }

    /**
     * TODO 根据id获取课程信息
     *
     * @Author DengChao
     * @Date 2023/2/9 10:55
     */
    @PreAuthorize("hasAuthority('job:course:select')")
    @GetMapping("/get/{courseId}")
    public Result getCourseById(@PathVariable String courseId) {
        Course course = courseService.getById(courseId);
        return new Result(200, "请求成功", course);
    }
}
