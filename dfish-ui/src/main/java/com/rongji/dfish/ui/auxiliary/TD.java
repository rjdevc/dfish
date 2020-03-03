package com.rongji.dfish.ui.auxiliary;

import com.rongji.dfish.ui.*;
import com.rongji.dfish.ui.widget.Html;

import java.util.Arrays;
import java.util.List;

/**
 * Td 表示一个Table的单元格
 * <p>在一些复杂布局结构中，它可以占不止一行或不止一列</p>
 * <p>TableCell 有两种工作模式，他内部可以包含一个Widget或简单的包含一个文本，如果包含了widget文本模式将失效</p>
 * <p>虽然TableCell也是一个Widget，但其很可能并不会专门设置ID。</p>
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
public class TD extends AbstractTD<TD> implements JsonWrapper<Object> {
    /**
     *
     */
    private static final long serialVersionUID = 4639610865052336483L;

    /**
     * 默认构造函数
     */
    public TD() {
        super();
    }

    /**
     * 拷贝构造函数 相当于clone
     *
     * @param td AbstractTd
     */
    public TD(AbstractTD<?> td) {
        super();
        copyProperties(this, td);
    }


    @Override
    public Object getPrototype() {
        if (hasTdProp(this)) {
            JsonTD p = new JsonTD();
            copyProperties(p, this);
            if (isTextWidget(getNode())) {
                //把文本当做cell的text
                String text = getTextValue(getNode());
                p.setText(text);
                p.setNode(null);
                return p;
            } else {
                p.setNode(getNode());
                return p;
            }
        } else {
            Widget<?> w = getNode();
            if (isTextWidget(getNode())) {
                String text = getTextValue(getNode());
                return text;
            } else {
                return w;
            }
        }
    }

    private static boolean hasTdProp(AbstractTD<?> td) {
        return td.getId() != null || td.getHeight() != null ||
                td.getAlign() != null || td.getVAlign() != null ||
                td.getColSpan() != null || td.getRowSpan() != null ||
                td.getCls() != null || td.getStyle() != null ||//常用的属性排在前面
                td.getBeforeContent() != null || td.getPrependContent() != null ||
                td.getAppendContent() != null || td.getAfterContent() != null ||
                td.getGid() != null || td.getHeightMinus() != null ||
                td.getMaxHeight() != null || td.getMaxWidth() != null ||
                td.getMinHeight() != null || td.getMinWidth() != null ||
                (td.getOn() != null && td.getOn().size() > 0) ||
                td.getWidth() != null || td.getWidthMinus() != null
                || td.getLabelWidth() != null;
    }

    /**
     * 取得html的内容。为了效率这个方法不再进行判断，所以只能跟在isTextWidget 后使用。
     *
     * @param node Widget
     * @return String
     */
    private String getTextValue(Widget<?> node) {
        Object prototype = node;
        while (prototype instanceof JsonWrapper) {
            prototype = ((JsonWrapper<?>) prototype).getPrototype();
        }
        Html cast = (Html) prototype;
        return cast.getText();
    }

    /**
     * 是不是内容里只有Text部分是有效信息，如果是的话。这里要简化输出json
     *
     * @param node
     * @return 是否文本组件
     */
    private static boolean isTextWidget(Widget<?> node) {
        if (node == null) {
            return false;
        }
        Object prototype = node;
        while (prototype instanceof JsonWrapper) {
            prototype = ((JsonWrapper<?>) prototype).getPrototype();
        }
        if (!(prototype instanceof Html)) {
            return false;
        }
        Html html = (Html) prototype;
        return html.getId() == null && html.getHeight() == null &&
                html.getWidth() == null && html.getEscape() == null &&
                html.getAlign() == null && html.getVAlign() == null &&
                html.getCls() == null && html.getStyle() == null &&//常用的属性排在前面
                html.getAppendContent() == null && html.getPrependContent() == null &&
                html.getGid() == null && html.getHeightMinus() == null &&
                html.getMaxHeight() == null && html.getMaxWidth() == null &&
                html.getMinHeight() == null && html.getMinWidth() == null &&
                (html.getOn() == null || html.getOn().size() == 0) &&
                html.getScroll() == null && html.getWidthMinus() == null;
    }
}

