package com.rongji.dfish.ui.auxiliary;

import com.rongji.dfish.base.util.Utils;
import com.rongji.dfish.ui.AbstractNode;
import com.rongji.dfish.ui.Alignable;
import com.rongji.dfish.ui.RawJson;
import com.rongji.dfish.ui.VAlignable;
import com.rongji.dfish.ui.form.AbstractBox;
import com.rongji.dfish.ui.form.Radio;
import com.rongji.dfish.ui.form.TripleBox;
import com.rongji.dfish.ui.form.Validate;
import com.rongji.dfish.ui.layout.Table;

import java.beans.Transient;

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
 * 可以使用 text / checkbox / hidden / radio方法快捷构建一个TableColumn
 */
public class Column extends AbstractNode<Column> implements Alignable<Column>, VAlignable<Column> {

    private static final long serialVersionUID = 3246628575622594917L;

    private String field;
    private String width;
    private String format;
    private String style;
    private String cls;
    private String align;
    private String vAlign;
    private Object tip;
    private Object sort;
    private Integer minWidth;
    private Integer maxWidth;
    private Highlight highlight;
    private String fixed;
    private RawJson rawFormat;
    private Integer labelWidth;

    public RawJson rawFormat() {
        return rawFormat;
    }


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
     * @param field String 输出时显示的JSON属性名字。注意不要有重复
     * @param width String 列宽度，可以是 数字表示多少像素 百分比 如35% 表示页面宽度 或* 自动分配
     */
    public Column(String field, String width) {
        this.field = field;
        this.width = width;
    }

    /**
     * 构建一个TEXT类型的TableColumn 在TableLayou调用的时候不自动绑定数据。所以没有绑定数据的选项和label
     * 这个一般在Table原型的时候使用。
     *
     * @param field String 输出时显示的JSON属性名字。注意不要有重复
     * @param width String 列宽度，可以是 数字表示多少像素 百分比 如35% 表示页面宽度 或* 自动分配
     * @return 本身，这样可以继续设置其他属性
     */
    public static Column text(String field, String width) {
        return new Column(field, width);
    }

    /**
     * 构建一个TEXT类型的TableColumn 在TableLayou调用的时候不自动绑定数据。所以没有绑定数据的选项和label
     *
     * @param field  String 输出时显示的JSON属性名字。注意不要有重复
     * @param width  String 列宽度，可以是 数字表示多少像素 百分比 如35% 表示页面宽度 或* 自动分配
     * @param format 文本格式转化
     * @return 本身，这样可以继续设置其他属性
     */
    public static Column text(String field, String width, String format) {
        return new Column(field, width).setFormat(format);
    }

    /**
     * 构建一个TableTriplebox类型的TableColumn
     *
     * @param checkedField 如果设定了这个字段，这个box选中状态将根据这个字段值进行设定
     * @param width        String 列宽度，可以是 数字表示多少像素 百分比 如35% 表示页面宽度 或* 自动分配
     * @return 本身，这样可以继续设置其他属性
     */
    public static Column tripleBox(String checkedField, String width) {
        return new Column(null, width).setTripleBox(BOX_NAME, checkedField, null, null);
    }


