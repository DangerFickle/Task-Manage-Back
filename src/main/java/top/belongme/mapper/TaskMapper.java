package top.belongme.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import top.belongme.model.pojo.Course;
import top.belongme.model.pojo.Task;
import top.belongme.model.vo.CourseQueryVo;
import top.belongme.model.vo.TaskQueryVo;

/**
 * @Title: TaskMapper
 * @ProjectName JobManage-Back
 * @Description: TODO
 * @Author DengChao
 * @Date 2023/2/1018:17
 */
public interface TaskMapper extends BaseMapper<Task> {
    IPage<Task> selectPage(Page<Task> pageParam, @Param("vo") TaskQueryVo taskQueryVo);
}
