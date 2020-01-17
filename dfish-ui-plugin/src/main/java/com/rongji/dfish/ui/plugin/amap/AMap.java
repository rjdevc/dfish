package com.rongji.dfish.ui.plugin.amap;

import com.rongji.dfish.ui.AbstractNode;
import com.rongji.dfish.ui.AbstractWidget;
import com.rongji.dfish.ui.json.JsonFormat;

import java.util.List;

/**
 * 地图组件
 */
public class AMap extends AbstractWidget<AMap> {

    private String value;

    public AMap(String id) {
        setId(id);
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
    public AMap setValue(String value) {
        this.value = value;
        return this;
    }

    /**
     * 显示的地图的位置信息点
     * @param value AmapValue
     * @return 本身，这样可以继续设置其他属性
     */
    public AMap setValue(Value value) {
        return setValue(toString(value));
    }

    /**
     * 地理坐标值
     *
     * @author DFish Team
     * @version 1.0
     */
    public static class Value extends AbstractNode<Value> {

        private List<Location> address;

        public Value(List<Location> address) {
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
        public Value setAddress(List<Location> address) {
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

}
