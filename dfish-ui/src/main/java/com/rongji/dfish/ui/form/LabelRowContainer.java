package com.rongji.dfish.ui.form;

import com.rongji.dfish.ui.Container;

/**
 * <p>LabelRowContainer表名这个对象可以可以容纳LabelRow</p>
 * <p>通常情况下，容器可以容纳任何widget，所以，我们可以把LabelRow看作为一个label加一个FormElement。
 * 但是实际上，由于这两个东西必须是联动的。替换的时候一起替换，布局的时候，如果换行也是一起换行，而不可做作为独立对象。
 * 某些封装类将会对这种行为进行封装。我们定义他们为LabelRowContainer</p>
 * 
 * @author DFish Team
 * @since 3.2.0
 * @see LabelRow
 *
 * @param <T> 当前对象类型
 */
public interface LabelRowContainer<T extends LabelRowContainer<T>>  extends Container<T>{

	/**
	 * 标签宽度
	 * @return String
	 */
	public String getLabelWidth() ;

	/**
	 * 设置标签宽度
	 * @param labelWidth String
	 * @return 本身，这样可以继续设置其他属性
	 */
	public T setLabelWidth(String labelWidth);


}
