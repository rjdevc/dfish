package com.rongji.dfish.ui;

/**
 * HtmlContentHolder 定义了该标签/片断，可以容纳HTML内容，而不仅仅可以容纳纯文本
 * @author DFish Team
 * @version 2.0
 * @param <T> 当前对象类型
 * @since XMLTMPL 1.0
 * FIXME 部分控件如TEXT的value可能需要转移，可能需要特殊处理，如果这里不能做统一管理的话。
 * 则需要 FormElement Hidden  等组件getValue独立处理。
 * Label Button Table Tree 几乎所有的表单元素可显示的部分，(label value) 和Hidden的(value)
 */
public interface HtmlContentHolder<T extends HtmlContentHolder<T>> {

	/**
	 * 取得转义配置
	 * @return 取得转义配置
	 */
    Boolean getEscape();
    
    /**
     * 设置转义配置，当转义配置为true的情况下放入该标签/片断将按纯文本处理
     * 如果为true小于号等纯文本字符将被转义，而在页面上直接显示出来。
     * 如果转义配置为true 那么，将当做转移处理。以便页面显示出完整的内容
     * @param escape Boolean
     * @return XMLFrag
     */
    T setEscape(Boolean escape);
}
