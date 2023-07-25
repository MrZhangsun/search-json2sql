/*
 * Copyright (c) 2020-2030 Sishun.Co.Ltd. All Rights Reserved.
 */
package com.vevor.search.json2sql;

import com.vevor.search.json2sql.enums.FieldTypeEnums;
import com.vevor.search.json2sql.enums.OperatorEnums;
import com.vevor.search.json2sql.enums.RuleTypeEnums;

import java.util.List;
import java.util.Set;

class Json2SqlEngine {
    private Json2SqlEngine() {}

    static String convert(List<Condition> conditions, Set<String> existFields) {
        // 1. 参数校验
        if (conditions == null) {
            return null;
        }

        // 2. 解析SQL条件
        StringBuilder conditionBuilder = new StringBuilder();
        for (Condition condition : conditions) {
            if (conditionBuilder.length() > 0) {
                conditionBuilder.append(" ");
                conditionBuilder.append(condition.getRelation());
                conditionBuilder.append(" ");
            }

            // 2.1. 嵌套解析
            if (RuleTypeEnums.RULES_RELATION.equals(condition.getType())) {
                conditionBuilder.append("(");
                conditionBuilder.append(convert(condition.getRules(), existFields));
                conditionBuilder.append(")");
                // 2.2. 具体规则解析
            } else if (RuleTypeEnums.FIELDS_RELATION.equals(condition.getType())) {
                String field = condition.getField();
                if (!existFields.contains(field)) {
                    throw new IllegalArgumentException("Doesn't exist field: " + field);
                }
                convert(condition, conditionBuilder);
            }
        }

        return conditionBuilder.toString();
    }

    private static void convert(Condition condition, StringBuilder sqlBuilder) {
        String field = condition.getField();
        OperatorEnums operator = condition.getOperator();
        List<String> values = condition.getValue();
        String toValue = condition.getToValue();
        String value = values.get(0);
        switch (operator) {
            // 包含
            case IN:
                in(field, values, sqlBuilder, false);
                break;

            // 不包含
            case NOT_IN:
                in(field, values, sqlBuilder, true);
                break;
            // 包含
            case LIKE:
                like(field, value, sqlBuilder);
                break;

            // 不包含
            case NOT_LIKE:
                notLike(field, value, sqlBuilder);
                break;

            // 左包含
            case LEFT_LIKE:
                leftLike(field, value, sqlBuilder);
                break;

            // 右包含
            case RIGHT_LIKE:
                rightLike(field, value, sqlBuilder);
                break;

            // 等于
            case EQUAL:
                eq(field, value, sqlBuilder);
                break;

            // 不等于
            case NOT_EQUAL:
                ne(field, value, sqlBuilder);
                break;

            // 介于
            case BETWEEN:
                between(field, value, toValue, sqlBuilder);
                break;

            // 大于
            case MORE_THAN:
                gt(field, value, sqlBuilder);
                break;

            // 大于等于
            case MORE_THAN_AND_EQUAL:
                gte(field, value, sqlBuilder);
                break;

            // 小于
            case LESS_THAN:
                lt(field, value, sqlBuilder);
                break;

            // 小于等于
            case LESS_THAN_AND_EQUAL:
                lte(field, value, sqlBuilder);
                break;
            default:
                throw new UnsupportedOperationException("Unsupported Operator: " + operator);
        }
    }

    private static void in(String field, List<String> values, StringBuilder sqlBuilder, Boolean isNot) {
        if (values == null || values.isEmpty()) {
            return;
        }
        if (isNot) {
            sqlBuilder.append(field).append(" NOT IN (");
        } else {
            sqlBuilder.append(field).append(" IN (");
        }

        for (int i = 0; i < values.size(); i++) {
            if (i > 0) {
                sqlBuilder.append(", ");
            }
            sqlBuilder.append("'").append(values.get(i)).append("'");
        }
        sqlBuilder.append(")");
    }

    private static void eq(String field, String value, StringBuilder sqlBuilder) {
        sqlBuilder.append(field)
                .append(" = '")
                .append(value)
                .append("'");
    }

    private static void ne(String field, String value, StringBuilder sqlBuilder) {
        sqlBuilder.append(field)
                .append(" <> '")
                .append(value)
                .append("'");
    }

    private static void gt(String field, String value, StringBuilder sqlBuilder) {
        sqlBuilder.append(field)
                .append(" > '")
                .append(value)
                .append("'");
    }


    private static void gte(String field, String value, StringBuilder sqlBuilder) {
        sqlBuilder.append(field)
                .append(" >= '")
                .append(value)
                .append("'");
    }

    private static void lt(String field, String value, StringBuilder sqlBuilder) {
        sqlBuilder.append(field)
                .append(" < '")
                .append(value)
                .append("'");
    }

    private static void lte(String field, String value, StringBuilder sqlBuilder) {
        sqlBuilder.append(field)
                .append(" <= '")
                .append(value)
                .append("'");
    }

    private static void between(String field, String value, String toValue, StringBuilder sqlBuilder) {
        sqlBuilder.append(field)
                .append(" BETWEEN ")
                .append(value)
                .append(" AND ")
                .append(toValue);
    }

    private static void like(String field, String value, StringBuilder sqlBuilder) {
        sqlBuilder.append(field)
                .append(" LIKE '%")
                .append(value)
                .append("%'");
    }

    private static void rightLike(String field, String value, StringBuilder sqlBuilder) {
        sqlBuilder.append(field)
                .append(" LIKE '")
                .append(value)
                .append("%'");
    }

    private static void leftLike(String field, String value, StringBuilder sqlBuilder) {
        sqlBuilder.append(field)
                .append(" LIKE '%")
                .append(value)
                .append("'");
    }

    private static void notLike(String field, String value, StringBuilder sqlBuilder) {
        sqlBuilder.append(field)
                .append(" NOT LIKE '%")
                .append(value)
                .append("%'");
    }
}