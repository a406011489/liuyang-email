package com.liuyang.email.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.liuyang.email.entity.ExcelFileDTO;
import com.liuyang.email.entity.ReceiverBO;
import freemarker.template.Configuration;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 监听读取
 */
@EqualsAndHashCode(callSuper = true)
@Slf4j
@Data
public class ExcelDataListener extends AnalysisEventListener<ExcelFileDTO> {
    private List<ExcelFileDTO> list = new ArrayList<>();
    private ReceiverBO receiverBO;

    // 需要将BO传进来进行赋值，对邮件正文里的内容进行赋值
    public ExcelDataListener(ReceiverBO receiverBO) {
        this.receiverBO = receiverBO;
    }

    @Override
    public void invoke(ExcelFileDTO data, AnalysisContext context) {
        // 根据文档要求，每读到最大数量要进行回收，但本业务只读取一行，就不必了回收了。
        list.add(data);
    }

    @SneakyThrows
    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        // 根据业务需求只取第1行
        ExcelFileDTO excelData = list.get(0);

        // 第8列要将数字设置成百分号
        String str8 = "";
        try {
            BigDecimal num = new BigDecimal(excelData.getStr8());
            DecimalFormat df = new DecimalFormat("0%");
            str8 = df.format(num);
        } catch (Exception e) {
            // 如果出现异常就直接原样
            str8 = excelData.getStr8();
        }

        Map<String, String> map = new HashMap<>();
        map.put("str2", excelData.getStr2());
        map.put("str3", excelData.getStr3());
        map.put("str4", excelData.getStr4());
        map.put("str5", excelData.getStr5());
        map.put("str6", excelData.getStr6());
        map.put("str7", excelData.getStr7());
        map.put("str8", str8);
        map.put("str9", excelData.getStr9());
        map.put("str10", excelData.getStr10());
        map.put("str11", excelData.getStr11());
        map.put("str12", excelData.getStr12());
        map.put("str13", excelData.getStr13());
        Configuration cfg = new Configuration(Configuration.VERSION_2_3_19);
        cfg.setClassForTemplateLoading(this.getClass(), "/template");
        // 通过freemarker将html转成字符串。
        String html = FreeMarkerTemplateUtils.processTemplateIntoString(cfg.getTemplate("mail.html"), map);
        this.receiverBO.setEmailContext(html);
    }
}
