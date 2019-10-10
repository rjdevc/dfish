package com.rongji.dfish.ui.command;

/**
 * 插入命令。在某个 widget 内部后置一个或多个 widget。
 * @author DFish Team
 *
 */
public class Append extends AddCommand<Append>{
	/**
	 * 
	 */
	private static final long serialVersionUID = -2369976181411608729L;

	@Override
	public String getType() {
		return "append";
	}

}
