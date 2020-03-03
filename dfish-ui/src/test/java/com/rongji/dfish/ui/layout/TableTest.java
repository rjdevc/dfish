package com.rongji.dfish.ui.layout;

import com.rongji.dfish.ui.DFishUITestCase;
import com.rongji.dfish.ui.Widget;
import com.rongji.dfish.ui.auxiliary.Column;
import com.rongji.dfish.ui.auxiliary.TD;
import com.rongji.dfish.ui.auxiliary.TR;
import com.rongji.dfish.ui.form.Text;
import com.rongji.dfish.ui.widget.Html;
import org.junit.Assert;
import org.junit.Test;

public class TableTest extends DFishUITestCase {
	protected Object getWidget(){
		Table gl=new Table("mygrid");
		gl.add(0, 0, "test");
		gl.add(0, 1, new Text("name","姓名","王德发"));
		return gl;
	}

	@Test
	public void autoExpend(){
		Table gl=new Table("mygrid");
		gl.add(1, 1, "test");
		Assert.assertTrue(gl.getTBody().getNodes().size()==2);
		
	}

	@Test
	public void trSuit(){
		Table gl=new Table("mygrid");
		gl.addColumn(Column.text("C1","*"));
		gl.add(new TR().putData("C1","第一列内容"));
		gl.add(new TR().setCls("tr-odd").putData("C1","第二行第一列内容"));
		output(gl);
	}
	
	@Test
	public void tdSuit(){
		Table gl=new Table("mygrid");

		gl.add(0,0,new TD().setNode(new Html("哈密瓜")));
		gl.add(0,1,new TD().setAlign(TD.ALIGN_RIGHT).setNode(new Html("橙子")));
		gl.add(1,0,new TD().setNode(new Html("山竹").setStyle("background-color:gray")));
		gl.add(1,1,new TD().setAlign(TD.ALIGN_RIGHT).setNode(new Html("杨桃").setStyle("background-color:gray")));
		gl.add(2,0,"火龙果");
		gl.add(2,1,new Html("西瓜"));//FIXME 输出不是最简的 西瓜
		gl.add(3,0,new Html("水蜜桃").setStyle("background-color:gray"));
		output(gl);
	}
	@Test
	public void layoutTest(){
		Table gl=new Table("mygrid");

		gl.add(1, 1, new Html("为了那苍白的爱情的继续").setId("tt1"));
		gl.add(1, 2, new Html("为了那得到又失去的美丽").setId("tt2"));
		
		Widget w=(Widget)gl.findNodeById("tt1");
		Assert.assertTrue(w!=null);
		
		gl.removeNodeById("tt1");
		Assert.assertTrue(gl.findNodeById("tt1")==null);
//		gl.repalceNodeById();
		gl.replaceNodeById(new Html("就让这擦干又留出的泪水，化作漫天相思的雨").setId("tt2"));
		output(gl);
		gl.replaceNodeById(new TD().setId("tt2").setNode(new Html("就让这擦干又留出的泪水，化作漫天相思的雨")));
		output(gl);

	}
	@Test
	public void templateTest(){
		Table props= new Table("elemProps");

		props.setFace(Table.FACE_LINE);

		props.addColumn(Column.text("C1","160"));
		props.addColumn(Column.text("C3","80"));
		props.addColumn(Column.text("C4","80"));
		props.addColumn(Column.text("C5","120"));
		props.addColumn(Column.text("C6","*"));

		TR tHead=new TR();
		props.getTHead().add(tHead);
		tHead.putData("C1","属性名");
		tHead.putData("C3","类型");
		tHead.putData("C4","必填");
		tHead.putData("C5","默认值");
		tHead.putData("C6","提示信息");

		TR tBodyRow=new TR();
		props.getTBody().add(tBodyRow);
		tBodyRow.putData("@C1","$item.name");
		tBodyRow.putData("@C3","$item.type");
		tBodyRow.putData("@C4","$item.required");
		tBodyRow.putData("@C5","$item.defaultValue");
		tBodyRow.putData("@C6","$item.tip");
        System.out.print(props.formatString());
	}
}
