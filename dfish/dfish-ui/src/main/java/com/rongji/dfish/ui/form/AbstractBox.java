package com.rongji.dfish.ui.form;

import com.rongji.dfish.ui.HasText;
import com.rongji.dfish.ui.HtmlContentHolder;

/**
 * 单选框或多选框，他们经常作为复杂对象里面可以被选择的项。
 * 比如说grid每行可以有一个 {@link Checkbox} 或  {@link Radio} 
 * @author DFish Team
 * @param <T> 当前对象类型
 */
@SuppressWarnings("unchecked")
public abstract class AbstractBox<T extends AbstractBox<T>> extends AbstractFormElement<T,Object> implements HtmlContentHolder<T>,HasText<T>{
	/**
	 * 
	 */
	private static final long serialVersionUID = -5120066286869690681L;
	protected String target ;
	protected String text;
	protected Object tip;
	protected Boolean checked;
	protected Boolean nobr;
	protected String sync;
	protected Boolean bubble;
	protected BoxField field;
	protected Boolean escape;
	
	public static final String SYNC_CLICK = "click";
	public static final String SYNC_FOCUS = "focus";
	
	/**
	 * 构造函数
	 * @param name 表单元素名
	 * @param label 标题
	 * @param checked 是否选中
	 * @param value 如果选中提交的值
	 * @param text 显示的文本
	 */
	public AbstractBox(String name, String label, Boolean checked, Object value, String text){
		this.setName(name);
		this.setValue(value);
		this.setLabel(label);
		this.setText(text);
		this.setChecked(checked);
//		escape=true;
	}
	
	/**
	 * 绑定 widget 或 widgetID，同步 disabled 属性。
	 * @return target
	 */
	public String getTarget() {
		return target;
	}
	/**
	 * 绑定 widget 或 widget ID，同步 disabled 属性。
	 * @param target widget ID
	 * @return 本身，这样可以继续设置其他属性
	 */
	public T setTarget(String target) {
		this.target = target;
		return (T)this;
	}
	/**
	 * 显示文本。
	 * @return text
	 */
	public String getText() {
		return text;
	}
	/**
	 * 显示文本。
	 * @param text String
	 * @return 本身，这样可以继续设置其他属性
	 */
	public T setText(String text) {
		this.text = text;
		return (T)this;
	}
	/**
	 * 是否默认选中。
	 * @return checked
	 */
	public Boolean getChecked() {
		return checked;
	}
	/**
	 * 是否默认选中。
	 * @param checked Boolean
	 * @return 本身，这样可以继续设置其他属性
	 */
	public T setChecked(Boolean checked) {
		this.checked = checked;
		return (T)this;
	}
	
	/**
	 * 设定该box绑定字段
	 * @return BoxField
	 */
	public BoxField getField() {
		return field;
	}
	
	/**
	 * 该box绑定字段
	 * @param field BoxField
	 * @return this
	 */

	public T setField(BoxField field) {
		this.field = field;
		return (T)this;
	}
	public Boolean getEscape() {
		return this.escape;
	}
	
	public T setEscape(Boolean escape) {
		this.escape = escape;
		return (T)this;
	}

	public Object getTip() {
		return tip;
	}


	public T setTip(Boolean tip) {
		this.tip = tip;
		return (T)this;
	}

	/**
	 * 提示
	 * @param tip String
	 * @return this
	 */
	public T setTip(String tip) {
		this.tip = tip;
		return (T)this;
	}
	/**
	 * 选中状态跟父节点保持同步
	 * @return String
	 */
	public String getSync() {
		return sync;
	}

	/**
	 * 设置选中状态跟父节点保持同步
	 * @param sync Boolean
	 * @return this
	 */
	public T setSync(String sync) {
		this.sync = sync;
		return (T)this;
	}
	
	/**
	 * 点击事件是否冒泡
	 * @return Boolean
	 */
	public Boolean getBubble() {
		return bubble;
	}

	/**
	 * 设置点击事件是否冒泡
	 * @param bubble Boolean
	 * @return this
	 */
	public T setBubble(Boolean bubble) {
		this.bubble = bubble;
		return (T) this;
	}


	public T setValue(Object value) {
		this.value = value;
		return (T) this;
	}
	
	/**
	 * 该选项不换行
	 * @return Boolean
	 */
	public Boolean getNobr() {
		return nobr;
	}
	/**
	 * 该选项不换行
	 * @param nobr Boolean
	 * @return this
	 */
	public T setNobr(Boolean nobr) {
		this.nobr = nobr;
		return (T)this;
	}
}

