package com.rongji.dfish.ui.form;

import java.util.Map;

/**
 * Validatable 是可校验的表单元素
 * @author DFish Team
 * @param <T>  当前对象类型
 * @since 2.4
 */
//FIXME 模式需要变一下，这个接口是在太多了应该要改为类似GridColumn的模式
//
//另外应该提供 getValidategroup和getValidate的方法
public interface Validatable<T extends Validatable<T>> {
	
	/**
	 * 增加一个校验，如果增加同类型的校验，后面增加的将会覆盖原来的校验，但不会覆盖不同类型的校验，比如required和minvalue
	 * <pre>
	 * xx.addValidate(Validate.require());
	 * </pre>
	 * @param validate Validate
	 * @return 本身，这样可以继续设置其他属性
	 */
	T addValidate(Validate validate);
	/**
	 * 设置一个校验，如果增加同类型的校验，后面增加的将会覆盖原来的校验
	 * <pre>
	 * xx.addValidate(Validate.require());
	 * </pre>
	 * @param validate Validate
	 * @return 本身，这样可以继续设置其他属性
	 */
	T setValidate(Validate validate);
	/**
	 * 增加一个校验，如果增加同类型的校验，后面增加的将会覆盖原来的校验，但不会覆盖不同类型的校验，比如required和minvalue
	 * <pre>
	 * xx.addValidate("draft",Validate.require() .setErrMsg("姓名不可以为空"));
	 * </pre>
	 * @param name 名字
	 * @param validate Validate
	 * @return 本身，这样可以继续设置其他属性
	 */
	T addValidate(String name,Validate validate);
	/**
	 * 设置一个校验，如果增加同类型的校验，后面增加的将会覆盖原来的校验
	 * <pre>
	 * xx.addValidate("draft",Validate.require() .setErrMsg("姓名不可以为空"));
	 * </pre>
	 * @param name 名字
	 * @param validate Validate
	 * @return 本身，这样可以继续设置其他属性
	 */
	T setValidate(String name,Validate validate);
	/**
	 * 去掉一个校验组
	 * @param name String
	 * @return 本身，这样可以继续设置其他属性
	 */	
	T removeValidate(String name);
	/**
	 * 取得默认校验信息
	 * @return 本身，这样可以继续设置其他属性
	 */
	Validate getValidate();
	
	/**
	 * 取得某个名字的校验
	 * @param name String
	 * @return Validate
	 */
	Validate getValidate(String name);
	/**
	 * 取得除默认校验组外的其他信息
	 * @return 本身，这样可以继续设置其他属性
	 */
	Map<String,Validate> getValidategroup();
	
	   /**
     * 字段输入值需要符合无符号整形(正整数)格式。这里是它的正则表达式格式
     */
    public static final String PATTERN_UNSIGN_INTEGER = "/^\\d+$/g"; ///
    /**
     * 字段输入值需要符合整形格式。这里是它的正则表达式格式
     */
    public static final String PATTERN_INTEGER = "/^-?\\d+$/g"; //

    /**
     * 字段输入值需要符合无符号数字，正数格式。这里是它的正则表达式格式
     * 如1.0 2.5 或2
     */
    public static final String PATTERN_UNSIGN_FLOAT = "/^\\d+(\\.\\d+)?$/g"; ///**
    /**
     * 字段输入值需要符合无符号数字，正数格式。小数不超过2位。这里是它的正则表达式格式
     * 如1.0 2.5 或2
     */
    public static final String PATTERN_UNSIGN_MONEY = "/^\\d+(\\.\\d{1,2})?$/g"; ///**
    /**
     * 字段输入值需要符合\数字。这里是它的正则表达式格式
     */
    public static final String PATTERN_FLOAT = "/^-?\\d+(\\.\\d+)?$/g"; //

    /**
     * 不能有逗号
     */
    public static final String PATTERN_NO_COMMA = "!/^ |,| $/"; // "!/,/"

    /**
     * 匹配EMAIL
     */
    public static final String PATTERN_EMAIL =
            "/^[_a-zA-Z0-9-]+(\\.[_a-zA-Z0-9-]+)*@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*$/";
    
	/**
	 * 默认验证名称
	 */
	public static String DEFAULT_VALIDATE_NAME="default";
//	/**
//	 * 默认的验证组
//	 */
//	public static String[] DEFAULT_VALIDATE_GROUPS={"default"};

}
