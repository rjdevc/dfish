package com.rongji.dfish.ui.form;

import com.rongji.dfish.ui.HasText;
import com.rongji.dfish.ui.command.Dialog;


/**
 * 选择框。
 *
 * @author DFish Team
 */
public class PickBox extends AbstractPickerBox<PickBox> implements HasText<PickBox> {

    private static final long serialVersionUID = 7493588393931087665L;

    private String text;
    private String format;
    private Boolean escape;

    /**
     * 显示文本
     *
     * @return String
     */
    @Override
    public String getText() {
        return text;
    }

    /**
     * 显示文本
     *
     * @param text 显示文本
     * @return this
     */
    @Override
    public PickBox setText(String text) {
        this.text = text;
        return this;
    }

    @Override
    public String getFormat() {
        return format;
    }

    @Override
    public PickBox setFormat(String format) {
        this.format = format;
        return this;
    }

    /**
     * 构造函数
     *
     * @param name  表单元素名
     * @param label 标题
     * @param value 值
     * @since DFish 3.0
     */
    public PickBox(String name, String label, String value) {
    	super(name, label, value);
    }

    @Override
    public PickBox setValue(Object value) {
        this.value = toString(value);
        return this;
    }

    @Override
    public PickBox setEscape(Boolean escape) {
        this.escape = escape;
        return this;
    }

    @Override
    public Boolean getEscape() {
        return escape;
    }
}
