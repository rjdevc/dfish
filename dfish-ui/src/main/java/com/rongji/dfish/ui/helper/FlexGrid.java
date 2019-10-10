package com.rongji.dfish.ui.helper;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import com.rongji.dfish.ui.HiddenContainer;
import com.rongji.dfish.ui.HiddenPart;
import com.rongji.dfish.ui.Layout;
import com.rongji.dfish.ui.PrototypeChangeable;
import com.rongji.dfish.ui.Scrollable;
import com.rongji.dfish.ui.Widget;
import com.rongji.dfish.ui.form.*;
import com.rongji.dfish.ui.json.JsonWrapper;
import com.rongji.dfish.ui.layout.AbstractLayout;
import com.rongji.dfish.ui.layout.GridLayout;
import com.rongji.dfish.ui.layout.grid.GridColumn;
import com.rongji.dfish.ui.widget.Html;

/**
 * FlexGrid 栅格布局 默认12列
 * 它是一个layout 同时也是一个Wrapper 原型是GridLayout
 * 和gridLayout不一样，它不能设置每列的宽度，固定是平均分配，如果不指定，那么它默认就是12列
 * 元素添加到12列栅格布局中的时候，只能设定宽度占多少列。不指定的话，则默认为1列。
 * 元素不能指定高度占多少行。
 * @author DFish team
 *
 */
