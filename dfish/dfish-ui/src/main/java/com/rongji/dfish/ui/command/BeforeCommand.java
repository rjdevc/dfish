package com.rongji.dfish.ui.command;

import java.util.Map;

/**
 * 插入命令。在某个 widget 之前插入一个或多个 widget。
 * @author DFish Team
 *
 */
public class BeforeCommand extends AddCommand<BeforeCommand> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7957942021832245204L;

	@Override
	public String getType() {
		return "before";
	}

}
