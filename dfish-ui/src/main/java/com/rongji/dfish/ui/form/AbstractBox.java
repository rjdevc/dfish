package com.rongji.dfish.ui.form;

import com.rongji.dfish.ui.HasText;
import com.rongji.dfish.ui.HtmlContentHolder;
import com.rongji.dfish.ui.TargetHolder;
import com.rongji.dfish.ui.Widget;

/**
 * 单选框或多选框，他们经常作为复杂对象里面可以被选择的项。
 * 比如说grid每行可以有一个 {@link CheckBox} 或  {@link Radio}
 *
 * @param <T> 当前对象类型
 * @author DFish Team
 * @version 3.1 lamontYu 所有属性和type按照驼峰命名方式调整
 *
 * @since DFish1.0
 */
public abstract class AbstractBox<T extends AbstractBox<T>> extends AbstractFormElement<T, Object>
        implements HtmlContentHolder<T>, HasText<T>, TargetHolder<T> {

    private static final long serialVersionUID = -5120066286869690681L;

    protected Boolean br;
    protected Boolean bubble;
    protected Boolean checked;
    protected Boolean escape;
    protected String format;
    protected String sync;
    protected Widget target;
    protected String text;
//    protected BoxField field;

    public static final String SYNC_CLICK = "click";
    public static final String SYNC_FOCUS = "focus";

    /**
     * 构造函数
     *
     * @param name    表单元素名
     * @param label   标题
     * @param value   如果选中提交的值
     * @param text    显示的文本
     */
    public AbstractBox(String name, String label, Object value, String text) {
        this.setName(name);
        this.setValue(value);
        this.setLabel(label);
        this.setText(text);
    }
    /**
     * 构造函数
     *
     * @param name    表单元素名
     * @param label   标题
     * @param value   如果选中提交的值
     * @param text    显示的文本
     */
    public AbstractBox(String name, Label label, Object value, String text) {
        this.setName(name);
        this.setValue(value);
        this.setLabel(label);
        this.setText(text);
    }

    /**
     * 绑定 widget，同步 disabled 属性。
     *
     * @return target
     */
    @Override
    public Widget getTarget() {
        return target;
    }

    /**
     * 绑定 widget，同步 disabled 属性。
     *
     * @param target widget
     * @return 本身，这样可以继续设置其他属性
     */
    @Override
    public T setTarget(Widget target) {
        this.target = target;
        return (T) this;
    }

    @Override
    public String getText() {
        return text;
    }

    @Override
    public T setText(String text) {
        this.text = text;
        return (T) this;
    }

    /**
     * 是否默认选中。
     *
     * @return checked
     */
    public Boolean getChecked() {
        return checked;
    }

    /**
     * 是否默认选中。
     *
     * @param checked Boolean
     * @return 本身，这样可以继续设置其他属性
     */
    public T setChecked(Boolean checked) {
        this.checked = checked;
        return (T) this;
    }

//    /**
//     * 设定该box绑定字段
//     *
//     * @return BoxField
//     */
//    public BoxField getField() {
//        return field;
//    }
//
//    /**
//     * 该box绑定字段
//     *
//     * @param field BoxField
//     * @return this
//     */
//
//    public T setField(BoxField field) {
//        this.field = field;
//        return (T) this;
//    }

    @Override
    public Boolean getEscape() {
        return this.escape;
    }

    @Override
    public T setEscape(Boolean escape) {
        this.escape = escape;
        return (T) this;
    }

    @Override
    public String getFormat() {
        return format;
    }

    @Override
    public T setFormat(String format) {
        this.format = format;
        return (T) this;
    }

    @Override
    public Object getTip() {
        return tip;
    }

    /**
     * 选中状态跟父节点保持同步
     *
     * @return String
     */
    public String getSync() {
        return sync;
    }

    /**
     * 设置选中状态跟父节点保持同步
     *
     * @param sync Boolean
     * @return this
     */
    public T setSync(String sync) {
        this.sync = sync;
        return (T) this;
    }

    /**
     * 点击事件是否冒泡
     *
     * @return Boolean
     */
    public Boolean getBubble() {
        return bubble;
    }

    /**
     * 设置点击事件是否冒泡
     *
     * @param bubble Boolean
     * @return this
     */
    public T setBubble(Boolean bubble) {
        this.bubble = bubble;
        return (T) this;
    }

    @Override
    public T setValue(Object value) {
        this.value = value;
        return (T) this;
    }

    /**
     * 该选项不换行
     *
     * @return Boolean
     */
    public Boolean getBr() {
        return br;
    }

    /**
     * 该选项不换行
     *
     * @param br Boolean
     * @return this
     */
    public T setBr(Boolean br) {
        this.br = br;
        return (T) this;
    }
}

