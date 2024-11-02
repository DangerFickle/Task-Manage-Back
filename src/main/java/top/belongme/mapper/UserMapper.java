package top.belongme.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import top.belongme.model.dto.TaskDetailsQueryDTO;
import top.belongme.model.dto.UserDTO;
import top.belongme.model.excel.UserExcel;
import top.belongme.model.pojo.user.User;

import java.util.List;

/**
 * @Title: UserMapper
 * @ProjectName JobManage-Back
 * @Description: TODO
 * @Author DengChao
 * @Date 2023/2/714:44
 */
public interface UserMapper extends BaseMapper<User> {
    IPage<User> getNotCommitUserList(Page<User> pageParam, @Param("dto") TaskDetailsQueryDTO taskDetailsQueryDTO);

    List<UserExcel> getNotCommitUserListExcel(@Param("dto") TaskDetailsQueryDTO taskDetailsQueryDTO);

    IPage<User> getUserWithoutGroupMember(Page<User> pageParam, @Param("dto") UserDTO userDTO);
}
