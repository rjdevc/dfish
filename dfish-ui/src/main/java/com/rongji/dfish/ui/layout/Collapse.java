package com.rongji.dfish.ui.layout;

import com.rongji.dfish.ui.*;
import com.rongji.dfish.ui.widget.AbstractButton;

import java.util.List;

/**
 * 折叠面板。可以折叠/展开的内容区域。
 * @author lamontYu
 * @date 2020-01-15
 * @since 5.0
 */
public class Collapse extends AbstractPubNodeContainer<Collapse, Collapse.Button> {

    private Boolean focusMultiple;

    public Collapse(String id) {
        super(id);
    }

    @Override
    protected Button newPub() {
        return new Button(null);
    }

    public Boolean getFocusMultiple() {
        return focusMultiple;
    }

    public Collapse setFocusMultiple(Boolean focusMultiple) {
        this.focusMultiple = focusMultiple;
        return this;
    }

    /**
     * 折叠按钮
     */
    public static class Button extends AbstractButton<Button> implements TargetHolder<Button> {

        private Widget<?> target;

        @Override
        public String getType() {
            return "CollapseButton";
        }

        /**
         * 构造函数
         *
         * @param text String 标题
         */
        public Button(String text) {
            super(text,null,null);
        }

        /**
         * 构造函数
         *
         * @param text String 标题
         * @param target Widget 目标组件
         */
        public Button(String text, Widget target) {
            super(text,null,null);
            this.setTarget(target);
        }

        /**
         * 折叠按钮对应的内容widget
         * @return Widget
         */
        @Override
        public Widget getTarget() {
            return target;
        }

        /**
         * 折叠按钮对应的内容widget
         * @param target Widget
         * @return 本身，这样可以继续设置其他属性
         */
        @Override
        public Button setTarget(Widget target) {
            this.target = target;
            return this;
        }

    }
}
