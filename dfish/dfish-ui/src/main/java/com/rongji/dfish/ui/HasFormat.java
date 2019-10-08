package com.rongji.dfish.ui;

public interface HasFormat<T extends HasFormat<T>> extends HasText<T> {

    /**
     * 格式化内容。支持替换 "$field" 和 "${field.prop}" 形式的变量。支持"javascript:"开头的js语句(需return返回值)。
     * 如果列表有多行，并且这个字段显示的时候，需要一个复杂HTML，而每行中需要的变化的仅仅是少量的数据，可以使用format来减少传输量。
     * 典型的有两种写法
     * <pre>
     * javascript:var d= this.x.data.s;if('1'==d){return \"&lt;span style='color:gray'&gt;唯一&lt;/span&gt;\"};return '';
     * </pre>或<pre>
     * [&lt;a href='javascript:;' onclick=\"demo.enterView(this,'$vId');\"&gt;查看&lt;/a&gt;]&amp;nbsp;
     * </pre>
     * @param format String
     * @return 本身，这样可以继续设置其他属性
     */
    T setFormat(String format);
    /**
     * 格式化内容。支持替换 "$field" 和 "${field.prop}" 形式的变量。支持"javascript:"开头的js语句(需return返回值)。
     * 如果列表有多行，并且这个字段显示的时候，需要一个复杂HTML，而每行中需要的变化的仅仅是少量的数据，可以使用format来减少传输量。
     * 典型的有两种写法
     * <pre>
     * javascript:var d= this.x.data.s;if('1'==d){return \"&lt;span style='color:gray'&gt;唯一&lt;/span&gt;\"};return '';
     * </pre>或<pre>
     * [&lt;a href='javascript:;' onclick=\"demo.enterView(this,'$vId');\"&gt;查看&lt;/a&gt;]&amp;nbsp;
     * </pre>
     *
     * @return String
     */
    String getFormat();
}
