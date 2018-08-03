package com.rongji.dfish.ui.helper;

import org.junit.Assert;
import org.junit.Test;

import com.rongji.dfish.ui.DFishUITestCase;
import com.rongji.dfish.ui.form.Text;
import com.rongji.dfish.ui.widget.Html;

public class FlexGridTest extends DFishUITestCase {

	protected Object getWidget(){
		FlexGrid fg=new FlexGrid("myflexgrid");
		fg.addLabelRow(new Text("name","标题","value"), 4);
		fg.addLabelRow(new Text("name2","标题2","value2"), FlexGrid.FULL_LINE);
		return fg;
	}
	
	@Test
	public void prototypeCombineCell(){
		FlexGrid fg=new FlexGrid("userInfo");
		fg.addLabelRow(new Text("name","姓名","张三"), 4);
		fg.addLabelRow(new Text("gender","性别","男"), 4);
		fg.add(new Html("占位符"), 4);
		
		fg.addLabelRow(new Text("dept","部门","开发部"), 4);
		fg.addLabelRow(new Text("job","职务","程序猿"), 4);
		fg.add(new Html("占位符"), 4);
		
		fg.addLabelRow(new Text("phone","电话","189啤酒白酒葡萄酒"), 4);
		fg.addLabelRow(new Text("email","EMail","LOST"), 4);
		fg.add(new Html("占位符"), 4);
		
		fg.addLabelRow(new Text("loginTime","登入时间","2018-07-13 14:33:21"), 4);
		fg.addLabelRow(new Text("loginIp","登入IP","192.168.212.84"), 4);
		fg.addLabelRow(new Text("loginLoc","登入位置","局域网内"), 4);
		
		fg.getPrototype().put(0, 8, 2, 11, new Html("<div>该DIV应该作为人员头像的编辑位置</div>"));
		boolean shouldBeError=false;
		try{
			fg.addLabelRow(new Text("loginTime","登入时间","2018-07-13 14:33:21"), 4);
		}catch(Exception ex){
			shouldBeError=true;
		}
		Assert.assertTrue(shouldBeError);
		output(fg);
	}
	
	@Test
	public void columnsSetting(){
		
		FlexGrid fgBefore=new FlexGrid("myflexgrid");
		fgBefore.addLabelRow(new Text("name","标题","value"), 4);
		fgBefore.addLabelRow(new Text("name2","标题2","value2"), FlexGrid.FULL_LINE);
		fgBefore.setColumns(8);
		
		FlexGrid fgAfter=new FlexGrid("myflexgrid");
		fgAfter.setColumns(8);
		fgAfter.addLabelRow(new Text("name","标题","value"), 4);
		fgAfter.addLabelRow(new Text("name2","标题2","value2"), FlexGrid.FULL_LINE);
		
		Assert.assertTrue(fgAfter.asJson().equals(fgBefore.asJson()));
	
		
		FlexGrid fgBefore2=new FlexGrid("myflexgrid");
		fgBefore2.add(new Text("name","标题","value"), 4);
		fgBefore2.add(new Text("name2","标题2","value2"), FlexGrid.FULL_LINE);
		fgBefore2.setColumns(8);
		
		FlexGrid fgAfter2=new FlexGrid("myflexgrid");
		fgAfter2.setColumns(8);
		fgAfter2.add(new Text("name","标题","value"), 4);
		fgAfter2.add(new Text("name2","标题2","value2"), FlexGrid.FULL_LINE);
		
		Assert.assertTrue(fgBefore2.asJson().equals(fgAfter2.asJson()));


	}
}
