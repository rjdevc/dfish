package com.rongji.dfish.ui.form;

/**
 * Number为可微调的数字输入框，该组件的效果是右侧有上下箭头增减（3.2-版本的Spinner）
 *
 * @author lamontYu
 * @date 2020-03-30
 * @since 5.0
 */
public class NumberBox extends AbstractNumberInput<NumberBox> {

    public NumberBox(String name, String label, Number value) {
        super(name, label, value);
    }

    public NumberBox(String name, Label label, Number value) {
        super(name, label, value);
    }

}
