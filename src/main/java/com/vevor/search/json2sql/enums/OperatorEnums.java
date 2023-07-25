/*
 * Copyright (c) 2020-2030 Sishun.Co.Ltd. All Rights Reserved.
 */
package com.vevor.search.json2sql.enums;

/**
 * @author ：Murphy ZhangSun
 * @version ：1.9.0
 * @description ：运算符枚举值
 * @program ：user-growth
 * @date ：Created in 2023/7/20 15:14
 * @since ：1.9.0
 *     // 数值：大于等于、等于、不等于、大于、小于、小于等于、介于
 *     // 日期：当天/当周/当月/当年（不需要维护标签值）、近x天、日期范围
 *     // 字符串： 精准匹配、包含、不包含
 */
public enum OperatorEnums {

    /**
     * 包含
     */
    IN("包含"),

    /**
     * 不包含
     */
    NOT_IN("不包含"),

    /**
     * 包含
     */
    LIKE("包含"),

    /**
     * 不包含
     */
    NOT_LIKE("包含"),

    /**
     * 左包含
     */
    LEFT_LIKE("左包含"),

    /**
     * 右包含
     */
    RIGHT_LIKE("右包含"),

    /**
     * 等于
     */
    EQUAL("等于"),

    /**
     * 不等于
     */
    NOT_EQUAL("不等于"),

    /**
     * 介于
     */
    BETWEEN("介于"),

    /**
     * 大于
     */
    MORE_THAN("大于"),

    /**
     * 大于等于
     */
    MORE_THAN_AND_EQUAL("大于等于"),

    /**
     * 小于
     */
    LESS_THAN("小于"),

    /**
     * 小于等于
     */
    LESS_THAN_AND_EQUAL("小于等于");

    public final String label;

    OperatorEnums(String label) {
        this.label = label;
    }
}