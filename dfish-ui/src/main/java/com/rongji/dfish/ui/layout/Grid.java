package com.rongji.dfish.ui.layout;

import java.beans.Transient;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.rongji.dfish.base.Utils;
import com.rongji.dfish.base.util.BeanUtil;
import com.rongji.dfish.base.util.MathUtil;
import com.rongji.dfish.ui.form.Combo;
import com.rongji.dfish.ui.HiddenContainer;
import com.rongji.dfish.ui.HiddenPart;
import com.rongji.dfish.ui.HtmlContentHolder;
import com.rongji.dfish.ui.PrototypeChangeable;
import com.rongji.dfish.ui.PubHolder;
import com.rongji.dfish.ui.Scrollable;
import com.rongji.dfish.ui.Widget;
import com.rongji.dfish.ui.form.Hidden;
import com.rongji.dfish.ui.layout.grid.GridPart;
import com.rongji.dfish.ui.layout.grid.GridColumn;
import com.rongji.dfish.ui.layout.grid.Td;
import com.rongji.dfish.ui.layout.grid.Tr;

/**
 * GridLayout 为表格。
 * <p>作为DFISH3.x 最重要的一个布局面板之一，GridLayout是所有表格类布局的原型。包括分组表格，包括多层级(可折叠)表格。
 * 同时DFish2.x 的FormPanel / GridPanel 现在都是一个帮助类(封装类)。它们仅仅是为了使用起来方便，最终这些类，会生成GridLayout的实体。
 * 而DFish3.x大力推荐的FlexGrid也是该类的封装类。</p>
 * <p>与2.x相似一个表格的定义最基本的有 列定义，表头定义，表体定义。</p>
 * <div style="border-top:1px solid #333;border-bottom:1px solid #333;background-color:#FEC;line-height:120%;font-size:12px;"><pre>
 * {
	"type":"grid","id":"f","face":"line","columns":[
		{
			"format":"javascript:return {\"type\":\"triplebox\",\"name\":\"selectItem\",\"value\":$id,\"sync\":\"click\"}","width":"40","align":"center","field":"grid_triplebox"
		},{"width":"*","field":"C1"},{"width":"100","field":"C2"},{"format":"yyyy-MM-dd HH:mm","width":"100","field":"C3"}
	],
	"thead":{
		"rows":[
			{
				"grid_triplebox":{"type":"triplebox","name":"selectItem","checkall":true,"sync":"click"},"C1":"消息","C2":"发送人","C3":"时间"
			}
		]
	},"tbody":{
		"rows":[
			{"id":"000001","C1":"【通知】请各位同事明天着正装上班，迎接XX领导一行莅临参观指导。","C2":"行政部","C3":"2018-07-06 10:48:22"},
			{"id":"000002","C1":"王哥，能不能把我工位上的一张XX项目的审批材料，拍个照发给我一下，谢谢","C2":"小张","C3":"2018-07-06 10:48:22"}
		]
	}
}</pre></div>
 * <p>thead 和 tbody其实都是一个基本表格结构。他们可以分开设置样式，以实现独立的表头效果。thead可以没有。基本表格结构GridBody最基本格式为tr和td。</p>
 * <p>Tr的完整格式其实是</p>
 * <div style="border-top:1px solid #333;border-bottom:1px solid #333;background-color:#FEC;line-height:120%;font-size:12px;"><pre>
 * {
		"cls":"tr-0",
		"data":{"id":"000001","C1":"【通知】请各位同事明天着正装上班，迎接XX领导一行莅临参观指导。","C2":"行政部","C3":"2018-07-06 11:13:50"}
	}</pre></div>
 * <p>为了节省流量可以只输出data的部分。</p>
 * <p>Td的完整格式其实是</p>
 * <div style="border-top:1px solid #333;border-bottom:1px solid #333;background-color:#FEC;line-height:120%;font-size:12px;"><pre>
 * {
	"node":{
		"type":"html","text":"【通知】请各位同事明天着正装上班，迎接XX领导一行莅临参观指导。","valign":"middle"
	},
	"rowspan":2
}</pre></div>
 * <p>如果没有rowspan或cls属性的时候，td可以只写其中node的部分。这时可以是一个html元素或一个Text等输入框。甚至可以是一个布局元素。里面放任何内容。
 * 而如果这个node就是为了输出一个html并且不需要复杂 cls 等属性的时候。 td就可以缩写成范例中的那样，一个字符串。用于最简输出。同时也更容易被人阅读，理解，和调试</p>
 * <p>支持折叠和多层折叠 详见 {@link com.rongji.dfish.ui.widget.Toggle} 和 {@link com.rongji.dfish.ui.layout.grid.GridLeaf}</p>
 * <p>支持指定位置添加内容 见{@link Grid#add(int, int, Object)} 甚至可以直接指定一个区块合并单元格，并填充内容{@link Grid#add(Integer, Integer, Integer, Integer, Object)}</p>
 * <p>如基础定义所见，如果在GridLayout中直接指定位置添加内容，实际上指的是tbody部分。如果想在thead上使用该功能，要显式先取得 getThead()</p>
 * @author DFish team
 * @since DFish 3.0 
 *
 */
