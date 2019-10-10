package com.rongji.dfish.ui.command;

import com.rongji.dfish.ui.HasText;

/**
 * JSCommand 封装一个基本的JS命令
 * 
 * @author DFish Team
 * @version 1.0
 * @since XMLTMPL 2.0
 */
public class JSCommand extends AbstractCommand<JSCommand> implements HasText<JSCommand>{
	/**
	 * 
	 */
	private static final long serialVersionUID = -3246276181010606891L;
	private String text;

	/**
	 * @param text
	 */
	public JSCommand(String text) {
		this.text = text;
	}
	
	/**
	 * 空命令
	 */
	public static final JSCommand EMPTY=new JSCommand(null);

	


	/**
	 * 设置 JS内容
	 * 
	 * @param text
	 *            String
	 * @return 本身，这样可以继续设置其他属性
	 */
	public JSCommand setText(String text) {
		this.text = text;
		return this;
	}

	/**
	 * 取得JS内容
	 * 
	 * @return String
	 */
	public String getText() {
		return text;
	}

	public String getType() {
		return "js";
	}


}
