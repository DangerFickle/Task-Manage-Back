package top.belongme.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.belongme.model.result.Result;
import top.belongme.model.vo.RemindVo;
import top.belongme.service.SendMailService;

import javax.annotation.Resource;

/**
 * @Title: EmailController
 * @ProjectName Task-Manage-Back
 * @Description: TODO
 * @Author DengChao
 * @Date 2023/2/2016:34
 */
@RestController
@RequestMapping("/email")
public class EmailController {

    @Resource
    private SendMailService sendMailService;

    @GetMapping("/remind")
    public Result remindUser(RemindVo remindVo) {
        return sendMailService.remindUser(remindVo);
    }
}
