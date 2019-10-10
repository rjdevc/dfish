package com.rongji.dfish.ui.layout.grid;

import com.rongji.dfish.ui.AbstractWidget;
import com.rongji.dfish.ui.HasFormat;
import com.rongji.dfish.ui.HasSrc;
import com.sun.org.apache.xpath.internal.operations.Bool;

/**
 * GridTreeItem 是可折叠表格中的折叠项
 * <p>在GridRow中添加了下级可折叠的行的时候，GridTreeItem作为一个视觉标志出现在当前行({@link Tr})上。
 * 它前方有一个+号(或-号)点击有展开或折叠效果。</p>
 * <p>多级别的GridTreeItem自动产生缩进效果</p>
 * @see Tr
 * @author DFish Team
 *
 */
public class GridLeaf extends AbstractWidget<GridLeaf> implements HasSrc<GridLeaf>,HasFormat<GridLeaf> {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7465823398383091843L;
	private String text;
	private Boolean escape;
	private String src;
	private Boolean sync;
	private String success;
	private String error;
	private String complete;
	private String filter;
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

	@Override
    public String getType() {
		return "grid/leaf";
	}
	/**
	 * 标题
	 * @return String
	 */
	@Override
    public String getText() {
		return text;
	}
	/**
	 * 标题
	 * @param text String
	 * @return 本身，这样可以继续设置其他属性
	 */
	@Override
    public GridLeaf setText(String text) {
		this.text = text;
		return this;
	}
	/**
	 * 如果展开的内容是延迟加载的。将在这个URL所指定的资源中获取内容
	 * @return String
	 */
	@Override
    public String getSrc() {
		return src;
	}
	/**
	 * 如果展开的内容是延迟加载的。将在这个URL所指定的资源中获取内容
	 * @param src String
	 * @return 本身，这样可以继续设置其他属性
	 */
	@Override
    public GridLeaf setSrc(String src) {
		this.src = src;
		return this;
	}

	@Override
    public String getFormat() {
		return format;
	}

	@Override
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
	@Override
    public String getSuccess() {
		return success;
	}

	@Override
    public GridLeaf setSuccess(String success) {
		this.success = success;
		return this;
	}
	@Override
    public String getError() {
		return error;
	}

	@Override
    public GridLeaf setError(String error) {
		this.error = error;
		return this;
	}
	@Override
    public String getComplete() {
		return complete;
	}

	@Override
    public GridLeaf setComplete(String complete) {
		this.complete = complete;
		return this;
	}
	@Override
    public String getFilter() {
		return filter;
	}

	@Override
    public GridLeaf setFilter(String filter) {
		this.filter = filter;
		return this;
	}
	@Override
    public GridLeaf setEscape(Boolean escape){
		this.escape=escape;
		return this;
	}
	@Override
    public Boolean getEscape(){
		return escape;
	}
	@Override
    public GridLeaf setSync(Boolean sync){
		this.sync=sync;
		return this;
	}
	@Override
    public Boolean getSync(){
		return sync;
	}
}
