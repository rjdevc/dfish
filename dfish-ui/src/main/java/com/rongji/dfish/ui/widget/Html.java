package com.rongji.dfish.ui.widget;

import com.rongji.dfish.ui.*;

/**
 * Html 组件/面板  用于展示html内容。
 * @author DFish Team
 *
 */
public class Html extends AbstractWidget<Html> implements Scrollable<Html>,HtmlContentHolder<Html>,Alignable<Html>, VAlignable<Html>,HasText<Html> {

	private static final long serialVersionUID = -3447946365229839223L;
	private String align;
	private String vAlign;
	private String text;
	private Boolean scroll;
	private Boolean escape;
	private String thumbWidth;
	private String format;

	/**
	 * 构造函数
	 * @param text html内容。文本支持 &lt;d:wg&gt; 标签。
	 */
	public Html(String text){
		setText(text);
	}


	@Override
    public Boolean getEscape() {
		return this.escape;
	}
	
	@Override
    public Html setEscape(Boolean escape) {
		this.escape = escape;
		return this;
	}

	@Override
    public Boolean getScroll() {
		return scroll;
	}

	@Override
    public Html setScroll(Boolean scroll) {
		this.scroll = scroll;
		return this;
	}

	/**
	 * 单行文本输入框
	 * @return String
	 */
	@Override
    public String getText() {
		return text;
	}

	/**
	 * 单行文本输入框
	 * @param text 显示文本
	 * @return this html内容
	 */
	@Override
    public Html setText(String text) {
		this.text = text;
		return this;
	}
	@Override
    public String getAlign() {
		return align;
	}
	@Override
    public Html setAlign(String align) {
		this.align = align;
		return this;
	}
	@Override
    public String getVAlign() {
		return vAlign;
	}
	@Override
    public Html setVAlign(String vAlign) {
		this.vAlign = vAlign;
		return this;
	}

	/**
	 * 设置内容区域所有图片的最大宽度。点击图片可以预览大图。
	 * @return String
	 */
	public String getThumbWidth() {
		return thumbWidth;
	}
	/**
	 * 设置内容区域所有图片的最大宽度。
	 * @param thumbWidth 图片的最大宽度。
	 * @return this
	 */
	public Html setThumbWidth(String thumbWidth) {
		this.thumbWidth = thumbWidth;
		return this;
	}
	/**
	 * 设置内容区域所有图片的最大宽度。
	 * @param thumbWidth 图片的最大宽度。
	 * @return this
	 */
	public Html setThumbWidth(int thumbWidth) {
    	this.thumbWidth = String.valueOf(thumbWidth);
	    return   this;
    }
	@Override
    public String getFormat() {
		return format;
	}

	@Override
    public Html setFormat(String format) {
		this.format = format;
		return this;
	}
}
