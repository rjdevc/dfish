package com.rongji.dfish.ui.plugin.echarts;

import com.github.abel533.echarts.json.GsonOption;
import com.rongji.dfish.ui.AbstractWidget;

public class ECharts extends AbstractWidget<ECharts> {
    private static final long serialVersionUID = 8385046704642567144L;

    public ECharts(String id) {
        setId(id);
    }

    private GsonOption option;

    public GsonOption getOption() {
        return option;
    }

    public ECharts setOption(GsonOption option) {
        this.option = option;
        return this;
    }

}
