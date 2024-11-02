package top.belongme.service;

import top.belongme.model.pojo.Email;
import top.belongme.model.result.Result;
import top.belongme.model.dto.RemindDTO;

public interface SendMailService {

    /**
     * 简单文本邮件
     *
     * @param email
     * @return
     */
    void sendSimpleMail(Email email);


    /**
     * Html格式邮件,可带附件
     *
     * @param email
     * @return
     */
    void sendHtmlMail(Email email);

    /**
     * TODO 提醒指定用户
     *
     * @Author DengChao
     * @Date 2023/2/20 16:37
     */
    Result remindUser(RemindDTO remindDTO);
}
