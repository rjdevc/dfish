package com.rongji.dfish.ui.form;

import com.rongji.dfish.ui.DFishUITestCase;

import java.util.Date;

public class DatapickerTest  extends DFishUITestCase {
    @Override
    protected Object getWidget() {
        return new DatePicker("update","时间",new Date());
    }
}
