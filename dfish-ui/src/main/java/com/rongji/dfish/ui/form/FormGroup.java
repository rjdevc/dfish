package com.rongji.dfish.ui.form;

import com.rongji.dfish.ui.*;
import com.rongji.dfish.ui.layout.LinearLayout;

/**
 * 与HorzontalPanel用法一样，多了setLabel，可以添加标题，可能有些属性不全，后期补上
 */
public class FormGroup extends LinearLayout<FormGroup>implements LabelRow<FormGroup>,Scrollable<FormGroup>,Alignable<FormGroup>, VAlignable<FormGroup>, MultiNodeContainer<FormGroup,Widget<?>> {
	
	private static final long serialVersionUID = 6045136733949121294L;

	private Label label;
	private Boolean noLabel;

	/**
	 * 构造函数
	 * @param label String
	 */
	public FormGroup(String label) {
		super(null);
		setLabel(label);
	}

	@Override
	public String getType() {
		return "FormGroup";
	}

	@Override
	public Label getLabel() {
		return label;
	}
	@Override
	public FormGroup setLabel(String label) {
		this.label=new Label(label).setWidth(null);
		return this;
	}
	@Override
	public FormGroup setLabel(Label label) {
		this.label=label;
		return this;
	}
	@Deprecated
	public FormGroup setTitle(String label) {
		return setLabel(label);
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
	public FormGroup add(int index, Widget w, String width) {
		if(index<0){
			nodes.add(w);
		}else{
			nodes.add(index,w);
		}
		if(width==null){
			if((!(w instanceof Hidden))&& w.getWidth()==null){
				w.setWidth("*");
			}
		}else if(w instanceof Widget){
	        	w.setWidth(width);
		}
		return this;
	}

	@Override
	public FormGroup setNoLabel(Boolean noLabel) {
		this.noLabel = noLabel;
		if(noLabel !=null&&label!=null){
			label.setWidth(noLabel ?"0":null);
		}
		return this;
	}

	@Override
	public Boolean getNoLabel() {
		return noLabel;
	}
	
}
