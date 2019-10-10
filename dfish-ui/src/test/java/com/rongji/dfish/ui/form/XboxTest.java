package com.rongji.dfish.ui.form;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.rongji.dfish.ui.DFishUITestCase;

public class XboxTest extends DFishUITestCase{

	@Override
	protected Object getWidget() {
		List<String[]> options=Arrays.asList(new String[][]{{"1","男"},{"2","女"},{"0","未确定或不可识别"},{"9","其他"}});
		List<Object>realOptions=new ArrayList<>();
		realOptions.add(new Option("ALL","全部").setCheckall(true));
		realOptions.addAll(options);
		Xbox xbox=new Xbox("gender","性别","2",realOptions).setMultiple(true);
		return xbox;
	}

}
