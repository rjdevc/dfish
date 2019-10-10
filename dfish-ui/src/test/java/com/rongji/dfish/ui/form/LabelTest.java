package com.rongji.dfish.ui.form;

import com.rongji.dfish.ui.Widget;
import com.rongji.dfish.ui.helper.FlexGrid;
import com.rongji.dfish.ui.helper.HorizontalGroup;
import com.rongji.dfish.ui.helper.Label;
import com.rongji.dfish.ui.json.J;
import com.rongji.dfish.ui.layout.View;
import com.rongji.dfish.ui.widget.Html;

import java.util.Date;

public class LabelTest {
	public static void main(String[] args) {
		LabelTest test=new LabelTest();
		Widget<?> w=test.getWidget();
		System.out.println(J.formatJson(w));
	}
	public Widget<?> getWidget(){
		View view =new View();
		FlexGrid fg=new FlexGrid("U_GUESS");
		Text text=new Text("userName","姓名","").setPlaceholder("请输入姓名");
		fg.addLabelRow(text, 4);
		text.getLabel().setWidth(100);
		Text text2=new Text("address","地址","").setPlaceholder("请输入地址");
		fg.addLabelRow(text2, 8);
//		text2.getLabel().setWidth(100);
		Text text3=new Text("address","地址","").setPlaceholder("请输入地址");
		fg.add(text3, 8);
		DatePicker begin=new DatePicker("begin","开始时间",new Date(),DatePicker.DATE);
		DatePicker end=new DatePicker("end","结束时间",null,DatePicker.DATE);
		Range r=new Range("时间",begin,end);
		fg.addLabelRow(r, FlexGrid.FULL_LINE);



		
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
		fg.addLabelRow(new Label("说明","请认真填写此项信息，改信息将纳入考勤考核"));
		return view.add(fg);
	}
	
}
