package com.rongji.dfish.ui.plugin.echarts;

import com.github.abel533.echarts.json.GsonOption;
import com.rongji.dfish.ui.AbstractWidget;

public class EChartsPanel extends AbstractWidget<EChartsPanel>{
	private static final long serialVersionUID = 8385046704642567144L;
	public EChartsPanel(String id){
		setId(id);
	}
	private GsonOption option;
	public GsonOption getOption() {
		return option;
	}
	public EChartsPanel setOption(GsonOption option) {
		this.option = option;
		return this;
	}
	@Override
	public String getType() {
		return "echarts";
	}
	

}
