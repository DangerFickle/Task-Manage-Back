package top.belongme.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import top.belongme.model.pojo.user.User;

/**
 * @Title: UserMapper
 * @ProjectName JobManage-Back
 * @Description: TODO
 * @Author DengChao
 * @Date 2023/2/714:44
 */
public interface UserMapper extends BaseMapper<User> {
    String getUserByCreatorId(@Param("creatorId") Long creatorId);
}
