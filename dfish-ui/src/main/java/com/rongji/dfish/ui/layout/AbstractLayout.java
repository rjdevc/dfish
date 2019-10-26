package com.rongji.dfish.ui.layout;

import java.util.ArrayList;
import java.util.List;

import com.rongji.dfish.ui.AbstractWidget;
import com.rongji.dfish.ui.FormElement;
import com.rongji.dfish.ui.Layout;
import com.rongji.dfish.ui.Widget;
/**
 * 抽象布局类，默认的布局同时还是一个Widget
 * @author DFish Team
 *
 * @param <T> 当前对象类型
 * @param <N> 子节点对象类型
 */
@SuppressWarnings("unchecked")
public abstract class AbstractLayout<T extends AbstractLayout<T,N>,N extends Widget<?>> extends AbstractWidget<T> implements Layout<T,N> {
	
	private static final long serialVersionUID = 6322077434879898040L;

	@Override
	public List<N> findNodes() {
		return nodes;
	}

	/**
	 * 构造函数
	 * @param id String
	 */
	public AbstractLayout(String id){
		this.id=id;
	}
	protected List<N> nodes = new ArrayList<N>();

	/**
     * 添加子面板
     * @param w  N
     * @return 本身，这样可以继续设置其他属性
     */
	public T add(N w) {
        return add(-1,w);
    }
    /**
     * 在指定的位置添加子面板
     * @param index 位置
     * @param w  N
     * @return 本身，这样可以继续设置其他属性
     */
	public T add(int index, N w) {
        if (w == null) {
            return (T) this;
        }
        if(w==this) {
            throw new IllegalArgumentException(
                    "can not add widget itself as a sub widget");
        }
        if(index<0){
        	nodes.add(w);
        }else{
        	nodes.add(index,w);
        }
        return (T) this;
    }

	@Override
	public Widget<?> findNodeById(String id) {
		return super.findNodeById(id);
	}
	
	@Override
	public List<FormElement<?,?>> findFormElementsByName(String name) {
		return super.findFormElementsByName(name);
	}

	@Override
	public T removeNodeById(String id) {
		return super.removeNodeById(id);
	}

    @Override
	public boolean replaceNodeById(Widget<?> w) {
		return super.replaceNodeById(w);
	}
	

//	/**
//	 * 点击变化
//	 * @return checkmodify
//	 */
//	public Boolean isCheckmodify() {
//		return checkmodify;
//	}
//
//	/**
//	 * 点击变化
//	 * @param checkmodify
//	 * @return 本身，这样可以继续设置其他属性
//	 */
//	@SuppressWarnings("unchecked")
//	public T setCheckmodify(Boolean checkmodify) {
//		this.checkmodify = checkmodify;
//		return (T)this;
//	}
    /**
     * 拷贝属性
     * @param to AbstractLayout
     * @param from AbstractLayout
     */
    protected void copyProperties(AbstractLayout<?,N>to,AbstractLayout<?,N>from){
		super.copyProperties(to, from);
		to.nodes=from.nodes;
	}

}
