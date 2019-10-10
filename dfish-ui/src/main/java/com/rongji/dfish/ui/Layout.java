package com.rongji.dfish.ui;

import java.util.List;



/**
 * Layout 布局类
 * @author DFish Team
 *
 * @param <T>  当前对象类型
 * @param <N> 子节点对象类型
 */
public interface Layout<T extends Layout<T,N>, N extends Widget<?>> extends Container<T>,Widget<T>{
    /**
     * 可以根据子Widget的Id取得这个Widget。
     * 如果子集里面没有包含这个Widget，那么返回空
     * 如果子集的Widget本身也是一个Layout那么将递归查找
     * 如果这个Layout中有多个Widget对应这个ID，仅找到第一个。
     * @param id String
     * @return Widget
     */
	Widget<?> findNodeById(String id);
	
	/**
	 * 根据表单提交名取得表单元素。可能学多个。
	 * @param name 提交名
	 * @return 本身，这样可以继续设置其他属性
	 */
	List<FormElement<?,?>> findFormElementsByName(String name);
	

    /**
     * 根据子Widget的Id，移除这个Widget
     * 如果子集的Widget本身也是一个Layout那么将递归查找
     * @param id String
     * @return 本身，这样可以继续设置其他属性
     */
    T removeNodeById(String id);
    /**
     * 替代一个Widget,同find仅替换第一个找到的。
     * <p>Widget 的种类可以不一致。但是ID必须一致，并且不能为空</p>
     * @param w 需要替换的Widget
     * @return 是否替换成功。一般失败的原因有:<ol><li>这个Widget的ID不存在</li>
     * <li>原先Layout中并不存在这个id的Widget。</li>
     * <li>不能替代Layout本身</li></ol>
     */
    boolean replaceNodeById(Widget<?> w);	
	
}
