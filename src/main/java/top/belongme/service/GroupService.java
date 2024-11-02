package top.belongme.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import top.belongme.model.dto.GroupQueryDTO;
import top.belongme.model.pojo.Group;
import top.belongme.model.pojo.GroupMember;
import top.belongme.model.result.Result;

/**
 * @Title: GroupService
 * @ProjectName Task-Manage-Back
 * @Description: TODO
 * @Author DengChao
 * @Date 2023/12/1715:13
 */
public interface GroupService extends IService<Group> {
    Result<Group> getGroup(IPage<GroupMember> page, String belongCourse);

    IPage<GroupMember> getGroupMember(IPage<GroupMember> page, String groupId, String userId, String name);

    Result kickOutMember(String groupId, String userId);

    Result quit(String groupId);

    Result addMember(String userId, String groupId);

    Result<Group> createGroup(String belongCourseId, Integer groupIndex);

    Result transferLeader(String transferUserId, String groupId);

    IPage<Group> getNoCommitGroupList(IPage<Group> pageParam, GroupQueryDTO groupQueryDTO);
}
