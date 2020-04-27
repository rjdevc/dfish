package com.rongji.dfish.ui.form;

import com.rongji.dfish.base.util.BeanUtil;
import com.rongji.dfish.base.util.LogUtil;
import com.rongji.dfish.base.util.Utils;
import com.rongji.dfish.ui.auxiliary.ValidateRule;

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
    private ValidateRule required;
    /**
     * 正则表达式
     */
    private ValidateRule pattern;
    /**
     * 另一个表单的name。用于简单的比较
     */
    private ValidateRule compare;
    /**
     * 最小字节数
     */
    private ValidateRule minLength;
    /**
     * 最大字节数。用于 text textarea password
     */
    private ValidateRule maxLength;
    /**
     * 最少选择几项。用于 checkbox
     */
    private ValidateRule minSize;
    /**
     * 最多选择几项。用于 checkbox
     */
    private ValidateRule maxSize;
    /**
     * 最小值。用于 spinner date
     */
    private ValidateRule minValue;
    /**
     * 最大值。用于 spinner date
     */
    private ValidateRule maxValue;
    /**
     * 不能大于当前时间。用于 date
     */
    private ValidateRule beforeNow;
    /**
     * 不能小于当前时间的显示文本。
     */
    private ValidateRule afterNow;
    /**
     * JS语句。如果验证不通过，执行语句应当 return 一个字符串作为说明。如果验证通过则无需返回或返回空。
     */
    private String method;

    /**
     * 是否必填。默认值为true。
     *
     * @return 必填校验规则
     */
    public ValidateRule getRequired() {
        return required;
    }

    /**
     * 是否必填。默认值为true。
     *
     * @param required boolean 是否必填
     * @return 本身，这样可以继续设置其他属性
     */
    public Validate setRequired(ValidateRule required) {
        this.required = required;
        return this;
    }

    /**
     * 正则表达式。
     *
     * @return 正则校验规则
     */
    public ValidateRule getPattern() {
        return pattern;
    }

    /**
     * 正则表达式。
     *
     * @param pattern ValidateRule 正则表达式
     * @return 本身，这样可以继续设置其他属性
     */
    public Validate setPattern(ValidateRule pattern) {
        this.pattern = pattern;
        return this;
    }

    /**
     * 和另一个表单做比较。
     *
     * @return 比较校验规则
     */
    public ValidateRule getCompare() {
        return compare;
    }

    /**
     * 和另一个表单做比较。
     *
     * @param compare ValidateRule 校验规则
     * @return 本身，这样可以继续设置其他属性
     */
    public Validate setCompare(ValidateRule compare) {
        this.compare = compare;
        return this;
    }

    /**
     * 最小字节数。
     *
     * @return 最小字节数校验规则
     */
    public ValidateRule getMinLength() {
        return minLength;
    }

    /**
     * 最小字节数。
     *
     * @param minLength ValidateRule 校验规则
     * @return 本身，这样可以继续设置其他属性
     */
    public Validate setMinLength(ValidateRule minLength) {
        this.minLength = minLength;
        return this;
    }

    /**
     * 最大字节数。用于 Text Textarea Password
     *
     * @return 最大字节数校验规则
     */
    public ValidateRule getMaxLength() {
        return maxLength;
    }

    /**
     * 最大字节数。用于 Text Textarea Password
     *
     * @param maxLength ValidateRule 校验规则
     * @return 本身，这样可以继续设置其他属性
     */
    public Validate setMaxLength(ValidateRule maxLength) {
        this.maxLength = maxLength;
        return this;
    }

    /**
     * 最少选择几项。用于 CheckBox
     *
     * @return 最少选择项校验规则
     */
    public ValidateRule getMinSize() {
        return minSize;
    }

    /**
     * 最少选择几项。用于 CheckBox
     *
     * @param minSize ValidateRule 校验规则
     * @return 本身，这样可以继续设置其他属性
     */
    public Validate setMinSize(ValidateRule minSize) {
        this.minSize = minSize;
        return this;
    }

    /**
     * 最多选择几项。用于 CheckBox
     *
     * @return 最多选择项校验规则
     */
    public ValidateRule getMaxSize() {
        return maxSize;
    }

    /**
     * 最多选择几项。用于 CheckBox
     *
     * @param maxSize ValidateRule 校验规则
     * @return 本身，这样可以继续设置其他属性
     */
    public Validate setMaxSize(ValidateRule maxSize) {
        this.maxSize = maxSize;
        return this;
    }

    /**
     * 最小值。用于 Spinner DatePicker
     *
     * @return 最小值校验规则
     */
    public ValidateRule getMinValue() {
        return minValue;
    }

    /**
     * 最小值。用于 Spinner DatePicker
     *
     * @param minValue ValidateRule 校验规则
     * @return 本身，这样可以继续设置其他属性
     */
    public Validate setMinValue(ValidateRule minValue) {
        this.minValue = minValue;
        return this;
    }

    /**
     * 最大值。用于 Spinner DatePicker
     *
     * @return 最大值校验规则
     */
    public ValidateRule getMaxValue() {
        return maxValue;
    }

    /**
     * 最大值。用于 Spinner DatePicker
     *
     * @param maxValue ValidateRule 校验规则
     * @return 本身，这样可以继续设置其他属性
     */
    public Validate setMaxValue(ValidateRule maxValue) {
        this.maxValue = maxValue;
        return this;
    }

    /**
     * 不能大于当前时间。用于 DatePicker
     *
     * @return 时间校验规则
     */
    public ValidateRule getBeforeNow() {
        return beforeNow;
    }

    /**
     * 不能大于当前时间。用于 DatePicker
     *
     * @param beforeNow ValidateRule 校验规则
     * @return 本身，这样可以继续设置其他属性
     */
    public Validate setBeforeNow(ValidateRule beforeNow) {
        this.beforeNow = beforeNow;
        return this;
    }

    /**
     * 不能小于当前时间。用于 DatePicker
     *
     * @return 时间校验规则
     */
    public ValidateRule getAfterNow() {
        return afterNow;
    }

    /**
     * 不能小于当前时间。用于 DatePicker
     *
     * @param afterNow ValidateRule 校验规则
     * @return 本身，这样可以继续设置其他属性
     */
    public Validate setAfterNow(ValidateRule afterNow) {
        this.afterNow = afterNow;
        return this;
    }

    /**
     * JS语句。如果验证不通过，执行语句应当 return 一个字符串作为说明。如果验证通过则无需返回或返回空。
     *
     * @return method JS方法
     */
    public String getMethod() {
        return method;
    }


    /**
     * JS语句。如果验证不通过，执行语句应当 return 一个字符串作为说明。如果验证通过则无需返回或返回空。
     *
     * @param method String JS方法
     * @return 本身，这样可以继续设置其他属性
     */
    public Validate setMethod(String method) {
        this.method = method;
        return this;
    }

    /**
     * 便捷构建一个必填的校验
     *
     * @param required 是否必填
     * @return 本身，这样可以继续设置其他属性
     */
    public static Validate required(boolean required) {
        return required(required, null);
    }

    /**
     * 便捷构建一个必填的校验
     *
     * @param required 是否必填
     * @param text     错误提示
     * @return 本身，这样可以继续设置其他属性
     */
    public static Validate required(boolean required, String text) {
        return new Validate().setRequired(new ValidateRule(required, text));
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

    /**
     * 便捷构建一个模式的校验
     *
     * @param pattern 正则表达式
     * @param text    错误提示
     * @return 本身，这样可以继续设置其他属性
     */
    public static Validate pattern(String pattern, String text) {
        if (Utils.isEmpty(pattern)) {
            throw new IllegalArgumentException("pattern is empty");
        }
        return new Validate().setPattern(new ValidateRule(pattern, text));
    }

    /**
     * 便捷构建一个对比的校验
     *
     * @param target 对比目标
     * @param mode   对比模式
     * @return 校验对象
     */
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
        return new Validate().setCompare(new ValidateRule(null, text).setTarget(target).setMode(mode));
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

    /**
     * 便捷构建一个最小长度的校验
     *
     * @param minLength 最小长度
     * @param text      错误提示
     * @return 本身，这样可以继续设置其他属性
     */
    public static Validate minLength(int minLength, String text) {
        return new Validate().setMinLength(new ValidateRule(minLength, text));
    }

    /**
     * 便捷构建一个最大长度的校验
     *
     * @param maxLength 最大长度
     * @return 本身，这样可以继续设置其他属性
     */
    public static Validate maxLength(int maxLength) {
        return maxLength(maxLength, null);
    }

    /**
     * 便捷构建一个最大长度的校验
     *
     * @param maxLength 最大长度
     * @param text      错误提示
     * @return 本身，这样可以继续设置其他属性
     */
    public static Validate maxLength(int maxLength, String text) {
        return new Validate().setMaxLength(new ValidateRule(maxLength, text));
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

    /**
     * 便捷构建一个最小选择项的校验
     *
     * @param minSize 最小选择项
     * @param text    错误提示
     * @return 本身，这样可以继续设置其他属性
     */
    public static Validate minSize(int minSize, String text) {
        return new Validate().setMinSize(new ValidateRule(minSize, text));
    }

    /**
     * 便捷构建一个最多选择项的校验
     *
     * @param maxSize 最多选择项
     * @return 本身，这样可以继续设置其他属性
     */
    public static Validate maxSize(int maxSize) {
        return maxSize(maxSize, null);
    }

    /**
     * 便捷构建一个最多选择项的校验
     *
     * @param maxSize 最多选择项
     * @param text    错误提示
     * @return 本身，这样可以继续设置其他属性
     */
    public static Validate maxSize(int maxSize, String text) {
        return new Validate().setMaxSize(new ValidateRule(maxSize, text));
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

    /**
     * 便捷构建一个最小值的校验
     *
     * @param minValue 最小值
     * @param text     错误提示
     * @return 本身，这样可以继续设置其他属性
     */
    public static Validate minValue(Object minValue, String text) {
        return new Validate().setMinValue(new ValidateRule(minValue, text));
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

    /**
     * 便捷构建一个最大值的校验
     *
     * @param maxValue 最大值
     * @param text     错误提示
     * @return 本身，这样可以继续设置其他属性
     */
    public static Validate maxValue(Object maxValue, String text) {
        return new Validate().setMaxValue(new ValidateRule(maxValue, text));
    }

    /**
     * 便捷构建一个早于当前时间的校验
     *
     * @param beforeNow 是否早于当前时间
     * @return 本身，这样可以继续设置其他属性
     */
    public static Validate beforeNow(boolean beforeNow) {
        return beforeNow(beforeNow, null);
    }

    /**
     * 便捷构建一个早于当前时间的校验
     *
     * @param beforeNow 是否早于当前时间
     * @param text      错误提示
     * @return 本身，这样可以继续设置其他属性
     */
    public static Validate beforeNow(boolean beforeNow, String text) {
        return new Validate().setBeforeNow(new ValidateRule(beforeNow, text));
    }

    /**
     * 便捷构建一个晚于当前时间的校验
     *
     * @param afterNow 是否晚于当前时间
     * @return 本身，这样可以继续设置其他属性
     */
    public static Validate afterNow(boolean afterNow) {
        return afterNow(afterNow, null);
    }

    /**
     * 便捷构建一个晚于当前时间的校验
     *
     * @param afterNow 是否晚于当前时间
     * @param text     错误提示
     * @return 本身，这样可以继续设置其他属性
     */
    public static Validate afterNow(boolean afterNow, String text) {
        return new Validate().setAfterNow(new ValidateRule(afterNow, text));
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

}
