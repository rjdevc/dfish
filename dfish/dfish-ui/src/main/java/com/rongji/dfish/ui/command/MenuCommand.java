package com.rongji.dfish.ui.command;


import java.util.ArrayList;
import java.util.List;

import com.rongji.dfish.ui.Command;
import com.rongji.dfish.ui.MultiContainer;
import com.rongji.dfish.ui.Widget;
import com.rongji.dfish.ui.widget.AbstractButton;
import com.rongji.dfish.ui.widget.Split;

/**
 * 显示一个菜单。menu 既是命令，也是 widget。
 * @author DFish Team
 *
 */
public class MenuCommand extends AbstractCommand<MenuCommand> implements Command<MenuCommand>,
MultiContainer<MenuCommand, Widget<?>>{
	/**
	 * 
	 */
	private static final long serialVersionUID = 7565861352403693874L;
	/**
	 * 默认构造函数
	 */
	public MenuCommand(){
	}

	public String getType() {
		return "menu";
	}
	private List<Widget<?>>nodes;
	/**
	 * 支持增加子按钮
	 * @param btn 按钮
	 * @return this
	 */
	public MenuCommand add(AbstractButton<?> btn){
		add((Widget<?>)btn);
		return this;
	}
	protected MenuCommand add(Widget<?> w){
		if(nodes==null){
			nodes=new ArrayList<Widget<?>>();
		}
		nodes.add(w);
		return this;
	}
	/**
	 * 支持增加分隔符
	 * @param split Split
	 * @return this
	 */
	public MenuCommand add(Split split){
		add((Widget<?>)split);
		return this;
	}

	public List<Widget<?>> getNodes() {
		return nodes;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<? extends Object> findNodes() {
		return nodes;
	}

}
