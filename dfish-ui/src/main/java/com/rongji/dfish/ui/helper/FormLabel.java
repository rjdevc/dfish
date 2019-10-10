package com.rongji.dfish.ui.helper;

import com.rongji.dfish.ui.AbstractNode;
import com.rongji.dfish.ui.form.LabelRow;

/**
 * 表单标签,目前仅内部使用,为了能够正常绑定属性,继承AbstractJsonObject
 * Description: 
 * Copyright:   Copyright (c)2017
 * Company:     rongji
 * @author     DFish Team - YuLM
 * @version    1.0
 * @deprecated 在dfish3.2中FormLabel 有新的用法并且在form包下。改类废弃不再使用。
 * Create at:   2017-8-14 下午3:37:13
 * 
 * Modification History:
 * Date			Author				Version		Description
 * ------------------------------------------------------------------
 * 2017-8-14	DFish Team - YuLM	1.0			1.0 Version
 */
@Deprecated
public class FormLabel extends AbstractNode <FormLabel>{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7110328028637252599L;
	String suffix;
	String id;
	String text;
	Boolean star;
	Boolean prototypeEscape;
	
	public FormLabel(LabelRow<?> labelRow, Boolean prototypeEscape) {
//		 super();
//		 if (labelRow != null) {
//			 this.id = labelRow.getId();
//			 this.text = labelRow.getLabel();
//			 this.star = labelRow.getStar();
//		 }
//		 this.prototypeEscape = prototypeEscape;
	}
	
	public FormLabel(String text, Boolean star, Boolean prototypeEscape) {
//	    super();
//	    this.text = text;
//		this.star = star;
//		this.prototypeEscape = prototypeEscape;
    }
	
	public String getSuffix() {
		return suffix;
	}

	public FormLabel setSuffix(String suffix) {
		this.suffix = suffix;
		return this;
	}

	public String getId() {
		return id;
	}

	public FormLabel setId(String id) {
		this.id = id;
		return this;
	}

	public String getText() {
		return text;
	}

	public FormLabel setText(String text) {
		this.text = text;
		return this;
	}

	public Boolean getStar() {
		return star;
	}

	public FormLabel setStar(Boolean star) {
		this.star = star;
		return this;
	}

	public Boolean getPrototypeEscape() {
		return prototypeEscape;
	}

	public FormLabel setPrototypeEscape(Boolean prototypeEscape) {
		this.prototypeEscape = prototypeEscape;
		return this;
	}

	public Object getLabelWidget() {
		return getLabelWidget(false);
	}
	
    public Object getLabelWidget(boolean needWidget) {
//    	StringBuilder label = new StringBuilder();
//		
//		Boolean labelEscape = null;
//		if(Boolean.TRUE.equals(star)) {
//			label.append("<em class=f-required>*</em>");
//			//控件本身只要有Validate为required=true会自动触发 z-required 样式
//			labelEscape = false;
//			needWidget = true;
//		}
//		if (Utils.notEmpty(text)) {
//			label.append(Boolean.TRUE.equals(star) ? Utils.escapeXMLword(text) : text);
//			if (Utils.notEmpty(suffix)) {
//				label.append(suffix);
//			}
//		}
//		String outputLabelId = null;
//		if (Utils.notEmpty(id)) {
//			outputLabelId = "lbl_" + id;
//			needWidget = true;
//		}
//		
//		
//		Object labelWidget = null;
//		if (needWidget) {
//			labelWidget = new Html(outputLabelId, label.toString()).setEscape(AbstractWidget.calcRealEscape(labelEscape, prototypeEscape));
//		} else { // 这样实现省流量
//			labelWidget = label.toString();
//		}
//		
//		return labelWidget;
    	return null;
    }

	@Override
    public String getType() {
	    return null;
    }

}
