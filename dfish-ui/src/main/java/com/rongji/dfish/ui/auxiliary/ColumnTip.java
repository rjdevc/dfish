package com.rongji.dfish.ui.auxiliary;

/**
 * 表格列提示
 *
 * @author lamontYu
 * @date 2017-07-31
 * @since 3.1
 */
public class ColumnTip {

    private String field;

    /**
     * 构造函数
     * @param field 提示的字段名
     */
    public ColumnTip(String field){
        setField(field);
    }

    /**
     * 提示的字段名
     *
     * @return String
     */
    public String getField() {
        return field;
    }

    /**
     * 设置提示的字段名
     *
     * @param field 字段名
     * @return 本身，这样可以继续设置其他属性
     */
    public ColumnTip setField(String field) {
        this.field = field;
        return this;
    }

}
