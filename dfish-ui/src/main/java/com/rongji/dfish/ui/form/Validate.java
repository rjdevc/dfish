package com.rongji.dfish.ui.form;

import com.rongji.dfish.base.util.BeanUtil;
import com.rongji.dfish.base.util.LogUtil;
import com.rongji.dfish.base.util.Utils;

import java.io.Serializable;
import java.util.Map;

/**
 * 校验
 *
 * @author DFish Team
 */
public class Validate implements Serializable {

    private static final long serialVersionUID = -6934337282036061111L;
    /**
     * 大于
     */
    public static final String COMPARE_MODE_GREATER_THAN = ">";
    /**
     * 小于
     */
    public static final String COMPARE_MODE_LESS_THAN = "<";
    /**
     * 小于或等于
     */
    public static final String COMPARE_MODE_NOT_GREATER_THAN = "<=";
    /**
     * 大于或等于
     */
    public static final String COMPARE_MODE_NOT_LESS_THAN = ">=";
    /**
     * 等于
     */
    public static final String COMPARE_MODE_EQUALS = "==";
    /**
     * 必填
     */
    private BooleanValueRule required;
    /**
     * 正则表达式
     */
    private StringValueRule pattern;
    /**
     * 另一个表单的name。用于简单的比较
     */
    private CompareRule compare;
    /**
     * 最小字节数
     */
    private NumberValueRule minLength;
    /**
     * 最大字节数。用于 text textarea password
     */
    private NumberValueRule maxLength;
    /**
     * 最少选择几项。用于 checkbox
     */
    private NumberValueRule minSize;
    /**
     * 最多选择几项。用于 checkbox
     */
    private NumberValueRule maxSize;
    /**
     * 最小值。用于 spinner date
     */
    private ObjectValueRule minValue;
    /**
     * 最大值。用于 spinner date
     */
    private ObjectValueRule maxValue;
    /**
     * 不能大于当前时间。用于 date
     */
    private BooleanValueRule beforeNow;
    /**
     * 不能小于当前时间的显示文本。
     */
    private BooleanValueRule afterNow;
    /**
     * JS语句。如果验证不通过，执行语句应当 return 一个字符串作为说明。如果验证通过则无需返回或返回空。
     */
    private String method;

    public BooleanValueRule getRequired() {
        return required;
    }

    public Validate setRequired(BooleanValueRule required) {
        this.required = required;
        return this;
    }

    public StringValueRule getPattern() {
        return pattern;
    }

    public Validate setPattern(StringValueRule pattern) {
        this.pattern = pattern;
        return this;
    }

    public CompareRule getCompare() {
        return compare;
    }

    public Validate setCompare(CompareRule compare) {
        this.compare = compare;
        return this;
    }

    public NumberValueRule getMinLength() {
        return minLength;
    }

    public Validate setMinLength(NumberValueRule minLength) {
        this.minLength = minLength;
        return this;
    }

    public NumberValueRule getMaxLength() {
        return maxLength;
    }

    public Validate setMaxLength(NumberValueRule maxLength) {
        this.maxLength = maxLength;
        return this;
    }

    public NumberValueRule getMinSize() {
        return minSize;
    }

    public Validate setMinSize(NumberValueRule minSize) {
        this.minSize = minSize;
        return this;
    }

    public NumberValueRule getMaxSize() {
        return maxSize;
    }

    public Validate setMaxSize(NumberValueRule maxSize) {
        this.maxSize = maxSize;
        return this;
    }

    public ObjectValueRule getMinValue() {
        return minValue;
    }

    public Validate setMinValue(ObjectValueRule minValue) {
        this.minValue = minValue;
        return this;
    }

    public ObjectValueRule getMaxValue() {
        return maxValue;
    }

    public Validate setMaxValue(ObjectValueRule maxValue) {
        this.maxValue = maxValue;
        return this;
    }

    public BooleanValueRule getBeforeNow() {
        return beforeNow;
    }

    public Validate setBeforeNow(BooleanValueRule beforeNow) {
        this.beforeNow = beforeNow;
        return this;
    }

    public BooleanValueRule getAfterNow() {
        return afterNow;
    }

