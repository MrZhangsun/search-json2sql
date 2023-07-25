/*
 * Copyright (c) 2020-2030 Sishun.Co.Ltd. All Rights Reserved.
 */
package com.vevor.search.json2sql;

import com.vevor.search.json2sql.enums.JoinTypeEnums;

import java.io.IOException;

/**
 * @author ：Murphy ZhangSun
 * @version ：
 * @description ：
 * @program ：user-growth
 * @date ：Created in 2023/7/24 18:47
 * @since ：
 */
public class Main {


    public static void main(String[] args) throws IOException {
        String resourcePath = "/test.json";
        Json2SqlWrapper json2SqlWrapper = new Json2SqlWrapper(resourcePath);
        String sql = json2SqlWrapper.addSelectItems(Customer::getId, Customer::getAge, Customer::getBirthday)
                .addSelectItems(Order::getcId, Order::getId, Order::getMoney)
                .addSelectItems(Product::getcId, Product::getId, Product::getMoney)
                .from(Order.class)
                .join(Customer.class, JoinTypeEnums.LEFT_JOIN)
                    .on(Order::getcId, Customer::getId)
                .join(Product.class, JoinTypeEnums.FULL_JOIN)
                    .on(Order::getId, Product::getId)
                .convert(true);

        System.out.println(sql);
    }

}