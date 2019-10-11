package com.rongji.dfish.ui.widget;


import java.util.List;

import com.rongji.dfish.ui.*;
import com.rongji.dfish.ui.layout.AbstractLayout;

/**
 * AbstractButton 为抽象按钮类 用于方便扩展多种按钮
 * 
 * @author DFish Team
 * 
 * @param <T> 当前对象类型
 */
@SuppressWarnings("unchecked")
public abstract class AbstractButton<T extends AbstractButton<T>> extends AbstractLayout<T, Widget<?>> implements MultiContainer<T, Widget<?>>,HasFormat<T>,HtmlContentHolder<T> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2205020103042977655L;
	/**
	 * 状态-正常
	 */
	public static final String STATUS_NORMAL = "normal";
	/**
	 * 状态-禁用
	 */
	public static final String STATUS_DISABLED = "disabled";
	
	/**
	 * 默认构造函数
	 */
	public AbstractButton() {
		super(null);
	}

	protected String name;
	protected String icon;
	protected String text;
	protected Object tip;// tip
	protected Boolean closeable;
//	protected Boolean disabled;
	protected String status;
	protected Boolean focus;
	protected Boolean focusable;
	protected String target;
	protected Boolean hoverdrop;
	protected Boolean hidetoggle;
	protected Boolean escape;
	protected String format;

	/**
	 * 是否有关闭图标。
	 * 
	 * @return Boolean
	 */
	public Boolean getCloseable() {
		return closeable;
	}

	/**
	 * 是否有关闭图标。
	 * 
	 * @param closeable
	 *            Boolean
	 * @return 本身，这样可以继续设置其他属性
	 */
	public T setCloseable(Boolean closeable) {
		this.closeable = closeable;
		return (T) this;
	}

	/**
	 * Menu|Dialog menu或dialog。点击按钮时展示。
	 * 
	 * @param more
	 *            Object
	 * @return 本身，这样可以继续设置其他属性
	 */
	public T setMore(Object more) {
		this.more = more;
		return (T) this;
	}

	protected Object more;

	/**
	 * Menu|Dialog menu或dialog。点击按钮时展示。
	 * 
	 * @return Object
	 */
	public Object getMore() {
		return this.more;
	}

