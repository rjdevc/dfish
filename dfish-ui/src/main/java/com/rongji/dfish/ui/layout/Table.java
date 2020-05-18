package com.rongji.dfish.ui.layout;

import com.rongji.dfish.base.util.BeanUtil;
import com.rongji.dfish.base.util.LogUtil;
import com.rongji.dfish.base.util.MathUtil;
import com.rongji.dfish.base.util.Utils;
import com.rongji.dfish.ui.*;
import com.rongji.dfish.ui.auxiliary.*;
import com.rongji.dfish.ui.form.Hidden;

import java.beans.Transient;
import java.util.*;

/**
 * Table 为表格。
 * <p>作为DFISH3.x 最重要的一个布局面板之一，Table是所有表格类布局的原型。包括分组表格，包括多层级(可折叠)表格。
 * 同时DFish2.x 的FormPanel / TablePanel 现在都是一个帮助类(封装类)。它们仅仅是为了使用起来方便，最终这些类，会生成Table的实体。
 * 而DFish3.x大力推荐的FlexTable也是该类的封装类。</p>
 * <p>与2.x相似一个表格的定义最基本的有 列定义，表头定义，表体定义。</p>
 * <div style="border-top:1px solid #333;border-bottom:1px solid #333;background-color:#FEC;line-height:120%;font-size:12px;"><pre>
 * {
 * "type":"Table","id":"table","face":"line","columns":[
 * {
 * "format":"javascript:return {\"type\":\"TableTripleBox\",\"name\":\"selectItem\",\"value\":$id,\"sync\":\"click\"}","width":"40","align":"center","field":"tripleBox"
 * },{"width":"*","field":"C1"},{"width":"100","field":"C2"},{"format":"yyyy-MM-dd HH:mm","width":"100","field":"C3"}
 * ],
 * "tHead":{
 * "rows":[
 * {
 * "tripleBox":{"type":"TableTripleBox","name":"selectItem","checkAll":true,"sync":"click"},"C1":"消息","C2":"发送人","C3":"时间"
 * }
 * ]
 * },"tBody":{
 * "rows":[
 * {"id":"000001","C1":"【通知】请各位同事明天着正装上班，迎接XX领导一行莅临参观指导。","C2":"行政部","C3":"2018-07-06 10:48:22"},
 * {"id":"000002","C1":"王哥，能不能把我工位上的一张XX项目的审批材料，拍个照发给我一下，谢谢","C2":"小张","C3":"2018-07-06 10:48:22"}
 * ]
 * }
 * }</pre></div>
 * <p>tHead 和 tBody其实都是一个基本表格结构。他们可以分开设置样式，以实现独立的表头效果。tHead可以没有。基本表格结构TableBody最基本格式为tr和td。</p>
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
 * <p>支持折叠和多层折叠 详见 {@link com.rongji.dfish.ui.widget.Toggle} 和 {@link Leaf}</p>
 * <p>支持指定位置添加内容 见{@link Table#add(int, int, Object)} 甚至可以直接指定一个区块合并单元格，并填充内容{@link Table#add(Integer, Integer, Integer, Integer, Object)}</p>
 * <p>如基础定义所见，如果在Table中直接指定位置添加内容，实际上指的是tBody部分。如果想在tHead上使用该功能，要显式先取得 getThead()</p>
 *
 * @author DFish team
 * @since DFish3.0
 */
