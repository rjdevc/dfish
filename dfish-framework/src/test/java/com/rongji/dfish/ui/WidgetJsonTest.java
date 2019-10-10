package com.rongji.dfish.ui;

import com.rongji.dfish.ui.form.Rate;
import com.rongji.dfish.ui.layout.HorizontalLayout;
import com.rongji.dfish.ui.layout.VerticalLayout;
import com.rongji.dfish.ui.widget.Html;
import com.rongji.dfish.ui.widget.Img;

public class WidgetJsonTest {

    public static void main(String[] args) {
        HorizontalLayout horz = new HorizontalLayout(null).setValign(HorizontalLayout.VALIGN_MIDDLE);
        horz.add(new Img("file/thumbnail?fileId=").setHeight(80).setWidth(80));
        VerticalLayout vert = new VerticalLayout(null);
        horz.add(vert);

        HorizontalLayout top = new HorizontalLayout(null).setHeight(-1);
        vert.add(top);
        Rate rate = new Rate(null, "userName：", 8).setRemark("4分");
        top.add(rate);
        rate.getLabel().setWidth(-1);

        top.add(new Html("时间:20190627").setAlign(Html.ALIGN_RIGHT));

        vert.add(new Html("commentContent").setHeight(-1));

        System.out.println(horz.asJson());
    }

}
