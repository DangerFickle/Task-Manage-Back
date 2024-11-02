package top.belongme.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.web.bind.annotation.*;
import top.belongme.model.dto.GroupQueryDTO;
import top.belongme.model.pojo.Group;
import top.belongme.model.pojo.GroupMember;
import top.belongme.model.result.Result;
import top.belongme.service.GroupService;
import top.belongme.utils.LoginUserUtil;

import javax.annotation.Resource;

/**
 * @Title: GroupController
 * @ProjectName Task-Manage-Back
 * @Description: TODO
 * @Author DengChao
 * @Date 2023/12/1715:15
 */
@RestController
@RequestMapping("group")
public class GroupController {
    @Resource
    private GroupService groupService;


    /**
     * TODO 创建群组
     *
     * @Author DengChao
     * @Date 2024/9/26 17:09
     */
    @PostMapping("create")
    public Result<Group> createGroup(String belongCourseId, Integer groupIndex) {
        return groupService.createGroup(belongCourseId, groupIndex);
    }

    /**
     * TODO 根据所属课程和当前用户，获取群组信息
     *
     * @Author DengChao
     * @Date 2023/12/17 15:15
     */
    @GetMapping("/{belongCourse}/{page}/{limit}")
    public Result<Group> getGroup(@PathVariable Long page, @PathVariable Long limit, @PathVariable String belongCourse) {
        Page<GroupMember> pageParam = new Page<>(page, limit);
        return groupService.getGroup(pageParam, belongCourse);
    }

    /**
     * TODO 获取指定群组的成员，除自己
     *
     * @Author DengChao
     * @Date 2024/10/23 15:01
     */
    @GetMapping("/pageGroupMembers")
    public Result<IPage<GroupMember>> getGroupMember(Long page, Long pageSize, String groupId, String name) {
        Page<GroupMember> pageParam = new Page<>(page, pageSize);
        String currentLoginUserId = LoginUserUtil.getCurrentLoginUserId();
        IPage<GroupMember> groupMember = groupService.getGroupMember(pageParam, groupId, currentLoginUserId, name);
        return Result.ok(groupMember);
    }

    /**
     * TODO 组长踢出组内成员
     *
     * @Author DengChao
     * @Date 2023/12/17 20:20
     */
    @DeleteMapping("/kick/{groupId}/{userId}")
    public Result kickOutMember(@PathVariable String groupId, @PathVariable String userId) {
        return groupService.kickOutMember(groupId, userId);
    }


    /**
     * TODO 成员主动退出群组
     *
     * @Author DengChao
     * @Date 2023/12/17 20:19
     */
    @DeleteMapping("/quit/{groupId}")
    public Result kickOutMember(@PathVariable String groupId) {
        return groupService.quit(groupId);
    }

    /**
     * TODO 组长添加成员
     *
     * @Author DengChao
     * @Date 2023/12/17 20:58
     */
    @PostMapping("addMember/{userId}/{groupId}")
    public Result addMember(@PathVariable String userId, @PathVariable String groupId) {
        return groupService.addMember(userId, groupId);
    }

    /**
     * TODO 组长转让群组
     *
     * @Author DengChao
     * @Date 2024/10/23 12:55
     */
    @PutMapping("transferLeader/{transferUserId}/{groupId}")
    public Result transferLeader(@PathVariable String transferUserId, @PathVariable String groupId) {
        return groupService.transferLeader(transferUserId, groupId);
    }


    /**
     * TODO 获取指定批次未提交作业的群组
     *
     * @Author DengChao
     * @Date 2024/10/27 15:35
     */
    @GetMapping("noCommitGroupList/{page}/{pageSize}")
    public Result<IPage<Group>> getNoCommitGroupList(@PathVariable Integer page,
                                                    @PathVariable Integer pageSize,
                                                    GroupQueryDTO groupQueryDTO
                                                    ) {
        IPage<Group> pageParam = new Page<>(page, pageSize);
        IPage<Group> groupPage = groupService.getNoCommitGroupList(pageParam, groupQueryDTO);
        return Result.ok(groupPage);
    }



}
