package com.rongji.dfish.misc.docpreview.data;

import java.util.List;

public class TableCell {
    public Integer getColSpan() {
        return colSpan;
    }

    public void setColSpan(Integer colSpan) {
        this.colSpan = colSpan;
    }

    public Integer getRowSpan() {
        return rowSpan;
    }

    public void setRowSpan(Integer rowSpan) {
        this.rowSpan = rowSpan;
    }

    public List<DocumentElement> getBody() {
        return body;
    }

    public void setBody(List<DocumentElement> body) {
        this.body = body;
    }

    private Integer colSpan;
    private Integer rowSpan;
    private List<DocumentElement> body;
}
