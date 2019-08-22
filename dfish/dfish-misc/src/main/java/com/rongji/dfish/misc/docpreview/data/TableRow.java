package com.rongji.dfish.misc.docpreview.data;

import java.util.List;

public class TableRow {

    public List<TableCell> getCells() {
        return cells;
    }

    public void setCells(List<TableCell> cells) {
        this.cells = cells;
    }

    private List<TableCell> cells;
}
