package com.rongji.dfish.ui.form;

import com.rongji.dfish.base.Utils;
import com.rongji.dfish.ui.JsonObject;
import com.rongji.dfish.ui.command.Dialog;

/**
 * SuggestionBox 默认可以通过填写出现输入提示的输入框，主要有{@link Combobox} {@link Linkbox}和{@link Onlinebox}
 * @author DFishTeam
 *
 * @param <T> 当前对象类型
 */
public abstract class SuggestionBox<T extends SuggestionBox<T>> extends AbstractInput<T,String> {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3727695759981575245L;
	private Dialog suggest;
//	private Boolean suggest;
	private Boolean multiple;
	private Boolean nobr;
	private Dialog drop; //显示所有结果的 view suggest。;
	private JsonObject picker;
	private Long delay;
	private String separator;

	/**
	 * 构造函数
	 * @param name 表单名
	 * @param label 标题
	 * @param value 初始值
	 * @param suggest 在线匹配关键词的 view suggest。支持 $value 和 $text 变量。
	 */
	public SuggestionBox(String name, String label, String value, String suggest) {
		this.setName(name);
		this.setLabel(label);
		this.setValue(value);
		if (Utils.notEmpty(suggest)) {
			this.setSuggest(src().setSrc(suggest));
		}
	}

	/**
	 * 构造函数
	 * @param name 表单名
	 * @param label 标题
	 * @param value 初始值
	 * @param suggest 在线匹配关键词的 view suggest。支持 $value 和 $text 变量。
	 */
	public SuggestionBox(String name, String label, String value, Dialog suggest) {
		this.setName(name);
		this.setLabel(label);
		this.setValue(value);
		this.setSuggest(suggest);
	}


//	private Integer matchlength;
	/**
	 * 设置是否可以多选
	 * 
	 * @param multiple boolean
	 * @return 本身，这样可以继续设置其他属性
	 */
	@SuppressWarnings("unchecked")
	public T setMultiple(Boolean multiple) {
		this.multiple = multiple;
		return (T)this;
	}

	/**
	 * 在线匹配关键词的 view suggest。支持 $value 和 $text 变量。
	 * @param suggestsrc String
	 * @return 本身，这样可以继续设置其他属性
	 */
	@Deprecated
	@SuppressWarnings("unchecked")
	public T setSuggestsrc(String suggestsrc) {
		this.setSuggest(suggestsrc);
//		this.suggest=true;
		return (T)this;
	}

	/**
	 * "选择"按钮点击动作
	 * @return "选择"组件
	 */
	public JsonObject getPicker() {
		return picker;
	}

	/**
	 * 组件最右侧显示的"选择"组件
	 * @param picker "选择"组件
	 * @return 本身，这样可以继续设置其他属性
	 */
	@SuppressWarnings("unchecked")
	public T setPicker(JsonObject picker) {
		this.picker = picker;
		return (T)this;
	}

	/**
	 * 设置是否可以多选
	 * @return the multiple
	 */
	public Boolean getMultiple() {
		return multiple;
	}

    /**
     * 设置当内容太多的时候不换行
	 * @return the nobr
	 */
	public Boolean getNobr() {
		return nobr;
	}

	/**
	 * @param nobr 设置当内容太多的时候不换行
	 * @return 本身，这样可以继续设置其他属性
	 */
	@SuppressWarnings("unchecked")
	public T setNobr(Boolean nobr) {
		this.nobr = nobr;
		return (T)this;
	}
	
	@SuppressWarnings("unchecked")
    @Override
	public T setValue(Object value) {
		this.value=toString(value);
		return (T) this;
	}
	/**
	 * 在线匹配关键词的动作。支持 $value 和 $text 变量。
	 * @return  String
	 */
	public Dialog getSuggest() {
		return suggest;
	}

	/**
	 * 获取在线匹配关键词的命令,当不存在时,将创建新的对话框
	 * @return
	 */
	protected Dialog src() {
		if (this.suggest == null) {
			this.suggest = new Dialog();
		}
		return this.suggest;
	}

	/**
	 * 在线匹配关键词的 view suggest。支持 $value 和 $text 变量。
	 * @param suggest 在线匹配关键词的 view suggest
	 * @return 本身，这样可以继续设置其他属性
	 */
	@SuppressWarnings("unchecked")
    public T setSuggest(String suggest) {
		Dialog thisSrc = src().setSrc(suggest);
		thisSrc.setSrc(suggest);
		return (T) this;
	}

	/**
	 * 在线匹配关键词的对话框命令
	 * @param suggest 在线匹配关键词的对话框命令
	 * @return 本身，这样可以继续设置其他属性
	 */
	public T setSuggest(Dialog suggest) {
		this.suggest = suggest;
		return (T) this;
	}

	/**
	 * 显示所有结果的 view suggest。;
	 * @return String
	 * @see #getDrop()
	 */
	@Deprecated
	public String getDropsrc() {
		if (this.drop != null) {
			return this.drop.getSrc();
		}
		return null;
	}
	
	/**
	 * 显示所有结果的 view suggest。;
	 * @param dropsrc 所有结果的 view suggest
	 * @return 本身，这样可以继续设置其他属性
	 * @see #setDrop(Dialog)
	 */
	@SuppressWarnings("unchecked")
	@Deprecated
    public T setDropsrc(String dropsrc) {
		drop().setSrc(dropsrc);
		return (T) this;
	}

	/**
	 * 获取下拉对话框,如果不存在则新建
	 * @return DialogCommand
	 */
	protected Dialog drop() {
		if (this.drop == null) {
			this.drop = new Dialog();
		}
		return this.drop;
	}

	/**
	 * 显示所有选项的下拉对话框。
	 * @return DialogCommand
	 */
	public Dialog getDrop() {
		return drop;
	}

	/**
	 * 显示所有选项的下拉对话框。
	 * @param drop DialogCommand
	 * @return 本身，这样可以继续设置其他属性
	 */
	public T setDrop(Dialog drop) {
		this.drop = drop;
		return (T) this;
	}

	/**
	 * 输入字符延时时间,单位:毫秒
	 * @return Long
	 */
	public Long getDelay() {
		return delay;
	}

	/**
	 * 输入字符延时时间,单位:毫秒
	 * @param delay Long
	 * @return 本身，这样可以继续设置其他属性
	 */
	@SuppressWarnings("unchecked")
    public T setDelay(Long delay) {
		this.delay = delay;
		return (T) this;
	}

	/**
	 * 文本选项分隔符，默认是逗号
	 * @return String
	 */
	public String getSeparator() {
		return separator;
	}

	/**
	 * 文本选项分隔符，默认是逗号
	 * @param separator String
	 * @return 本身，这样可以继续设置其他属性
	 */
	@SuppressWarnings("unchecked")
    public T setSeparator(String separator) {
		this.separator = separator;
		return (T) this;
	}

//	/**
//	 * 设置匹配高亮切词长度,<=0或者为空全字匹配,其他说明按照长度切词匹配
//	 * @return Integer
//	 */
//	public Integer getMatchlength() {
//		return matchlength;
//	}
//
//	/**
//	 * 设置匹配高亮切词长度,<=0或者为空全字匹配,其他说明按照长度切词匹配
//	 * @param matchlength 切词长度
//	 * @return 本身，这样可以继续设置其他属性
//	 */
//	public T setMatchlength(Integer matchlength) {
//		this.matchlength = matchlength;
//		return (T) this;
//	}
	
}