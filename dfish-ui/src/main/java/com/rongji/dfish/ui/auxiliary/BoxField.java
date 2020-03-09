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

    public BoxField setForbid(String forbid) {
        this.forbid = forbid;
        return this;
    }

    public String getRemark() {
        return remark;
    }

    public BoxField setRemark(String remark) {
        this.remark = remark;
        return this;
    }

    public String getSearch() {
        return search;
    }

    public BoxField setSearch(String search) {
        this.search = search;
        return this;
    }

    public String getText() {
        return text;
    }

    public BoxField setText(String text) {
        this.text = text;
        return this;
    }

    public String getValue() {
        return value;
    }

    public BoxField setValue(String value) {
        this.value = value;
        return this;
    }
}
