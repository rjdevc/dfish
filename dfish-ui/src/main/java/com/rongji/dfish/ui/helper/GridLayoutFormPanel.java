package com.rongji.dfish.ui.helper;

import java.util.List;

import com.rongji.dfish.ui.AbstractWidgetWrapper;
import com.rongji.dfish.ui.FormElement;
import com.rongji.dfish.ui.HiddenContainer;
import com.rongji.dfish.ui.Layout;
import com.rongji.dfish.ui.PrototypeChangeable;
import com.rongji.dfish.ui.Scrollable;
import com.rongji.dfish.ui.Widget;
import com.rongji.dfish.ui.form.Hidden;
import com.rongji.dfish.ui.form.LabelRow;
import com.rongji.dfish.ui.form.LabelRowContainer;
import com.rongji.dfish.ui.layout.GridLayout;
import com.rongji.dfish.ui.layout.grid.Tr;

/**
 * 多列表单
 * @author DFish Team
 *
 */
public class GridLayoutFormPanel extends AbstractWidgetWrapper<GridLayoutFormPanel, GridLayout> implements 
	Scrollable<GridLayoutFormPanel>,HiddenContainer<GridLayoutFormPanel>,PrototypeChangeable<GridLayout>,
	LabelRowContainer<GridLayoutFormPanel>,Layout<GridLayoutFormPanel,Widget<?>>{
	private static final long serialVersionUID = 3706745931483031949L;

	protected static final String COLUMN_CLS_LABEL="form-tt";

	protected String labelWidth;
//	protected Integer rowHeight;
	/**
	 * 构造函数
	 * @param id String
	 */
	public GridLayoutFormPanel(String id) {
		prototype = new GridLayout(id);
		prototype.setWrapper(this);
		prototype.prototypeBuilding(true);
//		labelWidth="120";
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
	 * @param value Widget
	 * @return 本身，这样可以继续设置其他属性
	 */
	public GridLayoutFormPanel add(int fromRow, int fromColumn, Widget<?> value) {
		add(fromRow, fromColumn, fromRow, fromColumn, value);
		return this;
	}
	/**
	 * 添加一个元素
	 * @param row int
	 * @param column int
	 * @param value LabelRow
	 * @return 本身，这样可以继续设置其他属性
	 */
	public GridLayoutFormPanel addLabelRow(int row, int column, LabelRow<?> value) {
		add(row, column, row, column, value);
		return this;
	}
	
	@Override
    public GridLayout getPrototype() {
	    return prototype;
    }

	
	/**
	 * 添加一个元素
	 * @param fromRow Integer
	 * @param fromColumn Integer
	 * @param toRow Integer
	 * @param toColumn Integer
	 * @param value LabelRow
	 * @return 本身，这样可以继续设置其他属性
	 */
	public GridLayoutFormPanel addLabelRow(Integer fromRow, Integer fromColumn, Integer toRow, Integer toColumn,  LabelRow<?> value) {
		prototype.add(fromRow, fromColumn, toRow, toColumn, value);
		if(value.getLabel()!=null){
			value.getLabel().setWidth(labelWidth);
		}
		return this;
	}
	/**
	 * 添加一个元素
	 * @param fromRow Integer
	 * @param fromColumn Integer
	 * @param toRow Integer
	 * @param toColumn Integer
	 * @param value Widget
	 * @return 本身，这样可以继续设置其他属性
	 */
	public GridLayoutFormPanel add(Integer fromRow, Integer fromColumn, Integer toRow, Integer toColumn, Widget<?> value) {
		prototype.add(fromRow, fromColumn, toRow, toColumn, value);
//		this.checkConcurrentModify();
//		if(fromRow==null||fromColumn==null){
//			return this;
//		}
//		if(toRow==null){
//			toRow=fromRow;
//		}
//		if(toColumn==null){
//			toColumn=fromColumn;
//		}
//		if(toRow<fromRow){
//			Integer temp=fromRow;
//			fromRow=toRow;
//			toRow=temp;
//		}
//		if(toColumn<fromColumn){
//			Integer temp=fromColumn;
//			fromColumn=toColumn;
//			toColumn=temp;
//		}
//		if (value == null) {
//			return this;
//		}
//		int widgetFromRow = fromRow;
//		int widgetFromColumn = 2 * fromColumn ;
//		int widgetToRow = toRow;
//		int widgetToColumn = 2 * toColumn +1;
//		
//		List<GridColumn> columns= prototype.getColumns();
//		// 若调用顺序不规则,需要先进行占位
//			while(columns.size()<=toColumn*2){
//				int columnIndex=columns.size()/2;
////				columns.add(GridColumn.text(FormPanel.COLUMN_FIELD_LABEL+columnIndex, labelWidth).setAlign(GridColumn.ALIGN_RIGHT).setCls(COLUMN_CLS_LABEL));
//				columns.add(GridColumn.text(FormPanel.COLUMN_FIELD_VALUE+columnIndex, FormPanel.COLUMN_WIDTH_VALUE));
//			}
//		
//		Td cell = new Td();
//		cell.setNode(value);
//		if (value instanceof LabelRow && (((LabelRow<?>) value).getHideLabel()==null||!((LabelRow<?>) value).getHideLabel())) {
//			LabelRow<?> cast = (LabelRow<?>) value;
//			prototype.add(widgetFromRow, widgetFromColumn, widgetToRow, widgetFromColumn, new FormLabel(cast, prototype.getEscape()).getLabelWidget());
//			prototype.add(widgetFromRow, widgetFromColumn+1, widgetToRow, widgetToColumn, cell);
//		}else{
//			cell.setColspan(2);
//			cell.setAlign(GridColumn.ALIGN_LEFT);
//			prototype.add(widgetFromRow, widgetFromColumn, widgetToRow, widgetToColumn, cell);
//		}
		
		return this;
	}
	
	 public GridLayoutFormPanel add(Hidden hidden) {
	    	prototype.add(hidden);
		    return this;
	    }
    public GridLayoutFormPanel addHidden(String name,String value) {
    	prototype.addHidden(name,value);
	    return this;
    }
	

	public GridLayoutFormPanel setScroll(Boolean scroll) {
		prototype.setScroll(scroll);
		return this;
	}
	
	public Boolean getScroll() {
		return prototype.getScroll();
	}

    public GridLayoutFormPanel setScrollClass(String scrollClass) {
    	prototype.setScrollClass(scrollClass);
	    return this;
    }

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
	public Widget<?> findNodeById(String id) {
		return prototype.findNodeById(id);
	}
//	@Override
//	public GridLayoutFormPanel removeNodeById(String id) {
//		prototype.removeNodeById(id);//FIXME 如果LabelRow标签不会被替换需要调用addLabelRow方法
//		return this;
//	}
//	@Override
//	public boolean replaceNodeById(Widget<?> w) {
//		return prototype.replaceNodeById(w);//FIXME 如果LabelRow标签不会被替换需要调用addLabelRow方法
//	}
	
	public boolean replaceNodeById(Widget<?> value) {
		//存在标签且不隐藏
//		if (value instanceof LabelRow && (((LabelRow<?>) value).getHideLabel()==null||!((LabelRow<?>) value).getHideLabel())) {
//			if(prototype.replaceNodeById(value)){
//				//尝试替换左边的label
//				LabelRow<?> cast=(LabelRow<?>)value;
//				prototype.replaceNodeById((Widget<?>) new FormLabel(cast, prototype.getEscape()).getLabelWidget(true));
//				return true;
//			}else{
//				return false;
//			}
//		} else {
			return prototype.replaceNodeById(value);
//		}
	}
	
	public GridLayoutFormPanel removeNodeById(String id) {
		prototype.removeNodeById(id);
//		prototype.removeNodeById("lbl_"+id);
		return this;
	}
	
	@Override
	public List<FormElement<?, ?>> findFormElementsByName(String name) {
		return prototype.findFormElementsByName(name);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Tr> findNodes() {
		 return prototype.findNodes();
	}

	@Override
	public void clearNodes() {
		prototype.clearNodes();
	}
}
