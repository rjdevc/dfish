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
 * @version 1.2
 * @since DFish1.0
 */
public class OnlineBox extends SuggestionBox<OnlineBox> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5233825733860904052L;

	/**
	 * 构造函数
	 * @param name 表单名
	 * @param label 标题
	 * @param value 初始值
	 * @param suggest 在线匹配关键词的 view src。支持 $value 和 $text 变量。
	 */
	public OnlineBox(String name, String label, String value, String suggest) {
		super(name, label, value, suggest);
	}

	/**
	 * 构造函数
	 * @param name 表单名
	 * @param label 标题
	 * @param value 初始值
	 * @param suggest 候选项的弹窗命令
	 */
	public OnlineBox(String name, Label label, String value, Dialog suggest) {
		super(name, label, value, suggest);
	}

	/**
	 * 设置搜索,onlinebox的用法就是suggest=true,所以没有suggest这个参数
	 * @param suggest 是否支持输入
	 * @return this
	 * @deprecated 不支持此方法，该方法调用不会有效果。
	 */
	@Deprecated
    public OnlineBox setSuggest(Boolean suggest) {
		return this;
    }
	
}