public class Grid extends AbstractLayout<Grid, Tr> implements ListView<Grid>,
	HiddenContainer<Grid>,HtmlContentHolder<Grid>,PubHolder<Grid, Tr>,Scrollable<Grid>,GridOper<Grid> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6537737987499258183L;
	private GridPart thead;
	private GridPart tbody;
	private List<GridColumn> columns=new ArrayList<GridColumn>();
	private Tr pub;
	
	private PrototypeChangeable<Grid> wrapper;
	private boolean prototypeBuilding=false;
	/**
	 * 因为GridLayout经常作为其他封装类的圆形。所以提供该模式设置
	 * 如果正在被当做原型来构建的时候，他们，原则上它构建的时候不要进行严格的校验会不会某格可能被占用。而直接添加。同时他也不会报封装类被改动的同步改动异常
	 * @return boolean
	 */
	public boolean prototypeBuilding(){
		return prototypeBuilding;
	}
	/**
	 * 因为GridLayout经常作为其他封装类的圆形。所以提供该模式设置
	 * 如果正在被当做原型来构建的时候，他们，原则上它构建的时候不要进行严格的校验会不会某格可能被占用。而直接添加。同时他也不会报封装类被改动的同步改动异常
	 * @param building boolean
	 * @return boolean
	 */
	public boolean prototypeBuilding(boolean building){
		return prototypeBuilding=building;
	}
	/**
	 * 如果GridLayout 是某个类的原型类，那么构建过程中，一般需要将封装类的句柄传递给GridLayout.以便GridLayout能够通知改类，它可能被改变了。
	 * @param wrapper 封装类
	 */
	public void setWrapper(PrototypeChangeable<Grid> wrapper){
		this.wrapper=wrapper;
	}
	/**
	 * 子元素，通知Gridlayout被变更了，继而通知封装类，内容被变更
	 */
	public void notifyChange(){
		if(wrapper!=null&&!prototypeBuilding){
			wrapper.notifyChage();
		}
	}
	/**
	 * 无格式
	 */
	public static final String FACE_NONE="none";

	/**
	 * 用横线把表格划分成多行
	 */
	public static final String FACE_LINE="line";
	/**
	 * 用横竖的线把表格划分成多个单元格
	 */
	public static final String FACE_CELL="cell";
	/**
	 * 用横线(虚线)把表格划分成多行
	 */
	public static final String FACE_DOT="dot";
//		private Thead2 thead;

	private String face;
	private Integer cellpadding;
	
//	private Boolean focusable;
	private Boolean focusmultiple;
