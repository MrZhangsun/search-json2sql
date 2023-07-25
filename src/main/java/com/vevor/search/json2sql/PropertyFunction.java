/*
 * Copyright (c) 2020-2030 Sishun.Co.Ltd. All Rights Reserved.
 */
package com.vevor.search.json2sql;

import java.io.Serializable;
import java.lang.invoke.SerializedLambda;
import java.lang.reflect.Method;
import java.util.IllegalFormatFlagsException;
import java.util.function.Function;

/**
 * @author ：Murphy ZhangSun
 * @version ：
 * @description ：
 * @program ：user-growth
 * @date ：Created in 2023/7/24 17:13
 * @since ：1.0.0
 */
public interface PropertyFunction<T, R> extends Function<T, R>, Serializable {

    default String getFieldName(PropertyFunction<T, ?> func) {
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

    default String resolveFieldName(String getMethodName) {
        if (getMethodName.startsWith("get")) {
            getMethodName = getMethodName.substring(3);
        } else if (getMethodName.startsWith("is")) {
            getMethodName = getMethodName.substring(2);
        }
        // 小写第一个字母
        return CaseUtils.camelToSnake(getMethodName);
    }

    default String fieldFormat(String tableName, String fieldName) {
        tableName = CaseUtils.camelToSnake(tableName);
        fieldName = CaseUtils.camelToSnake(fieldName);
        return String.format("`%s`.`%s`", tableName, fieldName);
    }


}