package com.rongji.dfish.ui.auxiliary;

import com.rongji.dfish.ui.form.AbstractBox;

/**
 * Table专用的单选框
 *
 * @author lamontYu - DFish Team
 */
public class TableRadio extends AbstractBox<TableRadio> {
    private static final long serialVersionUID = -8886839296833661491L;

    public TableRadio(String name, String label,  Object value, String text) {
        super(name, label, value, text);
    }
}
