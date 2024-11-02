package top.belongme.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import top.belongme.model.pojo.task.GroupTaskDetail;
import top.belongme.model.pojo.task.PersonalTaskDetail;
import top.belongme.model.dto.TaskDetailsQueryDTO;

/**
 * @Title: TaskDetailsService
 * @ProjectName Task-Manage-Back
 * @Description: TODO
 * @Author DengChao
 * @Date 2023/2/1220:08
 */
public interface TaskDetailsService extends IService<PersonalTaskDetail> {
    IPage<PersonalTaskDetail> selectPage(Page<PersonalTaskDetail> pageParam, TaskDetailsQueryDTO taskDetailsQueryDTO);

    IPage<GroupTaskDetail> selectGroupPage(Page<GroupTaskDetail> pageParam, TaskDetailsQueryDTO taskDetailsQueryDTO);
}
