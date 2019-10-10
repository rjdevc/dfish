package com.rongji.dfish.ui.plugin.amap;

import com.rongji.dfish.ui.form.AbstractFormElement;

/**
 * 
 * Description: 地图选择组件
 * Copyright:   Copyright (c)2017
 * Company:     rongji
 * @author:     DFish Team - YuLM
 * @version:    1.0
 * Create at:   2017-8-16 下午2:36:11
 * 
 * Modification History:
 * Date			Author				Version		Description
 * ------------------------------------------------------------------
 * 2017-8-16	DFish Team - YuLM	1.0			1.0 Version
 */
public class AmapPicker extends AbstractFormElement<AmapPicker, String> {

	private static final long serialVersionUID = 2234567246638912510L;

	@Override
    public String getType() {
	    return "amap/picker";
    }
	
	public AmapPicker(String name, String label, Object value) {
		this.setName(name);
		this.setLabel(label);
		this.setValue(value);
	}

	public String getValue() {
		return value;
	}

	public AmapPicker setValue(Object value) {
		this.value = toString(value);
		return this;
	}
	

}
