package com.rongji.dfish.ui.template;

import com.rongji.dfish.ui.form.Combo;
import com.rongji.dfish.ui.form.Combo.Field;
import com.rongji.dfish.ui.widget.Highlight;
import com.rongji.dfish.ui.widget.GridWrapper;
import com.rongji.dfish.ui.layout.Vertical;
import com.rongji.dfish.ui.layout.grid.GridColumn;
import com.rongji.dfish.ui.layout.grid.Tr;
import com.rongji.dfish.ui.widget.PageBar;

public class TemplateFactoryTest {
	public static void main(String[] args) {
		Vertical vert=new Vertical(null);
		GridWrapper grid =new GridWrapper("f_grid");
		vert.add(grid);
		grid.setFace(GridWrapper.FACE_LINE).setLimit(10);
		Combo combo=new Combo();
		grid.setCombo(combo);
		combo.setField(new Field("cateId","cateName"));
		grid.getPub().setHeight(36).setFocusable(true)
			.setOn(Tr.EVENT_CLICK, "$.dialog(this).commander.complete(this);$.close(this)");
		
		grid.addColumn(GridColumn.text("cateName", GridWrapper.WIDTH_REMAIN).setLabel("sha")
				.setHighlight(new Highlight().setKeycls("f-keyword").setMatchlength(2)));//FIXME nobr
		grid.addColumn(GridColumn.text("cateOrder", "80").setLabel("sha")
				.setAlign(GridColumn.ALIGN_RIGHT));
		
		
		PageBar pageBar=new PageBar(null,PageBar.TYPE_MINI);
		vert.add(pageBar,"22");
		pageBar.setBtncount(5).setAlign(PageBar.ALIGN_RIGHT).setTarget("f_grid");
		
		
		WidgetTemplate w=new WidgetTemplate(vert);
		WidgetTemplate gridTemp=w.findById("f_grid");
		
		WidgetTemplate tbodyTemp=gridTemp.createSubWidgetTemp("tbody");
		tbodyTemp.at("rows", "f_grid");
		//���������и���д������
//		gridTemp.setPropx("tbody.@rows","$data");
		
		System.out.println(w);
		System.out.println(new PreloadDefine("t/a", grid));
	}
}
