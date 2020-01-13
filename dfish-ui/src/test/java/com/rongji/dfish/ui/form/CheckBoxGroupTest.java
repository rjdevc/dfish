package com.rongji.dfish.ui.form;

import com.rongji.dfish.base.Option;
import com.rongji.dfish.ui.DFishUITestCase;

import java.util.Arrays;
import java.util.List;

public class CheckBoxGroupTest extends DFishUITestCase {

    @Override
    protected Object getWidget() {
 //        cbg.setValue(Arrays.asList("2","3"));
        List<String[]> options= Arrays.asList(new String[][]{{"1","一"},{"2","二"},{"3","三"}});
        Select cbg2=new Select("cbg","候选","2",null);
        cbg2.setOptions(options);
        System.out.println(cbg2.formatString());

        Xbox xbox2=new Xbox("xbox2","选项2","3",options);
        System.out.println(xbox2.formatString());

        List<Option> options3= Arrays.asList(new Option("1","问题一"),
                new Option("2","问题二"),
                new Option("3","问题三").setChecked(true));
        Xbox xbox3=new Xbox("xbox3","选项3",null, options3);
        System.out.println(xbox3.formatString());
        //如果value =2 会覆盖option 的checked
        xbox3.setValue("2");
        System.out.println(xbox3.formatString());
        xbox3.setValue(new String[]{"2","3"});
        // xbox3.setValue(Arrays.asList("2","3")); 也可以
        System.out.println(xbox3.formatString());

        List<String[]> options4= Arrays.asList(new String[][]{{"1","问题一"},
                {"2","问题二"},
                {"3","问题三"}});
        Xbox xbox4=new Xbox("xbox3","选项3","3",options4);
        System.out.println(xbox4.formatString());


        List<CheckBox> cbOptions =Arrays.asList(new CheckBox("","",false,"1","问题一"),
                new CheckBox("","",false,"2","问题二"),
                new CheckBox("","",false,"3","问题三"));
        CheckBoxGroup cbg3=new CheckBoxGroup("cbg","候选",Arrays.asList("2","3"),cbOptions);
        System.out.println(cbg3.formatString());

        CheckBoxGroup cbg=new CheckBoxGroup("cbg","候选",Arrays.asList("2","3"),options);

        return cbg;
    }
}
