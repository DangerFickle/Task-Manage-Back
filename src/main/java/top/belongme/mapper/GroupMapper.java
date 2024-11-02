package top.belongme.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import top.belongme.model.dto.GroupQueryDTO;
import top.belongme.model.pojo.Group;
import top.belongme.model.pojo.GroupMember;

/**
 * @Title: GroupMapper
 * @ProjectName Task-Manage-Back
 * @Description: TODO
 * @Author DengChao
 * @Date 2023/12/1715:13
 */
public interface GroupMapper extends BaseMapper<Group> {
    Group getGroup(@Param("belongCourse") String belongCourse, @Param("userId") String userId);

    IPage<GroupMember> getGroupMember(IPage<GroupMember> page, @Param("groupId") String groupId, @Param("userId") String userId, @Param("name") String name);

    String getGroupId(@Param("courseId") String courseId, @Param("userId") String userId);

    IPage<Group> selectNoCommitGroupList(IPage<Group> pageParam, @Param("dto") GroupQueryDTO groupQueryDTO);
}
