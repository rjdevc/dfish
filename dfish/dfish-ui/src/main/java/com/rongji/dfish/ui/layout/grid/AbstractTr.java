package com.rongji.dfish.ui.layout.grid;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.rongji.dfish.ui.Layout;
import com.rongji.dfish.ui.Widget;
import com.rongji.dfish.ui.layout.AbstractLayout;
import com.rongji.dfish.ui.layout.GridLayout;

/**
 * Tr 表示 表格的行
 * <p>表格的行有三种工作模式</p>
 * <p>常见的是里面包行单元格(Td)。
 * 每个单元格是一个文本或独立的widget，有widget的功能和属性，只是有的时候可能并不会给每个单元格设置ID。</p>
 * <p>为了能让表格的json尽可能小。允许data类型为 文本 widget 或GridCell。
 * 并用{@link GridColumn#getField} 来说明这个内容显示在哪里。</p>
 * <p>当一行里面包含可折叠的子集内容的时候，它将包含rows属性。rows里面是一个有子集GridRow构成的List。
 * 而会有一个GridTreeItem字段用于做折叠操作的视觉效果</p>
 * 
 * @see AbstractTd {@link GridColumn} {@link GridLeaf}
 * @author DFish Team
 * @param <T> 当前类型
 * @since DFish 3.0
 */
