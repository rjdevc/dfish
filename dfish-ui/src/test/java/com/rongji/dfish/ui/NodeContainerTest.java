package com.rongji.dfish.ui;

import com.rongji.dfish.ui.layout.Horizontal;
import com.rongji.dfish.ui.layout.Vertical;
import com.rongji.dfish.ui.layout.View;
import com.rongji.dfish.ui.widget.Html;
import org.junit.Assert;
import org.junit.Test;

public class NodeContainerTest {
    @Test
    public void findInVertical(){
        View view =new View();
        Vertical root =new Vertical(null);
        view.setNode(root);
        Horizontal horizontal =new Horizontal(null);
        root.add(horizontal);
        Html html=new Html("HELLO WORLD!").setId("hello");
        horizontal.add(html);

        Assert.assertTrue( view.findNodeById("hello")!=null);

    }
}
