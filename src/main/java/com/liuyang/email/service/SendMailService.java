package com.liuyang.email.service;

import com.alibaba.excel.EasyExcel;
import com.liuyang.email.configuration.ReceiverConst;
import com.liuyang.email.entity.EmailSendUtil;
import com.liuyang.email.entity.ReceiverBO;
import com.liuyang.email.entity.ReceiverDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
@Slf4j
public class SendMailService {

    private static ReceiverConst receiverConst;

    @Autowired
    public void setReceiverConst(ReceiverConst receiverConst) {
        SendMailService.receiverConst = receiverConst;
    }

    private static JavaMailSenderImpl mailSender;

    @Autowired
    public void sendMail(JavaMailSenderImpl mailSender) {
        SendMailService.mailSender = mailSender;
    }

    private static final ExecutorService executorService = Executors.newFixedThreadPool(10);

    /**
     * 开始发送邮件
     */
    public void sendMail() {
        // 1、获取发送名单
        String fileName = receiverConst.sendListPath;
        List<ReceiverDTO> dtoList = EasyExcel.read(fileName).head(ReceiverDTO.class).sheet().doReadSync();
        dtoList.forEach(dto -> log.info("发送名单集合：{}", dto));

        // 2、将发送名单转化为BO，并且将附件等信息一一对应上
        List<ReceiverBO> boList = this.setSendData(dtoList);

        // 3、放入线程池，在数据量不多的情况下可以选择固定线程的线程池，如果数据量多的话，容易造成OOM
        // 解决办法可以选择生产者消费者模式。发送完一批后再接着发送下一批。
        boList.forEach(bo -> executorService.execute(new EmailSendUtil(bo, SendMailService.mailSender)));
        log.info("放入完毕");
        executorService.shutdown();
        log.info("全部发送完毕");
    }


    /**
     * 业务上抄送人最多只有6个，所以一一进行添加，
     * 如果以后有更多了，那么可以先将抄送人提取出来变成一个CSV文件然后读取。
     */
    private String[] setCcAddress(ReceiverDTO dto) {
        List<String> addressList = new ArrayList<>();
        if (dto.getCcAddress1() != null) {
            addressList.add(dto.getCcAddress1());
        }
        if (dto.getCcAddress2() != null) {
            addressList.add(dto.getCcAddress2());
        }
        if (dto.getCcAddress3() != null) {
            addressList.add(dto.getCcAddress3());
        }
        if (dto.getCcAddress4() != null) {
            addressList.add(dto.getCcAddress4());
        }
        if (dto.getCcAddress5() != null) {
            addressList.add(dto.getCcAddress5());
        }
        if (dto.getCcAddress6() != null) {
            addressList.add(dto.getCcAddress6());
        }
        return addressList.toArray(new String[0]);
    }

    /**
     * 将发送的名单转化成发送的对象
     */
    private List<ReceiverBO> setSendData(List<ReceiverDTO> dtoList) {
        List<ReceiverBO> boList = new ArrayList<>();
        for (ReceiverDTO dto : dtoList) {
            ReceiverBO bo = new ReceiverBO(receiverConst.fileSuffix1);
            bo.setAddressee(dto.getAddressee()); // 收件人
            bo.setCcAddress(this.setCcAddress(dto));// 抄送人数组
            bo.setProxyName(dto.getProxyName());// 代理商名字
            bo.setThemeName(receiverConst.themePrefix, receiverConst.themeSuffix);// 邮件主题
            bo.setFiles(receiverConst.sendFilePath, receiverConst.fileSuffix1);// 附件1
            bo.setFiles(receiverConst.sendFilePath, receiverConst.fileSuffix2);// 附件2
            bo.setEmailContext();
            boList.add(bo);
        }
        return boList;
    }
}
