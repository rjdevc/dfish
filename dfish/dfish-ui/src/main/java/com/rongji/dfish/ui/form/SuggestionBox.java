package com.rongji.dfish.ui.form;

import com.rongji.dfish.ui.JsonObject;
import com.rongji.dfish.ui.command.DialogCommand;

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
	private DialogCommand src;
//	private Boolean suggest;
	private Boolean multiple;
	private Boolean nobr;
	private DialogCommand drop; //显示所有结果的 view src。;
	private JsonObject picker;
	private Long delay;
	private String separator;

	/**
	 * 构造函数
	 * @param name 表单名
	 * @param label 标题
	 * @param value 初始值
	 * @param src 在线匹配关键词的 view src。支持 $value 和 $text 变量。
	 */
	public SuggestionBox(String name, String label, String value, String src) {
		this.setName(name);
		this.setLabel(label);
		this.setValue(value);
		this.setSrc(src().setSrc(src));
	}

	/**
	 * 构造函数
	 * @param name 表单名
	 * @param label 标题
	 * @param value 初始值
	 * @param src 在线匹配关键词的 view src。支持 $value 和 $text 变量。
	 */
	public SuggestionBox(String name, String label, String value, DialogCommand src) {
		this.setName(name);
		this.setLabel(label);
		this.setValue(value);
		this.setSrc(src);
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
	 * 在线匹配关键词的 view src。支持 $value 和 $text 变量。
	 * @param suggestsrc String
	 * @return 本身，这样可以继续设置其他属性
	 */
	@Deprecated
	@SuppressWarnings("unchecked")
	public T setSuggestsrc(String suggestsrc) {
		this.setSrc(suggestsrc);
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


//	/**
//	 * 在线匹配关键词的 view src。支持 $value 和 $text 变量。
//	 * @return the suggestsrc
//	 */
//	@Deprecated
//	public String getSuggestsrc() {
//		return src;
//	}



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
	public DialogCommand getSrc() {
		return src;
	}

	/**
	 * 获取在线匹配关键词的命令,当不存在时,将创建新的对话框
	 * @return
	 */
	protected DialogCommand src() {
		if (this.src == null) {
			this.src = new DialogCommand();
		}
		return this.src;
	}

	/**
	 * 在线匹配关键词的 view src。支持 $value 和 $text 变量。
	 * @param src 在线匹配关键词的 view src
	 * @return 本身，这样可以继续设置其他属性
	 */
	@SuppressWarnings("unchecked")
    public T setSrc(String src) {
		DialogCommand thisSrc = src().setSrc(src);
		thisSrc.setSrc(src);
		return (T) this;
	}

	/**
	 * 在线匹配关键词的对话框命令
	 * @param src 在线匹配关键词的对话框命令
	 * @return 本身，这样可以继续设置其他属性
	 */
	public T setSrc(DialogCommand src) {
		this.src = src;
		return (T) this;
	}
//	/**
//	 * 输入建议模式，该模式下，不会一次性装载全部数据
//	 * @return Boolean
//	 */
//	public Boolean getSuggest() {
//		return suggest;
//	}
//
//	/**
//	 * 输入建议模式，该模式下，不会一次性装载全部数据
//	 * @param suggest Boolean
//	 * @return 本身，这样可以继续设置其他属性
//	 */
//	@SuppressWarnings({  "unchecked" })
//	public T setSuggest(Boolean suggest) {
//		this.suggest = suggest;
//		return (T) this;
//	}
	
	/**
	 * 显示所有结果的 view src。;
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
	 * 显示所有结果的 view src。;
	 * @param dropsrc 所有结果的 view src
	 * @return 本身，这样可以继续设置其他属性
	 * @see #setDrop(DialogCommand)
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
	protected DialogCommand drop() {
		if (this.drop == null) {
			this.drop = new DialogCommand();
		}
		return this.drop;
	}

	/**
	 * 显示所有选项的下拉对话框。
	 * @return DialogCommand
	 */
	public DialogCommand getDrop() {
		return drop;
	}

	/**
	 * 显示所有选项的下拉对话框。
	 * @param drop DialogCommand
	 * @return 本身，这样可以继续设置其他属性
	 */
	public T setDrop(DialogCommand drop) {
		this.drop = drop;
		return (T) this;
	}

	/**
	 * 输入字符延时时间
	 * @return Long
	 */
	public Long getDelay() {
		return delay;
	}

	/**
	 * 输入字符延时时间
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
