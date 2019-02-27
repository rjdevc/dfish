package com.rongji.dfish.ui.template;

import org.junit.Test;

import com.rongji.dfish.ui.command.JSCommand;
import com.rongji.dfish.ui.form.DatePicker;
import com.rongji.dfish.ui.form.FormGroup;
import com.rongji.dfish.ui.form.Spinner;
import com.rongji.dfish.ui.form.Text;
import com.rongji.dfish.ui.helper.FlexGrid;
import com.rongji.dfish.ui.layout.View;
import com.rongji.dfish.ui.widget.Html;
import com.rongji.dfish.ui.widget.Leaf;

public class ConditionTemplateTest {
	
	@Test
	public void getDefine() {
		String uri="t/cms/test";
		JudgeTemplate jt=new JudgeTemplate();
		jt.addIf("@w-if($error)", new WidgetTemplate(new JSCommand(null)).at("text","app.error($error);"));
		jt.addElse(getWidgetTemplate());
		TemplateDefine ret= new TemplateDefine(uri,jt);
		System.out.println(ret);
	}
	
	private WidgetTemplate getWidgetTemplate() {
		View view =new View();
		FlexGrid fg=new FlexGrid("你猜");
		view.add(fg);
		Text text=new Text("userName","姓名","").setPlaceholder("请输入姓名");
		fg.addLabelRow(text, 4);
		text.getLabel().setWidth(100);
		Text text2=new Text("address","地址","").setPlaceholder("请输入地址");
		fg.addLabelRow(text2, 8);
		text2.getLabel().setWidth(100);
		
		FormGroup hg1=new FormGroup("加班时间");
		fg.addLabelRow(hg1, FlexGrid.FULL_LINE);
//		hg1.getLabel().setWidth(100);
		hg1.add(new DatePicker("bt","开始时间","",DatePicker.DATE),"-1");
		hg1.add(new Html("到"),"-1");
		hg1.add(new DatePicker("et","结束时间","",DatePicker.DATE),"-1");
		hg1.add(new Html("共"),"-1");
		hg1.add(new Spinner("day","天数",null,0,999,1),"60");
		hg1.add(new Html("天"),"-1");
		hg1.add(new Spinner("hours","小时数",null,0,999,1),"60");
		hg1.add(new Html("小时"),"-1");

		return new WidgetTemplate(view);
	}
	
	@Test
	public void getFor() {
		WidgetTemplate leafTmp=new WidgetTemplate(new Leaf())
				.at("text", "$item.name");
		System.out.println(new WidgetTemplate ().addFor( "$data"));
	}

	@Test
	public void getIf() {
		WidgetTemplate textTmp=new WidgetTemplate(new Text("userName","",null))
				.at("value", "$data.name");
		WidgetTemplate roTmp=new WidgetTemplate(new Text("userName","",null).setStatus(Text.STATUS_READONLY))
				.at("value", "$data.name");
		WidgetTemplate htmTmp=new WidgetTemplate(new Html(null))
				.at("text", "$data.name");
		JudgeTemplate judge=new JudgeTemplate()
				.addIf("$data.readonly==0", textTmp)
				.addElseif("$data.readonly==1", roTmp)
				.addElse(htmTmp);
		System.out.println(judge);
	}
	
}
