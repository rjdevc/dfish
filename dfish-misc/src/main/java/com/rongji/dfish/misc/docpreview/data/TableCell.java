package com.rongji.dfish.misc.docpreview.data;

import java.util.List;

/**
 * 单元格
 */
public class TableCell {
    /**
     * 占用几列
     * @return Integer
     */
    public Integer getColSpan() {
        return colSpan;
    }

    /**
     * 占用几列
     * @param colSpan Integer
     */
    public void setColSpan(Integer colSpan) {
        this.colSpan = colSpan;
    }
    /**
     * 占用几行
     * @return Integer
     */
    public Integer getRowSpan() {
        return rowSpan;
    }

    /**
     * 占用几行
     * @param rowSpan Integer
     */
    public void setRowSpan(Integer rowSpan) {
        this.rowSpan = rowSpan;
    }

    /**
     * 主体内容
     * @return List
     */
    public List<DocumentElement> getBody() {
        return body;
    }

    /**
     * 主体内容
     * @param body List
     */
    public void setBody(List<DocumentElement> body) {
        this.body = body;
    }

    private Integer colSpan;
    private Integer rowSpan;
    private List<DocumentElement> body;
}
