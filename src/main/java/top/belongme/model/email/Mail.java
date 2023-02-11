package top.belongme.model.email;

import lombok.Data;

/**
 * @Title: Mail
 * @ProjectName Task-Manage-Back
 * @Description: TODO
 * @Author DengChao
 * @Date 2023/2/1122:18
 */
@Data
public class Mail {
    /**
     * 接收人
     */
    private String sendTo;

    /**
     *  邮件主题
     */
    private String subject;

    /**
     *  邮件内容
     */
    private String text;

}
