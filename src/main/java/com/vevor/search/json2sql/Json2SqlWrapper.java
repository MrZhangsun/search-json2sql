/*
 * Copyright (c) 2020-2030 Sishun.Co.Ltd. All Rights Reserved.
 */
package com.vevor.search.json2sql;

import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.util.JdbcConstants;
import com.vevor.search.json2sql.enums.JoinTypeEnums;
import com.vevor.search.json2sql.enums.OperatorEnums;
import org.apache.commons.lang3.StringUtils;
import javax.naming.ConfigurationException;
import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author ：Murphy ZhangSun
 * @version ：1.9.0
 * @description ：JSON转SQL
 * @program ：user-growth
 * @date ：Created in 2023/7/24 15:40
 * @since ：1.9.0
 */
public class Json2SqlWrapper {

    /**
     * 页面查询配置字段
     */
    private List<FieldConfiguration> fieldConfigurations;

    /**
     * 主表
     */
    private String mainTable;

    /**
     * 查询的字段
     */
    private final List<String> selectItems = new ArrayList<>();

    /**
     * 全部表涉及到的字段
     */
    private final Set<String> allTableFields = new HashSet<>();

    /**
     * 所有的联查表
     */
    private final List<JoinTable<Void, String, Void, String>>
            joinTables = new ArrayList<>();

