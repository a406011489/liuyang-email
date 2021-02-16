package com.liuyang.email.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ReceiverConst {
    @Value("${zzf.mail.prefix}")
    public String themePrefix; // 邮件主题前缀

    @Value("${zzf.mail.suffix}")
    public String themeSuffix;// 邮件主题后缀

    @Value("${zzf.file.suffix1}")
    public String fileSuffix1;// 附件名后缀1

    @Value("${zzf.file.suffix2}")
    public String fileSuffix2;// 附件名后缀2

    @Value("${zzf.send.list-path}")
    public String sendListPath;// 发送名单的路径

    @Value("${zzf.send.file-path}")
    public String sendFilePath;// 发送附加集合路径

    @Bean
    public ReceiverConst setReceiverConst() {
        return this;
    }

}
