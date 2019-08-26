package com.rongji.dfish.ui.form;

/**
 * 开关选项
 * @author DFish Team
 * @since dfish 3.1
 */
public class Switch extends AbstractFormElement<Switch, Object> {

    private Boolean checked;
    private String checkedtext;
    private String sync;
    private String target;
    private String uncheckedtext;
    private Boolean escape;
    private String format;

    /**
     * 构造函数(默认提交值是"1")
     * @param name 表单名
     * @param label 标题
     * @param checked 选中
     */
    public Switch(String name, String label, Boolean checked) {
        this(name, label, checked, "1");
    }

    /**
     * 构造函数
     * @param name 表单名
     * @param label 标题
     * @param checked 选中
     * @param value 如果选中的提交的值
     */
    public Switch(String name, String label, Boolean checked, Object value) {
        this.setName(name);
        this.setLabel(label);
        this.setChecked(checked);
        this.setValue(value);
    }

    public static final String SYNC_CLICK = "click";
    public static final String SYNC_FOCUS = "focus";

    @Override
    public Switch setValue(Object value) {
        this.value = value;
        return this;
    }

    @Override
    public String getType() {
        return "switch";
    }

    /**
     * 是否选中
     * @return Boolean
     */
    public Boolean getChecked() {
        return checked;
    }

    /**
     * 是否选中
     * @param checked Boolean
     * @return 本身，这样可以继续设置其他属性
     */
    public Switch setChecked(Boolean checked) {
        this.checked = checked;
        return this;
    }

    /**
     * 选中状态时的文本
     * @return String
     */
    public String getCheckedtext() {
        return checkedtext;
    }

    /**
     * 选中状态时的文本
     * @param checkedtext String
     * @return 本身，这样可以继续设置其他属性
     */
    public Switch setCheckedtext(String checkedtext) {
        this.checkedtext = checkedtext;
        return this;
    }

    /**
     * 选中状态跟父节点同步，用于 leaf 或 tr 的选项box。
     * 可选值: {@link #SYNC_CLICK}(点击父节点，box也触发点击), {@link #SYNC_FOCUS}(父节点聚焦则box则选中，父节点失去焦点则box未选中)
     * @return String
     */
    public String getSync() {
        return sync;
    }

    /**
     * 选中状态跟父节点同步，用于 leaf 或 tr 的选项box。
     * 可选值: {@link #SYNC_CLICK}(点击父节点，box也触发点击), {@link #SYNC_FOCUS}(父节点聚焦则box则选中，父节点失去焦点则box未选中)
     * @param sync String
     * @return 本身，这样可以继续设置其他属性
     */
    public Switch setSync(String sync) {
        this.sync = sync;
        return this;
    }

    /**
     * widget ID。使这个 widget 和当前 option 的 disabled 状态同步。
     * @return String
     */
    public String getTarget() {
        return target;
    }

    /**
     * widget ID。使这个 widget 和当前 option 的 disabled 状态同步。
     * @param target String
     * @return 本身，这样可以继续设置其他属性
     */
    public Switch setTarget(String target) {
        this.target = target;
        return this;
    }

    /**
     * 未选中状态时的文本。
     * @return String
     */
    public String getUncheckedtext() {
        return uncheckedtext;
    }

    /**
     * 未选中状态时的文本。
     * @param uncheckedtext String
     * @return 本身，这样可以继续设置其他属性
     */
    public Switch setUncheckedtext(String uncheckedtext) {
        this.uncheckedtext = uncheckedtext;
        return this;
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
    public Switch setEscape(Boolean escape) {
        this.escape = escape;
        return this;
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
    public Switch setFormat(String format) {
        this.format = format;
        return this;
    }
}
