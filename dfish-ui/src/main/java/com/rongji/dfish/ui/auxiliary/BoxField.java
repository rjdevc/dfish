package com.rongji.dfish.ui.auxiliary;

public class BoxField {
    private String value;
    private String text;
    private String search;
    private String remark;
    private String forbid;

    public BoxField(String value) {
        this.value = value;
    }

    public BoxField(String value, String text) {
        this.value = value;
        this.text = text;
    }

    public BoxField(String value, String text, String search) {
        this.value = value;
        this.text = text;
        this.search = search;
    }

    public String getForbid() {
        return forbid;
    }

    public void setForbid(String forbid) {
        this.forbid = forbid;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
