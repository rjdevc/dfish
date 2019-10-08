package com.rongji.dfish.ui.form;

import com.rongji.dfish.ui.HasText;
import com.rongji.dfish.ui.PubHolder;
import com.rongji.dfish.ui.command.DialogCommand;
import com.rongji.dfish.ui.layout.View;

/**
 * LinkableSuggestionBox 默认可以通过填写出现输入提示的输入框，且所选择的选项可支持链接，主要有{@link Combobox}和{@link Linkbox}
 * @author DFish Team
 *
 * @param <T> 当前对象类型
 */
@SuppressWarnings("unchecked")
public abstract class LinkableSuggestionBox<T extends LinkableSuggestionBox<T>> extends SuggestionBox<T> implements HasText<T>,PubHolder<T,ComboboxOption> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1444093499873660133L;
	private String text;
	private Boolean escape;
	private Boolean strict;
	private ComboboxOption pub;
//	private String src;//在线匹配关键词的 view src。支持 $value 和 $text 变量。
	private String loadingtext;
//	private Boolean suggest;
//	private View node;

	/**
	 * 构造函数
	 *
	 * @param name 表单名
	 * @param label 标题
	 * @param value 初始值
	 * @param suggest 候选项的URL
	 */
	public LinkableSuggestionBox(String name, String label, String value, String suggest) {
		super(name, label, value, suggest);
	}

	/**
	 * 构造函数
	 *
	 * @param name 表单名
	 * @param label 标题
	 * @param value 初始值
	 * @param suggest 候选项的弹窗命令
	 */
	public LinkableSuggestionBox(String name, String label, String value, DialogCommand suggest) {
		super(name, label, value, suggest);
	}

	public T setText(String text) {
		this.text = text;
		return (T) this;
	}

	public String getText() {
		return text;
	}

	public T setEscape(Boolean escape) {
		this.escape = escape;
		return (T) this;
	}
	public Boolean getEscape(){
		return escape;
	}
	
	/**
	 * 设置是否严格检查输入值必须有效。如果值为真则输入置必须在提示信息那有才能提交。 如果值为假输入值也当做值来提交
	 * 
	 * @param strict boolean
	 * @return 本身，这样可以继续设置其他属性
	 */
    public T setStrict(Boolean strict) {
		this.strict = strict;
		return (T) this;
	}

	/**
	 * 设置是否严格检查输入值必须有效。如果值为真则输入置必须在提示信息那有才能提交。 如果值为假输入值也当做值来提交
	 * @return the strict
	 */
	public Boolean getStrict() {
		return strict;
	}

	public ComboboxOption getPub() {
		if (pub == null) {
			pub = new ComboboxOption();
		}
		return pub;
	}


	public T setPub(ComboboxOption pub) {
		this.pub = pub;
		return (T) this;
	}

	/**
	 * 加载时显示的文本
	 * @return String
	 */
	public String getLoadingtext() {
		return loadingtext;
	}

	/**
	 * 加载时显示的文本
	 * @param loadingtext 加载时显示的文本
	 * @return this
	 */
	public T setLoadingtext(String loadingtext) {
		this.loadingtext = loadingtext;
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
//	public T setSuggest(Boolean suggest) {
//		this.suggest = suggest;
//		return (T) this;
//	}

//	/**
//	 * 选项节点的View
//	 * @return View
//	 * @author lamontYu
//	 */
//	public View getNode() {
//		return node;
//	}
//
//	/**
//	 * 选项节点的View
//	 * @param node 视图
//	 * @return 本身，这样可以继续设置其他属性
//	 * @author lamontYu
//	 */
//	public T setNode(View node) {
//		this.node = node;
//		return (T) this;
//	}
	
}
