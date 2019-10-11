package com.rongji.dfish.ui.command;

import java.util.List;

import com.rongji.dfish.ui.*;
import com.rongji.dfish.ui.widget.Button;

/**
 * 该命令用于打开一个警告框(Dialog)
 * 
 * @author DFish Team
 * @version 2.0
 * @since XMLTMPL 2.0
 */
public class Alert extends AbstractDialog<Alert> implements Command<Alert>,Positionable<Alert>,HasText<Alert>  {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3046146830347964521L;
	private String btncls;
	private String icon;
	private String text;
	private List<Button> buttons;

	/**
	 * 构造函数
	 * @param text 文本
	 */
	public Alert(String text) {
		this.text = text;
	}

	@Override
	public Alert setText(String text) {
		this.text = text;
		return this;
	}

	@Override
	public String getText() {
		return text;
	}

	/**
	 * 按钮样式名
	 * @return String
	 */
	public String getBtncls() {
		return btncls;
	}

	/**
	 * 按钮样式名
	 * @param btncls String
	 * @return this
	 */
	public Alert setBtncls(String btncls) {
		this.btncls = btncls;
		return this;
	}

	/**
	 * 图标
	 * @return the icon
	 * @since XMLTMPL 2.1
	 */
	public String getIcon() {
		return icon;
	}

	/**
	 * 图标
	 * @param icon the icon to set
	 * @return 本身，这样可以继续设置其他属性
	 * @since XMLTMPL 2.1
	 */
	public Alert setIcon(String icon) {
		this.icon = icon;
		return this;
	}

	@Override
	public String getType() {
		return "alert";
	}

	public List<Button> getButtons() {
		return buttons;
	}

	public Alert setButtons(List<Button> buttons) {
		this.buttons = buttons;
		return this;
	}

}
