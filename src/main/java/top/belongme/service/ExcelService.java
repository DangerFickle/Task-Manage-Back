package top.belongme.service;

import top.belongme.model.result.Result;
import top.belongme.model.dto.TaskDetailsQueryDTO;

import javax.servlet.http.HttpServletResponse;

/**
 * @Title: ExcelService
 * @ProjectName Task-Manage-Back
 * @Description: TODO
 * @Author DengChao
 * @Date 2023/3/2617:24
 */
public interface ExcelService {
    Result getNotCommitExcel(HttpServletResponse response, TaskDetailsQueryDTO taskDetailsQueryDTO) throws Exception;
}
