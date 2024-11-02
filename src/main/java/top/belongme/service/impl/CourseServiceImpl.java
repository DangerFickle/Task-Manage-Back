package top.belongme.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.belongme.exception.GlobalBusinessException;
import top.belongme.mapper.BatchMapper;
import top.belongme.mapper.CourseMapper;
import top.belongme.mapper.GroupMapper;
import top.belongme.mapper.UserMapper;
import top.belongme.model.dto.CourseQueryDTO;
import top.belongme.model.pojo.Batch;
import top.belongme.model.pojo.Course;
import top.belongme.model.pojo.Group;
import top.belongme.model.pojo.user.LoginUser;
import top.belongme.model.pojo.user.User;
import top.belongme.model.result.Result;
import top.belongme.service.CourseService;
import top.belongme.utils.LoginUserUtil;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
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
    private GroupMapper groupMapper;

    @Resource
    private UserMapper userMapper;


    @Override
    public IPage<Course> selectPage(Page<Course> pageParam, CourseQueryDTO courseQueryDTO) {
        return baseMapper.selectPage(pageParam, courseQueryDTO);
    }

    // 添加课程并创建同名文件夹
    @Override
    public Result addCourse(Course course) {
        Course oldCourse = baseMapper.selectOne(new QueryWrapper<Course>().eq("course_name", course.getCourseName()));
        if (Objects.nonNull(oldCourse)) {
            log.error("课程创建失败，因为即将创建的课程已存在");
            throw new GlobalBusinessException(800, "课程已存在");
        }
        // 获取当前登录用户
        LoginUser loginUser = LoginUserUtil.getCurrentLoginUser();
        // 设置创建人和修改人
        course.setCreatorId(loginUser.getUser().getId());
        course.setModifierId(loginUser.getUser().getId());
        if (course.getGroupMaxMemberSize() != null && course.getGroupMaxMemberSize() <= 0) {
            throw new GlobalBusinessException(800, "群组最大人数必须大于等于1");
        }
        // 计算该课程下群组的最大群组数量
        QueryWrapper<User> userQW = new QueryWrapper<>();
        userQW.ne("id", 1);
        BigDecimal userCount = BigDecimal.valueOf(userMapper.selectCount(userQW));
        BigDecimal groupMaxMemberSize = BigDecimal.valueOf(course.getGroupMaxMemberSize());
        BigDecimal groupMaxSize = userCount.divide(groupMaxMemberSize, 0, RoundingMode.CEILING);
        course.setGroupMaxSize(groupMaxSize.intValue());
        // 插入课程
        int insert = baseMapper.insert(course);
        if (insert <= 0) {
            log.error("课程创建失败，因为课程对应的文件夹创建失败");
            // 根据课程名构建同名文件夹
            throw new GlobalBusinessException(800, "课程创建失败");
        } else {
            return new Result(200, "课程创建成功");
        }
    }

    @Override
    public Result deleteCourse(String courseId) {
        // 查询课程
        Course course = baseMapper.selectById(courseId);
        if (Objects.isNull(course)) {
            log.error("课程删除失败，因为即将删除的课程不存在");
            throw new GlobalBusinessException(800, "课程不存在");
        }
        // 检查个人批次
        Long belongCourseList = batchMapper.selectCount(new QueryWrapper<Batch>().eq("belong_course_id", courseId));
        // 检查小组批次
        if (belongCourseList != 0) {
            log.error("课程删除失败，该课程下存在未删除的批次");
            throw new GlobalBusinessException(800, "该课程下存在未删除的批次，无法删除");
        }

        // 检查该课程下是否有群组
        Long group = groupMapper.selectCount(new QueryWrapper<Group>().eq("belong_course", courseId));
        if (group != 0) {
            log.error("课程删除失败，该课程下存在未删除的群组");
            throw new GlobalBusinessException(800, "该课程下存在未删除的群组，无法删除");
        }

        // 删除数据库中的课程记录
        int deleteDB = baseMapper.deleteById(courseId);
        if (deleteDB <= 0) {
            log.error("课程删除失败，因为删除数据库中的课程失败");
            throw new GlobalBusinessException(800, "课程删除失败");
        } else {
            // 获取当前登录用户
            LoginUser loginUser = LoginUserUtil.getCurrentLoginUser();

            log.info("管理员【{}】，删除了课程【{}】", loginUser.getUser().getName(), course.getCourseName());
            return new Result(200, "课程删除成功");
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
            Long isExistsCourse = baseMapper.selectCount(new QueryWrapper<Course>().eq("course_name", course.getCourseName()));
            if (isExistsCourse > 0) {
                log.error("课程更新失败，因为即将更新的课程名已存在");
                throw new GlobalBusinessException(800, "课程名已存在");
            }
            isModify = true;
        } else {
            course.setCourseName(null);
        }

        // 如果旧课程描述与新课程描述不相同，则标识为需要修改
        if (oldCourse.getDescription().equals(course.getDescription())) {
            course.setDescription(null);
        } else {
            isModify = true;
        }

        // 修改群组最大人数
        if (oldCourse.getGroupMaxMemberSize().equals(course.getGroupMaxMemberSize())) {
            course.setGroupMaxMemberSize(null);
        } else {
            if (course.getGroupMaxMemberSize() != null && course.getGroupMaxMemberSize() <= 0) {
                throw new GlobalBusinessException(800, "群组最大人数必须大于等于1");
            }
            // 根据新传入的群组最大人数计算课程下最大群组数
            QueryWrapper<User> userQW = new QueryWrapper<>();
            userQW.ne("id", 1);

            BigDecimal userCount = BigDecimal.valueOf(userMapper.selectCount(userQW));
            BigDecimal groupMaxMemberSize = BigDecimal.valueOf(course.getGroupMaxMemberSize());
            BigDecimal groupMaxSize = userCount.divide(groupMaxMemberSize, 0, RoundingMode.CEILING);
            course.setGroupMaxSize(groupMaxSize.intValue());
            isModify = true;
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
            throw new GlobalBusinessException(800, "课程信息未发生变化");
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
