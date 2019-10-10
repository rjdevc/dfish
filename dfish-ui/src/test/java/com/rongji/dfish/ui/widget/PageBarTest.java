package com.rongji.dfish.ui.widget;

import com.rongji.dfish.base.Page;
import com.rongji.dfish.ui.DFishUITestCase;
import org.junit.Test;

public class PageBarTest extends  DFishUITestCase{

	@Override
	protected Object getWidget() {
		PageBar pb=new PageBar("page",PageBar.TYPE_BUTTONGROUP);
		Page page=new Page();
		page.setCurrentCount(2);
		page.setPageSize(15);
		page.setRowCount(108);
		pb.setPage(page);
//		pb.setSrc(src);
		return pb;
	}

}