    /**
     * 将此列设置为复选框
     *
     * @param sync String 同步状态,该参数可为空,参数值详见{@link AbstractBox#SYNC_CLICK}和{@link AbstractBox#SYNC_CLICK}
     * @return 本身，这样可以继续设置其他属性
     */
    public Column setTripleBox(String sync) {
        return setTripleBox(BOX_NAME, null, null, sync);
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
    public Column setTripleBox(String boxName, String checkedField, Boolean required, String sync) {
        TripleBox tripleBox = new TripleBox(boxName, "", null, null).setSync(sync);
        if (required != null && required) {
            tripleBox.addValidate(Validate.required(true));
        }
        return setTripleBox(tripleBox, checkedField);
    }

    /**
     * 设置公共的复选框
     *
     * @param tripleBox 复选框
     * @return 本身，这样可以继续设置其他属性
     */
    public Column setTripleBox(TripleBox tripleBox) {
        return setTripleBox(tripleBox, null);
    }

    /**
     * 设置公共的复选框
     *
     * @param tripleBox    复选框
     * @param checkedField String 所选值指向的列名
     * @return 本身，这样可以继续设置其他属性
     */
    public Column setTripleBox(TripleBox tripleBox, String checkedField) {
        if (tripleBox == null) {
            throw new IllegalArgumentException("The TripleBox can not be null.");
        }
        String boxField = Utils.notEmpty(checkedField) ? checkedField : this.field;
        if (Utils.isEmpty(boxField)) {
            throw new IllegalArgumentException("The checkedField can not be null.");
        }
        if (Utils.isEmpty(tripleBox.getName())) {
            tripleBox.setName(BOX_NAME);
        }
        // 标题全选框
        this.rawFormat = new RawJson(tripleBox.setCheckAll(true).toString());
        // 置空,省流量
        tripleBox.setCheckAll(null);
        tripleBox.setValue(new RawJson("$" + boxField));
        return this.setFormat("javascript:return " + tripleBox).setAlign(Column.ALIGN_CENTER);
    }


    /**
     * 将此列设置为单选框
     *
     * @param sync String 同步状态,该参数可为空,参数值详见{@link AbstractBox#SYNC_CLICK}和{@link AbstractBox#SYNC_CLICK}
     * @return 本身，这样可以继续设置其他属性
     */
    public Column setRadio(String sync) {
        return setRadio(BOX_NAME, null, null, sync);
    }


    /**
     * 将此列设置为单选框
     *
     * @param boxName      String 单选框名称
     * @param checkedField String 所选值指向的列名
     * @param required     Boolean 是否必填
     * @param sync         String 是否和行点击动作同步
     * @return 本身，这样可以继续设置其他属性
     */
    public Column setRadio(String boxName, String checkedField, Boolean required, String sync) {
        Radio radio = new Radio(boxName, "", null, null).setSync(sync);
        if (required != null && required) {
            radio.addValidate(Validate.required(true));
        }

        return setRadio(radio, checkedField);
    }

    /**
     * 将此列设置为单选框
     *
     * @param radio TableRadio 单选框
     * @return 本身，这样可以继续设置其他属性
     */
    public Column setRadio(Radio radio) {
        return setRadio(radio, null);
    }

    /**
     * 将此列设置为单选框
     *
     * @param radio        TableRadio 单选框
     * @param checkedField String 所选值指向的列名
     * @return 本身，这样可以继续设置其他属性
     */
    public Column setRadio(Radio radio, String checkedField) {
        if (radio == null) {
            throw new IllegalArgumentException("The Radio can not be null.");
        }
        String boxField = Utils.notEmpty(checkedField) ? checkedField : this.field;
        if (Utils.isEmpty(boxField)) {
            throw new IllegalArgumentException("The checkedField can not be null.");
        }
        if (Utils.isEmpty(radio.getName())) {
            radio.setName(BOX_NAME);
        }
        return this.setFormat("javascript:return " + radio.setValue(new RawJson("$" + boxField))).setAlign(Column.ALIGN_CENTER);
    }


    /**
     * 构建一个CHECKBOX类型的TableColumn
     *
     * @param checkedField String 所选中值指定的列名
     * @param width        String 列宽度，可以是 数字表示多少像素 百分比 如35% 表示页面宽度 或* 自动分配
     * @return 本身，这样可以继续设置其他属性
     */
    public static Column radio(String checkedField, String width) {
        return radio(checkedField, width, null);
    }

    /**
     * 构建一个CHECKBOX类型的TableColumn
     *
     * @param checkedField String 所选中值指定的列名
     * @param width        String 列宽度，可以是 数字表示多少像素 百分比 如35% 表示页面宽度 或* 自动分配
     * @param sync         是否与行点击动作同步
     * @return 本身，这样可以继续设置其他属性
     */
    public static Column radio(String checkedField, String width, String sync) {
        return new Column(null, width).setRadio(BOX_NAME, checkedField, false, sync);
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
     * 排序状态。设置此参数表示当前列可点击排序。
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
    public Column setSort(ColumnSort sort) {
        this.sort = sort;
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
        return vAlign;
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
        this.vAlign = vAlign;
        return this;
    }


    /**
     * 是否可见的。如果是false表示当前TableColumn是隐藏字段，不输出给javascript前端。仅仅用于JAVA后端用于方便获取属性用，起到方便开发的作用
     *
     * @return boolean
     */
    @Transient
    public boolean isVisible() {
        return this.width != null && !"".equals(this.width);
    }

//        /**
//         * 选项表单，类型是 checkbox 或 radio。
//         *
//         * @param box AbstractBox
//         * @return 本身，这样可以继续设置其他属性
//         */
//        public Column setBox(AbstractBox box) {
//            if (box == null) {
//                this.setFormat("");
//                return this;
//            }
//            Boolean required = null;
//            if (box.getValidate() != null && box.getValidate().getRequired() != null) {
//                required = box.getValidate().getRequired().getValue();
//            }
//            if (box instanceof com.rongji.dfish.ui.form.TripleBox || box instanceof CheckBox) {
//                setTripleBox(box.getName(), null, required, box.getSync());
//            } else if (box instanceof com.rongji.dfish.ui.form.Radio) {
//                setRadio(box.getName(), null, required, box.getSync());
//            } else {
//                throw new IllegalArgumentException("The box must be TripleBox or Radio");
//            }
//            return this;
//        }


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
    public Column setTip(ColumnTip tip) {
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
    public Integer getMinWidth() {
        return minWidth;
    }

    /**
     * 列的最小宽度
     *
     * @param minWidth Integer
     * @return 本身，这样可以继续设置其他属性
     */
    public Column setMinWidth(Integer minWidth) {
        this.minWidth = minWidth;
        return this;
    }

    /**
     * 列的最大宽度
     *
     * @return Integer
     */
    public Integer getMaxWidth() {
        return maxWidth;
    }

    /**
     * 列的最大宽度
     *
     * @param maxWidth Integer
     * @return 本身，这样可以继续设置其他属性
     */
    public Column setMaxWidth(Integer maxWidth) {
        this.maxWidth = maxWidth;
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
     * @return 本身，这样可以继续设置其他属性
     */
    public Column setFixed(String fixed) {
        this.fixed = fixed;
        return this;
    }

    /**
     * 表单标题宽度
     * @return Integer
     */
    public Integer getLabelWidth() {
        return labelWidth;
    }

    /**
     * 表单标题宽度
     * @param labelWidth Integer
     * @return 本身，这样可以继续设置其他属性
     */
    public Column setLabelWidth(Integer labelWidth) {
        this.labelWidth = labelWidth;
        return this;
    }
}
