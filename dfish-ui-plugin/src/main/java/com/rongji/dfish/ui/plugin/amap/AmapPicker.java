package com.rongji.dfish.ui.plugin.amap;

import com.rongji.dfish.ui.form.AbstractFormElement;

/**
 * 高德地图选择框
 * @author lamontYu
 */
public class AmapPicker extends AbstractFormElement<AmapPicker, String> {

	private static final long serialVersionUID = 2234567246638912510L;

	public AmapPicker(String name, String label, Object value) {
		this.setName(name);
		this.setLabel(label);
		this.setValue(value);
	}

	@Override
	public String getValue() {
		return value;
	}

	@Override
	public AmapPicker setValue(Object value) {
		this.value = toString(value);
		return this;
	}

}
