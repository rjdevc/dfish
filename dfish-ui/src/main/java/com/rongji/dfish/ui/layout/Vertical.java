package com.rongji.dfish.ui.layout;

import com.rongji.dfish.ui.Widget;
import com.rongji.dfish.ui.form.Hidden;

/**
 * 子节点按垂直方向排列的布局widget。子节点的宽度默认为100%；高度可以设置数字,百分比,*。如果高度设为-1，表示自适应高度。
 * @author DFish Team
 *
 */
public class Vertical extends LinearLayout<Vertical> {

	private static final long serialVersionUID = 51468869447120232L;

	/**
	 * 构造函数
	 * @param id String
	 */
	public Vertical(String id) {
		super(id);
	}
	public Vertical() {
		super(null);
	}

	/**
	 * 添加子面板 一般在布局面板下只能添加可见的元素， 如果添加hidden那么该size将被忽略
	 * 
	 * @param index 位置
	 * @param w Widget
	 * @param height String
	 * @return 本身，这样可以继续设置其他属性
	 */
	@Override
    public Vertical add(int index, Widget w, String height) {
		if (w == null) {
			throw new UnsupportedOperationException("The added widget can not be null.");
		}
		if (w instanceof Hidden) {
			Hidden cast = (Hidden) w;
			return addHidden(cast.getName(), cast.getValue());
		}
		if (index < 0) {
			nodes.add(w);
		} else {
			nodes.add(index, w);
		}
		if (height == null) {
			if (w.getHeight() == null) {
				w.setHeight(getScroll()!=null&&getScroll()?"-1":"*");
			}
		} else if (w instanceof Widget) {
			w.setHeight(height);
		}
		return this;
	}
	

	
}