    public Validate setAfterNow(BooleanValueRule afterNow) {
        this.afterNow = afterNow;
        return this;
    }

    /**
     * JS语句。如果验证不通过，执行语句应当 return 一个字符串作为说明。如果验证通过则无需返回或返回空。
     *
     * @return method
     */
    public String getMethod() {
        return method;
    }


    /**
     * JS语句。如果验证不通过，执行语句应当 return 一个字符串作为说明。如果验证通过则无需返回或返回空。
     *
     * @param method String
     * @return 本身，这样可以继续设置其他属性
     */
    public Validate setMethod(String method) {
        this.method = method;
        return this;
    }

    /**
     * 便捷构建一个必填的校验
     *
     * @return 本身，这样可以继续设置其他属性
     */
    public static Validate required(boolean required) {
        return required(required, null);
    }

    public static Validate required(boolean required, String text) {
        return new Validate().setRequired(new BooleanValueRule(required, text));
    }

    /**
     * 便捷构建一个模式的校验
     *
     * @param pattern 模式(正则表达式)
     * @return 本身，这样可以继续设置其他属性
     */
    public static Validate pattern(String pattern) {
        return pattern(pattern, null);
    }

    public static Validate pattern(String pattern, String text) {
        if (Utils.isEmpty(pattern)) {
            throw new IllegalArgumentException("pattern is empty");
        }
        return new Validate().setPattern(new StringValueRule(pattern, text));
    }

    public static Validate compare(String target, String mode) {
        return compare(target, mode, null);
    }

    /**
     * 便捷构建一个对比的校验
     *
     * @param target 对比模式
     * @param mode   对比另外一个表单的名字
     * @param text   对比另外一个表单的名字
     * @return 本身，这样可以继续设置其他属性
     */
    public static Validate compare(String target, String mode, String text) {
        if (Utils.isEmpty(target)) {
            throw new IllegalArgumentException("target is empty");
        }
        if (Utils.isEmpty(mode)) {
            throw new IllegalArgumentException("mode is empty");
        }
        return new Validate().setCompare(new CompareRule(target, mode, text));
    }

    /**
     * 便捷构建一个最小长度的校验
     *
     * @param minLength 最小长度
     * @return 本身，这样可以继续设置其他属性
     */
    public static Validate minLength(int minLength) {
        return minLength(minLength, null);
    }

    public static Validate minLength(int minLength, String text) {
        return new Validate().setMinLength(new NumberValueRule(minLength, text));
    }

    public static Validate maxLength(int maxLength) {
        return maxLength(maxLength, null);
    }

    /**
     * 便捷构建一个最大长度的校验
     *
     * @param maxLength 最大长度
     * @return 本身，这样可以继续设置其他属性
     */
    public static Validate maxLength(int maxLength, String text) {
        return new Validate().setMaxLength(new NumberValueRule(maxLength, text));
    }

    /**
     * 便捷构建一个最小选择项的校验
     *
     * @param minSize 最小选择项
     * @return 本身，这样可以继续设置其他属性
     */
    public static Validate minSize(int minSize) {
        return minSize(minSize, null);
    }

    public static Validate minSize(int minSize, String text) {
        return new Validate().setMinSize(new NumberValueRule(minSize, text));
    }

    public static Validate maxSize(int maxSize) {
        return maxSize(maxSize, null);
    }

    /**
     * 便捷构建一个最多选择项的校验
     *
     * @param maxSize 最多选择项
     * @return 本身，这样可以继续设置其他属性
     */
    public static Validate maxSize(int maxSize, String text) {
        return new Validate().setMaxSize(new NumberValueRule(maxSize, text));
    }

    /**
     * 便捷构建一个最小值的校验
     *
     * @param minValue 最小值
     * @return 本身，这样可以继续设置其他属性
     */
    public static Validate minValue(Object minValue) {
        return minValue(minValue, null);
    }

    public static Validate minValue(Object minValue, String text) {
        return new Validate().setMinValue(new ObjectValueRule(minValue, text));
    }


    /**
     * 便捷构建一个最大值的校验
     *
     * @param maxValue 最大值
     * @return 本身，这样可以继续设置其他属性
     */
    public static Validate maxValue(Object maxValue) {
        return maxValue(maxValue, null);
    }

