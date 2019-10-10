package com.rongji.dfish.ui.layout.grid;

/**
 * 表格列提示
 * Description: 
 * Copyright:   Copyright (c)2017
 * Company:     rongji
 * @author     DFish Team - YuLM
 * @version    1.0
 * Create at:   2017-7-31 下午2:16:26
 * 
 * Modification History:
 * Date			Author				Version		Description
 * ------------------------------------------------------------------
 * 2017-7-31	DFish Team - YuLM	1.0			1.0 Version
 */
public class Tip {

	private String field;

	/**
	 * 提示的字段名
	 * @return String
	 */
	public String getField() {
		return field;
	}

	/**
	 * 设置提示的字段名
	 * @param field
	 * @return 本身，这样可以继续设置其他属性
	 */
	public Tip setField(String field) {
		this.field = field;
		return this;
	}
	
}
