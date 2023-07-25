/*
 * Copyright (c) 2020-2030 Sishun.Co.Ltd. All Rights Reserved.
 */
package com.vevor.search.json2sql;

import org.apache.commons.lang3.StringUtils;

/**
 * @author ：Murphy ZhangSun
 * @version ：1.0.0
 * @description ：命名转换工具
 * @program ：search-json2sql
 * @date ：Created in 2023/7/25 17:05
 * @since ：1.0.0
 */
public class CaseUtils {

    private CaseUtils() {}

    public static String camelToSnake(String camelCaseStr) {
        return camelCaseStr.replaceAll("([a-z0-9])([A-Z])", "$1_$2").toLowerCase();
    }

    public static String snakeToCamel(String snakeCaseStr, Boolean firstToLowerCase) {
        StringBuilder result = new StringBuilder();
        String[] parts = snakeCaseStr.split("_");
        for (String part : parts) {
            if (!part.isEmpty()) {
                result.append(Character.toUpperCase(part.charAt(0)));
                result.append(part.substring(1).toLowerCase());
            }
        }

        String camel = result.toString();
        if (firstToLowerCase) {
            camel = firstToLowerCase(camel);
        }
        return camel;
    }

    public static String firstToLowerCase(String param) {
        if (StringUtils.isBlank(param)) {
            return "";
        }
        return param.substring(0, 1).toLowerCase() + param.substring(1);
    }
}