package top.belongme.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import top.belongme.model.pojo.Course;
import top.belongme.model.dto.CourseQueryDTO;

/**
 * @Title: CourseMapper
 * @ProjectName JobManage-Back
 * @Description: TODO
 * @Author DengChao
 * @Date 2023/2/814:14
 */
public interface CourseMapper extends BaseMapper<Course> {
    IPage<Course> selectPage(Page<Course> pageParam, @Param("dto") CourseQueryDTO courseQueryDTO);
}
