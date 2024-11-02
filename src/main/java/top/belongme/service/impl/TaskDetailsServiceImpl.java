package top.belongme.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Service;
import top.belongme.exception.GlobalBusinessException;
import top.belongme.mapper.TaskDetailsMapper;
import top.belongme.model.pojo.Batch;
import top.belongme.model.pojo.task.GroupTaskDetail;
import top.belongme.model.pojo.task.PersonalTaskDetail;
import top.belongme.model.dto.TaskDetailsQueryDTO;
import top.belongme.service.BatchService;
import top.belongme.service.TaskDetailsService;

import javax.annotation.Resource;
import java.io.File;
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
@Slf4j
public class TaskDetailsServiceImpl extends ServiceImpl<TaskDetailsMapper, PersonalTaskDetail> implements TaskDetailsService {
    @Resource
    private Date GMTDate;

    @Resource
    private BatchService batchService;

    @Resource
    private String fileSaveLocation;

    @Override
    public IPage<PersonalTaskDetail> selectPage(Page<PersonalTaskDetail> pageParam, TaskDetailsQueryDTO taskDetailsQueryDTO) {
        // 查询该批次是否存在
        Batch belongBatch = batchService.getById(taskDetailsQueryDTO.getBelongBatchId());
        if (Objects.isNull(belongBatch)) {
            log.error("作业详情查询失败，因为传入的批次不存在");
            throw new GlobalBusinessException(800, "该批次不存在");
        }

        IPage<PersonalTaskDetail> taskDetailsIPage = baseMapper.selectPage(pageParam, taskDetailsQueryDTO);
        // 遍历并判断所属批次是否已经截止
        taskDetailsIPage.getRecords().forEach(personalTaskDetail -> {
            // 如果所属批次截止时间是格林威治时间，说明是永久有效的，设置为未截止
            if (personalTaskDetail.getBelongBatchEndTime().equals(GMTDate)) {
                personalTaskDetail.setIsEnd(0);
            } else {
                // 否则，判断是否已经截止
                int compareTo = personalTaskDetail.getBelongBatchEndTime().compareTo(new Date());
                if (compareTo <= 0) {
                    personalTaskDetail.setIsEnd(1);
                } else {
                    personalTaskDetail.setIsEnd(0);
                }
            }

            // 获取作业文件
            File taskFile = new File(fileSaveLocation + personalTaskDetail.getFileSha256() + personalTaskDetail.getFileType());
            if (taskFile.exists()) {
                // 获取作业文件大小
                long taskFileSize = FileUtils.sizeOf(taskFile);
                // 设置作业文件大小
                personalTaskDetail.setFileSize(taskFileSize);
            }
        });
        return taskDetailsIPage;
    }

    @Override
    public IPage<GroupTaskDetail> selectGroupPage(Page<GroupTaskDetail> pageParam, TaskDetailsQueryDTO taskDetailsQueryDTO) {
        // 查询该批次是否存在
        Batch belongBatch = batchService.getById(taskDetailsQueryDTO.getBelongBatchId());
        if (Objects.isNull(belongBatch)) {
            log.error("作业详情查询失败，因为传入的批次不存在");
            throw new GlobalBusinessException(800, "该批次不存在");
        }
        IPage<GroupTaskDetail> taskDetailIPage = baseMapper.selectGroupPage(pageParam, taskDetailsQueryDTO);
        taskDetailIPage.getRecords().forEach(groupTaskDetail -> {
            // 如果所属批次截止时间是格林威治时间，说明是永久有效的，设置为未截止
            if (groupTaskDetail.getBelongBatchEndTime().equals(GMTDate)) {
                groupTaskDetail.setIsEnd(0);
            } else {
                // 否则，判断是否已经截止
                int compareTo = groupTaskDetail.getBelongBatchEndTime().compareTo(new Date());
                if (compareTo <= 0) {
                    groupTaskDetail.setIsEnd(1);
                } else {
                    groupTaskDetail.setIsEnd(0);
                }
            }

            // 获取作业文件
            File taskFile = new File(fileSaveLocation + groupTaskDetail.getFileSha256() + groupTaskDetail.getFileType());
            if (taskFile.exists()) {
                // 获取作业文件大小
                long taskFileSize = FileUtils.sizeOf(taskFile);
                // 设置作业文件大小
                groupTaskDetail.setFileSize(taskFileSize);
            }
        });
        return taskDetailIPage;
    }


}
