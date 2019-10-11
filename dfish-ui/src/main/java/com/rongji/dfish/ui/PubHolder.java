package com.rongji.dfish.ui;

/**
 * 默认参数
 * @author DFish Team
 *
 * @param <T>  当前对象类型
 * @param <N> 子节点对象类型
 */
public interface PubHolder<T extends PubHolder<T,N>,N> {
		/**
		 * 取得默认对象。
		 * 通常取得默认对象属性是最安全的。
		 * 直接用设置的话，有可能会覆盖上次的设置
		 * @return pub 默认对象
		 */
		N getPub();
		
		/**
		 * 设置默认对象
		 *  通常取得默认对象属性是最安全的。
		 * 	直接用设置的话，有可能会覆盖上次的设置
		 * @param pub 默认对象
		 * @return 本身，这样可以继续设置其他属性
		 */
		T setPub(N pub);
}
