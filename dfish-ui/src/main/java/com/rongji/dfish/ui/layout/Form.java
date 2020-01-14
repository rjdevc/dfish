package com.rongji.dfish.ui.layout;

import com.rongji.dfish.ui.*;
import com.rongji.dfish.ui.form.Hidden;
import com.rongji.dfish.ui.form.LabelRow;

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
public class Form extends AbstractContainer<Form>
        implements HtmlContentHolder<Form>, Scrollable<Form>,MultiContainer<Form,Widget<?>> ,
        PubHolder<Form,Grid.TD> ,HiddenContainer<Form>{
    /**
     * 无格式
     */
    public static final String FACE_NONE=Grid.FACE_NONE;

    /**
     * 用横线把表格划分成多行
     */
    public static final String FACE_LINE=Grid.FACE_LINE;
    /**
     * 用横竖的线把表格划分成多个单元格
     */
    public static final String FACE_CELL=Grid.FACE_CELL;
    /**
     * 用横线(虚线)把表格划分成多行
     */
    public static final String FACE_DOT=Grid.FACE_DOT;
    private Integer cols;
    private Grid.TD pub;
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
    public Form setCols(Integer cols) {
        this.cols = cols;
        return this;
    }

    @Override
    public Grid.TD getPub() {
        if (pub == null) {
            setPub(new Grid.TD());
        }
        return pub;
    }
    @Override
    public Form setPub(Grid.TD pub) {
        this.pub = pub;
        return this;
    }

    /**
     * 构造函数
     *
     * @param id String
     */
    public Form(String id) {
        super(id);
    }

    @Override
    public String getType() {
        return "form";
    }

    @Override
    public Form add(HasId w){
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
     * 相当于add(new Grid.Td().setColspan(colspan).setNode(w))
     * @param w 元素
     * @param colspan 占用宽度
     * @return this
     */
    public Form add(Widget<?>w , int colspan){
        if (w instanceof LabelRow) {
            if("0".equals(((LabelRow<?>) w).getLabel().getWidth())){
                ((LabelRow<?>) w).getLabel().setWidth(null);
            }
        }
        return add (new Grid.TD().setColspan(colspan).setNode(w));
    }

    /**
     * 添加子元素，并且制定宽度。
     * 相当于add(new Grid.Td().setColspan(colspan).setRowspan(rowspan).setNode(w))
     * @param w 元素
     * @param colspan 占用宽度
     * @param rowspan 占用高度
     * @return
     */
    public Form add(Widget<?>w , int colspan, int rowspan){
        if (w instanceof LabelRow) {
            if("0".equals(((LabelRow<?>) w).getLabel().getWidth())){
                ((LabelRow<?>) w).getLabel().setWidth(null);
            }
        }
        return add (new Grid.TD().setColspan(colspan).setRowspan(rowspan).setNode(w));
    }
//    public FormLayout addLabelRow(LabelRow<?> w){
//        return add(w);
//    }
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
    public Form setNobr(Boolean nobr) {
        this.nobr = nobr;
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
    public Form setScroll(Boolean scroll) {
        this.scroll = scroll;
        return this;
    }
    @Override
    public String getScrollClass() {
        return scrollClass;
    }
    @Override
    public Form setScrollClass(String scrollClass) {
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
    public Form setFace(String face) {
        this.face = face;
        return this;
    }

    @Override
    public Form removeNodeById(String id){
       //如果remove的结果是，td的下一层是指定的元素，需要吧td删除
        for (Iterator<HasId<?>> iter = nodes.iterator();
             iter.hasNext(); ) {
            HasId<?> item = iter.next();
            if (id.equals(item.getId())||(item instanceof Grid.TD && id.equals(((Grid.TD)item).getNode().getId()))) {
                iter.remove();
            } else if (item instanceof Layout) {
                Layout<?> cast = (Layout<?>) item;
                cast.removeNodeById(id);
            }
        }


        return this;
    }

    @Override
    public List<Widget<?>> getNodes() {
        return (List)nodes;
    }
}
