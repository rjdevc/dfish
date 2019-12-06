package com.rongji.dfish.ui.layout.grid;

import com.rongji.dfish.ui.AbstractWidget;

/**
 * 
 * Description: 用于grid的自增序号列
 * Copyright:   Copyright (c)2017
 * Company:     rongji
 * @author     DFish Team - lamontYu
 * @version    1.0
 * Create at:   2017-8-10 上午10:17:30
 * 
 * Modification History:
 * Date			Author				Version		Description
 * ------------------------------------------------------------------
 * 2017-8-10	DFish Team - lamontYu	1.0			1.0 Version
 */
public class GridRownum extends AbstractWidget<GridRownum> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8038094039396279588L;

	@Override
    public String getType() {
	    return "grid/rownum";
    }
	
	public GridRownum() {
	    super();
    }

	public GridRownum(Integer start) {
	    super();
	    this.start = start;
    }

	private Integer start;

	/**
	 * 初始值
	 * @return Integer
	 */
	public Integer getStart() {
		return start;
	}

	/**
	 * 设置初始值
	 * @param start Integer
	 * @return 本身，这样可以继续设置其他属性
	 */
	public GridRownum setStart(Integer start) {
		this.start = start;
		return this;
	}

}
