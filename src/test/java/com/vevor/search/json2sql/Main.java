/*
 * Copyright (c) 2020-2030 Sishun.Co.Ltd. All Rights Reserved.
 */
package com.vevor.search.json2sql;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vevor.search.json2sql.enums.FieldTypeEnums;
import com.vevor.search.json2sql.enums.JoinTypeEnums;

import javax.naming.ConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * @author ：Murphy ZhangSun
 * @version ：
 * @description ：
 * @program ：user-growth
 * @date ：Created in 2023/7/24 18:47
 * @since ：
 */
public class Main {


    public static void main(String[] args) throws IOException, ConfigurationException {

        // 第一阶段：配置
        Json2SqlWrapper json2SqlWrapper = new Json2SqlWrapper.Configuration()
                .fields()
                    .addField(new TheFieldType<>(Order::getCode, "订单号", FieldTypeEnums.STRING_IN))
                    .addField(new TheFieldType<>(Order::getCreatedTime, "下单时间", FieldTypeEnums.DATE))
                    .addField(new TheFieldType<>(Customer::getName, "客户姓名", FieldTypeEnums.STRING_LIKE))
                    .addField(new TheFieldType<>(Product::getSku, "库存单位", FieldTypeEnums.STRING_IN))
                .sql()
                    .addSelectItems(Order::getId, Order::getCode, Order::getMoney, Order::getCreatedTime)
                    .addSelectItems(Product::getId, Product::getSku, Product::getTitle, Product::getDescription)
                    .addSelectItems(Customer::getId, Customer::getName, Customer::getAge, Customer::getBirthday)
                    .from(Order.class)
                    .join(Customer.class, JoinTypeEnums.LEFT_JOIN)
                        .addOn(Order::getCustomerId, Customer::getId)
                        .parent()
                    .join(Product.class, JoinTypeEnums.LEFT_JOIN)
                        .addOn(Order::getProductId, Product::getId)
                        .parent()
                    .parent()
                .config();

        // 第二阶段： 查询字段
        List<FieldConfiguration> fieldConfigurations = json2SqlWrapper.getConfiguredFields();
        System.out.println("配置字段：");
        System.out.println(fieldConfigurations);

        // 第三阶段： 转换
        String resourcePath = "/test.json";
        ObjectMapper objectMapper = new ObjectMapper();
        try (InputStream inputStream = Json2SqlWrapper.class.getResourceAsStream(resourcePath)) {
            List<Condition> conditions = objectMapper.readValue(inputStream, new TypeReference<List<Condition>>(){});
            String sql =  json2SqlWrapper.convert(conditions, true);
            System.out.println(sql);
        }
    }
}