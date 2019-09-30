package com.rongji.dfish.ui.layout;

import com.rongji.dfish.ui.*;
import com.rongji.dfish.ui.form.Hidden;
import com.rongji.dfish.ui.form.LabelRow;
import com.rongji.dfish.ui.layout.grid.Td;

import java.util.Iterator;
import java.util.List;

/**
 * FormLayout 作为通用的带表格布局的表单。默认为12 列。插入的元素可以是1-12列。
 * 可以通过设置getPub().setColspan(int)来设置默认的宽度。一般建议为4 12 或6.
 * 现在如果 添加一个 超过12 (默认情况，下同) 的列，则自动被缩减为12列。
 * 最后表单(FormLayout)上已有的最后一列，剩余的列 小于当前对象列大小。将会自动排到下一行。
 * 如果有些表单元素，rowspan大于1，也会占用当前列的大小。排不下的时候，也会自动排列到下一行。
 *
 * 3.2之前，它 功能由 FormPanel FlexGrid LayoutFormPanel分摊。但3.3以后，如果使用前后端分离的模式开发，
 * 则需要一个统一，方便的组件来代替，于是就封装了它。它现在对于前端是一个实际存在的类，不再是一个JsonWrapper。
 * 3.3以后建议  FormPanel FlexGrid LayoutFormPanel 全部改为使用该类。
 * @since 3.3
 *
 */
public class FormLayout extends AbstractLayout<FormLayout,Widget<?>>
        implements HtmlContentHolder<FormLayout>, Scrollable<FormLayout>,MultiContainer<FormLayout,Widget<?>> ,
        PubHolder<FormLayout,Td> ,HiddenContainer<FormLayout>{
    /**
     * 无格式
     */
    public static final String FACE_NONE=GridLayout.FACE_NONE;

    /**
     * 用横线把表格划分成多行
     */
    public static final String FACE_LINE=GridLayout.FACE_LINE;
    /**
     * 用横竖的线把表格划分成多个单元格
     */
    public static final String FACE_CELL=GridLayout.FACE_CELL;
    /**
     * 用横线(虚线)把表格划分成多行
     */
    public static final String FACE_DOT=GridLayout.FACE_DOT;
    private Integer cols;
    private Td pub;
    private Boolean escape;
    private String face;
    private Boolean nobr;
    private Boolean scroll;
    private String scrollClass;

    /**
     * 布局表格分成多少列。默认12
     * @return Integer
     */
    public Integer getCols() {
        return cols;
    }

    /**
     * 布局表格分成多少列。默认12
     * @param cols Integer
     * @return this
     */
    public FormLayout setCols(Integer cols) {
        this.cols = cols;
        return this;
    }

    public Td getPub() {
        if (pub == null) {
            setPub(new Td());
        }
        return pub;
    }
    public FormLayout setPub(Td pub) {
        this.pub = pub;
        return this;
    }

    /**
     * 构造函数
     *
     * @param id String
     */
    public FormLayout(String id) {
        super(id);
    }

    @Override
    public String getType() {
        return "form";
    }

    public FormLayout add(Widget<?>w ){
        if (w == null) {
            return this;
        }
        if(w instanceof Hidden){
            return add((Hidden)w);
        }
        if (w instanceof LabelRow) {
            if("0".equals(((LabelRow<?>) w).getLabel().getWidth())){
                ((LabelRow<?>) w).getLabel().setWidth(null);
            }
        }
        nodes.add(w);
        return this;
    }

    /**
     * 添加子元素，并且制定宽度。
     * 相当于add(new Td().setColspan(colspan).setNode(w))
     * @param w 元素
     * @param colspan 占用宽度
     * @return this
     */
    public FormLayout add(Widget<?>w ,int colspan){
        if (w instanceof LabelRow) {
            if("0".equals(((LabelRow<?>) w).getLabel().getWidth())){
                ((LabelRow<?>) w).getLabel().setWidth(null);
            }
        }
        return add (new Td().setColspan(colspan).setNode(w));
    }

    /**
     * 添加子元素，并且制定宽度。
     * 相当于add(new Td().setColspan(colspan).setRowspan(rowspan).setNode(w))
     * @param w 元素
     * @param colspan 占用宽度
     * @param rowspan 占用高度
     * @return
     */
    public FormLayout add(Widget<?>w ,int colspan,int rowspan){
        if (w instanceof LabelRow) {
            if("0".equals(((LabelRow<?>) w).getLabel().getWidth())){
                ((LabelRow<?>) w).getLabel().setWidth(null);
            }
        }
        return add (new Td().setColspan(colspan).setRowspan(rowspan).setNode(w));
    }
//    public FormLayout addLabelRow(LabelRow<?> w){
//        return add(w);
//    }
    private HiddenPart hiddens = new HiddenPart();

    public FormLayout add(Hidden hidden) {
        hiddens.add(hidden);
        return this;
    }
    public FormLayout addHidden(String name,String value) {
        hiddens.addHidden(name, value);
        return this;
    }

    public List<Hidden> getHiddens() {
        return hiddens.getHiddens();
    }

    public List<String> getHiddenValue(String name) {
        return hiddens.getHiddenValue(name);
    }

    public FormLayout removeHidden(String name) {
        hiddens.removeHidden(name);
        return this;
    }


    @Override
    public Boolean getEscape() {
        return this.escape;
    }

    @Override
    public FormLayout setEscape(Boolean escape) {
        this.escape=escape;
        return this;
    }
    /**
     * 内容不换行。
     * @return nobr
     */
    public Boolean getNobr() {
        return nobr;
    }

    /**
     * 内容过多的时候不会换行，而是隐藏不显示
     * @param nobr Boolean
     * @return 本身，这样可以继续设置其他属性
     */
    public FormLayout setNobr(Boolean nobr) {
        this.nobr = nobr;
        return this;
    }

//		@Override
//		public JsonGridLayout add(Tr w) {
//			throw new UnsupportedOperationException("not support this method");
//	    }

    public Boolean getScroll() {
        return scroll;
    }
    public FormLayout setScroll(Boolean scroll) {
        this.scroll = scroll;
        return this;
    }
    public String getScrollClass() {
        return scrollClass;
    }
    public FormLayout setScrollClass(String scrollClass) {
        this.scrollClass = scrollClass;
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
    public FormLayout setFace(String face) {
        this.face = face;
        return this;
    }

    @Override
    public FormLayout removeNodeById(String id){
       //如果remove的结果是，td的下一层是指定的元素，需要吧td删除
        for (Iterator<Widget<?>> iter = nodes.iterator();
             iter.hasNext(); ) {
            Widget<?> item = iter.next();
            if (id.equals(item.getId())||(item instanceof Td && id.equals(((Td)item).getNode().getId()))) {
                iter.remove();
            } else if (item instanceof Layout) {
                Layout<?, Widget<?>> cast = (Layout<?, Widget<?>>) item;
                cast.removeNodeById(id);
            }
        }


        return this;
    }

    @Override
    public List<Widget<?>> getNodes() {
        return nodes;
    }
}
