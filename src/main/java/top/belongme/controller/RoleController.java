package top.belongme.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.belongme.model.pojo.Role;
import top.belongme.model.result.Result;
import top.belongme.service.RoleService;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Title: RoleController
 * @ProjectName Task-Manage-Back
 * @Description: TODO
 * @Author DengChao
 * @Date 2023/3/816:50
 */
@RestController
@RequestMapping("role")
public class RoleController {

    @Resource
    private RoleService roleService;

    /**
     * TODO 获取角色列表
     *
     * @Author DengChao
     * @Date 2023/6/5 14:26
     */
    @GetMapping("/list")
    public Result<List<Role>> getRoleList() {
        List<Role> list = roleService.list().stream().filter(v -> {
            // 排除系统管理员角色
            String roleName = v.getRoleName();
            return !roleName.equals("系统管理员");
        }).collect(Collectors.toList());
        return new Result<>(200, "角色列表获取成功", list);
    }
}
