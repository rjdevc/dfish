package com.rongji.dfish.ui.auxiliary;

import com.rongji.dfish.ui.AbstractWidget;

/**
 * 用于table的自增数字字段。
 */
public class TableRowNum extends AbstractWidget<TableRowNum> {

    private static final long serialVersionUID = -8038094039396279588L;

    public TableRowNum() {
    }

    private Integer start;

    /**
     * 初始值
     *
     * @return Integer
     */
    public Integer getStart() {
        return start;
    }

    /**
     * 设置初始值
     *
     * @param start Integer
     * @return 本身，这样可以继续设置其他属性
     */
    public TableRowNum setStart(Integer start) {
        this.start = start;
        return this;
    }

}
