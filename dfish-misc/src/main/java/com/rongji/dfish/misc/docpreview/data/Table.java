package com.rongji.dfish.misc.docpreview.data;

import java.util.List;

/**
 * 表格
 */
public class Table implements DocumentElement{
    private List<TableColumn> columns;
    private List<TableRow> rows;

    /**
     * 表格的列
     * @return List
     */
    public List<TableColumn> getColumns() {
        return columns;
    }

    /**
     * 表格的列
     * @param columns List
     */
    public void setColumns(List<TableColumn> columns) {
        this.columns = columns;
    }

    /**
     * 表格的行
     * @return List
     */
    public List<TableRow> getRows() {
        return rows;
    }

    /**
     * 表格的行
     * @param rows
     */
    public void setRows(List<TableRow> rows) {
        this.rows = rows;
    }

    @Override
    public String getType() {
        return TYPE_TABLE;
    }
}
