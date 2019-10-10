package com.rongji.dfish.ui.form;

import com.rongji.dfish.ui.FormElement;


/**
 * Period 时间段控件，3.x中的时间段控件实际上就是一个容器，里面可以放置begin和end两个时间选择框。
 * @deprecated 使用Range代替
 * @see Range
 * @author DFish Team
 *
 */
public class Period extends Range {

	private static final long serialVersionUID = -1213706241756141217L;

	/**
	 * 构造函数
	 * @param label String
	 * @param begin  FormElement
	 * @param end FormElement
	 */
	public Period(String label, FormElement<?, ?> begin, FormElement<?, ?> end) {
		super(label, begin, end);
	}
}
