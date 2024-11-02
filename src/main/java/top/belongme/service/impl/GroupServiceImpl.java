package top.belongme.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.belongme.exception.GlobalBusinessException;
import top.belongme.mapper.BatchMapper;
import top.belongme.mapper.CourseMapper;
import top.belongme.mapper.GroupMapper;
import top.belongme.mapper.TaskMapper;
import top.belongme.model.dto.GroupQueryDTO;
import top.belongme.model.pojo.*;
import top.belongme.model.pojo.task.Task;
import top.belongme.model.pojo.user.User;
import top.belongme.model.result.Result;
import top.belongme.service.GroupService;
import top.belongme.service.UserGroupService;
import top.belongme.service.UserService;
import top.belongme.utils.LoginUserUtil;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

/**
 * @Title: GroupServiceImpl
 * @ProjectName Task-Manage-Back
 * @Description: TODO
 * @Author DengChao
 * @Date 2023/12/1715:13
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class GroupServiceImpl extends ServiceImpl<GroupMapper, Group> implements GroupService {

    @Resource
    private UserService userService;

    @Resource
    private UserGroupService userGroupService;
    @Autowired
    private CourseMapper courseMapper;
    @Autowired
    private GroupMapper groupMapper;
    @Autowired
    private TaskMapper taskMapper;
    @Autowired
    private BatchMapper batchMapper;

    @Override
    public Result<Group> getGroup(IPage<GroupMember> page, String belongCourse) {
        String userId = LoginUserUtil.getCurrentLoginUser().getUser().getId();
        Group group = baseMapper.getGroup(belongCourse, userId);
        if (Objects.isNull(group)) {
            throw new GlobalBusinessException(800, "此课程还未加入群组");
        }
        group.setMembers(getGroupMember(page, group.getId(), userId, ""));
        return Result.ok(group);
    }

    @Override
    public IPage<GroupMember> getGroupMember(IPage<GroupMember> page, String groupId, String userId, String name) {
        return baseMapper.getGroupMember(page, groupId, userId, name);
    }

    @Override
    public Result kickOutMember(String groupId, String userId) {
        // 检查群组是否存在
        Group groupArg = new Group();
        groupArg.setId(groupId);
        Group group = this.getOne(Wrappers.query(groupArg));
        if (Objects.isNull(group)) {
            throw new GlobalBusinessException(800, "该群组不存在");
        }

        // 检查当前登陆的用户是否为此群组的组长
        User loginUser = LoginUserUtil.getCurrentLoginUser().getUser();
        if (!Objects.equals(group.getLeader(), loginUser.getId())) {
            throw new GlobalBusinessException(800, "您不是组长");
        }

        // 检查要提出的用户是否存在
        User userArg = new User();
        userArg.setId(userId);
        long userCount = userService.count(Wrappers.query(userArg));
        if (userCount <= 0) {
            throw new GlobalBusinessException(800, "该用户不存在");
        }

        // 不能踢出自己
        if (userId.equals(loginUser.getId())) {
            throw new GlobalBusinessException(800, "不能踢出自己");
        }

        // 移除该成员
        UserGroup userGroupArg = new UserGroup(userId, groupId);
        // 检查此记录是否存在
        UserGroup userGroup = userGroupService.getOne(Wrappers.query(userGroupArg));
        if (Objects.isNull(userGroup)) {
            throw new GlobalBusinessException(800, "该成员未在此群组中");
        }

        boolean remove = userGroupService.removeById(userGroup.getId());
        if (!remove) {
            throw new GlobalBusinessException(800, "踢出用户失败");
        }

        group.setAlreadyMember(group.getAlreadyMember() - 1);
        boolean update = this.updateById(group);
        if (!update) {
            throw new GlobalBusinessException(800, "修改群组已有人数失败");
        }

        return Result.ok();
    }

    @Override
    public Result quit(String groupId) {
        // 检查群组是否存在
        Group groupArg = new Group();
        groupArg.setId(groupId);
        Group group = this.getOne(Wrappers.query(groupArg));
        if (Objects.isNull(group)) {
            throw new GlobalBusinessException(800, "该群组不存在");
        }

        // 检查该课程下是否有未删除的作业
        QueryWrapper<Task> qw = new QueryWrapper<>();
        qw.eq("uploader_id", group.getId());
        Long groupTask = taskMapper.selectCount(qw);
        if (groupTask > 0) {
            throw new GlobalBusinessException(800, "群组在该课程还有未删除的作业");
        }

        // 如果是群主，则检查当前群组是否只有一个人
        User loginUser = LoginUserUtil.getCurrentLoginUser().getUser();
        if (Objects.equals(group.getLeader(), loginUser.getId())) {
            if (group.getAlreadyMember() >= 2) {
                throw new GlobalBusinessException(800, "群组人数大于1人不可退出");
            }
            // 退出群组
            UserGroup userGroupArg = new UserGroup(loginUser.getId(), groupId);
            boolean remove = userGroupService.remove(Wrappers.query(userGroupArg));
            if (!remove) {
                throw new GlobalBusinessException(800, "退出群组失败");
            }
            // 删除群组
            boolean isRemoved = this.removeById(group.getId());
            if (!isRemoved) {
                throw new GlobalBusinessException(800, "群组删除失败");
            }
        } else {
            //不是群组直接退出
            UserGroup userGroupArg = new UserGroup(loginUser.getId(), groupId);
            boolean remove = userGroupService.remove(Wrappers.query(userGroupArg));
            if (!remove) {
                throw new GlobalBusinessException(800, "退出群组失败");
            }
            group.setAlreadyMember(group.getAlreadyMember() - 1);
            boolean update = this.updateById(group);
            if (!update) {
                throw new GlobalBusinessException(800, "修改群组已有人数失败");
            }
        }

        return Result.ok();
    }

    @Override
    public Result addMember(String userId, String groupId) {
        // 检查群组是否存在
        Group groupArg = new Group();
        groupArg.setId(groupId);
        Group group = this.getOne(Wrappers.query(groupArg));
        if (Objects.isNull(group)) {
            throw new GlobalBusinessException(800, "该群组不存在");
        }

        // 检查当前登陆的用户是否为此群组的组长
        User loginUser = LoginUserUtil.getCurrentLoginUser().getUser();
        if (!Objects.equals(group.getLeader(), loginUser.getId())) {
            throw new GlobalBusinessException(800, "您不是该群组的组长");
        }

        // 检查是否已经达到了限制人数
        Course course = courseMapper.selectById(group.getBelongCourse());
        if(group.getAlreadyMember().equals(course.getGroupMaxMemberSize())) {
            throw new GlobalBusinessException(800, "群组人数已达上限");
        }

        // 检查目标用户是否存在
        User userArg = new User();
        userArg.setId(userId);
        long count = userService.count(Wrappers.query(userArg));
        if (count <= 0) {
            throw new GlobalBusinessException(800, "您添加的用户不存在");
        }

        // 检查要添加的用户是否已经在群组中了
        UserGroup userGroupArg = new UserGroup(userId, groupId);

        long userGroupCount = userGroupService.count(Wrappers.query(userGroupArg));
        if (userGroupCount > 0) {
            throw new GlobalBusinessException(800, "该用户已在群组中");
        }

        // 添加用户到群组
        boolean save = userGroupService.save(userGroupArg);
        if (!save) {
            throw new GlobalBusinessException(800, "添加用户失败");
        }
        // 将群组已有人员+1
        group.setAlreadyMember(group.getAlreadyMember() + 1);
        boolean update = this.updateById(group);
        if (!update) {
            throw new GlobalBusinessException(800, "修改群组已有人数失败");
        }
        return Result.ok();
    }

    @Override
    public Result<Group> createGroup(String belongCourseId, Integer groupIndex) {
        Course course = courseMapper.selectById(belongCourseId);
        if (Objects.isNull(course)) {
            throw new GlobalBusinessException(800, "课程不存在");
        }

        // 判断当前课程的群组数量是否已经上限
        QueryWrapper<Group> qw = new QueryWrapper<>();
        qw.eq("belong_course", course.getId());
        Long currentGroupCount = groupMapper.selectCount(qw);
        if(course.getGroupMaxSize() == currentGroupCount.intValue()) {
            throw new GlobalBusinessException(800, "该课程群组数量已达上限");
        }

        if (groupIndex > course.getGroupMaxSize()) {
            throw new GlobalBusinessException(800, "组数不可超过" + course.getGroupMaxSize());
        }

        QueryWrapper<Group> wr = new QueryWrapper<>();
        wr.eq("name", groupIndex + "组");
        Long group = groupMapper.selectCount(wr);
        if (group > 0) {
            throw new GlobalBusinessException(800, "已经有" + groupIndex + "组了");
        }

        String currentLoginUserId = LoginUserUtil.getCurrentLoginUserId();
        // 检查是否在该课程已经加过群组
        String groupId = baseMapper.getGroupId(belongCourseId, currentLoginUserId);
        if (StringUtils.isNotBlank(groupId)) {
            throw new GlobalBusinessException(800, "您在该课程已有群组");
        }

        Group groupArgs = new Group();
        groupArgs.setName(groupIndex + "组");
        groupArgs.setLeader(currentLoginUserId);
        groupArgs.setBelongCourse(belongCourseId);
        groupArgs.setAlreadyMember(1);
        boolean save = this.save(groupArgs);
        if (!save) {
            throw new GlobalBusinessException(408, "创建群组失败！");
        }
        // 写入用户群组关系表
        UserGroup userGroup = new UserGroup();
        userGroup.setGroupId(groupArgs.getId());
        userGroup.setUserId(currentLoginUserId);
        userGroupService.save(userGroup);
        return Result.ok(groupArgs);
    }

    @Override
    public Result transferLeader(String transferUserId, String groupId) {
        // 检查群组是否存在
        Group group = this.getById(groupId);
        if (Objects.isNull(group)) {
            throw new GlobalBusinessException(800, "该群组不存在");
        }

        // 判断当前登录用户是否为该群组的组长
        String currentLoginUserId = LoginUserUtil.getCurrentLoginUserId();
        if (!currentLoginUserId.equals(group.getLeader())) {
            throw new GlobalBusinessException(800, "您不是该群组的组长");
        }

        // 判断是否组长转让给自己
        if (transferUserId.equals(group.getLeader())) {
            throw new GlobalBusinessException(800, "您已经是组长了");
        }

        // 判断被转让人是否为该群组的成员
        UserGroup userGroup = new UserGroup();
        userGroup.setGroupId(groupId);
        userGroup.setUserId(transferUserId);
        long count = userGroupService.count(Wrappers.query(userGroup));
        if (count == 0) {
            throw new GlobalBusinessException(800, "被转让人不是该群组的成员");
        }

        // 修改群组的Leader
        group.setLeader(transferUserId);
        boolean update = this.updateById(group);
        if (!update) {
            throw new GlobalBusinessException(800, "转让失败");
        }
        return Result.ok();
    }

    @Override
    public IPage<Group> getNoCommitGroupList(IPage<Group> pageParam, GroupQueryDTO groupQueryDTO) {
        Batch batch = batchMapper.selectById(groupQueryDTO.getBelongBatchId());
        if(Objects.isNull(batch)) {
            throw new GlobalBusinessException(800, "该批次不存在");
        }
        return baseMapper.selectNoCommitGroupList(pageParam, groupQueryDTO);
    }
}
