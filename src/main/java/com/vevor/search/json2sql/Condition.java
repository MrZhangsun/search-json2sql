/*
 * Copyright (c) 2020-2030 Sishun.Co.Ltd. All Rights Reserved.
 */
package com.vevor.search.json2sql;

import com.vevor.search.json2sql.enums.FieldTypeEnums;
import com.vevor.search.json2sql.enums.OperatorEnums;
import com.vevor.search.json2sql.enums.RelationEnums;
import com.vevor.search.json2sql.enums.RuleTypeEnums;
import lombok.Data;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author ：Murphy ZhangSun
 * @version ：1.9.0
 * @description ：计算规则
 * @program ：user-growth
 * @date ：Created in 2023/7/20 15:00
 * @since ：1.9.0
 */
@Data
public class Condition {

    /**
     * 多表字段映射关系
     */
    private static Map<String, Set<String>> tableFieldMap = new ConcurrentHashMap<>();

    /**
     * 代表当前规则的类型：rules_relation，
     */
    private RuleTypeEnums type;

    /**
     * 当前规则与之前的规则之间的逻辑关系,
     */
    private RelationEnums relation;

    /**
     * 当前规则存在嵌套时，改属性不为空，表示当前规则没有直接计算结果，依赖嵌套规则计算结果
     */
    private List<Condition> rules;

    /**
     * 比较的属性
     */
    private String field;

    /**
     * 用于存储字段的类型
     */
    private FieldTypeEnums className;

    /**
     * 字段输入方式
     */
    private String format;

    /**
     * 运算符
     */
    private OperatorEnums operator;

    /**
     * 运算值，数组类型，当是包含，或范围查询时，可以传递多值，当是单值运算时，只放当前值即可
     */
    private List<String> value;

    /**
     * 用于区间查询时，右区间值
     */
    private String toValue;

    public void setType(String type) {
        this.type = RuleTypeEnums.valueOf(type.toUpperCase(Locale.ROOT));
    }

    public void setRelation(String relation) {
        this.relation = RelationEnums.valueOf(relation.toUpperCase(Locale.ROOT));
    }

    public void setOperator(String operator) {
        this.operator = OperatorEnums.valueOf(operator.toUpperCase(Locale.ROOT));
    }

    public void setClassName(String className) {
        this.className = FieldTypeEnums.valueOf(className.toUpperCase(Locale.ROOT));
    }

    public String getField() {
        if (tableFieldMap == null || tableFieldMap.isEmpty()) {
            return String.format("`%s`", this.field);
        }

        tableFieldMap.forEach((tableName, fields) -> {
            for (String f : fields) {
                if (f.equalsIgnoreCase(this.field)) {
                    this.field = String.format("`%s`.`%s`", tableName, f);
                }
            }
        });

        return this.field;
    }

    public static void addTableFields(String tableName, Set<String> fields) {
        if (tableFieldMap.containsKey(tableName)) {
            Set<String> exists = tableFieldMap.get(tableName);
            exists.addAll(fields);
            return;
        }
        tableFieldMap.put(tableName, fields);
    }

    public static Set<String> tableNames() {
        return tableFieldMap.keySet();
    }
}