//	public T add(Widget<?> oper) {
//		if (oper == null || oper == this) { // 不允许子按钮为空或是自己
//			return (T) this;
//		}
//		if (more == null || !(more instanceof MenuCommand)) {
//			more = new MenuCommand();
//		}
//		MenuCommand menu = (MenuCommand) more;
//		if(oper instanceof AbstractButton){
//			menu.add((AbstractButton<?>)oper);
//		}else if(oper instanceof Split){
//			menu.add((Split)oper);
//		}else{
//			throw new java.lang.UnsupportedOperationException("Only AbstractButton(Button SubmitButton) or split is supported");
//		}
//		return (T) this;
//	}
	
	
	public T add(int index,Widget<?> oper) {
		if(oper instanceof AbstractButton || oper instanceof Split){
			nodes.add(index, oper);
			return (T)this;
		}else{
			throw new java.lang.UnsupportedOperationException("Only AbstractButton(Button SubmitButton) or split is supported");
		}
	}


	/**
	 * 获得 图标
	 * 
	 * @return 图标
	 */
	public String getIcon() {
		return icon;
	}

	/**
	 * 设置图标
	 * 
	 * @param icon 图标
	 * @return 本身，这样可以继续设置其他属性
	 */
	public T setIcon(String icon) {
		this.icon = icon;
		return (T) this;
	}

	/**
	 * 取得标签
	 * 
	 * @return 标签
	 */
	@Override
    public String getText() {
		return text;
	}

	/**
	 * 设置标签
	 * 
	 * @param text 显示文本
	 * @return 本身，这样可以继续设置其他属性
	 */
	@Override
    public T setText(String text) {
		this.text = text;
		return (T) this;
	}

	/**
	 * 设置所触发的动作(一段JS代码)
	 * 
	 * @param onclick String
	 * @return 本身，这样可以继续设置其他属性
	 * @deprecated 2.x的写法。现在统一setOn(EVENT_CLICK,'...');
	 */
	@Deprecated
	public T setOnclick(String onclick) {
		this.setOn(EVENT_CLICK, onclick);
		return (T) this;
	}

	/**
	 * 是否可用
	 * 
	 * @return 是否可用
	 */
	@Deprecated
	public Boolean getDisabled() {
		if (this.status == null) {
			return null;
		}
		return STATUS_DISABLED.equals(this.status);
	}

	/**
	 * 设置 是否禁用
	 * 
	 * @param disabled 禁用
	 * @return 本身，这样可以继续设置其他属性
	 */
	@Deprecated
	public T setDisabled(Boolean disabled) {
		String status = null;
		if (disabled != null) {
			if (disabled) {
				status = STATUS_DISABLED;
			} else {
				status = STATUS_NORMAL;
			}
		}
		return setStatus(status);
	}

	/**
	 * 设置按钮状态
	 * @return String
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * 按钮状态
	 * @param status String
	 * @return 本身，这样可以继续设置其他属性
	 */
	public T setStatus(String status) {
		this.status = status;
		return (T) this;
	}

	/**
	 * 是否焦点模式
	 * 
	 * @return 是否焦点模式
	 */
	public Boolean getFocus() {
		return focus;
	}

	/**
	 * 是否焦点模式
	 * 
	 * @param focus
	 *            Boolean
	 * @return 本身，这样可以继续设置其他属性
	 */
	public T setFocus(Boolean focus) {
		this.focus = focus;
		return (T) this;
	}

	/**
	 * 是否可聚焦
	 * @return Boolean
	 */
	public Boolean getFocusable() {
		return focusable;
	}

	/**
	 * 设置是否可聚焦
	 * @param focusable Boolean
	 * @return 本身，这样可以继续设置其他属性
	 */
	public T setFocusable(Boolean focusable) {
		this.focusable = focusable;
		return (T) this;
	}

	/**
	 * 鼠标悬停的时候的提示文字
	 * 
	 * @return 鼠标悬停的时候的提示文字
	 */
	public Object getTip() {
		return tip;
	}

	/**
	 * 鼠标悬停的时候的提示文字
	 * 
	 * @param tip 提示文字
	 * @return 本身，这样可以继续设置其他属性
	 */
	public T setTip(String tip) {
		this.tip = tip;
		return (T) this;
	}

	/**
	 * 鼠标悬停的时候的提示文字
	 * 
	 * @param tip 提示文字，如果设置true与text相同
	 * @return 本身，这样可以继续设置其他属性
	 */
	public T setTip(Boolean tip) {
		this.tip = tip;
		return (T) this;
	}

	/**
	 * 在一个 view 中设置了相同 name 的 button 将成为一组，focus 只会作用于其中一个。
	 * 
	 * @return String
	 */
	public String getName() {
		return name;
	}

	/**
	 * 在一个 view 中设置了相同 name 的 button 将成为一组，focus 只会作用于其中一个。
	 * 
	 * @param name
	 *            String
	 * @return 本身，这样可以继续设置其他属性
	 */
	public T setName(String name) {
		this.name = name;
		return (T) this;
	}

	/**
	 * 指定一个 film 内的 widget ID，使 button 的 focus 效果和绑定 widget 的显示隐藏效果。
	 * 
	 * @return String
	 */
	public String getTarget() {
		return target;
	}

	/**
	 * 指定一个 film 内的 widget ID，使 button 的 focus 效果和绑定 widget 的显示隐藏效果。
	 * 
	 * @param target
	 *            String
	 * @return 本身，这样可以继续设置其他属性
	 */
	public T setTarget(String target) {
		this.target = target;
		return (T) this;
	}
	@Override
	public List<Widget<?>> getNodes() {
		return nodes;
	}

	/**
	 * 是否当鼠标hover时展开下拉菜单
	 * @return Boolean
	 */
	public Boolean getHoverdrop() {
		return hoverdrop;
	}

	/**
	 * 是否当鼠标hover时展开下拉菜单
	 * @param hoverdrop Boolean
	 * @return 本身，这样可以继续设置其他属性
	 */
	public T setHoverdrop(Boolean hoverdrop) {
		this.hoverdrop = hoverdrop;
		return (T) this;
	}
	
	/**
	 * 是否隐藏 toggle 图标。
	 * @return Boolean 
	 */
	public Boolean getHidetoggle() {
		return hidetoggle;
	}

	/**
	 * 是否隐藏 toggle 图标。
	 * @param hidetoggle Boolean
	 * @return 本身，这样可以继续设置其他属性
	 */
	public T setHidetoggle(Boolean hidetoggle) {
		this.hidetoggle = hidetoggle;
		return (T) this;
	}

	/**
	 * 用于显示文本是否需要转义,不设置默认是true
	 * @return Boolean
	 */
	@Override
    public Boolean getEscape() {
		return escape;
	}

	/**
	 * 用于显示文本是否需要转义,不设置默认是true
	 * @param escape Boolean
	 * @return 本身，这样可以继续设置其他属性
	 */
	@Override
    public T setEscape(Boolean escape) {
		this.escape = escape;
		return (T) this;
	}

	@Override
    public String getFormat() {
		return format;
	}

	@Override
    public T setFormat(String format) {
		this.format = format;
		return (T) this;
	}
}