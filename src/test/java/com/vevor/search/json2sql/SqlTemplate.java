///*
// * Copyright (c) 2020-2030 Sishun.Co.Ltd. All Rights Reserved.
// */
//package com.vevor.crm.ctm.pojo;
//
//import com.alibaba.druid.sql.SQLUtils;
//import com.alibaba.druid.util.JdbcConstants;
//import com.vevor.crm.ctm.pojo.form.Condition;
//import com.vevor.crm.ctm.pojo.form.Join;
//import com.vevor.json2sql.Json2SqlEngine;
//import lombok.Getter;
//
//import java.util.Iterator;
//import java.util.List;
//
///**
// * @author ：Murphy ZhangSun
// * @version ：1.9.0
// * @description ：SQL模版
// * @program ：user-growth
// * @date ：Created in 2023/7/21 14:24
// * @since ：1.9.0
// */
//@Getter
//public class SqlTemplate {
//
//    private SqlTemplate() {}
//
//    /**
//     * 主表
//     */
//    private String mainTable;
//
//    /**
//     * 表连接模式
//     */
//    private List<Join> joins;
//
//    /**
//     * 查询字段
//     */
//    private List<String> selectFields;
//
//    /**
//     * 查询条件
//     */
//    private List<Condition> conditions;
//
//    public String convert(boolean format) {
//        StringBuilder sqlBuilder = new StringBuilder();
//        sqlBuilder.append("select ");
//        Iterator<String> iterator = selectFields.iterator();
//        while (iterator.hasNext()) {
//            String next = iterator.next();
//            sqlBuilder.append(next);
//            if (iterator.hasNext()) {
//                sqlBuilder.append(", ");
//            }
//        }
//        sqlBuilder.append(" from `")
//        .append(mainTable)
//        .append("` ");
//
//        // join
//        if (joins != null) {
//            for (Join join : joins) {
//                String joinTable = join.getJoin();
//                sqlBuilder.append(joinTable);
//            }
//        }
//
//        // where
//        sqlBuilder.append(" where ");
//        String where = Json2SqlEngine.convert(conditions);
//        sqlBuilder.append(where);
//
//        // 完整SQL
//        String sql = sqlBuilder.toString();
//        return format ? SQLUtils.format(sql, JdbcConstants.MYSQL, SQLUtils.DEFAULT_FORMAT_OPTION)
//                : sql;
//    }
//
//    public static class Builder {
//        private final SqlTemplate sqlTemplate;
//        public Builder() {
//            this.sqlTemplate = new SqlTemplate();
//        }
//        public Builder setMainTable(String mainTable) {
//            this.sqlTemplate.mainTable = mainTable;
//            return this;
//        }
//        public Builder setJoins(List<Join> joins) {
//            this.sqlTemplate.joins = joins;
//            return this;
//        }
//        public Builder setSelectFields(List<String> selectFields) {
//            this.sqlTemplate.selectFields = selectFields;
//            return this;
//        }
//        public Builder setConditions(List<Condition> conditions) {
//            this.sqlTemplate.conditions = conditions;
//            return this;
//        }
//        public SqlTemplate build() {
//            // 必填字段检查
//            return this.sqlTemplate;
//        }
//    }
//}