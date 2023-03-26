package top.belongme.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.belongme.model.result.Result;
import top.belongme.model.vo.TaskDetailsQueryVo;
import top.belongme.service.ExcelService;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

/**
 * @Title: ExcelController
 * @ProjectName Task-Manage-Back
 * @Description: TODO
 * @Author DengChao
 * @Date 2023/3/2617:23
 */
@RestController
@RequestMapping("/excel")
public class ExcelController {

    @Resource
    private ExcelService excelService;

    @PreAuthorize("hasAuthority('job:userInfo:export')")
    @GetMapping("/notCommit")
    public Result notCommit(HttpServletResponse response, TaskDetailsQueryVo taskDetailsQueryVo) throws Exception {
        return excelService.getNotCommitExcel(response, taskDetailsQueryVo);
    }
}
