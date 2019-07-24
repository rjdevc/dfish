package com.rongji.dfish.ui.helper;

import java.util.*;

import com.rongji.dfish.base.Utils;
import com.rongji.dfish.base.util.StringUtil;
import com.rongji.dfish.ui.Combo;
import com.rongji.dfish.ui.layout.grid.GridColumn;
import com.rongji.dfish.ui.layout.grid.Tr;

/**
 * GridPanel 为 表格模型封装类。
 * <p>为了延续DFish2.x 易用的有点，保留GridPanel类。这个类不是DFish3.x系类的原型类，它仅仅提供是一个看起来是表格布局的控件。
 * 他能够简单快速的产生一个表格，极大的减少编码的数量。</p>
 * <pre style='border:1px black solid;border-left:0px;border-right:0px;background-color:#CCC'>
 * GridPanel grid=new GridPanel("f_grid");
 * Object[][] data={
 *   {"USD","美元"},
 *   {"CNY","人民币"},
 *   {"HKD","港币"},
 * };
 * grid.setData(Arrays.asList(data));
 * grid.addColumn(GridColumn.checkbox("id", "40").setAlign(Align.center));
 * grid.addColumn(GridColumn.text(0,"id","编号","120"));
 * grid.addColumn(GridColumn.text(1,"n","名称","*"));
 * </pre>
 * <p>和所有封装类相同，它输出的时候实际上并不是自己，而是会转化成原型，它的原型是GridLayout</p>
 * <p>与GridLayout不同，这个类，并不是一个Layout</p>
 * @author DFish Team
 * @since dfish 2.0
 */
public class GridPanel extends AbstractGridPanel<GridPanel>  {
	
	private static final long serialVersionUID = -4393880179206017819L;

	/**
	 * 表格模型构造方法,表格模型编号
	 * @param id 表格编号
	 */
	public GridPanel(String id) {
		super(id);
//		prototype=new NoCheckGridLayout(id);
//		prototype.setWrapper(this);
//		bundleProperties();
		setFace(FACE_LINE);
	}
	

	private Collection<?> gridData;
	// FIXME 多行聚焦
	private Set<Integer> focusRowIndexs = new HashSet<Integer>();

	/**
	 * 高亮行
	 * @return Integer
	 */
    public Integer getFocusRowIndex() {
    	for (Iterator<Integer> iter = focusRowIndexs.iterator(); iter.hasNext();) {
    		Integer rowIndex = iter.next();
    		if (rowIndex != null) {
    			return rowIndex;
    		}
    	}
		return null;
	}
    /**
     * 高亮行
     * @param focusRowIndex Integer
     * @return this
     */
	public GridPanel setFocusRowIndex(Integer focusRowIndex) {
		return setFocusRowIndexs(focusRowIndex != null ? Collections.singletonList(focusRowIndex) : null);
	}
	/**
	 * 高亮行
	 * @return Integer
	 */
    public Set<Integer> getFocusRowIndexs() {
		return focusRowIndexs;
	}
    /**
     * 高亮行
     * @param focusRowIndexs focusRowIndexs
     * @return this
     */
	public GridPanel setFocusRowIndexs(Collection<Integer> focusRowIndexs) {
		this.focusRowIndexs.clear();
		if (focusRowIndexs != null) {
			this.focusRowIndexs.addAll(focusRowIndexs);
			this.getPub().setFocusable(true);
		}
		return this;
	}
	
	/**
     * 设置表格数据
     * @param gridData Collection
     * @return 本身，这样可以继续设置其他属性
     */
    public GridPanel setGridData(Collection<?> gridData) {
    	this.gridData = gridData;
    	this.checkConcurrentModify();
    	return this;
    }
    /**
     * 表格数据
     * @return Collection
     */
    public Collection<?> getGridData(){
    	return gridData;
    }
    
    
	/**
     * 初始化原型结点
     */
	@Override
    protected void buildPrototype() {
    	Tr headRow = null;
    	if(hasTableHead){
    		headRow=new Tr();
			prototype.getThead().add(headRow);
    	}
    	for(GridColumn gridColumn:columns){
    		if(gridColumn.getWidth()!=null){//隐藏的字段不显示
//    			prototype.getColumns().add(gridColumn);
    			prototype.addColumn(gridColumn);
    		}
			if(hasTableHead){
				// 需要填充thead
				Object columnData = null;
				if (gridColumn.rawFormat() != null) {
					columnData = gridColumn.rawFormat();
				} else {
					columnData = gridColumn.getLabel();
				}
				headRow.setData(gridColumn.getField(), columnData);
			}
		}
    	
    	
    	if (Utils.isEmpty(gridData)) {
    		prototype.prototypeBuilding(false);
    		return;
    	}
    	// 假定这个集合所有对象的类型是一致的
    	int index=0;
    	for (Object data : gridData) {
			Tr dataRow = new Tr();
			boolean focus=focusRowIndexs.contains(index);
			dataRow.setFocus(focus?true:null);
			prototype.add(dataRow);
			if (data == null) {
				continue;
			}
			for(GridColumn gc:columns){
				Object value=null;
				if(data instanceof Object[] &&gc.getDataColumnIndex()>=0){
					int length=((Object[])data).length;
					if(length<=gc.getDataColumnIndex()){
						prototype.prototypeBuilding(false);
						throw new ArrayIndexOutOfBoundsException(gc.getDataColumnIndex()+"\r\n in GridColumn "+gc);
					}else{
						value=((Object[])data)[gc.getDataColumnIndex()];
					}
				}else if(gc.getBeanProp()!=null){
					value= getProperty(data, gc.getBeanProp());
				}
				String dataFormat = gc.getDataFormat();
				if (Utils.notEmpty(dataFormat)) {
					value = StringUtil.format(value, dataFormat);
				}
				dataRow.setData(gc.getField(), value);
			}
			index++;
    	}
    }

	/**
	 * 最多显示多少行。如果需要前端翻页，可设置这个属性。
	 * @param limit 限制行数
	 * @return 本身，这样可以继续设置其他属性
	 */
	public GridPanel setLimit(Integer limit) {
		prototype.setLimit(limit);
		return this;
		
	}
	/**
	 * 设置当前的 grid 为某个 combobox 或 onlinebox 的数据选项表。
	 * @param combo 数据选项表
	 * @return 本身，这样可以继续设置其他属性
	 */
	public GridPanel setCombo(Combo combo) {
		prototype.setCombo(combo);
		return this;
		
	}

	/**
	 * 内容不换行
	 * @param nobr Boolean
	 * @return 本身，这样可以继续设置其他属性
	 */
	public GridPanel setNobr(Boolean nobr) {
		prototype.setNobr(nobr);
		return this;
	}

	/**
	 * 鼠标可移上去效果,目前无该方法
	 */
	@Deprecated
	public Boolean getHoverable(){
		return prototype.getHoverable();
	}

	/**
	 * 鼠标可移上去效果,目前无该方法
	 */
	@Deprecated
	public GridPanel setHoverable(Boolean hoverable){
		prototype.setHoverable(hoverable);
		return this;
	}

	public GridPanel setFocusmultiple(Boolean focusmultiple) {
		prototype.setFocusmultiple(focusmultiple);
		return this;
	}
	


}
