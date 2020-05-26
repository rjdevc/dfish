package com.rongji.dfish.framework.dto;

import java.io.Serializable;

/**
 * 用于数据传输交互的选项类,该类包含的属性几乎与dfish-ui中的Option一致,由于包层依赖关系,目前只能独立创建这个类
 */
public class DataOption implements Serializable {

    private static final long serialVersionUID = -2264187221457615319L;
    private Object value;
    private String text;
    private String icon;
    private String status;

    /**
     * 构造函数
     * @param value Object 选项值
     */
    public DataOption(Object value) {
        this.value = value;
    }

    /**
     * 构造函数
     * @param value Object 选项值
     * @param text String 选项文本
     */
    public DataOption(Object value, String text) {
        this.value = value;
        this.text = text;
    }

    /**
     * 构造函数
     * @param value Object 选项值
     * @param text String 选项文本
     * @param icon String 选项图标
     */
    public DataOption(Object value, String text, String icon) {
        this.value = value;
        this.text = text;
        this.icon = icon;
    }

    /**
     * 选项值
     * @return Object
     */
    public Object getValue() {
        return value;
    }

    /**
     * 选项值
     * @param value Object
     * @return 本身，这样可以继续设置其他属性
     */
    public DataOption setValue(Object value) {
        this.value = value;
        return this;
    }

    /**
     * 选项文本
     * @return String
     */
    public String getText() {
        return text;
    }

    /**
     * 选项文本
     * @param text String
     * @return 本身，这样可以继续设置其他属性
     */
    public DataOption setText(String text) {
        this.text = text;
        return this;
    }

    /**
     * 选项图标
     * @return String
     */
    public String getIcon() {
        return icon;
    }

    /**
     * 选项图标
     * @param icon String
     * @return 本身，这样可以继续设置其他属性
     */
    public DataOption setIcon(String icon) {
        this.icon = icon;
        return this;
    }

    /**
     * 选项状态
     * @return String
     */
    public String getStatus() {
        return status;
    }

    /**
     * 选项状态
     * @param status String
     * @return 本身，这样可以继续设置其他属性
     */
    public DataOption setStatus(String status) {
        this.status = status;
        return this;
    }
}
