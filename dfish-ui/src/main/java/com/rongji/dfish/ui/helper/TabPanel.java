package com.rongji.dfish.ui.helper;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.rongji.dfish.base.Utils;
import com.rongji.dfish.ui.AbstractWidgetWrapper;
import com.rongji.dfish.ui.FormElement;
import com.rongji.dfish.ui.JsonObject;
import com.rongji.dfish.ui.Layout;
import com.rongji.dfish.ui.Widget;
import com.rongji.dfish.ui.layout.AbstractLayout;
import com.rongji.dfish.ui.layout.ButtonBar;
import com.rongji.dfish.ui.layout.FrameLayout;
import com.rongji.dfish.ui.layout.HorizontalLayout;
import com.rongji.dfish.ui.layout.VerticalLayout;
import com.rongji.dfish.ui.widget.Button;

/**
 * 标签面板
 * FIXME 暂未封装完成
 * @author DFish Team
 *
 */
public class TabPanel extends AbstractWidgetWrapper<TabPanel, VerticalLayout> implements Layout<TabPanel, Widget<?>> ,JsonObject {

	
	private static final long serialVersionUID = 3327378599298593391L;
	private ButtonBar tabBar;
	private FrameLayout main;
	
	/**
	 * 标签栏ID后缀名
	 */
	public static final String SUFFIX_BAR = "_bar";
	/**
	 * 标签栏按钮ID后缀名
	 */
	public static final String SUFFIX_BAR_BUTTON = "_bar_btn";
	/**
	 * 标签主页面ID后缀名
	 */
	public static final String SUFFIX_MAIN = "_main";
	
	/**
	 * 标签栏按钮组后缀名
	 */
	public static final String SUFFIX_BAR_BUTTON_GROUP = "_group";
	
	/**
	 * 标签栏位置-上
	 */
	public static final int POS_TOP = 1;
	/**
	 * 标签栏位置-右
	 */
	public static final int POS_RIGHT = 2;
	/**
	 * 标签栏位置-下
	 */
	public static final int POS_BOTTOM = 3;
	/**
	 * 标签栏位置-左
	 */
	public static final int POS_LEFT = 4;
	/**
	 * 标签栏位置,默认是顶部
	 */
	private int tabPosition = POS_TOP;
	/**
	 * 构造函数
	 * @param id String
	 */
	public TabPanel(String id) {
		// 原型是垂直布局(绝大多数都是上下结构)
		prototype = new VerticalLayout(id).addCls("x-tab");
		// 构建标签栏
		tabBar = new ButtonBar(null).addCls("x-tab-bar").setHeight(-1);
		tabBar.getPub().setFocusable(true).addCls("x-tab-btn");
		prototype.add(tabBar);
		
		// 构建主页面
		main = new FrameLayout(null).addCls("x-tab-main").setHeight("*").setWidth("*");
		prototype.add(main);
		// 设置编号/名称统一由此方法来处理
		this.setId(id);
		
		bundleProperties();
	}
	
	@Override
    public TabPanel setId(String id) {
		prototype.setId(id);
		String preId = id == null ? "" : id;
		tabBar.setId(preId + SUFFIX_BAR);
		tabBar.getPub().setName(preId + SUFFIX_BAR_BUTTON_GROUP);
		main.setId(preId + SUFFIX_MAIN);
	    return this;
    }
	
	/**
	 * 设置默认显示的项
	 * 注意index从0开始。即最 左边/顶部的tab为0
	 * 当与{@link #setFocusWidgetId(String)}一起使用的时候。
	 * 该设置将失效。
	 * @param focusIndex 设置默认显示的项
	 * @return 本身，这样可以继续设置其他属性
	 */
	public TabPanel setFocusIndex(int focusIndex) {
		if (focusIndex < 0) {
			throw new IndexOutOfBoundsException("focusIndex: " + focusIndex);
		}
		
		List<Widget<?>> nodes = tabBar.getNodes();
		if (focusIndex >= nodes.size()) {
			throw new IndexOutOfBoundsException("focusIndex: " + focusIndex + ", Item Count: " + nodes.size());
		}
		return focusButton((Button) nodes.get(focusIndex));
	}
	
