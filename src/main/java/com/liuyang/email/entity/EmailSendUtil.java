package com.liuyang.email.entity;

import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.util.Objects;

/**
 * 发送邮件
 */
@Slf4j
public class EmailSendUtil implements Runnable {

    private final ReceiverBO receiverBO;
    private final JavaMailSenderImpl mailSender;

    public EmailSendUtil(ReceiverBO receiverBO, JavaMailSenderImpl mailSender) {
        this.receiverBO = receiverBO;
        this.mailSender = mailSender;
    }

    @Override
    public void run() {
        log.info("开始发送：{}---{}", receiverBO.getProxyName(),receiverBO);
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper messageHelper;
        try {
            messageHelper = new MimeMessageHelper(mimeMessage, true);
            messageHelper.setFrom(Objects.requireNonNull(mailSender.getUsername()));// 设置本人
            messageHelper.setTo(receiverBO.getAddressee());// 设置收件人
            messageHelper.setCc(receiverBO.getCcAddress());// 设置多个抄送人
            messageHelper.setSubject(receiverBO.getEmailTheme());// 设置邮件主题
            messageHelper.setText(receiverBO.getEmailContext(), true);// 设置邮件正文
            for (File file : receiverBO.getAttachments()) {
                messageHelper.addAttachment(file.getName(), file);// 添加附件
            }
            mailSender.send(mimeMessage);
        } catch (Exception e) {
            log.error("发送错误，请重点注意：{}", receiverBO.getProxyName());
            return;
        }
        log.info("发送完毕: {}", receiverBO.getProxyName());
    }
}