public class Table extends AbstractPubNodeContainer<Table, TR, TR> implements TableOperation<Table>,
        HiddenContainer<Table>,  Scrollable<Table> {

    private static final long serialVersionUID = 6537737987499258183L;
    private THead tHead;
    private TBody tBody;
    private TFoot tFoot;
    private List<Column> columns = new ArrayList<>();

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

    private String face;
    private Integer cellPadding;
    private Boolean focusMultiple;
    //    private Combo combo;
    private Boolean resizable;
    //    private Integer limit;

    private Boolean scroll;

    public Table() {
        this(null);
    }

    public Table(String id) {
        super(id);
        this.setTHead(new THead());
        this.setTBody(new TBody());
        this.setTFoot(new TFoot());

    }

    @Override
    protected TR newPub() {
        throw new UnsupportedOperationException();
    }
    @Override
    public TR pub(){
        return tBody.pub();
    }
    @Override
    @Deprecated
    public TR getPub(){
        return null;
    }
    @Override
    public Table setPub(TR pub){
        tBody.setPub(pub);
        return this;
    }

    /**
     * 表头
     *
     * @return Thead
     */
    public THead getTHead() {
        return tHead;
    }

    /**
     * 设置表头
     *
     * @param tHead Thead
     * @return 本身，这样可以继续设置其他属性
     */
    public Table setTHead(THead tHead) {
//        if (tHead == null) {
//            throw new UnsupportedOperationException("THead can not be null.");
//        }
        tHead.owner(this);
        this.tHead = tHead;
        return this;
    }

    /**
     * 表体
     *
     * @return Tbody
     */
    public TBody getTBody() {
        return tBody;
    }

    /**
     * 设置表体
     *
     * @param tBody 设置表体
     * @return this
     */
    public Table setTBody(TBody tBody) {
//        if (tBody == null) {
//            throw new UnsupportedOperationException("TBody can not be null.");
//        }
        tBody.owner(this);
        this.tBody = tBody;
        return this;
    }

    /**
     * 表尾
     *
     * @return tfoot
     */
    public TFoot getTFoot() {
        return tFoot;
    }

    /**
     * 设置表尾
     *
     * @param tFoot Thead
     * @return 本身，这样可以继续设置其他属性
     */
    public Table setTFoot(TFoot tFoot) {
//        if (tFoot == null) {
//            throw new UnsupportedOperationException("TFoot can not be null.");
//        }
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
    public Map<String, Integer> getVisibleColumnNumMap() {
        Map<String, Integer> columnMap = new HashMap<>();
        int column = 0;
        if (getColumns() != null) {
            for (Column c : getColumns()) {
                if (c.isVisible()) {
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
    public Table setColumns(List<Column> columns) {
        this.columns = columns;
        return this;
    }

    /**
     * 添加列
     *
     * @param gridColumn TableColumn
     * @return 本身，这样可以继续设置其他属性
     */
    @SuppressWarnings("deprecation")
    public Table addColumn(Column gridColumn) {
        //FIXME 重名
        if (Utils.isEmpty(gridColumn.getField()) && Utils.notEmpty(gridColumn.getWidth())) {
//	 		int i=columns.size();
//			String s=Integer.toString(i+360, 36);
            int visableColumns = 0;
            for (Column column : columns) {
                if (column.isVisible()) {
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

    private HiddenPart hiddens = new HiddenPart();

    @Override
    public Table add(Hidden hidden) {
        hiddens.add(hidden);
        return this;
    }

    @Override
    public Table addHidden(String name, String value) {
        hiddens.addHidden(name, value);
        return this;
    }
//	public Table addHidden(String name,AtExpression value) {
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
    public Table removeHidden(String name) {
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
    public Table setFocusable(Boolean focusable) {
//		this.focusable = focusable;
        getPub().setFocusable(focusable);
        return this;
    }

    /**
     * 是否有多选的点击高亮效果。
     * @return Boolean 是否多行聚焦
     */
    public Boolean getFocusMultiple() {
        return focusMultiple;
    }

    /**
     * 是否有多选的点击高亮效果。
     * @param focusMultiple 是否多行聚焦
     * @return 本身，这样可以继续设置其他属性
     */
    public Table setFocusMultiple(Boolean focusMultiple) {
        this.focusMultiple = focusMultiple;
        return this;
    }

//    /**
//     * 设置当前的 grid 为某个 combobox 或 onlinebox 的数据选项表。
//     *
//     * @return combo
//     */
//    public Combo getCombo() {
//        return combo;
//    }
//
//    /**
//     * 设置当前的 grid 为某个 combobox 或 onlinebox 的数据选项表。
//     *
//     * @param combo 数据选项表。
//     * @return 本身，这样可以继续设置其他属性
//     */
//    public Table setCombo(Combo combo) {
//        this.combo = combo;
//        return this;
//    }

//    /**
//     * 最多显示多少行。如果需要前端翻页，可设置这个属性。
//     * 一般做combobox里的构成时才会用这个属性。
//     *
//     * @return limit
//     */
//    public Integer getLimit() {
//        return limit;
//    }
//
//    /**
//     * 最多显示多少行。如果需要前端翻页，可设置这个属性。
//     * 一般做combobox里的构成时才会用这个属性。
//     *
//     * @param limit Integer
//     * @return 本身，这样可以继续设置其他属性
//     */
//    public Table setLimit(Integer limit) {
//        this.limit = limit;
//        return this;
//    }

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
    public Table setResizable(Boolean resizable) {
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
    public Table setFace(String face) {
        this.face = face;
        return this;
    }

    /**
     * 空白填充
     *
     * @return cellPadding
     */
    public Integer getCellPadding() {
        return cellPadding;
    }

    /**
     * 空白填充
     *
     * @param cellPadding Integer(像素)
     * @return 本身，这样可以继续设置其他属性
     */

    public Table setCellPadding(Integer cellPadding) {
        this.cellPadding = cellPadding;
        return this;
    }



//		@Override
//		public JsonTable add(Tr w) {
//			throw new UnsupportedOperationException("not support this method");
//	    }

    @Override
    public Boolean getScroll() {
        return scroll;
    }

    @Override
    public Table setScroll(Boolean scroll) {
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
    public Table minimize() {
        //需要判定head的最大宽度和最大高度，以及Column的最大宽度
        //FIXME

        Map<String, Integer> columnMap = new HashMap<>();
        int column = 0;
        int columnSize = 0;
        for (Column c : this.getColumns()) {
            if (c.isVisible()) {
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
        MinimizeState state = new MinimizeState();
        minimize(tBody, MinimizeState.TBODY, state, columnMap);
        minimize(tHead, MinimizeState.THEAD, state, columnMap);
        minimize(tFoot, MinimizeState.TFOOT, state, columnMap);

        retain(this.getColumns(), MathUtil.max(columnSize,
                state.columns[MinimizeState.TBODY],
                state.columns[MinimizeState.THEAD],
                state.columns[MinimizeState.TFOOT]));
        retain(tBody.getNodes(), state.columns[MinimizeState.TBODY]);
        retain(tHead.getNodes(), state.columns[MinimizeState.THEAD]);
        retain(tFoot.getNodes(), state.columns[MinimizeState.TFOOT]);
        return this;
    }

    private class MinimizeState {
        int[] rows;
        int[] columns;

        public MinimizeState() {
            rows = new int[3];
            columns = new int[3];
            Arrays.fill(rows, 0);
            Arrays.fill(columns, 0);
        }

        public static final int TBODY = 0;
        public static final int THEAD = 1;
        public static final int TFOOT = 2;
    }

    private void minimize(TablePart part, int type, MinimizeState state, Map<String, Integer> columnMap) {
        int rowIndex = 0;
        for (Node node : part.getNodes()) {
            TR tr = (TR) node;
            if (tr.getData() != null) {
                for (Map.Entry<String, Object> entry : tr.getData().entrySet()) {
                    String key = entry.getKey();
                    TD td = (TD) entry.getValue();
                    int rowCount = rowIndex + (td.getRowSpan() == null ? 1 : td.getRowSpan());
                    int formColumn = columnMap.get(key);
                    int columnCount = formColumn + (td.getColSpan() == null ? 1 : td.getColSpan());
                    if (rowCount > state.rows[type]) {
                        state.rows[type] = rowCount;
                    }
                    if (columnCount > state.columns[type]) {
                        state.columns[type] = columnCount;
                    }
                }
            }
            rowIndex++;
        }
    }

    private void retain(List list, int retainLength) {
        for (int i = list.size() - 1; i >= retainLength; i--) {
            list.remove(i);
        }
    }

    @Override
    public Table add(TR w) {
        tBody.add(w);
        return this;
    }

    @Override
    public Table add(int row, int column, Object o) {
        tBody.add(row, column, o);
        return this;
    }

    @Override
    public Table add(Integer fromRow, Integer fromColumn, Integer toRow, Integer toColumn, Object value) {
        tBody.add(fromRow, fromColumn, toRow, toColumn, value);
        return this;
    }

    @Override
    public Table put(int row, int column, Object o) {
        tBody.put(row, column, o);
        return this;
    }

    @Override
    public Table put(Integer fromRow, Integer fromColumn, Integer toRow, Integer toColumn, Object value) {
        tBody.put(fromRow, fromColumn, toRow, toColumn, value);
        return this;
    }

    @Override
    public Table removeNode(int row, int column) {
        tBody.removeNode(row, column);
        return this;
    }

    @Override
    public boolean containsNode(int fromRow, int fromColumn, int toRow, int toColumn) {
        return tBody.containsNode(fromRow, fromColumn, toRow, toColumn);
    }

    @Override
    public Table removeNode(int fromRow, int fromColumn, int toRow, int toColumn) {
        tBody.removeNode(fromRow, fromColumn, toRow, toColumn);
        return this;
    }

    @Override
    protected NodeContainerDecorator getNodeContainerDecorator() {
        return new NodeContainerDecorator() {
            @Override
            protected List<Node> nodes() {
                return Arrays.asList(tHead, tBody, tFoot);
            }

            @Override
            protected void setNode(int i, Node node) {
                switch (i) {
                    case 0:
                        tHead = (THead) node;
                        break;
                    case 1:
                        tBody = (TBody) node;
                        break;
                    case 2:
                        tFoot = (TFoot) node;
                        break;
                    default:
                        throw new IllegalArgumentException("expect 0-tHead 1-tBody  2-tFoot,, but get " + i);
                }
            }
        };
    }

}