//	private Boolean hoverable;

	private Boolean nobr;

	private Combo combo;
	private Integer limit;
	private Boolean resizable;
	private Boolean escape;
	private Boolean scroll;
	private String scrollClass;
	

	public Grid(String id) {
		super(id);
		this.setThead(new GridPart());
		this.setTbody(new GridPart());
	}
	@Override
    public String getType() {
		return "grid";
	}
	
	/**
	 * 表头
	 * @return Thead
	 */
	public GridPart getThead() {
		return thead;
	}
	
	/**
	 * 设置表头
	 * @param thead Thead
	 * @return 本身，这样可以继续设置其他属性
	 */
	public Grid setThead(GridPart thead) {
		if (thead == null) {
			throw new UnsupportedOperationException("Thead can not be null.");
		}
		thead.owner(this);
		this.thead = thead;
		return this;
	}
	
	/**
	 * 表体
	 * @return Tbody
	 */
	public GridPart getTbody() {
		return tbody;
	}
	/**
	 * 设置表体
	 * @param tbody 设置表体
	 * @return this
	 */
	public Grid setTbody(GridPart tbody) {
		if (tbody == null) {
			throw new UnsupportedOperationException("Tbody can not be null.");
		}
		tbody.owner(this);
		this.tbody = tbody;
		return this;
	}	
	
	/**
	 * 取得列属性定义
	 * @return List
	 */
	public List<GridColumn> getColumns() {
		return columns;
	}
	/**
	 * 取得可见列中的 key对应的列数。
	 * @return Map
	 */
	@Transient
	public Map<String,Integer> getVisableColumnNumMap() {
		Map<String,Integer> columnMap=new HashMap<String,Integer>();
		int column=0;
		if(getColumns()!= null){
			for(GridColumn c:getColumns()){
				if(c.isVisable()){
					columnMap.put(c.getField(), column++);
				}
			}
		}
		return columnMap;
	}
	/**
	 * 设置列属性定义
	 * @param columns 列属性定义
	 * @return this
	 */
	public Grid setColumns(List<GridColumn> columns) {
		this.columns = columns;
		return this;
	}
	
	/**
	 * 添加列
	 * @param gridColumn GridColumn
	 * @return 本身，这样可以继续设置其他属性
	 */
	@SuppressWarnings("deprecation")
	public Grid addColumn(GridColumn gridColumn) {
	 	//FIXME 重名
	 	if(Utils.isEmpty(gridColumn.getField())&&Utils.notEmpty(gridColumn.getWidth())){
//	 		int i=columns.size();
//			String s=Integer.toString(i+360, 36);
	 		int visableColumns=0;
	 		for(GridColumn column:columns){
	 			if(column.isVisable()){
	 				visableColumns++;
	 			}
	 		}
			gridColumn.setField(calcColumnLabel(visableColumns));
	 	}
	 	columns.add(gridColumn);
	 	return this;
	}
	
	/**
     * 和EXCEL 类似 第0列为A 第25列为Z 第26列为AA 第27列为AB 第51列为AZ 第52列为BA .. 第(27*26-1)列为ZZ 第27*26列为AAA 第27*27*26列为AAAA
     * @param size int
     * @return String
     */
    private static String calcColumnLabel(int size) {
 		int x=size;
 		StringBuilder sb=new StringBuilder();
 		do{
 			sb.append((char)('A'+ x % 26));//26个大写字符
 		}while( (x=x/26-1)>=0);
 		return sb.reverse().toString();
 	}
	
	@Override
    public Tr getPub() {
		if (pub == null) {
			setPub(new Tr());
		}
		return pub;
	}
	@Override
    public Grid setPub(Tr pub) {
		this.pub = pub;
		return this;
	}
	@SuppressWarnings("unchecked")
    @Override
	public List<Tr> findNodes() {
		List<Tr>resultList=new ArrayList<Tr>();
		if(tbody.findNodes()!=null){
			resultList.addAll(tbody.findNodes());
		}
		if(thead.findNodes()!=null){
			resultList.addAll(thead.findNodes());
		}
		return resultList;
	}
	@Override
	public Widget<?> findNodeById(String id) {
		Widget<?> w= tbody.findNodeById(id);
		if(w!=null){
			return w;
		}
		return thead.findNodeById(id);
	}

	@Override
	public Grid removeNodeById(String id) {
		tbody.removeNodeById(id);
		thead.removeNodeById(id);
		return this;
	}


	@Override
	public boolean replaceNodeById(Widget<?> w) {
		if(!tbody.replaceNodeById(w)){
			return thead.replaceNodeById(w);
		}
		return true;
	}

	
	  
	private HiddenPart hiddens = new HiddenPart();
    
	@Override
    public Grid add(Hidden hidden) {
		hiddens.add(hidden);
		return this;
	}
	@Override
    public Grid addHidden(String name, String value) {
		hiddens.addHidden(name, value);
		return this;
	}