    private void collectTableFields(Class<?> clazz) {
        String tableName = getTableName(clazz);
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            String fieldName = field.getName();
            fieldName = CaseUtils.camelToSnake(fieldName);
            String tableFieldName = fieldFormat(tableName, fieldName);
            this.allTableFields.add(tableFieldName);
        }
    }

    /**
     * JSON条件转SQL语句
     *
     * @param conditions Where 条件
     * @param format 是否SQL格式化
     * @return SQL语句
     */
    public String convert(List<Condition> conditions, Boolean format) {
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
            Iterator<JoinCondition> joinConditionIterator = joinConditions.iterator();
            while (joinConditionIterator.hasNext()) {
                JoinCondition joinCondition = joinConditionIterator.next();
                PropertyFunction sourceField = joinCondition.getSourceField();
                PropertyFunction targetField = joinCondition.getTargetField();
                String sourceFieldName = sourceField.getFieldName(sourceField);
                String targetFieldName = targetField.getFieldName(targetField);
                sqlBuilder.append(sourceFieldName)
                        .append(" = ")
                        .append(targetFieldName);
                if (joinConditionIterator.hasNext()) {
                    sqlBuilder.append(" AND ");
                }
            }
        }

        // 3. 解析where条件
        String where = Json2SqlEngine.convert(conditions, allTableFields);
        if (StringUtils.isNotBlank(where)) {
            sqlBuilder.append(" WHERE ");
            sqlBuilder.append(where);
        }
        sqlBuilder.append(";");

        // 4. 格式化SQL

        String sql = sqlBuilder.toString();
        return format ? SQLUtils.format(sql, JdbcConstants.MYSQL, SQLUtils.DEFAULT_FORMAT_OPTION)
                : sql;
    }

    private static String getTableName(Class<?> targetTable) {
        String simpleName = targetTable.getSimpleName();
        return CaseUtils.camelToSnake(simpleName);
    }

    /**
     * 字段格式化
     *
     * @param tableName 表名
     * @param fieldName 字段名
     * @return 格式化后的字段名
     */
    private static String fieldFormat(String tableName, String fieldName) {
        return String.format("`%s`.`%s`", tableName, fieldName);
    }

    /**
     * 获取配置字段及支持的操作
     *
     * @return 配置字段类表
     */
    public List<FieldConfiguration> getConfiguredFields() throws ConfigurationException {
        if (fieldConfigurations == null) {
            throw new ConfigurationException("字段未配置");
        }
        return this.fieldConfigurations;
    }

    /**
     * 配置建造者
     */
    public static class Configuration {

        /**
         * 最终要使用的对象，只能通过config()方法产生
         */
        private final Json2SqlWrapper json2SqlWrapper;

        public Configuration() {
            this.json2SqlWrapper = new Json2SqlWrapper();
        }

        public FieldConfig fields() {
            return new FieldConfig(this);
        }

        /**
         * 返回Json2SqlWrapper类给API调用者使用，返回之前，会对配置项进行校验
         *
         * @return Json2SqlWrapper
         */
        public Json2SqlWrapper config() throws ConfigurationException {
            // 参数校验
            // 1. 字段配置
            List<FieldConfiguration> fieldConfigurations =
                    this.json2SqlWrapper.fieldConfigurations;
            if (fieldConfigurations == null || fieldConfigurations.isEmpty()) {
                throw new ConfigurationException("请检查字段类型配置");
            }
            // 2. SQL配置
            if (this.json2SqlWrapper.mainTable == null) {
                throw new ConfigurationException("请检查主表字段配置");
            }
            if (this.json2SqlWrapper.selectItems.isEmpty()) {
                throw new ConfigurationException("请检查查询字段列表配置");
            }
            return this.json2SqlWrapper;
        }
    }

    public static class FieldConfig {
        private final Configuration configuration;
        private final Json2SqlWrapper json2SqlWrapper;

        public FieldConfig(Configuration configuration) {
            this.configuration = configuration;
            this.json2SqlWrapper = this.configuration.json2SqlWrapper;
        }

        @SafeVarargs
        public final <T, R> FieldConfig addFields(TheFieldType<T, R>... fields) {
            for (TheFieldType<T, R> field : fields) {
                this.addField(field);
            }
            return this;
        }

        /**
         * 添加配置字段
         *
         * @param field 字段信息及类型
         * @param <T> 字段父类型
         * ziaram <R> 字段类型
         * @return 字段配置
         */
        public <T, R> FieldConfig addField(TheFieldType<T, R> field) {
            if (json2SqlWrapper.fieldConfigurations == null) {
                json2SqlWrapper.fieldConfigurations = new ArrayList<>();
            }
            PropertyFunction<T, R> fieldFunc = field.getField();
            String fieldName = fieldFunc.getFieldName(fieldFunc);
            OperatorEnums[] operators = field.getFieldType().getOperators();
            List<String> operatorNames = Arrays.stream(operators)
                    .map(Enum::name)
                    .collect(Collectors.toList());

            FieldConfiguration fieldConfiguration = new FieldConfiguration(fieldName, operatorNames);
            json2SqlWrapper.fieldConfigurations.add(fieldConfiguration);
            return this;
        }

        /**
         * SQL配置对象
         *
         * @return 返回一个SQL配置对象
         */
        public SQLConfig sql() {
            return new SQLConfig(this.configuration);
        }
    }

    public static class SQLConfig {
        private final Configuration configuration;
        private final Json2SqlWrapper json2SqlWrapper;
        private JoinTable currentJoinTable;

        public SQLConfig(Configuration configuration) {
            this.configuration = configuration;
            this.json2SqlWrapper = this.configuration.json2SqlWrapper;
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
        public final <T, R> SQLConfig addSelectItems(PropertyFunction<T, R>... selectItems) {
            for (PropertyFunction<T, R> selectItem : selectItems) {
                String fieldName = selectItem.getFieldName(selectItem);
                if (!this.json2SqlWrapper.selectItems.contains(fieldName)) {
                    this.json2SqlWrapper.selectItems.add(fieldName);
                }
            }
            return this;
        }

        /**
         * 设置主表
         *
         * @param mainTableClass 主表
         * @return 当前对象
         */
        public SQLConfig from(Class<?> mainTableClass) {
            this.json2SqlWrapper.mainTable = getTableName(mainTableClass);
            // 添加全表字段
            this.json2SqlWrapper.collectTableFields(mainTableClass);
            return this;
        }

        /**
         * 表连接
         *
         * @param joinTable 连接的表类型
         * @param joinType 连接类型
         * @return 表连接
         */
        public JoinConfig join(Class<?> joinTable, JoinTypeEnums joinType) {
            // 添加/去重
            this.currentJoinTable = new JoinTable<>(joinTable, joinType);
            this.json2SqlWrapper.joinTables.add(this.currentJoinTable);

            // 添加全表字段
            this.json2SqlWrapper.collectTableFields(joinTable);
            return new JoinConfig(this);
        }

        public Json2SqlWrapper config() {
            return this.configuration.json2SqlWrapper;
        }

        public Configuration parent() {
            return this.configuration;
        }
    }

    public static class JoinConfig {
        private final SQLConfig sqlConfig;
        public JoinConfig(SQLConfig sqlConfig) {
            this.sqlConfig = sqlConfig;
        }

        /**
         * 设置联查条件
         *
         *
         * @param left 左表达式
         * @param right 右表达式
         * @param <T1> 输入类型
         * @param <R1> 返回类型
         * @param <T2> 输入类型
         * @param <R2> 返回类型
         * @return 当前对象
         */
        public <T1, R1,T2, R2> JoinConfig addOn(PropertyFunction<T1, R1> left,
                                              PropertyFunction<T2, R2> right) {

            this.sqlConfig.currentJoinTable.on(left, right);
            return this;
        }

        public SQLConfig parent() {
            return this.sqlConfig;
        }
    }
}