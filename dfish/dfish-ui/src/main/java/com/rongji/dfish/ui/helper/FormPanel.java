package com.rongji.dfish.ui.helper;

import java.util.ArrayList;
import java.util.List;

import com.rongji.dfish.ui.AbstractWidgetWrapper;
import com.rongji.dfish.ui.HiddenContainer;
import com.rongji.dfish.ui.PrototypeChangeable;
import com.rongji.dfish.ui.Scrollable;
import com.rongji.dfish.ui.Widget;
import com.rongji.dfish.ui.form.Hidden;
import com.rongji.dfish.ui.form.LabelRow;
import com.rongji.dfish.ui.layout.GridLayout;
import com.rongji.dfish.ui.layout.grid.GridColumn;
import com.rongji.dfish.ui.layout.grid.Td;
import com.rongji.dfish.ui.layout.grid.Tr;

/**
 * FormPanel 为 简易表单面板的封装类。
 * <p>为了延续DFish2.x 易用的优点，保留FormPanel类。这个类不是DFish3.x系类的原型类。
 * 他能够简单快速的产生一个表单，极大的减少编码的数量。表单里面的元素从上到下顺序排列。</p>
 * <pre style='border:1px black solid;border-left:0px;border-right:0px;background-color:#CCC'>
 * FormPanel form=new FormPanel("f_form");
 * form.add(new Text("id","编号","123",100));
 * form.add(new Label("备注","&lt;A HERF='#'&gt;查看详情&lt;/A&gt;").setEscape(false));
 * form.add(new Text("userName","姓名","张三",-1).setHideLabel(true));
 * form.addHidden("act","save");
 * </pre>
 * <p>和所有封装类相同，它输出的时候实际上并不是自己，而是会转化成原型，它的原型是GridLayout</p>
 * <p>与GridLayout不同，这个类虽然是一个Layout，但它不能随意容纳Widget。它只能容纳有标题的表单元素({@link com.rongji.dfish.ui.form.LabelRow})。</p>
 * @author DFish Team
 * @since dfish 2.0
 */
public class FormPanel extends AbstractWidgetWrapper<FormPanel, GridLayout> implements Scrollable<FormPanel>,HiddenContainer<FormPanel>, PrototypeChangeable<GridLayout>{
	/**
	 * 
	 */
	private static final long serialVersionUID = -4359022902192699451L;
	protected static final String COLUMN_FIELD_LABEL="L";
	protected static final String COLUMN_FIELD_VALUE="V";
//	protected static final String COLUMN_WIDTH_LABEL="90";
	protected static final String COLUMN_WIDTH_VALUE="*";
//	protected static final int PADDING_SIZE=20;

	/**
	 * 构造函数
	 * @param id 编号
	 */
	public FormPanel(String id) {
		prototype = new GridLayout(id);
		prototype.setWrapper( this);
		bundleProperties();
	}
	/**
	 * 表单封装组件带标题的行元素
	 */
	private List<Widget<?>> rows = new ArrayList<Widget<?>>();

	/**
	 * 添加带标题的行元素
	 * 一般只能 LabelRow 和toggle
	 * @param row Widget
	 * @return 本身，这样可以继续设置其他属性
	 */
	public FormPanel add(Widget<?> row) {
		if (row == null) {
			return this;
		}
		rows.add(row);
		this.checkConcurrentModify();
		return this;
	}
	/**
	 * 标签宽度
	 */
	protected String labelWidth;
	/**
	 * 行高
	 */
	protected Integer rowHeight;
	/**
	 * 标签间距
	 */
	protected Integer paddingSize;
	
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
	public FormPanel setLabelWidth(String labelWidth) {
		this.labelWidth = labelWidth;
		return this;
	}

	/**
	 * 标签间距
	 * @return Integer
	 */
	public Integer getPaddingSize() {
		return paddingSize;
	}

	/**
	 * 设置标签间距
	 * @param paddingSize Integer
	 * @return 本身，这样可以继续设置其他属性
	 */
	public FormPanel setPaddingSize(Integer paddingSize) {
		this.paddingSize = paddingSize;
		return this;
	}
	
	/**
	 * 行高
	 * @return Integer
	 */
	public Integer getRowHeight() {
		return rowHeight;
	}

	/**
	 * 设置行高
	 * @param rowHeight Integer
	 * @return 本身，这样可以继续设置其他属性
	 */
	public FormPanel setRowHeight(Integer rowHeight) {
		this.rowHeight = rowHeight;
		return this;
	}

	/**
	 * 初始化GridLayout中的表单元素
	 */
	private void buildPrototype() {
	
//		prototype.getColumns().add(GridColumn.text("C0","20"));
		// FIXME 标签设置样式
		prototype.addColumn(GridColumn.text(COLUMN_FIELD_LABEL, labelWidth).setAlign(GridColumn.ALIGN_RIGHT).setStyle("padding-left:"+paddingSize+"px"));
		prototype.addColumn(GridColumn.text(COLUMN_FIELD_VALUE, COLUMN_WIDTH_VALUE).setStyle("padding-right:"+paddingSize+"px"));
//		prototype.getColumns().add(GridColumn.text("C3","20"));
//		prototype.setStyle("padding:0 20px;");
//		prototype.setStyleClass("bg-white");
//		prototype.setWmin(40);
//		List<Tr> proRows = prototype.findNodes();
		if (rowHeight != null) {
			prototype.getPub().setHeight(rowHeight);
		}
		for (Widget<?> row : rows) {
			Tr dataRow = new Tr();
			prototype.add(dataRow);
			// FIXME label 太长时的处理
//			Map<String, Object> cells = dataRow.getData();
			Td cell = new Td();
			cell.setNode(row);
			if (row instanceof LabelRow) {
				LabelRow<?> cast = (LabelRow<?>) row;
				if (cast.getHideLabel() != null && cast.getHideLabel()) {
					// 将2列合并
					cell.setColspan(2);
					cell.setAlign(GridColumn.ALIGN_LEFT);
					// formElement移到第一列
					dataRow.setData(COLUMN_FIELD_LABEL, cell);
				}else{
					dataRow.setData(COLUMN_FIELD_LABEL, new FormLabel(cast, prototype.getEscape()).getLabelWidget());
					dataRow.setData(COLUMN_FIELD_VALUE, cell);
				}
			} else {
				cell.setColspan(2);
				cell.setAlign(GridColumn.ALIGN_LEFT);
				dataRow.setData(COLUMN_FIELD_LABEL, cell);
			}
		}
	}
	
	@Override
    public GridLayout getPrototype() {
		if (!this.prototypeChanged) {
			prototype.prototypeBuilding(true);
			prototype.clearNodes();
			buildPrototype();
			prototype.prototypeBuilding(true);
		}
	    return this.prototype;
    }

	public FormPanel setScroll(Boolean scroll) {
		prototype.setScroll(scroll);
		return this;
	}
	public FormPanel setCellpadding(Integer cellpadding) {
		prototype.setCellpadding(cellpadding);
		return this;
	}
	
	public Boolean getScroll() {
		return prototype.getScroll();
	}

    public FormPanel setScrollClass(String scrollClass) {
    	prototype.setScrollClass(scrollClass);
	    return this;
    }

    public String getScrollClass() {
	    return prototype.getScrollClass();
    }

	@Override
	public FormPanel addHidden(String name,String value) {
		prototype.addHidden(name,value);
		return this;
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
	public FormPanel removeHidden(String name) {
		prototype.removeHidden(name);
		return this;
	}

}
