package top.belongme.beanconverter;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;
import top.belongme.model.vo.CourseVO;
import top.belongme.model.pojo.Course;

import java.util.ArrayList;
import java.util.List;

/**
 * @Title: CourseConverter
 * @ProjectName Task-Manage-Back
 * @Description: TODO
 * @Author DengChao
 * @Date 2023/5/20:39
 */
@Component
public class CourseConverter {
    public IPage<CourseVO> convertPage(IPage<Course> source) {
        IPage<CourseVO> courseDTOIPage = new Page<>();

        BeanUtils.copyProperties(source, courseDTOIPage);

        List<CourseVO> courseVOList = new ArrayList<>();
        for(Course course : source.getRecords()) {
            CourseVO courseVO = new CourseVO();
            BeanUtils.copyProperties(course, courseVO);
            courseVOList.add(courseVO);
        }
        courseDTOIPage.setRecords(courseVOList);
        return courseDTOIPage;
    }
    public List<CourseVO> convertToDTOList(List<Course> courseList) {
        List<CourseVO> courseVOList = new ArrayList<>();
        courseList.forEach(course -> {
            CourseVO courseVO = new CourseVO();
            BeanUtils.copyProperties(course, courseVO);
            courseVOList.add(courseVO);
        });
        return courseVOList;
    }
    public CourseVO convertToDTO(Course course) {
        CourseVO courseVO = new CourseVO();
        BeanUtils.copyProperties(course, courseVO);
        return courseVO;
    }
}
