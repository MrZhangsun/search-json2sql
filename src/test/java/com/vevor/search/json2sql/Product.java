/*
 * Copyright (c) 2020-2030 Sishun.Co.Ltd. All Rights Reserved.
 */
package com.vevor.search.json2sql;

import lombok.Data;

/**
 * @author ：Murphy ZhangSun
 * @version ：1.0.0
 * @description ：商品表
 * @program ：user-growth
 * @date ：Created in 2023/7/24 11:15
 * @since ：1.0.0
 */
@Data
public class Product {

    private Integer id;

    private String sku;

    private String title;

    private String description;

}