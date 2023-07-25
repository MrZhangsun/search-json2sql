/*
 * Copyright (c) 2020-2030 Sishun.Co.Ltd. All Rights Reserved.
 */
package com.vevor.search.json2sql;

import lombok.Getter;

/**
 * @author ：Murphy ZhangSun
 * @version ：1.9.0
 * @description ：联查条件
 * @program ：user-growth
 * @date ：Created in 2023/7/24 11:03
 * @since ：1.9.0
 */
@Getter
public class JoinCondition<T1, R1, T2, R2> {

    private final PropertyFunction<T1, R1> sourceField;

    private final PropertyFunction<T2, R2> targetField;

    public JoinCondition(PropertyFunction<T1, R1> sourceField, PropertyFunction<T2, R2> targetField) {
        this.sourceField = sourceField;
        this.targetField = targetField;
    }

}