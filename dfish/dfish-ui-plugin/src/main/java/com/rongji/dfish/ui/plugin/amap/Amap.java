package com.rongji.dfish.ui.plugin.amap;

import com.rongji.dfish.ui.AbstractWidget;

/**
 * 地图组件
 */
public class Amap extends AbstractWidget<Amap> {

    private String value;

    public Amap(String id) {
        setId(id);
    }

    @Override
    public String getType() {
        return "amap";
    }

    /**
     * 显示的地图的位置信息点
     * @return AmapValue
     */
    public String getValue() {
        return value;
    }

    /**
     * 显示的地图的位置信息点
     * @param value String
     * @return 本身，这样可以继续设置其他属性
     */
    public Amap setValue(String value) {
        this.value = value;
        return this;
    }

    /**
     * 显示的地图的位置信息点
     * @param value AmapValue
     * @return 本身，这样可以继续设置其他属性
     */
    public Amap setValue(AmapValue value) {
        return setValue(toString(value));
    }

}
