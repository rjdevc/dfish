package com.rongji.dfish.ui.form;


import com.rongji.dfish.ui.layout.Form;

public class TextTest {
	public static void main(String[] args) {
		Text t=new Text("a","b","c").setRequired(true);
//		t.getLabel().setWidth(null);
		System.out.println(t);
		Form form =new Form("");
		form.add(new FormLabel("what","you guess"));
		System.out.println(form.formatString());

	}
}
