package com.rongji.dfish.base.util.json.impl;

import com.rongji.dfish.base.util.json.JsonBuilder;

/**
 * Json对象构建器的默认实现类,具体实现类可继承该方法
 * @author lamontYu
 */
public abstract class AbstractJsonBuilder implements JsonBuilder {

    /**
     * 日期格式
     */
    protected String dateFormat;

    @Override
    public String getDateFormat() {
        return dateFormat;
    }

    /**
     * 设置日期格式
     * @param dateFormat 日期格式
     */
    public void setDateFormat(String dateFormat) {
        this.dateFormat = dateFormat;
    }

}
