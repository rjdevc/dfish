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
	 * 
	 * 构造函数
	 * @param icon String 图标
	 * @param text String 标题
	 */
	public ValueButton(String icon, String text) {
		this.setIcon(icon);
		this.setText(text);
      
	}
	/**
	 * 构造函数
	 * @param text String 标题
	 */
	public ValueButton(String text) {
		this.setText(text);
	}
  
	@Override
	public String getType() {
		return null;
	}

}
