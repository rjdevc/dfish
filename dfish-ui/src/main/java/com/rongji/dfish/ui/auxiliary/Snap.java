package com.rongji.dfish.ui.auxiliary;

import com.rongji.dfish.ui.Positionable;

public class Snap implements Positionable<Snap> {
    private String target;
    private String position;
    private Boolean inner;
    private Integer indent;

    public String getTarget() {
        return target;
    }

    public Snap setTarget(String target) {
        this.target = target;
        return this;
    }

    @Override
    public Snap setPosition(String position) {
        this.position = position;
        return this;
    }

    @Override
    public String getPosition() {
        return position;
    }

    public Boolean getInner() {
        return inner;
    }

    public Snap setInner(Boolean inner) {
        this.inner = inner;
        return this;
    }

    public Integer getIndent() {
        return indent;
    }

    public Snap setIndent(Integer indent) {
        this.indent = indent;
        return this;
    }

}
