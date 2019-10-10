package com.rongji.dfish.misc.docpreview.data;

import java.util.List;

public class Table implements DocumentElement{
    private List<TableColumn> columns;
    private List<TableRow> rows;

    public List<TableColumn> getColumns() {
        return columns;
    }

    public void setColumns(List<TableColumn> columns) {
        this.columns = columns;
    }

    public List<TableRow> getRows() {
        return rows;
    }

    public void setRows(List<TableRow> rows) {
        this.rows = rows;
    }

    @Override
    public String getType() {
        return TYPE_TABLE;
    }
}
