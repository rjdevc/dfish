package com.rongji.dfish.ui.form;

import com.rongji.dfish.ui.widget.AbstractButton;

/**
 * 上传按钮。upload/file 和 upload/image 的专用按钮。
 * @author DFish Team
 *
 */
public class ValueButton extends AbstractButton<ValueButton> {

	private static final long serialVersionUID = 7556749223547329191L;

	/**
	 * 构造函数
	 * @param text String 标题
	 */
	public ValueButton(String text) {
		super(text,null,null);
	}

	/**
	 * 构造函数
	 * @param text 文本
	 * @param onClick 点击动作
	 */
    public ValueButton(String text,String onClick) {
        super(text,onClick,null);
    }

	/**
	 * 构造函数
	 * @param text 文本
	 * @param onClick 点击动作
	 * @param icon 图标
	 */
	public ValueButton(String text,String onClick, String icon) {
		super(text,onClick,icon);
	}
  
	@Override
	public String getType() {
		return null;
	}

}
