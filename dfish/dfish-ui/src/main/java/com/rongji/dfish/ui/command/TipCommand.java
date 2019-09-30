package com.rongji.dfish.ui.command;

import com.rongji.dfish.ui.*;

/**
 * 提示信息
 * @author DFish Team
 *
 */
public class TipCommand extends AbstractDialog<TipCommand> implements Command<TipCommand>,Snapable<TipCommand>,HasText<TipCommand> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3534531697064109684L;
	private String text;
	private Boolean hoverdrop;
	
	/**
	 * 构造函数
	 * @param text 提示文本
	 */
	public TipCommand(String text) {
		this.text = text;
	}
	
	@Override
	public String getType() {
		return "tip";
	}

    /**
     * 内容
     * @return String
     */
	public String getText() {
		return text;
	}
	/**
	 * 设置内容
	 * @param text 内容
	 * @return 本身，这样可以继续设置其他属性
	 */
	public TipCommand setText(String text) {
		this.text = text;
		return this;
	}

	/**
	 * 鼠标移开时tip自动关闭
	 * @return Boolean
	 */
	public Boolean getHoverdrop() {
		return hoverdrop;
	}

	/**
	 * 设置鼠标移开时tip自动关闭
	 * @param hoverdrop Boolean
	 * @return 本身，这样可以继续设置其他属性
	 */
	public TipCommand setHoverdrop(Boolean hoverdrop) {
		this.hoverdrop = hoverdrop;
		return this;
	}
	
}
