/*
 * Copyright (c) 2020-2030 Sishun.Co.Ltd. All Rights Reserved.
 */
package com.vevor.search.json2sql;

import com.vevor.search.json2sql.enums.JoinTypeEnums;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ：Murphy ZhangSun
 * @version ：1.9.0
 * @description ：联查表
 * @program ：user-growth
 * @date ：Created in 2023/7/24 11:20
 * @since ：1.9.0
 */
@Getter
public class JoinTable<T1, R1,T2, R2> {

    private final Class<?> targetTable;
    private final JoinTypeEnums joinType;
    private final List<JoinCondition<T1, R1,T2, R2>> joinConditions;

    public JoinTable(Class<?> targetTable, JoinTypeEnums joinType) {
        this.targetTable = targetTable;
        this.joinType = joinType;
        this.joinConditions = new ArrayList<>();
    }

    public JoinTable on(PropertyFunction<T1, R1> sourceField,
                        PropertyFunction<T2, R2>  targetField) {
        JoinCondition<T1, R1, T2, R2> joinCondition = new JoinCondition<>(sourceField, targetField);
        joinConditions.add(joinCondition);
        return this;
    }
}