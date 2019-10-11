package com.rongji.dfish.ui.widget;

import com.rongji.dfish.ui.DFishUITestCase;
import com.rongji.dfish.ui.layout.ButtonBar;
import com.rongji.dfish.ui.layout.Vertical;

public class ButtonBarTest extends DFishUITestCase{

	@Override
	protected Object getWidget() {
		Vertical vl=new Vertical("");
		ButtonBar bar=new ButtonBar("bbp");
		vl.add(bar);
		bar.getPub().setCls("f-buton");
		bar.add(new Button("新增"));
		bar.add(new SubmitButton("保存"));
		return vl;
	}
	
//	public static void main(String[] args) {
//		ButtonBarTest test=new ButtonBarTest();
//		Widget w=(Widget) test.getWidget();
//		for(;;){
//			w.toString();
//		}
//	}
}
