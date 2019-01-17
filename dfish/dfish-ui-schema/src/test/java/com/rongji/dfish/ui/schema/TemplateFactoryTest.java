package com.rongji.dfish.ui.schema;

import com.rongji.dfish.ui.Combo;
import com.rongji.dfish.ui.Combo.Field;
import com.rongji.dfish.ui.Highlight;
import com.rongji.dfish.ui.helper.GridPanel;
import com.rongji.dfish.ui.layout.VerticalLayout;
import com.rongji.dfish.ui.layout.grid.GridColumn;
import com.rongji.dfish.ui.layout.grid.Tr;
import com.rongji.dfish.ui.widget.PageBar;
import com.rongji.dfish.ui.schema.WidgetSchema;

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
		
		grid.addColumn(GridColumn.text("cateName", GridPanel.WIDTH_REMAIN).setLabel("sha")
				.setHighlight(new Highlight().setKeycls("f-keyword").setMatchlength(2)));//FIXME nobr
		grid.addColumn(GridColumn.text("cateOrder", "80").setLabel("sha")
				.setAlign(GridColumn.ALIGN_RIGHT));
		
		
		PageBar pageBar=new PageBar(null,PageBar.TYPE_MINI);
		vert.add(pageBar,"22");
		pageBar.setBtncount(5).setAlign(PageBar.ALIGN_RIGHT).setTarget("f_grid");
		
		
		WidgetSchema w=new WidgetSchema(vert);
		WidgetSchema gridTemp=w.findById("f_grid");
		
		WidgetSchema tbodyTemp=gridTemp.createSubWidgetTemp("tbody");
		tbodyTemp.setAtProp("rows", "f_grid");
		//���������и���д������
//		gridTemp.setPropx("tbody.@rows","$data");
		
		System.out.println(w);
		System.out.println(new TemplateDefine("t/a", grid));
	}
}
