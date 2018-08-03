package com.rongji.dfish.ui.layout.grid;

import com.rongji.dfish.ui.form.Radio;

/**
 * Grid专用的单选框
 * @author YuLM - DFish Team
 *
 */
public class GridRadio extends Radio {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8886839296833661491L;

	public GridRadio(String name, String label, Boolean checked, Object value, String text) {
	    super(name, label, checked, value, text);
    }

	@Override
    public String getType() {
	    return "grid/radio";
    }

}
