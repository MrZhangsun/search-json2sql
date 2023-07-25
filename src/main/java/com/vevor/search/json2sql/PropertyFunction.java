/*
 * Copyright (c) 2020-2030 Sishun.Co.Ltd. All Rights Reserved.
 */
package com.vevor.search.json2sql;

import java.io.Serializable;
import java.util.function.Function;

/**
 * @author ：Murphy ZhangSun
 * @version ：
 * @description ：
 * @program ：user-growth
 * @date ：Created in 2023/7/24 17:13
 * @since ：
 */
public interface PropertyFunction<T, R> extends Function<T, R>, Serializable {
}