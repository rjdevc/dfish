package com.rongji.dfish.ui.form;

import com.rongji.dfish.ui.widget.AbstractButton;

/**
 * 上传按钮。upload/file 和 upload/image 的专用按钮。
 * @author DFish Team
 *
 */
public class UploadButton extends AbstractButton<UploadButton > {

	private static final long serialVersionUID = -2174797939042068668L;

	/**
	 * 构造函数
	 * @param text String 标题
	 */
	public UploadButton(String text) {
		super(text,null,null);
	}

	/**
	 * 构造函数
	 * @param text
	 * @param onClick
	 */
	public UploadButton(String text,String onClick) {
		super(text,onClick,null);
	}

	/**
	 * 构造函数
	 * @param text
	 * @param onClick
	 * @param icon
	 */
	public UploadButton(String text,String onClick, String icon) {
		super(text,onClick,icon);
	}

}
