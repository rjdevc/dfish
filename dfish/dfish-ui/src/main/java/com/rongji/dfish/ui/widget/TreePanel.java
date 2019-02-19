package com.rongji.dfish.ui.widget;

import java.util.List;

import com.rongji.dfish.ui.AtExpression;
import com.rongji.dfish.ui.Combo;
import com.rongji.dfish.ui.HasSrc;
import com.rongji.dfish.ui.HiddenContainer;
import com.rongji.dfish.ui.HiddenPart;
import com.rongji.dfish.ui.Highlight;
import com.rongji.dfish.ui.HtmlContentHolder;
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
	 PubHolder<TreePanel,Leaf>,MultiContainer<TreePanel, Leaf>,HasSrc<TreePanel> {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6971221234006954948L;
	private Leaf pub;
	private Boolean scroll;
	private String scrollClass;
    private Boolean escape;
    private Combo combo;
    private Highlight highlight;
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


	/**
	 * 设置默认高亮的节点PK
	 * 
	 * @param pkid
	 *            高亮的节点PK
	 * @return 本身，这样可以继续设置其他属性
	 * @deprecated 必须找到指定的节点，并设置setFocus(true);
	 */
	@Deprecated
	public TreePanel setFocus(String pkid) {
		// pub.setFocus(pkid);
		return this;
	}




	public String getType() {
		return "tree";
	}

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
//    public TreePanel addHidden(String name,AtExpression value) {
//    	hiddens.addHidden(name,value);
//		return this;
//    }
    
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
	 * @deprecated 在pub中设置该值
	 * @see #getPub()
	 */
	@Deprecated
    public Boolean getEllipsis() {
		return ellipsis;
	}
    /**
     * 文本超出可视范围部分以省略号显示，默认为true
     * @param ellipsis Boolean
     * @return 本身，这样可以继续设置其他属性
     * @deprecated 在pub中设置该值
     * @see #getPub()
     */
    @Deprecated
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
