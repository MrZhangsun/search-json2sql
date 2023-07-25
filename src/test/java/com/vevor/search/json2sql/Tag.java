/*
 * Copyright (c) 2020-2030 Sishun.Co.Ltd. All Rights Reserved.
 */
package com.vevor.search.json2sql;

import lombok.Data;

import java.util.Date;

/**
 * @author ：Murphy ZhangSun
 * @version ：1.9.0
 * @description ：标签
 * @program ：user-growth
 * @date ：Created in 2023/7/19 18:36
 * @since ：1.9.0
 */
@Data
public class Tag {

    /**
     * 标签ID
     */
    private int id;

    /**
     * 标签分类ID
     */
    private int pId;

    /**
     * 标签名称
     */
    private String label;

    /**
     * 标签编码（唯一）
     */
    private String code;

    /**
     * 标签值类型： 字符串，数值，日期
     */
    private String valueType;

    /**
     * 标签最新一次圈选人数
     */
    private long number;

    /**
     * 标签计算规则
     */
    private String rule;

    /**
     * 标签计算结果值有哪些，如： 共 3 个值, 分别为: 高度流失、轻度流失、中度流失
     */
    private Object[] values;

    /**
     * 最新版本计算状态
     */
    private String runningStatus;

    /**
     * 技术更新方式：每天定时，立即更新
     */
    private String updateMethod;

    /**
     * 标签状态，审核中，正常运行
     */
    private String status;

    /**
     * 创建方式： SQL，自定义标签值，基础指标值，首次末次特征...
     */
    private String creationMethod;

    /**
     * 创建人
     */
    private String createdBy;

    /**
     * 创建时间
     */
    private Date createdTime;

    /**
     * 更新人
     */
    private String updatedBy;

    /**
     * 更新时间
     */
    private Date updatedTime;

    /**
     * 是否删除
     */
    private Boolean deleted;
}