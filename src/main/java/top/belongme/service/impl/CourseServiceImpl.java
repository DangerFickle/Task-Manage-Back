package top.belongme.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.belongme.exception.GlobalBusinessException;
import top.belongme.mapper.BatchMapper;
import top.belongme.mapper.CourseMapper;
import top.belongme.mapper.TaskMapper;
import top.belongme.model.pojo.Batch;
import top.belongme.model.pojo.Course;
import top.belongme.model.pojo.Task;
import top.belongme.model.pojo.user.LoginUser;
import top.belongme.model.result.Result;
import top.belongme.model.vo.CourseQueryVo;
import top.belongme.service.CourseService;

import javax.annotation.Resource;
import java.io.File;
import java.util.List;
import java.util.Objects;

/**
 * @Title: CourseServiceImpl
 * @ProjectName JobManage-Back
 * @Description: TODO
 * @Author DengChao
 * @Date 2023/2/814:13
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class CourseServiceImpl extends ServiceImpl<CourseMapper, Course> implements CourseService {
    @Resource
    private String filePathBySystem;

    @Resource
    private BatchMapper batchMapper;

    @Resource
    private TaskMapper taskMapper;

    @Override
    public IPage<Course> selectPage(Page<Course> pageParam, CourseQueryVo courseQueryVo) {
        return baseMapper.selectPage(pageParam, courseQueryVo);
    }

    // 添加课程并创建同名文件夹
    @Override
    public Result addCourseAndFolderPath(Course course) {
        Course oldCourse = baseMapper.selectOne(new QueryWrapper<Course>().eq("course_name", course.getCourseName()));
        if (Objects.nonNull(oldCourse)) {
            throw new GlobalBusinessException(800, "课程已存在");
        }
        File courseFolder = new File(filePathBySystem + course.getCourseName());
        if (!courseFolder.exists()) {
            boolean isMakeDir = courseFolder.mkdirs();
            if (isMakeDir) {
                LoginUser loginUser = (LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
                course.setCreatorId(loginUser.getUser().getId());
                course.setModifierId(loginUser.getUser().getId());
                course.setFolderPath(courseFolder.getAbsolutePath());
                int insert = baseMapper.insert(course);
                if (insert > 0) {
                    // 根据课程名构建同名文件夹
                    return new Result(200, "课程创建成功");
                }
            } else {
                throw new GlobalBusinessException(800, "课程对应的文件夹创建失败");
            }
        } else {
            throw new GlobalBusinessException(800, "课程对应的文件夹已存在");
        }
        throw new GlobalBusinessException(800, "课程创建失败");
    }

    @Override
    public Result deleteCourse(String id) {
        List<Batch> belongCourseList = batchMapper.selectList(new QueryWrapper<Batch>().eq("belong_course_id", id));
        if (!belongCourseList.isEmpty()) {
            throw new GlobalBusinessException(800, "该课程下存在未删除的批次，无法删除");
        }

        Course course = baseMapper.selectById(id);
        if (Objects.nonNull(course)) {
            File courseFolder = new File(course.getFolderPath());
            courseFolder.delete();
            int delete = baseMapper.deleteById(id);
            if (delete > 0) {
                return new Result(200, "课程删除成功");
            }
        } else {
            throw new GlobalBusinessException(800, "课程不存在");
        }
        throw new GlobalBusinessException(800, "课程删除失败");
    }

    /**
     * TODO 更新课程信息
     *
     * @Author DengChao
     * @Date 2023/2/8 17:45
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result updateCourse(Course course) {
        Course oldCourse = baseMapper.selectById(course.getId());
        if (Objects.isNull(oldCourse)) {
            throw new GlobalBusinessException(800, "课程不存在");
        }
        // 标识最终是否更新数据库中的课程记录
        boolean isModify = false;
        // 原课程与新课程名不相同时，则进行修改
        if (!oldCourse.getCourseName().equals(course.getCourseName())) {
            // 查询数据库中是否存在新课程名
            Course isExistsCourse = baseMapper.selectOne(new QueryWrapper<Course>().eq("course_name", course.getCourseName()));
            if (Objects.nonNull(isExistsCourse)) {
                throw new GlobalBusinessException(800, "课程名已存在");
            }
            // 课程要更改为的新文件夹路径
            String newFolderPath = filePathBySystem + course.getCourseName();
            // 获取课程下所有的批次
            List<Batch> batchList = batchMapper.selectList(new QueryWrapper<Batch>().eq("belong_course_id", course.getId()));
            // 替换批次的文件夹路径中的课程名，oldCourse.getCourseName()替换为course.getCourseName()
            batchList.forEach(batch -> {
                batch.setFolderPath(newFolderPath + File.separator + batch.getBatchName());
                // 重新存入数据库
                batchMapper.updateById(batch);
                // 获取该批次下的所有作业
                List<Task> taskList = taskMapper.selectList(new QueryWrapper<Task>().eq("belong_batch_id", batch.getId()));
                taskList.forEach(task -> {
                    task.setFilePath(batch.getFolderPath() + File.separator + task.getFileName());
                    taskMapper.updateById(task);
                });
            });

            // 数据库中的记录更新完毕后，再修改文件夹的名称
            File oldCourseFolder = new File(oldCourse.getFolderPath());
            if (!oldCourseFolder.exists()) {
                throw new GlobalBusinessException(800, "课程对应的文件夹不存在");
            }
            // 构建新文件夹
            File newCourseFolder = new File(newFolderPath);

            // 重命名文件夹为新文件夹
            oldCourseFolder.renameTo(newCourseFolder);

            // 判断新文件夹是否存在，存在则重命名成功，不存在则重命名失败
            if (!newCourseFolder.exists()) {
                throw new GlobalBusinessException(800, "课程文件夹重命名失败");
            }
            course.setFolderPath(newFolderPath);
            isModify = true;
        } else {
            course.setCourseName(null);
        }

        // 如果旧课程描述与新课程描述不相同，则标识为需要修改
        if (!oldCourse.getDescription().equals(course.getDescription())) {
            isModify = true;
        } else {
            course.setDescription(null);
        }

        // 判断是否要修改数据库中的课程记录
        if (isModify) {
            // 获取当前登录用户
            LoginUser loginUser = (LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            // 设置修改者id
            course.setModifierId(loginUser.getUser().getId());
            baseMapper.updateById(course);
            return new Result(200, "课程更新成功");
        } else {
            throw new GlobalBusinessException(800, "未修改，课程信息未发生变化");
        }
    }

    @Override
    public Result updateStatus(String id, Integer status) {
        Course course = new Course();
        course.setId(id);
        course.setStatus(status);
        Course oldCourse = baseMapper.selectById(id);
        if (Objects.isNull(oldCourse)) {
            throw new GlobalBusinessException(800, "课程不存在");
        }
        // 如果课程状态与传入的状态相同，则抛出异常
        if (Objects.equals(oldCourse.getStatus(), status)) {
            throw new GlobalBusinessException(800, "课程状态同步失败");
        } else {
            // 获取当前登录用户
            LoginUser loginUser = (LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            // 修改修改者id
            course.setModifierId(loginUser.getUser().getId());
            int update = baseMapper.updateById(course);
            if (update > 0) {
                return new Result(200, "课程状态更新成功");
            } else {
                throw new GlobalBusinessException(800, "课程状态更新失败");
            }
        }
    }

    @Override
    public Result deleteBatchByIds(List<String> ids) {
        // 根据ids获取对应课程
        List<Course> courseList = baseMapper.selectBatchIds(ids);
        courseList.forEach(course -> {
            // 删除课程对应的文件夹
            File courseFolder = new File(course.getFolderPath());
            courseFolder.delete();
            // 删除课程
            baseMapper.deleteById(course.getId());
        });
        return new Result(200, "批量删除成功");
    }
}
