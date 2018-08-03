package com.rongji.dfish.ui.form;

import com.rongji.dfish.ui.widget.AbstractButton;

/**
 * 上传按钮。upload/file 和 upload/image 的专用按钮。
 * @author DFish Team
 *
 */
public class UploadButton extends AbstractButton<UploadButton > {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2174797939042068668L;

	/**
	 * 
	 * 构造函数
	 * @param icon String 图标
	 * @param text String 标题
	 */
	public UploadButton(String icon, String text) {
		this.setIcon(icon);
		this.setText(text);
      
	}
	/**
	 * 构造函数
	 * @param text String 标题
	 */
	public UploadButton( String text) {
		this.setText(text);
	}
  
	@Override
	public String getType() {
		return "upload/button";
	}
	
}
