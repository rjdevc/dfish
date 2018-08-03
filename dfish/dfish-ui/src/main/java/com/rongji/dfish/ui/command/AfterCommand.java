package com.rongji.dfish.ui.command;

/**
 * 在元素外部的后方增加内容
 * <div>
 * <div>before</div>
 * <div style='border:1px solid #666;'>
 * <div>prepend</div>
 * <span style='line-height:300%;font-weight:bold;'>TARGET</span>
 * <div>append</div>
 * </div>
 * <div>after</div>
 * </div>
 * @author DFish Team
 * @version 1.0
 * @since DFish 3.0
 */
public class AfterCommand extends AddCommand<AfterCommand>{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6784590200007210824L;

	@Override
	public String getType() {
		return "after";
	}

}
