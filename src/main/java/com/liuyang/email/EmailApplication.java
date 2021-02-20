package com.liuyang.email;

import com.liuyang.email.service.SendMailService;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class EmailApplication implements ApplicationRunner {
    public static void main(String[] args) {
        SpringApplication.run(EmailApplication.class, args);
    }

    @Override
    public void run(ApplicationArguments args) {
        // 启动完之后就开始执行
        SendMailService.sendMail();
    }
}
