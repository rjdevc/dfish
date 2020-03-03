package com.rongji.dfish.ui.layout;

import com.rongji.dfish.ui.*;
import com.rongji.dfish.ui.auxiliary.CollapseButton;
import com.rongji.dfish.ui.widget.AbstractButton;

import java.util.List;

/**
 * 折叠面板。可以折叠/展开的内容区域。
 * @author lamontYu
 * @date 2020-01-15
 * @since 5.0
 */
public class Collapse extends AbstractPubNodeContainer<Collapse, CollapseButton, CollapseButton> {

    private Boolean focusMultiple;

    public Collapse(String id) {
        super(id);
    }

    @Override
    protected CollapseButton newPub() {
        return new CollapseButton(null);
    }

    public Boolean getFocusMultiple() {
        return focusMultiple;
    }

    public Collapse setFocusMultiple(Boolean focusMultiple) {
        this.focusMultiple = focusMultiple;
        return this;
    }

}
