package top.belongme.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import top.belongme.model.pojo.user.User;
import top.belongme.model.excel.UserExcel;
import top.belongme.model.vo.TaskDetailsQueryVo;

import java.util.List;

/**
 * @Title: UserMapper
 * @ProjectName JobManage-Back
 * @Description: TODO
 * @Author DengChao
 * @Date 2023/2/714:44
 */
public interface UserMapper extends BaseMapper<User> {
    IPage<User> getNotCommitUserList(Page<User> pageParam, @Param("vo") TaskDetailsQueryVo taskDetailsQueryVo);
    List<UserExcel> getNotCommitUserListExcel(@Param("vo") TaskDetailsQueryVo taskDetailsQueryVo);
}
