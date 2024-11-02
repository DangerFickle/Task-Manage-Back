package top.belongme.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.belongme.exception.GlobalBusinessException;
import top.belongme.model.dto.TaskDetailsQueryDTO;
import top.belongme.model.pojo.task.GroupTaskDetail;
import top.belongme.model.pojo.task.PersonalTaskDetail;
import top.belongme.model.result.Result;
import top.belongme.service.TaskDetailsService;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.Objects;

/**
 * @Title: TaskDetailsController
 * @ProjectName Task-Manage-Back
 * @Description: TODO
 * @Author DengChao
 * @Date 2023/2/1220:05
 */
@RestController
@RequestMapping("/taskDetails")
public class TaskDetailsController {
    @Resource
    private TaskDetailsService taskDetailsService;

    @PreAuthorize("hasAuthority('job:taskDetail:select')")
    @GetMapping("/listPage/{page}/{limit}")
    public Result<IPage<PersonalTaskDetail>> getBatchList(@PathVariable Long page,
                                                          @PathVariable Long limit,
                                                          @Valid TaskDetailsQueryDTO taskDetailsQueryDTO,
                                                          BindingResult result) {
        if (result != null && result.hasErrors()) {
            throw new GlobalBusinessException(800, Objects.requireNonNull(result.getFieldError()).getDefaultMessage());
        }
        //创建page对象
        Page<PersonalTaskDetail> pageParam = new Page<>(page, limit);
        //调用service方法
        IPage<PersonalTaskDetail> pageModel = taskDetailsService.selectPage(pageParam, taskDetailsQueryDTO);
        return new Result<>(200, "请求成功", pageModel);
    }

    /**
     * TODO 群组批次的提交情况
     *
     * @Author DengChao
     * @Date 2024/10/27 14:22
     */
    @PreAuthorize("hasAuthority('job:taskDetail:select')")
    @GetMapping("/listGroupPage/{page}/{limit}")
    public Result<IPage<GroupTaskDetail>> listGroupPage(@PathVariable Long page,
                                                        @PathVariable Long limit,
                                                        @Valid TaskDetailsQueryDTO taskDetailsQueryDTO,
                                                        BindingResult result) {
        if (result != null && result.hasErrors()) {
            throw new GlobalBusinessException(800, Objects.requireNonNull(result.getFieldError()).getDefaultMessage());
        }
        //创建page对象
        Page<GroupTaskDetail> pageParam = new Page<>(page, limit);
        //调用service方法
        IPage<GroupTaskDetail> pageModel = taskDetailsService.selectGroupPage(pageParam, taskDetailsQueryDTO);
        return new Result<>(200, "请求成功", pageModel);
    }
}
