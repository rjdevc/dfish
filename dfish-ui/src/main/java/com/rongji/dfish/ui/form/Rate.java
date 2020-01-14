package com.rongji.dfish.ui.form;

/**
 * Rate 为5个星评价输入
 *
 * @author DFish Team
 * @version 1.1
 * @since 3.0
 */
public class Rate extends AbstractFormElement<Rate, Integer> {

    private static final long serialVersionUID = -6595357232541220426L;

    @Override
    public Rate setValue(Object value) {
        if (value == null) {
            this.value = null;
            return this;
        }
        Integer val = null;
        if (value instanceof Integer) {
            val = (Integer) value;
        } else if (value instanceof String) {
            val = Integer.valueOf((String) value);
        } else if (value instanceof Number) {
            val = ((Number) value).intValue();
        } else {
            throw new IllegalArgumentException("[" + value.getClass().getName() + "]" + value);
        }
        if (val > 10 || val < 0) {
            throw new IllegalArgumentException(String.valueOf(value));
        }
        this.value = val;
        return this;
    }

    /**
     * 默认构造函数
     *
     * @param name  名字
     * @param label 标题
     * @param value 值
     */
    public Rate(String name, String label, Integer value) {
        this.name = name;
        setLabel(label);
        setValue(value);
    }

    @Override
    @Deprecated
    public Rate setTip(Boolean tip) {
        return this;
    }

    @Override
    @Deprecated
    public Rate setTip(String tip) {
        return this;
    }
}
