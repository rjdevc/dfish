package com.rongji.dfish.ui.widget;

import com.rongji.dfish.ui.DFishUITestCase;

public class LeafTest extends  DFishUITestCase{

	@Override
	protected Object getWidget() {
		Leaf shell=new Leaf();
		Leaf root=new Leaf("root","根节点").setSrc("tree/open?id=root");
		shell.add(root);
		shell.add(new Leaf("350000","福建省"));
		shell.add(new Leaf("350100","福州市"));
		return shell;
	}

}