    public static Validate maxValue(Object maxValue, String text) {
        return new Validate().setMaxValue(new ObjectValueRule(maxValue, text));
    }

    /**
     * 便捷构建一个早于当前时间的校验
     *
     * @return 本身，这样可以继续设置其他属性
     */
    public static Validate beforeNow(boolean beforeNow) {
        return beforeNow(beforeNow, null);
    }

    public static Validate beforeNow(boolean beforeNow, String text) {
        return new Validate().setBeforeNow(new BooleanValueRule(beforeNow, text));
    }

    /**
     * 便捷构建一个晚于当前时间的校验
     *
     * @return 本身，这样可以继续设置其他属性
     */
    public static Validate afterNow(boolean afterNow) {
        return afterNow(afterNow, null);
    }

    public static Validate afterNow(boolean afterNow, String text) {
        return new Validate().setAfterNow(new BooleanValueRule(afterNow, text));
    }

    /**
     * 合并另一个条件，以便输出
     *
     * @param other 另一个条件
     */
    public void combine(Validate other) {
        if (other == null) {
            return;
        }
        try {
            Map<String, Object> props = BeanUtil.getPropMap(other);
            props.remove("class");
            props.remove("type");
            for (Map.Entry<String, Object> entry : props.entrySet()) {
                String name = entry.getKey();
                Object value = entry.getValue();
                if (name.endsWith("text")) {
                    continue;
                }
                BeanUtil.invokeMethod(this, "set" + (char) (name.charAt(0) - 32) + name.substring(1), new Object[]{value});
            }
        } catch (Exception e) {
            LogUtil.error(getClass(), null, e);
        }

    }

    /**
     * 清楚校验条件
     */
    public void clear() {
        BeanUtil.copyPropertiesExact(this, new Validate());
    }

    public static class Rule<T extends Rule<T>> {
        protected String text;

        public String getText() {
            return text;
        }

        public T setText(String text) {
            this.text = text;
            return (T) this;
        }
    }

    public static class BooleanValueRule extends Rule<BooleanValueRule> {
        private Boolean value;

        public BooleanValueRule(Boolean value) {
            this.value = value;
        }

        public BooleanValueRule(Boolean value, String text) {
            this.value = value;
            this.text = text;
        }

        public Boolean getValue() {
            return value;
        }

        public BooleanValueRule setValue(Boolean value) {
            this.value = value;
            return this;
        }
    }

    public static class StringValueRule extends Rule<StringValueRule> {
        private String value;

        public StringValueRule(String value) {
            this.value = value;
        }

        public StringValueRule(String value, String text) {
            this.value = value;
            this.text = text;
        }

        public String getValue() {
            return value;
        }

        public StringValueRule setValue(String value) {
            this.value = value;
            return this;
        }
    }

    public static class NumberValueRule extends Rule<NumberValueRule> {
        private Number value;

        public NumberValueRule(Number value) {
            this.value = value;
        }

        public NumberValueRule(Number value, String text) {
            this.value = value;
            this.text = text;
        }

        public Number getValue() {
            return value;
        }

        public NumberValueRule setValue(Number value) {
            this.value = value;
            return this;
        }
    }

    public static class ObjectValueRule extends Rule<ObjectValueRule> {
        private Object value;

        public ObjectValueRule(Object value) {
            this.value = value;
        }

        public ObjectValueRule(Object value, String text) {
            this.value = value;
            this.text = text;
        }

        public Object getValue() {
            return value;
        }

        public ObjectValueRule setValue(Object value) {
            this.value = value;
            return this;
        }
    }

    public static class CompareRule extends Rule<StringValueRule> {
        private String target;
        private String mode;

        public CompareRule(String target, String mode) {
            this.target = target;
            this.mode = mode;
        }

        public CompareRule(String target, String mode, String text) {
            this.target = target;
            this.mode = mode;
            this.text = text;
        }

        public String getTarget() {
            return target;
        }

        public CompareRule setTarget(String target) {
            this.target = target;
            return this;
        }

        public String getMode() {
            return mode;
        }

        public CompareRule setMode(String mode) {
            this.mode = mode;
            return this;
        }
    }

}
