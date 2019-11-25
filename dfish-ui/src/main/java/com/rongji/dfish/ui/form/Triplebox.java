package com.rongji.dfish.ui.form;

/**
 * checkbox的基础上扩展半选中状态,tripleBox组件去掉checked属性，以value为准
 *
 * @author DFish Team
 * @version 1.0
 * @since XMLTMPL 2.1
 */
public class Triplebox extends AbstractBox<Triplebox> {
    /**
     *
     */
    private static final long serialVersionUID = -5234746180880441591L;

    private Boolean partialsubmit;

    private Boolean checkall;

    /**
     * 半选状态是否提交数据
     *
     * @return Boolean
     */
    public Boolean getPartialsubmit() {
        return partialsubmit;
    }

    /**
     * 半选状态是否提交数据
     *
     * @param partialsubmit Boolean
     * @return 本身，这样可以继续设置其他属性
     */
    public Triplebox setPartialsubmit(Boolean partialsubmit) {
        this.partialsubmit = partialsubmit;
        return this;
    }

    /**
     * 构造函数
     *
     * @param name       名称
     * @param label      标题
     * @param checked    选中状态
     * @param value      值，这里的值只有3中状态 选中,未选中,部分选中
     * @param text       显示文本
     */
    public Triplebox(String name, String label, Boolean checked, Object value, String text) {
        super(name, label, checked, value, text);
    }

    @Override
    public String getType() {
        return "triplebox";
    }
//	@Override
//	public Triplebox setValue(Object value) {
//		this.value=value;
//		return this;
//	}

    /**
     * 选中所有
     *
     * @return Boolean
     */
    public Boolean getCheckall() {
        return checkall;
    }

    /**
     * 选中所有
     *
     * @param checkall Boolean
     * @return 本身，这样可以继续设置其他属性
     */
    public Triplebox setCheckall(Boolean checkall) {
        this.checkall = checkall;
        return this;
    }
}
