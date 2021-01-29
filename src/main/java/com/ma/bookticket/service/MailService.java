package com.ma.bookticket.service;

/**
 * 封装一个发邮件的接口，后边直接调用即可
 * @author yong
 * @date 2021/1/26 15:45
 */

public interface MailService {
    /**
     * 发送文本邮件
     * @param to 收件人
     * @param subject 主题
     * @param content 内容
     * @author yong
     * @date 2021/1/26 15:54 
     * @return void
     */
    void sendSimpleMail(String to, String subject, String content);
    
    /**
     * 发送HTML邮件
     * @param to 收件人
     * @param subject 主题
     * @param content 内容
     * @author yong
     * @date 2021/1/26 15:55
     * @return void
     */

    public void sendHtmlMail(String to, String subject, String content);

    /**
     * 发送带附件的邮件
     * @param to 收件人
     * @param subject 主题
     * @param content 内容
     * @param filePath 附件
     * @author yong
     * @date 2021/1/26 15:58 
     * @return void
     */

    public void sendAttachmentsMail(String to, String subject, String content, String filePath);
}
