package com.rongji.dfish.uitemp;

import com.rongji.dfish.ui.Combo;
import com.rongji.dfish.ui.Combo.Field;
import com.rongji.dfish.ui.Highlight;
import com.rongji.dfish.ui.helper.GridPanel;
import com.rongji.dfish.ui.layout.VerticalLayout;
import com.rongji.dfish.ui.layout.grid.GridColumn;
import com.rongji.dfish.ui.layout.grid.Tr;
import com.rongji.dfish.ui.widget.PageBar;

public class TemplateFactoryTest {
	public static void main(String[] args) {
		VerticalLayout vert=new VerticalLayout(null);
		GridPanel grid =new GridPanel("f_grid");
		vert.add(grid);
		grid.setFace(GridPanel.FACE_LINE).setLimit(10);
		Combo combo=new Combo();
		grid.setCombo(combo);
		combo.setField(new Field("cateId","cateName"));
		grid.getPub().setHeight(36).setFocusable(true)
			.setOn(Tr.EVENT_CLICK, "$.dialog(this).commander.complete(this);$.close(this)");
		
		grid.addColumn(GridColumn.text("cateName", GridPanel.WIDTH_REMAIN).setLabel("标题")
				.setHighlight(new Highlight().setKeycls("f-keyword").setMatchlength(2)));//FIXME nobr
		grid.addColumn(GridColumn.text("cateOrder", "80").setLabel("排序")
				.setAlign(GridColumn.ALIGN_RIGHT));
		
		
		PageBar pageBar=new PageBar(null,PageBar.TYPE_MINI);
		vert.add(pageBar,"22");
		pageBar.setBtncount(5).setAlign(PageBar.ALIGN_RIGHT).setTarget("f_grid");
		
		
		WidgetTemplate w=WidgetTemplate.convert(vert);
		WidgetTemplate gridTemp=w.findById("f_grid");
		
		WidgetTemplate tbodyTemp=gridTemp.createSubWidgetTemp("tbody");
		tbodyTemp.setAtProp("rows", "f_grid");
		//以上两句有个简单写法如下
//		gridTemp.setPropx("tbody.@rows","$data");
		
		System.out.println(w);
	}
}
