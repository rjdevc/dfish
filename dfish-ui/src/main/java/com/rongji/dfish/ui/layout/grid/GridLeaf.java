package com.rongji.dfish.ui.layout.grid;

import com.rongji.dfish.ui.AbstractWidget;
import com.rongji.dfish.ui.HasSrc;

/**
 * GridTreeItem 是可折叠表格中的折叠项
 * <p>在GridRow中添加了下级可折叠的行的时候，GridTreeItem作为一个视觉标志出现在当前行({@link Tr})上。
 * 它前方有一个+号(或-号)点击有展开或折叠效果。</p>
 * <p>多级别的GridTreeItem自动产生缩进效果</p>
 * @see Tr
 * @author DFish Team
 *
 */
public class GridLeaf extends AbstractWidget<GridLeaf> implements HasSrc<GridLeaf>{
	/**
	 * 
	 */
	private static final long serialVersionUID = -7465823398383091843L;
	private String text;
	private String src;
	private String template;
	private String format;
	private Boolean line;
	private Object tip;
	public GridLeaf() {
	    super();
    }
	
	public GridLeaf(String text) {
	    super();
	    this.text = text;
    }

	public String getType() {
		return "grid/leaf";
	}
	/**
	 * 标题
	 * @return String
	 */
	public String getText() {
		return text;
	}
	/**
	 * 标题
	 * @param text String
	 * @return 本身，这样可以继续设置其他属性
	 */
	public GridLeaf setText(String text) {
		this.text = text;
		return this;
	}
	/**
	 * 如果展开的内容是延迟加载的。将在这个URL所指定的资源中获取内容
	 * @return String
	 */
	public String getSrc() {
		return src;
	}
	/**
	 * 如果展开的内容是延迟加载的。将在这个URL所指定的资源中获取内容
	 * @param src String
	 * @return 本身，这样可以继续设置其他属性
	 */
	public GridLeaf setSrc(String src) {
		this.src = src;
		return this;
	}

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
	public String getFormat() {
		return format;
	}

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
	public GridLeaf setFormat(String format) {
		this.format = format;
		return this;
	}

	@Override
	public String getTemplate() {
		return template;
	}

	@Override
	public GridLeaf setTemplate(String template) {
		this.template=template;
		return this;
	}
	/**
	 * 是否显示树结构的辅助线
	 * @return Boolean
	 */
    public Boolean getLine() {
		return line;
	}
    /**
     * 是否显示树结构的辅助线
     * @param line Boolean
     * @return 本身，这样可以继续设置其他属性
     */
	public GridLeaf setLine(Boolean line) {
		this.line = line;
		return this;
	}
	/**
	 * 提示信息。设为true，提示信息将使用text参数的值。
	 * @return tip
	 */
	public Object getTip() {
		return tip;
	}
	/**
	 * 提示信息。设为true，提示信息将使用text参数的值。
	 * @param tip Boolean
	 * @return 本身，这样可以继续设置其他属性
	 */
	public GridLeaf setTip(Boolean tip) {
		this.tip = tip;
		return this;
	}
	/**
	 * 提示信息。设为true，提示信息将使用text参数的值。
	 * @param tip 提示信息
	 * @return 本身，这样可以继续设置其他属性
	 */
	public GridLeaf setTip(String tip) {
		this.tip = tip;
		return this;
	}

}
