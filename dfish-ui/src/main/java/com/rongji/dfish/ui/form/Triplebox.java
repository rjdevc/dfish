package com.rongji.dfish.ui.form;

import java.beans.Transient;


/**
 * checkbox的基础上扩展半选中状态,tripleBox组件去掉checked属性，以value为准
 * @author DFish Team
 * @version 1.0
 * @since XMLTMPL 2.1
 */
public class Triplebox extends AbstractBox<Triplebox> {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5234746180880441591L;

	private Integer checkstate;
	
	private Boolean partialsubmit;
	
	private Boolean checkall;
	/**
	 * 状态
	 * @return  Integer
	 */
	public Integer getCheckstate() {
		return checkstate;
	}

	/**
	 * 状态
	 * @param checkstate Integer
	 * @return this
	 */
	public Triplebox setCheckstate(Integer checkstate) {
		this.checkstate = checkstate;
		return this;
	}
	
	@Transient
	@Override
	public Boolean getChecked() {
		return super.getChecked();
	}

	@Override
	public Triplebox setChecked(Boolean checked) {
		if(checked!=null){
			this.checkstate=checked?CHECKSTATE_CHECKED:CHECKSTATE_UNCHECKED;
		}
		return this;
	}
	/**
	 * 半选状态是否提交数据
	 * @return Boolean
	 */
	public Boolean getPartialsubmit() {
		return partialsubmit;
	}

	/**
	 * 半选状态是否提交数据
	 * @param partialsubmit Boolean
	 * @return 本身，这样可以继续设置其他属性
	 */
	public Triplebox setPartialsubmit(Boolean partialsubmit) {
		this.partialsubmit = partialsubmit;
		return this;
	}
	
	/**
	 * 未选中状态
	 */
	public static final int CHECKSTATE_UNCHECKED=0;
	/**
	 * 选中状态
	 */
	public static final int CHECKSTATE_CHECKED=1;
	/**
	 * 部分选中状态
	 */
	public static final int CHECKSTATE_PARTIALCHECKED=2;
	
	/**
	 * 构造函数
	 * @param name 名称
	 * @param label 标题
	 * @param checkstate 状态
	 * @param value 值，这里的值只有3中状态 选中,未选中,部分选中
	 * @param text 显示文本
	 */
	public Triplebox(String name, String label, Integer checkstate, Object value,String text){
		super(name, label, null, value, text);
		this.checkstate=checkstate;
	}
	

	@Override
    public String getType() {
		return "triplebox";
	}
//	@Override
//	public Triplebox setValue(Object value) {
//		this.value=value;
//		return this;
//	}

	/**
	 * 选中所有
	 * @return Boolean
	 */
	public Boolean getCheckall() {
		return checkall;
	}

	/**
	 * 选中所有
	 * @param checkall Boolean
	 * @return 本身，这样可以继续设置其他属性
	 */
	public Triplebox setCheckall(Boolean checkall) {
		this.checkall = checkall;
		return this;
	}
}
