package com.rongji.dfish.ui.form;

import com.rongji.dfish.ui.DFishUITestCase;
import com.rongji.dfish.ui.widget.GridWrapper;

import java.util.Arrays;
import java.util.List;

public class CheckboxgroupTest extends DFishUITestCase {

    @Override
    protected Object getWidget() {
        List<String[]> options= Arrays.asList(new String[][]{{"1","一"},{"2","二"},{"3","三"}});
        Checkboxgroup cbg=new Checkboxgroup("cbg","候选",Arrays.asList("2","3"),options);
//        cbg.setValue(Arrays.asList("2","3"));

        Select cbg2=new Select("cbg","候选","2",null);
        cbg2.setOptions(options);
        System.out.print(cbg2.formatString());



        return cbg;
    }
}
