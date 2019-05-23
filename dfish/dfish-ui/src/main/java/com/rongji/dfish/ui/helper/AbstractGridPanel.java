package com.rongji.dfish.ui.helper;

import java.util.ArrayList;
import java.util.List;

import com.rongji.dfish.base.Utils;
import com.rongji.dfish.base.util.BeanUtil;
import com.rongji.dfish.base.util.StringUtil;
import com.rongji.dfish.ui.AbstractWidgetWrapper;
import com.rongji.dfish.ui.HiddenContainer;
import com.rongji.dfish.ui.PrototypeChangeable;
import com.rongji.dfish.ui.PubHolder;
import com.rongji.dfish.ui.Scrollable;
import com.rongji.dfish.ui.form.Hidden;
import com.rongji.dfish.ui.layout.GridLayout;
import com.rongji.dfish.ui.layout.ListView;
import com.rongji.dfish.ui.layout.grid.GridPart;
import com.rongji.dfish.ui.layout.grid.GridColumn;
import com.rongji.dfish.ui.layout.grid.Tr;

/**
 * 
 * @author DFish Team
 *
 * @param <T> 当前对象类型
 */
@SuppressWarnings("unchecked")
public abstract class AbstractGridPanel<T extends AbstractGridPanel<T>> extends AbstractWidgetWrapper<T, GridLayout> implements Scrollable<T>,HiddenContainer<T>,PubHolder<T,Tr>, ListView<T>,PrototypeChangeable<GridLayout>{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7218756238863890230L;
	/**
	 * 用横竖的线把表格划分成多个单元格
	 */
	public static final String FACE_CELL = GridLayout.FACE_CELL;
	/**
	 *  用横线(虚线)把表格划分成多行
	 */
	public static final String FACE_DOT = GridLayout.FACE_DOT;
	/**
	 *  用横线把表格划分成多行
	 */
	public static final String FACE_LINE = GridLayout.FACE_LINE;
	/**
	 * 无格式
	 */
	public static final String FACE_NONE = GridLayout.FACE_NONE;

	/**
	 * 表格模型构造方法,表格模型编号
	 * @param id 表格编号
	 */
	public AbstractGridPanel(String id) {
		prototype = new GridLayout(id);
		prototype.setWrapper(this);
		bundleProperties();
	}
	
//	/**
//	 * 获取表格标题栏
//	 * @return Tr headRow
//	 */
//	public Tr getHeadRow() {
//		List<Tr> headRows = prototype.getThead().getRows();
//		Tr headRow = null;
//		if (Utils.isEmpty(headRows)) {
//			headRow = new Tr();
//			prototype.getThead().add(headRow);
//		} else {
//			headRow = headRows.get(0);
//		}
//		if(headRow==null){
//			headRow=new Tr();
//		}
//		return headRow;
//	}
	
	// 显示域属性列表(仅在按数组下标添加域属性时添加数据)
//	protected List<String> fieldProps = new ArrayList<String>();
//	// 对象属性对应显示域属性的Map;Key-Value --> beanProp-field;
//	protected Map<Object, String> propMap = new HashMap<Object, String>();
	// 显示域属性对应的数据格式化
//	protected Map<String, String> formatters = new HashMap<String, String>();
	// 原型是否需要重构,默认是true

	protected static final int MODE_ARRAY = 2;
	protected static final int MODE_BEAN = 1;
	protected static final int MODE_UNDEFINED = 0;
	protected int dataMode=MODE_UNDEFINED;
	protected List<GridColumn> columns=new ArrayList<GridColumn>();
	
	protected Tr pub;
//	protected Tr headRow;
	/**
	 * 取得所有列定义
	 * @return List
	 */
	public List<GridColumn> getColumns() {
		return columns;
	}

	protected boolean hasTableHead=true;

