package com.rongji.dfish.ui.command;


/**
 * WidgetControlCommand 是 用于控制视图对象动作的命令
 * 一般分为视图对象的增加，删除，修改，
 * @param <T> 当前对象类型
 *
 */
public abstract class NodeControlCommand<T extends NodeControlCommand<T>> extends AbstractCommand<T> {

    /**
	 * 
	 */
	private static final long serialVersionUID = 999627992841928211L;
	protected String target;
    protected String section;
    
    /**
     * 目标类型-视图组件
     */
    public static final String SECTION_WIDGET = "widget";
    /**
     * 目标类型-命令
     */
    public static final String SECTION_COMMAND = "cmd";
    /**
     * 目标类型-模板
     */
    public static final String SECTION_TEMPLATE = "template";
//    protected Boolean applyVariable;
	/**
	 * widget ID。
	 * @return target
	 */
	public String getTarget() {
		return target;
	}
	/**
	 * widget ID。
	 * @param target String 目标
	 * @return 本身，这样可以继续设置其他属性
	 */
	@SuppressWarnings("unchecked")
	public T setTarget(String target) {
		this.target = target;
		return (T) this;
	}
//	/**
//	 * 
//	 * @return applyVariable
//	 */
//	public Boolean getApplyVariable() {
//		return applyVariable;
//	}
//	/**
//	 * 
//	 * @param applyVariable
//	 * @return 本身，这样可以继续设置其他属性
//	 */
//	@SuppressWarnings("unchecked")
//	public T setApplyVariable(Boolean applyVariable) {
//		this.applyVariable = applyVariable;
//		return (T)this;
//	}
	public String getSection() {
		return section;
	}
	@SuppressWarnings("unchecked")
    public T setSection(String section) {
		this.section = section;
		return (T) this;
	}
}
