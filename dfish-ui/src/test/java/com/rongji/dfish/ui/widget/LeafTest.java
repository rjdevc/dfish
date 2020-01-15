package com.rongji.dfish.ui.widget;

import com.rongji.dfish.ui.DFishUITestCase;

public class LeafTest extends  DFishUITestCase{

	@Override
	protected Object getWidget() {
		Tree.Leaf shell=new Tree.Leaf();
		Tree.Leaf root=new Tree.Leaf("root","根节点").setSrc("tree/open?id=root");
		shell.add(root);
		root.add(new Tree.Leaf("350000","福建省"));
		root.add(new Tree.Leaf("350100","福州市"));
		return shell;
	}

}
