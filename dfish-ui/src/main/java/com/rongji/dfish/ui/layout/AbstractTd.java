package com.rongji.dfish.ui.layout;

import java.util.Arrays;
import java.util.List;

import com.rongji.dfish.ui.*;

/**
 *  Td 表示一个Grid的单元格
 * <p>在一些复杂布局结构中，它可以占不止一行或不止一列</p>
 * <p>GridCell 有两种工作模式，他内部可以包含一个Widget或简单的包含一个文本，如果包含了widget文本模式将失效</p>
 * <p>虽然GridCell也是一个Widget，但其很可能并不会专门设置ID。虽然它是一个Layout，但它最多包含1个子节点。即其内容。</p>
 * @author DFish Team
 * @param <T> 本身类型
 * @see com.rongji.dfish.ui.layout.Grid.Td
 */
@SuppressWarnings("unchecked")
public abstract class AbstractTd<T extends AbstractTd<T>> extends AbstractLayout<T>
		implements SingleContainer<T>,Alignable<T>,Valignable<T>{
	/**
	 * 
	 */
	private static final long serialVersionUID = -7870476532478876521L;
	/**
	 * 默认构造函数
	 */
	public AbstractTd() {
		super(null);
	}
	protected Integer colspan;
	protected Integer rowspan;
//	private String text;
	protected String align;
	protected String valign;
	protected Widget<?> node;
	protected Boolean escape;
	protected String format;
	protected Integer labelwidth;

	@Override
    public String getType() {
		return null;
	}

	/**
	 * 这个这个单元格占几列。
	 * 为空的时候相当于1
	 * @return Integer
	 */
	public Integer getColspan() {
		return colspan;
	}
	/**
	 * 这个这个单元格占几列。
	 * 为空的时候相当于1
	 * @param colspan Integer
	 * @return 本身，这样可以继续设置其他属性
	 */
	public T setColspan(Integer colspan) {
		if(colspan!=null){
			if(colspan<1){
				throw new java.lang.IllegalArgumentException("colspan must greater than 1");
			}
			if(colspan==1){
				colspan=null;
			}
		}
		this.colspan = colspan;
		return (T)this;
	}
	/**
	 * 这个这个单元格占几行。
	 * 为空的时候相当于1
	 * @return Integer
	 */ 
	public Integer getRowspan() {
		return rowspan;
	}
	/**
	 * 这个这个单元格占几行。
	 * 为空的时候相当于1
	 * @param rowspan Integer
	 * @return 本身，这样可以继续设置其他属性
	 */
	public T setRowspan(Integer rowspan) {
		if(rowspan!=null){
			if(rowspan<1){
				throw new java.lang.IllegalArgumentException("rowspan must greater than 1");
			}
			if(rowspan==1){
				rowspan=null;
			}
		}
		this.rowspan = rowspan;
		return (T)this;
	}
//	/**
//	 * 文本模式时， 取得单元格内部文本的值
//	 * @return String
//	 */
//	public String getText() {
//		return text;
//	}
//	/**
//	 * 文本模式时， 设置单元格内部文本的值
//	 * @param text String
//	 * @return 本身，这样可以继续设置其他属性
//	 */
//	public T setText(String text) {
//		this.text = text;
//		return (T)this;
//	}
	/**
	 * 部件(Widget)模式时， 取得单元格内部部件
	 * @return Widget
	 */
	@Override
    public Widget<?> getNode() {
		return node;
	}
	/**
	 * 部件(Widget)模式时， 设置单元格内部部件
	 * @param node Widget
	 * @return 本身，这样可以继续设置其他属性
	 */
	@Override
    public T setNode(HasId node) {
		if(!(node instanceof Widget)){
			throw new IllegalArgumentException("Widget only");
		}
		this.node = (Widget<?>) node;
		return (T)this;
	}
	/**
	 * 
	 * GridCell 下只能有一个node，所以add和setNode是相同的功能
	 * @param node Widget
	 * @return 本身，这样可以继续设置其他属性
	 * @see #setNode(HasId)
	 */
	@Override
    public T add(HasId node) {
		this.node = (Widget) node;
		return (T)this;
	}

	@Override
    public Widget<?> findNodeById(String id) {
		if (id == null || node==null) {
			return null;
		}
		if (id.equals(node.getId())) {
			return node;
		} else if(node instanceof Layout) {
           Layout cast =(Layout)node;
			return (Widget) cast.findNodeById(id);
		}
		return null;
	}

	@Override
    public List<HasId<?>> findNodes() {
		return Arrays.asList(new Widget<?>[]{node});
	}


	@Override
    public T removeNodeById(String id) {
		if (id == null || node==null) {
			return (T)this;
		}
		if(id.equals(node.getId())){
			node=null;
		}
		if(node instanceof Layout) {
            Layout cast =(Layout)node;
			cast.removeNodeById(id);
		}
		return (T)this;
	}
	@Override
    public boolean replaceNodeById(Widget<?> w) {
		if (w == null || w.getId() == null || node==null) {
			return false;
		}
		if (w.getId().equals(node.getId())) {
			// 替换该元素
			node=w;
			return true;
		} else if(node instanceof Layout<?>) {
			Layout cast =(Layout)node;
			boolean replaced = cast.replaceNodeById(w);
			if (replaced) {
				return true;
			}
		}
		
		return false;
	}

	@Override
	public String getvAlign() {
		return valign;
	}
	@Override
	public T setvAlign(String vAlign) {
		this.valign= vAlign;
		return (T)this;
	}
	@Override
	public String getAlign() {
		return align;
	}
	@Override
	public T setAlign(String align) {
		this.align=align;
		return (T)this;
	}
	/**
	 * 拷贝属性
	 * @param to AbstractTd
	 * @param from AbstractTd
	 */
	protected void copyProperties(AbstractTd<?> to,AbstractTd<?> from){
		super.copyProperties(to, from);
		to.node=from.node;
		to.align=from.align;
		to.colspan=from.colspan;
		to.rowspan=from.rowspan;
		to.valign=from.valign;
		to.labelwidth=from.labelwidth;
	}

	/**
	 * 用于显示文本是否需要转义,不设置默认是true
	 * @return Boolean
	 */
	public Boolean getEscape() {
		return escape;
	}

	/**
	 * 用于显示文本是否需要转义,不设置默认是true
	 * @param escape Boolean
	 * @return 本身，这样可以继续设置其他属性
	 */
	public T setEscape(Boolean escape) {
		this.escape = escape;
		return (T) this;
	}

	/**
	 * 格式化内容。"$字段名"形式的变量将被解析替换。支持"javascript:"开头的js语句(需return返回值)。
	 * @return String 格式化内容
	 */
	public String getFormat() {
		return format;
	}

	/**
	 * 格式化内容。"$字段名"形式的变量将被解析替换。支持"javascript:"开头的js语句(需return返回值)。
	 * @param format String 格式化内容
	 * @return 本身，这样可以继续设置其他属性
	 */
	public T setFormat(String format) {
		this.format = format;
		return (T) this;
	}

	/**
	 * 表单标题宽度。
	 * @return Integer
	 * @since 3.3
	 */
	public Integer getLabelwidth() {
		return labelwidth;
	}

	/**
	 * 表单标题宽度。
	 * @param labelwidth Integer
	 * @return 本身，这样可以继续设置其他属性
	 * @since 3.3
	 */
	public T setLabelwidth(Integer labelwidth) {
		this.labelwidth = labelwidth;
		return (T) this;
	}
}
