package com.rongji.dfish.ui.form;

import com.rongji.dfish.ui.Alignable;
import com.rongji.dfish.ui.MultiContainer;
import com.rongji.dfish.ui.Scrollable;
import com.rongji.dfish.ui.Valignable;
import com.rongji.dfish.ui.Widget;
import com.rongji.dfish.ui.layout.LinearLayout;

/**
 * 与HorzontalPanel用法一样，多了setLabel，可以添加标题，可能有些属性不全，后期补上
 */
@SuppressWarnings("unchecked")
public class FormGroup extends LinearLayout<FormGroup>implements LabelRow<FormGroup>,Scrollable<FormGroup>,Alignable<FormGroup>,Valignable<FormGroup>,MultiContainer<FormGroup,Widget<?>>{
	
	private static final long serialVersionUID = 6045136733949121294L;
	private Boolean star;
	private FormLabel label;
	private Boolean hideLabel;

	@Override
	public FormLabel getLabel() {
		return label;
	}

	@Override
	public FormGroup setLabel(String label) {
		this.label=new FormLabel(label).setWidth(null);
		return this;
	}
	@Override
	public FormGroup setLabel(FormLabel label) {
		this.label=label;
		return this;
	}
	@Deprecated
	public FormGroup setTitle(String label) {
		return setLabel(label);
	}
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
		return "formgroup";
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
	public FormGroup add(int index,Widget<?> w,String width) {
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
	public FormGroup setHideLabel(Boolean hideLabel) {
		this.hideLabel=hideLabel;
		if(hideLabel!=null&&label!=null){
			label.setWidth(hideLabel?"0":null);
		}
		return this;
	}

	@Override
	public Boolean getHideLabel() {
		return hideLabel;
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
	public FormGroup setNotnull(Boolean star) {
		this.star=star;
		return this;
	}

	@Override
	public FormGroup setStar(Boolean star) {
		this.star=star;
		return this;
	}

	@Override
	public Boolean getStar() {
		return star;
	}

//	@Override
//	public HorizontalLayout getPrototype() {
//		HorizontalLayout propotype=new HorizontalLayout(null);
//		BeanUtil.copyPropertiesExact(propotype,this);
//		if(getData()!=null)
//		for(Map.Entry<String, Object>entry:getData().entrySet()){
//			propotype.setData(entry.getKey(), entry.getValue());
//		}
//		if(getOn()!=null)
//		for(Map.Entry<String, String>entry:getOn().entrySet()){
//			propotype.setOn(entry.getKey(), entry.getValue());
//		}
//		if(findNodes()!=null)
//		for(Widget<?>node:findNodes()){
//			propotype.add(node);
//		}
//		return propotype;
//	}


}
