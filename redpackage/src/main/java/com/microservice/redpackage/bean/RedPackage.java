package com.microservice.redpackage.bean;

import lombok.Data;

import java.sql.Timestamp;

/**
 * @author 上官炳强
 * @description
 * @since 2018-10-15 / 09:23:23
 */
@Data
public class RedPackage {
    private Long id;
    private Long userId;
    private Double amount;
    private Timestamp sendDate;
    private Integer total;
    private Double unitAmount;
    private Integer stock;
    private String note;
}
