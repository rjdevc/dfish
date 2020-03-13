package com.rongji.dfish.ui.auxiliary;

/**
 * Box专用，设置数据来源
 */
public class BoxBind {
    private BoxField field;
    private Boolean keepShow;
    private Boolean fullPath;
    private String target;

    /**
     * 构造函数
     * @param field 字段参数
     */
    public BoxBind(BoxField field) {
        this.field = field;
    }

    /**
     * 构造函数
     * @param field 字段参数
     * @param target 数据来源Widget的ID。
     */
    public BoxBind(BoxField field, String target) {
        this.field = field;
        this.target = target;
    }

    /**
     * 字段参数
     * @return BoxField
     */
    public BoxField getField() {
        return field;
    }

    /**
     * 字段参数
     * @param field
     * @return 本身，这样可以继续设置其他属性
     */
    public BoxBind setField(BoxField field) {
        this.field = field;
        return this;
    }

    /**
     * 设置为true，无论是否有匹配到内容，都始终显示搜索结果框。
     * @return
     */
    public Boolean getKeepShow() {
        return keepShow;
    }

    /**
     * 设置为true，无论是否有匹配到内容，都始终显示搜索结果框。
     * @param keepShow
     * @return 本身，这样可以继续设置其他属性
     */
    public BoxBind setKeepShow(Boolean keepShow) {
        this.keepShow = keepShow;
        return this;
    }

    /**
     * 设置为true，选中项的文本显示完整的路径。
     * @return
     */
    public Boolean getFullPath() {
        return fullPath;
    }

    /**
     * 设置为true，选中项的文本显示完整的路径。
     * @param fullPath
     * @return 本身，这样可以继续设置其他属性
     */
    public BoxBind setFullPath(Boolean fullPath) {
        this.fullPath = fullPath;
        return this;
    }

    /**
     * 数据来源Widget的ID。类型可以是Table或Tree。如果不设置此参数，将以对话框内的第一个Table或第一个Tree作为数据来源。
     * @return
     */
    public String getTarget() {
        return target;
    }

    /**
     * 数据来源Widget的ID。类型可以是Table或Tree。如果不设置此参数，将以对话框内的第一个Table或第一个Tree作为数据来源。
     * @param target
     * @return 本身，这样可以继续设置其他属性
     */
    public BoxBind setTarget(String target) {
        this.target = target;
        return this;
    }
}