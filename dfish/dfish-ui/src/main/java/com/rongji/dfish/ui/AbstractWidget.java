package com.rongji.dfish.ui;



/**
 * AbstractWidget 为抽象widget类，为方便widget构建而创立
 * @author DFish Team
 *
 * @param <T> 当前对象类型
 */
public abstract class AbstractWidget<T extends AbstractWidget<T>> extends AbstractNode<T> implements Widget<T> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6752586392648341685L;

	/**
	 * 计算得到当前节点是否要输出转义的Json
	 * @param selfEscape 当前节点转义开关
	 * @param parentEscape 上级节点转义开关
	 * @return Boolean 
	 */
	public static Boolean calcRealEscape(Boolean selfEscape, Boolean parentEscape) {
		// 暂时先将方法写在这里
		if (selfEscape == null) { // 为空时默认继承上级的设置
			return null;
		} else if (parentEscape == null) { // 上级为空,默认相当于没有转义
			return Boolean.TRUE.equals(selfEscape) ? true : null;
		} else { // 2个都不为空时
			return !selfEscape.equals(parentEscape) ? selfEscape : null;
		}
	}
	
}
