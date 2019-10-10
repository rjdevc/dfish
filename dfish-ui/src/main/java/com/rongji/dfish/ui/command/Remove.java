package com.rongji.dfish.ui.command;


/**
 * 删除命令。删除某个 widget。
 *@author DFish Team
 *
 */
public class Remove extends NodeControlCommand<Remove>{
	/**
	 * 
	 */
	private static final long serialVersionUID = -5276859092129055953L;
	/**
	 * 默认构造函数
	 */
	public Remove(){}
	/**
	 * 构造函数
	 * @param target String目标的ID
	 */
	public Remove(String target){
		setTarget(target);
	}
	@Override
	public String getType() {
		return "remove";
	}
}
