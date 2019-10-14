package com.rongji.dfish.ui.layout;

import com.rongji.dfish.ui.Widget;
import com.rongji.dfish.ui.form.Hidden;

/**
 * 子节点按水平方向排列的布局widget。子节点的高度默认为100%；宽度可以设置数字,百分比,*。如果宽度设为-1，表示自适应宽度。
 * @author DFish Team
 *
 */
public class Horizontal extends LinearLayout<Horizontal>{
	/**
	 * 
	 */
	private static final long serialVersionUID = 7197719640365003017L;
	private Boolean nobr;
	/**
	 * 当内容太多的时候不换行
	 * @return Boolean
	 */
	public Boolean getNobr() {
		return nobr;
	}
	/**
	 * 当内容太多的时候不换行
	 * @param nobr Boolean
	 * @return this
	 */
	public Horizontal setNobr(Boolean nobr) {
		this.nobr = nobr;
		return this;
	}

	/**
	 * 构造函数
	 * @param id String
	 */
	public Horizontal(String id) {
		super(id);
	}

	@Override
    public String getType() {
		return "horizontal";
	}

	/**
	 * 添加子面板 一般在布局面板下只能添加可见的元素， 如果添加hidden那么该size将被忽略
	 * 
	 * @param index 位置
	 * @param w Widget
	 * @param width String
	 * @return 本身，这样可以继续设置其他属性
	 */
	@Override
    public Horizontal add(int index, Widget<?> w, String width) {
		if (w == null) {
			throw new UnsupportedOperationException("The added widget can not be null.");
		}
		if (w instanceof Hidden) {
			Hidden cast = (Hidden) w;
			return addHidden(cast.getName(), cast.getValue());
		}
		if(index<0){
			nodes.add(w);
		}else{
			nodes.add(index,w);
		}
		if(width==null){
			if(w.getWidth()==null){
				w.setWidth("*");
			}
		}else if(w instanceof Widget){
	        w.setWidth(width);
		}
		return this;
	}

}