	/**
	 * 添加表格列
	 * @param gridColumn GridColumn
	 * @return 本身，这样可以继续设置其他属性
	 */
    public T addColumn(GridColumn gridColumn) {
		if (gridColumn == null) {
			return (T) this;
		}
		checkMod(gridColumn);
		this.checkConcurrentModify();
		columns.add(gridColumn);
		return (T) this;
	}
	/**
	 * 添加表格列
	 * @param index 在指定位置增加设置
	 * @param gridColumn GridColumn
	 * @return 本身，这样可以继续设置其他属性
	 */
    public T addColumn(int index,GridColumn gridColumn) {
		if (gridColumn == null) {
			return (T) this;
		}
		checkMod(gridColumn);
		this.checkConcurrentModify();
		columns.add(index,gridColumn);
		return (T) this;
	}
	protected void checkMod(GridColumn gridColumn){
		int currMode = MODE_UNDEFINED;
		if (gridColumn.getDataColumnIndex() >= 0) { // 数组模式
			currMode = MODE_ARRAY;
		} else if (Utils.notEmpty(gridColumn.getBeanProp())) {//Bean模式
			currMode = MODE_BEAN;
			//部分是用format写的特效，可以没有这种判断
		}
		if (currMode!=MODE_UNDEFINED) {
			if (dataMode==MODE_UNDEFINED) {
				dataMode = currMode;
			} else if (dataMode != currMode) {
				throw new UnsupportedOperationException("data mode should be same");
			}
		}
		//FIXME 如果field重复则报异常		
	}
	
    

    protected abstract void buildPrototype();
    
    protected String getFormattedData(GridColumn gc, Object data) {
    	String pattern =gc.getDataFormat();
    	if (Utils.isEmpty(pattern)) {
    		return data.toString();
    	}
    	return StringUtil.format(data, pattern);
    }

	/**
     * 获取表格行格式
     * @return 本身，这样可以继续设置其他属性
     */
	public Tr getPub() {
//		Tr pub = prototype.getPub();
//		if (pub == null) {
//			pub = new Tr();
//			prototype.setPub(pub);
//		}
		if(pub==null){
			pub=new Tr();
		}
		return pub;
	}
	
	/**
     * 获取表格行格式
	 * @param pub Tr
     * @return 本身，这样可以继续设置其他属性
     */
	public T setPub(Tr pub) {
		prototype.setPub(pub);
		return (T)this;
	}
	
	/**
	 * 皮肤。表格行的样式。可选值: line(默认值，横线), dot(虚线), cell(横线和竖线), none(无样式)。
	 * @return 本身，这样可以继续设置其他属性
	 */
	public String getFace() {
		return prototype.getFace();
	}

	/**
	 * 皮肤。表格行的样式。可选值: line(默认值，横线), dot(虚线), cell(横线和竖线), none(无样式)。
	 * @param face String
	 * @return 本身，这样可以继续设置其他属性
	 */
    public T setFace(String face) {
		prototype.setFace(face);
		return (T) this;
	}
	
	/**
	 * 单元格边距
	 * @return 本身，这样可以继续设置其他属性
	 */
	public Integer getCellpadding() {
		return prototype.getCellpadding();
	}
	
	/**
	 * 单元格边距
	 * @param cellpadding Integer
	 * @return 本身，这样可以继续设置其他属性
	 */
    public T setCellpadding(Integer cellpadding) {
		prototype.setCellpadding(cellpadding);
		return (T) this;
	}
	
	/**
	 * 内容不换行。
	 * @return 本身，这样可以继续设置其他属性
	 */
	public Boolean getNobr() {
		return prototype.getNobr();
	}
	
	public Boolean getScroll() {
		return prototype.getScroll();
	}
	
    public T setScroll(Boolean scroll) {
		prototype.setScroll(scroll);
		return (T) this;
	}
    
    /**
	 * 是否可以拖动表头调整列宽。
	 * @return Boolean
	 */
    public Boolean getResizable() {
	    return prototype.getResizable();
    }
    /**
     *  是否可以拖动表头调整列宽。
     * @param resizable Boolean
     * @return this
     */
    public T setResizable(Boolean resizable) {
		prototype.setResizable(resizable);
	    return (T) this;
    }
	
