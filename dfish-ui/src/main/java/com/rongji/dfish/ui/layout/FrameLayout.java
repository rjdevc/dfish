package com.rongji.dfish.ui.layout;

import java.util.List;

import com.rongji.dfish.base.Utils;
import com.rongji.dfish.ui.MultiContainer;
import com.rongji.dfish.ui.Widget;


/**
 * 帧布局
 *
 * @author DFish Team
 */
public class FrameLayout extends AbstractLayout<FrameLayout, Widget<?>> implements MultiContainer<FrameLayout, Widget<?>> {
    /**
     *
     */
    private static final long serialVersionUID = -6122938745828564809L;
    protected String dft;

    /**
     * 构造函数
     *
     * @param id String
     */
    public FrameLayout(String id) {
        super(id);
    }

    @Override
    public String getType() {
        return "frame";//
    }

    /**
     * 默认显示 widget 的 ID
     *
     * @return dft
     */
    public String getDft() {
        return dft;
    }

    /**
     * 默认显示 widget 的 ID
     *
     * @param dft 默认值
     * @return 本身，这样可以继续设置其他属性
     */
    public FrameLayout setDft(String dft) {
        this.dft = dft;
        return this;
    }

    @Override
    public List<Widget<?>> getNodes() {
        return nodes;
    }

    /**
     * 在指定的位置添加子面板
     *
     * @param index 位置
     * @param w     N
     * @return 本身，这样可以继续设置其他属性
     */
    public FrameLayout add(int index, Widget<?> w) {
        if (w == null) {
            return this;
        }
        if (w == this) {
            throw new IllegalArgumentException(
                    "can not add widget itself as a sub widget");
        }
        if (index < 0) {
            nodes.add(w);
        } else {
            nodes.add(index, w);
        }
        if (Utils.isEmpty(dft)) {
            // 默认聚焦widgetId显示
            Widget<?> firstNode = nodes.get(0);
            if (firstNode != null) {
                dft = firstNode.getId();
            }
        }
        return this;
    }

    @Override
    public FrameLayout add(Widget<?> w) {
        return add(-1, w);
    }
}
