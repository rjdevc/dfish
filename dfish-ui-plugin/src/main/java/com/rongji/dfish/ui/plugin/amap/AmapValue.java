package com.rongji.dfish.ui.plugin.amap;

import com.rongji.dfish.ui.AbstractNode ;
import com.rongji.dfish.ui.json.JsonFormat;

import java.util.List;

/**
 * 地理坐标值
 *
 * @author DFish Team
 * @version 1.0
 */
public class AmapValue extends AbstractNode<AmapValue> {

    private List<Location> address;

    public AmapValue(List<Location> address) {
        this.address = address;
    }

    /**
     * 地图坐标点
     * @return List
     */
    public List<Location> getAddress() {
        return address;
    }

    /**
     * 地图坐标
     * @param address List
     * @return 本身，这样可以继续设置其他属性
     */
    public AmapValue setAddress(List<Location> address) {
        this.address = address;
        return this;
    }

    @Override
    public String getType() {
        return null;
    }

    @Override
    public String toString() {
        return JsonFormat.toJson(this);
    }
}
