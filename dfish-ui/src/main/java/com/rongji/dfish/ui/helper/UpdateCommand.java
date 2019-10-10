package com.rongji.dfish.ui.helper;

import com.rongji.dfish.ui.command.ReplaceCommand;



/**
 * <p>UpdateCommand 更新命令</p>
 * <p>里面内置一个view。这个view里面包含需要更新的视图全部或需要更新部分。</p>
 * <p>由于view本身只能由一个子Panel。所以如果需要同时更新2个Panel时，要们直接更新他们的外框架。要么发送2个UpdateCommand
 * </p>
 * @author DFish Team
 * @version 1.1
 * @since XMLTMPL 2.0
 */
@Deprecated
public class UpdateCommand extends ReplaceCommand{

	private static final long serialVersionUID = -1291353060574163117L;
	
}
