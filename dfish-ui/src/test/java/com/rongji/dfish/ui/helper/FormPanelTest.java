package com.rongji.dfish.ui.helper;

import java.util.Arrays;

import com.rongji.dfish.ui.DFishUITestCase;
import com.rongji.dfish.ui.form.Text;
import com.rongji.dfish.ui.form.Xbox;

public class FormPanelTest extends  DFishUITestCase{

	@Override
	protected Object getWidget() {
		FormPanel fp=new FormPanel("fp");
		fp.addHidden("uid", "uid");
		fp.add(new Text("uname","user name","replace me").setNotnull(true));
		fp.add(new Xbox ("nam2","gender","0",Arrays.asList(new String[][]{{"","none"},{"1","mail"},{"0","female"}})));
		fp.add(new Text("phone","phone","replace me"));
		
//		fp.setLabelWidth("10");
		
		//因为getColumn的setWidth方法现在没有正确通知fp其实已经被变更了。
		//如果不想这个setWidth马上失效的话，要写前面这句
		return fp;
	}
	
	public static void main(String[] args) {
		System.out.println(System.currentTimeMillis());
	}
	
}
