package com.rongji.dfish.ui.form;

import com.rongji.dfish.ui.JsonWrapper;

public class Label extends AbstractLabel<Label> implements JsonWrapper<Object> {

    private static final long serialVersionUID = -1384522916094820984L;

    public Label(String text) {
        this.setText(text);
//        this.setWidth("0");
    }

    @Override
    public Object getPrototype() {
        if (isComplex()) {
            JsonLabel jo = new JsonLabel();
            copyProperties(jo, this);
            return jo;
        }
        return this.text;
    }

    private boolean isComplex() {
        return !"0".equals(getWidth()) ||
                getCls() != null || getStyle() != null ||//常用的属性排在前面
                getId() != null || getHeight() != null ||
                getAlign() != null || getEscape() != null ||
                getTemplate() != null ||
                (getData() != null && getData().size() > 0) ||
                getFormat() != null || getSuffix() != null ||//新增的属性 2020-03-05
                getBeforeContent() != null || getPrependContent() != null ||
                getAppendContent() != null || getAfterContent() != null ||
                getGid() != null || getHeightMinus() != null ||
                getMaxHeight() != null || getMaxWidth() != null ||
                getMinHeight() != null || getMinWidth() != null ||
                (getOn() != null && getOn().size() > 0) ||
                getWidthMinus() != null;
    }

}
