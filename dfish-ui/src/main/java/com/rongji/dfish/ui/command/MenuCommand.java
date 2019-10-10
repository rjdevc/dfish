package com.rongji.dfish.ui.command;


import com.rongji.dfish.ui.AbstractDialog;
import com.rongji.dfish.ui.Command;
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
public class MenuCommand extends AbstractDialog<MenuCommand> implements Command<MenuCommand>,
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
//	private Integer indent;
	private Boolean prong;
//	private String  snap;
//	private String  snaptype;
//	private Integer timeout;
	/**
	 * 支持增加子按钮
	 * @param btn 按钮
	 * @return 本身，这样可以继续设置其他属性
	 */
	public MenuCommand add(AbstractButton<?> btn){
		add((Widget<?>)btn);
		return this;
	}
	protected MenuCommand add(Widget<?> w){
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
	public MenuCommand add(Split split){
		add((Widget<?>)split);
		return this;
	}

	public List<Widget<?>> getNodes() {
		return nodes;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Widget<?>> findNodes() {
		return nodes;
	}

//	/**
//	 * 当设置了 snap 时，再设置 indent 指定相对于初始位置缩进多少个像素。
//	 * @return Integer
//	 */
//	public Integer getIndent() {
//		return indent;
//	}
//
//	/**
//	 * 当设置了 snap 时，再设置 indent 指定相对于初始位置缩进多少个像素。
//	 * @param indent Integer
//	 * @return 本身，这样可以继续设置其他属性
//	 */
//	public MenuCommand setIndent(Integer indent) {
//		this.indent = indent;
//		return this;
//	}

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
	public MenuCommand setProng(Boolean prong) {
		this.prong = prong;
		return this;
	}

//	/**
//	 * 吸附的对象。可以是 html 元素或 widget ID。
//	 * @return String
//	 */
//	public String getSnap() {
//		return snap;
//	}
//
//	/**
//	 * 吸附的对象。可以是 html 元素或 widget ID。
//	 * @param snap 吸附的对象
//	 * @return 本身，这样可以继续设置其他属性
//	 */
//	public MenuCommand setSnap(String snap) {
//		this.snap = snap;
//		return this;
//	}
//
//	/**
//	 * 指定 snap 的位置。
//	 * 可选值: 11,12,14,21,22,23,32,33,34,41,43,44,bb,bt,tb,tt,ll,lr,rl,rr,cc。
//	 * 其中 1、2、3、4、t、r、b、l、c 分别代表左上角、右上角、右下角、左下角、上中、右中，下中、左中、中心。
//	 * 例如 "41" 表示 snap 对象的左下角和 Dialog 对象的左上角吸附在一起。
//	 * @return String
//	 */
//	public String getSnaptype() {
//		return snaptype;
//	}
//
//	/**
//	 * 指定 snap 的位置。
//	 * 可选值: 11,12,14,21,22,23,32,33,34,41,43,44,bb,bt,tb,tt,ll,lr,rl,rr,cc。
//	 * 其中 1、2、3、4、t、r、b、l、c 分别代表左上角、右上角、右下角、左下角、上中、右中，下中、左中、中心。
//	 * 例如 "41" 表示 snap 对象的左下角和 Dialog 对象的左上角吸附在一起。
//	 * @param snaptype String
//	 * @return 本身，这样可以继续设置其他属性
//	 */
//	public MenuCommand setSnaptype(String snaptype) {
//		this.snaptype = snaptype;
//		return this;
//	}
//
//	/**
//	 * 定时关闭，单位:秒。
//	 * @return Integer
//	 */
//	public Integer getTimeout() {
//		return timeout;
//	}
//
//	/**
//	 * 定时关闭，单位:秒。
//	 * @param timeout Integer
//	 * @return 本身，这样可以继续设置其他属性
//	 */
//	public MenuCommand setTimeout(Integer timeout) {
//		this.timeout = timeout;
//		return this;
//	}

}
