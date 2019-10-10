package com.rongji.dfish.ui.layout.grid;

import java.util.List;

import com.rongji.dfish.ui.Widget;
import com.rongji.dfish.ui.json.JsonWrapper;
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
 * @since DFish 3.0
 */
@SuppressWarnings("unchecked")
public class Tr extends AbstractTr<Tr> implements JsonWrapper<Object>{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1895404892414786019L;

	/**
	 * 默认构造函数
	 */
	public Tr(){
		super();
	}
	protected GridLayout owner;

	public GridLayout owner() {
		return owner;
	}

	public Tr owner(GridLayout owner) {
		this.owner = owner;
		return this;
	}
	private void nc(){
		if(owner!=null){
			owner.notifyChange();
		}
	}
	/**
	 * 构造函数
	 * @param id String
	 */
	public Tr(String id){
		super(id);
	}

	/**
	 * 拷贝构造函数，相当于clone
	 * @param tr another tr
	 */
	public Tr(AbstractTr<?> tr){
		super();
		copyProperties(this,tr);
	}
	
	
	@Override
	public Object getPrototype() {
		if(hasTrProp(this)){
			JsonTr p=new JsonTr();
			copyProperties(p,this);
			return p;
		}else{
			return this.getData();
		}
	}
	
	private static boolean hasTrProp(AbstractTr<?> tr) {
		if(tr==null){
			return false;
		}
		return tr.getId()!=null||tr.getFocus()!=null||tr.getFocusable()!=null||
				tr.getHeight()!=null||tr.getSrc()!=null||
				(tr.getRows()!=null&&tr.getRows().size()>0)||
				tr.getCls()!=null||tr.getStyle()!=null ||//常用的属性排在前面
				tr.getBeforecontent()!=null||tr.getPrependcontent()!=null||
				tr.getAppendcontent()!=null||tr.getAftercontent()!=null||
				tr.getGid()!=null||tr.getHmin()!=null||
				tr.getMaxheight()!=null||tr.getMaxwidth()!=null||
				tr.getMinheight()!=null||tr.getMinwidth()!=null||
				(tr.getOn()!=null&&tr.getOn().size()>0)||
				tr.getWidth()!=null||tr.getWmin()!=null||tr.ats()!=null;
	}

	@Override
	public void setRows(List<Tr> rows) {
		for(Tr row:rows){
			row.owner(owner);
		}
		nc();
		super.setRows(rows);
	}

	@Override
	public Tr addRow(Tr row) {
		row.owner(owner);
		nc();
		return super.addRow(row);
	}

	@Override
	public Tr setFocus(Boolean focus) {
		nc();
		return super.setFocus(focus);
	}

	@Override
	public Tr setFocusable(Boolean focusable) {
		nc();
		return super.setFocusable(focusable);
	}

	@Override
	public Tr removeNodeById(String id) {
		Tr tr= super.removeNodeById(id);
		if(tr!=null){
			nc();
		}
		return tr;
	}

	@Override
	public boolean replaceNodeById(Widget<?> panel) {
		boolean success=super.replaceNodeById(panel);
		if(success){
			nc();
		}
		return success;
	}

	@Override
	public Tr setData(String key, Object value) {
		nc();
		if(value instanceof Td){
			((Td) value).owner(owner);
		}
		return super.setData(key, value);
	}

	@Override
	public Tr setSrc(String src) {
		nc();
		return super.setSrc(src);
	}

	@Override
	public Tr setStyle(String style) {
		nc();
		return super.setStyle(style);
	}

	@Override
	public Tr setCls(String cls) {
		nc();
		return super.setCls(cls);
	}

	@Override
	public Tr setId(String id) {
		nc();
		return super.setId(id);
	}

	@Override
	public Tr setGid(String gid) {
		nc();
		return super.setGid(gid);
	}

	@Override
	public Tr setWidth(String width) {
		nc();
		return super.setWidth(width);
	}

	@Override
	public Tr setHeight(String height) {
		nc();
		return super.setHeight(height);
	}

	@Override
	public Tr setWidth(int width) {
		nc();
		return super.setWidth(width);
	}

	@Override
	public Tr setHeight(int height) {
		nc();
		return super.setHeight(height);
	}

	@Override
	public Tr setOn(String eventName, String script) {
		nc();
		return super.setOn(eventName, script);
	}

	@Override
	public Tr setWmin(Integer wmin) {
		nc();
		return super.setWmin(wmin);
	}

	@Override
	public Tr setHmin(Integer hmin) {
		nc();
		return super.setHmin(hmin);
	}

	@Override
	public Tr setMaxwidth(int maxwidth) {
		nc();
		return super.setMaxwidth(maxwidth);
	}

	@Override
	public Tr setMaxwidth(String maxwidth) {
		nc();
		return super.setMaxwidth(maxwidth);
	}

	@Override
	public Tr setMaxheight(String maxheight) {
		nc();
		return super.setMaxheight(maxheight);
	}

	@Override
	public Tr setMaxheight(int maxheight) {
		nc();
		return super.setMaxheight(maxheight);
	}

	@Override
	public Tr setMinwidth(int minwidth) {
		nc();
		return super.setMinwidth(minwidth);
	}

	@Override
	public Tr setMinwidth(String minwidth) {
		nc();
		return super.setMinwidth(minwidth);
	}

	@Override
	public Tr setMinheight(String minheight) {
		nc();
		return super.setMinheight(minheight);
	}

	@Override
	public Tr setMinheight(int minheight) {
		nc();
		return super.setMinheight(minheight);
	}
	
	


}
