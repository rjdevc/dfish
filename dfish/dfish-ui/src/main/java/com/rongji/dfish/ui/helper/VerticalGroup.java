package com.rongji.dfish.ui.helper;

import java.util.Map;

import com.rongji.dfish.base.util.BeanUtil;
import com.rongji.dfish.ui.Alignable;
import com.rongji.dfish.ui.MultiContainer;
import com.rongji.dfish.ui.Scrollable;
import com.rongji.dfish.ui.Valignable;
import com.rongji.dfish.ui.Widget;
import com.rongji.dfish.ui.form.Hidden;
import com.rongji.dfish.ui.form.LabelRow;
import com.rongji.dfish.ui.json.JsonWrapper;
import com.rongji.dfish.ui.layout.LinearLayout;
import com.rongji.dfish.ui.layout.VerticalLayout;

/**
 *  与VerticalPanel用法一样，多了setLabel，可以添加标题，可能有些属性不全，后期补上
 */
public class VerticalGroup extends LinearLayout<VerticalGroup>implements JsonWrapper<VerticalLayout>, LabelRow<VerticalGroup>,Scrollable<VerticalGroup>,Alignable<VerticalGroup>, Valignable<VerticalGroup>,
MultiContainer<VerticalGroup,Widget<?>>{
	private static final long serialVersionUID = 7233516564762156371L;
	private Boolean star;
	private String label;
	private Boolean hideLabel;

	public String getLabel() {
		return label;
	}

	public VerticalGroup setLabel(String label) {
		this.label=label;
		return this;
	}
	@Deprecated
	public VerticalGroup setTitle(String label) {
		this.label=label;
		return this;
	}
	/**
	 * @param id
	 */
	public VerticalGroup(String id) {
		super(id);
	}

	public String getType() {
		return "vert";
	}


	/**
	 * 添加子面板 一般在布局面板下只能添加可见的元素， 如果添加hidden那么该size将被忽略
	 * 
	 * @param index 位置
	 * @param w Widget
	 * @param height String
	 * @return 本身，这样可以继续设置其他属性
	 */
	public VerticalGroup add(int index,Widget<?> w,String height) {
		if(index<0){
			nodes.add(w);
		}else{
			nodes.add(index,w);
		}
		if(height==null){
			if((!(w instanceof Hidden))&& w.getHeight()==null){
				w.setHeight("*");
			}
		}else if(w instanceof Widget){
	        	w.setHeight(height);
		}
		return this;
	}


	@Override
	public VerticalGroup setHideLabel(Boolean hideLabel) {
		this.hideLabel=hideLabel;
		return this;
	}

	@Override
	public Boolean getHideLabel() {
		return hideLabel;
	}

	/**
     * 这个元素，是否非空，这里非空不是dfish2.4以后的真实判断，而是在界面显示一个非空标记
     * 通常是红色字体的半角星号(*)
     * @param notnull boolean
     * @return 本身，这样可以继续设置其他属性
     * @since 3.0
     * @deprecated notnull 属性已经被 required替代
     */
	@Deprecated
	public VerticalGroup setNotnull(Boolean notnull) {
		this.star=notnull;
		return this;
	}

	@Override
	public VerticalGroup setStar(Boolean star) {
		this.star=star;
		return this;
	}

	@Override
	public Boolean getStar() {
		return star;
	}

	@Override
	public VerticalLayout getPrototype() {
	
		VerticalLayout propotype=new VerticalLayout(null);
		BeanUtil.copyPropertiesExact(propotype,this);
		if(getData()!=null)
		for(Map.Entry<String, Object>entry:getData().entrySet()){
			propotype.setData(entry.getKey(), entry.getValue());
		}
		if(getOn()!=null)
		for(Map.Entry<String, String>entry:getOn().entrySet()){
			propotype.setOn(entry.getKey(), entry.getValue());
		}
		if(findNodes()!=null)
		for(Widget<?>node:findNodes()){
			propotype.add(node);
		}
		return propotype;
	}


}
