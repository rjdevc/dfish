package com.rongji.dfish.ui.widget;

import com.rongji.dfish.base.util.BeanUtil;
import com.rongji.dfish.base.util.LogUtil;
import com.rongji.dfish.base.util.StringUtil;
import com.rongji.dfish.base.util.Utils;
import com.rongji.dfish.ui.*;
import com.rongji.dfish.ui.form.Hidden;
import com.rongji.dfish.ui.layout.Grid;
import com.rongji.dfish.ui.layout.Grid.Column;
import com.rongji.dfish.ui.layout.ListView;

import java.util.ArrayList;
import java.util.List;

/**
 * @param <T> 当前对象类型
 * @author DFish Team
 */
@SuppressWarnings("unchecked")
public abstract class AbstractGridWrapper<T extends AbstractGridWrapper<T>> extends AbstractWidgetWrapper<T, Grid> implements Scrollable<T>, HiddenContainer<T>, PubHolder<T, Grid.TR>, ListView<T>, PrototypeChangeable<Grid> {

    private static final long serialVersionUID = -7218756238863890230L;
    /**
     * 用横竖的线把表格划分成多个单元格
     */
    public static final String FACE_CELL = Grid.FACE_CELL;
    /**
     * 用横线(虚线)把表格划分成多行
     */
    public static final String FACE_DOT = Grid.FACE_DOT;
    /**
     * 用横线把表格划分成多行
     */
    public static final String FACE_LINE = Grid.FACE_LINE;
    /**
     * 无格式
     */
    public static final String FACE_NONE = Grid.FACE_NONE;

    /**
     * 表格模型构造方法,表格模型编号
     *
     * @param id 表格编号
     */
    public AbstractGridWrapper(String id) {
        prototype = new Grid(id);
        prototype.setWrapper(this);
        bundleProperties();
    }

    protected static final int MODE_ARRAY = 2;
    protected static final int MODE_BEAN = 1;
    protected static final int MODE_UNDEFINED = 0;
    protected int dataMode = MODE_UNDEFINED;
    protected List<Column> columns = new ArrayList<>();

    protected Grid.TR pub;
//	protected Grid.Tr headRow;

    /**
     * 取得所有列定义
     *
     * @return List
     */
    public List<Column> getColumns() {
        return columns;
    }

    protected boolean hasTableHead = true;

    /**
     * 添加表格列
     *
     * @param column GridColumn
     * @return 本身，这样可以继续设置其他属性
     */
    public T addColumn(Column column) {
        if (column == null) {
            return (T) this;
        }
        checkMod(column);
        this.checkConcurrentModify();
        columns.add(column);
        return (T) this;
    }

    /**
     * 添加表格列
     *
     * @param index  在指定位置增加设置
     * @param column GridColumn
     * @return 本身，这样可以继续设置其他属性
     */
    public T addColumn(int index, Column column) {
        if (column == null) {
            return (T) this;
        }
        checkMod(column);
        this.checkConcurrentModify();
        columns.add(index, column);
        return (T) this;
    }

    protected void checkMod(Column column) {
        int currMode = MODE_UNDEFINED;
        if (column.getDataColumnIndex() >= 0) { // 数组模式
            currMode = MODE_ARRAY;
        } else if (Utils.notEmpty(column.getBeanProp())) {//Bean模式
            currMode = MODE_BEAN;
            //部分是用format写的特效，可以没有这种判断
        }
        if (currMode != MODE_UNDEFINED) {
            if (dataMode == MODE_UNDEFINED) {
                dataMode = currMode;
            } else if (dataMode != currMode) {
                throw new UnsupportedOperationException("data mode should be same");
            }
        }
        //FIXME 如果field重复则报异常
    }

    /**
     * 构建自己的原型。
     */
    protected abstract void buildPrototype();

    protected String getFormattedData(Column gc, Object data) {
        String pattern = gc.getDataFormat();
        if (Utils.isEmpty(pattern)) {
            return data.toString();
        }
        return StringUtil.format(data, pattern);
    }

    /**
     * 获取表格行格式
     *
     * @return 本身，这样可以继续设置其他属性
     */
    @Override
    public Grid.TR getPub() {
//		Grid.Tr pub = prototype.getPub();
//		if (pub == null) {
//			pub = new Grid.Tr();
//			prototype.setPub(pub);
//		}
        if (pub == null) {
            pub = new Grid.TR();
        }
        return pub;
    }

    /**
     * 获取表格行格式
     *
     * @param pub Grid.Tr
     * @return 本身，这样可以继续设置其他属性
     */
    @Override
    public T setPub(Grid.TR pub) {
        prototype.setPub(pub);
        return (T) this;
    }

