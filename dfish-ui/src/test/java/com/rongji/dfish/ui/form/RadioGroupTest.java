package com.rongji.dfish.ui.form;

import com.rongji.dfish.ui.DFishUITestCase;

public class RadioGroupTest extends DFishUITestCase {

    @Override
    protected Object getWidget() {
        RadioGroup rg=new RadioGroup("name","label","2",null);
//        rg.addOption(new Radio(null,null,false,"1","本月"));
//        rg.add("2","本月",null);
//        rg.add("3","直到",new DatePicker("theDate","直到","2019-10-01 09:00", DatePicker.FORMAT_DATE_TIME).setId("myStyle"));
//        rg.addOption(new Radio(null,null,false,"4","其他"));
        System.out.println(rg.formatString());
        rg.replaceNodeById(new DatePicker("theDate","直到","2019-10-01").setId("myStyle"));
        return rg;
    }
}
