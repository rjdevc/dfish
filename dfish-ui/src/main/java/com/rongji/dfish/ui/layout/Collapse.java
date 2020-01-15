package com.rongji.dfish.ui.layout;

import com.rongji.dfish.ui.MultiNodeContainer;
import com.rongji.dfish.ui.PubHolder;
import com.rongji.dfish.ui.Widget;
import com.rongji.dfish.ui.widget.AbstractButton;

import java.util.List;

/**
 * 折叠面板。可以折叠/展开的内容区域。
 * @author lamontYu
 * @date 2020-01-15
 * @since 5.0
 */
public class Collapse extends AbstractNodeContainer<Collapse> implements MultiNodeContainer<Collapse>, PubHolder<Collapse, Collapse.Button>{

    private Boolean focusMultiple;
    private Button pub;

    public Collapse(String id) {
        super(id);
    }

    public Boolean getFocusMultiple() {
        return focusMultiple;
    }

    public Collapse setFocusMultiple(Boolean focusMultiple) {
        this.focusMultiple = focusMultiple;
        return this;
    }

    @Override
    public List getNodes() {
        return nodes;
    }

    public Button pub() {
        if (pub == null) {
            pub = new Button(null);
        }
        return pub;
    }

    @Override
    public Button getPub() {
        return pub;
    }

    @Override
    public Collapse setPub(Button pub) {
        this.pub = pub;
        return this;
    }

    /**
     * 折叠按钮
     */
    public static class Button extends AbstractButton<Button> {

        private Widget<?> target;
        /**
         * 构造函数
         *
         * @param text String 标题
         */
        public Button(String text) {
            this.setText(text);
        }

        /**
         * 构造函数
         *
         * @param text String 标题
         * @param target Widget&lt;?&gt; 目标组件
         */
        public Button(String text, Widget<?> target) {
            this.setText(text);
            this.setTarget(target);
        }

        /**
         * 折叠按钮对应的内容widget
         * @return Widget&lt;?&gt;
         */
        public Widget<?> getTarget() {
            return target;
        }

        /**
         * 折叠按钮对应的内容widget
         * @param target Widget&lt;?&gt;
         * @return 本身，这样可以继续设置其他属性
         */
        public Button setTarget(Widget<?> target) {
            this.target = target;
            return this;
        }

    }
}
