package com.rongji.dfish.ui.widget;

import com.rongji.dfish.ui.*;

/**
 * Html 组件/面板  用于展示html内容。
 * @author DFish Team
 *
 */
public class Html extends AbstractWidget<Html> implements Scrollable<Html>,HtmlContentHolder<Html>,Alignable<Html>,Valignable<Html>,HasFormat<Html> {

	private static final long serialVersionUID = -3447946365229839223L;
	private String align;
	private String valign;
	private String text;
	private Boolean scroll;
	private String scrollClass;
	private Boolean escape;
	private String thumbwidth;
	private String format;
	/**
	 * 构造函数
	 * @param id  自定义的ID。可通过 view.find( id ) 方法来获取 widget。
	 * @param text html内容。文本支持 &lt;d:wg&gt; 标签。
	 */
	public Html(String id,String text){
		super();
		this.id=id;
		this.text=text;
	}
	/**
	 * 构造函数
	 * @param text html内容。文本支持 &lt;d:wg&gt; 标签。
	 */
	public Html(String text){
		this(null, text);
	}


	public String getType() {
		return "html";
	}

	/**
	 * 设置文本
	 * @param text 显示文本
	 * @return this
	 * @deprecated 已过时，使用 {@link #setText(String)}替代
	 */
	@Deprecated 
	public Html setHtml(String text) {
		this.text = text;
		return this;
	}

	public Boolean getEscape() {
		return this.escape;
	}
	
	public Html setEscape(Boolean escape) {
		this.escape = escape;
		return this;
	}

	public Boolean getScroll() {
		return scroll;
	}

	public Html setScroll(Boolean scroll) {
		this.scroll = scroll;
		return this;
	}

	public String getScrollClass() {
		return scrollClass;
	}
	
	public Html setScrollClass(String scrollClass) {
		this.scrollClass = scrollClass;
		return this;
	}
	
	/**
	 * 单行文本输入框
	 * @return String
	 */
	public String getText() {
		return text;
	}

	/**
	 * 单行文本输入框
	 * @param text 显示文本
	 * @return this html内容
	 */
	public Html setText(String text) {
		this.text = text;
		return this;
	}
	public String getAlign() {
		return align;
	}
	public Html setAlign(String align) {
		this.align = align;
		return this;
	}
	public String getValign() {
		return valign;
	}
	public Html setValign(String valign) {
		this.valign = valign;
		return this;
	}
	
	/**
	 * 设置内容区域所有图片的最大宽度。点击图片可以预览大图。
	 * @return String
	 */
	public String getThumbwidth () {
		return thumbwidth ;
	}
	/**
	 * 设置内容区域所有图片的最大宽度。
	 * @param thumbwidth 图片的最大宽度。
	 * @return this
	 */
	public Html setThumbwidth(String thumbwidth) {
		this.thumbwidth = thumbwidth;
		return this;
	}
	/**
	 * 设置内容区域所有图片的最大宽度。
	 * @param thumbwidth 图片的最大宽度。
	 * @return this
	 */
	public Html setThumbwidth(int thumbwidth) {
    	this.thumbwidth = String.valueOf(thumbwidth);
	    return   this;
    }
	public String getFormat() {
		return format;
	}

	public Html setFormat(String format) {
		this.format = format;
		return this;
	}
}
