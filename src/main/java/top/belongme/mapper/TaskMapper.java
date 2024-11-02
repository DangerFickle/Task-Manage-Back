package top.belongme.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import top.belongme.model.pojo.Group;
import top.belongme.model.pojo.task.Task;
import top.belongme.model.dto.TaskQueryDTO;
import top.belongme.model.pojo.user.User;

import java.util.List;
import java.util.Map;

/**
 * @Title: TaskMapper
 * @ProjectName JobManage-Back
 * @Description: TODO
 * @Author DengChao
 * @Date 2023/2/1018:17
 */
public interface TaskMapper extends BaseMapper<Task> {
    IPage<Task> selectPage(Page<Task> pageParam, @Param("dto") TaskQueryDTO taskQueryDTO);

    List<User> getPersonalTaskUploaders(String belongBatchId);

    List<Group> getGroupTaskUploaders(String belongBatchId);
}
