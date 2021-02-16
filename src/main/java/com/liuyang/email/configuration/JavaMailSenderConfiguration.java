package com.liuyang.email.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class JavaMailSenderConfiguration {

    @Value("${spring.mail.host}")
    private String host;
    @Value("${spring.mail.port}")
    private int port;
    @Value("${spring.mail.username}")
    private String username;
    @Value("${spring.mail.password}")
    private String password;
    @Value("${spring.mail.default-encoding}")
    private String defaultEncoding;

    @Bean
    public JavaMailSenderImpl getMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(host);//链接服务器
        mailSender.setPort(port);//默认使用25端口发送，如果设置了ssl，那么指定为465
        mailSender.setUsername(username);//账号
        mailSender.setPassword(password);//授权码
        mailSender.setDefaultEncoding(defaultEncoding);
        Properties properties = new Properties();
        properties.setProperty("mail.smtp.auth", "true");//开启认证
        properties.setProperty("mail.smtp.socketFactory.port", "465");//设置ssl端口
        properties.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");// 设置套接字对象

        mailSender.setJavaMailProperties(properties);
        return mailSender;
    }

}
