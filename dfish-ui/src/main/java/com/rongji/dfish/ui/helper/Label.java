package com.rongji.dfish.ui.helper;

import com.rongji.dfish.base.util.BeanUtil;
import com.rongji.dfish.ui.AbstractWidget;
import com.rongji.dfish.ui.HasText;
import com.rongji.dfish.ui.HtmlContentHolder;
import com.rongji.dfish.ui.form.LabelRow;
import com.rongji.dfish.ui.json.JsonWrapper;
import com.rongji.dfish.ui.widget.Html;

/**
 * Label 是用于表单中的标签。
 * <p>Label 本身并不是一个实体元素，而是一个封装类，他的实现是一个Html。为了能够在表单中更加方便的调用，封装了label属性。</p>
 * <pre style='border:1px black solid;border-left:0px;border-right:0px;background-color:#CCC'>
 * FormPanel form=new FormPanel("f_form");
 * form.add(new Text("id","编号","123",100));
 * form.add(new Label("备注","&lt;A HERF='#'&gt;查看详情&lt;/A&gt;"));
 * </pre>
 * @author DFish Team
 *
 */
public class Label extends AbstractWidget<Label> implements JsonWrapper<Html>,LabelRow<Label>,
	HtmlContentHolder<Label>,HasText<Label>{

	private static final long serialVersionUID = 2082708957092349423L;
	private String text;
	private String label;
	private Boolean star;
//	private Boolean hidden;
	private Boolean hideLabel;
	private Boolean escape;


	
	/**
	 * 构造函数
	 * @param label 标题
	 * @param text 标签内容
	 */
	public Label(String label, String text){
		this.label=label;
		this.text=text;
		bundleProperties();
	}
	
	@Override
	public Html getPrototype() {
		Html prototype = new Html(text);
		BeanUtil.copyPropertiesExact(prototype,this);
		return prototype;
	}

	public String getLabel() {
		return label;
	}

	public Boolean getHideLabel() {
		return hideLabel;
	}

	public Label setHideLabel(Boolean hideLabel) {
		this.hideLabel=hideLabel;
		return this;
	}

	 /**
     * 这个元素，是否非空，这里非空不是dfish2.4以后的真实判断，而是在界面显示一个非空标记
     * 通常是红色字体的半角星号(*)
     * @param star boolean
     * @return 本身，这样可以继续设置其他属性
     * @since 3.0
     * @deprecated notnull 属性已经被 required替代
     */
	@Deprecated
	public Label setNotnull(Boolean star) {
		this.star=star;
		return this;
	}

	public Label setLabel(String label) {
		this.label=label;
		return this;
	}
	
	@Deprecated
	public Label setTitle(String label) {
		this.label=label;
		return this;
	}

	public String getType() {
		return null;
	}

	/**
	 * 显示文本
	 * @return String
	 */
	public String getText() {
		return text;
	}

	/**
	 * 显示文本
	 * @param text String
	 * @return 本身，这样可以继续设置其他属性
	 */
	public Label setText(String text) {
		this.text = text;
		return this;
	}

	public Boolean getEscape() {
		return this.escape;
	}
	
	public Label setEscape(Boolean escape) {
		this.escape = escape;
		return this;
	}
	
	public Label setStar(Boolean star) {
		this.star=star;
		return this;
	}
	
	public Boolean getStar() {
		return star;
	}
	

}