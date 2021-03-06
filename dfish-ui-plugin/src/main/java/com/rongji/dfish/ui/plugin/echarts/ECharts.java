package com.rongji.dfish.ui.plugin.echarts;

import com.github.abel533.echarts.json.GsonOption;
import com.rongji.dfish.ui.AbstractWidget;

/**
 * 图表插件，将后台的数据以图表的形式展现，支持支持折线图、柱状图、饼图等展示类型
 */
public class ECharts extends AbstractWidget<ECharts> {
    private static final long serialVersionUID = 8385046704642567144L;

    /**
     * 构造函数
     * @param id
     */
    public ECharts(String id) {
        setId(id);
    }

    private GsonOption option;

    /**
     * ECharts的option对象，可设置标题、图例、工具栏等
     * @return GsonOption
     */
    public GsonOption getOption() {
        return option;
    }

    /**
     * ECharts的option对象，可设置标题、图例、工具栏等
     * @param option
     * @return ECharts
     */
    public ECharts setOption(GsonOption option) {
        this.option = option;
        return this;
    }

}
