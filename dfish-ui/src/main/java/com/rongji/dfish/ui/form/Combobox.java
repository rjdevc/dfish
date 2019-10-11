package com.rongji.dfish.ui.form;


import com.rongji.dfish.ui.command.Dialog;

/**
 * <p>
 * Title: XML模板
 * </p>
 * <p>
 * Description: XML模板
 * </p>
 * <p>
 * Copyright: Copyright (c) 2005
 * </p>
 * <p>
 * Company: RJ-SOFT
 * </p>
 * TextselectTag 有输入提示的输入框,可以选择,下拉,以及双击察看详细的功能
 * 
 * @author DFish Team
 * @version 3.0
 * @since XMLTMPL 1.0
 */
public class Combobox extends LinkableSuggestionBox<Combobox> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3265703320204676156L;
	/**
	 * 显示样式-默认
	 */
	public static final String FACE_DEFAULT = "default";
	/**
	 * 显示样式-标签
	 */
	public static final String FACE_TAG = "tag";
	
	private String face;
	
	/**
	 * 构造函数
	 * 
	 * @param name 表单名
	 * @param label 标题
	 * @param value 初始值
	 * @param suggest 候选项的URL
	 */
	public Combobox(String name, String label, String value, String suggest) {
		super(name, label, value, suggest);
	}

	/**
	 * 构造函数
	 *
	 * @param name 表单名
	 * @param label 标题
	 * @param value 初始值
	 * @param suggest 候选项的弹窗命令
	 */
	public Combobox(String name, String label, String value, Dialog suggest) {
		super(name, label, value, suggest);
	}

	@Override
    public String getType() {
		return "combobox";
	}

	public String getFace() {
		return face;
	}

	public Combobox setFace(String face) {
		this.face = face;
		return this;
	}
	
	@Override
    @Deprecated
	public Combobox setTip(Boolean tip) {
		return this;
	}
	
	@Override
    @Deprecated
	public Combobox setTip(String tip) {
		return this;
	}
	
}