package com.rongji.dfish.ui.form;

/**
 * checkbox的基础上扩展半选中状态,tripleBox组件去掉checked属性，以value为准
 *
 * @author DFish Team
 * @version 1.0
 * @since XMLTMPL 2.1
 */
public class TripleBox extends AbstractBox<TripleBox> {
    /**
     *
     */
    private static final long serialVersionUID = -5234746180880441591L;

    private Boolean partialSubmit;
    private Boolean checkAll;

    /**
     * 半选状态是否提交数据
     *
     * @return Boolean
     */
    public Boolean getPartialSubmit() {
        return partialSubmit;
    }

    /**
     * 半选状态是否提交数据
     *
     * @param partialSubmit Boolean
     * @return 本身，这样可以继续设置其他属性
     */
    public TripleBox setPartialSubmit(Boolean partialSubmit) {
        this.partialSubmit = partialSubmit;
        return this;
    }

    /**
     * 构造函数
     *
     * @param name       名称
     * @param label      标题
     * @param value      值
     * @param text       显示文本
     */
    public TripleBox(String name, String label, Object value, String text) {
        super(name, label, value, text);
    }
    /**
     * 构造函数
     *
     * @param name       名称
     * @param label      标题
     * @param value      值
     * @param text       显示文本
     */
    public TripleBox(String name, Label label, Object value, String text) {
        super(name, label, value, text);
    }

    /**
     * 选中所有
     *
     * @return Boolean
     */
    public Boolean getCheckAll() {
        return checkAll;
    }

    /**
     * 选中所有
     *
     * @param checkAll Boolean
     * @return 本身，这样可以继续设置其他属性
     */
    public TripleBox setCheckAll(Boolean checkAll) {
        this.checkAll = checkAll;
        return this;
    }
}
