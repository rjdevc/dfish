package com.rongji.dfish.ui.form;

import com.rongji.dfish.ui.*;
/**
 * 表单容器。默认横向排列。
 */
public class FormLabel extends AbstractWidget<FormLabel> implements Scrollable<FormLabel>,HtmlContentHolder<FormLabel>,Alignable<FormLabel>, VAlignable<FormLabel>,HasText<FormLabel>,LabelRow<FormLabel> {
    private String align;
    private String vAlign;
    private Object label;
    private Boolean noLabel;
    private String text;
    private Boolean scroll;
    private Boolean escape;
    private String format;
    public FormLabel( String label, String text){
        this.setLabel(label);
        this.setText(text);
    }

    public FormLabel( Label label, String text){
        this.setLabel(label);
        this.setText(text);
    }

    @Override
    public String getAlign() {
        return align;
    }

    @Override
    public FormLabel setAlign(String align) {
        this.align=align;
        return this;
    }

    @Override
    public FormLabel setText(String text) {
        this.text=text;
        return this;
    }

    @Override
    public String getText() {
        return this.text;
    }

    @Override
    public FormLabel setFormat(String format) {
        this.format=format;
        return this;
    }

    @Override
    public String getFormat() {
        return format;
    }

    @Override
    public Boolean getEscape() {
        return escape;
    }

    @Override
    public FormLabel setEscape(Boolean escape) {
        this.escape=escape;
        return this;
    }

    @Override
    public FormLabel setScroll(Boolean scroll) {
        this.scroll=scroll;
        return this;
    }

    @Override
    public Boolean getScroll() {
        return scroll;
    }

    @Override
    public String getVAlign() {
        return vAlign;
    }

    @Override
    public FormLabel setVAlign(String vAlign) {
        this.vAlign=vAlign;
        return this;
    }

    @Override
    public FormLabel setLabel(Label label) {
        this.label=label;
        return this;
    }
    @Override
    public FormLabel setLabel(String label) {
        this.label=label;
        return this;
    }

    @Override
    public Object getLabel() {
        return label;
    }

    @Override
    public FormLabel setNoLabel(Boolean noLabel) {
        this.noLabel=noLabel;
        return this;
    }

    @Override
    public Boolean getNoLabel() {
        return noLabel;
    }
}