//	public GridLayout addHidden(String name,AtExpression value) {
//		hiddens.addHidden(name, value);
//		return this;
//	}


	@Override
    public List<Hidden> getHiddens() {
		return hiddens.getHiddens();
	}
	
	@Override
    public List<String> getHiddenValue(String name) {
		return hiddens.getHiddenValue(name);
	}
	
	@Override
    public Grid removeHidden(String name) {
		hiddens.removeHidden(name);
		return this;
	}
	
	/**
	 * 点击聚焦效果,
	 * @param focusable Boolean
	 * @return this 
	 * @deprecated 目前通过{@link #getPub()}.{@link #setFocusable(Boolean)}来设置
	 */
	@Deprecated
	public Grid setFocusable(Boolean focusable) {
//		this.focusable = focusable;
		getPub().setFocusable(focusable);
		return this;
	}

	@Override
    public Boolean getFocusmultiple() {
		return focusmultiple;
	}

	@Override
    public Grid setFocusmultiple(Boolean focusmultiple) {
		this.focusmultiple = focusmultiple;
		return this;
	}

	/**
	 * 是否有鼠标悬停效果
	 * @return Boolean
	 * @deprecated 目前不支持
	 */
	@Override
    @Deprecated
	public Boolean getHoverable() {
		return null;
	}

	/**
	 * 是否有鼠标悬停效果
	 * @param hoverable Boolean
	 * @return 本身，这样可以继续设置其他属性
	 * @deprecated  目前不支持
	 */
	@Override
    @Deprecated
	public Grid setHoverable(Boolean hoverable) {
//		this.hoverable = hoverable;
		return this;
	}

	/**
	 * 设置当前的 grid 为某个 combobox 或 onlinebox 的数据选项表。
	 * @return combo
	 */
	public Combo getCombo() {
		return combo;
	}

	/**
	 * 设置当前的 grid 为某个 combobox 或 onlinebox 的数据选项表。
	 * @param combo 数据选项表。
	 * @return 本身，这样可以继续设置其他属性
	 */
	public Grid setCombo(Combo combo) {
		this.combo = combo;
		return this;
	}

	/**
	 * 最多显示多少行。如果需要前端翻页，可设置这个属性。
	 * 一般做combobox里的构成时才会用这个属性。
	 * @return limit
	 */
	public Integer getLimit() {
		return limit;
	}

	/**
	 * 最多显示多少行。如果需要前端翻页，可设置这个属性。
	 * 一般做combobox里的构成时才会用这个属性。
	 * @param limit Integer
	 * @return 本身，这样可以继续设置其他属性
	 */
	public Grid setLimit(Integer limit) {
		this.limit = limit;
		return this;
	}

	/**
	 * 是否可以拖动表头调整列宽。
	 * @return resizable
	 */
	public Boolean getResizable() {
		return resizable;
	}

	/**
	 * 是否可以拖动表头调整列宽。
	 * @param resizable Boolean
	 * @return 本身，这样可以继续设置其他属性
	 */
	public Grid setResizable(Boolean resizable) {
		this.resizable = resizable;
		return this;
	}
	/**
	 * 表格行的样式。可选值: line(默认值，横线), dot(虚线), cell(横线和竖线), none(无样式)。
	 * @return  face
	 */
	public String getFace() {
		return face;
	}

	/**
	 * 表格行的样式。可选值: line(默认值，横线), dot(虚线), cell(横线和竖线), none(无样式)。
	 * @param face 表格行的样式
	 * @return 本身，这样可以继续设置其他属性
	 */
	public Grid setFace(String face) {
		this.face = face;
		return this;
	}
	/**
	 * 空白填充
	 * @return cellpadding
	 */
	public Integer getCellpadding() {
		return cellpadding;
	}

	/**
	 * 空白填充
	 * @param cellpadding Integer(像素)
	 * @return 本身，这样可以继续设置其他属性
	 */

	public Grid setCellpadding(Integer cellpadding) {
		this.cellpadding = cellpadding;
		return this;
	}

	/**
	 * 内容不换行。
	 * @return nobr
	 */
	@Override
    public Boolean getNobr() {
		return nobr;
	}

	/**
	 * 内容过多的时候不会换行，而是隐藏不显示
	 * @param nobr Boolean
	 * @return 本身，这样可以继续设置其他属性
	 */
	@Override
    public Grid setNobr(Boolean nobr) {
		this.nobr = nobr;
		return this;
	}

	@Override
    public Boolean getEscape() {
		return escape;
	}
	@Override
    public Grid setEscape(Boolean escape) {
		this.escape = escape;
		return this;
	}
	
