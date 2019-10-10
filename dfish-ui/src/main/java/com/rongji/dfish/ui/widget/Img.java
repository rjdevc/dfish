package com.rongji.dfish.ui.widget;

import com.rongji.dfish.ui.*;
import com.rongji.dfish.ui.form.AbstractBox;

/**
 * img 图片
 * @author DFish Team
 *
 */
public class Img extends AbstractWidget<Img> implements Alignable<Img>,Valignable<Img>,HtmlContentHolder<Img>, HasFormat<Img> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 672057364742087182L;
	/**
	 * 默认皮肤
	 */
	public static final String FACE_NONE="none";
	/**
	 * 当 album face="straight" 时会显示说明(description)。
	 */
	public static final String FACE_STRAIGHT="straight";
	
	private AbstractBox<?> box;
	private Boolean nobr;
	private String text;
	private String src;
	private Boolean focus;
	private Boolean focusable;
	private String align;
	private String valign;
	private Object tip;
	private String description;
	private Integer textwidth;
	private String face;
	private String imgwidth;
	private String imgheight;
	private String format;
	private Boolean escape;

	/**
     * 构造函数
     * @param src String 图标
     */
	public Img(String src) {
		this.setSrc(src);
	}
	
	/**
     * 构造函数
     * @param src String 图标
     * @param text String 显示文本
     */
	public Img(String src, String text) {
		super();
		this.setSrc(src);
		this.setText(text);
	}

	@Override
	public String getType() {
		return "img";
	}
	/**
	 *  选项表单，类型是 checkbox 或 radio。取消或勾选这个box，将同步fieldset内部所有表单的状态。
	 * @return box
	 */
	public AbstractBox<?> getBox() {
		return box;
	}
	/**
	 *  选项表单，类型是 checkbox 或 radio。取消或勾选这个box，将同步fieldset内部所有表单的状态。
	 * @param box 附带的选项表单
	 * @return 本身，这样可以继续设置其他属性
	 */
	public Img setBox(AbstractBox<?> box) {
		this.box = box;
		return this;
	}
	/**
	 * 文本是否换行。
	 * @return nobr 
	 */
	public Boolean getNobr() {
		return nobr;
	}
	/**
	 * 文本是否换行。
	 * @param nobr Boolean
	 * @return 本身，这样可以继续设置其他属性
	 */
	public Img setNobr(Boolean nobr) {
		this.nobr = nobr;
		return this;
	}
	/**
	 * 是否有滚动条。
	 * @return text 显示文本
	 */
	public String getText() {
		return text;
	}
	/**
	 * 是否有滚动条。
	 * @param text 显示文本
	 * @return 本身，这样可以继续设置其他属性
	 */
	public Img setText(String text) {
		this.text = text;
		return this;
	}
	/**
	 * 图片地址。
	 * @return src
	 */
	public String getSrc() {
		return src;
	}
	/**
	 * 图片地址。
	 * @param src String
	 * @return 本身，这样可以继续设置其他属性
	 */ 
	public Img setSrc(String src) {
		this.src = src;
		return this;
	}
	/**
	 * 是否焦点状态。
	 * @return Boolean
	 */
	public Boolean getFocus() {
		return focus;
	}
	/**
	 * 是否焦点状态。
	 * @param focus Boolean
	 * @return 本身，这样可以继续设置其他属性
	 */
	public Img setFocus(Boolean focus) {
		this.focus = focus;
		return this;
	}

	/**
	 * 是否可聚焦
	 * @return Boolean
	 */
	public Boolean getFocusable() {
		return focusable;
	}

	/**
	 * 设置是否可聚焦
	 * @param focusable Boolean
	 * @return 本身，这样可以继续设置其他属性
	 */
	public Img setFocusable(Boolean focusable) {
		this.focusable = focusable;
		return this;
	}

	/**
	 * 水平对齐方式
	 * @return String
	 */
	public String getAlign() {
		return align;
	}

	/**
	 * 水平对齐方式
	 * @param align String
	 * @return 本身，这样可以继续设置其他属性
	 */
	public Img setAlign(String align) {
		this.align = align;
		return this;
	}

	/**
	 * 垂直对齐方式
	 */
	public String getValign() {
		return valign;
	}

	/**
	 * 垂直对齐方式
	 */
	public Img setValign(String valign) {
		this.valign = valign;
		return this;
	}

	/**
	 * 提示
	 * @return String
	 */
	public Object getTip() {
		return tip;
	}

	/**
	 * 提示
	 * @param tip String 
	 * @return 本身，这样可以继续设置其他属性
	 */
	public Img setTip(Boolean tip) {
		this.tip = tip;
		return this;
	}
	
	/**
	 * 提示
	 * @param tip String 
	 * @return 本身，这样可以继续设置其他属性
	 */
	public Img setTip(String tip) {
		this.tip = tip;
		return this;
	}
	
	/**
	 * 图片描述
	 * @return description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * 图片描述
	 * @param description 图片描述
	 * @return 本身，这样可以继续设置其他属性
	 */
	public Img setDescription(String description) {
		this.description = description;
		return this;
	}

	/**
	 * 文本宽度
	 * @return textwidth
	 */
	public Integer getTextwidth() {
		return textwidth;
	}

	/**
	 * 文本宽度
	 * @param textwidth 文本宽度
	 * @return 本身，这样可以继续设置其他属性
	 */
	public Img setTextwidth(Integer textwidth) {
		this.textwidth = textwidth;
		return this;
	}
	
	/**
	 * 图片展现方式。可选值: "none","straight"。默认值为"none"。
	 * @return String
	 */
	public String getFace() {
		return face;
	}

	/**
	 * 图片展现方式。可选值: "none","straight"。默认值为"none"。
	 * @param face String
	 * @return 本身，这样可以继续设置其他属性
	 */
	public Img setFace(String face) {
		this.face = face;
		return this;
	}

	/**
	 * 图片宽度。
	 * @return Integer
	 */
	public String getImgwidth() {
		return imgwidth;
	}
	/**
	 * 图片宽度。
	 * @param imgwidth Integer
	 * @return 本身，这样可以继续设置其他属性
	 */
	public Img setImgwidth(Integer imgwidth) {
		this.imgwidth = String.valueOf(imgwidth);
		return this;
	}
	/**
	 *  图片宽度。
	 * @param imgwidth String
	 * @return 本身，这样可以继续设置其他属性
	 */
	public Img setImgwidth(String imgwidth) {
		this.imgwidth = imgwidth;
		return this;
	}
	/**
	 * 图片高度。
	 * @return Integer
	 */
	public String getImgheight() {
		return imgheight;
	}
	/**
	 * 图片高度。
	 * @param imgheight Integer
	 * @return 本身，这样可以继续设置其他属性
	 */
	public Img setImgheight(Integer imgheight) {
		this.imgheight = String.valueOf(imgheight);
		return this;
	}
	/**
	 * 图片高度。
	 * @param imgheight String
	 * @return 本身，这样可以继续设置其他属性
	 */
	public Img setImgheight(String imgheight) {
		this.imgheight = imgheight;
		return this;
	}

	public String getFormat() {
		return format;
	}

	public Img setFormat(String format) {
		this.format = format;
		return this;
	}

	/**
	 * 用于显示文本是否需要转义,不设置默认是true
	 * @return Boolean
	 */
	public Boolean getEscape() {
		return escape;
	}

	/**
	 * 用于显示文本是否需要转义,不设置默认是true
	 * @param escape Boolean
	 * @return 本身，这样可以继续设置其他属性
	 */
	public Img setEscape(Boolean escape) {
		this.escape = escape;
		return this;
	}
}
