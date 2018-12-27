package com.rongji.dfish.ui.widget;

import java.util.List;

import com.rongji.dfish.ui.Combo;
import com.rongji.dfish.ui.HiddenContainer;
import com.rongji.dfish.ui.HiddenPart;
import com.rongji.dfish.ui.Highlight;
import com.rongji.dfish.ui.HtmlContentHolder;
import com.rongji.dfish.ui.LazyLoad;
import com.rongji.dfish.ui.MultiContainer;
import com.rongji.dfish.ui.PubHolder;
import com.rongji.dfish.ui.Scrollable;
import com.rongji.dfish.ui.Widget;
import com.rongji.dfish.ui.form.Hidden;
import com.rongji.dfish.ui.form.Triplebox;
import com.rongji.dfish.ui.layout.AbstractLayout;

/**
 * TreePanel 树的面板 这个面板里面可以防止树节点Leaf
 * 
 * @author DFish Team
 * @version 1.0
 * @since XMLTMPL 2.0
 */
public class TreePanel extends AbstractLayout<TreePanel, Leaf> 
	implements Scrollable<TreePanel>,HiddenContainer<TreePanel>,  HtmlContentHolder<TreePanel>,
	 PubHolder<TreePanel,Leaf>,MultiContainer<TreePanel, Leaf>,LazyLoad<TreePanel> {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6971221234006954948L;
	private Leaf pub;
	private Boolean scroll;
	private String scrollClass;
//	private String format;
    private Boolean escape;
//    private Boolean hidetoggle;
    private Combo combo;
    private Highlight highlight;
//    private String src;
    private Boolean ellipsis;
    
	/**
	 * 构造函数
	 * 
	 * @param id
	 *            String
	 */
	public TreePanel(String id) {
		super(id);
	}


//	public TreePanel add(Leaf tree) {
//		core.add(tree);
//		return this;
//	}
	/**
	 * 添加一个树节点
	 * 
	 * @param tree
	 *            Leaf
	 * @return TreePanel
	 * @deprecated 不再支持和2.x的写法
	 */
	@Deprecated
	public TreePanel addTreeItem(Leaf tree) {
		return add(tree);
	}

//	public Leaf findNodeById(String id) {
//		return (Leaf) core.findNodeById(id);
//	}
//
//	public List<FormElement<?>> findFormElementsByName(String name) {
//		return (List<FormElement<?>>) core.findFormElementsByName(name);
//	}

//	/**
//	 * 添加默认的Combox支持
//	 */
//	public void addDefaultComboboxSupport() {
//		// pub.addDefaultComboboxSupport();
//	}
//
//	/**
//	 * 添加默认的OnlineBox支持
//	 */
//	public void addDefaultOnlineboboxSupport() {
//		// pub.addDefaultOnlineboboxSupport();
//	}
//
//	/**
//	 * 拖动图标
//	 * 
//	 * @return 拖动图标
//	 */
//	public String getDragIcon() {
//		return null;// pub.getDragIcon();
//	}
//
//	/**
//	 * 拖动动作(带排序)处理接口的后台URL
//	 * 
//	 * @return 拖动动作(带排序)处理接口的后台URL
//	 */
//	public String getDragSupportOrderSrc() {
//		return null;// pub.getDragSupportOrderSrc();
//	}
//
//	/**
//	 * 拖动动作处理接口的后台URL
//	 * 
//	 * @return 拖动动作处理接口的后台URL
//	 */
//	public String getDragSupportSrc() {
//		return null;// pub.getDragSupportSrc();
//	}
//
//	/**
//	 * 默认高亮的节点PK
//	 * 
//	 * @return 默认高亮的节点PK
//	 */
//	public String getFocus() {
//		return null;// pub.getFocus();
//	}
//
//	/**
//	 * 节点过滤器 这个过滤器会排除和这个关键字无关的节点
//	 * 
//	 * @return 节点过滤器
//	 */
//	public String getItemFilter() {
//		return null;// pub.getItemFilter();
//	}
//
//	/**
//	 * 是否支持多选
//	 * 
//	 * @return 是否支持多选
//	 */
//	public Boolean getMultiSelection() {
//		return false;// pub.isMultiSelection();
//	}

//	/**
//	 * 是否显示数的折线。折线太多层的时候，会影响页面显示性能
//	 * 
//	 * @return 是否显示数的折线
//	 */
//	public Boolean getShowLine() {
//		return false;// pub.isShowLine();
//	}
//
//	/**
//	 * 支持选择框的时候，是不是显示成单选钮
//	 * 
//	 * @return 支持选择框的时候，是不是显示成单选钮
//	 */
//	public Boolean getShowSelectorAsRadio() {
//		return false;// pub.isShowSelectorAsRadio();
//	}
//
//	/**
//	 * 设置Combobox的支持
//	 * 
//	 * @param valueField
//	 *            绑定的值字段 业务上常用pk但有时PK不合适
//	 * @param textField
//	 *            绑定的显示内容字段 业务上常用 t，但有时会用路径
//	 * @param searchField
//	 *            绑定的搜索字段，比如拼音是常用的搜索字段
//	 * @return 输入+下拉的表单
//	 */
//	public TreePanel setComboboxSupport(String valueField, String textField, String searchField) {
//		// pub.setComboboxSupport(valueField, textField, searchField);
//		return this;
//	}
//
//	/**
//	 * 设置默认的动作监听
//	 * 
//	 * @param eventName
//	 *            事件名称
//	 * @param script
//	 *            动作脚本
//	 * @return 本身，这样可以继续设置其他属性
//	 */
//	public TreePanel setDefaultOn(String eventName, String script) {
//		// pub.setDefaultOn(eventName, script);
//		return this;
//	}

	// /**
	// * 设置默认值
	// * @param key
	// * @param value
	// */
	// public TreePanel setDefaultValue(String key,String value){
	// this.defaults.setProperty(key, value);
	// // defaultValues.put(key, value);
	// return this;
	// }

//	/**
//	 * 设置 拖动图标
//	 * 
//	 * @param dragIcon
//	 *            拖动图标
//	 * @return 本身，这样可以继续设置其他属性
//	 */
//	public TreePanel setDragIcon(String dragIcon) {
//		// pub.setDragIcon(dragIcon);
//		return this;
//	}
//
//	/**
//	 * 设置 拖动动作处理接口的后台URL
//	 * 
//	 * @param dragSupportSrc
//	 *            拖动动作处理接口的后台URL
//	 * @return 本身，这样可以继续设置其他属性
//	 */
//	public TreePanel setDragSupportSrc(String dragSupportSrc) {
//		// pub.setDragSupportSrc(dragSupportSrc);
//		return this;
//	}
//
//	/**
//	 * 设置 拖动动作(带排序)处理接口的后台URL
//	 * 
//	 * @param dragSupportOrderSrc
//	 *            拖动动作(带排序)处理接口的后台URL
//	 * @return 本身，这样可以继续设置其他属性
//	 */
//	public TreePanel setDragSupportOrderSrc(String dragSupportOrderSrc) {
//		// pub.setDragSupportOrderSrc(dragSupportOrderSrc);
//		return this;
//	}
//
//	/**
//	 * 设置 拖动动作处理接口的后台URL 以及拖动的图标
//	 * 
//	 * @param commitSrc
//	 *            拖动动作处理接口的后台URL
//	 * @param icon
//	 *            拖动图标
//	 * @return 本身，这样可以继续设置其他属性
//	 */
//	public TreePanel setDragSupprot(String commitSrc, String icon) {
//		// pub.setDragSupprot(commitSrc, icon);
//		return this;
//	}
//
//	/**
//	 * 设置 拖动动作(带排序)处理接口的后台URL
//	 * 
//	 * @param commitSrc
//	 *            拖动动作处理接口的后台URL
//	 * @param icon
//	 *            拖动图标
//	 * @param dragSupportOrderSrc
//	 *            拖动动作(带排序)处理接口的后台URL
//	 * @return 本身，这样可以继续设置其他属性
//	 */
//	public TreePanel setDragSupprot(String commitSrc, String icon, String dragSupportOrderSrc) {
//		// pub.setDragSupprot(commitSrc, icon, dragSupportOrderSrc);
//		return this;
//	}

	/**
	 * 设置默认高亮的节点PK
	 * 
	 * @param pkid
	 *            高亮的节点PK
	 * @return 本身，这样可以继续设置其他属性
	 */
	public TreePanel setFocus(String pkid) {
		// pub.setFocus(pkid);
		return this;
	}

//	/**
//	 * 设置节点过滤器 这个过滤器会排除和这个关键字无关的节点
//	 * 
//	 * @param itemFilter
//	 *            节点过滤器
//	 * @return 本身，这样可以继续设置其他属性
//	 */
//	public TreePanel setItemFilter(String itemFilter) {
//		// pub.setItemFilter(itemFilter);
//		return this;
//	}



	public String getType() {
		return "tree";
	}

//	public List<Leaf> getNodes() {
//		return core.getNodes();
//	}

	// public Map<String, Object> getDefaultValues() {
	// return defaultValues;
	// }

	public Leaf getPub() {
		if(pub==null){
			pub=new Leaf();
		}
		return pub;
	}

	public TreePanel setPub(Leaf pub) {
		this.pub = pub;
		return this;
	}

    public TreePanel setScroll(Boolean scroll) {
    	this.scroll = scroll;
	    return this;
    }

    public Boolean getScroll() {
	    return this.scroll;
    }

    public TreePanel setScrollClass(String scrollClass) {
    	this.scrollClass = scrollClass;
	    return this;
    }

    public String getScrollClass() {
	    return this.scrollClass;
    }
	
    HiddenPart hiddens = new HiddenPart();
    public TreePanel addHidden(String name,String value) {
    	hiddens.addHidden(name,value);
		return this;
    }
    public TreePanel add(Hidden hidden) {
    	hiddens.add(hidden);
		return this;
    }

    public List<Hidden> getHiddens() {
	    return hiddens.getHiddens();
    }

    public List<String> getHiddenValue(String name) {
	    return hiddens.getHiddenValue(name);
    }

    public TreePanel removeHidden(String name) {
    	hiddens.removeHidden(name);
	    return this;
    }
	public Boolean getEscape() {
		return this.escape;
	}
	
	public TreePanel setEscape(Boolean escape) {
		this.escape = escape;
		return this;
	}



	/**
	 * 是否隐藏 toggle 图标。
	 * @deprecated 转移到{@link #getPub()}
	 * @see Leaf#setHidetoggle(Boolean)
	 * @param hidetoggle Boolean
	 * @return 本身，这样可以继续设置其他属性
	 */
	public TreePanel setHidetoggle(Boolean hidetoggle) {
		getPub().setHidetoggle(hidetoggle);
		return this;
	}
	/**
	 * 格式化内容。"$字段名"形式的变量将被解析替换。支持"javascript:"开头的js语句(需return返回值)。
	 * 不设置相当于默认显示就是${text}
	 * @deprecated 转移到{@link #getPub()}
	 * @see Leaf#getFormat()
	 * @return String
	 */
	public String getFormat() {
		return getPub().getFormat();
	}


	/**
	 * 格式化内容。"$字段名"形式的变量将被解析替换。支持"javascript:"开头的js语句(需return返回值)。
	 * 不设置相当于默认显示就是${text}
	 * @deprecated 转移到{@link #getPub()}
	 * @see Leaf#setFormat(String)
	 * @param format String
	 * @return 本身，这样可以继续设置其他属性
	 */
	public TreePanel setFormat(String format) {
		getPub().setFormat(format);
		return this;
	}

	/**
	 * 设置当前的 tree 为某个 combobox 或 onlinebox 的数据选项表。
	 * @return Combo
	 */
	public Combo getCombo() {
		return combo;
	}

	/**
	 * 设置当前的 tree 为某个 combobox 或 onlinebox 的数据选项表。
	 * @param combo Combo
	 * @return 本身，这样可以继续设置其他属性
	 */
	public TreePanel setCombo(Combo combo) {
		this.combo = combo;
		return this;
	}

	/**
	 * 设置树的src
	 * @return String
	 */
	public String getSrc() {
		return rootLeaf.getSrc();
	}

	/**
	 * 设置树的src
	 * @param src String
	 * @return 本身，这样可以继续设置其他属性
	 */
	public TreePanel setSrc(String src) {
		rootLeaf.setSrc(src);
		return this;
	}


	@Override
	public List<Leaf> getNodes() {
		return rootLeaf.getNodes();
	}
	private Leaf rootLeaf=new Leaf();
	/**
	 * 取得根节点。这样树面板可以直接使用Leaf的接口
	 * @return Leaf
	 */
	public Leaf rootLeaf(){
		return rootLeaf;
	}


	@SuppressWarnings("unchecked")
	@Override
	public List<Leaf> findNodes() {
		return rootLeaf.findNodes();
	}


	@Override
	public TreePanel add(Leaf w) {
		rootLeaf.add(w);
		return this;
	}
	
	@Override
	public TreePanel add(int index,Leaf w) {
		rootLeaf.add(index,w);
		return this;
	}


	@Override
	public TreePanel removeNodeById(String id) {
		rootLeaf.removeNodeById(id);
		return this;
	}

	@Override
	public boolean replaceNodeById(Widget<?> w) {
		return rootLeaf.replaceNodeById(w);
	}
	
	/**
	 * 修复叶节点的选中状态
	 * @return 本身，这样可以继续设置其他属性
	 */
	public TreePanel fixBoxCheckStatus() {
		// FIXME 根节点必须补box
		rootLeaf.setBox(new Triplebox(null, null, null, null, null));
		rootLeaf.fixBoxCheckStatus();
		return this;
	}

	/**
	 * 高亮关键词配置
	 * @return Highlight
	 * @author YuLM
	 */
	public Highlight getHighlight() {
		return highlight;
	}

	/**
	 * 高亮关键词配置
	 * @param highlight Highlight
	 * @return 本身，这样可以继续设置其他属性
	 * @author YuLM
	 */
	public TreePanel setHighlight(Highlight highlight) {
		this.highlight = highlight;
		return this;
	}
	/**
	 * 文本超出可视范围部分以省略号显示，默认为true
	 * @return Boolean
	 */
    public Boolean getEllipsis() {
		return ellipsis;
	}
    /**
     * 文本超出可视范围部分以省略号显示，默认为true
     * @param ellipsis Boolean
     * @return 本身，这样可以继续设置其他属性
     */
	public TreePanel setEllipsis(Boolean ellipsis) {
		this.ellipsis = ellipsis;
		return this;
	}


	@Override
	public String getTemplate() {
		return rootLeaf.getTemplate();
	}


	@Override
	public TreePanel setTemplate(String template) {
		rootLeaf.setTemplate(template);
		return this;
	}
	
}
