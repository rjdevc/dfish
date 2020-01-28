package com.rongji.dfish.framework.dto;

import java.io.Serializable;

public class DataOption implements Serializable {

    private Object value;
    private String text;
    private String icon;
    private String status;

    public DataOption(Object value, String text) {
        this.value = value;
        this.text = text;
    }

    public DataOption(Object value, String text, String icon) {
        this.value = value;
        this.text = text;
        this.icon = icon;
    }

    public Object getValue() {
        return value;
    }

    public DataOption setValue(Object value) {
        this.value = value;
        return this;
    }

    public String getText() {
        return text;
    }

    public DataOption setText(String text) {
        this.text = text;
        return this;
    }

    public String getIcon() {
        return icon;
    }

    public DataOption setIcon(String icon) {
        this.icon = icon;
        return this;
    }

    public String getStatus() {
        return status;
    }

    public DataOption setStatus(String status) {
        this.status = status;
        return this;
    }
}
