package com.rongji.dfish.ui.form;

import java.util.Arrays;

import com.rongji.dfish.ui.DFishUITestCase;

public class XboxTest extends DFishUITestCase{

	@Override
	protected Object getWidget() {
		String[][] options={{"1","男"},{"2","女"},{"0","未确定或不可识别"},{"9","其他"}};
		Xbox xbox=new Xbox("gender","性别","2",Arrays.asList(options));
		return xbox;
	}

}
