package com.rongji.dfish.framework.plugin.exception.view;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Component;

import com.rongji.dfish.base.Page;
import com.rongji.dfish.base.Utils;
import com.rongji.dfish.base.util.DateUtil;
import com.rongji.dfish.framework.plugin.exception.entity.PubExptRecord;
import com.rongji.dfish.framework.plugin.exception.service.ExceptionManager;
import com.rongji.dfish.framework.plugin.exception.service.ExceptionTypeInfo;
import com.rongji.dfish.ui.layout.GridLayout;
import com.rongji.dfish.ui.layout.VerticalLayout;
import com.rongji.dfish.ui.layout.View;
import com.rongji.dfish.ui.layout.grid.GridColumn;
import com.rongji.dfish.ui.layout.grid.GridLeaf;
import com.rongji.dfish.ui.layout.grid.Td;
import com.rongji.dfish.ui.layout.grid.Tr;
import com.rongji.dfish.ui.widget.Html;
import com.rongji.dfish.ui.widget.PageBar;
import com.rongji.dfish.ui.widget.Toggle;
@Component
public class ExceptionViewerView {
	public View buildListView(List<PubExptRecord> recs, Page page, Long typeId) {
		VerticalLayout root=new VerticalLayout("f_root");
		View view=new View().setNode(root);
		String titleText="<a href='javascript:;' onclick=\"VM(this).reload('exceptionViewer/changeLock')\" style=\"float:right\">"+(ExceptionManager.getInstance().isEnabled()?"[关闭]":"[打开]")+"</a>"+
				"查询所有的异常记录<p style=\"float:right\">&nbsp;&nbsp;&nbsp;&nbsp;</p><a href='javascript:;' onclick=\"VM(this).reload('exceptionViewer/deleteAllExpt')\"  style=\"float:right\">[清除所有记录]</a>"
				+"<p style=\"float:right\">&nbsp;&nbsp;&nbsp;&nbsp;</p><a href='javascript:;' onclick=\"VM(this).reload('exceptionViewer/removeRepit')\" style=\"float:right\">[去重2]</a>"
				+"<p style=\"float:right\">&nbsp;&nbsp;&nbsp;&nbsp;</p><a href='javascript:;' onclick=\"VM(this).reload('exceptionViewer/distinct')\" style=\"float:right\">[去重]</a>";
		ExceptionManager ep=ExceptionManager.getInstance();
		if(typeId != null){
			ExceptionTypeInfo eti=ep.getExceptionType(typeId);
			titleText="查询l类型为 <b>("+typeId+")"+eti.getClassName()+"</b> 的异常记录, [<a href='javascript:;' onclick=\"VM(this).reload('exceptionViewer/showAsLog')\">清空条件</a>]";
		}
		root.add(new Html(titleText).setStyle("margin:0 6px;border :1px dashed #CCC;background-color:#EEE;padding:8px 12px;"),"40");
		
		GridLayout grid=buildGrid(recs);
		root.add(grid,"*");
		
		PageBar pageBar=buildPageBar(page,typeId);
		root.add(pageBar,"40");
		
		return view;
	}
	public PageBar buildPageBar(Page page, Long typeId) {
		PageBar pageBar= new PageBar("f_page",PageBar.TYPE_BUTTONGROUP);
		pageBar.setSrc("exceptionViewer/logTurnPage?cp=$0"+(typeId==null?"":"&typeId="+typeId));
		pageBar.setBtncount(5);
		pageBar.setPage(page);
		return pageBar;
	}
	public GridLayout buildGrid(List<PubExptRecord>recs) {
		ExceptionManager ep=ExceptionManager.getInstance();
		GridLayout grid=new GridLayout("f_grid");
		grid.setFace(GridLayout.FACE_DOT);
		Tr thead=new Tr();
		grid.getThead().add(thead);
		thead.setData("C1", "时间");
		thead.setData("C2", "错误类型");
		thead.setData("C3", "信息");
		thead.setData("C4", "次数");
		grid.addColumn(GridColumn.text("C1", "90"));
		grid.addColumn(GridColumn.text("C4", "50").setAlign(GridColumn.ALIGN_RIGHT));
		grid.addColumn(GridColumn.text("C2", "240").setTip(true));
		grid.addColumn(GridColumn.text("C3", "*").setTip(true));

		
		SimpleDateFormat SDF=new SimpleDateFormat("HH:mm:ss.SSS");
		String datestr="";
		
		for(PubExptRecord rec:recs){
			String curDatestr=DateUtil.format(new Date(rec.getEventTime()),DateUtil.LV_DATE);
			if(!curDatestr.equals(datestr)){
				Tr tr=new Tr();
				grid.add(tr);
				tr.setData("C1", new Td().setColspan(4).setNode(new Toggle().setText("<B>"+curDatestr+"</B>").setHr(true).setOpen(true)));
				datestr=curDatestr;
			}
			Tr tr=new Tr();
			grid.add(tr);
			tr.setData("C1", SDF.format(new Date(rec.getEventTime())));
			ExceptionTypeInfo eti=ep.getExceptionType(new Long(rec.getTypeId()));
			GridLeaf gl=new GridLeaf();
			gl.setText("("+rec.getTypeId()+")"+eti.getClassName());
			gl.setSrc("exceptionViewer/showStackTrace?typeId="+rec.getTypeId());
			tr.setData("C2", gl);
			tr.setData("C3", rec.getExptMsg());
			tr.setData("C4", rec.getExptRepetitions());

		}
		return grid;
	}
}
