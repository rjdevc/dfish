package com.rongji.dfish.ui.widget;

import com.rongji.dfish.ui.*;
import com.rongji.dfish.ui.auxiliary.Highlight;
import com.rongji.dfish.ui.auxiliary.Leaf;
import com.rongji.dfish.ui.form.Hidden;
import com.rongji.dfish.ui.form.TripleBox;

import java.util.List;

/**
 * TreePanel 树的面板 这个面板里面可以防止树节点Leaf
 *
 * @author DFish Team
 * @version 1.0
 * @since XMLTMPL 2.0
 */
public class Tree extends AbstractPubNodeContainer<Tree, Leaf, Leaf>
        implements Scrollable<Tree>, HiddenContainer<Tree>, HtmlContentHolder<Tree>, LazyLoad<Tree> {
    /**
     *
     */
    private static final long serialVersionUID = -6971221234006954948L;
    private Boolean scroll;
//    private Combo combo;
    private Highlight highlight;
    private Boolean ellipsis;
    private Boolean rootInvisible;

    /**
     * 构造函数
     *
     * @param id String
     */
    public Tree(String id) {
        super(id);

    }
    public Tree() {
        this(null);
    }

    @Override
    public Tree setScroll(Boolean scroll) {
        this.scroll = scroll;
        return this;
    }

    @Override
    public Boolean getScroll() {
        return this.scroll;
    }

    HiddenPart hiddens = new HiddenPart();

    @Override
    public Tree addHidden(String name, String value) {
        hiddens.addHidden(name, value);
        return this;
    }
//    public TreePanel addHidden(String name,AtExpression value) {
//    	hiddens.addHidden(name,value);
//		return this;
//    }

    @Override
    public Tree add(Hidden hidden) {
        hiddens.add(hidden);
        return this;
    }

    @Override
    public List<Hidden> getHiddens() {
        return hiddens.getHiddens();
    }

    @Override
    public List<String> getHiddenValue(String name) {
        return hiddens.getHiddenValue(name);
    }

    @Override
    public Tree removeHidden(String name) {
        hiddens.removeHidden(name);
        return this;
    }

    /**
     * 格式化内容。
     *
     * @return Boolean
     * @see #getPub()
     */
    @Override
    @Deprecated
    public Boolean getEscape() {
        return getPub().getEscape();
    }

    /**
     * 格式化内容。
     *
     * @return 本身，这样可以继续设置其他属性
     * @see #getPub()
     */
    @Override
    public Tree setEscape(Boolean escape) {
        getPub().setEscape(escape);
        return this;
    }


    /**
     * 是否隐藏 toggle 图标。
     *
     * @param noToggle Boolean
     * @return 本身，这样可以继续设置其他属性
     * @see Leaf#setNoToggle(Boolean)
     * @deprecated 转移到{@link #getPub()}
     */
    public Tree setNoToggle(Boolean noToggle) {
        getPub().setNoToggle(noToggle);
        return this;
    }

    /**
     * 格式化内容。"$字段名"形式的变量将被解析替换。支持"javascript:"开头的js语句(需return返回值)。
     * 不设置相当于默认显示就是${text}
     *
     * @return String
     * @see Leaf#getFormat()
     * @deprecated 转移到{@link #getPub()}
     */
    public String getFormat() {
        return getPub().getFormat();
    }


    /**
     * 格式化内容。"$字段名"形式的变量将被解析替换。支持"javascript:"开头的js语句(需return返回值)。
     * 不设置相当于默认显示就是${text}
     *
     * @param format String
     * @return 本身，这样可以继续设置其他属性
     * @see Leaf#setFormat(String)
     * @deprecated 转移到{@link #getPub()}
     */
    public Tree setFormat(String format) {
        getPub().setFormat(format);
        return this;
    }

//    /**
//     * 设置当前的 tree 为某个 combobox 或 onlinebox 的数据选项表。
//     *
//     * @return Combo
//     */
//    public Combo getCombo() {
//        return combo;
//    }
//
//    /**
//     * 设置当前的 tree 为某个 combobox 或 onlinebox 的数据选项表。
//     *
//     * @param combo Combo
//     * @return 本身，这样可以继续设置其他属性
//     */
//    public Tree setCombo(Combo combo) {
//        this.combo = combo;
//        return this;
//    }

    /**
     * 设置树的src
     *
     * @return String
     */
    @Override
    public String getSrc() {
        return rootLeaf.getSrc();
    }

    /**
     * 设置树的src
     *
     * @param src String
     * @return 本身，这样可以继续设置其他属性
     */
    @Override
    public Tree setSrc(String src) {
        rootLeaf.setSrc(src);
        return this;
    }


    @Override
    public List getNodes() {
        return rootLeaf.getNodes();
    }

    @Override
    protected Leaf newPub() {
        return new Leaf(null);
    }

    private Leaf rootLeaf = new Leaf(null);

    /**
     * 取得根节点。这样树面板可以直接使用Leaf的接口
     *
     * @return Leaf
     */
    public Leaf rootLeaf() {
        return rootLeaf;
    }


    /**
     * 高亮关键词配置
     *
     * @return Highlight
     */
    public Highlight getHighlight() {
        return highlight;
    }

    /**
     * 高亮关键词配置
     *
     * @param highlight Highlight
     * @return 本身，这样可以继续设置其他属性
     */
    public Tree setHighlight(Highlight highlight) {
        this.highlight = highlight;
        return this;
    }

    /**
     * 文本超出可视范围部分以省略号显示，默认为true
     *
     * @return Boolean
     * @see #getPub()
     * @deprecated 在pub中设置该值
     */
    public Boolean getEllipsis() {
        return ellipsis;
    }

    /**
     * 文本超出可视范围部分以省略号显示，默认为true
     *
     * @param ellipsis Boolean
     * @return 本身，这样可以继续设置其他属性
     * @see #getPub()
     * @deprecated 在pub中设置该值
     */
    public Tree setEllipsis(Boolean ellipsis) {
        this.ellipsis = ellipsis;
        return this;
    }

    /**
     * 根节点是否可见。设为true，隐藏根节点，根节点的子节点缩进一层。
     * @return Boolean
     */
    public Boolean getRootInvisible() {
        return rootInvisible;
    }

    /**
     * 根节点是否可见。设为true，隐藏根节点，根节点的子节点缩进一层。
     * @param rootInvisible Boolean
     * @return 本身，这样可以继续设置其他属性
     */
    public Tree setRootInvisible(Boolean rootInvisible) {
        this.rootInvisible = rootInvisible;
        return this;
    }

    @Override
    public String getSuccess() {
        return rootLeaf.getSuccess();
    }

    @Override
    public Tree setSuccess(String success) {
        rootLeaf.setSuccess(success);
        return this;
    }

    @Override
    public String getError() {
        return rootLeaf.getError();
    }

    @Override
    public Tree setError(String error) {
        rootLeaf.setError(error);
        return this;
    }

    @Override
    public String getComplete() {
        return rootLeaf.getComplete();
    }

    @Override
    public Tree setComplete(String complete) {
        rootLeaf.setComplete(complete);
        return this;
    }

    @Override
    public String getFilter() {
        return rootLeaf.getFilter();
    }

    @Override
    public Tree setFilter(String filter) {
        rootLeaf.setFilter(filter);
        return this;
    }

    @Override
    public Boolean getSync() {
        return rootLeaf.getSync();
    }

    @Override
    public Tree setSync(Boolean sync) {
        rootLeaf.setSync(sync);
        return this;
    }

    @Override
    protected NodeContainerDecorator getNodeContainerDecorator(){
        return new NodeContainerDecorator() {
            @Override
            protected  List<Leaf> nodes() {
                return rootLeaf.getNodes();
            }

            @Override
            protected void setNode(int i, Node node) {
                if(node==null){
                    rootLeaf.getNodes().remove(i);
                }else{
                    rootLeaf.getNodes().set(i,(Leaf)node);
                }
            }
        };
    }

}
