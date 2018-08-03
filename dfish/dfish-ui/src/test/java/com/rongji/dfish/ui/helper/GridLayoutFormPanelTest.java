package com.rongji.dfish.ui.helper;

import org.junit.Test;

import com.rongji.dfish.ui.DFishUITestCase;
import com.rongji.dfish.ui.form.Text;

public class GridLayoutFormPanelTest extends DFishUITestCase {
	protected Object getWidget(){
		GridLayoutFormPanel glfp =new GridLayoutFormPanel("myform");
		glfp.add(0, 1, new Text("name2","标签2","value2"));
		glfp.add(0, 0, new Text("name1","标签1","value1"));
		glfp.add(1, 0, new Text("name3","标签3","value3"));
		glfp.add(1, 1, new Text("name4","标签4","value4"));
		glfp.getPrototype().getColumns().get(0).setWidth("10");
		return glfp;
	}

}
