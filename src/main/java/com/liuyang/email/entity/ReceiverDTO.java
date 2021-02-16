package com.liuyang.email.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

/**
 * 通过此来接收发送人名单
 */
@Data
public class ReceiverDTO {
    @ExcelProperty(index = 0)
    private String addressee;
    @ExcelProperty(index = 1)
    private String proxyName;
    @ExcelProperty(index = 2)
    private String ccAddress1;
    @ExcelProperty(index = 3)
    private String ccAddress2;
    @ExcelProperty(index = 4)
    private String ccAddress3;
    @ExcelProperty(index = 5)
    private String ccAddress4;
    @ExcelProperty(index = 6)
    private String ccAddress5;
    @ExcelProperty(index = 7)
    private String ccAddress6;
}
