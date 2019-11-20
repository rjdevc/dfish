package com.rongji.dfish.ui.helper;

import java.util.List;

import com.rongji.dfish.ui.AbstractWidgetWrapper;
import com.rongji.dfish.ui.HiddenContainer;
import com.rongji.dfish.ui.PrototypeChangeable;
import com.rongji.dfish.ui.Scrollable;
import com.rongji.dfish.ui.Widget;
import com.rongji.dfish.ui.form.Hidden;
import com.rongji.dfish.ui.form.LabelRow;
import com.rongji.dfish.ui.form.LabelRowContainer;
import com.rongji.dfish.ui.layout.GridLayout;
import com.rongji.dfish.ui.layout.grid.GridColumn;
import com.rongji.dfish.ui.layout.grid.Td;
import com.rongji.dfish.ui.layout.grid.Tr;

/**
 * 多列表单
 * @author DFish Team
 *
 */
public class GridLayoutFormPanel extends AbstractWidgetWrapper<GridLayoutFormPanel, GridLayout> implements 
	Scrollable<GridLayoutFormPanel>,HiddenContainer<GridLayoutFormPanel>,PrototypeChangeable<GridLayout>,
	LabelRowContainer<GridLayoutFormPanel>{
	private static final long serialVersionUID = 3706745931483031949L;

	protected static final String COLUMN_CLS_LABEL="form-tt";

	protected String labelWidth;
//	protected Integer rowHeight;
	/**
	 * @param id
	 */
	public GridLayoutFormPanel(String id) {
		prototype = new GridLayout(id);
		prototype.setWrapper(this);
		prototype.prototypeBuilding(true);
		labelWidth="120";
		bundleProperties();
	}
	
	/**
	 * 标签宽度
	 * @return String
	 */
	public String getLabelWidth() {
		return labelWidth;
	}

	/**
	 * 设置标签宽度
	 * @param labelWidth String
	 * @return 本身，这样可以继续设置其他属性
	 */
	public GridLayoutFormPanel setLabelWidth(String labelWidth) {
		this.labelWidth = labelWidth;
		return this;
	}

	/**
	 * 行高
	 * @return Integer
	 * @deprecated 用getPrototype().getPub().getHeight() 代替 
	 */
	@Deprecated
	public Integer getRowHeight() {
		String ph=prototype.getPub().getHeight();
		if(ph==null){
			return null;
		}try{
			return Integer.parseInt(ph);
		}catch(Exception ex){
			throw new RuntimeException("call getPrototype().getPub().getHeight() instead",ex);
		}
	}

	/**
	 * 设置行高
	 * @param rowHeight 行高
	 * @return 本身，这样可以继续设置其他属性
	 */
	public GridLayoutFormPanel setRowHeight(Integer rowHeight) {
		prototype.getPub().setHeight(rowHeight);
		return this;
	}

	/**
	 * 添加一个元素
	 * @param fromRow 行
	 * @param fromColumn 列
	 * @param value 
	 * @return 本身，这样可以继续设置其他属性
	 */
	public GridLayoutFormPanel add(int fromRow, int fromColumn, Widget<?> value) {
		add(fromRow, fromColumn, fromRow, fromColumn, value);
		return this;
	}
	/**
	 * 添加一个元素
	 * @param fromRow
	 * @param fromColumn
	 * @param value
	 * @return 本身，这样可以继续设置其他属性
	 */
	public GridLayoutFormPanel addLabelRow(int fromRow, int fromColumn, LabelRow<?> value) {
		add(fromRow, fromColumn, fromRow, fromColumn, value);
		return this;
	}
	
	@Override
    public GridLayout getPrototype() {
	    return prototype;
    }

	
	/**
	 * 添加一个元素
	 * @param fromRow
	 * @param fromColumn
	 * @param toRow
	 * @param toColumn
	 * @param value
	 * @return 本身，这样可以继续设置其他属性
	 */
	public GridLayoutFormPanel addLabelRow(Integer fromRow, Integer fromColumn, Integer toRow, Integer toColumn,  LabelRow<?> value) {
		add(fromRow, fromColumn, toRow, toColumn, value);
		return this;
	}
	/**
	 * 添加一个元素
	 * @param fromRow
	 * @param fromColumn
	 * @param toRow
	 * @param toColumn
	 * @param value
	 * @return 本身，这样可以继续设置其他属性
	 */
	@SuppressWarnings("deprecation")
	public GridLayoutFormPanel add(Integer fromRow, Integer fromColumn, Integer toRow, Integer toColumn, Widget<?> value) {
		this.checkConcurrentModify();
		if(fromRow==null||fromColumn==null){
			return this;
		}
		if(toRow==null){
			toRow=fromRow;
		}
		if(toColumn==null){
			toColumn=fromColumn;
		}
		if(toRow<fromRow){
			Integer temp=fromRow;
			fromRow=toRow;
			toRow=temp;
		}
		if(toColumn<fromColumn){
			Integer temp=fromColumn;
			fromColumn=toColumn;
			toColumn=temp;
		}
		if (value == null) {
			return this;
		}
		int widgetFromRow = fromRow;
		int widgetFromColumn = 2 * fromColumn ;
		int widgetToRow = toRow;
		int widgetToColumn = 2 * toColumn +1;
		
		List<GridColumn> columns= prototype.getColumns();
		// 若调用顺序不规则,需要先进行占位
//		while(columns.size()<=widgetFromColumn) {
//			columns.add(GridColumn.text(GridColumn.COLUMN_FIELD_UNKNOWN, labelWidth).setAlign(GridColumn.ALIGN_RIGHT).setCls(COLUMN_CLS_LABEL));
//			columns.add(GridColumn.text(GridColumn.COLUMN_FIELD_UNKNOWN, FormPanel.COLUMN_WIDTH_VALUE));
//		}
		for(int i=fromColumn;i<=toColumn;i++){
			int columnIndex = 2 * i;
			while(columns.size()<=columnIndex){
				columns.add(GridColumn.text(FormPanel.COLUMN_FIELD_LABEL+i, labelWidth).setAlign(GridColumn.ALIGN_RIGHT).setCls(COLUMN_CLS_LABEL));
				columns.add(GridColumn.text(FormPanel.COLUMN_FIELD_VALUE+i, FormPanel.COLUMN_WIDTH_VALUE));
//			} else {
//				GridColumn labelColumn = columns.get(columnIndex);
//				if (labelColumn != null && GridColumn.COLUMN_FIELD_UNKNOWN.equals(labelColumn.getField())) {
//					labelColumn.setField(FormPanel.COLUMN_FIELD_LABEL+i);
//				}
//				GridColumn valueColumn = columns.get(columnIndex+1);
//				if (valueColumn != null && GridColumn.COLUMN_FIELD_UNKNOWN.equals(valueColumn.getField())) {
//					valueColumn.setField(FormPanel.COLUMN_FIELD_VALUE+i);
//				}
			}
		}
		
		Td cell = new Td();
		cell.setNode(value);
		if (value instanceof LabelRow && (((LabelRow<?>) value).getHideLabel()==null||!((LabelRow<?>) value).getHideLabel())) {
			LabelRow<?> cast = (LabelRow<?>) value;
//			StringBuilder label = new StringBuilder();
//			if (Utils.notEmpty(cast.getId())) {
//				// Label的ID为lbl_xxx,xxx代表控件的ID
//				label.append("<label id='lbl_").append(cast.getId()).append("'>");
//			}
//			Boolean labelEscape = null;
//			if(cast.getStar()!=null&&cast.getStar()){
//				label.append("<span class='f-required'>*&nbsp;</span>");
//				//控件本身只要有Validate为required=true会自动触发 z-required 样式
//				labelEscape = false;
//			}
//			label.append(cast.getLabel()==null?"":Utils.escapeXMLword(cast.getLabel()));
//			if (Utils.notEmpty(cast.getId())) {
//				label.append("</label>");
//			}
//			String label=null;
//			if(cast.getStar()!=null&&cast.getStar()){
//				label="<span class='f-required'>*&nbsp;</span>"+cast.getLabel();
//				//控件本身只要有Validate为required=true会自动触发 z-required 样式
//			}else{
//				label=cast.getLabel();
//			}
			
//			Html labelWidget = new Html("lbl_" + cast.getId(), label.toString()).setEscape(calcLabelRealEscape(labelEscape, prototype.getEscape()));
			prototype.add(widgetFromRow, widgetFromColumn, widgetToRow, widgetFromColumn, new FormLabel(cast, prototype.getEscape()).getLabelWidget());
			prototype.add(widgetFromRow, widgetFromColumn+1, widgetToRow, widgetToColumn, cell);
		}else{
			cell.setColspan(2);
			cell.setAlign(GridColumn.ALIGN_LEFT);
			prototype.add(widgetFromRow, widgetFromColumn, widgetToRow, widgetToColumn, cell);
		}
		
		return this;
	}
	
    /**
     * @param name
     * @param value
     * @return 本身，这样可以继续设置其他属性
     */
    @Override
    public GridLayoutFormPanel addHidden(String name, String value) {
    	prototype.addHidden(name,value);
	    return this;
    }
	

	@Override
    public GridLayoutFormPanel setScroll(Boolean scroll) {
		prototype.setScroll(scroll);
		return this;
	}
	
	@Override
    public Boolean getScroll() {
		return prototype.getScroll();
	}

    @Override
    public GridLayoutFormPanel setScrollClass(String scrollClass) {
    	prototype.setScrollClass(scrollClass);
	    return this;
    }

    @Override
    public String getScrollClass() {
	    return prototype.getScrollClass();
    }
    
    /**
	 * 无格式
	 */
	public static final String FACE_NONE = GridLayout.FACE_NONE;

	/**
	 * 用横线把表格划分成多行
	 */
	public static final String FACE_LINE = GridLayout.FACE_LINE;
	/**
	 * 用横竖的线把表格划分成多个单元格
	 */
	public static final String FACE_CELL = GridLayout.FACE_CELL;
	/**
	 * 用横线(虚线)把表格划分成多行
	 */
	public static final String FACE_DOT = GridLayout.FACE_DOT;
	
	/**
	 * 表格行的样式。可选值: line(默认值，横线), dot(虚线), cell(横线和竖线), none(无样式)。
	 * @param face String
	 * @return 本身，这样可以继续设置其他属性
	 */
    public GridLayoutFormPanel setFace(String face) {
    	prototype.setFace(face);
    	return this;
    }
    
    /**
     * 表格行的样式。可选值: line(默认值，横线), dot(虚线), cell(横线和竖线), none(无样式)。
     * @return String
     */
    public String getFace() {
    	return prototype.getFace();
    }


	@Override
	public List<Hidden> getHiddens() {
		return prototype.getHiddens();
	}


	@Override
	public List<String> getHiddenValue(String name) {
		return prototype.getHiddenValue(name);
	}


	@Override
	public GridLayoutFormPanel removeHidden(String name) {
		prototype.removeHidden(name);
		return this;
	}

	@Override
	public List<Tr> findNodes() {
		 return prototype.findNodes();
	}
}