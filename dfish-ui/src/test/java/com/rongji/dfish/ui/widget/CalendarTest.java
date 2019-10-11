package com.rongji.dfish.ui.widget;

import com.rongji.dfish.ui.DFishUITestCase;

public class CalendarTest extends  DFishUITestCase{

	@Override
	protected Object getWidget() {
		Calendar w=new Calendar(Calendar.TYPE_DATE,"fake");
		w.getPub().setCls("mycls");
		w.add(1, new CalendarTd("1号"));
		w.add(5, new CalendarTd("5号").setCls("highlight"));
		return w;
	}

}
