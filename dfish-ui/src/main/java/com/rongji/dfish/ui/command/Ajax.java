package com.rongji.dfish.ui.command;



/**
 * AjaxCommand 封装了这样一个命令，该命令发送一个 http 请求到服务器。服务端应当返回一个命令格式JSON。
 * @author DFish Team
 * @version 2.0
 * @since DFish 2.0
 * 
 */
public class Ajax extends CommunicateCommand<Ajax> {
	

    
	/**
	 * 
	 */
	private static final long serialVersionUID = 8222929005827829591L;


	/**
	 * 最简构造函数
	 * @param src String
	 */
    public Ajax(String src) {
    	super(src);
    }


	@Override
    public String getType() {
		return "ajax";
	}


}
