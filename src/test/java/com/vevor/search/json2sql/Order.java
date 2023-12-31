/*
 * Copyright (c) 2020-2030 Sishun.Co.Ltd. All Rights Reserved.
 */
package com.vevor.search.json2sql;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author ：Murphy ZhangSun
 * @version ：
 * @description ：表1
 * @program ：user-growth
 * @date ：Created in 2023/7/24 11:15
 * @since ：
 */
@Data
public class Order {

    private Integer id;

    private String code;

    private Date createdTime;

    private BigDecimal money;

    private Long customerId;

    private Long productId;
}