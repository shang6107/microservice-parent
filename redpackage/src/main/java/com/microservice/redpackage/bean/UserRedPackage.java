package com.microservice.redpackage.bean;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * @author 上官炳强
 * @description
 * @since 2018-10-15 / 09:33:16
 */
@Data
public class UserRedPackage implements Serializable {
    private Long id;
    private String redPackageId;
    private Long userId;
    private Double amount;
    private Timestamp grabTime;
    private String note;

}
