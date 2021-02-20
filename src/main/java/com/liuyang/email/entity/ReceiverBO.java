package com.liuyang.email.entity;

import com.alibaba.excel.EasyExcel;
import com.liuyang.email.listener.ExcelDataListener;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

/**
 * 通过此类来存放发送邮件的所有数据，并且对自身的数据进行封装
 */
@Data
@ToString(exclude = "emailContext")
public class ReceiverBO {

    private String targetSuffix;// 从哪个文件里中读取邮件正文内容。
    private static final String fileSuffix = ".xls";// 业务上读取的是.xls的文件

    private String addressee;// 收件人

    private String[] ccAddress;// 抄送人数组

    private String proxyName;// 代理商名字

    private String emailTheme;// 邮件主题

    private File contextFile;// 从此附件中获取邮件正文的内容

    private String emailContext;// 邮件正文

    private List<File> attachments = new ArrayList<>();// 附件集合

    public ReceiverBO(String targetSuffix) {
        this.targetSuffix = targetSuffix;
    }

    public void setEmailContext(String emailContext) {
        this.emailContext = emailContext;
    }

    /**
     * 设置邮件主题，格式为前缀 + 代理商名字 + 后缀。
     */
    public void setThemeName(String prefix, String suffix) {
        this.emailTheme = prefix + this.proxyName + suffix;
    }

    /**
     * 设置附件
     */
    public void setFiles(String filePath, String suffix) {
        // 通过VBA进行拆表后文件名字是固定的，后缀均为.xls
        String fileName = filePath + proxyName + suffix + fileSuffix;
        File file = new File(fileName);
        if(!file.exists()){
            return;
        }
        if (targetSuffix.equals(suffix)) { // 需要从账单计算表里取出数据，判断该文件是不是账单计算表
            this.contextFile = file; // 需要从账单计算表里取出数据，在这里进行设置
        }
        this.attachments.add(file);
    }

    /**
     * 设置邮件正文，根据技术文档里显示：某某Listener是不能被spring管理的，要每次读取excel都要new,
     * 然后里面用到spring可以构造方法传进去。
     * 鄙人的理解：这才是真正的用到了面向对象的思想，通过一个个的对象去处理业务，而不是一main到底，
     * 把校验代码、业务代码等所有代码都写在一个Service里，甚至干脆全部写在Controller里，
     * 虽然这样代码也能跑，这其实是一种面向过程的思维方式，代码维护起来会非常难，
     * 谁愿意去看一个有着上万行代码的文件？
     * 其实虽然官方说不能注入Listener，那是因为默认情况下Spring注入的对象都是单例的，
     * 而这里的业务是必须用多例去解决的，单例容易造成线程安全问题。
     * 理论上说，如果将Listener的注入设置成为多例，应该也可以交给Spring去托管，
     * 在类上面加上注解@Scope(value="prototype"，proxyMode = ScopedProxyMode.TARGET_CLASS)即可。
     */
    public void setEmailContext() {
        ExcelDataListener excelDataListener = new ExcelDataListener(this);
        EasyExcel.read(contextFile, ExcelFileDTO.class, excelDataListener).sheet().doRead();
    }

}
