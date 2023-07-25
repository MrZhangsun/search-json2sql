/*
 * Copyright (c) 2020-2030 Sishun.Co.Ltd. All Rights Reserved.
 */
package com.vevor.search.json2sql;

import com.vevor.search.json2sql.enums.FieldTypeEnums;

/**
 * @author ：Murphy ZhangSun
 * @version ：1.0.0
 * @description ：指定的字段类型
 * @program ：search-json2sql
 * @date ：Created in 2023/7/25 11:30
 * @since ：1.0.0
 */
public class TheFieldType<T, R> {

    private final PropertyFunction<T, R> field;

    private final FieldTypeEnums fieldType;

    private final String label;

    public TheFieldType(PropertyFunction<T, R> field, String label, FieldTypeEnums fieldType) {
        this.field = field;
        this.fieldType = fieldType;
        this.label = label;
    }

    public PropertyFunction<T, R> getField() {
        return field;
    }

    public FieldTypeEnums getFieldType() {
        return fieldType;
    }

    public String getLabel() {
        return label;
    }
}