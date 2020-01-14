package com.rongji.dfish.ui.form;

import com.rongji.dfish.base.util.BeanUtil;
import com.rongji.dfish.ui.AbstractWidget;
import com.rongji.dfish.ui.HasText;
import com.rongji.dfish.ui.HtmlContentHolder;
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
 */
public class LabelWrapper extends AbstractWidget<LabelWrapper> implements JsonWrapper<FormGroup>,LabelRow<LabelWrapper>,
	HtmlContentHolder<LabelWrapper>,HasText<LabelWrapper>{

	private static final long serialVersionUID = 2082708957092349423L;
	private String text;
	private Label label;
	private Boolean hideLabel;
	private Boolean escape = true;

	
	/**
	 * 构造函数
	 * @param label 标题
	 * @param text 标签内容
	 */
	public LabelWrapper(String label, String text){
		this.label=new Label(label).setWidth(null);
		this.text=text;
		bundleProperties();
	}

	@Override
	public FormGroup getPrototype() {
		Html prototype = new Html(text);
		BeanUtil.copyPropertiesExact(prototype,this);
		prototype.setId(null);
		FormGroup hg=new FormGroup(null).setId(this.getId());
		hg.setLabel(label);
		hg.add(prototype);
		return hg;
	}

	@Override
    public Label getLabel() {
		return label;
	}

	@Override
    public Boolean getNoLabel() {
		return hideLabel;
	}

	@Override
    public LabelWrapper setNoLabel(Boolean noLabel) {
		this.hideLabel= noLabel;
		if(noLabel !=null&&label!=null){
			label.setWidth(noLabel ?"0":null);
		}
		return this;
	}

	@Override
    public LabelWrapper setLabel(String label) {
		this.label=new Label(label);
		return this;
	}
	
	@Override
    public LabelWrapper setLabel(Label label) {
		this.label= label;
		return this;
	}
	
	@Deprecated
	public LabelWrapper setTitle(String label) {
		return this.setLabel(label);
	}

	@Override
    public String getType() {
		return null;
	}

	/**
	 * 显示文本
	 * @return String
	 */
	@Override
    public String getText() {
		return text;
	}

	/**
	 * 显示文本
	 * @param text String
	 * @return 本身，这样可以继续设置其他属性
	 */
	@Override
    public LabelWrapper setText(String text) {
		this.text = text;
		return this;
	}

	@Override
    public Boolean getEscape() {
		return this.escape;
	}
	
	@Override
    public LabelWrapper setEscape(Boolean escape) {
		this.escape = escape;
		return this;
	}

}
