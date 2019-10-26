package com.rongji.dfish.ui.layout;

import java.util.List;

import com.rongji.dfish.ui.Alignable;
import com.rongji.dfish.ui.MultiContainer;
import com.rongji.dfish.ui.Scrollable;
import com.rongji.dfish.ui.Valignable;
import com.rongji.dfish.ui.Widget;

/**
 * 通过把当前面板，简单的划分为上下或左右的方式进行布局。是最基础的布局类型。
 * 划分该类布局的时候一般支持 数字(单位像素) 百分比 * 和 -1
 * @author DFish team
 *
 * @param <T> 当前类型
 */
@SuppressWarnings("unchecked")
public abstract class LinearLayout<T extends LinearLayout<T>> extends AbstractLayout<T, Widget<?>> 
implements Scrollable<T>,Alignable<T>,Valignable<T>, MultiContainer<T,Widget<?>>{
	/**
	 * 
	 */
	private static final long serialVersionUID = -7555807071265375322L;
	private Boolean scroll; 
	private String scrollClass;
	private String align;
	private String valign;
	/**
	 * 默认构造函数
	 * @param id String
	 */
	public LinearLayout(String id) {
		super(id);
	}


	@Override
    public String getAlign() {
		return align;
	}

	@Override
    public T setAlign(String align) {
		this.align = align;
		return (T)this;
	}

	@Override
    public String getValign() {
		return valign;
	}

	@Override
    public T setValign(String valign) {
		this.valign = valign;
		return (T)this;
	}
	
	@Override
    public Boolean getScroll() {
		return scroll;
	}

	@Override
    public T setScroll(Boolean scroll) {
		this.scroll = scroll;
		return (T)this;
	}

	@Override
    public String getScrollClass() {
		return scrollClass;
	}


	@Override
    public T setScrollClass(String scrollClass) {
		this.scrollClass = scrollClass;
		return (T)this;
	}
	@Override
	public List<Widget<?>> getNodes() {
		return nodes;
	}
	
	/**
	 * 添加子面板 一般在布局面板下只能添加可见的元素， 如果添加hidden那么该size将被忽略
	 * 
	 * @param index 位置
	 * @param w Widget
	 * @param size String width或者height
	 * @return 本身，这样可以继续设置其他属性
	 */
	public abstract T add(int index,Widget<?>w,String size);

	@Override
	public T add(int index, Widget<?> w) {
		return add(index, w,null);
	}
	@Override
	public T add(Widget<?> w) {
		return add(-1, w,null);
	}
	
	/**
	 * 添加子面板 一般在布局面板下只能添加可见的元素， 如果添加hidden那么该size将被忽略
	 * 
	 * @param w Widget
	 * @param size String width或者height
	 * @return 本身，这样可以继续设置其他属性
	 */
	public T add(Widget<?> w,String size) {
		return add(-1, w,size);
	}
	
}
