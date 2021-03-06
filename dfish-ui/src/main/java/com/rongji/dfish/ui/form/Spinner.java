package com.rongji.dfish.ui.form;

/**
 * Spinner 为可微调的数字输入框
 *
 * @author DFish Team
 * @version 1.1
 * @since DFish1.0
 */
public class Spinner extends AbstractNumberInput<Spinner> {

    private static final long serialVersionUID = -3131991622042455798L;

    public Spinner(String name, String label, Number value) {
        super(name, label, value);
    }

    public Spinner(String name, Label label, Number value) {
        super(name, label, value);
    }

}
