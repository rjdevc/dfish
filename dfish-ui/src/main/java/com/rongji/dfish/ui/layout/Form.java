package com.rongji.dfish.ui.layout;

import com.rongji.dfish.ui.*;
import com.rongji.dfish.ui.auxiliary.TD;
import com.rongji.dfish.ui.form.Hidden;
import com.rongji.dfish.ui.form.Label;
import com.rongji.dfish.ui.form.LabelRow;

import java.util.ArrayList;
import java.util.List;

/**
 * FormLayout 作为通用的带表格布局的表单。默认为12 列。插入的元素可以是1-12列。
 * 可以通过设置getPub().setColspan(int)来设置默认的宽度。一般建议为4 12 或6.
 * 现在如果 添加一个 超过12 (默认情况，下同) 的列，则自动被缩减为12列。
 * 最后表单(FormLayout)上已有的最后一列，剩余的列 小于当前对象列大小。将会自动排到下一行。
 * 如果有些表单元素，rowspan大于1，也会占用当前列的大小。排不下的时候，也会自动排列到下一行。
 * <p>
 * 3.2之前，它 功能由 FormPanel FlexTable LayoutFormPanel分摊。但3.3以后，如果使用前后端分离的模式开发，
 * 则需要一个统一，方便的组件来代替，于是就封装了它。它现在对于前端是一个实际存在的类，不再是一个JsonWrapper。
 * 5.0以后建议  FormPanel FlexTable LayoutFormPanel 全部改为使用该类。
 * @author LinLW
 * @since 5.0
 */
public class Form extends AbstractPubNodeContainer<Form, Widget, TD> implements HtmlContentHolder<Form>, Scrollable<Form>, HiddenContainer<Form> {
    /**
     * 无格式
     */
    public static final String FACE_NONE = Table.FACE_NONE;

    /**
     * 用横线把表格划分成多行
     */
    public static final String FACE_LINE = Table.FACE_LINE;
    /**
     * 用横竖的线把表格划分成多个单元格
     */
    public static final String FACE_CELL = Table.FACE_CELL;
    /**
     * 用横线(虚线)把表格划分成多行
     */
    public static final String FACE_DOT = Table.FACE_DOT;
    private Integer cols;
    private Boolean escape;
    private String face;
    private Boolean br;
    private Boolean scroll;

    public Form() {
        super(null);
    }
    public Form(String id) {
        super(id);
    }

    @Override
    protected TD newPub() {
        return new TD();
    }

    /**
     * 布局表格分成多少列。默认12
     *
     * @return Integer
     */
    public Integer getCols() {
        return cols;
    }

    /**
     * 布局表格分成多少列。默认12
     *
     * @param cols Integer
     * @return this
     */
    public Form setCols(Integer cols) {
        this.cols = cols;
        return this;
    }

    public Form add(Widget w) {
        if (w == null) {
            return this;
        }
        if (w instanceof Hidden) {
            return add((Hidden) w);
        }else if (w instanceof LabelRow) {
            if (((LabelRow) w).getLabel()!=null &&"0".equals(((LabelRow<?>) w).getLabel().getWidth())) {
                ((LabelRow<?>) w).getLabel().setWidth(null);
            }
        }else if(w instanceof TD){
            if(((TD) w).getNode() instanceof LabelRow){
                LabelRow cast= (LabelRow) ((TD) w).getNode();
                if(cast.getLabel()!=null&&"0".equals(cast.getLabel().getWidth())){
                    cast.getLabel().setWidth(null);
                }
            }
        }
        if(nodes==null){
            nodes=new ArrayList<>();
        }
        nodes.add(w);
        return this;
    }

    /**
     * 添加子元素，并且制定宽度。
     * 相当于add(new Table.Td().setColspan(colspan).setNode(w))
     *
     * @param w       元素
     * @param colspan 占用宽度
     * @return this
     */
    public Form add(Widget<?> w, int colspan) {
        if (w instanceof LabelRow) {
            if (((LabelRow) w).getNoLabel()!=null &&"0".equals(((LabelRow<?>) w).getLabel().getWidth())) {
                ((LabelRow<?>) w).getLabel().setWidth(null);
            }
        }
        return add(new TD().setColSpan(colspan).setNode(w));
    }

    /**
     * 添加子元素，并且制定宽度。
     * 相当于add(new Table.Td().setColspan(colspan).setRowspan(rowspan).setNode(w))
     *
     * @param w       元素
     * @param colspan 占用宽度
     * @param rowspan 占用高度
     * @return
     */
    public Form add(Widget<?> w, int colspan, int rowspan) {
        if (w instanceof LabelRow) {
            if (((LabelRow) w).getNoLabel()!=null && "0".equals(((LabelRow<?>) w).getLabel().getWidth())) {
                ((LabelRow<?>) w).getLabel().setWidth(null);
            }
        }
        return add(new TD().setColSpan(colspan).setRowSpan(rowspan).setNode(w));
    }

    private HiddenPart hiddens = new HiddenPart();

    @Override
    public Form add(Hidden hidden) {
        hiddens.add(hidden);
        return this;
    }

    @Override
    public Form addHidden(String name, String value) {
        hiddens.addHidden(name, value);
        return this;
    }

    @Override
    public List<Hidden> getHiddens() {
        return hiddens.getHiddens();
    }

    @Override
    public List<String> getHiddenValue(String name) {
        return hiddens.getHiddenValue(name);
    }

    @Override
    public Form removeHidden(String name) {
        hiddens.removeHidden(name);
        return this;
    }


    @Override
    public Boolean getEscape() {
        return this.escape;
    }

    @Override
    public Form setEscape(Boolean escape) {
        this.escape = escape;
        return this;
    }

    /**
     * 内容不换行。
     *
     * @return Boolean
     */
    public Boolean getBr() {
        return br;
    }

    /**
     * 内容过多的时候不会换行，而是隐藏不显示
     *
     * @param br Boolean
     * @return 本身，这样可以继续设置其他属性
     */
    public Form setBr(Boolean br) {
        this.br = br;
        return this;
    }

//		@Override
//		public JsonTableLayout add(Tr w) {
//			throw new UnsupportedOperationException("not support this method");
//	    }

    @Override
    public Boolean getScroll() {
        return scroll;
    }

    @Override
    public Form setScroll(Boolean scroll) {
        this.scroll = scroll;
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
    public Form setFace(String face) {
        this.face = face;
        return this;
    }

}
