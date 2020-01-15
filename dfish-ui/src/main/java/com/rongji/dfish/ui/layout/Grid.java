package com.rongji.dfish.ui.layout;

import com.rongji.dfish.base.util.BeanUtil;
import com.rongji.dfish.base.util.LogUtil;
import com.rongji.dfish.base.util.MathUtil;
import com.rongji.dfish.base.util.Utils;
import com.rongji.dfish.ui.*;
import com.rongji.dfish.ui.form.*;
import com.rongji.dfish.ui.JsonWrapper;
import com.rongji.dfish.ui.PrototypeChangeable;
import com.rongji.dfish.ui.RawJson;
import com.rongji.dfish.ui.widget.Highlight;
import com.rongji.dfish.ui.widget.Html;

import java.beans.Transient;
import java.util.*;

/**
 * GridLayout 为表格。
 * <p>作为DFISH3.x 最重要的一个布局面板之一，GridLayout是所有表格类布局的原型。包括分组表格，包括多层级(可折叠)表格。
 * 同时DFish2.x 的FormPanel / GridPanel 现在都是一个帮助类(封装类)。它们仅仅是为了使用起来方便，最终这些类，会生成GridLayout的实体。
 * 而DFish3.x大力推荐的FlexGrid也是该类的封装类。</p>
 * <p>与2.x相似一个表格的定义最基本的有 列定义，表头定义，表体定义。</p>
 * <div style="border-top:1px solid #333;border-bottom:1px solid #333;background-color:#FEC;line-height:120%;font-size:12px;"><pre>
 * {
 * "type":"grid","id":"f","face":"line","columns":[
 * {
 * "format":"javascript:return {\"type\":\"triplebox\",\"name\":\"selectItem\",\"value\":$id,\"sync\":\"click\"}","width":"40","align":"center","field":"grid_triplebox"
 * },{"width":"*","field":"C1"},{"width":"100","field":"C2"},{"format":"yyyy-MM-dd HH:mm","width":"100","field":"C3"}
 * ],
 * "thead":{
 * "rows":[
 * {
 * "grid_triplebox":{"type":"triplebox","name":"selectItem","checkall":true,"sync":"click"},"C1":"消息","C2":"发送人","C3":"时间"
 * }
 * ]
 * },"tbody":{
 * "rows":[
 * {"id":"000001","C1":"【通知】请各位同事明天着正装上班，迎接XX领导一行莅临参观指导。","C2":"行政部","C3":"2018-07-06 10:48:22"},
 * {"id":"000002","C1":"王哥，能不能把我工位上的一张XX项目的审批材料，拍个照发给我一下，谢谢","C2":"小张","C3":"2018-07-06 10:48:22"}
 * ]
 * }
 * }</pre></div>
 * <p>thead 和 tbody其实都是一个基本表格结构。他们可以分开设置样式，以实现独立的表头效果。thead可以没有。基本表格结构GridBody最基本格式为tr和td。</p>
 * <p>Tr的完整格式其实是</p>
 * <div style="border-top:1px solid #333;border-bottom:1px solid #333;background-color:#FEC;line-height:120%;font-size:12px;"><pre>
 * {
 * "cls":"tr-0",
 * "data":{"id":"000001","C1":"【通知】请各位同事明天着正装上班，迎接XX领导一行莅临参观指导。","C2":"行政部","C3":"2018-07-06 11:13:50"}
 * }</pre></div>
 * <p>为了节省流量可以只输出data的部分。</p>
 * <p>Td的完整格式其实是</p>
 * <div style="border-top:1px solid #333;border-bottom:1px solid #333;background-color:#FEC;line-height:120%;font-size:12px;"><pre>
 * {
 * "node":{
 * "type":"html","text":"【通知】请各位同事明天着正装上班，迎接XX领导一行莅临参观指导。","valign":"middle"
 * },
 * "rowspan":2
 * }</pre></div>
 * <p>如果没有rowspan或cls属性的时候，td可以只写其中node的部分。这时可以是一个html元素或一个Text等输入框。甚至可以是一个布局元素。里面放任何内容。
 * 而如果这个node就是为了输出一个html并且不需要复杂 cls 等属性的时候。 td就可以缩写成范例中的那样，一个字符串。用于最简输出。同时也更容易被人阅读，理解，和调试</p>
 * <p>支持折叠和多层折叠 详见 {@link com.rongji.dfish.ui.widget.Toggle} 和 {@link GridLeaf}</p>
 * <p>支持指定位置添加内容 见{@link Grid#add(int, int, Object)} 甚至可以直接指定一个区块合并单元格，并填充内容{@link Grid#add(Integer, Integer, Integer, Integer, Object)}</p>
 * <p>如基础定义所见，如果在GridLayout中直接指定位置添加内容，实际上指的是tbody部分。如果想在thead上使用该功能，要显式先取得 getThead()</p>
 *
 * @author DFish team
 * @since DFish 3.0
 */