public class FlexGrid extends AbstractLayout<FlexGrid, Widget<?>> 
		implements JsonWrapper<GridLayout>, HiddenContainer<FlexGrid>,
		Scrollable<FlexGrid>,PrototypeChangeable<GridLayout>,LabelRowContainer<FlexGrid>{

	private static final long serialVersionUID = 6148451405533076L;
	/**
	 * 如果设置了FULL_LINE即使columns变化了也是占满整行
	 */
	public static final int FULL_LINE = -1;

	private int columns = 12;
	private Integer rowHeight;
	/**
	 * 默认添加的组件所占的列数(当组件没有设定列数时,将使用该值)
	 */
	private int defaultOccupy = 1;
	/**
	 * 是否需要重构GridLayout中的表单元素
	 */
	protected GridLayout prototype;
	protected boolean prototypeChanged=false;

	private List<FlexGridAppendingMode> nodes=new ArrayList<FlexGridAppendingMode>();
	protected String labelWidth;

	public void notifyChage(){
		prototypeChanged=true;
	}
	protected void checkConcurrentModify(){
		if(prototypeChanged){
			throw new java.util.ConcurrentModificationException("can NOT change wrapper when prototype is changed");
		}
	}
	/**
	 * 构造函数
	 * @param id String
	 */
	public FlexGrid(String id) {
		super(id);
		prototype=new GridLayout(id);
		prototype.setWrapper(this);
		bundleProperties();
	}
	/**
	 * 这个栅格布局 列数
	 * @return int
	 */
	public int getColumns() {
		return columns;
	}
	/**
	 * 这个栅格布局 列数
	 * @param columns 列数
	 * @return  this
	 */
	public FlexGrid setColumns(int columns) {
		this.checkConcurrentModify();
		this.columns = columns;
		return this;
	}
	/**
	 * 获取默认添加的组件所占的列数
	 * @return int
	 */
	public int getDefaultOccupy() {
		return defaultOccupy;
	}
	/**
	 * 设置默认添加的组件所占的列数
	 * @param defaultOccupy int
	 * @return 本身,这样以便更好地设置参数
	 */
	public FlexGrid setDefaultOccupy(int defaultOccupy) {
		this.defaultOccupy = defaultOccupy;
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
	 * @return 本身,这样以便更好地设置参数
	 */
	public FlexGrid setRowHeight(Integer rowHeight) {
		this.rowHeight = rowHeight;
		return this;
	}
	
	@Override
	public FlexGrid add(Widget<?> w) {
		if(w instanceof Hidden){
			return add((Hidden)w);
		}
		return add(w, defaultOccupy);
	}

	
	/**
	 * 添加子节点
	 * @param w Widget
	 * @param occupy 占用列数
	 * @return this
	 */
	public FlexGrid add(Widget<?> w, Integer occupy) {
		if (w != null && occupy != null) { // 占有列数必须大于0
							
			// 空闲列数不足
			// 加入节点
			addNode(FlexGridAppendingMode.widget(w, occupy));
			// 计算得到空闲的列数
		}
		return this;
	}
	
	private FlexGrid addNode(FlexGridAppendingMode mode) {
		this.checkConcurrentModify();
		nodes.add(mode);
		return this;
	}
	
	/**
	 * 一般是增加标题。默认会右对齐
	 * dfish中标题默认的text格式 一般是
	 * 如果没有非空判定则直接是文本(如果有特殊字符可能需要HTML转义)
	 * 如果有非空判断则增加 &lt;em class=f-required&gt;*&amp;&lt;/em&gt; 作为前缀
	 * 再由CSS来确定这个*显示的样式。
	 * 
	 * 如果用于添加文字说明。请使用add(Html,Integer)
	 * @param text 文本
	 * @param occupy 占用列数
	 * @param required 是否显示必填标记
	 * @return this
	 * @deprecated 3.2开始label和内容是一体的，该方法没有用处了
	 */
	@Deprecated
	public FlexGrid add(String text, Integer occupy, boolean required) {
//		StringBuilder realText = new StringBuilder();
//		if(required){
//			realText.append(FormPanel.labelRequire);
//		}
//		Boolean escape = null;
//		if (Boolean.TRUE.equals(getEscape())) {
//			realText.append(text);
//		} else {
//			realText.append(Utils.escapeXMLword(text));
//			escape = false;
////		}
//		Html html = (Html) new FormLabel(text, required, getEscape()).getLabelWidget(true);
//		html.setAlign(Html.ALIGN_RIGHT);
//
//		return add(html, occupy);
		return this;
	}
	/**
	 *   一般是增加标题。默认会右对齐
	 *   如果用于添加文字说明。请使用add(Html,Integer)
	 * @param text 文本
	 * @param occupy 占用列数
	 * @return this
	 */
	public FlexGrid add(String text, Integer occupy) {
		return add(new Html(text), occupy);
	}


	@Override
	public String getType() {
		return "flex_grid";
	}
	
	@Override
	public GridLayout getPrototype() {
		if(!this.prototypeChanged){
			prototype.prototypeBuilding(true);
//			prototype.clearNodes();
			prototype.getTbody().clearNodes();
			prototype.getThead().clearNodes();
			//不再删除columns 把columns当成样式的一部分。
			int visableColumnCount=prototype.getVisableColumnNumMap().size();
			for(int i=visableColumnCount;i<columns;i++){
				prototype.addColumn(GridColumn.text(null, WIDTH_REMAIN));
			}
			if(columns<visableColumnCount){
				//删除最后可见的几个column
				int reduce=visableColumnCount-columns;
				for(ListIterator<GridColumn> iter=prototype.getColumns().listIterator(prototype.getColumns().size());iter.hasPrevious();){
					GridColumn c=iter.previous();
					if(c.isVisable()){
						if(--reduce<0){
							break;
						}
						iter.remove();
					}
				}
			}
		
			// 剩余列数
			int occupied = 0;
			int rowIndex = 0;
			
			for(FlexGridAppendingMode m : nodes) {
				Widget<?> w=m.getPrototype();
				int occupy = m.getOccupy()==null?defaultOccupy:m.getOccupy();
				if(occupy <= 0 || occupy > columns){
					occupy = columns;
				}
				if(occupied >= columns || (columns - occupied) < occupy) { // 已占用列数超出最大列或者空余列太少
					// 行数加1,同时空余列恢复到最大列数
					rowIndex++;
					occupied = 0;
				}
				int toColumn = occupied + occupy - 1;
				prototype.add(rowIndex, occupied, rowIndex, toColumn, w);
				if(m.getMode()==FlexGridAppendingMode.MODE_LABEL_ROW&&w instanceof LabelRow){
					LabelRow cast=(LabelRow)w;
					if(cast.getLabel()!=null&&"0".equals(cast.getLabel().getWidth())){
//					if("0".equals(((AbstractFormElement<?,?>)w).getLabel().getWidth())){
						((LabelRow)w).getLabel().setWidth(labelWidth);
					}
				}
				occupied += occupy;
			}
			prototype.prototypeBuilding(false);
		}
//		Utils.copyPropertiesExact(prototype, this);
//		if(getData()!=null){
//			for(Map.Entry<String, Object> entry:getData().entrySet()){
//				prototype.setData(entry.getKey(), entry.getValue());
//			}
//		}
//		if(getOn()!=null){
//			for(Map.Entry<String, String> entry:getOn().entrySet()){
//				prototype.setOn(entry.getKey(), entry.getValue());
//			}
//		}
		if (rowHeight != null) {
			prototype.getPub().setHeight(rowHeight);
		}
		copyProperties(prototype, this);
		
		if(getHiddens()!=null){
			for(Hidden h:getHiddens()){
				prototype.addHidden(h.getName(),h.getValue());
			}
		}
		
		return prototype;
	}

	@Override
    protected void copyProperties(AbstractLayout<?, Widget<?>> to, AbstractLayout<?, Widget<?>> from) {
	    super.copyProperties(to, from);
//	    prototype.setEscape(escape);
//	    prototype.setScroll(scroll);
//	    prototype.setScrollClass(scrollClass);
    }
	


	private HiddenPart hiddens = new HiddenPart();
//	private Boolean scroll;
//	private String scrollClass;
//	private Boolean escape;
	
	public FlexGrid addHidden(String name,String value) {
		hiddens.addHidden(name, value);
		return this;
	}
//	public FlexGrid addHidden(String name,AtExpression value) {
//		hiddens.addHidden(name, value);
//		return this;
//	}

	public FlexGrid add(Hidden hidden) {
		hiddens.add(hidden);
		return this;
	}

	public List<Hidden> getHiddens() {
		// FIXME 最好在初始化的时候将原型初始化好,然后直接设置在原型上
		return hiddens.getHiddens();
	}
	
	public List<String> getHiddenValue(String name) {
		return hiddens.getHiddenValue(name);
	}
	
	public FlexGrid removeHidden(String name) {
		hiddens.removeHidden(name);
		return this;
	}
	@Override
	public FlexGrid setScroll(Boolean scroll) {
//		this.scroll = scroll;
		prototype.setScroll(scroll);
		return this;
	}
	@Override
	public Boolean getScroll() {
		return prototype.getScroll();
	}
	@Override
	public FlexGrid setScrollClass(String scrollClass) {
//		this.scrollClass = scrollClass;
		prototype.setScrollClass(scrollClass);
		return this;
	}
	@Override
	public String getScrollClass() {
		return prototype.getScrollClass();
	}
	
	/**
	 * 添加带有标题的行组件,默认占4列
	 * 1.添加的组件是否带标题是根据参数hideLabel来决定是否要显示标题,默认显示标题
	 * 2.占有列数(包含标题和表单组件):
	 * (1)标题列数将采用默认占有列数{@link FlexGrid#defaultOccupy};
	 * (2)若所占列数&lt;=默认占有列数将不会显示标题,自动将标题隐藏
	 * (3)若设置隐藏标题,则表单组件将占满设定的列数
	 * @param labelRow 带有标题的行组件
	 * @return 本身,这样以便更好地设置参数
	 */
	public FlexGrid addLabelRow(LabelRow<?> labelRow) {
		return addLabelRow(labelRow, 4);
	}
	/**
	 * 添加带有标题的行组件
	 * 1.添加的组件是否带标题是根据参数hideLabel来决定是否要显示标题,默认显示标题
	 * 2.占有列数(包含标题和表单组件):
	 * (1)标题列数将采用默认占有列数{@link FlexGrid#defaultOccupy};
	 * (2)若所占列数&lt;=默认占有列数将不会显示标题,自动将标题隐藏
	 * (3)若设置隐藏标题,则表单组件将占满设定的列数
	 * @param labelRow 带有标题的行组件
	 * @param occupy 占有列数,包含完整的标题和表单组件
	 * @return 本身,这样以便更好地设置参数
	 */
	public FlexGrid addLabelRow(LabelRow<?> labelRow, Integer occupy) {
		if (labelRow == null || occupy == null) {
			return this;
		}
		addNode(FlexGridAppendingMode.labelRow(labelRow, occupy));
//		// 理论上这里应该是判断-1,但是占用0列似乎也没作用
//		if (occupy <= 0 || occupy > columns) {
//			occupy = columns;
//		}
//		
//		// 占有列数大于默认列数时才显示前面的标题
//		if ((labelRow.getHideLabel() == null || !labelRow.getHideLabel()) && occupy > defaultOccupy) { 
//			// 剩余占有列数
//			occupy -= defaultOccupy;
//			if (labelRow instanceof AbstractFormElement) {
//				// 表单元素,必填效果
//				AbstractFormElement<?, ?> cast = (AbstractFormElement<?, ?>) labelRow;
//				boolean required = cast.getValidate() != null && cast.getValidate().getRequired() != null && cast.getValidate().getRequired();
//				add(labelRow.getLabel(), defaultOccupy, required);
//				//FIXME 最好需要对该label设置ID。如果组件被替换可以用JS的方式替换该内容
//			} else {
//				add(labelRow.getLabel(), defaultOccupy);
//			}
//		}
//		return add(labelRow, occupy);
		return this;
	}
	
	public Boolean getEscape() {
		return prototype.getEscape();
	}
	
	public FlexGrid setEscape(Boolean escape) {
//		this.escape = escape;
		prototype.setEscape(escape);
		return this;
	}
	@SuppressWarnings("unchecked")
	@Override
	public List<Widget<?>> findNodes() {
		List<Widget<?>> result=new ArrayList<Widget<?>>();
		for(FlexGridAppendingMode m: this.nodes){
			result.add(m.getPrototype());
		}
		return result;
	}
	@Override
	public FlexGrid removeNodeById(String id) {
		if (id == null||nodes==null) {
            return  this;
        }
        for (Iterator<FlexGridAppendingMode> iter = nodes.iterator();
                                    iter.hasNext(); ) {
        	Widget<?> item = iter.next().getPrototype();
            if (id.equals(item.getId())) {
                iter.remove();
            } else if (item instanceof Layout) {
            	@SuppressWarnings("unchecked")
				Layout<?,Widget<?>> cast = (Layout<?,Widget<?>>) item;
                cast.removeNodeById(id);
            }
        }
        return this;
	}
	@Override
	public boolean replaceNodeById(Widget<?> w) {
		 if (w == null || w.getId() == null||nodes==null) {
			return false;
		}
		String id = w.getId();
		for (int i = 0; i < nodes.size(); i++) {
			Widget<?> item =nodes.get(i).getPrototype();
			if (id.equals(item.getId())) {
				// 替换该元素
				if(onReplace(item,w)){
					nodes.get(i).setPrototype(w);
					return true;
				}else{
					return false;
				}
			} else if (item instanceof Layout) {
                @SuppressWarnings("unchecked")
			    Layout<?,Widget<?>> cast = (Layout<?,Widget<?>>) item;
				boolean replaced = cast.replaceNodeById(w);
				if (replaced) {
					return true;
				}
			}
		}
		return false;
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
	public FlexGrid setLabelWidth(String labelWidth) {
		this.labelWidth = labelWidth;
		return this;
	}
}
