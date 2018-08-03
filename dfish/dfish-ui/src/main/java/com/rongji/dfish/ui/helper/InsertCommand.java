package com.rongji.dfish.ui.helper;

import com.rongji.dfish.ui.command.AppendCommand;


/**
 * 插入某个命令/解析器/面板/按钮 当它为命令或解析器的时候，insert和update效果都是一样的。
 * 原先view种，如果没有这些命令或解析器就插入。有就更新
 * 
 */
@Deprecated
public class InsertCommand extends AppendCommand{

	private static final long serialVersionUID = 3848321298409180760L;
	
}
