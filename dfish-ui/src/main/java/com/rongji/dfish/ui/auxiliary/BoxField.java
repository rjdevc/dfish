package com.rongji.dfish.ui.auxiliary;

/**
 * Box专用，字段参数
 */
public class BoxField {
    private String value;
    private String text;
    private String search;
    private String remark;
    private String forbid;

    /**
     * 构造函数
     * @param value 值字段名
     */
    public BoxField(String value) {
        this.value = value;
    }

    /**
     * 构造函数
     * @param value 值字段名
     * @param text 文本字段名
     */
    public BoxField(String value, String text) {
        this.value = value;
        this.text = text;
    }

    /**
     * 构造函数
     * @param value 值字段名
     * @param text 文本字段名
     * @param search 搜索字段名
     */
    public BoxField(String value, String text, String search) {
        this.value = value;
        this.text = text;
        this.search = search;
    }

    /**
     * 禁用字段名
     * @return String
     */
    public String getForbid() {
        return forbid;
    }

    /**
     * 设置禁用字段名
     * @param forbid
     * @return 本身，这样可以继续设置其他属性
     */
    public BoxField setForbid(String forbid) {
        this.forbid = forbid;
        return this;
    }

    /**
     * 备注字段名
     * @return String
     */
    public String getRemark() {
        return remark;
    }

    /**
     * 设置备注字段名
     * @param remark
     * @return 本身，这样可以继续设置其他属性
     */
    public BoxField setRemark(String remark) {
        this.remark = remark;
        return this;
    }

    /**
     * 搜索字段名
     * @return String
     */
    public String getSearch() {
        return search;
    }

    /**
     * 设置搜索字段名
     * @param search
     * @return 本身，这样可以继续设置其他属性
     */
    public BoxField setSearch(String search) {
        this.search = search;
        return this;
    }

    /**
     * 文本字段名
     * @return String
     */
    public String getText() {
        return text;
    }

    /**
     * 设置文本字段名
     * @param text
     * @return 本身，这样可以继续设置其他属性
     */
    public BoxField setText(String text) {
        this.text = text;
        return this;
    }

    /**
     * 值字段名
     * @return String
     */
    public String getValue() {
        return value;
    }

    /**
     * 设置值字段名
     * @param value
     * @return 本身，这样可以继续设置其他属性
     */
    public BoxField setValue(String value) {
        this.value = value;
        return this;
    }
}
