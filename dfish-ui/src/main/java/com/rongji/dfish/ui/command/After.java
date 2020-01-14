package com.rongji.dfish.ui.command;

import com.rongji.dfish.ui.Widget;

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
 * @date 2018-08-03 before
 * @since 3.0
 * @version 1.1 lamontYu 所有属性和type按照驼峰命名方式调整
 */
public class After extends AddCommand<After>{

	private static final long serialVersionUID = -6784590200007210824L;

	public After(String target, Widget<?>... nodes) {
		super(target, nodes);
	}
}
