package com.rongji.dfish.ui.helper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.junit.Test;

import com.rongji.dfish.ui.Combo;
import com.rongji.dfish.ui.DFishUITestCase;
import com.rongji.dfish.ui.Highlight;
import com.rongji.dfish.ui.json.J;
import com.rongji.dfish.ui.layout.GridLayout;
import com.rongji.dfish.ui.layout.grid.GridColumn;
import com.rongji.dfish.ui.layout.grid.Td;
import com.rongji.dfish.ui.widget.Html;

public class GridPanelTest extends DFishUITestCase {

	
	protected GridPanel getWidget(){
		GridPanel gp=new GridPanel("f");
		gp.setStyle("margin:10px;margin-bottom:0;");
		gp.getPub().setHeight(40).setOn(GridPanel.EVENT_DBLCLICK, "dblclick();");
		gp.addColumn(GridColumn.hidden(0, "id"));
		gp.addColumn(GridColumn.gridTriplebox("id", "40"));
		gp.addColumn(GridColumn.text(1, "C1","消息","*"));
		gp.addColumn(GridColumn.text(2, "C2","发送人","100"));
		gp.addColumn(GridColumn.text(3, "C3","时间","100").setFormat("yyyy-MM-dd HH:mm"));
		gp.setGridData(Arrays.asList( new Object[][]{
			{"000001","【通知】请各位同事明天着正装上班，迎接XX领导一行莅临参观指导。","行政部",new Date()},
			{"000002","王哥，能不能把我工位上的一张XX项目的审批材料，拍个照发给我一下，谢谢","小张",new Date()},
			}));
//		gp.getHeadRow().setCls("handle_grid_head");

		System.out.println(gp.getEscape());
		System.out.println(gp.getScroll());
		return gp;
	}
	@Test
	public void comboTest(){
		GridPanel grid = new GridPanel("my");
		grid.setCls("x-grid-odd");
		grid.setCombo(new Combo().setField(new Combo.Field("uid", "un").setSearch("py,jp")).setKeepshow(false));
		grid.setGridData(Arrays.asList(new String[][]{
			{"uid1","名称1","mingcheng1","jp1"},
			{"uid2","名称2","mingcheng2","jp2"}}));
		grid.addColumn(GridColumn.hidden(0, "uid"));
		GridColumn un = GridColumn.text(1, "un", "名称", "60%").setHighlight(new Highlight(null, 2));
		un.setTipfield("un");
		grid.addColumn(un);
		grid.addColumn(GridColumn.hidden(2, "py"));
		grid.addColumn(GridColumn.hidden(3, "jp"));
		
		output(grid);
	}
	
	@Test
	public void getPrototypeTest(){
		GridPanel gp=getWidget();
		GridLayout gl=gp.getPrototype();
		gl.getTbody().getRows().get(0).setCls("tr-0");
		Td  td=new Td();
		gl.getTbody().getRows().get(0).setData("C1",td );
		td.setRowspan(2);
		td.setNode(new Html("something").setValign(Html.VALIGN_MIDDLE));
		
		output(gp);
	}
	
	@Test
	public void testGridColumn() {
		GridPanel grid = new GridPanel(null);
		
		
		int dataColumnIndex = 0;
		List<Object[]> gridData = new ArrayList<Object[]>();
		for (int i=0; i<10; i++) {
			gridData.add(new Object[]{
					"第" + i + "行列1",
					"第" + i + "行列2",
					"第" + i + "行列3"
			});
		}
		grid.addColumn(GridColumn.gridTriplebox("C", "40"));
		grid.addColumn(GridColumn.text(dataColumnIndex++, "第1列", "*").setGridTriplebox("itemB", "B", true, null));
		grid.addColumn(GridColumn.text(dataColumnIndex++, "第2列", "*"));
		grid.addColumn(GridColumn.text(dataColumnIndex++, "第3列", "*"));
		grid.setGridData(gridData);
		
//		grid.getPrototype().getThead().getRows().get(0).setCls("x-grid-head");
		output(grid);
	}

}
