package com.rongji.dfish.ui.widget;

import com.rongji.dfish.ui.DFishUITestCase;

public class CalendarPanelTest extends  DFishUITestCase{

	@Override
	protected Object getWidget() {
		CalendarPanel w=new CalendarPanel(CalendarPanel.TYPE_DATE,"fake");
		w.getPub().setCls("mycls");
		w.add(1, new CalendarTd("1号"));
		w.add(5, new CalendarTd("5号").setCls("highlight"));
		return w;
	}

}