@SuppressWarnings("unchecked")
public abstract class AbstractTr<T extends AbstractTr<T>> extends AbstractLayout<T,Widget<?>> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4300223953187136245L;


	/**
	 * 构造函数
	 * @param id 编号
	 */
	public AbstractTr(String id) {
		super(id);
	}
	/**
	 * 默认构造函数
	 */
	public AbstractTr() {
		super(null);
	}

	protected Boolean focus;
	protected Boolean focusable;
	protected String src;
	protected List<Tr> rows;

	

	@Deprecated
	public T add(Widget<?> w) {
		throw new UnsupportedOperationException("Use setData(String, GridCell) instead");
	}

	/**
	 * 取得可折叠的子元素
	 * @return List
	 */
	public List<Tr> getRows() {
		return rows;
	}
	/**
	 * 设置可折叠的子元素
	 * @param rows List
	 */
	public void setRows(List<Tr> rows) {
		this.rows = rows;
	}

	public String getType() {
		return null;
	}
	/**
	 * 添加一个可折叠的行。
	 * @param row GridRow
	 * @return 本身，这样可以继续设置其他属性
	 */

	public T addRow(Tr row) {
		if(rows==null){
			rows=new ArrayList<Tr>();
		}
		rows.add(row);

		return (T)this;
	}

	/**
	 * 当前行是不是聚焦状态
	 * @return Boolean
	 */
	public Boolean getFocus() {
		return focus;
	}
	/**
	 * 当前行是不是聚焦状态
	 * @param focus Boolean
	 * @return 本身，这样可以继续设置其他属性
	 */
	public T setFocus(Boolean focus) {
		this.focus = focus;
		return (T)this;
	}
	
	/**
	 * 是否可聚焦
	 * @return Boolean
	 */
	public Boolean getFocusable() {
		return focusable;
	}

	/**
	 * 设置是否可聚焦
	 * @param focusable Boolean
	 * @return this
	 */
	public T setFocusable(Boolean focusable) {
		this.focusable = focusable;
		return (T) this;
	}

	public T removeNodeById(String id) {

		if (id == null||(rows==null&&data==null)) {
			return (T)this;
		}
		if(rows!=null){
			for (Iterator<Tr> iter = rows.iterator(); iter.hasNext();) {
				Tr item = iter.next();
				if (id.equals(item.getId())) {
					iter.remove();
				} else {
					item.removeNodeById(id);
				}
			}
		}
		if (data != null) {
			for (Iterator<Map.Entry<String, Object>> iter = data.entrySet().iterator(); iter.hasNext();) {
				Map.Entry<String, Object> entry = iter.next();
				if (!(entry.getValue() instanceof Widget)) {
					// FIXME 本不该出现
					continue;
				}
				Widget<?> cast = (Widget<?>) entry.getValue();
				if (id.equals(cast.getId())) {
					iter.remove();
				} else {
					if (cast instanceof AbstractTd) {
						AbstractTd<?> cast2 = (AbstractTd<?>) entry.getValue();
						if (cast2.getNode() != null && id.equals(cast2.getNode().getId())) {
							// 删除Cell的时候如果cell是以node方式存在 node被删除cell也应该被删除
							iter.remove();
						}
					}
					if (cast instanceof Layout) {
						Layout<?, ?> cast3 = (Layout<?, ?>) entry.getValue();
						cast3.removeNodeById(id);
					}
				}
			}
		}
		return (T) this;
	}

	@Override
	public List<Widget<?>> findNodes() {
		List<Widget<?>> result=new ArrayList<Widget<?>>();
		if(rows!=null){
			for(Widget<?> o:rows){
				result.add( o);
			}
		}
		if(data!=null){
			for(Object o:data.values()){
				if(o instanceof Widget){
					//去除字符串
					result.add( (Widget<?>)o);
				}
			}
		}
		return result;
	}
	public boolean replaceNodeById(Widget<?> panel) {

		if (panel == null || panel.getId() == null) {
			return false;
		}
		String id = panel.getId();
		if(rows!=null){
			for (int i = 0; i < rows.size(); i++) {
				Tr item = rows.get(i);
				if (id.equals(item.getId())) {
					// 替换该元素
					rows.set(i, (Tr)panel);
					return true;
				} else  {
					boolean replaced = item.replaceNodeById(panel);
					if (replaced) {
						return true;
					}
				}
			}
		}
		if(data!=null){
			for (Iterator<Map.Entry<String, Object>> iter = data.entrySet()
					.iterator(); iter.hasNext();) {
				Map.Entry<String, Object> entry = iter.next();
				if(!(entry.getValue() instanceof Widget)){
					//FIXME 本不该出现
					continue;
				}
				Widget<?> cast=(Widget<?>)entry.getValue();
				if (id.equals(cast.getId())) {
					entry.setValue(panel);
				} else if(cast instanceof Layout){
					boolean replaced = ((Layout<?,?>)cast).replaceNodeById(panel);
					if (replaced) {
						return true;
					}
				}
			}
		}
		return false;
	}
	public T setData(String key, Object value) {
		if(value==null){
			return (T)this;
		}

    	if(data == null){
    		data = new LinkedHashMap<String, Object>();
    	}
    	// 如果插入的内容是String/Object  以及非GridCell的Widget需要做一层封装。
//    	if(value instanceof Td){
    	data.put(key, value);
    		
//    	}else if(value instanceof AbstractTd){
//    		data.put(key, new Td((AbstractTd<?>) value));
//    	}else if(value instanceof Widget<?>){
//    		Td cell=new Td();
//    		cell.setNode((Widget<?>) value);
//    		data.put(key, value);
//    	}else{
//    		data.put(key, value);
//    		Td cell=new Td();
//    		data.put(key, cell);
//    		String v=value.toString();
//    		cell.setNode(new Html(null,v));
//    	}
        return (T)this;
    }



	/**
	 * 排序src
	 * @return String
	 */
	public String getSrc() {
		return src;
	}
	/**
	 * 排序src
	 * @param src String
	 * @return 本身，这样可以继续设置其他属性
	 */
	public T setSrc(String src) {
		this.src = src;
		return (T)this;
	}
	/**
	 * 拷贝属性
	 * @param to AbstractTr
	 * @param from AbstractTr
	 */
	protected void copyProperties(AbstractTr<?> to,AbstractTr<?> from){
		super.copyProperties(to, from);
		to.rows=from.rows;
		to.focus=from.focus;
		to.src=from.src;
		to.focusable=from.focusable;
	}
}
