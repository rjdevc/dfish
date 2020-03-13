package com.rongji.dfish.misc.docpreview.data;

import java.util.List;

/**
 * 表格行
 */
public class TableRow {

    /**
     * 行的内容，由单元格构成
     * @return List
     */
    public List<TableCell> getCells() {
        return cells;
    }

    /**
     * 行的内容，由单元格构成
     * @param cells List
     */
    public void setCells(List<TableCell> cells) {
        this.cells = cells;
    }

    private List<TableCell> cells;
}
