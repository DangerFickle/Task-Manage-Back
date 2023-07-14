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
import top.belongme.model.pojo.task.Task;
import top.belongme.model.pojo.user.LoginUser;
import top.belongme.model.result.Result;
import top.belongme.model.vo.CourseQueryVo;
import top.belongme.service.CourseService;
import top.belongme.utils.LoginUserUtil;

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
            log.error("课程创建失败，因为即将创建的课程已存在");
            throw new GlobalBusinessException(800, "课程已存在");
        }
        // 根据课程名构建同名文件夹
        File courseFolder = new File(filePathBySystem + course.getCourseName());
        // 获取当前登录用户
        LoginUser loginUser = LoginUserUtil.getCurrentLoginUser();
        // 设置创建人和修改人
        course.setCreatorId(loginUser.getUser().getId());
        course.setModifierId(loginUser.getUser().getId());
        // 设置课程文件夹路径
        course.setFolderPath(courseFolder.getAbsolutePath());
        // 插入课程
        int insert = baseMapper.insert(course);
        if (insert <= 0) {
            log.error("课程创建失败，因为课程对应的文件夹创建失败");
            // 根据课程名构建同名文件夹
            throw new GlobalBusinessException(800, "课程创建失败");
        }

        if (!courseFolder.exists()) {
            boolean isMakeDir = courseFolder.mkdirs();
            if (isMakeDir) {
                log.info("管理员【{}】创建了课程：【{}】", loginUser.getUser().getName(), course.getCourseName());
                return new Result(200, "课程创建成功");
            } else {
                log.error("客车创建失败，因为课程对应的文件夹创建失败");
                throw new GlobalBusinessException(800, "课程对应的文件夹创建失败");
            }
        } else {
            log.error("客车创建失败，因为课程对应的文件夹已存在，有人在程序运行的目录手动创建了同名文件夹！");
            throw new GlobalBusinessException(800, "课程对应的文件夹已存在，有人在程序运行的目录手动创建了同名文件夹！");
        }
    }

    @Override
    public Result deleteCourse(String courseId) {
        List<Batch> belongCourseList = batchMapper.selectList(new QueryWrapper<Batch>().eq("belong_course_id", courseId));
        if (!belongCourseList.isEmpty()) {
            log.error("课程删除失败，因为该课程下存在未删除的批次");
            throw new GlobalBusinessException(800, "该课程下存在未删除的批次，无法删除");
        }
        // 查询课程
        Course course = baseMapper.selectById(courseId);
        if (Objects.isNull(course)) {
            log.error("课程删除失败，因为即将删除的课程不存在");
            throw new GlobalBusinessException(800, "课程不存在");
        }

        // 删除数据库中的课程记录
        int deleteDB = baseMapper.deleteById(courseId);
        if (deleteDB <= 0) {
            log.error("课程删除失败，因为删除数据库中的课程失败");
            throw new GlobalBusinessException(800, "课程删除失败");
        }

        File courseFolder = new File(course.getFolderPath());
        if (courseFolder.exists()) {
            boolean deleteFile = courseFolder.delete();
            if (!deleteFile) {
                log.error("课程删除失败，因为删除课程对应的文件夹失败");
                throw new GlobalBusinessException(800, "课程对应的文件夹删除失败");
            }
            // 获取当前登录用户
            LoginUser loginUser = LoginUserUtil.getCurrentLoginUser();

            log.info("管理员【{}】，删除了课程【{}】", loginUser.getUser().getName(), course.getCourseName());
            return new Result(200, "课程删除成功");
        } else {
            throw new GlobalBusinessException(800, "课程对应的文件夹不存在");
        }

    }

    /**
     * TODO 更新课程信息
     *
     * @Author DengChao
     * @Date 2023/2/8 17:45
     */
    @Override
    public Result updateCourse(Course course) {
        Course oldCourse = baseMapper.selectById(course.getId());
        if (Objects.isNull(oldCourse)) {
            log.error("课程更新失败，因为即将更新的课程不存在");
            throw new GlobalBusinessException(800, "课程不存在");
        }
        // 标识最终是否更新数据库中的课程记录
        boolean isModify = false;
        // 原课程与新课程名不相同时，则进行修改
        if (!oldCourse.getCourseName().equals(course.getCourseName())) {
            // 查询数据库中是否存在新课程名
            Course isExistsCourse = baseMapper.selectOne(new QueryWrapper<Course>().eq("course_name", course.getCourseName()));
            if (Objects.nonNull(isExistsCourse)) {
                log.error("课程更新失败，因为即将更新的课程名已存在");
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
                int updateBatch = batchMapper.updateById(batch);
                if (updateBatch <= 0) {
                    log.error("课程更新失败，因为在数据库中更新课程下的批次失败");
                    throw new GlobalBusinessException(800, "课程更新失败");
                }
                // 获取该批次下的所有作业
                List<Task> taskList = taskMapper.selectList(new QueryWrapper<Task>().eq("belong_batch_id", batch.getId()));
                taskList.forEach(task -> {
                    task.setFilePath(batch.getFolderPath() + File.separator + task.getFileName());
                    int updateTask = taskMapper.updateById(task);
                    if (updateTask <= 0) {
                        log.error("课程更新失败，因为在数据库中更新课程下的作业失败");
                        throw new GlobalBusinessException(800, "课程更新失败");
                    }
                });
            });

            // 数据库中的记录更新完毕后，再修改文件夹的名称
            File oldCourseFolder = new File(oldCourse.getFolderPath());
            if (!oldCourseFolder.exists()) {
                log.error("课程更新失败，因为课程对应的文件夹不存在");
                throw new GlobalBusinessException(800, "课程对应的文件夹不存在");
            }
            // 构建新文件夹
            File newCourseFolder = new File(newFolderPath);
            if (newCourseFolder.exists()) {
                log.error("课程更新失败，因为课程对应的文件夹已存在，有人在程序运行的目录手动创建了同名文件夹！");
                throw new GlobalBusinessException(800, "课程对应的文件夹已存在，有人在程序运行的目录手动创建了同名文件夹！");
            }

            // 重命名文件夹为新文件夹
            oldCourseFolder.renameTo(newCourseFolder);

            // 判断新文件夹是否存在，存在则重命名成功，不存在则重命名失败
            if (!newCourseFolder.exists()) {
                log.error("课程更新失败，因为课程对应的文件夹重命名失败");
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
            LoginUser loginUser = LoginUserUtil.getCurrentLoginUser();
            // 设置修改者id
            course.setModifierId(loginUser.getUser().getId());
            baseMapper.updateById(course);
            log.info("管理员【{}】，更新了课程：【{}】", loginUser.getUser().getName(), oldCourse.getCourseName());
            return new Result(200, "课程更新成功");
        } else {
            log.error("课程未更新，因为传入的课程信息与旧信息无差异");
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
            log.error("课程状态更新失败，因为课程不存在");
            throw new GlobalBusinessException(800, "课程不存在");
        }
        // 如果课程状态与传入的状态相同，则抛出异常
        if (Objects.equals(oldCourse.getStatus(), status)) {
            log.error("课程状态更新失败，因为课程状态同步失败");
            throw new GlobalBusinessException(800, "课程状态同步失败");
        } else {
            // 获取当前登录用户
            LoginUser loginUser = LoginUserUtil.getCurrentLoginUser();
            // 修改修改者id
            course.setModifierId(loginUser.getUser().getId());
            int update = baseMapper.updateById(course);
            if (update > 0) {
                log.info("管理员【{}】，更新了【{}】课程的状态为【{}】", loginUser.getUser().getName(), oldCourse.getCourseName(), status == 1 ? "启用" : "禁用");
                return new Result(200, "课程状态更新成功");
            } else {
                log.error("课程状态更新失败，因为课程状态同步失败");
                throw new GlobalBusinessException(800, "课程状态更新失败");
            }
        }
    }

}
