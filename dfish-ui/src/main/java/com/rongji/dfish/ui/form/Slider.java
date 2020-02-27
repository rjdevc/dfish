package com.rongji.dfish.ui.form;


/**
 * Slider 滑块。用户可以通过移动滑块在控件中显示对应的值
 *
 * @author DFish Team
 */
public class Slider extends AbstractFormElement<Slider, Number> {

    private static final long serialVersionUID = -2998281453210751005L;

    /**
     * 默认构造函数
     *
     * @param name  表单元素名
     * @param label 标题
     * @param value 值
     */
    public Slider(String name, String label, Number value) {
        super.setName(name);
        super.setLabel(label);
        setValue(value);
    }


    /**
     * 设置最大值
     *
     * @param maxValue Number
     * @return 本身，这样可以继续设置其他属性
     */
    public Slider setMaxValue(Number maxValue) {
        this.addValidate(Validate.maxValue(maxValue == null ? null : String.valueOf(maxValue)));
        return this;
    }

    /**
     * 设置最小值
     *
     * @param minValue Number
     * @return 本身，这样可以继续设置其他属性
     */
    public Slider setMinValue(Number minValue) {
        this.addValidate(Validate.minValue(minValue == null ? null : String.valueOf(minValue)));
        return this;
    }

    @Override
    public Slider setValue(Object value) {
        this.value = toNumber(value);
        return this;
    }

}