public class Grid extends AbstractNodeContainer<Grid> implements ListView<Grid>,
        HiddenContainer<Grid>, HtmlContentHolder<Grid>, PubHolder<Grid, Grid.TR>, Scrollable<Grid>, GridOperation<Grid> {

    private static final long serialVersionUID = 6537737987499258183L;
    private THead tHead;
    private TBody tBody;
    private TFoot tFoot;
    private List<Column> columns = new ArrayList<>();
    private TR pub;

    private PrototypeChangeable<Grid> wrapper;
    private boolean prototypeBuilding = false;

    /**
     * 因为GridLayout经常作为其他封装类的圆形。所以提供该模式设置
     * 如果正在被当做原型来构建的时候，他们，原则上它构建的时候不要进行严格的校验会不会某格可能被占用。而直接添加。同时他也不会报封装类被改动的同步改动异常
     *
     * @return boolean
     */
    public boolean prototypeBuilding() {
        return prototypeBuilding;
    }

    /**
     * 因为GridLayout经常作为其他封装类的圆形。所以提供该模式设置
     * 如果正在被当做原型来构建的时候，他们，原则上它构建的时候不要进行严格的校验会不会某格可能被占用。而直接添加。同时他也不会报封装类被改动的同步改动异常
     *
     * @param building boolean
     * @return boolean
     */
    public boolean prototypeBuilding(boolean building) {
        return prototypeBuilding = building;
    }

    /**
     * 如果GridLayout 是某个类的原型类，那么构建过程中，一般需要将封装类的句柄传递给GridLayout.以便GridLayout能够通知改类，它可能被改变了。
     *
     * @param wrapper 封装类
     */
    public void setWrapper(PrototypeChangeable<Grid> wrapper) {
        this.wrapper = wrapper;
    }

    /**
     * 子元素，通知Gridlayout被变更了，继而通知封装类，内容被变更
     */
    public void notifyChange() {
        if (wrapper != null && !prototypeBuilding) {
            wrapper.notifyChange();
        }
    }

    /**
     * 无格式
     */
    public static final String FACE_NONE = "none";

    /**
     * 用横线把表格划分成多行
     */
    public static final String FACE_LINE = "line";
    /**
     * 用横竖的线把表格划分成多个单元格
     */
    public static final String FACE_CELL = "cell";
    /**
     * 用横线(虚线)把表格划分成多行
     */
    public static final String FACE_DOT = "dot";
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
        this.settHead(new THead());
        this.settBody(new TBody());
        this.settFoot(new TFoot());
    }

    @Override
    public String getType() {
        return "grid";
    }

    /**
     * 表头
     *
     * @return Thead
     */
    public THead gettHead() {
        return tHead;
    }

    /**
     * 设置表头
     *
     * @param tHead Thead
     * @return 本身，这样可以继续设置其他属性
     */
    public Grid settHead(THead tHead) {
        if (tHead == null) {
            throw new UnsupportedOperationException("Thead can not be null.");
        }
        tHead.owner(this);
        this.tHead = tHead;
        return this;
    }

    /**
     * 表体
     *
     * @return Tbody
     */
    public TBody gettBody() {
        return tBody;
    }

    /**
     * 设置表体
     *
     * @param tBody 设置表体
     * @return this
     */
    public Grid settBody(TBody tBody) {
        if (tBody == null) {
            throw new UnsupportedOperationException("Tbody can not be null.");
        }
        tBody.owner(this);
        this.tBody = tBody;
        return this;
    }

    /**
     * 表尾
     *
     * @return tfoot
     */
    public TFoot gettFoot() {
        return tFoot;
    }

    /**
     * 设置表尾
     *
     * @param tFoot Thead
     * @return 本身，这样可以继续设置其他属性
     */
    public Grid settFoot(TFoot tFoot) {
        if (tFoot == null) {
            throw new UnsupportedOperationException("tfoot can not be null.");
        }
        tFoot.owner(this);
        this.tFoot = tFoot;
        return this;
    }

    /**
     * 取得列属性定义
     *
     * @return List
     */
    public List<Column> getColumns() {
        return columns;
    }

    /**
     * 取得可见列中的 key对应的列数。
     *
     * @return Map
     */
    @Transient
    public Map<String, Integer> getVisableColumnNumMap() {
        Map<String, Integer> columnMap = new HashMap<String, Integer>();
        int column = 0;
        if (getColumns() != null) {
            for (Column c : getColumns()) {
                if (c.isVisable()) {
                    columnMap.put(c.getField(), column++);
                }
            }
        }
        return columnMap;
    }

    /**
     * 设置列属性定义
     *
     * @param columns 列属性定义
     * @return this
     */
    public Grid setColumns(List<Column> columns) {
        this.columns = columns;
        return this;
    }

    /**
     * 添加列
     *
     * @param gridColumn GridColumn
     * @return 本身，这样可以继续设置其他属性
     */
    @SuppressWarnings("deprecation")
    public Grid addColumn(Column gridColumn) {
        //FIXME 重名
        if (Utils.isEmpty(gridColumn.getField()) && Utils.notEmpty(gridColumn.getWidth())) {
//	 		int i=columns.size();
//			String s=Integer.toString(i+360, 36);
            int visableColumns = 0;
            for (Column column : columns) {
                if (column.isVisable()) {
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
     *
     * @param size int
     * @return String
     */
    private static String calcColumnLabel(int size) {
        int x = size;
        StringBuilder sb = new StringBuilder();
        do {
            sb.append((char) ('A' + x % 26));//26个大写字符
        } while ((x = x / 26 - 1) >= 0);
        return sb.reverse().toString();
    }

    @Override
    public TR getPub() {
        if (pub == null) {
            setPub(new TR());
        }
        return pub;
    }

    @Override
    public Grid setPub(TR pub) {
        this.pub = pub;
        return this;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List findNodes() {
        List<Node> resultList = new ArrayList<>();
        if (tBody.findNodes() != null) {
            resultList.addAll(tBody.findNodes());
        }
        if (tHead.findNodes() != null) {
            resultList.addAll(tHead.findNodes());
        }
        if (tFoot.findNodes() != null) {
            resultList.addAll(tHead.findNodes());
        }
        return resultList;
    }

    @Override
    public Node findNodeById(String id) {
        Node w = tBody.findNodeById(id);
        if (w != null) {
            return w;
        }
        w = tHead.findNodeById(id);
        if (w != null) {
            return w;
        }
        return tFoot.findNodeById(id);
    }

    @Override
    public Grid removeNodeById(String id) {
        tBody.removeNodeById(id);
        tHead.removeNodeById(id);
        tFoot.removeNodeById(id);
        return this;
    }


    @Override
    public boolean replaceNodeById(Widget<?> w) {
        if (!tBody.replaceNodeById(w)) {
            if (!tHead.replaceNodeById(w)) {
                return tFoot.replaceNodeById(w);
            }
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
     *
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
    public Boolean getFocusMultiple() {
        return focusmultiple;
    }

    @Override
    public Grid setFocusMultiple(Boolean focusMultiple) {
        this.focusmultiple = focusMultiple;
        return this;
    }

    /**
     * 是否有鼠标悬停效果
     *
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
     *
     * @param hoverable Boolean
     * @return 本身，这样可以继续设置其他属性
     * @deprecated 目前不支持
     */
    @Override
    @Deprecated
    public Grid setHoverable(Boolean hoverable) {
//		this.hoverable = hoverable;
        return this;
    }

    /**
     * 设置当前的 grid 为某个 combobox 或 onlinebox 的数据选项表。
     *
     * @return combo
     */
    public Combo getCombo() {
        return combo;
    }

    /**
     * 设置当前的 grid 为某个 combobox 或 onlinebox 的数据选项表。
     *
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
     *
     * @return limit
     */
    public Integer getLimit() {
        return limit;
    }

    /**
     * 最多显示多少行。如果需要前端翻页，可设置这个属性。
     * 一般做combobox里的构成时才会用这个属性。
     *
     * @param limit Integer
     * @return 本身，这样可以继续设置其他属性
     */
    public Grid setLimit(Integer limit) {
        this.limit = limit;
        return this;
    }

    /**
     * 是否可以拖动表头调整列宽。
     *
     * @return resizable
     */
    public Boolean getResizable() {
        return resizable;
    }

    /**
     * 是否可以拖动表头调整列宽。
     *
     * @param resizable Boolean
     * @return 本身，这样可以继续设置其他属性
     */
    public Grid setResizable(Boolean resizable) {
        this.resizable = resizable;
        return this;
    }

    /**
     * 表格行的样式。可选值: line(默认值，横线), dot(虚线), cell(横线和竖线), none(无样式)。
     *
     * @return face
     */
    public String getFace() {
        return face;
    }

    /**
     * 表格行的样式。可选值: line(默认值，横线), dot(虚线), cell(横线和竖线), none(无样式)。
     *
     * @param face 表格行的样式
     * @return 本身，这样可以继续设置其他属性
     */
    public Grid setFace(String face) {
        this.face = face;
        return this;
    }

    /**
     * 空白填充
     *
     * @return cellpadding
     */
    public Integer getCellpadding() {
        return cellpadding;
    }

    /**
     * 空白填充
     *
     * @param cellpadding Integer(像素)
     * @return 本身，这样可以继续设置其他属性
     */

    public Grid setCellpadding(Integer cellpadding) {
        this.cellpadding = cellpadding;
        return this;
    }

    /**
     * 内容不换行。
     *
     * @return nobr
     */
    @Override
    public Boolean getNoBr() {
        return nobr;
    }

    /**
     * 内容过多的时候不会换行，而是隐藏不显示
     *
     * @param noBr Boolean
     * @return 本身，这样可以继续设置其他属性
     */
    @Override
    public Grid setNoBr(Boolean noBr) {
        this.nobr = noBr;
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
    public void clearNodes() {
        this.tBody.clearNodes();
        this.tHead.clearNodes();
        this.tFoot.clearNodes();
        if (this.columns != null) {
            this.columns.clear();
        }
    }

    /**
     * 去除最下方和最右方空白的行和列。
     * 注意最右方如果设置了样式或format 则会被视为有内容。
     * 而如果有hidden列，也可能导致行不会被删除
     *
     * @return 本身，这样可以继续设置其他属性
     */
    public Grid minimize() {
        //需要判定head的最大宽度和最大高度，以及Column的最大宽度
        //FIXME

        Map<String, Integer> columnMap = new HashMap<String, Integer>();
        int column = 0;
        int columnSize = 0;
        for (Column c : this.getColumns()) {
            if (c.isVisable()) {
                columnMap.put(c.getField(), column++);
                try {
                    Map<String, Object> props = BeanUtil.getPropMap(c);
                    props.remove("field");
                    props.remove("dataColumnIndex");
                    props.remove("beanProp");
                    props.remove("dataFormat");
                    props.remove("visable");
                    if (WIDTH_REMAIN.equals(props.get("width"))) {
                        props.remove("width");
                    }
                    if (props.size() > 0) {
                        columnSize = column;
                    }
                } catch (Exception e) {
                    LogUtil.error(getClass(), null, e);
                }
            }
        }
        int headRows = 0, headColumns = 0;
        int row = 0;
        for (TR tr : tHead.getRows()) {
            if (tr.getData() != null) {
                for (Map.Entry<String, Object> entry : tr.getData().entrySet()) {
                    String key = entry.getKey();
                    TD td = (TD) entry.getValue();
                    int rows = row + (td.getRowSpan() == null ? 1 : td.getRowSpan());
                    int formColumn = columnMap.get(key);
                    int columns = formColumn + (td.getColSpan() == null ? 1 : td.getColSpan());
                    if (rows > headRows) {
                        headRows = rows;
                    }
                    if (columns > headColumns) {
                        headColumns = columns;
                    }
                }
            }
            row++;
        }
        int footRows = 0, footColumns = 0;
        row = 0;
        for (TR tr : tFoot.getRows()) {
            if (tr.getData() != null) {
                for (Map.Entry<String, Object> entry : tr.getData().entrySet()) {
                    String key = entry.getKey();
                    TD td = (TD) entry.getValue();
                    int rows = row + (td.getRowSpan() == null ? 1 : td.getRowSpan());
                    int formColumn = columnMap.get(key);
                    int columns = formColumn + (td.getColSpan() == null ? 1 : td.getColSpan());
                    if (rows > footRows) {
                        footRows = rows;
                    }
                    if (columns > footColumns) {
                        footColumns = columns;
                    }
                }
            }
            row++;
        }
        int bodyRows = 0, bodyColumns = 0;
        row = 0;
        for (TR tr : tBody.getRows()) {
            if (tr.getData() != null) {
                for (Map.Entry<String, Object> entry : tr.getData().entrySet()) {
                    String key = entry.getKey();
                    TD td = (TD) entry.getValue();
                    int rows = row + (td.getRowSpan() == null ? 1 : td.getRowSpan());
                    int formColumn = columnMap.get(key);
                    int columns = formColumn + (td.getColSpan() == null ? 1 : td.getColSpan());
                    if (rows > bodyRows) {
                        bodyRows = rows;
                    }
                    if (columns > bodyColumns) {
                        bodyColumns = columns;
                    }
                }
            }
            row++;
        }

        retain(this.getColumns(), MathUtil.max(columnSize, bodyColumns, headColumns, footColumns));
        retain(tBody.getRows(), bodyRows);
        retain(tHead.getRows(), headRows);
        retain(tFoot.getRows(), footRows);
        return this;
    }

    private void retain(List<?> list, int retainLength) {
        for (int i = list.size() - 1; i >= retainLength; i--) {
            list.remove(i);
        }
    }

    @Override
    public Grid add(Node w) {
        tBody.add((TR)w);
        return this;
    }

    @Override
    public Grid add(int row, int column, Object o) {
        tBody.add(row, column, o);
        return this;
    }

    @Override
    public Grid add(Integer fromRow, Integer fromColumn, Integer toRow, Integer toColumn, Object value) {
        tBody.add(fromRow, fromColumn, toRow, toColumn, value);
        return this;
    }

    @Override
    public Grid put(int row, int column, Object o) {
        tBody.put(row, column, o);
        return this;
    }

    @Override
    public Grid put(Integer fromRow, Integer fromColumn, Integer toRow, Integer toColumn, Object value) {
        tBody.put(fromRow, fromColumn, toRow, toColumn, value);
        return this;
    }

    @Override
    public Grid removeNode(int row, int column) {
        tBody.removeNode(row, column);
        return this;
    }

    @Override
    public boolean containsNode(int fromRow, int fromColumn, int toRow, int toColumn) {
        return tBody.containsNode(fromRow, fromColumn, toRow, toColumn);
    }

    @Override
    public Grid removeNode(int fromRow, int fromColumn, int toRow, int toColumn) {
        tBody.removeNode(fromRow, fromColumn, toRow, toColumn);
        return this;
    }

    /**
     * <p>配置某列的属性</p>
     * <p>属性介绍如下</p>
     * <ul>
     * <li><strong>beanProp 与 dataColumnIndex</strong>:<br>
     * 说明这个数据从data中获取的方式。如果data的格式为List&lt;Object[]&gt;则用dataColumnIndex指定取那个列的数据。
     * 否则，则用beanProp表示，取得哪个属性</li>
     * <li><strong>field</strong>:<br>
     * 数据在传输前台客户端的时候，转为JSON，这个为JSON的属性名，相当于是前台客户端里面的数据列的名字。注意不要有重复。</li>
     * <li><strong>label</strong>:<br>
     * 如果这个列是可以显示的，那么他应该有一个标题</li>
     * <li><strong>width</strong>:<br>
     * 如果这个列是可以显示的，那么他应该有一个宽度。宽度可以是数字(单位像素)，也可以是百分数，或者是<strong>*</strong>。
     * 如果有多个列为*,将平摊宽度</li>
     * <li><strong>align 和 valign</strong>:<br>
     * 这个列的对齐方式，默认为左对齐，高度居中</li>
     * <li><strong>sortfld 与 sortType</strong>:<br>
     * 如果这个列在前端可以有排序效果，那么需要指定它按哪个列(取field)排序的。而排序方式是文本还是数字可以由sortType指定。
     * 默认为文本。</li>
     * <li><strong>sortIcon 与 sortClick</strong>:<br>
     * 如果这个列在JAVA端可以有排序效果，那么需要指定它当前的排序方式用什么图标表示。并指定如果这个图标被点击时会触发什么动作。</li>
     * <li><strong>format</strong>:<br>
     * 如果这个字段显示的时候，需要一个复杂HTML，而每行中需要的变化的仅仅是少量的数据，可以使用format来减少传输量，
     * 典型的是做成一个链接。这个字段显示成一个链接，而其中只有标题和编号是变化的数据。那么可以使用format。
     * 并把标题和编号放在grid的数据中。format中以$id 和$tt 来动态加载数据(id和tt以产生的JSON为准)<br>
     * format以javascript:开头的将运行一系列java script代码</li>
     * <li><strong>dataFormat</strong>:<br>
     * 这个字段显示的时候的格式，这个不会输出到JSON中去，它在把时间/数字格式的字段输出时，就提供了格式化。如果字段是时间型
     * 格式请参看java.text.SimpleDateFormat的表达式，如果字段是字符型的，请参照java.text.NumberFormat的表达式<br>
     * format以javascript:开头的将运行一系列java script代码</li>
     * <li><strong>checkBoxName</strong>:<br>
     * 默认grid里面的checkbox提交的时候名字为selectItem 如果需要指定这个名字，(如需要同时提交多个grid的时候)，
     * 可以指定这个值</li>
     * <li><strong>synchronizedFocus</strong>:<br>
     * 当为 Radio/checkBox 是否让radio和当前焦点状态同步。
     * </li>
     * </ul>
     * 可以使用 text / checkbox / hidden / radio方法快捷构建一个GridColumn
     */
    public static class Column extends AbstractNode<Column> implements Alignable<Column>, VAlignable<Column> {

        /**
         *
         */
        private static final long serialVersionUID = 3246628575622594917L;

        public static final String COLUMN_FIELD_UNKNOWN = "UNKNOWN";

        private String beanProp;
        private int dataColumnIndex = -1;
        private String field;
        private String label;
        private String width;
        private Integer labelwidth;
        private String format;
        //	String sortsrc;
//	String sort;
        private String style;
        private String cls;
        private String align;
        private String valign;
        private String dataFormat;
        private Object tip;
        private Object sort;
        private Integer minwidth;
        private Integer maxwidth;
        private Highlight highlight;
        private String fixed;
        private RawJson rawFormat;


        public RawJson rawFormat() {
            return rawFormat;
        }

//	AbstractBox<?> box;

        /**
         * 排序-默认 {@link Sort#STATUS_DEFAULT}
         */
        @Deprecated
        public static final String SORT_DEFAULT = Sort.STATUS_DEFAULT;
        /**
         * 排序-正序 {@link Sort#STATUS_ASC}
         */
        @Deprecated
        public static final String SORT_ASC = Sort.STATUS_ASC;
        /**
         * 排序-倒序 {@link Sort#STATUS_DESC}
         */
        @Deprecated
        public static final String SORT_DESC = Sort.STATUS_DESC;

        public static final String BOX_NAME = "selectItem";

        /**
         * 是否浮动，不随滚动条滚动，粘在左边
         */
        public static final String FIXED_LEFT = "left";
        /**
         * 是否浮动，不随滚动条滚动，粘在右边
         */
        public static final String FIXED_RIGHT = "right";

        /**
         * 默认构造函数。
         */
        public Column() {
        }

        /**
         * <p>构造函数</p>
         * <p>这个构造函数适用与数据是List&lt;Object[]&gt;的时候.</p>
         *
         * @param dataColumnIndex int 数据对象是List&lt;Object[]&gt;时表示数据在第几列
         * @param field           String 输出时显示的JSON属性名字。注意不要有重复
         * @param label           String 在表头显示的标题
         * @param width           String 列宽度，可以是 数字表示多少像素 百分比 如35% 表示页面宽度 或* 自动分配
         */
        public Column(int dataColumnIndex, String field, String label, String width) {
            this.dataColumnIndex = dataColumnIndex;
            this.field = field;
            this.label = label;
            this.width = width;
        }

        /**
         * <p>构造函数</p>
         * <p>这个构造函数适用与数据是List&lt;Object[]&gt;的时候.</p>
         *
         * @param beanProp String String 数据对象是List&lt;JAVA Bean&gt;时表示属性名
         * @param field    String 输出时显示的JSON属性名字。注意不要有重复
         * @param label    String 在表头显示的标题
         * @param width    String 列宽度，可以是 数字表示多少像素 百分比 如35% 表示页面宽度 或* 自动分配
         */
        public Column(String beanProp, String field, String label, String width) {
            this.beanProp = beanProp;
            this.field = field;
            this.label = label;
            this.width = width;
        }

        /**
         * 构建一个TEXT类型的GridColumn
         *
         * @param dataColumnIndex int 数据对象是List&lt;Object[]&gt;时表示数据在第几列
         * @param field           String 输出时显示的JSON属性名字。注意不要有重复
         * @param label           String 在表头显示的标题
         * @param width           String 列宽度，可以是 数字表示多少像素 百分比 如35% 表示页面宽度 或* 自动分配
         * @return 本身，这样可以继续设置其他属性
         */
        public static Column text(int dataColumnIndex, String field, String label, String width) {
            return new Column(dataColumnIndex, field, label, width);
        }

        /**
         * 构建一个TEXT类型的GridColumn
         *
         * @param dataColumnIndex int 数据对象是List&lt;Object[]&gt;时表示数据在第几列
         * @param label           String 在表头显示的标题
         * @param width           String 列宽度，可以是 数字表示多少像素 百分比 如35% 表示页面宽度 或* 自动分配
         * @return 本身，这样可以继续设置其他属性
         */
        public static Column text(int dataColumnIndex, String label, String width) {
            return new Column(dataColumnIndex, null, label, width);
        }

        /**
         * 构建一个TEXT类型的GridColumn
         *
         * @param beanProp String String 数据对象是List&lt;JAVA Bean&gt;时表示属性名
         * @param field    String 输出时显示的JSON属性名字。注意不要有重复
         * @param label    String 在表头显示的标题
         * @param width    String 列宽度，可以是 数字表示多少像素 百分比 如35% 表示页面宽度 或* 自动分配
         * @return 本身，这样可以继续设置其他属性
         */
        public static Column text(String beanProp, String field, String label, String width) {
            return new Column(beanProp, field, label, width);
        }

        /**
         * 构建一个TEXT类型的GridColumn 在GridLayou调用的时候不自动绑定数据。所以没有绑定数据的选项和label
         * 这个一般在GridLayout原型的时候使用。
         *
         * @param field String 输出时显示的JSON属性名字。注意不要有重复
         * @param width String 列宽度，可以是 数字表示多少像素 百分比 如35% 表示页面宽度 或* 自动分配
         * @return 本身，这样可以继续设置其他属性
         */
        public static Column text(String field, String width) {
            return new Column(null, field, null, width);
        }

        /**
         * 构建一个TEXT类型的GridColumn 在GridLayou调用的时候不自动绑定数据。所以没有绑定数据的选项和label
         *
         * @param field  String 输出时显示的JSON属性名字。注意不要有重复
         * @param width  String 列宽度，可以是 数字表示多少像素 百分比 如35% 表示页面宽度 或* 自动分配
         * @param format 文本格式转化
         * @return 本身，这样可以继续设置其他属性
         */
        public static Column text(String field, String width, String format) {
            return new Column(null, field, null, width).setFormat(format);
        }

        /**
         * 构建一个HIDDEN类型的GridColumn
         *
         * @param beanProp String String 数据对象是List&lt;JAVA Bean&gt;时表示属性名
         * @param field    String 输出时显示的JSON属性名字。注意不要有重复
         * @return 本身，这样可以继续设置其他属性
         */
        public static Column hidden(String beanProp, String field) {
            return new Column(beanProp, field, null, null);
        }

        /**
         * 构建一个HIDDEN类型的GridColumn
         *
         * @param dataColumnIndex int 数据对象是List&lt;Object[]&gt;时表示数据在第几列
         * @param field           String 输出时显示的JSON属性名字。注意不要有重复
         * @return 本身，这样可以继续设置其他属性
         */
        public static Column hidden(int dataColumnIndex, String field) {
            return new Column(dataColumnIndex, field, null, null);
        }

        /**
         * 构建一个序号列
         *
         * @param label 标签
         * @param width 宽度
         * @return 本身，这样可以继续设置其他属性
         */
        public static Column rownum(String label, String width) {
            return rownum(label, null, width);
        }

        /**
         * 构建一个序号列
         *
         * @param label 标签
         * @param start 开始编号
         * @param width 宽度
         * @return 本身，这样可以继续设置其他属性
         */
        public static Column rownum(String label, Integer start, String width) {
            return new Column(null, null, label, width).setFormat("javascript:return " + new GridRowNum(start)).setAlign(Column.ALIGN_CENTER);
        }

        /**
         * 构建一个CHECKBOX类型的GridColumn
         *
         * @param checkedField String 所选中值指定的列名
         * @param width        String 列宽度，可以是 数字表示多少像素 百分比 如35% 表示页面宽度 或* 自动分配
         * @return 本身，这样可以继续设置其他属性
         * @see #gridTriplebox(String, String)
         */
        @Deprecated
        public static Column checkbox(String checkedField, String width) {
//		return new GridColumn( null, field, null, width).setBox(new Checkbox(CHECK_FIELD_NAME, null, null, null,null).setField(new BoxField().setValue(field))).setAlign(GridColumn.ALIGN_CENTER);
            // FIXME 刚开始若未指定field,这个效果就有问题
//		return new GridColumn( null, field, null, width).setModel(new GridTriplebox(CHECK_FIELD_NAME, null, null, null, null)).addModelfield("value", field).setAlign(GridColumn.ALIGN_CENTER);
//		return checkbox(null, FIELD_TRIPLEBOX, width, BOX_NAME, checkedField, null, null);
            return gridTriplebox(checkedField, width);
        }

        /**
         * 构建一个CHECKBOX类型的GridColumn
         *
         * @param beanProp     String 数据对象是List&lt;JAVA Bean&gt;时表示属性名
         * @param field        String 返回前端的属性名
         * @param width        String 列宽度，可以是 数字表示多少像素 百分比 如35% 表示页面宽度 或* 自动分配
         * @param boxName      box提交时的名字
         * @param checkedField 如果设定了这个字段，这个box选中状态将根据这个字段值进行设定
         * @param required     是否必填提交时校验
         * @return 本身，这样可以继续设置其他属性
         */
        @Deprecated
        public static Column checkbox(String beanProp, String field, String width, String boxName, String checkedField, Boolean required) {
//		return new GridColumn( beanProp, field, null, width).setBox(new Checkbox(boxName, null, null, null,null).addValidate(new Validate().setRequired(required)).setField(new BoxField().setValue(field).setChecked(checkedField))).setAlign(GridColumn.ALIGN_CENTER);
//		return new GridColumn(beanProp, field, null, width).setGridTriplebox(boxName, checkedField, required, sync);
            return gridTriplebox(beanProp, field, width).setGridTriplebox(boxName, checkedField, required, null);
        }

        /**
         * 构建一个CHECKBOX类型的GridColumn
         *
         * @param dataColumnIndex int 数据对象是List&lt;Object[]&gt;时表示数据在第几列
         * @param field           String 返回前端的属性名
         * @param width           String 列宽度，可以是 数字表示多少像素 百分比 如35% 表示页面宽度 或* 自动分配
         * @param boxName         box提交时的名字
         * @param checkedField    如果设定了这个字段，这个box选中状态将根据这个字段值进行设定
         * @param required        是否必填提交时校验
         * @return 本身，这样可以继续设置其他属性
         */
        @Deprecated
        public static Column checkbox(int dataColumnIndex, String field, String width, String boxName, String checkedField, Boolean required) {
            return gridTriplebox(dataColumnIndex, field, width).setGridTriplebox(boxName, checkedField, required, null);
        }

        /**
         * 构建一个GridTriplebox类型的GridColumn
         *
         * @param checkedField 如果设定了这个字段，这个box选中状态将根据这个字段值进行设定
         * @param width        String 列宽度，可以是 数字表示多少像素 百分比 如35% 表示页面宽度 或* 自动分配
         * @return 本身，这样可以继续设置其他属性
         */
        public static Column gridTriplebox(String checkedField, String width) {
            return new Column(null, null, null, width).setGridTriplebox(BOX_NAME, checkedField, null, null);
        }

        /**
         * 构建一个GridTriplebox类型的GridColumn
         *
         * @param beanProp String 数据对象是List&lt;JAVA Bean&gt;时表示属性名
         * @param field    String 返回前端的属性名
         * @param width    String 列宽度，可以是 数字表示多少像素 百分比 如35% 表示页面宽度 或* 自动分配
         * @return 本身，这样可以继续设置其他属性
         */
        public static Column gridTriplebox(String beanProp, String field, String width) {
            return new Column(beanProp, field, null, width).setGridTriplebox(BOX_NAME, field, null, null);
        }

        /**
         * 构建一个GridTriplebox类型的GridColumn
         *
         * @param dataColumnIndex int 数据对象是List&lt;Object[]&gt;时表示数据在第几列
         * @param field           String 返回前端的属性名
         * @param width           String 列宽度，可以是 数字表示多少像素 百分比 如35% 表示页面宽度 或* 自动分配
         * @return 本身，这样可以继续设置其他属性
         */
        public static Column gridTriplebox(int dataColumnIndex, String field, String width) {
            return new Column(dataColumnIndex, field, null, width).setGridTriplebox(BOX_NAME, field, null, null);
        }

//	/**
//	 * 构建一个Triplebox类型的GridColumn
//	 * @param checkedField 如果设定了这个字段，这个box选中状态将根据这个字段值进行设定
//	 * @param width String 列宽度，可以是 数字表示多少像素 百分比 如35% 表示页面宽度 或* 自动分配
//	 * @return 本身，这样可以继续设置其他属性
//	 */
//	public static GridColumn triplebox(String checkedField, String width) {
//		return triplebox(null, FIELD_TRIPLEBOX, width, BOX_NAME, checkedField, null);
//	}
//
//	/**
//	 * 构建一个Triplebox类型的GridColumn
//	 * @param beanProp String 数据对象是List&lt;JAVA Bean&gt;时表示属性名
//	 * @param field String 返回前端的属性名
//	 * @param width String 列宽度，可以是 数字表示多少像素 百分比 如35% 表示页面宽度 或* 自动分配
//	 * @param boxName box提交时的名字
//	 * @param checkedField 如果设定了这个字段，这个box选中状态将根据这个字段值进行设定
//	 * @param required 是否必填提交时校验
//	 * @return 本身，这样可以继续设置其他属性
//	 */
//	public static GridColumn triplebox(String beanProp, String field, String width, String boxName, String checkedField, Boolean required){
//		return new GridColumn(beanProp, field, null, width).setTriplebox(boxName, checkedField, required);
//	}
//
//
//	/**
//	 * 构建一个Triplebox类型的GridColumn
//	 * @param dataColumnIndex int 数据对象是List&lt;Object[]&gt;时表示数据在第几列
//	 * @param field String 返回前端的属性名
//	 * @param width String 列宽度，可以是 数字表示多少像素 百分比 如35% 表示页面宽度 或* 自动分配
//	 * @param boxName box提交时的名字
//	 * @param checkedField 如果设定了这个字段，这个box选中状态将根据这个字段值进行设定
//	 * @param required 是否必填提交时校验
//	 * @return 本身，这样可以继续设置其他属性
//	 */
//	public static GridColumn triplebox(int dataColumnIndex, String field, String width, String boxName, String checkedField, Boolean required){
//		return new GridColumn(dataColumnIndex, field, null, width).setTriplebox(boxName, checkedField, required);
//	}

        /**
         * 将此列设置为复选框
         *
         * @param sync String 同步状态,该参数可为空,参数值详见{@link AbstractBox#SYNC_CLICK}和{@link AbstractBox#SYNC_CLICK}
         * @return 本身，这样可以继续设置其他属性
         */
        public Column setGridTriplebox(String sync) {
            return setGridTriplebox(BOX_NAME, null, null, sync);
        }

        /**
         * 将此列设置为表格复选框
         *
         * @param boxName      String 复选框名称
         * @param checkedField String 所选值指向的列名
         * @param required     Boolean 是否必填
         * @param sync         String 同步状态,该参数可为空,参数值详见{@link AbstractBox#SYNC_CLICK}和{@link AbstractBox#SYNC_FOCUS}
         * @return 本身，这样可以继续设置其他属性
         */
        public Column setGridTriplebox(String boxName, String checkedField, Boolean required, String sync) {
            GridTriplebox triplebox = new GridTriplebox(boxName, null, null, null, null);
            triplebox.setSync(sync).addValidate(new Validate().setRequired(required));
            return setGridTriplebox(triplebox, checkedField);
        }

        /**
         * 设置公共的复选框
         *
         * @param triplebox 复选框
         * @return 本身，这样可以继续设置其他属性
         */
        public Column setGridTriplebox(GridTriplebox triplebox) {
            return setGridTriplebox(triplebox, null);
        }

        /**
         * 设置公共的复选框
         *
         * @param triplebox    复选框
         * @param checkedField String 所选值指向的列名
         * @return 本身，这样可以继续设置其他属性
         */
        public Column setGridTriplebox(GridTriplebox triplebox, String checkedField) {
            if (triplebox == null) {
                throw new IllegalArgumentException("The triplebox can not be null.");
            }
            String boxField = Utils.notEmpty(checkedField) ? checkedField : this.field;
            if (Utils.isEmpty(boxField)) {
                throw new IllegalArgumentException("The checkedField can not be null.");
            }
            if (Utils.isEmpty(triplebox.getName())) {
                triplebox.setName(BOX_NAME);
            }
            // 标题全选框
            this.rawFormat = new RawJson(triplebox.setCheckAll(true).toString());
            // 置空,省流量
            triplebox.setCheckAll(null);
            triplebox.setValue(new RawJson("$" + boxField));
            return this.setFormat("javascript:return " + triplebox).setAlign(Column.ALIGN_CENTER);
        }


        /**
         * 将此列设置为单选框
         *
         * @param sync String 同步状态,该参数可为空,参数值详见{@link AbstractBox#SYNC_CLICK}和{@link AbstractBox#SYNC_CLICK}
         * @return 本身，这样可以继续设置其他属性
         */
        public Column setGridRadio(String sync) {
            return setGridRadio(BOX_NAME, null, null, sync);
        }

//	/**
//	 * 将此列设置为单选框
//	 * @param boxName String 单选框名称
//	 * @param required Boolean 是否必填
//	 * @return 本身，这样可以继续设置其他属性
//	 */
//	public GridColumn setGridRadio(String boxName, Boolean required) {
//		return setGridRadio(boxName, null, required, null);
//	}

        /**
         * 将此列设置为单选框
         *
         * @param boxName      String 单选框名称
         * @param checkedField String 所选值指向的列名
         * @param required     Boolean 是否必填
         * @param sync         String 是否和行点击动作同步
         * @return 本身，这样可以继续设置其他属性
         */
        public Column setGridRadio(String boxName, String checkedField, Boolean required, String sync) {
            GridRadio gridRadio = new GridRadio(boxName, null, null, null, null);
            gridRadio.setSync(sync).addCls(sync).addValidate(new Validate().setRequired(required));
            return setGridRadio(gridRadio, checkedField);
        }

        /**
         * 将此列设置为单选框
         *
         * @param gridRadio GridRadio 单选框
         * @return 本身，这样可以继续设置其他属性
         */
        public Column setGridRadio(GridRadio gridRadio) {
            return setGridRadio(gridRadio, null);
        }

        /**
         * 将此列设置为单选框
         *
         * @param gridRadio    GridRadio 单选框
         * @param checkedField String 所选值指向的列名
         * @return 本身，这样可以继续设置其他属性
         */
        public Column setGridRadio(GridRadio gridRadio, String checkedField) {
            if (gridRadio == null) {
                throw new IllegalArgumentException("The gridRadio can not be null.");
            }
            String boxField = Utils.notEmpty(checkedField) ? checkedField : this.field;
            if (Utils.isEmpty(boxField)) {
                throw new IllegalArgumentException("The checkedField can not be null.");
            }
            if (Utils.isEmpty(gridRadio.getName())) {
                gridRadio.setName(BOX_NAME);
            }
            return this.setFormat("javascript:return " + gridRadio.setValue(new RawJson("$" + boxField))).setAlign(Column.ALIGN_CENTER);
        }

//	/**
//	 * 将此列设置为单选框
//	 * @param required Boolean 是否必填
//	 * @return 本身，这样可以继续设置其他属性
//	 */
//	public GridColumn setRadio(Boolean required) {
//		return setRadio(BOX_NAME, required);
//	}
//
//	/**
//	 * 将此列设置为单选框
//	 * @param boxName String 单选框名称
//	 * @param required Boolean 是否必填
//	 * @return 本身，这样可以继续设置其他属性
//	 */
//	public GridColumn setRadio(String boxName, Boolean required) {
//		return setRadio(boxName, null, required, null);
//	}
//
//	/**
//	 * 将此列设置为单选框
//	 * @param boxName String 单选框名称
//	 * @param checkedField String 所选值指向的列名
//	 * @param required Boolean 是否必填
//	 * @return 本身，这样可以继续设置其他属性
//	 */
//	public GridColumn setRadio(String boxName, String checkedField, Boolean required, String sync) {
//		return setCommonRadio(boxName, checkedField, required, new Radio(null, null, null, null, null).setSync(sync));
//	}

//	/**
//	 * 构建一个CHECKBOX类型的GridColumn
//	 * @param checkedField String 所选中值指定的列名
//	 * @param width String 列宽度，可以是 数字表示多少像素 百分比 如35% 表示页面宽度 或* 自动分配
//	 * @return 本身，这样可以继续设置其他属性
//	 */
//	public static GridColumn radio(String checkedField, String width){
////		return new GridColumn(null, field, null, width).setModel(new Radio(CHECK_FIELD_NAME, null, null, null,null), "value").setAlign(GridColumn.ALIGN_CENTER);
//		return radio(null, FIELD_RADIO, width, BOX_NAME, checkedField, null);
//	}
//
//	/**
//	 * 构建一个RADIO类型的GridColumn
//	 * @param beanProp String 数据对象是List&lt;JAVA Bean&gt;时表示属性名
//	 * @param field String 返回前端的属性名
//	 * @param width String 列宽度，可以是 数字表示多少像素 百分比 如35% 表示页面宽度 或* 自动分配
//	 * @param boxName box提交时的名字
//	 * @param checkedField 如果设定了这个字段，这个box选中状态将根据这个字段值进行设定
//	 * @param required 是否必填提交时校验
//	 * @return 本身，这样可以继续设置其他属性
//	 */
//	public static GridColumn radio(String beanProp,String field, String width,String boxName,String checkedField,Boolean required){
////		return new GridColumn(beanProp, field, null, width).setModel(new Radio(boxName, null, null, null,null).addValidate(new Validate().setRequired(required)), "value").setAlign(GridColumn.ALIGN_CENTER);
//		return new GridColumn(beanProp, field, null, width).setRadio(boxName, checkedField, required);
//	}
//	/**
//	 * 构建一个RADIO类型的GridColumn
//	 * @param dataColumnIndex int 数据对象是List&lt;Object[]&gt;时表示数据在第几列
//	 * @param field String 返回前端的属性名
//	 * @param width String 列宽度，可以是 数字表示多少像素 百分比 如35% 表示页面宽度 或* 自动分配
//	 * @param boxName box提交时的名字
//	 * @param checkedField 如果设定了这个字段，这个box选中状态将根据这个字段值进行设定
//	 * @param required 是否必填提交时校验
//	 * @return 本身，这样可以继续设置其他属性
//	 */
//	public static GridColumn radio(int dataColumnIndex,String field, String width,String boxName,String checkedField,Boolean required){
////		return new GridColumn( dataColumnIndex, field, null, width).setBox(new Radio(boxName, null, null, null,null).addValidate(new Validate().setRequired(required)).setField(new BoxField().setValue(field).setChecked(checkedField))).setAlign(GridColumn.ALIGN_CENTER);
//		return new GridColumn(dataColumnIndex, field, null, width).setRadio(boxName, checkedField, required);
//	}

        /**
         * 构建一个CHECKBOX类型的GridColumn
         *
         * @param checkedField String 所选中值指定的列名
         * @param width        String 列宽度，可以是 数字表示多少像素 百分比 如35% 表示页面宽度 或* 自动分配
         * @return 本身，这样可以继续设置其他属性
         */
        public static Column gridRadio(String checkedField, String width) {
//		return new GridColumn(null, field, null, width).setModel(new Radio(CHECK_FIELD_NAME, null, null, null,null), "value").setAlign(GridColumn.ALIGN_CENTER);
            return gridRadio(checkedField, width, null);
        }

        /**
         * 构建一个CHECKBOX类型的GridColumn
         *
         * @param checkedField String 所选中值指定的列名
         * @param width        String 列宽度，可以是 数字表示多少像素 百分比 如35% 表示页面宽度 或* 自动分配
         * @param sync         是否与行点击动作同步
         * @return 本身，这样可以继续设置其他属性
         */
        public static Column gridRadio(String checkedField, String width, String sync) {
            return gridRadio(null, null, width, BOX_NAME, checkedField, null, sync);
        }

        /**
         * 构建一个RADIO类型的GridColumn
         *
         * @param beanProp     String 数据对象是List&lt;JAVA Bean&gt;时表示属性名
         * @param field        String 返回前端的属性名
         * @param width        String 列宽度，可以是 数字表示多少像素 百分比 如35% 表示页面宽度 或* 自动分配
         * @param boxName      box提交时的名字
         * @param checkedField 如果设定了这个字段，这个box选中状态将根据这个字段值进行设定
         * @param required     是否必填提交时校验
         * @param sync         是否与行点击动作同步
         * @return 本身，这样可以继续设置其他属性
         */
        public static Column gridRadio(String beanProp, String field, String width, String boxName, String checkedField, Boolean required, String sync) {
//		return new GridColumn(beanProp, field, null, width).setModel(new Radio(boxName, null, null, null,null).addValidate(new Validate().setRequired(required)), "value").setAlign(GridColumn.ALIGN_CENTER);
            return new Column(beanProp, field, null, width).setGridRadio(boxName, checkedField, required, sync);
        }

        /**
         * 构建一个RADIO类型的GridColumn
         *
         * @param dataColumnIndex int 数据对象是List&lt;Object[]&gt;时表示数据在第几列
         * @param field           String 返回前端的属性名
         * @param width           String 列宽度，可以是 数字表示多少像素 百分比 如35% 表示页面宽度 或* 自动分配
         * @param boxName         box提交时的名字
         * @param checkedField    如果设定了这个字段，这个box选中状态将根据这个字段值进行设定
         * @param required        是否必填提交时校验
         * @param sync            是否与行点击动作同步
         * @return 本身，这样可以继续设置其他属性
         */
        public static Column gridRadio(int dataColumnIndex, String field, String width, String boxName, String checkedField, Boolean required, String sync) {
//		return new GridColumn( dataColumnIndex, field, null, width).setBox(new Radio(boxName, null, null, null,null).addValidate(new Validate().setRequired(required)).setField(new BoxField().setValue(field).setChecked(checkedField))).setAlign(GridColumn.ALIGN_CENTER);
            return new Column(dataColumnIndex, field, null, width).setGridRadio(boxName, checkedField, required, sync);
        }

        /**
         * 如果数据是List&lt;JavaBean&gt;则这里表示这个JavaBean的属性名。该列取这个属性的值
         *
         * @return the beanProp
         */
        @Transient
        public String getBeanProp() {
            return beanProp;
        }

        /**
         * <p>设置属性名</p>
         * <p>如果数据是List&lt;JavaBean&gt;则这里表示这个JavaBean的属性名。该列取这个属性的值</p>
         *
         * @param beanProp 属性名
         * @return this
         */
        public Column setBeanProp(String beanProp) {
            this.beanProp = beanProp;
            return this;
        }


        /**
         * 取得 该列在JSON中的属性名
         *
         * @return the field
         */
        public String getField() {
            return field;
        }

        /**
         * 设置 该列在JSON中的属性名
         * 一般来说grid名称指定以后，相关的操作如tr里面会按这个名字放置内容，所以，一般不会在运行期改变这个值。
         *
         * @param field 该列在JSON中的属性名
         * @return 本身，这样可以继续设置其他属性
         */
        public Column setField(String field) {
            this.field = field;
            return this;
        }


        /**
         * 标题(一般设置在表头上)
         *
         * @return the label
         */
        @Transient
        public String getLabel() {
            return label;
        }

        /**
         * 标题(一般设置在表头上)
         *
         * @param label the label to set
         * @return 本身，这样可以继续设置其他属性
         */
        public Column setLabel(String label) {
            this.label = label;
            return this;
        }

        /**
         * 标题(一般设置在表头上)
         *
         * @param label the label to set
         * @return 本身，这样可以继续设置其他属性
         * @see #setLabel(String)
         * @deprecated setLabel
         */
        @Deprecated
        public Column setTitle(String label) {
            this.label = label;
            return this;
        }

        /**
         * 宽度 支持 数字(单位：像素) 百分比或*
         *
         * @return the width
         */
        public String getWidth() {
            return width;
        }

        /**
         * 宽度 支持 数字(单位：像素) 百分比或*
         *
         * @param width the width to set
         * @return 本身，这样可以继续设置其他属性
         */
        public Column setWidth(String width) {
            this.width = width;
            return this;
        }

        /**
         * 表单标题宽度。
         *
         * @return Integer
         * @since 3.3
         */
        public Integer getLabelwidth() {
            return labelwidth;
        }

        /**
         * 表单标题宽度。
         *
         * @param labelwidth Integer
         * @return 本身，这样可以继续设置其他属性
         * @since 3.3
         */
        public Column setLabelwidth(Integer labelwidth) {
            this.labelwidth = labelwidth;
            return this;
        }

        /**
         * 格式化内容。支持替换 "$field" 和 "${field.prop}" 形式的变量。支持"javascript:"开头的js语句(需return返回值)。
         * 如果列表有多行，并且这个字段显示的时候，需要一个复杂HTML，而每行中需要的变化的仅仅是少量的数据，可以使用format来减少传输量。
         * 典型的有两种写法
         * <pre>
         * javascript:var d= this.x.data.s;if('1'==d){return \"&lt;span style='color:gray'&gt;唯一&lt;/span&gt;\"};return '';
         * </pre>或<pre>
         * [&lt;a href='javascript:;' onclick=\"demo.enterView(this,'$vId');\"&gt;查看&lt;/a&gt;]&amp;nbsp;
         * </pre>
         *
         * @return String
         */
        public Object getFormat() {
            return format;
        }

        /**
         * 格式化内容。支持替换 "$field" 和 "${field.prop}" 形式的变量。支持"javascript:"开头的js语句(需return返回值)。
         * 如果列表有多行，并且这个字段显示的时候，需要一个复杂HTML，而每行中需要的变化的仅仅是少量的数据，可以使用format来减少传输量。
         * 典型的有两种写法
         * <pre>
         * javascript:var d= this.x.data.s;if('1'==d){return \"&lt;span style='color:gray'&gt;唯一&lt;/span&gt;\"};return '';
         * </pre>或<pre>
         * [&lt;a href='javascript:;' onclick=\"demo.enterView(this,'$vId');\"&gt;查看&lt;/a&gt;]&amp;nbsp;
         * </pre>
         *
         * @param format String
         * @return 本身，这样可以继续设置其他属性
         */
        public Column setFormat(String format) {
            this.format = format;
            return this;
        }


        /**
         * 排序状态。设置此参数表示当前列可点击排序。可选值为三个: {@link #SORT_DEFAULT}, {@link #SORT_ASC}, {@link #SORT_DESC}
         *
         * @return Object
         */
        public Object getSort() {
            return sort;
        }

        /**
         * 设置列排序
         *
         * @param sort Boolean
         * @return 本身，这样可以继续设置其他属性
         */
        public Column setSort(Boolean sort) {
            this.sort = sort;
            return this;
        }

        /**
         * 设置列排序
         *
         * @param sort Sort
         * @return 本身，这样可以继续设置其他属性
         */
        public Column setSort(Sort sort) {
            this.sort = sort;
            return this;
        }

        /**
         * int 数据对象是List&lt;Object[]&gt;时表示数据在第几列
         *
         * @return the dataColumnIndex
         */
        @Transient
        public int getDataColumnIndex() {
            return dataColumnIndex;
        }

        /**
         * int 数据对象是List&lt;Object[]&gt;时表示数据在第几列
         *
         * @param dataColumnIndex int
         * @return 本身，这样可以继续设置其他属性
         */
        public Column setDataColumnIndex(int dataColumnIndex) {
            this.dataColumnIndex = dataColumnIndex;
            return this;
        }

        @Override
        public String getType() {
            return null;
        }

        /**
         * CSS样式
         *
         * @return String
         */
        public String getStyle() {
            return style;
        }

        /**
         * CSS样式
         *
         * @param style String
         * @return 本身，这样可以继续设置其他属性
         */
        public Column setStyle(String style) {
            this.style = style;
            return this;
        }

        /**
         * CSS样式类名
         *
         * @return String
         */
        public String getCls() {
            return cls;
        }

        /**
         * CSS样式类名
         *
         * @param cls String
         * @return 本身，这样可以继续设置其他属性
         */
        public Column setCls(String cls) {
            this.cls = cls;
            return this;
        }

        /**
         * 水平居中。可用值: left,right,center
         *
         * @return align
         * @see #ALIGN_LEFT
         * @see #ALIGN_RIGHT
         * @see #ALIGN_CENTER
         */
        @Override
        public String getAlign() {
            return align;
        }

        /**
         * 水平居中。可用值: left,right,center
         *
         * @param align String
         * @return 本身，这样可以继续设置其他属性
         * @see #ALIGN_LEFT
         * @see #ALIGN_RIGHT
         * @see #ALIGN_CENTER
         */
        @Override
        public Column setAlign(String align) {
            this.align = align;
            return this;
        }

        /**
         * 垂直对齐。可选值: top,middle,bottom
         *
         * @return valign String
         * @see #V_ALIGN_TOP
         * @see #V_ALIGN_MIDDLE
         * @see #V_ALIGN_BOTTOM
         */
        @Override
        public String getVAlign() {
            return valign;
        }

        /**
         * 垂直对齐。可选值: top,middle,bottom
         *
         * @param vAlign String
         * @return 本身，这样可以继续设置其他属性
         * @see #V_ALIGN_TOP
         * @see #V_ALIGN_MIDDLE
         * @see #V_ALIGN_BOTTOM
         */
        @Override
        public Column setVAlign(String vAlign) {
            this.valign = vAlign;
            return this;
        }

        /**
         * 格式化内容
         *
         * @return dataFormat
         */
        @Transient
        public String getDataFormat() {
            return dataFormat;
        }

        /**
         * 格式化内容
         *
         * @param dataFormat String
         * @return 本身，这样可以继续设置其他属性
         */
        public Column setDataFormat(String dataFormat) {
            this.dataFormat = dataFormat;
            return this;
        }

        /**
         * 是否可见的。如果是false表示当前GridColumn是隐藏字段，不输出给javascript前端。仅仅用于JAVA后端用于方便获取属性用，起到方便开发的作用
         *
         * @return boolean
         */
        @Transient
        public boolean isVisable() {
            return this.width != null && !"".equals(this.width);
        }

        /**
         * 选项表单，类型是 checkbox 或 radio。
         *
         * @return box
         */
        @Deprecated
        public AbstractBox<?> getBox() {
            throw new UnsupportedOperationException("");
        }

        /**
         * 选项表单，类型是 checkbox 或 radio。
         *
         * @param box AbstractBox
         * @return 本身，这样可以继续设置其他属性
         */
        public Column setBox(AbstractBox<?> box) {
            if (box == null) {
                this.setFormat("");
                return this;
            }
            Boolean required = null;
            if (box.getValidate() != null) {
                required = box.getValidate().getRequired();
            }
            if (box instanceof TripleBox || box instanceof CheckBox) {
                setGridTriplebox(box.getName(), null, required, box.getSync());
            } else if (box instanceof Radio) {
                setGridRadio(box.getName(), null, required, box.getSync());
            } else {
                throw new IllegalArgumentException("The box must be Triplebox or Radio");
            }
            return this;
        }

        /**
         * 排序结果的 url
         *
         * @return String
         */
        @Deprecated
        public String getSortsrc() {
            if (sort != null && sort instanceof Sort) {
                return ((Sort) sort).getSrc();
            }
            return null;
        }

        /**
         * 排序结果的 url
         *
         * @param sortsrc String
         * @return 本身，这样可以继续设置其他属性
         */
        @Deprecated
        public Column setSortsrc(String sortsrc) {
            this.sort = new Sort().setSrc(sortsrc);
            return this;
        }

        /**
         * 提示的字段
         *
         * @return String
         */
        @Deprecated
        public String getTipfield() {
            if (tip != null && tip instanceof Tip) {
                return ((Tip) tip).getField();
            }
            return null;
        }

        /**
         * 提示的字段
         *
         * @param tipfield String
         * @return 本身，这样可以继续设置其他属性
         */
        @Deprecated
        public Column setTipfield(String tipfield) {
            return setTip(new Tip().setField(tipfield));
        }

        /**
         * 提示的字段
         *
         * @param tip Boolean
         * @return 本身，这样可以继续设置其他属性
         */
        public Column setTip(Boolean tip) {
            this.tip = tip;
            return this;
        }

        /**
         * 提示的字段
         *
         * @param tip Tip
         * @return 本身，这样可以继续设置其他属性
         */
        public Column setTip(Tip tip) {
            this.tip = tip;
            return this;
        }

        /**
         * 提示的字段
         *
         * @return Object
         */
        public Object getTip() {
            return tip;
        }

        /**
         * 列的最小宽度
         *
         * @return Integer
         */
        public Integer getMinwidth() {
            return minwidth;
        }

        /**
         * 列的最小宽度
         *
         * @param minwidth Integer
         * @return 本身，这样可以继续设置其他属性
         */
        public Column setMinwidth(Integer minwidth) {
            this.minwidth = minwidth;
            return this;
        }

        /**
         * 列的最大宽度
         *
         * @return Integer
         */
        public Integer getMaxwidth() {
            return maxwidth;
        }

        /**
         * 列的最大宽度
         *
         * @param maxwidth Integer
         * @return 本身，这样可以继续设置其他属性
         */
        public Column setMaxwidth(Integer maxwidth) {
            this.maxwidth = maxwidth;
            return this;
        }

        /**
         * 设置高亮效果
         *
         * @return Highlight
         */
        public Highlight getHighlight() {
            return highlight;
        }

        /**
         * 设置高亮效果
         *
         * @param highlight 高亮效果
         * @return 本身，这样可以继续设置其他属性
         */
        public Column setHighlight(Highlight highlight) {
            this.highlight = highlight;
            return this;
        }

        /**
         * 是否浮动，不随滚动条滚动，只能用left/right
         *
         * @return String
         */
        public String getFixed() {
            return fixed;
        }

        /**
         * 是否浮动，不随滚动条滚动，只能用left/right
         *
         * @param fixed String
         * @return this
         */
        public Column setFixed(String fixed) {
            this.fixed = fixed;
            return this;
        }

    }

    /**
     * 表格列排序
     *
     * @author lamontYu
     * @date 2017-07-31
     * @since 3.1
     */
    public static class Sort {

        /**
         * 排序状态-默认
         */
        public static final String STATUS_DEFAULT = "default";
        /**
         * 排序状态-正序
         */
        public static final String STATUS_ASC = "asc";
        /**
         * 排序状态-倒序
         */
        public static final String STATUS_DESC = "desc";

        private String field;
        private Boolean isnumber;
        private String status;
        private String src;

        /**
         * 排序的字段名
         *
         * @return String
         */
        public String getField() {
            return field;
        }

        /**
         * 设置排序的字段名
         *
         * @param field 字段名
         * @return 本身，这样可以继续设置其他属性
         */
        public Sort setField(String field) {
            this.field = field;
            return this;
        }

        /**
         * 是否按照数字方式排序
         *
         * @return Boolean
         */
        public Boolean getIsnumber() {
            return isnumber;
        }

        /**
         * 设置是否按照数字方式排序
         *
         * @param isnumber 按照数字方式排序
         * @return 本身，这样可以继续设置其他属性
         */
        public Sort setIsnumber(Boolean isnumber) {
            this.isnumber = isnumber;
            return this;
        }

        /**
         * 当前排序状态:{@link #STATUS_ASC} ,{@link #STATUS_DESC}
         *
         * @return String
         */
        public String getStatus() {
            return status;
        }

        /**
         * 设置当前排序状态:{@link #STATUS_ASC} ,{@link #STATUS_DESC}
         *
         * @param status 排序状态
         * @return 本身，这样可以继续设置其他属性
         */
        public Sort setStatus(String status) {
            this.status = status;
            return this;
        }

        /**
         * 如果排序数据需要后台支持，设置当前排序url
         *
         * @return String
         */
        public String getSrc() {
            return src;
        }

        /**
         * 如果排序数据需要后台支持，设置当前排序url
         *
         * @param src String
         * @return 本身，这样可以继续设置其他属性
         */
        public Sort setSrc(String src) {
            this.src = src;
            return this;
        }
    }

    /**
     * 表格列提示
     * @author lamontYu
     * @date 2017-07-31
     * @since 3.1
     */
    public static class Tip {

        private String field;

        /**
         * 提示的字段名
         * @return String
         */
        public String getField() {
            return field;
        }

        /**
         * 设置提示的字段名
         * @param field 字段名
         * @return 本身，这样可以继续设置其他属性
         */
        public Tip setField(String field) {
            this.field = field;
            return this;
        }

    }

    public static class THead extends Part {
        @Override
        public String getType() {
            return "Head";
        }
    }

    public static class TBody extends Part {
        @Override
        public String getType() {
            return "Body";
        }
    }

    public static class TFoot extends Part {
        @Override
        public String getType() {
            return "Foot";
        }
    }

    /**
     * 抽象的GridLayout 它有两个实例，一个是底层的JsonGridLayout 仅作吻合前端JSON要求的最低的封装，必要的getter和setter
     * 另一个是常用的GridLayout 可以指定某个位置直接增加内容。
     *
     * @author DFish team
     */
    private static abstract class Part extends AbstractNodeContainer<Part> implements GridOperation<Part> {
        /**
         *
         */
        private static final long serialVersionUID = -8651836374729282552L;

        /**
         * 构造函数
         *
         * @param id String
         */
        public Part(String id) {
            super(id);
        }

        /**
         * 默认构造函数
         */
        public Part() {
            super(null);
        }

        protected Grid owner;

        public Grid owner() {
            return owner;
        }

        public Part owner(Grid owner) {
            this.owner = owner;
            return this;
        }

        /**
         * 取得行
         *
         * @return List
         */
        public List<TR> getRows() {
            return (List)nodes;
        }


        @Override
        public Part add(Node row) {
            TR w=(TR)row;
            w.owner(owner);
            return super.add(w);
        }

        @Override
        public Part add(int row, int column, Object o) {
            put(row, column, o, false);
            return this;
        }

        private void put(int row, int column, Object o, boolean overwrite) {
            if (o != null && o instanceof AbstractTd) {
                int toColumn = column;
                int toRow = row;
                //防止加了如一个大小大于大于Grid
                AbstractTd<?> cast = (AbstractTd<?>) o;
                if (cast.getColSpan() != null && cast.getColSpan() > 1) {
                    toColumn += cast.getColSpan() - 1;
                }
                if (cast.getRowSpan() != null && cast.getRowSpan() > 1) {
                    toRow += cast.getRowSpan() - 1;
                }
                put(row, column, toRow, toColumn, o, overwrite);
            } else {
                put(row, column, row, column, o, overwrite);
            }
        }

        private void put(int fromRow, int fromColumn, int toRow, int toColumn, Object o, boolean overwrite) {
            if (owner == null) {
                throw new UnsupportedOperationException("can NOT use [x,y] mode when Thead or Tbody NOT in GridLayout.");
            }
            if (fromRow < 0 || fromColumn < 0 || toRow < 0 || toColumn < 0) { // 行列小于0,抛异常
                throw new IndexOutOfBoundsException("Poisition: (" + Math.min(fromRow, toRow) + "," + Math.min(fromColumn, toColumn) + ")");
            }
            if (fromRow > toRow) {
                int temp = fromRow;
                fromRow = toRow;
                toRow = temp;
            }
            if (fromColumn > toColumn) {
                int temp = fromColumn;
                fromColumn = toColumn;
                toColumn = temp;
            }
            if (o == null) {
                removeNode(fromRow, fromColumn, toRow, toColumn);
                return;
            }
            if (overwrite) {
                removeNode(fromRow, fromColumn, toRow, toColumn);
            } else {
                // 判定是否有重复，如果有抛出异常
                if (containsNode(fromRow, fromColumn, toRow, toColumn)) {
                    throw new UnsupportedOperationException("The position is occupied:[" + fromRow + "," + fromColumn + "," + toRow + "," + toColumn + "]");
                }
            }
            while (toRow >= nodes.size()) {
                nodes.add(new TR().owner(owner));
            }
            Map<Integer, String> columnMap = new HashMap<Integer, String>();
            int column = 0;
            List<Column> columns = owner.getColumns();
            // 获取当前已设定的列
            if (columns != null) {
                for (Column c : columns) {
                    if (c.isVisable()) {
                        columnMap.put(column++, c.getField());
                    }
                }
            }

            while (toColumn >= columnMap.size()) {
                Column gridColumn = Column.text(null, WIDTH_REMAIN);
                owner.addColumn(gridColumn);
                columnMap.put(columnMap.size(), gridColumn.getField());
            }

            int rowspan = toRow - fromRow + 1;
            int colspan = toColumn - fromColumn + 1;
            if (o instanceof AbstractTd) {
                //如果是entry是GridCell就直接使用GridCell但，GridCell的rowspan和
                //colspan 会重新计算。GridCell本身是什么模式就是什么模式
                AbstractTd<?> cell = (AbstractTd<?>) o;
                if (rowspan > 1) {
                    cell.setRowSpan(rowspan);
                }
                if (colspan > 1) {
                    cell.setColSpan(colspan);
                }

                ((Widget)nodes.get(fromRow)).setData(columnMap.get(fromColumn), cell);
            } else if (o instanceof Widget) {
                //如果entry 是 Widget 那么将会包装在一个GridCell里面
                if (rowspan > 1 || colspan > 1) {
                    AbstractTd<?> cell = new TD();
                    cell.setNode((Widget<?>) o);
                    if (rowspan > 1) {
                        cell.setRowSpan(rowspan);
                    }
                    if (colspan > 1) {
                        cell.setColSpan(colspan);
                    }

                    ((Widget)nodes.get(fromRow)).setData(columnMap.get(fromColumn), cell);
                } else {
                    ((Widget)nodes.get(fromRow)).setData(columnMap.get(fromColumn), o);
                }
            } else {
                if (o != null && (rowspan > 1 || colspan > 1)) {
                    JsonTd td = new JsonTd();
                    td.setText(o.toString());
                    if (rowspan > 1) {
                        td.setRowSpan(rowspan);
                    }
                    if (colspan > 1) {
                        td.setColSpan(colspan);
                    }
                    ((Widget)nodes.get(fromRow)).setData(columnMap.get(fromColumn), td);
                } else {
                    ((Widget)nodes.get(fromRow)).setData(columnMap.get(fromColumn), o);
                }
            }
            // 如果 columns 不够则，自动增加columns
            // 如果 tr不够，则自动增加tr的数量
            owner.notifyChange();
        }

        @Override
        public Part add(Integer fromRow, Integer fromColumn, Integer toRow, Integer toColumn, Object value) {
            if (toRow == null) {
                toRow = fromRow;
            }
            if (toColumn == null) {
                toColumn = fromColumn;
            }
            this.put(fromRow, fromColumn, toRow, toColumn, value, false);
            return this;
        }

        @Override
        public Part put(int row, int column, Object o) {
            put(row, column, o, true);
            return this;
        }

        @Override
        public Part put(Integer fromRow, Integer fromColumn, Integer toRow, Integer toColumn, Object value) {
            if (toRow == null) {
                toRow = fromRow;
            }
            if (toColumn == null) {
                toColumn = fromColumn;
            }
            this.put(fromRow, fromColumn, toRow, toColumn, value, true);
            return this;
        }

        @Override
        public Part removeNode(int row, int column) {
            removeNode(row, column, row, column);
            return this;
        }

        @Override
        public boolean containsNode(int fromRow, int fromColumn, int toRow, int toColumn) {
            if (owner == null) {
                throw new UnsupportedOperationException("can NOT use [x,y] mode when Thead or Tbody NOT in GridLayout.");
            }
            if (owner.prototypeBuilding()) {
                return false;//假定prototype的构建过程是稳定的，不会构建错内容，所以不检查格子是否被占用。
            }
            Map<String, Integer> columnMap = owner.getVisableColumnNumMap();
            int targetFromRow = 0;
            for (Object obj : nodes) {
                TR tr=(TR)obj;
                if (tr.getData() == null) {
                    targetFromRow++;
                    continue;
                }
                for (Map.Entry<String, Object> entry : tr.getData().entrySet()) {
                    String key = entry.getKey();
                    if (!columnMap.containsKey(key)) {
                        continue;//防止hidden字段进入查询
                    }
                    int targetFromColumn = columnMap.get(key);
                    if (entry.getValue() instanceof TD) {
                        TD td = (TD) entry.getValue();
                        int targetToRow = targetFromRow + (td.getRowSpan() == null ? 1 : td.getRowSpan()) - 1;
                        int targetToColumn = targetFromColumn + (td.getColSpan() == null ? 1 : td.getColSpan()) - 1;
                        if (targetFromRow <= toRow && targetToRow >= fromRow
                                && targetFromColumn <= toColumn && targetToColumn >= fromColumn) {
                            return true;
                        }
                    } else {
                        if (targetFromRow <= toRow && targetFromRow >= fromRow
                                && targetFromColumn <= toColumn && targetFromColumn >= fromColumn) {
                            return true;
                        }
                    }
                }
                targetFromRow++;
            }
            return false;
        }

        @Override
        public Part removeNode(int fromRow, int fromColumn, int toRow, int toColumn) {
            if (owner == null) {
                throw new UnsupportedOperationException("can NOT use [x,y] mode when Thead or Tbody NOT in GridLayout.");
            }
            // 如果有一个格子不能被移除则报错，不能被移除的可能，是指这个区域里面的某个格子被合并过单元，并且这个合并的单元格部分区域在这个区域之外。
            Map<String, Integer> columnMap = owner.getVisableColumnNumMap();
            List<Object[]> toRemove = new ArrayList<Object[]>();
            int row = 0;
            for (Object obj : nodes) {
                TR tr=(TR)obj;
                if (tr.getData() != null) {
                    for (Map.Entry<String, Object> entry : tr.getData().entrySet()) {
                        String key = entry.getKey();
                        if (!columnMap.containsKey(key)) {
                            continue;//防止hidden字段进入查询
                        }

                        int targetFromRow = row;
                        int targetFromColumn = columnMap.get(key);
                        int targetToRow = targetFromRow;
                        int targetToColumn = targetFromColumn;
                        if (entry.getValue() instanceof TD) {
                            TD td = (TD) entry.getValue();
                            targetToRow = targetFromRow + (td.getRowSpan() == null ? 1 : td.getRowSpan()) - 1;
                            targetToColumn = targetFromColumn + (td.getColSpan() == null ? 1 : td.getColSpan()) - 1;
                        }
                        if (targetFromRow <= toRow && targetToRow >= fromRow
                                && targetFromColumn <= toColumn && targetToColumn >= fromColumn) {
                            //有交叠
                            if (targetFromRow < fromRow || targetToRow > toRow
                                    || targetFromColumn < fromColumn || targetToColumn > toColumn) {
                                throw new UnsupportedOperationException("The position is occupied by the entry:[" + row + "," + targetFromColumn + "] \r\n" + entry.getValue());
                            }
                            toRemove.add(new Object[]{row, key});
                        }
                    }
                }
                row++;
            }
            row = 0;
            if (toRemove != null && toRemove.size() > 0) {
                for (Object[] o : toRemove) {
                    int row_ = (Integer) o[0];
                    String key = (String) o[1];
                    ((TR)nodes.get(row_)).removeData(key);
                }
                if (owner != null) {
                    owner.notifyChange();
                }
            }

            return this;
        }

        public Part setScroll(Boolean scroll) {
            this.scroll = scroll;
            return this;
        }

        public Boolean getScroll() {
            return scroll;
        }

        public Part setScrollClass(String scrollClass) {
            this.scrollClass = scrollClass;
            return this;
        }

        public String getScrollClass() {
            return scrollClass;
        }

        private Boolean scroll;
        private String scrollClass;
    }

    /**
     * Tr 表示 表格的行
     * <p>表格的行有三种工作模式</p>
     * <p>常见的是里面包行单元格(Td)。
     * 每个单元格是一个文本或独立的widget，有widget的功能和属性，只是有的时候可能并不会给每个单元格设置ID。</p>
     * <p>为了能让表格的json尽可能小。允许data类型为 文本 widget 或GridCell。
     * 并用{@link Grid.Column#getField()} 来说明这个内容显示在哪里。</p>
     * <p>当一行里面包含可折叠的子集内容的时候，它将包含rows属性。rows里面是一个有子集GridRow构成的List。
     * 而会有一个GridTreeItem字段用于做折叠操作的视觉效果</p>
     *
     * @see AbstractTd {@link Grid.Column} {@link GridLeaf}
     * @author DFish Team
     * @since DFish 3.0
     */
    public static class TR extends AbstractTr<TR> implements JsonWrapper<Object> {

        /**
         *
         */
        private static final long serialVersionUID = -1895404892414786019L;

        /**
         * 默认构造函数
         */
        public TR(){
            super();
        }
        protected Grid owner;

        public Grid owner() {
            return owner;
        }

        public TR owner(Grid owner) {
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
        public TR(String id){
            super(id);
        }

        /**
         * 拷贝构造函数，相当于clone
         * @param tr another tr
         */
        public TR(AbstractTr<?> tr){
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
                    tr.getBeforeContent()!=null||tr.getPrependContent()!=null||
                    tr.getAppendContent()!=null||tr.getAfterContent()!=null||
                    tr.getGid()!=null||tr.getHeightMinus()!=null||
                    tr.getMaxHeight()!=null||tr.getMaxWidth()!=null||
                    tr.getMinHeight()!=null||tr.getMinWidth()!=null||
                    (tr.getOn()!=null&&tr.getOn().size()>0)||
                    tr.getWidth()!=null||tr.getWidthMinus()!=null;
        }

        @Override
        public void setRows(List<TR> rows) {
            for(TR row:rows){
                row.owner(owner);
            }
            nc();
            super.setRows(rows);
        }

        @Override
        public TR addRow(TR row) {
            row.owner(owner);
            nc();
            return super.addRow(row);
        }

        @Override
        public TR setFocus(Boolean focus) {
            nc();
            return super.setFocus(focus);
        }

        @Override
        public TR setFocusable(Boolean focusable) {
            nc();
            return super.setFocusable(focusable);
        }

        @Override
        public TR removeNodeById(String id) {
            TR tr= super.removeNodeById(id);
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
        public TR setData(String key, Object value) {
            nc();
            if(value instanceof TD){
                ((TD) value).owner(owner);
            }
            return super.setData(key, value);
        }

        @Override
        public TR setSrc(String src) {
            nc();
            return super.setSrc(src);
        }

        @Override
        public TR setStyle(String style) {
            nc();
            return super.setStyle(style);
        }

        @Override
        public TR setCls(String cls) {
            nc();
            return super.setCls(cls);
        }

        @Override
        public TR setId(String id) {
            nc();
            return super.setId(id);
        }

        @Override
        public TR setGid(String gid) {
            nc();
            return super.setGid(gid);
        }

        @Override
        public TR setWidth(String width) {
            nc();
            return super.setWidth(width);
        }

        @Override
        public TR setHeight(String height) {
            nc();
            return super.setHeight(height);
        }

        @Override
        public TR setWidth(int width) {
            nc();
            return super.setWidth(width);
        }

        @Override
        public TR setHeight(int height) {
            nc();
            return super.setHeight(height);
        }

        @Override
        public TR setOn(String eventName, String script) {
            nc();
            return super.setOn(eventName, script);
        }

        @Override
        public TR setWidthMinus(Integer widthMinus) {
            nc();
            return super.setWidthMinus(widthMinus);
        }

        @Override
        public TR setHeightMinus(Integer heightMinus) {
            nc();
            return super.setHeightMinus(heightMinus);
        }

        @Override
        public TR setMaxWidth(int maxWidth) {
            nc();
            return super.setMaxWidth(maxWidth);
        }

        @Override
        public TR setMaxWidth(String maxWidth) {
            nc();
            return super.setMaxWidth(maxWidth);
        }

        @Override
        public TR setMaxHeight(String maxHeight) {
            nc();
            return super.setMaxHeight(maxHeight);
        }

        @Override
        public TR setMaxHeight(int maxHeight) {
            nc();
            return super.setMaxHeight(maxHeight);
        }

        @Override
        public TR setMinWidth(int minWidth) {
            nc();
            return super.setMinWidth(minWidth);
        }

        @Override
        public TR setMinWidth(String minWidth) {
            nc();
            return super.setMinWidth(minWidth);
        }

        @Override
        public TR setMinHeight(String minHeight) {
            nc();
            return super.setMinHeight(minHeight);
        }

        @Override
        public TR setMinHeight(int minHeight) {
            nc();
            return super.setMinHeight(minHeight);
        }
    }

    /**
     *  Td 表示一个Grid的单元格
     * <p>在一些复杂布局结构中，它可以占不止一行或不止一列</p>
     * <p>GridCell 有两种工作模式，他内部可以包含一个Widget或简单的包含一个文本，如果包含了widget文本模式将失效</p>
     * <p>虽然GridCell也是一个Widget，但其很可能并不会专门设置ID。</p>
     * <p>由于很多情况下，界面上出现的就是TD元素。所以，TD元素一般输出的时候会自动简写</p>
     * 其完整格式为。
     * <pre>
     * {
     *   "align":"right","node":{
     *     "type":"html","text":"杨桃","style":"background-color:gray"
     *   }
     * }</pre>
     * <p>如果没有TD本身的属性都没有设置。很可能只输出node的部分</p>
     * {"type":"html","text":"杨桃","style":"background-color:gray"}
     * <p>而如果这时候html的其他属性也没设置，很可能进一步简写为 "杨桃"</p>
     * <p>一个特殊的简写规则，如果Td设置了属性，但内部node是Html而且只设置了text属性，可以被简写为</p>
     * <p>{"text":"橙子","align":"right"}</p>
     * <p>而td本身并没有setText属性</p>
     *
     * @author DFish Team
     * @see TR
     */
    public static class TD extends AbstractTd<TD> implements JsonWrapper<Object>{
        /**
         *
         */
        private static final long serialVersionUID = 4639610865052336483L;
        /**
         * 默认构造函数
         */
        public TD(){
            super();
        }
        /**
         * 拷贝构造函数 相当于clone
         * @param td  AbstractTd
         */
        public TD(AbstractTd<?> td){
            super();
            copyProperties(this,td);
        }
        protected Grid owner;

        public Grid owner() {
            return owner;
        }

        public TD owner(Grid owner) {
            this.owner = owner;
            return this;
        }
        private void nc(){
            if(owner!=null){
                owner.notifyChange();
            }
        }

        @Override
        public Object getPrototype() {
            if(hasTdllInfo(this)){
                JsonTd p=new JsonTd();
                copyProperties(p,this);
                if(isTextWidget(getNode())){
                    //把文本当做cell的text
                    String text=getTextValue(getNode());
                    p.setText(text);
                    p.setNode(null);
                    return p;
                }else{
                    p.setNode(getNode());
                    return p;
                }
            }else{
                Widget<?> w=getNode();
                if(isTextWidget(getNode())){
                    String text=getTextValue(getNode());
                    return text;
                }else{
                    return w;
                }
            }
        }

        private static boolean hasTdllInfo(AbstractTd<?> td){
            return td.getId()!=null||td.getHeight()!=null||
                    td.getAlign()!=null||td.getVAlign()!=null||
                    td.getColSpan()!=null||td.getRowSpan()!=null||
                    td.getCls()!=null||td.getStyle()!=null ||//常用的属性排在前面
                    td.getBeforeContent()!=null||td.getPrependContent()!=null||
                    td.getAppendContent()!=null||td.getAfterContent()!=null||
                    td.getGid()!=null||td.getHeightMinus()!=null||
                    td.getMaxHeight()!=null||td.getMaxWidth()!=null||
                    td.getMinHeight()!=null||td.getMinWidth()!=null||
                    (td.getOn()!=null&&td.getOn().size()>0)||
                    td.getWidth()!=null||td.getWidthMinus()!=null
                    ||td.getLabelWidth()!=null;
        }
        /**
         * 取得html的内容。为了效率这个方法不再进行判断，所以只能跟在isTextWidget 后使用。
         * @param node Widget
         * @return String
         */
        private String getTextValue(Widget<?> node) {
            Object prototype=node;
            while (prototype instanceof JsonWrapper){
                prototype=((JsonWrapper<?>)prototype).getPrototype();
            }
            Html cast=(Html)prototype;
            return cast.getText();
        }
        /**
         * 是不是内容里只有Text部分是有效信息，如果是的话。这里要简化输出json
         * @param node
         * @return 是否文本组件
         */
        private static boolean isTextWidget(Widget<?> node) {
            if(node==null){
                return false;
            }
            Object prototype=node;
            while (prototype instanceof JsonWrapper){
                prototype=((JsonWrapper<?>)prototype).getPrototype();
            }
            if(!(prototype instanceof Html)){
                return false;
            }
            Html html=(Html)prototype;
            return html.getId()==null&&html.getHeight()==null&&
                    html.getWidth()==null&&html.getEscape()==null&&
                    html.getAlign()==null&&html.getVAlign()==null&&
                    html.getCls()==null&&html.getStyle()==null &&//常用的属性排在前面
                    html.getAppendContent()==null&&html.getPrependContent()==null&&
                    html.getGid()==null&&html.getHeightMinus()==null&&
                    html.getMaxHeight()==null&&html.getMaxWidth()==null&&
                    html.getMinHeight()==null&&html.getMinWidth()==null&&
                    (html.getOn()==null||html.getOn().size()==0)&&
                    html.getScroll()==null&&html.getWidthMinus()==null;
        }
        @Override
        public TD setColSpan(Integer colSpan) {
            nc();
            return super.setColSpan(colSpan);
        }
        @Override
        public TD setRowSpan(Integer rowSpan) {
            nc();
            return super.setRowSpan(rowSpan);
        }
        @Override
        public TD setNode(Widget node) {
            nc();
            return super.setNode(node);
        }
        @Override
        public TD removeNodeById(String id) {
            TD td=super.removeNodeById(id);
            if(td!=null){
                nc();
            }
            return td;
        }
        @Override
        public boolean replaceNodeById(Widget<?> w) {
            boolean success=super.replaceNodeById(w);
            if(success){nc();}
            return success;
        }
        @Override
        public TD setVAlign(String vAlign) {
            nc();
            return super.setVAlign(vAlign);
        }
        @Override
        public TD setAlign(String align) {
            nc();
            return super.setAlign(align);
        }
        @Override
        public TD setStyle(String style) {
            nc();
            return super.setStyle(style);
        }
        @Override
        public TD setCls(String cls) {
            nc();
            return super.setCls(cls);
        }
        @Override
        public TD setPrependContent(String prependContent) {
            nc();
            return super.setPrependContent(prependContent);
        }
        @Override
        public TD setAppendContent(String appendContent) {
            nc();
            return super.setAppendContent(appendContent);
        }
        @Override
        public TD setId(String id) {
            nc();
            return super.setId(id);
        }
        @Override
        public TD setGid(String gid) {
            nc();
            return super.setGid(gid);
        }
        @Override
        public TD setWidth(String width) {
            nc();
            return super.setWidth(width);
        }
        @Override
        public TD setHeight(String height) {
            nc();
            return super.setHeight(height);
        }
        @Override
        public TD setWidth(int width) {
            nc();
            return super.setWidth(width);
        }
        @Override
        public TD setHeight(int height) {
            nc();
            return super.setHeight(height);
        }
        @Override
        public TD setOn(String eventName, String script) {
            nc();
            return super.setOn(eventName, script);
        }
        @Override
        public TD setWidthMinus(Integer widthMinus) {
            nc();
            return super.setWidthMinus(widthMinus);
        }
        @Override
        public TD setHeightMinus(Integer heightMinus) {
            nc();
            return super.setHeightMinus(heightMinus);
        }
        @Override
        public TD setData(String key, Object value) {
            nc();
            return super.setData(key, value);
        }
        @Override
        public TD setMaxWidth(int maxWidth) {
            nc();
            return super.setMaxWidth(maxWidth);
        }
        @Override
        public TD setMaxWidth(String maxWidth) {
            nc();
            return super.setMaxWidth(maxWidth);
        }
        @Override
        public TD setMaxHeight(String maxHeight) {
            nc();
            return super.setMaxHeight(maxHeight);
        }
        @Override
        public TD setMaxHeight(int maxHeight) {
            nc();
            return super.setMaxHeight(maxHeight);
        }
        @Override
        public TD setMinWidth(int minWidth) {
            nc();
            return super.setMinWidth(minWidth);
        }
        @Override
        public TD setMinWidth(String minWidth) {
            nc();
            return super.setMinWidth(minWidth);
        }
        @Override
        public TD setMinHeight(String minHeight) {
            nc();
            return super.setMinHeight(minHeight);
        }
        @Override
        public TD setMinHeight(int minHeight) {
            nc();
            return super.setMinHeight(minHeight);
        }

    }

    /**
     * Grid.Tr 表示 表格的行
     * <p>表格的行有三种工作模式</p>
     * <p>常见的是里面包行单元格(Td)。
     * 每个单元格是一个文本或独立的widget，有widget的功能和属性，只是有的时候可能并不会给每个单元格设置ID。</p>
     * <p>为了能让表格的json尽可能小。允许data类型为 文本 widget 或GridCell。
     * 并用{@link Grid.Column#getField} 来说明这个内容显示在哪里。</p>
     * <p>当一行里面包含可折叠的子集内容的时候，它将包含rows属性。rows里面是一个有子集GridRow构成的List。
     * 而会有一个GridTreeItem字段用于做折叠操作的视觉效果</p>
     *
     * @see AbstractTd {@link Grid.Column} {@link GridLeaf}
     * @author DFish Team
     * @param <T> 当前类型
     * @since DFish 3.0
     */
    protected static abstract class AbstractTr<T extends AbstractTr<T>> extends AbstractNodeContainer<T> {

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
        protected List<Grid.TR> rows;



        @Override
        @Deprecated
        public T add(Node w) {
            throw new UnsupportedOperationException("Use setData(String, GridCell) instead");
        }

        /**
         * 取得可折叠的子元素
         * @return List
         */
        public List<Grid.TR> getRows() {
            return rows;
        }
        /**
         * 设置可折叠的子元素
         * @param rows List
         */
        public void setRows(List<Grid.TR> rows) {
            this.rows = rows;
        }

        @Override
        public String getType() {
            return null;
        }
        /**
         * 添加一个可折叠的行。
         * @param row GridRow
         * @return 本身，这样可以继续设置其他属性
         */

        public T addRow(Grid.TR row) {
            if(rows==null){
                rows=new ArrayList<Grid.TR>();
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

        @Override
        public T removeNodeById(String id) {

            if (id == null||(rows==null&&data==null)) {
                return (T)this;
            }
            if(rows!=null){
                for (Iterator<TR> iter = rows.iterator(); iter.hasNext();) {
                    Grid.TR item = iter.next();
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
                            Layout<?> cast3 = (Layout<?>) entry.getValue();
                            cast3.removeNodeById(id);
                        }
                    }
                }
            }
            return (T) this;
        }

        @Override
        public List<Node<?>> findNodes() {
            List<Node<?>> result=new ArrayList<>();
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
        @Override
        public boolean replaceNodeById(Widget<?> panel) {
            if (panel == null || panel.getId() == null) {
                return false;
            }
            String id = panel.getId();
            if(rows!=null){
                for (int i = 0; i < rows.size(); i++) {
                    Grid.TR item = rows.get(i);
                    if (id.equals(item.getId())) {
                        // 替换该元素
                        rows.set(i, (Grid.TR)panel);
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
                        boolean replaced = ((Layout<?>)cast).replaceNodeById(panel);
                        if (replaced) {
                            return true;
                        }
                    }
                }
            }
            return false;
        }
        @Override
        public T setData(String key, Object value) {
            if(value==null){
                return (T)this;
            }

            if(data == null){
                data = new LinkedHashMap<String, Object>();
            }
            // 如果插入的内容是String/Object  以及非GridCell的Widget需要做一层封装。
            data.put(key, value);
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
    /**
     * 和javascript端是对应的TR模型。
     * json中如果tr没有cls等额外属性，可能会简化显示它的data部分
     * 所以Tr默认不能显示按封装类格式。这时候json中的原型将有可能还是这个JsonTr的格式
     * 也有可能是Map格式。
     * @author DFish team
     *
     */
    @SuppressWarnings("unchecked")
    protected static class JsonTr extends AbstractTr<JsonTr> {
        private static final long serialVersionUID = -1034767067781605568L;

    }
    /**
     *  Td 表示一个Grid的单元格
     * <p>在一些复杂布局结构中，它可以占不止一行或不止一列</p>
     * <p>GridCell 有两种工作模式，他内部可以包含一个Widget或简单的包含一个文本，如果包含了widget文本模式将失效</p>
     * <p>虽然GridCell也是一个Widget，但其很可能并不会专门设置ID。虽然它是一个Layout，但它最多包含1个子节点。即其内容。</p>
     * @author DFish Team
     * @param <T> 本身类型
     * @see Grid.TD
     */
    protected static abstract class AbstractTd<T extends AbstractTd<T>> extends AbstractNodeContainer<T>
            implements SingleNodeContainer<T ,Widget>,Alignable<T>, VAlignable<T> {

        private static final long serialVersionUID = -7870476532478876521L;
        /**
         * 默认构造函数
         */
        public AbstractTd() {
            super(null);
        }
        protected Integer colSpan;
        protected Integer rowSpan;
        //	private String text;
        protected String align;
        protected String vAlign;
        protected Widget<?> node;
        protected Boolean escape;
        protected String format;
        protected Integer labelWidth;

        @Override
        public String getType() {
            return null;
        }

        /**
         * 这个这个单元格占几列。
         * 为空的时候相当于1
         * @return Integer
         */
        public Integer getColSpan() {
            return colSpan;
        }
        /**
         * 这个这个单元格占几列。
         * 为空的时候相当于1
         * @param colSpan Integer
         * @return 本身，这样可以继续设置其他属性
         */
        public T setColSpan(Integer colSpan) {
            if(colSpan !=null){
                if(colSpan <1){
                    throw new java.lang.IllegalArgumentException("colspan must greater than 1");
                }
                if(colSpan ==1){
                    colSpan =null;
                }
            }
            this.colSpan = colSpan;
            return (T)this;
        }
        /**
         * 这个这个单元格占几行。
         * 为空的时候相当于1
         * @return Integer
         */
        public Integer getRowSpan() {
            return rowSpan;
        }
        /**
         * 这个这个单元格占几行。
         * 为空的时候相当于1
         * @param rowSpan Integer
         * @return 本身，这样可以继续设置其他属性
         */
        public T setRowSpan(Integer rowSpan) {
            if(rowSpan !=null){
                if(rowSpan <1){
                    throw new java.lang.IllegalArgumentException("rowspan must greater than 1");
                }
                if(rowSpan ==1){
                    rowSpan =null;
                }
            }
            this.rowSpan = rowSpan;
            return (T)this;
        }
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
        public T setNode(Widget node) {
            this.node =  node;
            return (T)this;
        }
        /**
         *
         * GridCell 下只能有一个node，所以add和setNode是相同的功能
         * @param node Widget
         * @return 本身，这样可以继续设置其他属性
         */
        @Override
        public T add(Node node) {
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
        public List<Node<?>> findNodes() {
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
        public String getVAlign() {
            return vAlign;
        }
        @Override
        public T setVAlign(String vAlign) {
            this.vAlign = vAlign;
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
            to.colSpan =from.colSpan;
            to.rowSpan =from.rowSpan;
            to.vAlign =from.vAlign;
            to.labelWidth =from.labelWidth;
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
        public Integer getLabelWidth() {
            return labelWidth;
        }

        /**
         * 表单标题宽度。
         * @param labelWidth Integer
         * @return 本身，这样可以继续设置其他属性
         * @since 3.3
         */
        public T setLabelWidth(Integer labelWidth) {
            this.labelWidth = labelWidth;
            return (T) this;
        }
    }

    /**
     * 和javascript端是对应的TD模型。
     * json中如果td没有cls等额外属性，可能会简化显示它的node
     * 如果这个node还是文本，可能会进一步简化显示成文本。
     * 所以Td默认不能显示按封装类格式。这时候json中的原型将有可能还是这个JsonTd的格式
     * 也有可能是Widget格式或者是text格式。
     * @author DfishTeam
     *
     */
    protected static class JsonTd extends AbstractTd<JsonTd> implements HasText<JsonTd>{
        /**
         *
         */
        private static final long serialVersionUID = -5125782398657967546L;
        private String text;
        /**
         * 文本模式时， 取得单元格内部文本的值
         * @return String
         */
        @Override
        public String getText() {
            return text;
        }
        /**
         * 文本模式时， 设置单元格内部文本的值
         * @param text String
         * @return 本身，这样可以继续设置其他属性
         */
        @Override
        public JsonTd setText(String text) {
            this.text = text;
            return this;
        }
    }

}
