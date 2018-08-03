package com.rongji.dfish.ui;

/**
 * Description: 有状态的
 * Copyright:   Copyright © 2018
 * Company:     rongji
 * @author		YuLM
 * @version		1.0
 *
 * Modification History:
 * Date						Author			Version			Description
 * ------------------------------------------------------------------
 * 2018年6月28日 下午5:36:17		YuLM			1.0				1.0 Version
 */
public interface Statusful<T extends Statusful<T>> {

	/**
	 * 默认
	 */
	String STATUS_NORMAL = "normal";
	/**
	 * 只读，并且不校验数据
	 */
	String STATUS_READONLY = "readonly";
	/**
	 * 只读，并且校验数据
	 */
	String STATUS_VALIDONLY = "validonly";
	/**
	 * 禁用(不可编辑数据，不校验数据，也不提交)
	 */
	String STATUS_DISABLED = "disabled";
	
	/**
	 * 状态
	 * @return String
	 * @author YuLM
	 */
	String getStatus();
	
	/**
	 * 设置状态
	 * @param status String
	 * @return 本身，这样可以继续设置其他属性
	 * @author YuLM
	 */
	T setStatus(String status);
	
}
