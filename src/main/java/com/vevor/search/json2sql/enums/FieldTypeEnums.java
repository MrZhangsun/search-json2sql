/*
 * Copyright (c) 2020-2030 Sishun.Co.Ltd. All Rights Reserved.
 */
package com.vevor.search.json2sql.enums;

/**
 * @author ：Murphy ZhangSun
 * @version ：1.9.0
 * @description ：字段类型枚举
 * @program ：user-growth
 * @date ：Created in 2023/7/20 17:36
 * @since ：1.9.0
 */
public enum FieldTypeEnums {

    /**
     * 固定值(不可分词)-字符串
     */
    STRING_IN(new OperatorEnums[]{OperatorEnums.IN,
            OperatorEnums.NOT_IN}),

    /**
     * 可分词-字符串
     */
    STRING_LIKE(new OperatorEnums[]{OperatorEnums.LIKE,
            OperatorEnums.NOT_LIKE,
            OperatorEnums.LEFT_LIKE,
            OperatorEnums.RIGHT_LIKE}),

    /**
     * 日期
     */
    DATE(new OperatorEnums[]{
            OperatorEnums.BETWEEN,
            OperatorEnums.MORE_THAN,
            OperatorEnums.MORE_THAN_AND_EQUAL,
            OperatorEnums.LESS_THAN,
            OperatorEnums.LESS_THAN_AND_EQUAL}),
    /**
     * 数值
     */
    NUMBER(new OperatorEnums[]{OperatorEnums.EQUAL,
            OperatorEnums.NOT_EQUAL,
            OperatorEnums.BETWEEN,
            OperatorEnums.MORE_THAN,
            OperatorEnums.MORE_THAN_AND_EQUAL,
            OperatorEnums.LESS_THAN,
            OperatorEnums.LESS_THAN_AND_EQUAL});

    /**
     * 字段类型支持的操作符
     */
    private final OperatorEnums[] operators;

    FieldTypeEnums(OperatorEnums[] operators) {
        this.operators = operators;
    }

    public OperatorEnums[] getOperators() {
        return operators;
    }
}