	public String getScrollClass() {
		return prototype.getScrollClass();
	}
	
	public T setScrollClass(String scrollClass) {
		prototype.setScrollClass(scrollClass);
		return (T) this;
	}
	
	public T add(Hidden hidden) {
		prototype.add(hidden);
		return (T) this;
	}
    public T addHidden(String name, String value) {
		prototype.addHidden(name, value);
		return (T) this;
	}
		
	public List<Hidden> getHiddens() {
		return prototype.getHiddens();
	}
	
	public List<String> getHiddenValue(String name) {
		return prototype.getHiddenValue(name);
	}
	
    public T removeHidden(String name) {
		prototype.removeHidden(name);
		return (T) this;
	}
//	@Override
//	public Boolean getFocusable() {
//		return prototype.getFocusable();
//	}

    /**
	 * 点击聚焦效果,目前通过{@link #getPub()}.{@link #setFocusable(Boolean)}来设置
	 * @param focusable Boolean
	 * @return this
	 */
	@Deprecated
	public T setFocusable(Boolean focusable) {
		prototype.setFocusable(focusable);
		return (T) this;
	}

	@Override
	public Boolean getFocusmultiple() {
		return prototype.getFocusmultiple();
	}

	@Override
	public T setFocusmultiple(Boolean focusmultiple) {
		prototype.setFocusmultiple(focusmultiple);
		return (T) this;
	}

	/**
	 * 鼠标可移上去效果,目前无该方法
	 */
	@Deprecated
	@Override
	public Boolean getHoverable() {
		return prototype.getHoverable();
	}

	/**
	 * 鼠标可移上去效果,目前无该方法
	 */
	@Deprecated
	@Override
	public T setHoverable(Boolean hoverable) {
		prototype.setHoverable(hoverable);
		return (T) this;
	}

	@Override
	public T setNobr(Boolean nobr) {
		prototype.setNobr(nobr);
		return (T) this;
	}
	/**
	 * 内容转义
	 * <p>描述:</p>
	 * @return 本身，这样可以继续设置其他属性
	 */
	public Boolean getEscape() {
		return prototype.getEscape();
	}
	
	/**
	 * 内容转义
	 * <p>描述:</p>
	 * @param escape Boolean 转义
	 * @return 本身，这样可以继续设置其他属性
	 */
	public T setEscape(Boolean escape) {
		prototype.setEscape(escape);
		return (T) this;
	}
	
	/**
	 * 是否有表头配置
	 * @param hasTableHead boolean 是否有表头
	 * @return 本身，这样可以继续设置其他属性
	 */
	public T setHasTableHead(boolean hasTableHead){
		this.hasTableHead=hasTableHead;
		return (T) this;
	}
	/**
	 * 获取属性信息，支持bean中类反射获取，或者是从Map中获取(主要支持JdbcTemplate的List&lt;Map&lt;String,Object&gt;&gt;)
	 * @param item Object
	 * @param prop String 支持点号(.)分隔的多级属性
	 * @return Object
	 */
	protected static Object getProperty(Object item, String prop){
		if (item == null || prop == null||prop.equals("")) {
			return null;
		}
		try {
			return BeanUtil.getProperty(item, prop);
		} catch (Exception e) {
			LOG.debug(null, e);
			return null;
		}
	
	}
	public GridLayout getPrototype() {
		if(!this.prototypeChanged){
			prototype.prototypeBuilding(true);
			prototype.clearNodes();
			if(pub!=null){
				prototype.setPub(pub);
			}
			
			buildPrototype();
			prototype.prototypeBuilding(false);
		}
		return this.prototype;
	}
	/**
	 * 取得表头
	 * @return Tr
	 * @deprecated 该方法将会触发获取原型。请谨慎使用
	 * @see #getPrototype()
	 */
	public Tr getHeadRow(){
		GridPart gb=getPrototype().getThead();
		if(gb.getRows()!=null&&gb.getRows().size()>0){
			return gb.getRows().get(0);
		}
		return null;
	}
}
