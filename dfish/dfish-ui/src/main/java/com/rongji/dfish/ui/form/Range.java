package com.rongji.dfish.ui.form;

import java.beans.Transient;
import java.util.ArrayList;
import java.util.List;

import com.rongji.dfish.ui.FormElement;
import com.rongji.dfish.ui.MultiContainer;
import com.rongji.dfish.ui.Widget;
import com.rongji.dfish.ui.layout.AbstractLayout;

/**
 * 指定范围的表单组合，实际上就是一个容器，里面可以放置begin和end两个表单
 * @version 1.0
 * @since Period
 * @author DFish Team
 *
 */
public class Range extends AbstractLayout<Range,FormElement<?, ?>> implements LabelRow<Range>,MultiContainer<Range,FormElement<?, ?>>{
	/**
	 * 
	 */
	private static final long serialVersionUID = -4525721180514710555L;
	private FormLabel label;
	private FormElement<?, ?> begin;
	private FormElement<?, ?> end;
	private Boolean hideLabel;
	private Boolean star;
	private Object to;
	
	/**
	 * 构造函数
	 * @param label String 标签名
	 * @param begin FormElement 范围的开始表单
	 * @param end FormElement 范围的结束表单
	 */
	public Range(String label, FormElement<?, ?> begin, FormElement<?, ?> end) {
		super(null);
		this.setLabel(label);
		this.begin=begin;
		this.end=end;
	}
	/**
	 * 范围的开始表单
	 * @return FormElement
	 */
	public FormElement<?, ?> getBegin() {
		return begin;
	}
	/**
	 * 设置范围的开始表单
	 * @param begin FormElement
	 * @return this
	 */
	public Range setBegin(FormElement<?, ?> begin) {
		this.begin = begin;
		return this;
	}
	/**
	 * 范围的结束表单
	 * @return FormElement
	 */
	public FormElement<?, ?> getEnd() {
		return end;
	}
	/**
	 * 设置范围的结束表单
	 * @param end FormElement
	 * @return this
	 */
	public Range setEnd(FormElement<?, ?> end) {
		this.end = end;
		return this;
	}
	
	@Override
	public String getType() {
		return "range";
	}

	@Override
	public Range setLabel(String label) {
		this.label=new FormLabel(label);
		return this;
	}
	@Override
	public Range setLabel(FormLabel label) {
		this.label=label;
		return this;
	}
	
	@Deprecated
	public Range setTitle(String label) {
		return setLabel(label);
	}

	@Override
	public FormLabel getLabel() {
		return label;
	}

	@Override
	public Range setHideLabel(Boolean hideLabel) {
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
     * @param notnull boolean
     * @return 本身，这样可以继续设置其他属性
     * @since 3.0
     * @deprecated notnull 属性已经被 required替代
     */

	@Deprecated
	public Range setNotnull(Boolean notnull) {
		return setStar(notnull);
	}

	@Override
	public Range setStar(Boolean star) {
		this.star=star;
		return this;
	}

	@Override
	public Boolean getStar() {
		return star;
	}
	@Transient
	@Override
	public List<FormElement<?, ?>> getNodes() {
		return nodes;
	}
	@Deprecated
	public Range add(FormElement<?, ?> node){
		throw new UnsupportedOperationException("use setBegin / setEnd instead");
	}
	@SuppressWarnings("unchecked")
	public List<FormElement<?, ?>> findNodes(){
		ArrayList<FormElement<?, ?>> ret=new ArrayList<FormElement<?, ?>>();
		if(begin!=null){
			ret.add(begin);
		}
		if(end!=null){
			ret.add(end);
		}
		return ret;
	}
	
	public boolean replaceNodeById(Widget<?> w){
		if(w!=null && w instanceof FormElement){
			if(w.getId()==null){
				return false;
			}
			if(begin!=null&&w.getId().equals(begin.getId())){
				begin=(FormElement<?, ?>) w;
				return true;
			}
			if(end!=null&&w.getId().equals(end.getId())){
				end=(FormElement<?, ?>) w;
				return true;
			}
		}
		return false;
	}
	public Range removeNodeById(String id){
		if(id==null){
			return this;
		}
		if(begin!=null&&id.equals(begin.getId())){
			begin=null;
		}
		if(end!=null&&id.equals(end.getId())){
			end=null;
		}
		return this;
	}
	/**
	 * 表单组合中间文本"至"
	 * @return the to
	 */
	public Object getTo() {
		return to;
	}
	/**
	 * 表单组合中间文本"至"
	 * @param to the to to set
	 * @return this
	 */
	public Range setTo(Object to) {
		this.to = to;
		return this;
	}
	
}
