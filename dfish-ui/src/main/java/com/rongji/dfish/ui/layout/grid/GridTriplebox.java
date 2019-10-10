package com.rongji.dfish.ui.layout.grid;

import com.rongji.dfish.ui.form.Triplebox;

/**
 * Grid专用的复选框
 * @author YuLM - DFish Team
 *
 */
public class GridTriplebox extends Triplebox {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4770736316914887083L;

	public GridTriplebox(String name, String label, Integer status, Object value, String text) {
	    super(name, label, status, value, text);
    }

	@Override
    public String getType() {
	    return "grid/triplebox";
    }

}
