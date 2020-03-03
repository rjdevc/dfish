package com.rongji.dfish.ui.auxiliary;

import com.rongji.dfish.ui.form.AbstractBox;

/**
 * Table专用的复选框
 *
 * @author lamontYu - DFish Team
 */
public class TableTripleBox extends AbstractBox<TableTripleBox> {
    private static final long serialVersionUID = -4770736316914887083L;

    private Boolean partialSubmit;
    private Boolean checkAll;

    /**
     * 构造函数
     *
     * @param name    名称
     * @param label   标题
     * @param value   值
     * @param text    显示文本
     */
    public TableTripleBox(String name, String label, Object value, String text) {
        super(name, label,  value, text);
    }

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
    public TableTripleBox setPartialSubmit(Boolean partialSubmit) {
        this.partialSubmit = partialSubmit;
        return this;
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
    public TableTripleBox setCheckAll(Boolean checkAll) {
        this.checkAll = checkAll;
        return this;
    }

}
