package com.rongji.dfish.ui.auxiliary;

public class ValidateRule<E> {
    private E value;
    private String text;
    private String target;
    private String mode;

    public ValidateRule() {
    }

    public ValidateRule(E value) {
        this.value = value;
    }

    public ValidateRule(E value, String text) {
        this.value = value;
        this.text = text;
    }

    public E getValue() {
        return value;
    }

    public ValidateRule setValue(E value) {
        this.value = value;
        return this;
    }

    public String getText() {
        return text;
    }

    public ValidateRule setText(String text) {
        this.text = text;
        return this;
    }

    public String getTarget() {
        return target;
    }

    public ValidateRule setTarget(String target) {
        this.target = target;
        return this;
    }

    public String getMode() {
        return mode;
    }

    public ValidateRule setMode(String mode) {
        this.mode = mode;
        return this;
    }
}
