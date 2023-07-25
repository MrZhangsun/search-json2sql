/*
 * Copyright (c) 2020-2030 Sishun.Co.Ltd. All Rights Reserved.
 */
package com.vevor.search.json2sql.enums;

/**
 * @author ：Murphy ZhangSun
 * @version ：1.9.0
 * @description ：联查类型
 * @program ：user-growth
 * @date ：Created in 2023/7/24 11:00
 * @since ：1.9.0
 */
public enum JoinTypeEnums {
    INNER_JOIN("INNER JOIN"),
    LEFT_JOIN("LEFT JOIN"),
    RIGHT_JOIN("RIGHT JOIN"),
    FULL_JOIN("FULL JOIN"),
    CROSS_JOIN("CROSS JOIN");

    public final String keyword;

    JoinTypeEnums(String keyword) {
        this.keyword = keyword;
    }
}