package top.belongme.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import top.belongme.model.dto.TaskDetailsQueryDTO;
import top.belongme.model.pojo.task.GroupTaskDetail;
import top.belongme.model.pojo.task.PersonalTaskDetail;

/**
 * @Title: TaskDetailsMapper
 * @ProjectName Task-Manage-Back
 * @Description: TODO
 * @Author DengChao
 * @Date 2023/2/1220:11
 */
public interface TaskDetailsMapper extends BaseMapper<PersonalTaskDetail> {
    IPage<PersonalTaskDetail> selectPage(Page<PersonalTaskDetail> pageParam, @Param("dto") TaskDetailsQueryDTO taskDetailsQueryDTO);

    IPage<GroupTaskDetail> selectGroupPage(Page<GroupTaskDetail> pageParam, @Param("dto") TaskDetailsQueryDTO taskDetailsQueryDTO);
}
