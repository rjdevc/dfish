package com.rongji.dfish.ui.helper;

import com.rongji.dfish.ui.form.FormGroup;

/**
 * 与HorzontalPanel用法一样，多了setLabel，可以添加标题，可能有些属性不全，后期补上
 * @deprecated use FormGroup instead
 * @see com.rongji.dfish.ui.form.FormGroup
 */
@Deprecated
public class HorizontalGroup extends FormGroup{
	private static final long serialVersionUID = -8524199135154814449L;

	public HorizontalGroup(String label) {
		super(label);
	}
}