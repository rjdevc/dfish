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
 * @since 1.0
 */
public class LinkBox extends LinkableSuggestionBox<LinkBox> {

	private static final long serialVersionUID = 885423787975226663L;

	/**
	 * 构造函数
	 * 
	 * @param name 表单名
	 * @param label 标题
	 * @param value 初始值
	 * @param suggest 候选项的URL
	 */
	public LinkBox(String name, String label, String value, String suggest) {
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
	public LinkBox(String name, String label, String value, Dialog suggest) {
		super(name, label, value, suggest);
	}

}
