package com.rongji.dfish.base;

import com.rongji.dfish.base.util.BeanUtil;
import com.rongji.dfish.ui.auxiliary.TD;
import com.rongji.dfish.ui.json.JsonBuilder;
import com.rongji.dfish.ui.json.JsonFormat;
import com.rongji.dfish.ui.layout.Form;
import com.rongji.dfish.ui.widget.Html;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class BeanUtilTest {
    public static void main(String[] args) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Form form=new Form();
        TD pub =new TD().setColSpan(4);
        form.setPub(pub);

        System.out.println(form);
        System.out.println(pub);

        JsonBuilder jb=JsonFormat.get(Form.class) ;


        Method m=BeanUtil.getMethod(TD.class,"setColSpan",Integer.class);
        Method m3=BeanUtil.getMethod(Form.class,"setPub",TD.class);

        m.invoke(pub,4);
        System.out.println(m);
    }
}
