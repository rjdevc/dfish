package com.rongji.dfish.ui.form;

/**
 * Number为可微调的数字输入框，该组件的效果是右侧有上下箭头增减（3.2-版本的Spinner）
 *
 * @author lamontYu
 * @since DFish5.0
 */
public class NumberBox extends AbstractNumberInput<NumberBox> {

    private static final long serialVersionUID = -8926939363636955539L;

    public NumberBox(String name, String label, Number value) {
        super(name, label, value);
    }

    public NumberBox(String name, Label label, Number value) {
        super(name, label, value);
    }

}
