package com.rongji.dfish.ui.widget;

import com.rongji.dfish.ui.DFishUITestCase;
import com.rongji.dfish.ui.Widget;
import com.rongji.dfish.ui.layout.ButtonBar;
import com.rongji.dfish.ui.layout.VerticalLayout;

public class ButtonBarTest extends DFishUITestCase{

	@Override
	protected Object getWidget() {
		VerticalLayout vl=new VerticalLayout("");
		ButtonBar bar=new ButtonBar("bbp");
		vl.add(bar);
		bar.getPub().setCls("f-buton");
		bar.add(new Button("新增"));
		bar.add(new SubmitButton("保存"));
		return vl;
	}
	
	public static void main(String[] args) {
		ButtonBarTest test=new ButtonBarTest();
		Widget w=(Widget) test.getWidget();
		for(;;){
			w.toString();
		}
	}
}
