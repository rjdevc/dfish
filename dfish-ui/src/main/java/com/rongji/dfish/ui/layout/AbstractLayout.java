package com.rongji.dfish.ui.layout;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.rongji.dfish.base.util.Utils;
import com.rongji.dfish.ui.*;
import com.rongji.dfish.ui.form.FormElement;
import com.rongji.dfish.ui.form.Hidden;

/**
 * 抽象布局类，默认的布局同时还是一个Widget
 * @author DFish Team
 *
 * @param <T> 当前对象类型
 */
@SuppressWarnings("unchecked")
public abstract class AbstractLayout<T extends AbstractLayout<T> > extends AbstractWidget<T> implements Container<T> {
	
	private static final long serialVersionUID = 6322077434879898040L;

//	@Overrideer
//	public List<N> findNodes() {
//		return nodes;
//	}

	/**
	 * 构造函数
	 * @param id String
	 */
	public AbstractLayout(String id){
		this.id=id;
	}
	protected List<HasId<?>> nodes = new ArrayList<>();

	/**
     * 添加子面板
     * @param w  N
     * @return 本身，这样可以继续设置其他属性
     */
	public T add(HasId w) {
		  if (w == null) {
            return (T) this;
        }
        if(w==this) {
        	throw new IllegalArgumentException("can not add widget itself as a sub widget");
		}
        nodes.add(w);
        return (T) this;
    }

//    /**
//     * 在指定的位置添加子面板
//     * @param index 位置
//     * @param w  N
//     * @return 本身，这样可以继续设置其他属性
//     */
//	public T add(int index, N w) {
//        if (w == null) {
//            return (T) this;
//        }
//        if(w==this)throw new IllegalArgumentException(
//        		"can not add widget itself as a sub widget");
//        if(index<0){
//        	nodes.add(w);
//        }else{
//        	nodes.add(index,w);
//        }
//        return (T) this;
//    }

//	@Override
//    public Widget<?> findNodeById(String id) {
//		return super.findNodeById(id);
//	}
//
//	@Override
//    public List<FormElement<?,?>> findFormElementsByName(String name) {
//		return super.findFormElementsByName(name);
//	}
//
//	@Override
//    public T removeNodeById(String id) {
//		return super.removeNodeById(id);
//	}
//
//    @Override
//    public boolean replaceNodeById(Widget<?> w) {
//		return super.replaceNodeById(w);
//	}
	

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
    protected void copyProperties(AbstractLayout<?>to,AbstractLayout<?>from){
		super.copyProperties(to, from);
		to.nodes=from.nodes;
	}

	@Override
	public List<HasId<?>> findNodes() {
		return (List<HasId<?>>)nodes;
	}

	@Override
	public HasId<? extends HasId<?>> findNodeById(String id) {
		List<?> nodes = findNodes();
		if (id == null || nodes == null) {
			return null;
		}
		for (Iterator iter = nodes.iterator(); iter.hasNext(); ) {
			Object item = iter.next();

			if (item instanceof Widget && id.equals(((Widget)item).getId())) {
				return (HasId<? extends HasId<?>>) item;
			} else if (item instanceof Container) {
				Container cast = (Container) item;
				HasId<? extends HasId<?>> c = cast.findNodeById(id);
				if (c != null) {
					return c;
				}
			}
		}
		return null;
	}

	@Override
	public T removeNodeById(String id) {
		List nodes = findNodes();
		if (id == null || nodes == null) {
			return (T) this;
		}
		for (Iterator<Widget<?>> iter = nodes.iterator();
			 iter.hasNext(); ) {
			Widget<?> item = iter.next();
			if (id.equals(item.getId())) {
				iter.remove();
			} else if (item instanceof Container) {
				Container cast = (Container) item;
				cast.removeNodeById(id);
			}
		}
		return (T)this;
	}

	@Override
	public boolean replaceNodeById(Widget<?> w) {
		List nodes = findNodes();
		if (w == null || w.getId() == null || nodes == null) {
			return false;
		}
		String id = w.getId();
		for (int i = 0; i < nodes.size(); i++) {
			Object item = nodes.get(i);
			if (item instanceof Widget && id.equals(((Widget)item).getId())) {
				// 替换该元素
				if (onReplace((Widget)item, w)) {
					nodes.set(i, w);
					return true;
				} else {
					return false;
				}
			} else if (item instanceof Container) {
				Container cast = (Container) item;
				boolean replaced = cast.replaceNodeById(w);
				if (replaced) {
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public void clearNodes() {
		List nodes = findNodes();
		if (nodes != null) {
			nodes.clear();
		}
	}

	protected boolean onReplace(Widget<?> oldWidget, Widget<?> newWidget) {
		if (!Utils.isEmpty(oldWidget.getWidth()) &&
				Utils.isEmpty(newWidget.getWidth())) {
			newWidget.setWidth(oldWidget.getWidth());
		}
		if (!Utils.isEmpty(oldWidget.getHeight()) &&
				Utils.isEmpty(newWidget.getHeight())) {
			newWidget.setHeight(oldWidget.getHeight());
		}
		return true;
	}

	/**
	 * 根据表单提交名取得表单元素。可能学多个。
	 * @param name 提交名
	 * @return 本身，这样可以继续设置其他属性
	 */
	public List<FormElement<?, ?>> findFormElementsByName(String name) {
		List<FormElement<?, ?>> result = new ArrayList<FormElement<?, ?>>();
		findFormElementsByName(name, result);
		return result;
	}

	protected void findFormElementsByName(String name, List<FormElement<?, ?>> result) {
		if (name == null || "".equals(name)) {
			return;
		}
		if (this instanceof HiddenContainer<?>) {
			HiddenContainer<?> cast = (HiddenContainer<?>) this;
			if (cast.getHiddens() != null) {
				for (Hidden hidden : cast.getHiddens()) {
					if (name.equals(hidden.getName())) {
						result.add(hidden);
					}
				}
			}
		}
		List nodes = findNodes();
		if (nodes != null) {
			for (Object item : nodes) {
				if (item == null ||!(item instanceof Widget)) {
					continue;
				}
				if (item instanceof FormElement<?, ?>) {
					FormElement<?, ?> cast = (FormElement<?, ?>) item;
					if (name.equals(cast.getName())) {
						result.add(cast);
					}
				} else if (item instanceof AbstractLayout) {
					((AbstractLayout) item).findFormElementsByName(name, result);
				}
			}
		}
	}


}