	/**
	 * 设置聚焦按钮
	 * @param focusBtn
	 * @return 本身，这样可以继续设置其他属性
	 */
	private TabPanel focusButton(Button focusBtn) {
		if (focusBtn != null) {
			List<Widget<?>> nodes = tabBar.getNodes();
			for (Widget<?> node : nodes) { // 需要将所有按钮聚焦去除
				Button btn = (Button) node;
				btn.setFocus(null);
			}
			// 找到对应的按钮聚焦
			focusBtn.setFocus(true);
			main.setDft(focusBtn.getTarget());
		}
		return this;
	}
	
	/**
	 * 设置默认显示的组件编号
	 * 
	 * @param widgetId 设置默认显示的组件编号
	 * @return 本身，这样可以继续设置其他属性
	 */
	public TabPanel setFocusWidgetId(String widgetId) {
		if (Utils.isEmpty(widgetId)) {
			return this;
		}
		
		List<Widget<?>> nodes = tabBar.getNodes();
		for (Widget<?> item : nodes) {
			Button btn = (Button) item;
			if (widgetId.equals(btn.getTarget())) { // 找到对应名称设置聚焦
				return focusButton(btn);
			}
		}
		
		return this;
	}
	
	/**
	 * 
	 * <p>描述:设置标签栏的样式</p>
	 * @param cls String
	 * @return 本身，这样可以继续设置其他属性
	 */
	@Deprecated
	public TabPanel setTabCls(String cls) {
		tabBar.setCls(cls);
		return this;
	}
	
	/**
	 * 
	 * <p>描述:设置标签栏高度</p>
	 * @param height int
	 * @return 本身，这样可以继续设置其他属性
	 */
	@Deprecated
	public TabPanel setTabHeight(int height) {
		tabBar.setHeight(height);
		return this;
	}
	
	/**
	 * 
	 * <p>描述:设置标签栏高度</p>
	 * @param height String
	 * @return 本身，这样可以继续设置其他属性
	 */
	@Deprecated
	public TabPanel setTabHeight(String height) {
		tabBar.setHeight(height);
		return this;
	}

	/**
	 * 添加标签页
	 * @param icon 标签图标
	 * @param text 标签标题
	 * @param w 内容面板
	 * @return 本身，这样可以继续设置其他属性
	 * @deprecated  用 {@link #add(Widget, String, String)}代替。
	 */
	@Deprecated
	public TabPanel addSubWidget(String icon, String text, Widget<?> w) {
		return add(w, icon, text);
	}
	
	/**
	 * 添加标签页
	 * @param w 内容面板
	 * @param icon 标签图标
	 * @param text 标签标题
	 * @return  本身，这样可以继续设置其他属性
	 */
	public TabPanel add(Widget<?> w, String icon, String text) {
		return add(w, new Button(icon, text));
	}
	
	/**
	 * 添加标签页
	 * @param w 元素
	 * @param text 标签标题
	 * @return 本身，这样可以继续设置其他属性
	 */
	public TabPanel add(Widget<?> w, String text) {
		return add(w, new Button(text));
	}
	
