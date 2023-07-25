/*
 * Copyright (c) 2020-2030 Sishun.Co.Ltd. All Rights Reserved.
 */
package com.vevor.search.json2sql;

import java.util.List;

/**
 * @author ：Murphy ZhangSun
 * @version ：1.0.0
 * @description ：字段配置类
 * @program ：search-json2sql
 * @date ：Created in 2023/7/25 14:47
 * @since ：1.0.0
 */
public class FieldConfiguration {

    private final String field;

    private final List<String> operations;

    public FieldConfiguration(String field, List<String> operations) {
        this.field = field;
        this.operations = operations;
    }

    public String getField() {
        return field;
    }

    public List<String> getOperations() {
        return operations;
    }
}