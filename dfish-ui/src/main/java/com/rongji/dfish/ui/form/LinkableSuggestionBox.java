package com.rongji.dfish.ui.form;

import com.rongji.dfish.ui.HasText;
import com.rongji.dfish.ui.PubHolder;
import com.rongji.dfish.ui.command.Dialog;

/**
 * LinkableSuggestionBox 默认可以通过填写出现输入提示的输入框，且所选择的选项可支持链接，主要有{@link ComboBox}和{@link LinkBox}
 *
 * @param <T> 当前对象类型
 * @author DFish Team
 */
@SuppressWarnings("unchecked")
public abstract class LinkableSuggestionBox<T extends LinkableSuggestionBox<T>> extends SuggestionBox<T> implements HasText<T>, PubHolder<T, ComboBoxOption> {

    /**
     *
     */
    private static final long serialVersionUID = -1444093499873660133L;
    private String text;
    private String format;
    private Boolean escape;
    private Boolean strict;
    private ComboBoxOption pub;
    private String loadingText;

    /**
     * 构造函数
     *
     * @param name    表单名
     * @param label   标题
     * @param value   初始值
     * @param suggest 候选项的URL
     */
    public LinkableSuggestionBox(String name, String label, String value, String suggest) {
        super(name, label, value, suggest);
    }

    /**
     * 构造函数
     *
     * @param name    表单名
     * @param label   标题
     * @param value   初始值
     * @param suggest 候选项的弹窗命令
     */
    public LinkableSuggestionBox(String name, String label, String value, Dialog suggest) {
        super(name, label, value, suggest);
    }

    @Override
    public T setText(String text) {
        this.text = text;
        return (T) this;
    }

    @Override
    public String getText() {
        return text;
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
    public T setEscape(Boolean escape) {
        this.escape = escape;
        return (T) this;
    }

    @Override
    public Boolean getEscape() {
        return escape;
    }

    /**
     * 设置是否严格检查输入值必须有效。如果值为真则输入置必须在提示信息那有才能提交。 如果值为假输入值也当做值来提交
     *
     * @param strict boolean
     * @return 本身，这样可以继续设置其他属性
     */
    public T setStrict(Boolean strict) {
        this.strict = strict;
        return (T) this;
    }

    /**
     * 设置是否严格检查输入值必须有效。如果值为真则输入置必须在提示信息那有才能提交。 如果值为假输入值也当做值来提交
     *
     * @return the strict
     */
    public Boolean getStrict() {
        return strict;
    }

    @Override
    public ComboBoxOption getPub() {
        if (pub == null) {
            pub = new ComboBoxOption();
        }
        return pub;
    }


    @Override
    public T setPub(ComboBoxOption pub) {
        this.pub = pub;
        return (T) this;
    }

    /**
     * 加载时显示的文本
     *
     * @return String
     */
    public String getLoadingText() {
        return loadingText;
    }

    /**
     * 加载时显示的文本
     *
     * @param loadingText 加载时显示的文本
     * @return this
     */
    public T setLoadingText(String loadingText) {
        this.loadingText = loadingText;
        return (T) this;
    }

}
