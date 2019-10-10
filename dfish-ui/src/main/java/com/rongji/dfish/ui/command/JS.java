package com.rongji.dfish.ui.command;

/**
 * JSCommand 封装一个基本的JS命令
 * 
 * @author DFish Team
 * @version 1.0
 * @since XMLTMPL 2.0
 */
public class JS extends AbstractCommand<JS> {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3246276181010606891L;
	private String text;

	/**
	 * 构造函数
	 * @param text 文本内容
	 */
	public JS(String text) {
		this.text = text;
	}
	
	/**
	 * 空命令
	 */
	public static final JS EMPTY=new JS(null);

	


	/**
	 * 设置 JS内容
	 * 
	 * @param text
	 *            String
	 * @return 本身，这样可以继续设置其他属性
	 */
	public JS setText(String text) {
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

	@Override
    public String getType() {
		return "js";
	}


}
