package com.rongji.dfish.ui;

import com.rongji.dfish.ui.form.Rate;
import com.rongji.dfish.ui.layout.Horizontal;
import com.rongji.dfish.ui.layout.Vertical;
import com.rongji.dfish.ui.widget.Html;
import com.rongji.dfish.ui.widget.Img;

public class WidgetJsonTest {

    public static void main(String[] args) {
        Horizontal horz = new Horizontal(null).setValign(Horizontal.VALIGN_MIDDLE);
        horz.add(new Img("file/thumbnail?fileId=").setHeight(80).setWidth(80));
        Vertical vert = new Vertical(null);
        horz.add(vert);

        Horizontal top = new Horizontal(null).setHeight(-1);
        vert.add(top);
        Rate rate = new Rate(null, "userName：", 8).setRemark("4分");
        top.add(rate);
        rate.getLabel().setWidth(-1);

        top.add(new Html("时间:20190627").setAlign(Html.ALIGN_RIGHT));

        vert.add(new Html("commentContent").setHeight(-1));

        System.out.println(horz.asJson());
    }

}
