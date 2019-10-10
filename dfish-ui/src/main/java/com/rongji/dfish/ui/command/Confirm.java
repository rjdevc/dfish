package com.rongji.dfish.ui.command;

import com.rongji.dfish.ui.AbstractDialog;
import com.rongji.dfish.ui.Command;
import com.rongji.dfish.ui.HasText;
import com.rongji.dfish.ui.widget.Button;

import java.util.List;


/**
 * 确定命令。
 * <p>
 * 在执行系列命令，或单格命令前。允许询问一个信息，在征得用户同意后执行命令，否则则终止命令的执行。
 * </p>
 * 
 * @author DFish Team
 * @version 1.0
 */
public class Confirm extends AbstractDialog<Confirm> implements Command<Confirm>,HasText<Confirm> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6715410304552489693L;
	private String btncls;
	private String text;// 显示文本。这里一般是询问的问题内容
//	private Boolean cover;// 如果设为 true, 页面和对话框之间将覆盖一层半透明蒙版。
	private String icon;// 图标。
	private Command<?> yes;// 点击"确定"执行的命令。
	private Command<?> no;// 点击"取消"执行的命令。
	private List<Button> buttons;

	/**
	 * 构造函数
	 * 
	 * @param text 显示文本。这里一般是询问的问题内容
	 */
	public Confirm(String text) {
		this.text = text;
	}
	
	/**
	 * 构造函数
	 * 
	 * @param text 显示文本。这里一般是询问的问题内容
	 * @param yes 点击"确定"执行的命令。
	 */
	public Confirm(String text, Command<?> yes) {
		this.text = text;
		this.yes = yes;
	}
	
	/**
	 * 构造函数
	 * 
	 * @param text 显示文本。这里一般是询问的问题内容
	 * @param yes 点击"确定"执行的命令。
	 * @param no 点击"取消"执行的命令。
	 */
	public Confirm(String text, Command<?> yes, Command<?> no) {
		this.text = text;
		this.yes = yes;
		this.no = no;
	}

	@Override
	public String getType() {
		return "confirm";
	}

	/**
	 * 按钮样式名
	 * @return 按钮样式名
	 */
	public String getBtncls() {
		return btncls;
	}

	/**
	 * 按钮样式名
	 * @param btncls 按钮样式名
	 * @return 本身
	 */
	public Confirm setBtncls(String btncls) {
		this.btncls = btncls;
		return this;
	}

	/**
	 * 显示文本。这里一般是询问的问题内容
	 * @return String
	 */
	@Override
    public String getText() {
		return text;
	}
	/**
	 * 显示文本。这里一般是询问的问题内容
	 * @param text String
	 * @return this
	 */
	@Override
    public Confirm setText(String text) {
		this.text = text;
		return this;
	}
//	/**
//	 * 如果设为 true, 页面和对话框之间将覆盖一层半透明蒙版。
//	 * @return Boolean
//	 */
//	public Boolean getCover() {
//		return cover;
//	}
//	/**
//	 * 如果设为 true, 页面和对话框之间将覆盖一层半透明蒙版。
//	 * @param cover Boolean
//	 * @return this
//	 */
//	public ConfirmCommand setCover(Boolean cover) {
//		this.cover = cover;
//		return this;
//	}
	/**
	 * 图标。
	 * @return String
	 */
	public String getIcon() {
		return icon;
	}
	/**
	 * 图标。
	 * @param icon String
	 * @return this
	 */
	public Confirm setIcon(String icon) {
		this.icon = icon;
		return this;
	}
	/**
	 * 点击"确定"执行的命令。
	 * @return Command
	 */
	public Command<?> getYes() {
		return yes;
	}
	/**
	 * 点击"确定"执行的命令。
	 * @param yes ConfirmCommand
	 * @return this
	 */
	public Confirm setYes(Command<?> yes) {
		this.yes = yes;
		return this;
	}
	/**
	 * 点击"取消"执行的命令。
	 * @return Command
	 */
	public Command<?> getNo() {
		return no;
	}
	/**
	 * 点击"取消"执行的命令。
	 * @param no Command
	 * @return this
	 */
	public Confirm setNo(Command<?> no) {
		this.no = no;
		return this;
	}

	public List<Button> getButtons() {
		return buttons;
	}

	public Confirm setButtons(List<Button> buttons) {
		this.buttons = buttons;
		return this;
	}

}
