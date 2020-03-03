package com.rongji.dfish.ui.helper;

import com.rongji.dfish.ui.DFishUITestCase;
import com.rongji.dfish.ui.layout.Table;
import com.rongji.dfish.ui.layout.TableFactory;
import com.rongji.dfish.ui.auxiliary.Highlight;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class TablePanelTest extends DFishUITestCase {

	
	protected Table getWidget(){
		TableFactory.DefaultTableFactory gp= TableFactory.newTable("gp");
		gp.addColumn(TableFactory.Column.hidden(0, "id"));
		gp.addColumn(TableFactory.Column.tripleBox("id", "40"));
		gp.addColumn(TableFactory.Column.text(1, "C1","消息","*"));
		gp.addColumn(TableFactory.Column.text(2, "C2","发送人","100"));
		gp.addColumn((TableFactory.Column) TableFactory.Column.text(3, "C3","时间","100")
				.setFormat("yyyy-MM-dd HH:mm"));
		gp.setBodyData(Arrays.asList( new Object[][]{
			{"000001","【通知】请各位同事明天着正装上班，迎接XX领导一行莅临参观指导。","行政部",new Date()},
			{"000002","王哥，能不能把我工位上的一张XX项目的审批材料，拍个照发给我一下，谢谢","小张",new Date()},
			}));
		Table table =gp.build();

		table.setStyle("margin:10px;margin-bottom:0;");
		table.pub().setHeight(40).setOn(Table.EVENT_DBL_CLICK, "dblclick();");
//		gp.getHeadRow().setCls("handle_grid_head");

		System.out.println(table.getEscape());
		System.out.println(table.getScroll());
		return table;
	}
	@Test
	public void comboTest(){
		TableFactory.DefaultTableFactory gp= TableFactory.newTable("my");

		gp.setBodyData(Arrays.asList(new String[][]{
			{"uid1","名称1","mingcheng1","jp1"},
			{"uid2","名称2","mingcheng2","jp2"}}));
		gp.addColumn(TableFactory.Column.hidden(0, "uid"));

		com.rongji.dfish.ui.auxiliary.Column un = TableFactory.Column.text(1, "un", "名称", "60%").setHighlight(new Highlight(null, 2));
//		un.setTipfield("un");
		gp.addColumn((TableFactory.Column)un);
		gp.addColumn(TableFactory.Column.hidden(2, "py"));
		gp.addColumn(TableFactory.Column.hidden(3, "jp"));

		Table table =gp.build();
		table.setCls("x-grid-odd");
//		table.setCombo(new Combo().setField(new Combo.Field("uid", "un").setSearch("py,jp")).setKeepShow(false));
		output(table);
	}
	
	@Test
	public void getPrototypeTest(){
//		TableWrapper gp=getWidget();
//		Table gl=gp.getPrototype();
//		gl.gettBody().getRows().get(0).setCls("tr-0");
//		Table.TD td=new Table.TD();
//		gl.gettBody().getRows().get(0).setData("C1",td );
//		td.setRowSpan(2);
//		td.setNode(new Html("something").setVAlign(Html.V_ALIGN_MIDDLE));
//
//		output(gp);
	}
	
	@Test
	public void testColumn() {
		TableFactory.DefaultTableFactory gp= TableFactory.newTable(null);
		
		
		int dataColumnIndex = 0;
		List<Object[]> gridData = new ArrayList<Object[]>();
		for (int i=0; i<10; i++) {
			gridData.add(new Object[]{
					"第" + i + "行列1",
					"第" + i + "行列2",
					"第" + i + "行列3"
			});
		}
		gp.addColumn(TableFactory.Column.tripleBox("C", "40"));
		gp.addColumn((TableFactory.Column) TableFactory.Column.text(dataColumnIndex++, "第1列", "*")
				.setTripleBox("itemB", "B", true, null));
		gp.addColumn(TableFactory.Column.text(dataColumnIndex++, "第2列", "*"));
		gp.addColumn(TableFactory.Column.text(dataColumnIndex++, "第3列", "*"));
		gp.setBodyData(gridData);
		
//		grid.getPrototype().getThead().getRows().get(0).setCls("x-grid-head");
		output(gp.build());
	}

}
