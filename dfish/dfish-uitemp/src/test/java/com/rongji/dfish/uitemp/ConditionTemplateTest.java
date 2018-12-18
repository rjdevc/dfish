package com.rongji.dfish.uitemp;

import com.rongji.dfish.ui.form.Text;
import com.rongji.dfish.ui.widget.Html;
import com.rongji.dfish.ui.widget.Leaf;

public class ConditionTemplateTest {
	public static void main(String[] args) {
		DFishTemplate dt1=getIf();
		System.out.println(dt1);
//		DFishTemplate dt2=getFor();
//		System.out.println(dt2);
	}

	private static DFishTemplate getFor() {
		WidgetTemplate leafTmp=WidgetTemplate.convert(new Leaf())
				.setAtProp("text", "$item.name");
		return new ForTemplate("nodes", "data").setNode(leafTmp);
	}

	private static DFishTemplate getIf() {
		WidgetTemplate textTmp=WidgetTemplate.convert(new Text("userName","",null))
				.setAtProp("value", "$data.name");
		WidgetTemplate roTmp=WidgetTemplate.convert(new Text("userName","",null).setReadonly(true))
				.setAtProp("value", "$data.name");
		WidgetTemplate htmTmp=WidgetTemplate.convert(new Html(null))
				.setAtProp("text", "$data.name");
		JudgeTemplate judge=new JudgeTemplate()
				.addIf("$data.readonly==0", textTmp)
				.addElseif("$data.readonly==1", roTmp)
				.addElse(htmTmp);
		return judge;
	}
	
}
