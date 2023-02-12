package top.belongme.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import top.belongme.model.pojo.task.Task;
import top.belongme.model.pojo.task.TaskDetails;
import top.belongme.model.vo.TaskDetailsQueryVo;

/**
 * @Title: TaskDetailsMapper
 * @ProjectName Task-Manage-Back
 * @Description: TODO
 * @Author DengChao
 * @Date 2023/2/1220:11
 */
public interface TaskDetailsMapper extends BaseMapper<TaskDetails> {
    IPage<TaskDetails> selectPage(Page<TaskDetails> pageParam, @Param("vo")TaskDetailsQueryVo taskDetailsQueryVo);
}
