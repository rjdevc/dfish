package com.rongji.dfish.framework.dao;

import java.io.Serializable;

public class SortDefine implements Serializable {

    private static final long serialVersionUID = -447359413768504711L;
    /**
     * 属性名(即对应数据库的列名),按照实体类的属性名来传输
     */
    private String field;
    /**
     * 排序方向,ASC和DESC
     */
    private String direction;

    public SortDefine() {
    }

    public SortDefine(String field, String direction) {
        this.field = field;
        this.direction = direction;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

}
