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
            messageHelper.setFrom(Objects.requireNonNull(mailSender.getUsername()));
            messageHelper.setTo(receiverBO.getAddressee());
            messageHelper.setCc(receiverBO.getCcAddress());
            messageHelper.setSubject(receiverBO.getEmailTheme());
            messageHelper.setText(receiverBO.getEmailContext(), true);
            for (File file : receiverBO.getAttachments()) {
                messageHelper.addAttachment(file.getName(), file);
            }
            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            log.error("发送错误，请重点注意：{}", receiverBO.getProxyName());
            e.printStackTrace();
        }
        log.info("发送完毕: {}", receiverBO.getProxyName());
    }
}