	private int widgetIndex;
	/**
	 * 添加标签页
	 * @param w 元素
	 * @param b 按钮
	 * @return 本身，这样可以继续设置其他属性
	 */
	public TabPanel add(Widget<?> w, Button b) {
		if (w == null || w == this) {
			throw new UnsupportedOperationException("The widget is illegal(null or self).");
		}
		if (b == null) {
			throw new UnsupportedOperationException("The button can not be null.");
		}
//		subItems.add(new Object[]{ b, w });
//		markRebuild();
		
		String wId = w.getId();
		if (Utils.isEmpty(wId)) { // 没有ID时自动产生
			wId = getId() + Integer.toString(widgetIndex++, 32);
			w.setId(wId);
		}
		// 没有任何界面时默认聚焦当前页
		Boolean needFocus = null;
		if (Utils.isEmpty(tabBar.findNodes())) {
			// 这样写法是为了json输出省流量
			needFocus = true;
			main.setDft(w.getId());
		}
		
		tabBar.add(b.setId(wId + SUFFIX_BAR_BUTTON).setTarget(wId).setFocus(needFocus));
		main.add(w);
		
		return this;
	}
	
	/**
	 * 获取标签栏
	 * @return 标签栏
	 */
	public ButtonBar getTabBar() {
		return tabBar;
	}
	
	/**
	 * 获取主页面
	 * @return 主页面
	 */
	public FrameLayout getMain() {
		return main;
	}
	
	/**
	 * 设置标签栏位置
	 * @param tabPosition int 位置
	 * @return 本身，这样可以继续设置其他属性
	 * @see #POS_BOTTOM
	 * @see #POS_LEFT
	 * @see #POS_RIGHT
	 * @see #POS_TOP
	 */
	public TabPanel setTabPosition(int tabPosition) {
		Set<Integer> positions = new HashSet<Integer>(4);
		positions.add(POS_TOP);
		positions.add(POS_RIGHT);
		positions.add(POS_BOTTOM);
		positions.add(POS_LEFT);
		if (!positions.contains(tabPosition)) {
			throw new UnsupportedOperationException("only accept the positions:" + positions);
		}
		
		if (this.tabPosition != tabPosition) { // 位置不相同时,需要重构
			this.tabPosition = tabPosition;
			
			// FIXME 节点清理方法
			prototype.findNodes().clear();
			
			AbstractLayout<?, Widget<?>> layout = null;
			boolean btnFirst = true;
			if (tabPosition == POS_TOP) {
				layout = prototype;
			} else if (tabPosition == POS_BOTTOM) {
				layout = prototype;
				btnFirst = false;
			} else if (tabPosition == POS_RIGHT) {
				layout = new HorizontalLayout(null);
				prototype.add(layout);
				btnFirst = false;
			} else if (tabPosition == POS_LEFT) {
				layout = new HorizontalLayout(null);
				prototype.add(layout);
			}
			if (btnFirst) { // 按钮优先添加
				layout.add(tabBar);
				layout.add(main);
			} else {
				layout.add(main);
				layout.add(tabBar);
			}
		}
		
		return this;
	}

	/**
	 * 标签栏位置
	 * @return 标签栏位置
	 */
	public int getTabPosition() {
		return tabPosition;
	}

	@SuppressWarnings("unchecked")
    @Override
	public List<Widget<?>> findNodes() {
		return main.findNodes();
	}

	public Widget<?> findNodeById(String id) {
		return super.findNodeById(id);
	}
	
	public List<FormElement<?,?>> findFormElementsByName(String name) {
		return super.findFormElementsByName(name);
	}
	
	/**
	 * 根据编号移除组件
	 * @param id String
	 */
	public TabPanel removeNodeById(String id) {
		if (Utils.isEmpty(id)) {
			return this;
		}
		// 移除还需要移除对应按钮栏上的按钮
		tabBar.removeNodeById(id + SUFFIX_BAR_BUTTON);
		return super.removeNodeById(id);
	}

	/**
	 * 根据组件编号替换组件
	 * @param w widget to replace
	 */
    public boolean replaceNodeById(Widget<?> w) {
		return super.replaceNodeById(w);
	}
    
    /**
     * 添加隐藏域的值
     * @param name String
     * @param value String
     * @return this
     */
    public TabPanel addHidden(String name, String value) {
    	prototype.addHidden(name, value);
    	return this;
    }

	@Override
	public void clearNodes() {
		tabBar.clearNodes();
		main.clearNodes();
	}
}
