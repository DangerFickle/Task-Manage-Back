package top.belongme.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import top.belongme.model.pojo.Menu;

import java.util.List;

/**
 * @Title: MenuMapper
 * @ProjectName JobManage-Back
 * @Description: TODO
 * @Author DengChao
 * @Date 2023/2/716:27
 */
public interface MenuMapper extends BaseMapper<Menu> {
    List<Menu> getMenuListByUserId(@Param("userId") String userId);
}
