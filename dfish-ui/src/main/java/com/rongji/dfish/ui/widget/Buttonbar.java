package com.rongji.dfish.ui.widget;

import java.util.List;

import com.rongji.dfish.base.util.Utils;
import com.rongji.dfish.ui.Alignable;
import com.rongji.dfish.ui.Directional;
import com.rongji.dfish.ui.MultiContainer;
import com.rongji.dfish.ui.PubHolder;
import com.rongji.dfish.ui.Valignable;
import com.rongji.dfish.ui.Widget;
import com.rongji.dfish.ui.layout.AbstractLayout;

/**
 * button 的父类。 Alignable
 * 
 */
// FIXME ButtonBar的实现是HorizonalLayout,java端概念是否和js端统一大
public class Buttonbar extends AbstractLayout<Buttonbar, Widget<?>> implements PubHolder<Buttonbar, Button>,
        Alignable<Buttonbar>, Valignable<Buttonbar>, MultiContainer<Buttonbar, Widget<?>>, Directional<Buttonbar> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5193505708325695202L;

	private String dir;
	private Boolean focusmultiple;
	private Button pub;
	private Integer space;
	private String align;
	private String valign;
	private Split split;
	private Boolean nobr;
	private Boolean scroll;
	private String scrollClass;
	private Overflow overflow;

	/**
	 * 当按钮过多，放不下的时候的效果
	 * @return Overflow
	 */
	public Overflow getOverflow() {
		return overflow;
	}
	/**
	 * 当按钮过多，放不下的时候，的效果
	 * @param overflow 设置当内容太多的时候不换行
	 * @return this
	 */
	public Buttonbar setOverflow(Overflow overflow) {
		this.overflow = overflow;
		return this;
	}
	/**
	 * 当内容太多的时候不换行
	 * @return Boolean
	 */
	public Boolean getNobr() {
		return nobr;
	}
	/**
	 * 设置
	 * @param nobr 设置当内容太多的时候不换行
	 * @return 本身，这样可以继续设置其他属性
	 */
	public Buttonbar setNobr(Boolean nobr) {
		this.nobr = nobr;
		return this;
	}

	/**
	 * 按钮排列方向。可用值: h(横向,默认),v(纵向)
	 * 
	 * @return dir
	 */
	@Override
    public String getDir() {
		return dir;
	}

	/**
	 * 按钮排列方向。可用值: h(横向,默认),v(纵向)
	 * 
	 * @param dir h/v
	 * @return 本身，这样可以继续设置其他属性
	 */
	@Override
    public Buttonbar setDir(String dir) {
		this.dir = dir;
		return this;
	}


	/**
	 * 设置为 true，按钮点击后转为焦点状态(按钮增加焦点样式 .z-on )
	 * 
	 * @param focusable Boolean
	 * @return 本身，这样可以继续设置其他属性
	 */
	@Deprecated
	public Buttonbar setFocusable(Boolean focusable) {
		getPub().setFocusable(true);
		return this;
	}

	/**
	 * 是否有多个按钮可同时设为焦点状态。
	 * 
	 * @return focusmultiple
	 */
	public Boolean getFocusmultiple() {
		return focusmultiple;
	}

	/**
	 * 是否有多个按钮可同时设为焦点状态。
	 * 
	 * @param focusmultiple Boolean
	 * @return 本身，这样可以继续设置其他属性
	 */
	public Buttonbar setFocusmultiple(Boolean focusmultiple) {
		this.focusmultiple = focusmultiple;
		return this;
	}

	/**
	 * 按钮之间的间隔。
	 * 
	 * @return space
	 */
	public Integer getSpace() {
		return space;
	}

	/**
	 * 按钮之间的间隔。
	 * 
	 * @param space 间距 (像素)
	 * @return 本身，这样可以继续设置其他属性
	 */
	public Buttonbar setSpace(Integer space) {
		this.space = space;
		return this;
	}

	@Override
    public Buttonbar setPub(Button pub) {
		this.pub = pub;
		return this;
	}

	/**
	 * 构造函数
	 * @param id String
	 */
	public Buttonbar(String id) {
		super(id);
	}

	@Override
    public String getType() {
		return "buttonbar";
	}

	/**
	 * 添加分隔符，默认添加2像素的分隔符
	 * 
	 * @return 本身，这样可以继续设置其他属性
	 * @deprecated 使用 {@link #add(Widget)}
	 */
	@Deprecated
	public Buttonbar addSplit() {
		nodes.add(new Split().setWidth("2"));
		return this;
	}

	@Override
    public Button getPub() {
		if (pub == null) {
			pub = new Button(null);
		}
		return pub;
	}

	/**
	 * 设置默认样式
	 * 
	 * @param face
	 *            String
	 * @return 本身，这样可以继续设置其他属性
	 * @deprecated 现在一般是setPub(new Button(null).setCls("xxx"));
	 */
	@Deprecated
	public Buttonbar setFace(String face) {
		// FIXME 这里face和styleClass冲了,该如何定义? 是否将这个方法去除
		if (Utils.isEmpty(face)) {
			return this;
		}
		if (pub == null) {
			setPub(new Button(null));
		}
		getPub().setCls(face);
		return this;
	}

	@Override
	public String getValign() {
		return valign;
	}

	@Override
	public Buttonbar setValign(String valign) {
		this.valign = valign;
		return this;
	}

	@Override
	public String getAlign() {
		return align;
	}

	@Override
	public Buttonbar setAlign(String align) {
		this.align = align;
		return this;
	}

	@Override
	public List<Widget<?>> getNodes() {
		return nodes;
	}

	/**
	 * 在按钮之间默认插入的split
	 * @return Split
	 */
	public Split getSplit() {
		if (split == null) {
			split = new Split();
		}
		return split;
	}
	
	/**
	 * 在按钮之间默认插入的split;
	 * 设置了该属性,无需调用{@link #add(Widget)}来添加split,按钮间自动添加1个split
	 * @param split Split
	 * @return this
	 */
	public Buttonbar setSplit(Split split) {
		this.split = split;
		return this;
	}
	
	/**
	 * 
	 * <p>描述:是否滚动</p>
	 * @return Boolean
	 */
	public Boolean getScroll() {
		return scroll;
	}
	
	/**
	 * 
	 * <p>描述:设置是否滚动</p>
	 * @param scroll Boolean
	 * @return 本身，以便继续设置属性
	 */
	public Buttonbar setScroll(Boolean scroll) {
		this.scroll = scroll;
		return this;
	}
	
	/**
	 * 
	 * <p>描述:滚动条样式</p>
	 * @return String
	 */
	public String getScrollClass() {
		return scrollClass;
	}
	
	/**
	 * 
	 * <p>描述:设置滚动条样式</p>
	 * @param scrollClass String
	 * @return 本身，以便继续设置属性
	 */
	public Buttonbar setScrollClass(String scrollClass) {
		this.scrollClass = scrollClass;
		return this;
	}

	  /**
	  * 在指定的位置添加子面板
	  * @param index 位置
	  * @param w  N
	  * @return 本身，这样可以继续设置其他属性
	  */
		public Buttonbar add(int index, Widget<?> w) {
	     if (w == null) {
	         return  this;
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
	     return this;
	 }
}
