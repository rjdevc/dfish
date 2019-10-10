package com.rongji.dfish.ui.command;

/**
 * 插入命令。在某个 widget 内部前置一个或多个 widget。
 * @author DFish Team
 *
 */
public class Prepend extends AddCommand<Prepend>{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6112878724451958092L;

	@Override
	public String getType() {
		return "prepend";
	}

}
