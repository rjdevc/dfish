package com.rongji.dfish.ui.form;

import com.rongji.dfish.ui.Command;
import com.rongji.dfish.ui.LazyLoad;
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
	private Boolean multiple;
	private Boolean nobr;
	private Command<?> picker;
	private Long delay;
	private String separator;
	private DialogCommand drop;
	private DialogCommand suggest;
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
	 * "选择"按钮点击动作
	 * @return "选择"按钮点击动作
	 */
	public Command<?> getPicker() {
		return picker;
	}

	/**
	 * "选择"按钮点击动作
	 * @param picker "选择"按钮点击动作
	 * @return 本身，这样可以继续设置其他属性
	 */
	@SuppressWarnings("unchecked")
	public T setPicker(Command<?> picker) {
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

	/**
	 * 设置点击下拉的效果
	 * @return String
	 */
	public DialogCommand getDrop() {
		return drop;
	}

	/**
	 * 点击下拉的效果
	 * @param drop DialogCommand
	 * @return 本身，这样可以继续设置其他属性
	 */
	@SuppressWarnings("unchecked")
	public T setDrop(DialogCommand drop) {
		this.drop = drop;
		return (T) this;
	}

	/**
	 * 设置输入提示的效果
	 * @return String
	 */
	public DialogCommand getSuggest() {
		return suggest;
	}

	/**
	 * 设置输入提示的效果
	 * @param suggest DialogCommand
	 * @return 本身，这样可以继续设置其他属性
	 */
	@SuppressWarnings("unchecked")
	public T setSuggest(DialogCommand suggest) {
		this.suggest = suggest;
		return (T) this;
	}


}
