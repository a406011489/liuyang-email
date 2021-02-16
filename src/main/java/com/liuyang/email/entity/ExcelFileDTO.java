package com.liuyang.email.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

/**
 * 通过此类来接收附件里需要显示在邮件正文里的内容
 */
@Data
public class ExcelFileDTO {
    @ExcelProperty(index = 0)
    private String str1;
    @ExcelProperty(index = 1)
    private String str2;
    @ExcelProperty(index = 2)
    private String str3;
    @ExcelProperty(index = 3)
    private String str4;
    @ExcelProperty(index = 4)
    private String str5;
    @ExcelProperty(index = 5)
    private String str6;
    @ExcelProperty(index = 6)
    private String str7;
    @ExcelProperty(index = 7)
    private String str8;
    @ExcelProperty(index = 8)
    private String str9;
    @ExcelProperty(index = 9)
    private String str10;
    @ExcelProperty(index = 10)
    private String str11;
    @ExcelProperty(index = 11)
    private String str12;
    @ExcelProperty(index = 12)
    private String str13;
}