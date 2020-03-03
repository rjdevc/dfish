package com.rongji.dfish.ui.auxiliary;

public class BoxBind {
    private BoxField field;
    private Boolean keepShow;
    private Boolean fullPath;
    private String target;

    public BoxBind(BoxField field) {
        this.field = field;
    }

    public BoxBind(BoxField field, String target) {
        this.field = field;
        this.target = target;
    }

    public BoxField getField() {
        return field;
    }

    public BoxBind setField(BoxField field) {
        this.field = field;
        return this;
    }

    public Boolean getKeepShow() {
        return keepShow;
    }

    public BoxBind setKeepShow(Boolean keepShow) {
        this.keepShow = keepShow;
        return this;
    }

    public Boolean getFullPath() {
        return fullPath;
    }

    public BoxBind setFullPath(Boolean fullPath) {
        this.fullPath = fullPath;
        return this;
    }

    public String getTarget() {
        return target;
    }

    public BoxBind setTarget(String target) {
        this.target = target;
        return this;
    }
}