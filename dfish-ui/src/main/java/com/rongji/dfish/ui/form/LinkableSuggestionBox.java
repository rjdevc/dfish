package com.rongji.dfish.ui.form;

import com.rongji.dfish.ui.*;
import com.rongji.dfish.ui.command.Dialog;

import java.util.List;

/**
 * LinkableSuggestionBox 默认可以通过填写出现输入提示的输入框，且所选择的选项可支持链接，主要有{@link ComboBox}和{@link LinkBox}
 *
 * @param <T> 当前对象类型
 * @author DFish Team
 */
@SuppressWarnings("unchecked")
public abstract class LinkableSuggestionBox<T extends LinkableSuggestionBox<T>> extends SuggestionBox<T> implements HasText<T>, PubNodeContainer<T, ComboOption> {
    private static final long serialVersionUID = -1444093499873660133L;
    private String text;
    private String format;
    private Boolean escape;
    private Boolean strict;
    private String loadingText;
    private List<Node> nodes;
    private ComboOption pub;

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

    @Override
    public List<Node> getNodes() {
        return nodes;
    }

    @Override
    public ComboOption pub() {
        if (pub == null) {
            pub = new ComboOption();
        }
        return pub;
    }

    @Override
    public ComboOption getPub() {
        return pub;
    }

    @Override
    public T setPub(ComboOption pub) {
        this.pub = pub;
        return (T) this;
    }

    protected AbstractNodeContainerPart containerPart=new AbstractNodeContainerPart() {
        @Override
        protected List<Node> nodes() {
            return nodes;
        }

        @Override
        protected void setNode(int i, Node node) {
            if(node==null){
                nodes.remove(i);
            }else{
                nodes.set(i,node);
            }
        }
    };
    @Override
    public Node findNode(Filter filter) {
        return containerPart.findNode(filter);
    }

    @Override
    public List<Node> findAllNodes(Filter filter) {
        return containerPart.findAllNodes(filter);
    }

    @Override
    public Node replaceNode(Filter filter, Node node) {
        return containerPart.replaceNode(filter,node);
    }

    @Override
    public int replaceAllNodes(Filter filter, Node node) {
        return containerPart.replaceAllNodes(filter,node);
    }

}
