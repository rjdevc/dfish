package com.rongji.dfish.ui.layout;

import org.junit.Assert;
import org.junit.Test;

import com.rongji.dfish.ui.DFishUITestCase;
import com.rongji.dfish.ui.Widget;
import com.rongji.dfish.ui.form.Text;
import com.rongji.dfish.ui.layout.grid.GridColumn;
import com.rongji.dfish.ui.layout.grid.Td;
import com.rongji.dfish.ui.layout.grid.Tr;
import com.rongji.dfish.ui.widget.Html;

public class GridLayoutTest extends DFishUITestCase {
	protected Object getWidget(){
		GridLayout gl=new GridLayout("mygrid");
		gl.add(0, 0, "test");
		gl.add(0, 1, new Text("name","姓名","王德发"));
		return gl;
	}

	@Test
	public void autoExpend(){
		GridLayout gl=new GridLayout("mygrid");
		gl.add(1, 1, "test");
		Assert.assertTrue(gl.getTbody().getRows().size()==2);
		
	}

	@Test
	public void trSuit(){
		GridLayout gl=new GridLayout("mygrid");
		gl.addColumn(GridColumn.text("C1","*"));
		gl.add(new Tr().setData("C1","第一列内容"));
		gl.add(new Tr().setCls("tr-odd").setData("C1","第二行第一列内容"));
		output(gl);
	}
	
	@Test
	public void tdSuit(){
		GridLayout gl=new GridLayout("mygrid");

		gl.add(0,0,new Td().setNode(new Html("哈密瓜")));
		gl.add(0,1,new Td().setAlign(Td.ALIGN_RIGHT).setNode(new Html("橙子")));
		gl.add(1,0,new Td().setNode(new Html("山竹").setStyle("background-color:gray")));
		gl.add(1,1,new Td().setAlign(Td.ALIGN_RIGHT).setNode(new Html("杨桃").setStyle("background-color:gray")));
		gl.add(2,0,"火龙果");
		gl.add(2,1,new Html("西瓜"));//FIXME 输出不是最简的 西瓜
		gl.add(3,0,new Html("水蜜桃").setStyle("background-color:gray"));
		output(gl);
	}
	@Test
	public void layoutTest(){
		GridLayout gl=new GridLayout("mygrid");

		gl.add(1, 1, new Html("为了那苍白的爱情的继续").setId("tt1"));
		gl.add(1, 2, new Html("为了那得到又失去的美丽").setId("tt2"));
		
		Widget w=gl.findNodeById("tt1");
		Assert.assertTrue(w!=null);
		
		gl.removeNodeById("tt1");
		Assert.assertTrue(gl.findNodeById("tt1")==null);
		
		gl.replaceNodeById(new Html("就让这擦干又留出的泪水，化作漫天相思的雨").setId("tt2"));
		output(gl);
		gl.replaceNodeById(new Td().setId("tt2").setNode(new Html("就让这擦干又留出的泪水，化作漫天相思的雨")));
		output(gl);

	}
	@Test
	public void templateTest(){
		GridLayout props= new GridLayout("elemProps");

		props.setFace(GridLayout.FACE_LINE);

		props.addColumn(GridColumn.text("C1","160"));
		props.addColumn(GridColumn.text("C3","80"));
		props.addColumn(GridColumn.text("C4","80"));
		props.addColumn(GridColumn.text("C5","120"));
		props.addColumn(GridColumn.text("C6","*"));

		Tr tHead=new Tr();
		props.getThead().add(tHead);
		tHead.setData("C1","属性名");
		tHead.setData("C3","类型");
		tHead.setData("C4","必填");
		tHead.setData("C5","默认值");
		tHead.setData("C6","提示信息");

		Tr tBodyRow=new Tr();
		props.getTbody().add(tBodyRow);
		tBodyRow.setData("@C1","$item.name");
		tBodyRow.setData("@C3","$item.type");
		tBodyRow.setData("@C4","$item.required");
		tBodyRow.setData("@C5","$item.defaultValue");
		tBodyRow.setData("@C6","$item.tip");
        tBodyRow.setFor("$data.props");
        System.out.print(props.formatString());
	}
}
