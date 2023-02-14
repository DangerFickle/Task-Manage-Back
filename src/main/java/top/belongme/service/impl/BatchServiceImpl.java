package top.belongme.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.io.FileUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.belongme.exception.GlobalBusinessException;
import top.belongme.mapper.BatchMapper;
import top.belongme.mapper.CourseMapper;
import top.belongme.mapper.TaskMapper;
import top.belongme.mapper.UserMapper;
import top.belongme.model.pojo.Batch;
import top.belongme.model.pojo.Course;
import top.belongme.model.pojo.task.Task;
import top.belongme.model.pojo.user.LoginUser;
import top.belongme.model.pojo.user.User;
import top.belongme.model.result.Result;
import top.belongme.model.vo.BatchQueryVo;
import top.belongme.service.BatchService;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * @Title: BatchServiceImpl
 * @ProjectName JobManage-Back
 * @Description: TODO
 * @Author DengChao
 * @Date 2023/2/915:30
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class BatchServiceImpl extends ServiceImpl<BatchMapper, Batch> implements BatchService {

    @Resource
    private CourseMapper courseMapper;

    @Resource
    private TaskMapper taskMapper;

    @Resource
    private UserMapper userMapper;

    @Resource
    private String filePathBySystem;


    @Resource
    private Date GMTDate;


    /**
     * TODO 添加批次并创建同名文件夹
     *
     * @Author DengChao
     * @Date 2023/2/9 16:11
     */
    @Override
    public Result addBatchAndFolderPath(Batch batch) {
        Batch oldBatch = baseMapper.selectOne(new QueryWrapper<Batch>().eq("batch_name", batch.getBatchName()).eq("belong_course_id", batch.getBelongCourseId()));
        if (Objects.nonNull(oldBatch)) {
            throw new GlobalBusinessException(800, "批次已存在");
        }

        // 获取所属课程
        Course course = courseMapper.selectById(batch.getBelongCourseId());
        if (Objects.isNull(course)) {
            throw new GlobalBusinessException(800, "课程不存在");
        }

        // 创建批次文件夹
        String batchFolderName = course.getFolderPath() + File.separator + batch.getBatchName();
        File batchFolder = new File(batchFolderName);
        boolean mkdirs = batchFolder.mkdirs();
        if (!mkdirs) {
            throw new GlobalBusinessException(800, "批次文件夹创建失败");
        }
        batch.setFolderPath(batchFolderName);
        // 获取当前登录用户
        LoginUser loginUser = (LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        // 设置创建者id
        batch.setCreatorId(loginUser.getUser().getId());
        // 设置修改者id
        batch.setModifierId(loginUser.getUser().getId());
        int insert = baseMapper.insert(batch);
        if (insert > 0) {
            return new Result(200, "批次添加成功");
        } else {
            throw new GlobalBusinessException(800, "批次添加失败");
        }
    }

    /**
     * TODO 分页查询批次
     *
     * @Author DengChao
     * @Date 2023/2/9 22:47
     */
    @Override
    public IPage<Batch> selectPage(Page<Batch> pageParam, BatchQueryVo batchQueryVo) {
        Course course = courseMapper.selectById(batchQueryVo.getBelongCourseId());
        if (Objects.isNull(course)) {
            throw new GlobalBusinessException(800, "该课程不存在");
        }

        if (course.getStatus() == 0) {
            throw new GlobalBusinessException(800, "该课程已被禁用");
        }

        IPage<Batch> batchIPage = baseMapper.selectPage(pageParam, batchQueryVo);
        batchIPage.getRecords().forEach(batch -> {
            // 判断批次的截止时间是否是格林威治时间，是则设置为未截止，否则设置为已截止
            if (GMTDate.equals(batch.getEndTime())) {
                batch.setIsEnd(0);
            } else {
                // 判断批次是否已截止，date1.compareTo(date2)返回值为1则date1大于date2，返回值为0则date1等于date2，返回值为-1则date1小于date2
                int compare = batch.getEndTime().compareTo(new Date());
                if (compare <= 0) {
                    // 设置批次的截止状态，1为已截止，0为未截止
                    batch.setIsEnd(1);
                } else {
                    batch.setIsEnd(0);
                }
            }
        });
        return batchIPage;
    }

    /**
     * TODO 修改批次文件夹名称，修改批次所属课程（需要移动文件夹）
     *
     * @Author DengChao
     * @Date 2023/2/9 21:55
     */
    @Override
    public Result updateBatchAndFolderPath(Batch batch) {
        // 获取原批次信息，判断批次是否存在
        Batch oldBatch = baseMapper.selectById(batch.getId());
        if (Objects.isNull(oldBatch)) {
            throw new GlobalBusinessException(800, "批次不存在");
        }

        // 标志批次最终是否需要被修改
        boolean isModify = false;

        // 获取旧批次的所属课程，因为修改批次名或所属课程名时都要使用，所以提前获取，以免修改课程时二次获取
        Course oldBelongCourse = courseMapper.selectById(oldBatch.getBelongCourseId());

        // 批次名称要需改的情况，文件系统中
        if (!Objects.equals(batch.getBatchName(), oldBatch.getBatchName())) {
            // 拼接新批次文件夹路径，旧课程的路径 + 新批次名称
            String newBatchFolderPath = oldBelongCourse.getFolderPath() + File.separator + batch.getBatchName();
            // 构建要修改文件夹路径的新批次对象
            Batch batchUpdateFolderPath = new Batch();
            // 设置批次id
            batchUpdateFolderPath.setId(batch.getId());
            // 设置批次新名称
//            batchUpdateFolderPath.setBatchName(batch.getBatchName());
            // 设置批次新文件夹路径
            batchUpdateFolderPath.setFolderPath(newBatchFolderPath);
            // 设置批次修改人id为当前登录的用户id
//            batchUpdateFolderPath.setModifierId(loginUser.getUser().getId());
            // 更新数据库中的批次信息
            int update = baseMapper.updateById(batchUpdateFolderPath);
            // 如果更新成功则重命名文件系统中的文件夹名称
            if (update > 0) {
                // 获取旧批次文件夹路径
                String oldBatchFolderPath = oldBatch.getFolderPath();
                // 构建旧批次文件夹
                File oldBatchFolder = new File(oldBatchFolderPath);
                // 构建新批次文件夹
                File newBatchFolder = new File(newBatchFolderPath);

                // 修改文件夹名称
                boolean renameTo = oldBatchFolder.renameTo(newBatchFolder);
                if (!renameTo) {
                    throw new GlobalBusinessException(800, "批次文件夹重命名失败");
                }
                /*
                  如果批次名称修改了
                  则将新批次文件夹路径赋值给方法第一行获取到的旧批次对象
                  以供下方修改所属课程时使用
                 */
                oldBatch.setFolderPath(newBatchFolderPath);
                isModify = true;
            } else {
                throw new GlobalBusinessException(800, "批次修改失败");
            }
        } else {
            batch.setBatchName(null);
        }

        // 修改批次所属课程
        if (batch.getBelongCourseId() != null && !batch.getBelongCourseId().equals(oldBatch.getBelongCourseId())) {
            // 获取新的所属课程
            Course newBelongCourse = courseMapper.selectById(batch.getBelongCourseId());
            // 构建新所属课程的文件夹对象
            File newBelongCourseFolder = new File(newBelongCourse.getFolderPath());
            // 这里要获取旧的所属课程，上面提前获取过了，这里直接使用
            // 获取最批次即将要移动到的目的地文件夹路径
            String destBatchFolderPath = oldBatch.getFolderPath().replace(oldBelongCourse.getCourseName(), newBelongCourse.getCourseName());
            // 构建最终要存入数据库的批次对象，因为是修改所属课程，所以不需要设置课程名称
            Batch batchUpdateBelongCourse = new Batch();
            batchUpdateBelongCourse.setId(batch.getId());
//            batchUpdateBelongCourse.setBelongCourseId(batch.getBelongCourseId());
            batchUpdateBelongCourse.setFolderPath(destBatchFolderPath);
//            batchUpdateBelongCourse.setModifierId(loginUser.getUser().getId());
            // 更新批次信息
            int update = baseMapper.updateById(batchUpdateBelongCourse);
            // 如果批次的信息更新成功，则修改该批次下的所有作业的文件路径，将路径中旧所属课程名称替换为新所属课程名称
            if (update > 0) {
                // 获取该批次下的所有作业
                List<Task> taskList = taskMapper.selectList(new QueryWrapper<Task>().eq("belong_batch_id", batch.getId()));
                // 遍历作业，修改作业路径
                taskList.forEach(task -> {
                    task.setFilePath(batchUpdateBelongCourse.getFolderPath() + File.separator + task.getFileName());
                    int updateTask = taskMapper.updateById(task);
                    if (updateTask <= 0) {
                        throw new GlobalBusinessException(800, "批次下的作业修改失败");
                    }
                });
            } else {
                throw new GlobalBusinessException(800, "批次修改失败");
            }
            // 构建原批次文件夹路径，要移动到新新所属课程的文件夹下
            File oldBatchFolder = new File(oldBatch.getFolderPath());
            try {

                // 将原批次文件夹移动到新所属课程的文件夹下
                FileUtils.moveDirectoryToDirectory(oldBatchFolder, newBelongCourseFolder, true);
                // 如果所属课程修改成功，设置修改标志为true
                isModify = true;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            // 如果所属课程未修改，则将所属课程id设置为null，不修改
            batch.setBelongCourseId(null);
        }


        // 如果新描述与旧描述相同，则设置新描述为null，不修改
        if (!oldBatch.getDescription().equals(batch.getDescription())) {
            isModify = true;
        }

        // 如果新截止时间为null，代表前端未传入，则设置为格林威治时间
        if (Objects.isNull(batch.getEndTime())) {
            batch.setEndTime(GMTDate);
            isModify = true;
        } else {
            // 如果新截止时间与旧截止时间不同，则设置标志为true，相同则不修改
            if (!oldBatch.getEndTime().equals(batch.getEndTime())) {
                isModify = true;
            }
        }

        if (isModify) {
            // 获取当前登录用户
            LoginUser loginUser = (LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            // 设置修改者id
            batch.setModifierId(loginUser.getUser().getId());
            int update = baseMapper.updateById(batch);
            if (update > 0) {
                return new Result(200, "批次修改成功");
            } else {
                throw new GlobalBusinessException(800, "批次修改失败");
            }
        } else {
            throw new GlobalBusinessException(800, "批次信息未发生变化");
        }

    }

    @Override
    public Result deleteBatchAndFolderPath(String batchId) {
        // 获取批次信息
        Batch batch = baseMapper.selectById(batchId);
        if (Objects.isNull(batch)) {
            throw new GlobalBusinessException(800, "该批次不存在");
        }
        // 获取该批次下的作业，如果存在未删除的作业，则无法删除该批次
        List<Task> taskList = taskMapper.selectList(new QueryWrapper<Task>().eq("belong_batch_id", batch.getId()));
        if (!taskList.isEmpty()) {
            throw new GlobalBusinessException(800, "该批次下存在未删除的作业，无法删除");
        }

        int delete = baseMapper.deleteById(batchId);
        if (delete > 0) {
            String batchFolderPath = batch.getFolderPath();
            // 要删除的批次文件夹
            File batchFolder = new File(batchFolderPath);
            // 如果批次文件夹存在，则删除
            try {
                FileUtils.deleteDirectory(batchFolder);
                return new Result(200, "批次删除成功");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            throw new GlobalBusinessException(800, "批次删除失败");
        }

    }

    @Override
    public Result updateStatus(String batchId, Integer status) {
        Batch oldBatch = baseMapper.selectById(batchId);
        if (Objects.isNull(oldBatch)) {
            throw new GlobalBusinessException(800, "该批次不存在");
        }

        // 判断原批次是否已截止
        if (oldBatch.getEndTime().equals(GMTDate)) {
            oldBatch.setIsEnd(0);
        } else {
            if (oldBatch.getEndTime().compareTo(new Date()) <= 0) {
                oldBatch.setIsEnd(1);
            } else {
                oldBatch.setIsEnd(0);
            }
        }

        // 如果要更改的状态刚好是当前批次的状态，则不修改
        if (Objects.equals(status, oldBatch.getIsEnd())) {
            throw new GlobalBusinessException(800, "批次状态同步失败");
        }

        Batch batch = new Batch();
        batch.setId(batchId);
        // 如果要切换成截止状态，则设置截止时间为当前时间
        if (status == 1) {
            batch.setEndTime(new Date());
        } else if (status == 0) {
            // 如果要切换成未截止状态，则设置截止时间为格林威治时间
            batch.setEndTime(GMTDate);
        }

        // 获取当前登录用户
        LoginUser loginUser = (LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        // 设置修改者id
        batch.setModifierId(loginUser.getUser().getId());
        int update = baseMapper.updateById(batch);
        if (update > 0) {
            return new Result(200, "批次状态更新成功");
        } else {
            throw new GlobalBusinessException(800, "批次状态更新失败");
        }
    }

    @Override
    public IPage<Batch> selectPageIsCommit(Page<Batch> pageParam, BatchQueryVo batchQueryVo) {
        Course belongCourse = courseMapper.selectById(batchQueryVo.getBelongCourseId());
        if (Objects.isNull(belongCourse)) {
            throw new GlobalBusinessException(800, "该课程不存在");
        }
        if (belongCourse.getStatus() == 0) {
            throw new GlobalBusinessException(800, "该课程已被禁用");
        }

        IPage<Batch> batchIPage = this.selectPage(pageParam, batchQueryVo);
        List<Batch> records = batchIPage.getRecords();
        // 获取当前登陆的用户
        LoginUser loginUser = (LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        records.forEach(batch -> {
            // 根据所属批次id和当前登陆用户id查询任务表
            QueryWrapper<Task> taskQueryWrapper = new QueryWrapper<>();
            // 设置作业所属批次id
            taskQueryWrapper.eq("belong_batch_id", batch.getId());
            // 设置作业的上传者id
            taskQueryWrapper.eq("uploader_id", loginUser.getUser().getId());
            // 如果在作业表中能查到记录，则说明该批次已提交
            Task task = taskMapper.selectOne(taskQueryWrapper);
            if (Objects.nonNull(task)) {
                // 设置批次为已提交，1为已提交，0为未提交
                batch.setIsCommit(1);
            } else {
                batch.setIsCommit(0);
            }

            // 判断批次的截止时间是否是格林威治时间，是则设置为未截止，否则设置为已截止
            if (Objects.equals(batch.getEndTime(), GMTDate)) {
                batch.setIsEnd(0);
            } else {
                // 判断批次是否已截止，已截止为true
                int compare = batch.getEndTime().compareTo(new Date());
                if (compare <= 0) {
                    // 设置批次的截止状态，1为已截止，0为未截止
                    batch.setIsEnd(1);
                } else {
                    batch.setIsEnd(0);
                }
            }

        });

        // 只看已提交或未提交
//        if (Objects.nonNull(batchQueryVo.getIsCommit())) {
//            if (Objects.equals(batchQueryVo.getIsCommit(), 1)) {
//                records.removeIf(batch -> Objects.equals(batch.getIsCommit(), 0));
//            } else if (Objects.equals(batchQueryVo.getIsCommit(), 0)) {
//                records.removeIf(batch -> Objects.equals(batch.getIsCommit(), 1));
//            }
//        }
//
//        // 只看已结束或未结束
//        if (Objects.nonNull(batchQueryVo.getIsEnd())) {
//            if (Objects.equals(batchQueryVo.getIsEnd(), 1)) {
//                records.removeIf(batch -> Objects.equals(batch.getIsEnd(), 0));
//            } else if (Objects.equals(batchQueryVo.getIsEnd(), 0)) {
//                records.removeIf(batch -> Objects.equals(batch.getIsEnd(), 1));
//            }
//        }

        return batchIPage;
    }

    @Override
    public IPage<Batch> selectPageIsCommitAndCount(Page<Batch> pageParam, BatchQueryVo batchQueryVo) {
        IPage<Batch> batchIPage = this.selectPageIsCommit(pageParam, batchQueryVo);
        List<Batch> records = batchIPage.getRecords();
        records.forEach(batch -> {
            // 根据批次id查询作业表，统计作业数量
            QueryWrapper<Task> taskQueryWrapper = new QueryWrapper<>();
            taskQueryWrapper.eq("belong_batch_id", batch.getId());
            Long personCount = taskMapper.selectCount(taskQueryWrapper);
            batch.setPersonCount(personCount);
            // 查询用户总人数，不包括系统管理员
            Long totalCount = userMapper.selectCount(new QueryWrapper<User>().ne("id",1));
            batch.setTotalCount(totalCount);
        });
        return batchIPage;
    }
}
