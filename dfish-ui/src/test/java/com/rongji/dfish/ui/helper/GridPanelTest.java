package com.rongji.dfish.ui.helper;

import com.rongji.dfish.ui.DFishUITestCase;
import com.rongji.dfish.ui.form.Combo;
import com.rongji.dfish.ui.layout.Grid;
import com.rongji.dfish.ui.layout.GridFactory;
import com.rongji.dfish.ui.widget.Highlight;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class GridPanelTest extends DFishUITestCase {

	
	protected Grid getWidget(){
		GridFactory.DefaultGridFactory gp=GridFactory.newGrid("gp");
		gp.addColumn(GridFactory.Column.hidden(0, "id"));
		gp.addColumn(GridFactory.Column.tripleBox("id", "40"));
		gp.addColumn(GridFactory.Column.text(1, "C1","消息","*"));
		gp.addColumn(GridFactory.Column.text(2, "C2","发送人","100"));
		gp.addColumn((GridFactory.Column)GridFactory.Column.text(3, "C3","时间","100")
				.setFormat("yyyy-MM-dd HH:mm"));
		gp.setGridData(Arrays.asList( new Object[][]{
			{"000001","【通知】请各位同事明天着正装上班，迎接XX领导一行莅临参观指导。","行政部",new Date()},
			{"000002","王哥，能不能把我工位上的一张XX项目的审批材料，拍个照发给我一下，谢谢","小张",new Date()},
			}));
		Grid grid=gp.build();

		grid.setStyle("margin:10px;margin-bottom:0;");
		grid.getPub().setHeight(40).setOn(Grid.EVENT_DBL_CLICK, "dblclick();");
//		gp.getHeadRow().setCls("handle_grid_head");

		System.out.println(grid.getEscape());
		System.out.println(grid.getScroll());
		return grid;
	}
	@Test
	public void comboTest(){
		GridFactory.DefaultGridFactory gp=GridFactory.newGrid("my");

		gp.setGridData(Arrays.asList(new String[][]{
			{"uid1","名称1","mingcheng1","jp1"},
			{"uid2","名称2","mingcheng2","jp2"}}));
		gp.addColumn(GridFactory.Column.hidden(0, "uid"));
		Grid.Column un = GridFactory.Column.text(1, "un", "名称", "60%").setHighlight(new Highlight(null, 2));
//		un.setTipfield("un");
		gp.addColumn((GridFactory.Column)un);
		gp.addColumn(GridFactory.Column.hidden(2, "py"));
		gp.addColumn(GridFactory.Column.hidden(3, "jp"));

		Grid grid=gp.build();
		grid.setCls("x-grid-odd");
		grid.setCombo(new Combo().setField(new Combo.Field("uid", "un").setSearch("py,jp")).setKeepShow(false));
		output(grid);
	}
	
	@Test
	public void getPrototypeTest(){
//		GridWrapper gp=getWidget();
//		Grid gl=gp.getPrototype();
//		gl.gettBody().getRows().get(0).setCls("tr-0");
//		Grid.TD td=new Grid.TD();
//		gl.gettBody().getRows().get(0).setData("C1",td );
//		td.setRowSpan(2);
//		td.setNode(new Html("something").setVAlign(Html.V_ALIGN_MIDDLE));
//
//		output(gp);
	}
	
	@Test
	public void testColumn() {
		GridFactory.DefaultGridFactory gp=GridFactory.newGrid(null);
		
		
		int dataColumnIndex = 0;
		List<Object[]> gridData = new ArrayList<Object[]>();
		for (int i=0; i<10; i++) {
			gridData.add(new Object[]{
					"第" + i + "行列1",
					"第" + i + "行列2",
					"第" + i + "行列3"
			});
		}
		gp.addColumn(GridFactory.Column.tripleBox("C", "40"));
		gp.addColumn((GridFactory.Column)GridFactory.Column.text(dataColumnIndex++, "第1列", "*")
				.setTripleBox("itemB", "B", true, null));
		gp.addColumn(GridFactory.Column.text(dataColumnIndex++, "第2列", "*"));
		gp.addColumn(GridFactory.Column.text(dataColumnIndex++, "第3列", "*"));
		gp.setGridData(gridData);
		
//		grid.getPrototype().getThead().getRows().get(0).setCls("x-grid-head");
		output(gp.build());
	}

}
