/*
 * Copyright (c) 2020-2030 Sishun.Co.Ltd. All Rights Reserved.
 */
package com.vevor.search.json2sql;

import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.util.JdbcConstants;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vevor.search.json2sql.enums.JoinTypeEnums;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.lang.invoke.SerializedLambda;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

/**
 * @author ：Murphy ZhangSun
 * @version ：1.9.0
 * @description ：JSON转SQL
 * @program ：user-growth
 * @date ：Created in 2023/7/24 15:40
 * @since ：1.9.0
 */
public class Json2SqlWrapper {

    private final List<Condition> conditions;

    private String mainTable;

    private final List<JoinTable> joinTables;

    private JoinTable currentTable;

    // todo 默认实现添加
    private final List<String> selectItems = new ArrayList<>();
    private final Set<String> allTableFields = new HashSet<>();

    public Json2SqlWrapper(List<Condition> conditions) {
        this.conditions = conditions;
        this.joinTables = new ArrayList<>();
    }

    public Json2SqlWrapper(String jsonFile) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        try (InputStream inputStream = Json2SqlWrapper.class.getResourceAsStream(jsonFile)) {
            this.conditions = objectMapper.readValue(inputStream, new TypeReference<List<Condition>>(){});
            this.joinTables = new ArrayList<>();
        }
    }

    /**
     * 添加SELECT字段
     * 如果没有设置，默认取主表所有字段
     *
     * @param selectItems 表字段
     * @param <T> 类型
     * @param <R> 返回值
     * @return 当前对象
     */
    @SafeVarargs
    public final <T, R> Json2SqlWrapper addSelectItems(PropertyFunction<T, R>... selectItems) {
        for (PropertyFunction<T, R> selectItem : selectItems) {
            String fieldName = getFieldName(selectItem);
            if (!this.selectItems.contains(fieldName)) {
                this.selectItems.add(fieldName);
            }
        }
        return this;
    }

    public Json2SqlWrapper join(Class<?> joinTable, JoinTypeEnums joinType) {
        // 创建新表
        this.currentTable = new JoinTable(joinTable, joinType);
        this.joinTables.add(this.currentTable);
        // 添加全表字段
        this.addTableFields(joinTable);
        return this;
    }

    /**
     * 设置主表
     *
     * @param mainTableClass 主表
     * @return 当前对象
     */
    public Json2SqlWrapper from(Class<?> mainTableClass) {
        this.mainTable = getTableName(mainTableClass);
        // 添加全表字段
        this.addTableFields(mainTableClass);
        return this;
    }

    private void addTableFields(Class<?> clazz) {
        String tableName = this.getTableName(clazz);
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            String fieldName = field.getName();
            String tableFieldName = fieldFormat(tableName, fieldName);
            this.allTableFields.add(tableFieldName);
        }
    }

    /**
     * 设置联查条件
     *
     *
     * @param sourceField 左表达式
     * @param targetField 右表达式
     * @param <T1> 输入类型
     * @param <R1> 返回类型
     * @param <T2> 输入类型
     * @param <R2> 返回类型
     * @return 当前对象
     */
    public <T1, R1,T2, R2> Json2SqlWrapper on(PropertyFunction<T1, R1> sourceField,
                                        PropertyFunction<T2, R2>  targetField) {
        this.currentTable.on(sourceField, targetField);
        return this;
    }

    public String convert(Boolean format) {
        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append("select ");
        // 1. 解析字段
        Iterator<String> selectItemIterator = selectItems.iterator();
        while (selectItemIterator.hasNext()) {
            String next = selectItemIterator.next();
            sqlBuilder.append(next);
            if (selectItemIterator.hasNext()) {
                sqlBuilder.append(", ");
            }
        }
        sqlBuilder.append(" from `")
                .append(this.mainTable)
                .append("` ");

        // 2. 解析表关系
        if (joinTables != null) {
            for (JoinTable join : joinTables) {
                // left join target on mainTable.
                JoinTypeEnums joinType = join.getJoinType();
                String keyword = joinType.keyword;
                sqlBuilder.append(keyword)
                        .append(" ");

                Class<?> targetTable = join.getTargetTable();
                String tableName = getTableName(targetTable);
                sqlBuilder.append("`")
                        .append(tableName)
                        .append("` ON ");

                List<JoinCondition> joinConditions = join.getJoinConditions();
                Iterator<JoinCondition> joinConditonIterator = joinConditions.iterator();
                while (joinConditonIterator.hasNext()) {
                    JoinCondition joinCondition = joinConditonIterator.next();
                    PropertyFunction sourceField = joinCondition.getSourceField();
                    PropertyFunction targetField = joinCondition.getTargetField();
                    String sourceFieldName = getFieldName(sourceField);
                    String targetFieldName = getFieldName(targetField);
                    sqlBuilder.append(sourceFieldName)
                            .append(" = ")
                            .append(targetFieldName);
                    if (joinConditonIterator.hasNext()) {
                        sqlBuilder.append(" AND ");
                    }
                }
            }
        }

        // 3. 解析where条件
        String where = Json2SqlEngine.convert(conditions, allTableFields);
        if (StringUtils.isNotBlank(where)) {
            sqlBuilder.append(" WHERE ");
            sqlBuilder.append(where);
        }

        // 4. 格式化SQL
        String sql = sqlBuilder.toString();
        return format ? SQLUtils.format(sql, JdbcConstants.MYSQL, SQLUtils.DEFAULT_FORMAT_OPTION)
                : sql;
    }

    private String getTableName(Class<?> targetTable) {
        String simpleName = targetTable.getSimpleName();
        return simpleName.toLowerCase(Locale.ROOT);
    }

    private static <T> String getFieldName(PropertyFunction<T, ?> func) {
        try {
            // writeReplace 方法是 Serializable 接口中的一个特殊方法。当一个对象实现了 Serializable 接口时，Java 序列化框架会在序列化对象时调用 writeReplace 方法，而不是默认的序列化过程。这个方法允许开发者在对象序列化之前进行一些定制操作。
            // 通过获取 writeReplace 方法，实际上是为了获取 SerializedLambda 对象，从而可以解析方法引用的信息，包括方法引用所在的类、方法名称、方法参数等信息。
            Method method = func.getClass().getDeclaredMethod("writeReplace");
            method.setAccessible(Boolean.TRUE);
            // 利用jdk的SerializedLambda 解析方法引用
            java.lang.invoke.SerializedLambda serializedLambda = (SerializedLambda) method.invoke(func);
            String getName = serializedLambda.getImplMethodName();
            String implClass = serializedLambda.getImplClass();

            String className = implClass.substring(implClass.lastIndexOf("/") + 1);
            String fieldName = resolveFieldName(getName);
            return fieldFormat(className, fieldName);
        } catch (ReflectiveOperationException e) {
            throw new IllegalFormatFlagsException("reflection failed, reason: " + e.getMessage());
        }
    }

    /**
     * 字段格式化
     *
     * @param tableName 表名
     * @param fieldName 字段名
     * @return 格式化后的字段名
     */
    private static String fieldFormat(String tableName, String fieldName) {
        return String.format("`%s`.`%s`", tableName, fieldName).toLowerCase(Locale.ROOT);
    }

    private static String resolveFieldName(String getMethodName) {
        if (getMethodName.startsWith("get")) {
            getMethodName = getMethodName.substring(3);
        } else if (getMethodName.startsWith("is")) {
            getMethodName = getMethodName.substring(2);
        }
        // 小写第一个字母
        return firstToLowerCase(getMethodName);
    }

    private static String firstToLowerCase(String param) {
        if (StringUtils.isBlank(param)) {
            return "";
        }
        return param.substring(0, 1).toLowerCase() + param.substring(1);
    }
}