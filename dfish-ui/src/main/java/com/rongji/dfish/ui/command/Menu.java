package com.rongji.dfish.ui.command;


import com.rongji.dfish.ui.MultiContainer;
import com.rongji.dfish.ui.Widget;
import com.rongji.dfish.ui.widget.AbstractButton;
import com.rongji.dfish.ui.widget.Split;

import java.util.ArrayList;
import java.util.List;

/**
 * 显示一个菜单。menu 既是命令，也是 widget。
 * @author DFish Team
 *
 */
public class Menu extends AbstractDialog<Menu> implements Command<Menu>,
MultiContainer<Menu, Widget<?>>{
	/**
	 * 
	 */
	private static final long serialVersionUID = 7565861352403693874L;
	/**
	 * 默认构造函数
	 */
	public Menu(){
	}

	@Override
    public String getType() {
		return "menu";
	}
	private List<Widget<?>>nodes;
	private Boolean prong;
	/**
	 * 支持增加子按钮
	 * @param btn 按钮
	 * @return 本身，这样可以继续设置其他属性
	 */
	public Menu add(AbstractButton<?> btn){
		add((Widget<?>)btn);
		return this;
	}
	protected Menu add(Widget<?> w){
		if(nodes==null){
			nodes=new ArrayList<>();
		}
		nodes.add(w);
		return this;
	}
	/**
	 * 支持增加分隔符
	 * @param split Split
	 * @return 本身，这样可以继续设置其他属性
	 */
	public Menu add(Split split){
		add((Widget<?>)split);
		return this;
	}

	@Override
    public List<Widget<?>> getNodes() {
		return nodes;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Widget<?>> findNodes() {
		return nodes;
	}

	/**
	 * 设为 true，显示一个箭头，指向 snap 参数对象。
	 * @return Boolean
	 */
	public Boolean getProng() {
		return prong;
	}

	/**
	 * 设为 true，显示一个箭头，指向 snap 参数对象。
	 * @param prong Boolean
	 * @return 本身，这样可以继续设置其他属性
	 */
	public Menu setProng(Boolean prong) {
		this.prong = prong;
		return this;
	}

}
