package com.rongji.dfish.ui.layout;

import java.util.List;

import com.rongji.dfish.ui.HtmlContentHolder;
import com.rongji.dfish.ui.MultiContainer;
import com.rongji.dfish.ui.PubHolder;
import com.rongji.dfish.ui.Scrollable;
import com.rongji.dfish.ui.widget.Img;

/**
 * 图片平铺
 * @author DFish Team
 *
 */
public class AlbumLayout extends AbstractLayout<AlbumLayout,Img> implements HtmlContentHolder<AlbumLayout>, Scrollable<AlbumLayout>,ListView<AlbumLayout>,PubHolder<AlbumLayout,Img>,
MultiContainer<AlbumLayout,Img>{
	/**
	 * 
	 */
	private static final long serialVersionUID = 7141941441960631331L;
	private Integer space;
//	private Boolean focusable;
	private Boolean focusmultiple;
//	private Boolean hoverable;
	private Img pub;
	private Boolean scroll;
	private String scrollClass;
//	private Boolean escape;
//	private Boolean format;
	private String face;
	private Boolean nobr;

	
	/**
	 * 皮肤-默认
	 */
	public static final String FACE_NONE = "none";
	/**
	 * 皮肤-平铺
	 */
	public static final String FACE_STRAIGHT = "straight";

	/**
	 * 构造函数
	 * @param id String
	 */
	public AlbumLayout(String id) {
		super(id);
	}

	@Override
	public String getType() {
		return "album";
	}

	@Override
	public AlbumLayout setScroll(Boolean scroll) {
		this.scroll=scroll;
		return this;
	}

	@Override
	public Boolean getScroll() {
		return scroll;
	}

	@Override
	public AlbumLayout setScrollClass(String scrollClass) {
		this.scrollClass=scrollClass;
		return this;
	}

	@Override
	public String getScrollClass() {
		return scrollClass;
	}

	/**
	 * 图片之间的间隔。
	 * @return space
	 */
	public Integer getSpace() {
		return space;
	}

	/**
	 * 图片之间的间隔。
	 * @param space 间距(像素)
	 * @return 本身，这样可以继续设置其他属性
	 */
	public AlbumLayout setSpace(Integer space) {
		this.space = space;
		return this;
	}


	@Deprecated
	public AlbumLayout setFocusable(Boolean focusable) {
//		this.focusable = focusable;
		getPub().setFocusable(focusable);
		return this;
	}

	@Override
    public Boolean getFocusmultiple() {
		return focusmultiple;
	}

	@Override
    public AlbumLayout setFocusmultiple(Boolean focusmultiple) {
		this.focusmultiple = focusmultiple;
		return this;
	}

	@Override
    @Deprecated
	public Boolean getHoverable() {
		return null;
	}

	@Override
    @Deprecated
	public AlbumLayout setHoverable(Boolean hoverable) {
//		this.hoverable = hoverable;
		return this;
	}

	/**
	 * 子节点的默认配置项
	 * @return pub
	 */
	@Override
    public Img getPub() {
		if (pub == null) {
			pub = new Img(null);
		}
		return pub;
	}

	/**
	 * 子节点的默认配置项
	 * @param pub 默认配置项
	 * @return 本身，这样可以继续设置其他属性
	 */
	@Override
    public AlbumLayout setPub(Img pub) {
		this.pub = pub;
		return this;
	}
	
	/**
	 * 是否对html内容转义
	 * @return escape
     * @see #getPub()
	 */
	@Override
    @Deprecated
	public Boolean getEscape() {
		return getPub().getEscape();
	}

	/**
	 * 是否对html内容转义
	 * @param escape Boolean
	 * @return 本身，这样可以继续设置其他属性
     * @see #getPub()
	 */
	@Override
    @Deprecated
	public AlbumLayout setEscape(Boolean escape) {
		getPub().setEscape(escape);
		return this;
	}

//	/**
//	 * 格式化内容。"$字段名"形式的变量将被解析替换。支持"javascript:"开头的js语句(需return返回值)。
//	 * @return format
//	 */
//	public Boolean getFormat() {
//		return format;
//	}
//
//	/**
//	 * 格式化内容。"$字段名"形式的变量将被解析替换。支持"javascript:"开头的js语句(需return返回值)。
//	 * @param format 格式化内容
//	 * @return 本身，这样可以继续设置其他属性
//	 */
//	public AlbumLayout setFormat(Boolean format) {
//		this.format = format;
//		return this;
//	}

	@Override
	public List<Img> getNodes() {
		return nodes;
	}

	/**
	 * 图片皮肤
	 * @return face 皮肤
	 */
	public String getFace() {
		return face;
	}

	/**
	 * 图片皮肤
	 * @param face 图片皮肤
	 * @return 本身，这样可以继续设置其他属性
	 */
	public AlbumLayout setFace(String face) {
		this.face = face;
		return this;
	}


	@Override
	public AlbumLayout setNobr(Boolean nobr) {
		this.nobr=nobr;
		return this;
	}

	@Override
	public Boolean getNobr() {
		return nobr;
	}
	  /**
	  * 在指定的位置添加子面板
	  * @param index 位置
	  * @param img  Img
	  * @return 本身，这样可以继续设置其他属性
	  */
		public AlbumLayout add(int index, Img img) {
	     if (img == null) {
	         return  this;
	     }
	     if(index<0){
	     	nodes.add(img);
	     }else{
	     	nodes.add(index, img);
	     }
	     return this;
	 }
}
