package top.belongme.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import top.belongme.exception.GlobalBusinessException;
import top.belongme.mapper.BatchMapper;
import top.belongme.mapper.CourseMapper;
import top.belongme.mapper.TaskDetailsMapper;
import top.belongme.model.pojo.Batch;
import top.belongme.model.pojo.task.TaskDetails;
import top.belongme.model.vo.TaskDetailsQueryVo;
import top.belongme.service.TaskDetailsService;

import javax.annotation.Resource;
import java.util.Date;
import java.util.Objects;

/**
 * @Title: TaskDetailsServiceImpl
 * @ProjectName Task-Manage-Back
 * @Description: TODO
 * @Author DengChao
 * @Date 2023/2/1220:08
 */
@Service
public class TaskDetailsServiceImpl extends ServiceImpl<TaskDetailsMapper, TaskDetails> implements TaskDetailsService {
    @Resource
    private Date GMTDate;

    @Resource
    private CourseMapper courseMapper;

    @Resource
    private BatchMapper batchMapper;

    @Override
    public IPage<TaskDetails> selectPage(Page<TaskDetails> pageParam, TaskDetailsQueryVo taskDetailsQueryVo) {
        // 查询该批次是否存在
        Batch belongBatch = batchMapper.selectById(taskDetailsQueryVo.getBelongBatchId());
        if (Objects.isNull(belongBatch)) {
            throw new GlobalBusinessException(800, "该批次不存在");
        }

        IPage<TaskDetails> taskDetailsIPage = baseMapper.selectPage(pageParam, taskDetailsQueryVo);
        // 遍历并判断所属批次是否已经截止
        taskDetailsIPage.getRecords().forEach(taskDetails -> {
            // 如果所属批次截止时间是格林威治时间，说明是永久有效的，设置为未截止
            if (taskDetails.getBelongBatchEndTime().equals(GMTDate)) {
                taskDetails.setIsEnd(0);
            } else {
                // 否则，判断是否已经截止
                int compareTo = taskDetails.getBelongBatchEndTime().compareTo(new Date());
                if (compareTo <= 0) {
                    taskDetails.setIsEnd(1);
                } else {
                    taskDetails.setIsEnd(0);
                }
            }
        });

        return taskDetailsIPage;
    }
}
