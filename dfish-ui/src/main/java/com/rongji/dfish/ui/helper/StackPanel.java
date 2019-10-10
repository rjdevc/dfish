package com.rongji.dfish.ui.helper;

import java.util.ArrayList;
import java.util.List;

import com.rongji.dfish.ui.AbstractWidgetWrapper;
import com.rongji.dfish.ui.Scrollable;
import com.rongji.dfish.ui.Widget;
import com.rongji.dfish.ui.layout.VerticalLayout;
import com.rongji.dfish.ui.widget.Html;

/**
 * 一个面板显示其所有的子部件“甲板”,
 * 一次只有一个可以看得见的地方
 * 和TAB不同，因为标签是竖排的，内容又总跟在标签下方，所以内容的位置会随着选择的标签不同而不同。
 * 常用于狭长的区域。
 * (注意本类仅实现水平的切割，从上到下分为基本标题。并不包含竖直切割，从左到右的布局。)
 * @author DFish Team
 * @since 2.0
 */
public class StackPanel extends AbstractWidgetWrapper<StackPanel, VerticalLayout> implements Scrollable<StackPanel> {
	
	private static final long serialVersionUID = 2549668048033584474L;
	/**
	 * @param id
	 */
	public StackPanel(String id) {
		prototype = new VerticalLayout(id);
		bundleProperties();
	}
	
	private List<Object[]> subItems = new ArrayList<Object[]>();
	/**
	 * 添加标签页
	 * @param icon 图标
	 * @param title 标题
	 * @param w 内容
	 * @return 本身，这样可以继续设置其他属性
	 */
	public StackPanel addSubWidget(String icon, String title, Widget<?> w) {
		if (w == null) {
			w = new Html(null);
		}
		subItems.add(new Object[]{ icon, title, w });
		return this;
	}
	
	@Override
    public VerticalLayout getPrototype() {
    	buildPrototype();
	    return super.getPrototype();
    }
	
	private void buildPrototype() {
//		prototype = new VerticalLayout(id);
		// FIXME 标题高度默认值
		if (labelHeight < 0) {
			labelHeight = 25;
		}
		String labelHeightStr = String.valueOf(labelHeight);
		setData("labelHeight", labelHeightStr);
		int index = 0;
		if (showIndex < 0 || showIndex >= subItems.size()) {
			showIndex = 0;
		}
		for (Object[] subItem : subItems) {
			VerticalLayout subShell = new VerticalLayout(prototype.getId() + "_" + index);
			
			String labelStr = (String) subItem[1];
			subShell.add(new Html(labelStr).setOn(Html.EVENT_CLICK, "editor.file.stack(this);").setCls("hand"));
			subShell.add((Widget<?>) subItem[2]);
			String heightStr = "";
			if (showIndex == index) {
				heightStr = "*";
				prototype.setData("focus", (index));
			} else {
				heightStr = labelHeightStr;
			}
			prototype.add(subShell, heightStr);
			index++;
		}
	}

	private int labelHeight = -1;

	private int showIndex;
	
	/**
	 * @return labelHeight
	 */
	public int getLabelHeight() {
		return labelHeight;
	}
	
	/**
	 * 标签高度
	 * @param labelHeight
	 * @return return this
	 */
	public StackPanel setLabelHeight(int labelHeight) {
		this.labelHeight = labelHeight;
		return this;
	}
	
	/**
	 * @return showIndex
	 */
	public int getShowIndex() {
		return showIndex;
	}

	/**
	 * @param showIndex
	 * @return 本身，这样可以继续设置其他属性
	 */
	public StackPanel setShowIndex(int showIndex) {
		this.showIndex = showIndex;
		return this;
	}

	public StackPanel setScroll(Boolean scroll) {
		prototype.setScroll(scroll);
		return this;
	}
	
	public Boolean getScroll() {
		return prototype.getScroll();
	}

    public StackPanel setScrollClass(String scrollClass) {
		prototype.setScrollClass(scrollClass);
	    return this;
    }

    public String getScrollClass() {
	    return prototype.getScrollClass();
    }

}
