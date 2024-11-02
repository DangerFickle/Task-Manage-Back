package top.belongme.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.belongme.constant.Constants;
import top.belongme.exception.GlobalBusinessException;
import top.belongme.mapper.*;
import top.belongme.model.dto.BatchQueryDTO;
import top.belongme.model.pojo.Batch;
import top.belongme.model.pojo.Course;
import top.belongme.model.pojo.Group;
import top.belongme.model.pojo.task.Task;
import top.belongme.model.pojo.user.LoginUser;
import top.belongme.model.pojo.user.User;
import top.belongme.model.result.Result;
import top.belongme.service.BatchService;
import top.belongme.utils.LoginUserUtil;

import javax.annotation.Resource;
import java.io.File;
import java.util.*;

/**
 * @Title: BatchServiceImpl
 * @ProjectName JobManage-Back
 * @Description: TODO
 * @Author DengChao
 * @Date 2023/2/915:30
 */
@Service
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class BatchServiceImpl extends ServiceImpl<BatchMapper, Batch> implements BatchService {

    @Resource
    private CourseMapper courseMapper;

    @Resource
    private TaskMapper taskMapper;

    @Resource
    private UserMapper userMapper;

    @Resource
    private String fileSaveLocation;

    @Resource
    private Date GMTDate;

    private Course oldBelongCourse;
    @Autowired
    private GroupMapper groupMapper;


    /**
     * TODO 分页查询批次
     *
     * @Author DengChao
     * @Date 2023/2/9 22:47
     */
    @Override
    public IPage<Batch> selectPage(Page<Batch> pageParam, BatchQueryDTO batchQueryDTO) {
        Course course = courseMapper.selectById(batchQueryDTO.getBelongCourseId());

        if (Objects.isNull(course)) {
            log.info("查询失败，因为传入的所属课程不存在");
            throw new GlobalBusinessException(800, "该课程不存在");
        }

        if (course.getStatus() == 0) {
            log.info("查询失败，因为传入的所属课程已经被禁用");
            throw new GlobalBusinessException(800, "该课程已被禁用");
        }

        IPage<Batch> batchIPage = baseMapper.selectPage(pageParam, batchQueryDTO);
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
     * TODO 添加批次
     *
     * @Author DengChao
     * @Date 2023/2/9 16:11
     */
    @Override
    public Result addBatch(Batch batch) {
        QueryWrapper<Batch> qw = new QueryWrapper<Batch>()
                .eq("batch_name", batch.getBatchName())
                .eq("belong_course_id", batch.getBelongCourseId())
                .eq("batch_type", batch.getBatchType());
        Batch oldBatch = this.getOne(qw);
        if (Objects.nonNull(oldBatch)) {
            log.error("批次创建失败，因为即将添加的批次已经存在");
            throw new GlobalBusinessException(800, "批次已存在");
        }

        // 获取所属课程
        Course course = courseMapper.selectById(batch.getBelongCourseId());
        if (Objects.isNull(course)) {
            log.error("批次创建失败，因为批次所属的课程不存在");
            throw new GlobalBusinessException(800, "课程不存在");
        }

        // 获取当前登录用户
        LoginUser loginUser = LoginUserUtil.getCurrentLoginUser();
        // 设置创建者id
        batch.setCreatorId(loginUser.getUser().getId());
        // 设置修改者id
        batch.setModifierId(loginUser.getUser().getId());
        int insert = baseMapper.insert(batch);
        if (insert > 0) {
            // 打印日志
            log.info("管理员【{}】，在【{}】课程下创建了一个批次：【{}】", loginUser.getUser().getName(), course.getCourseName(), batch.getBatchName());
            return new Result(200, "批次添加成功");
        } else {
            log.error("批次创建失败，因为向数据库中插入记录失败");
            throw new GlobalBusinessException(800, "批次添加失败");
        }
    }

    /**
     * TODO 修改批次文件夹名称，修改批次所属课程
     *
     * @Author DengChao
     * @Date 2023/2/9 21:55
     */
    @Override
    public Result updateBatch(Batch batch) {
        // 获取原批次信息，判断批次是否存在
        Batch oldBatch = baseMapper.selectById(batch.getId());
        if (Objects.isNull(oldBatch)) {
            log.error("更新批次失败，批次不存在");
            throw new GlobalBusinessException(800, "批次不存在");
        }

        Course course = courseMapper.selectById(oldBatch.getBelongCourseId());

        // 获取当前登录用户
        LoginUser user = LoginUserUtil.getCurrentLoginUser();

        boolean update = this.updateById(batch);
        if (update) {
            log.info("管理员【{}】，修改了【{}】课程下的【{}】批次，原信息：{}，新信息：{}", user.getUser().getName(), course.getCourseName(), oldBatch.getBatchName(), JSON.toJSON(oldBatch), JSON.toJSON(batch));
            return new Result(200, "批次修改成功");
        } else {
            log.error("更新批次失败，因为向数据库中更新批次失败");
            throw new GlobalBusinessException(800, "批次修改失败");
        }

    }

    @Override
    public Result deleteBatch(String batchId) {
        // 获取批次信息
        Batch batch = this.getById(batchId);
        if (Objects.isNull(batch)) {
            log.error("批次删除失败，因为传入的批次不存在");
            throw new GlobalBusinessException(800, "该批次不存在");
        }
        // 获取该批次下的作业，如果存在未删除的作业，则无法删除该批次
        Long taskCount = taskMapper.selectCount(new QueryWrapper<Task>().eq("belong_batch_id", batch.getId()));
        if (taskCount > 0) {
            log.error("批次删除失败，因为该批次下存在未删除的作业");
            throw new GlobalBusinessException(800, "该批次下存在未删除的作业");
        }

        boolean delete = this.removeById(batchId);
        if (delete) {
            // 获取当前登陆的用户
            LoginUser loginUser = LoginUserUtil.getCurrentLoginUser();
            // 获取批次的所属课程
            Course course = courseMapper.selectById(batch.getBelongCourseId());
            log.info("管理员【{}】，删除了【{}】课程下的【{}】批次", loginUser.getUser().getName(), course.getCourseName(), batch.getBatchName());
            return new Result(200, "批次删除成功");
        } else {
            log.error("批次删除失败，因为数据库中的批次记录删除失败");
            throw new GlobalBusinessException(800, "批次删除失败");
        }

    }

    @Override
    public Result updateStatus(String batchId, Integer status) {
        Batch oldBatch = this.getById(batchId);
        if (Objects.isNull(oldBatch)) {
            throw new GlobalBusinessException(800, "该批次不存在");
        }

        // 判断原批次是否已截止
        if (oldBatch.getEndTime().equals(GMTDate)) {
            oldBatch.setIsEnd(Constants.NO);
        } else {
            if (oldBatch.getEndTime().compareTo(new Date()) <= 0) {
                oldBatch.setIsEnd(Constants.YES);
            } else {
                oldBatch.setIsEnd(Constants.NO);
            }
        }

        // 如果要更改的状态刚好是当前批次的状态，则不修改
        if (Objects.equals(status, oldBatch.getIsEnd())) {
            throw new GlobalBusinessException(800, "批次状态同步失败");
        }

        Batch batch = new Batch();
        batch.setId(batchId);
        // 如果要切换成截止状态，则设置截止时间为当前时间
        if (Objects.equals(status, Constants.YES)) {
            batch.setEndTime(new Date());
        } else if (Objects.equals(status, Constants.NO)) {
            // 如果要切换成未截止状态，则设置截止时间为格林威治时间
            batch.setEndTime(GMTDate);
        }

        // 获取当前登录用户
        LoginUser loginUser = LoginUserUtil.getCurrentLoginUser();
        // 设置修改者id
        batch.setModifierId(loginUser.getUser().getId());
        boolean update = this.updateById(batch);
        if (update) {
            // 获取批次所属课程
            Course course = courseMapper.selectById(oldBatch.getBelongCourseId());
            log.info("管理员【{}】，更改了【{}】课程下【{}】批次的状态为【{}】", loginUser.getUser().getName(), course.getCourseName(), oldBatch.getBatchName(), status == 1 ? "已截止" : "未截止");
            return new Result(200, "批次状态更新成功");
        } else {
            throw new GlobalBusinessException(800, "批次状态更新失败");
        }
    }

    /**
     * TODO 用户提交作业时，查询各个批次的提交状态和截止状态
     *
     * @Author DengChao
     * @Date 2023/10/29 14:34
     */
    @Override
    public IPage<Batch> selectPageIsCommit(Page<Batch> pageParam, BatchQueryDTO batchQueryDTO) {
        Course belongCourse = courseMapper.selectById(batchQueryDTO.getBelongCourseId());
        if (Objects.isNull(belongCourse)) {
            log.error("查询是否已提交时失败，因为传入的课程不存在");
            throw new GlobalBusinessException(800, "该课程不存在");
        }
        if (Objects.equals(belongCourse.getStatus(), Constants.NO)) {
            log.error("查询是否已提交时失败，因为传入的课程已被禁用");
            throw new GlobalBusinessException(800, "该课程已被禁用");
        }

        IPage<Batch> batchIPage = this.selectPage(pageParam, batchQueryDTO);

        String uploaderId = LoginUserUtil.getCurrentLoginUserId();
        if ("group".equals(batchQueryDTO.getBatchType())) {
            uploaderId = groupMapper.getGroupId(belongCourse.getId(), uploaderId);
        }

        for (Batch batch : batchIPage.getRecords()) {
            // 根据所属批次id和当前登陆用户id查询作业表
            QueryWrapper<Task> taskQueryWrapper = new QueryWrapper<>();
            // 设置作业所属批次id
            taskQueryWrapper.eq("belong_batch_id", batch.getId());
            // 设置作业的上传者id
            taskQueryWrapper.eq("uploader_id", uploaderId);
            // 如果在作业表中能查到记录，则说明该批次已提交
            Long taskCount = taskMapper.selectCount(taskQueryWrapper);
            if (taskCount > 0) {
                // 设置批次为已提交，1为已提交，0为未提交
                batch.setIsCommit(Constants.YES);
            } else {
                batch.setIsCommit(Constants.NO);
            }

            // 判断批次的截止时间是否是格林威治时间，是则设置为未截止，否则设置为已截止
            if (Objects.equals(batch.getEndTime(), GMTDate)) {
                batch.setIsEnd(Constants.NO);
            } else {
                // 判断批次是否已截止，已截止为true
                int compare = batch.getEndTime().compareTo(new Date());
                if (compare <= Constants.NO) {
                    // 设置批次的截止状态，1为已截止，0为未截止
                    batch.setIsEnd(Constants.YES);
                } else {
                    batch.setIsEnd(Constants.NO);
                }
            }
        }

//        records.forEach(batch -> {
//        });

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

    /**
     * TODO 管理员查看提交详情时，查询批次列表，包括已提交人数和总人数
     *
     * @Author DengChao
     * @Date 2023/10/29 14:32
     */
    @Override
    public IPage<Batch> selectPageIsCommitAndCount(Page<Batch> pageParam, BatchQueryDTO batchQueryDTO) {
        IPage<Batch> batchIPage = this.selectPageIsCommit(pageParam, batchQueryDTO);
        List<Batch> records = batchIPage.getRecords();
        records.forEach(batch -> {

            // 根据批次id查询作业表，统计作业数量
            QueryWrapper<Task> taskQW = new QueryWrapper<>();
            taskQW.eq("belong_batch_id", batch.getId());
            // 设置已交数
            batch.setAlreadyCount(taskMapper.selectCount(taskQW));


            // 查询用户总人数，不包括系统管理员
            Long totalCount = userMapper.selectCount(new QueryWrapper<User>().ne("id", 1));
            if ("group".equals(batchQueryDTO.getBatchType())) {
                // 群组批次，则将总人数改为总群数
                totalCount = groupMapper.selectCount(new QueryWrapper<Group>().eq("belong_course", batchQueryDTO.getBelongCourseId()));
            }
            // 设置总数
            batch.setTotalCount(totalCount);

            // 获取总文件夹
            File fileFolder = new File(fileSaveLocation);
            // 获取批次下所有的作业记录
            List<Task> taskList = taskMapper.selectList(taskQW);
            // 获取所有的文件名
            List<String> fileNameList = taskList.stream().map((task -> task.getFileSha256() + task.getFileType())).toList();
            // 获取文件夹下所有的fileNameList中对应的文件
            List<File> fileList = new ArrayList<>();
            fileNameList.forEach(fileName -> {
                File file = fileFolder.listFiles((dir, name) -> name.equals(fileName))[0];
                fileList.add(file);
            });

            // 统计所有文件的大小
            long sizeOfFiles = 0;
            if (fileList.size() != 0) {
                sizeOfFiles = fileList.stream().mapToLong(File::length).sum();
            }
            // 设置文件夹大小
            batch.setSizeOfDirectory(sizeOfFiles);
        });
        return batchIPage;
    }
}
