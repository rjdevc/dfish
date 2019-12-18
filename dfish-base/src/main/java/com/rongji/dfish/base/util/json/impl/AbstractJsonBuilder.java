package com.rongji.dfish.base.util.json.impl;

import com.rongji.dfish.base.util.json.JsonBuilder;

/**
 * Json对象构建器的默认实现类,具体实现类可继承该方法
 * @author lamontYu
 * @date 2019-09-29
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

    public void setDateFormat(String dateFormat) {
        this.dateFormat = dateFormat;
    }

}
