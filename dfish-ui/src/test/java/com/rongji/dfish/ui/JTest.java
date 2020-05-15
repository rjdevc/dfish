package com.rongji.dfish.ui;

import java.math.BigDecimal;
import java.util.ArrayList;

import com.rongji.dfish.ui.json.JsonFormat;

public class JTest {
	public static void main(String[] args) {
		Object[] o=new Object[2];
		o[0]="法克";
		ArrayList<Object> arrayList= new ArrayList<>();
		o[1]=arrayList;
		arrayList.add(new BigDecimal(2));
		System.out.print(JsonFormat.toJson(o));
	}
}
