package com.rongji.dfish.ui.command;

/**
 * SubmitCommand 提交命令，改命令用于提交页面上的表单
 *
 * @author DFish Team
 * @version 1.1
 * @since XMLTMPL 2.0
 */
public class SubmitCommand extends CommunicateCommand<SubmitCommand> {
    /**
	 * 
	 */
	private static final long serialVersionUID = 7949712536303839674L;
	private String range;
    private String validategroup;
    private String validateeffect;
    private String validaterange;
    /**
     * 如果没校验通过，弹出窗口提示
     */
    public static final String VALIDATE_EFFECT_ALERT="alert";
    /**
     * 如果没校验通过，在输入框提示红线
     */
    public static final String VALIDATE_EFFECT_RED="red";
 
    /**
     * 构造函数
     * @param src 提交地址
     */
    public SubmitCommand(String src) {
       super(src);
    }
    
    /**
     * 需要提交的面板的ID，多个用逗号(半角)隔开，如果为空则表示整个view里面的所有表单。值得注意的是Grid等面板里面也可以有隐藏值。提交的时候，要考虑非FormPanel的情况
     * @param range String 设置需要提交的面板的ID
     * @return 本身，这样可以继续设置其他属性
     */
    public SubmitCommand setRange(String range) {
        this.range = range;
        return this;
    }

	/**
	 *  需要提交的面板的ID，多个用逗号(半角)隔开，如果为空则表示整个view里面的所有表单。值得注意的是Grid等面板里面也可以有隐藏值。提交的时候，要考虑非FormPanel的情况
	 * @return the range  String
	 * @since XMLTMPL 2.1
	 */
	public String getRange() {
		return range;
	}

	/**
	 * 使用哪一种策略进行校验
	 * @return String
	 * @see #getValidategroup()
	 */
	@Deprecated
	public String getValidate() {
		return getValidategroup();
	}

	/**
	 * 使用哪一种策略进行校验
	 * @param validate String
	 * @return 本身，这样可以继续设置其他属性
	 * @see #setValidategroup(String)
	 */
	@Deprecated
	public SubmitCommand setValidate(String validate) {
		return setValidategroup(validate);
	}

	/**
	 * 指定校验组
	 * @return String
	 */
	public String getValidategroup() {
		return validategroup;
	}

	/**
	 * 校验组
	 * @param validategroup String
	 * @return 本身，这样可以继续设置其他属性
	 */
	public SubmitCommand setValidategroup(String validategroup) {
		this.validategroup = validategroup;
		return this;
	}

	public String getType() {
		return "submit";
	}
	
	/**
	 * 验证效果
	 * @return validateeffect
	 * @see #VALIDATE_EFFECT_ALERT
	 * @see #VALIDATE_EFFECT_RED
	 */
	public String getValidateeffect() {
		return validateeffect;
	}
	/**
	 *  验证效果。
	 * @param validateeffect String
	 * @return 本身，这样可以继续设置其他属性
	 * @see #VALIDATE_EFFECT_ALERT
	 * @see #VALIDATE_EFFECT_RED
	 */
	public SubmitCommand setValidateeffect(String validateeffect) {
		this.validateeffect = validateeffect;
		return this;
	}
	
	/**
	 * 范围，指定一个 widget id，只校验这个 widget 内的表单。
	 * 多个 id 用逗号隔开。如果以感叹号 ! 开头，则表示不校验 widget 内的表单。
	 * @return String
	 */
	public String getValidaterange() {
		return validaterange;
	}
	/**
	 * 范围，指定一个 widget id，只校验这个 widget 内的表单。
	 * 多个 id 用逗号隔开。如果以感叹号 ! 开头，则表示不校验 widget 内的表单。
	 * @param validaterange String
	 * @return this
	 */
	public SubmitCommand setValidaterange(String validaterange) {
		this.validaterange = validaterange;
		return this;
	}

}