abstract class AbstractTD<T extends AbstractTD<T>> extends AbstractWidget<T>
            implements SingleNodeContainer<T, Widget>, Alignable<T>, VAlignable<T> {

        private static final long serialVersionUID = -7870476532478876521L;


        protected Integer colSpan;
        protected Integer rowSpan;
        //	private String text;
        protected String align;
        protected String vAlign;
        protected Widget node;
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
         *
         * @return Integer
         */
        public Integer getColSpan() {
            return colSpan;
        }

        /**
         * 这个这个单元格占几列。
         * 为空的时候相当于1
         *
         * @param colSpan Integer
         * @return 本身，这样可以继续设置其他属性
         */
        public T setColSpan(Integer colSpan) {
            this.colSpan = colSpan;
            return (T) this;
        }

        /**
         * 这个这个单元格占几行。
         * 为空的时候相当于1
         *
         * @return Integer
         */
        public Integer getRowSpan() {
            return rowSpan;
        }

        /**
         * 这个这个单元格占几行。
         * 为空的时候相当于1
         *
         * @param rowSpan Integer
         * @return 本身，这样可以继续设置其他属性
         */
        public T setRowSpan(Integer rowSpan) {
            this.rowSpan = rowSpan;
            return (T) this;
        }




        @Override
        public String getVAlign() {
            return vAlign;
        }

        @Override
        public T setVAlign(String vAlign) {
            this.vAlign = vAlign;
            return (T) this;
        }

        @Override
        public String getAlign() {
            return align;
        }

        @Override
        public T setAlign(String align) {
            this.align = align;
            return (T) this;
        }

        /**
         * 拷贝属性
         *
         * @param to   AbstractTd
         * @param from AbstractTd
         */
        protected void copyProperties(AbstractTD<?> to, AbstractTD<?> from) {
            super.copyProperties(to, from);
            to.node = from.node;
            to.align = from.align;
            to.colSpan = from.colSpan;
            to.rowSpan = from.rowSpan;
            to.vAlign = from.vAlign;
            to.labelWidth = from.labelWidth;
        }

        /**
         * 用于显示文本是否需要转义,不设置默认是true
         *
         * @return Boolean
         */
        public Boolean getEscape() {
            return escape;
        }

        /**
         * 用于显示文本是否需要转义,不设置默认是true
         *
         * @param escape Boolean
         * @return 本身，这样可以继续设置其他属性
         */
        public T setEscape(Boolean escape) {
            this.escape = escape;
            return (T) this;
        }

        /**
         * 格式化内容。"$字段名"形式的变量将被解析替换。支持"javascript:"开头的js语句(需return返回值)。
         *
         * @return String 格式化内容
         */
        public String getFormat() {
            return format;
        }

        /**
         * 格式化内容。"$字段名"形式的变量将被解析替换。支持"javascript:"开头的js语句(需return返回值)。
         *
         * @param format String 格式化内容
         * @return 本身，这样可以继续设置其他属性
         */
        public T setFormat(String format) {
            this.format = format;
            return (T) this;
        }

        /**
         * 表单标题宽度。
         *
         * @return Integer
         * @since 3.3
         */
        public Integer getLabelWidth() {
            return labelWidth;
        }

        /**
         * 表单标题宽度。
         *
         * @param labelWidth Integer
         * @return 本身，这样可以继续设置其他属性
         * @since 3.3
         */
        public T setLabelWidth(Integer labelWidth) {
            this.labelWidth = labelWidth;
            return (T) this;
        }

        protected NodeContainerDecorator getNodeContainerDecorator(){
            return new NodeContainerDecorator() {
                @Override
                protected List<Node> nodes() {
                    return Arrays.asList(AbstractTD.this.node) ;
                }

                @Override
                protected void setNode(int i, Node node) {
                    assert(i==0);
                    AbstractTD.this.setNode((Widget) node);
                }
            };
        }
        @Override
        public Node findNode(Filter filter) {
            return getNodeContainerDecorator().findNode(filter);
        }

        @Override
        public List<Node> findAllNodes(Filter filter) {
            return getNodeContainerDecorator().findAllNodes(filter);
        }

        @Override
        public Node replaceNode(Filter filter, Node node) {
            return getNodeContainerDecorator().replaceNode(filter,node);
        }

        @Override
        public int replaceAllNodes(Filter filter, Node node) {
            return getNodeContainerDecorator().replaceAllNodes(filter,node);
        }
        @Override
        public T setNode(Widget node){
            this.node=(Widget)node;
            return (T)this;
        }
        @Override
        public Widget getNode(){
            return node;
        }
    }
/**
 * 和javascript端是对应的TD模型。
 * json中如果td没有cls等额外属性，可能会简化显示它的node
 * 如果这个node还是文本，可能会进一步简化显示成文本。
 * 所以Td默认不能显示按封装类格式。这时候json中的原型将有可能还是这个JsonTd的格式
 * 也有可能是Widget格式或者是text格式。
 *
 * @author DfishTeam
 */
class JsonTD extends AbstractTD<JsonTD> implements HasText<JsonTD> {
    /**
     *
     */
    private static final long serialVersionUID = -5125782398657967546L;
    private String text;

    /**
     * 文本模式时， 取得单元格内部文本的值
     *
     * @return String
     */
    @Override
    public String getText() {
        return text;
    }

    /**
     * 文本模式时， 设置单元格内部文本的值
     *
     * @param text String
     * @return 本身，这样可以继续设置其他属性
     */
    @Override
    public JsonTD setText(String text) {
        this.text = text;
        return this;
    }
}

