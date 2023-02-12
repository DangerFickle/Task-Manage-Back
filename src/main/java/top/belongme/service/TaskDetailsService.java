package top.belongme.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;
import top.belongme.model.pojo.task.Task;
import top.belongme.model.pojo.task.TaskDetails;
import top.belongme.model.vo.TaskDetailsQueryVo;

/**
 * @Title: TaskDetailsService
 * @ProjectName Task-Manage-Back
 * @Description: TODO
 * @Author DengChao
 * @Date 2023/2/1220:08
 */
public interface TaskDetailsService extends IService<TaskDetails> {
    IPage<TaskDetails> selectPage(Page<TaskDetails> pageParam, @Param("vo") TaskDetailsQueryVo taskDetailsQueryVo);
}
