package com.rongji.dfish.ui.form;

import com.rongji.dfish.base.Option;

import java.util.List;



/**
 * Imgbox 图片下拉选择表单
 * @author DFish Team
 * @version 1.2
 * @since 1.0
 */
public class ImgBox extends AbstractOptionsHolder<ImgBox, Option> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 666601483514746346L;
	private Integer imgheight;
	private Integer imgwidth;

	/**
	 * 构造函数
	 * @param name 表单元素名
	 * @param label 标题
	 * @param value 选中的值
	 * @param options 候选项
	 */
	public ImgBox(String name, String label, Object value, List<?> options) {
		super(name, label, value, options);
	}

	@Override
	protected Option buildOption(Option o) {
		return o;
	}

	@Override
    public String getType() {
		return "imgbox";
	}

	/**
	 * 图标高度
	 * @return Integer
	 */
	public Integer getImgheight() {
		return imgheight;
	}

	/**
	 * 设置图标高度
	 * @param imgheight Integer
	 * @return 本身，这样可以继续设置其他属性
	 */
	public ImgBox setImgheight(Integer imgheight) {
		this.imgheight = imgheight;
		return this;
	}

	/**
	 * 图标宽度
	 * @return Integer
	 */
	public Integer getImgwidth() {
		return imgwidth;
	}

	/**
	 * 设置图标宽度
	 * @param imgwidth Integer
	 * @return 本身，这样可以继续设置其他属性
	 */
	public ImgBox setImgwidth(Integer imgwidth) {
		this.imgwidth = imgwidth;
		return this;
	}
	
	@Override
    @Deprecated
	public ImgBox setTip(Boolean tip) {
		return super.setTip(tip);
	}
	
	@Override
    @Deprecated
	public ImgBox setTip(String tip) {
		return super.setTip(tip);
	}
	
}
