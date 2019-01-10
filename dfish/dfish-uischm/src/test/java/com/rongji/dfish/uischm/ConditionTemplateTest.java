package com.rongji.dfish.uischm;

import com.rongji.dfish.ui.command.JSCommand;
import com.rongji.dfish.ui.form.DatePicker;
import com.rongji.dfish.ui.form.Spinner;
import com.rongji.dfish.ui.form.Text;
import com.rongji.dfish.ui.helper.FlexGrid;
import com.rongji.dfish.ui.helper.HorizontalGroup;
import com.rongji.dfish.ui.layout.View;
import com.rongji.dfish.ui.widget.Html;
import com.rongji.dfish.ui.widget.Leaf;
import com.rongji.dfish.uischm.DFishSchema;
import com.rongji.dfish.uischm.JudgeSchema;
import com.rongji.dfish.uischm.TemplateDefine;
import com.rongji.dfish.uischm.WidgetSchema;

public class ConditionTemplateTest {
	public static void main(String[] args) {
		DFishSchema dt1=getDefine();
		System.out.println(dt1);
//		DFishTemplate dt2=getFor();
//		System.out.println(dt2);
		
		
	}
	private static DFishSchema getDefine() {
		String uri="t/cms/test";
		JudgeSchema jt=new JudgeSchema();
		jt.addIf("@w-if($error)", new WidgetSchema(new JSCommand(null)).setAtProp("text","app.error($error);"));
		jt.addElse(getWidgetTemplate());
		TemplateDefine ret= new TemplateDefine(uri,jt);
		return ret;
	}
	private static WidgetSchema getWidgetTemplate() {
		View view =new View();
		FlexGrid fg=new FlexGrid("你猜");
		view.add(fg);
		Text text=new Text("userName","姓名","").setPlaceholder("请输入姓名");
		fg.addLabelRow(text, 4);
		text.getLabel().setWidth(100);
		Text text2=new Text("address","地址","").setPlaceholder("请输入地址");
		fg.addLabelRow(text2, 8);
		text2.getLabel().setWidth(100);
		
		HorizontalGroup hg1=new HorizontalGroup("加班时间");
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

		return  new WidgetSchema(view);
	}
	private static DFishSchema getFor() {
		WidgetSchema leafTmp=new WidgetSchema(new Leaf())
				.at("text", "$item.name");
		return new WidgetSchema ().addFor("nodes", "$data",leafTmp);
	}

	private static DFishSchema getIf() {
		WidgetSchema textTmp=new WidgetSchema(new Text("userName","",null))
				.setAtProp("value", "$data.name");
		WidgetSchema roTmp=new WidgetSchema(new Text("userName","",null).setReadonly(true))
				.setAtProp("value", "$data.name");
		WidgetSchema htmTmp=new WidgetSchema(new Html(null))
				.setAtProp("text", "$data.name");
		JudgeSchema judge=new JudgeSchema()
				.addIf("$data.readonly==0", textTmp)
				.addElseif("$data.readonly==1", roTmp)
				.addElse(htmTmp);
		return judge;
	}
	
}