    /**
     * 皮肤。表格行的样式。可选值: line(默认值，横线), dot(虚线), cell(横线和竖线), none(无样式)。
     *
     * @return 本身，这样可以继续设置其他属性
     */
    public String getFace() {
        return prototype.getFace();
    }

    /**
     * 皮肤。表格行的样式。可选值: line(默认值，横线), dot(虚线), cell(横线和竖线), none(无样式)。
     *
     * @param face String
     * @return 本身，这样可以继续设置其他属性
     */
    public T setFace(String face) {
        prototype.setFace(face);
        return (T) this;
    }

    /**
     * 单元格边距
     *
     * @return 本身，这样可以继续设置其他属性
     */
    public Integer getCellpadding() {
        return prototype.getCellpadding();
    }

    /**
     * 单元格边距
     *
     * @param cellpadding Integer
     * @return 本身，这样可以继续设置其他属性
     */
    public T setCellpadding(Integer cellpadding) {
        prototype.setCellpadding(cellpadding);
        return (T) this;
    }

    /**
     * 内容不换行。
     *
     * @return 本身，这样可以继续设置其他属性
     */
    @Override
    public Boolean getNoBr() {
        return prototype.getNoBr();
    }

    @Override
    public Boolean getScroll() {
        return prototype.getScroll();
    }

    @Override
    public T setScroll(Boolean scroll) {
        prototype.setScroll(scroll);
        return (T) this;
    }

    /**
     * 是否可以拖动表头调整列宽。
     *
     * @return Boolean
     */
    public Boolean getResizable() {
        return prototype.getResizable();
    }

    /**
     * 是否可以拖动表头调整列宽。
     *
     * @param resizable Boolean
     * @return this
     */
    public T setResizable(Boolean resizable) {
        prototype.setResizable(resizable);
        return (T) this;
    }

    @Override
    public T add(Hidden hidden) {
        prototype.add(hidden);
        return (T) this;
    }

    @Override
    public T addHidden(String name, String value) {
        prototype.addHidden(name, value);
        return (T) this;
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
     *
     * @param focusable Boolean
     * @return this
     */
    @Deprecated
    public T setFocusable(Boolean focusable) {
        prototype.setFocusable(focusable);
        return (T) this;
    }

    @Override
    public Boolean getFocusMultiple() {
        return prototype.getFocusMultiple();
    }

    @Override
    public T setFocusMultiple(Boolean focusMultiple) {
        prototype.setFocusMultiple(focusMultiple);
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
    public T setNoBr(Boolean noBr) {
        prototype.setNoBr(noBr);
        return (T) this;
    }

    /**
     * 内容转义
     * <p>描述:</p>
     *
     * @return 本身，这样可以继续设置其他属性
     */
    public Boolean getEscape() {
        return prototype.getEscape();
    }

    /**
     * 内容转义
     * <p>描述:</p>
     *
     * @param escape Boolean 转义
     * @return 本身，这样可以继续设置其他属性
     */
    public T setEscape(Boolean escape) {
        prototype.setEscape(escape);
        return (T) this;
    }

    /**
     * 是否有表头配置
     *
     * @param hasTableHead boolean 是否有表头
     * @return 本身，这样可以继续设置其他属性
     */
    public T setHasTableHead(boolean hasTableHead) {
        this.hasTableHead = hasTableHead;
        return (T) this;
    }

    /**
     * 获取属性信息，支持bean中类反射获取，或者是从Map中获取(主要支持JdbcTemplate的List&lt;Map&lt;String,Object&gt;&gt;)
     *
     * @param item Object
     * @param prop String 支持点号(.)分隔的多级属性
     * @return Object
     */
    protected static Object getProperty(Object item, String prop) {
        if (item == null || prop == null || "".equals(prop)) {
            return null;
        }
        try {
            return BeanUtil.getProperty(item, prop);
        } catch (Exception e) {
            LogUtil.error(AbstractGridWrapper.class, null, e);
            return null;
        }

    }

    @Override
    public Grid getPrototype() {
        if (!this.prototypeChanged) {
            prototype.prototypeBuilding(true);
            prototype.clearNodes();
            if (pub != null) {
                prototype.setPub(pub);
            }

            buildPrototype();
            prototype.prototypeBuilding(false);
        }
        return this.prototype;
    }

    /**
     * 取得表头
     *
     * @return Grid.Tr
     * @see #getPrototype()
     * @deprecated 该方法将会触发获取原型。请谨慎使用
     */
    public Grid.TR getHeadRow() {
        Grid.THead head = getPrototype().gettHead();
        if (head.getRows() != null && head.getRows().size() > 0) {
            return head.getRows().get(0);
        }
        return null;
    }
}
