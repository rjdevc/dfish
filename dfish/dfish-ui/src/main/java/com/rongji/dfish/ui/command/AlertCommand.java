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
public class AlertCommand extends AbstractDialog<AlertCommand> implements Command<AlertCommand>,Positionable<AlertCommand>,HasText<AlertCommand>  {

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
	public AlertCommand(String text) {
		this.text = text;
	}

	/**
	 * 设置内容
	 * @param text 内容
	 * @return 本身，这样可以继续设置其他属性
	 */
	public AlertCommand setText(String text) {
		this.text = text;
		return this;
	}

	/**
	 * 内容
	 * @return the content
	 * @since XMLTMPL 2.1
	 */
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
	public AlertCommand setBtncls(String btncls) {
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
	public AlertCommand setIcon(String icon) {
		this.icon = icon;
		return this;
	}

	public String getType() {
		return "alert";
	}

	public List<Button> getButtons() {
		return buttons;
	}

	public AlertCommand setButtons(List<Button> buttons) {
		this.buttons = buttons;
		return this;
	}

}