//		@Override
//		public JsonGridLayout add(Tr w) {
//			throw new UnsupportedOperationException("not support this method");
//	    }

	@Override
    public Boolean getScroll() {
		return scroll;
	}
	@Override
    public Grid setScroll(Boolean scroll) {
		this.scroll = scroll;
		return this;
	}
	@Override
    public String getScrollClass() {
		return scrollClass;
	}
	@Override
    public Grid setScrollClass(String scrollClass) {
		this.scrollClass = scrollClass;
		return this;
	}

	@Override
	public void clearNodes() {
		this.tbody.clearNodes();
		this.thead.clearNodes();
		if(this.columns!=null){
			this.columns.clear();
		}
	}
	/**
	 * 去除最下方和最右方空白的行和列。
	 * 注意最右方如果设置了样式或format 则会被视为有内容。
	 * 而如果有hidden列，也可能导致行不会被删除
	 * @return 本身，这样可以继续设置其他属性
	 */
	public Grid minimize(){
		//需要判定head的最大宽度和最大高度，以及Column的最大宽度
		//FIXME 
		
		Map<String,Integer> columnMap=new HashMap<String,Integer>();
		int column=0;
		int columnSize=0;
		for(GridColumn c:this.getColumns()){
			if(c.isVisable()){
				columnMap.put(c.getField(), column++);
				try {
					Map<String,Object>props=BeanUtil.getPropMap(c);
					props.remove("field");
					props.remove("dataColumnIndex");
					props.remove("beanProp");
					props.remove("dataFormat");
					props.remove("visable");
					if(WIDTH_REMAIN.equals(props.get("width"))){
						props.remove("width");
					}
					if(props.size()>0){
						columnSize=column;
					}
				} catch (Exception e) {
					LOG.error(null,e);
				} 
			}
		}
		int headRows=0,headColumns=0;
		int row=0;
		for(Tr tr: thead.getRows()){
			if(tr.getData()!=null){
			for(Map.Entry<String,Object>entry:tr.getData().entrySet()){
				String key=entry.getKey();
				Td td=(Td) entry.getValue();
				int rows=row+(td.getRowspan()==null?1:td.getRowspan());
				int formColumn=columnMap.get(key);
				int columns=formColumn+(td.getColspan()==null?1:td.getColspan());
				if(rows>headRows){headRows=rows;}
				if(columns>headColumns){headColumns=columns;}
			}}
			row++;
		}
		int bodyRows=0,bodyColumns=0;
		row=0;
		for(Tr tr: tbody.getRows()){
			if(tr.getData()!=null){
			for(Map.Entry<String,Object>entry:tr.getData().entrySet()){
				String key=entry.getKey();
				Td td=(Td) entry.getValue();
				int rows=row+(td.getRowspan()==null?1:td.getRowspan());
				int formColumn=columnMap.get(key);
				int columns=formColumn+(td.getColspan()==null?1:td.getColspan());
				if(rows>bodyRows){bodyRows=rows;}
				if(columns>bodyColumns){bodyColumns=columns;}
			}
			}
			row++;
		}
		
		retain(this.getColumns(),MathUtil.max(columnSize,bodyColumns,headColumns));
		retain(tbody.getRows(),bodyRows);
		retain(thead.getRows(),headRows);
		return this;
	}
	private void retain(List<?> list, int retainLength) {
		for(int i=list.size()-1;i>=retainLength;i--){
			list.remove(i);
		}
	}
	
	@Override
    public Grid add(Tr w) {
		tbody.add(w);
	    return this;
    }
    @Override
    public Grid add(int row, int column, Object o) {
		tbody.add(row, column, o);
	    return this;
    }
    @Override
    public Grid add(Integer fromRow, Integer fromColumn, Integer toRow, Integer toColumn, Object value) {
		tbody.add(fromRow, fromColumn, toRow, toColumn, value);
	    return this;
    }
    @Override
    public Grid put(int row, int column, Object o) {
		tbody.put(row, column, o);
	    return this;
    }
    @Override
    public Grid put(Integer fromRow, Integer fromColumn, Integer toRow, Integer toColumn, Object value) {
		tbody.put(fromRow, fromColumn, toRow, toColumn, value);
	    return this;
    }
    @Override
    public Grid removeNode(int row, int column) {
		tbody.removeNode(row, column);
	    return this;
    }
    @Override
    public boolean containsNode(int fromRow, int fromColumn, int toRow, int toColumn) {
	    return tbody.containsNode(fromRow, fromColumn, toRow, toColumn);
    }
    @Override
    public Grid removeNode(int fromRow, int fromColumn, int toRow, int toColumn) {
		tbody.removeNode(fromRow, fromColumn, toRow, toColumn);
	    return this;
    }
    
}
