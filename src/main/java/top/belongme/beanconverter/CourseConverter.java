package top.belongme.beanconverter;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;
import top.belongme.model.dto.CourseDTO;
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
    public IPage<CourseDTO> convertPage(IPage<Course> source) {
        IPage<CourseDTO> courseDTOIPage = new Page<>();

        BeanUtils.copyProperties(source, courseDTOIPage);

        List<CourseDTO> courseDTOList = new ArrayList<>();
        for(Course course : source.getRecords()) {
            CourseDTO courseDTO = new CourseDTO();
            BeanUtils.copyProperties(course, courseDTO);
            courseDTOList.add(courseDTO);
        }
        courseDTOIPage.setRecords(courseDTOList);
        return courseDTOIPage;
    }
    public List<CourseDTO> convertToDTOList(List<Course> courseList) {
        List<CourseDTO> courseDTOList = new ArrayList<>();
        courseList.forEach(course -> {
            CourseDTO courseDTO = new CourseDTO();
            BeanUtils.copyProperties(course, courseDTO);
            courseDTOList.add(courseDTO);
        });
        return courseDTOList;
    }
    public CourseDTO convertToDTO(Course course) {
        CourseDTO courseDTO = new CourseDTO();
        BeanUtils.copyProperties(course, courseDTO);
        return courseDTO;
    }
}
