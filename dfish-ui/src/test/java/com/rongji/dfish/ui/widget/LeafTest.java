package com.rongji.dfish.ui.widget;

import com.rongji.dfish.ui.DFishUITestCase;
import com.rongji.dfish.ui.auxiliary.Leaf;

public class LeafTest extends  DFishUITestCase{

	@Override
	protected Object getWidget() {
		Leaf shell=new Leaf(null);
		Leaf root=new Leaf("根节点").setSrc("tree/open?id=root");
		shell.add(root);
		root.add(new Leaf("福建省"));
		root.add(new Leaf("福州市"));

		Tree tree = new Tree();
		tree.add(root);
		return tree;
	}


}
