package com.rongji.dfish.uitemp;

import java.util.Arrays;

import com.rongji.dfish.ui.Widget;
import com.rongji.dfish.ui.command.JSCommand;
import com.rongji.dfish.ui.form.DatePicker;
import com.rongji.dfish.ui.form.Hidden;
import com.rongji.dfish.ui.form.Radiogroup;
import com.rongji.dfish.ui.form.Text;
import com.rongji.dfish.ui.form.Textarea;
import com.rongji.dfish.ui.helper.FormPanel;
import com.rongji.dfish.ui.helper.Label;
import com.rongji.dfish.ui.layout.ButtonBar;
import com.rongji.dfish.ui.layout.VerticalLayout;
import com.rongji.dfish.ui.widget.Button;
import com.rongji.dfish.ui.widget.SubmitButton;

import org.junit.Assert;
import org.junit.Test;

public class CmsTemplateTest {
	@Test
	public void testEditTemplate(){
		VerticalLayout vert =new VerticalLayout(null);
		vert.setHeight(VerticalLayout.HEIGHT_REMAIN);
		FormPanel form=new FormPanel("f_form");
		vert.add(form);
		//gp 
		form.setStyle("padding-right:20px");
		
		form.add(new Text("contentTitle","标题",null).at("value","$data.contentSubtitle"));
		form.add(new Text("contentSubtitle","副标题",null).at("value","$data.contentSubtitle"));
		form.add(new Textarea("contentText","内容",null).at("value","$data.contentText"));
		form.add(new Text("contentAuthor","作者",null).at("value","$data.contentAuthor"));
		form.add(new Radiogroup("contentStatus","状态",null,Arrays.asList(new String[][]{
			{"0","作废"},{"1","草稿"},{"2","发布"}})).at("value","$data.contentStatus"));
		form.add(new DatePicker("publicTime","发布时间",null,DatePicker.DATE_TIME_FULL).at("text","$data.publicTime"));
		form.add(new Label("创建时间",null).at("text","$data.createTime"));
//		form.add(new DefaultUploadImage("sealingPages","图片",null));
		form.add(new Hidden("cateId", null).at("value","$data.cateId"));
		form.add(new Hidden("contentId", null).at("value","$data.contentId"));
		
		//buttonbar 
		ButtonBar bar=new ButtonBar("bbr");
		vert.add(bar,"50");
		bar.setStyle("padding:0 30px;background:#f9f9f9")
			.setSpace(10).setAlign(ButtonBar.ALIGN_RIGHT);
		bar.getPub().setCls("f-button");
		bar.add(new SubmitButton("  确定  ").setOn(SubmitButton.EVENT_CLICK,"cms.saveContentForm(this);"));
		bar.add(new Button("  取消  ").setOn(Button.EVENT_CLICK,"dfish.close(this);"));
		
		System.out.println( shell(vert,"myname"));
	}
	
	protected TemplateDefine shell(WidgetTemplate wt,String uri){
		JudgeTemplate jt=new JudgeTemplate();
		jt.addIf("$error", WidgetTemplate.convert(new JSCommand(null).at("text","app.error($error);")));
		jt.addElse(wt);
		TemplateDefine ret= new TemplateDefine(uri,jt);
		return ret;
	}
	protected TemplateDefine shell(Widget<?> w,String uri){
		return shell(WidgetTemplate.convert(w),uri);
	}
}
