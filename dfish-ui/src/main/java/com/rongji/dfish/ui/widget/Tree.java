package com.rongji.dfish.ui.widget;

import com.rongji.dfish.ui.*;
import com.rongji.dfish.ui.form.AbstractBox;
import com.rongji.dfish.ui.form.Hidden;
import com.rongji.dfish.ui.form.TripleBox;

import java.util.*;

/**
 * TreePanel 树的面板 这个面板里面可以防止树节点Leaf
 *
 * @author DFish Team
 * @version 1.0
 * @since XMLTMPL 2.0
 */
public class Tree extends AbstractPubNodeContainer<Tree, Tree.Leaf>
        implements Scrollable<Tree>, HiddenContainer<Tree>, HtmlContentHolder<Tree>, LazyLoad<Tree> {
    /**
     *
     */
    private static final long serialVersionUID = -6971221234006954948L;
    private Boolean scroll;
//    private Combo combo;
    private Highlight highlight;
    private Boolean ellipsis;

    /**
     * 构造函数
     *
     * @param id String
     */
    public Tree(String id) {
        super(id);
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
        return new Leaf();
    }

    private Leaf rootLeaf = new Leaf();

    /**
     * 取得根节点。这样树面板可以直接使用Leaf的接口
     *
     * @return Leaf
     */
    public Leaf rootLeaf() {
        return rootLeaf;
    }


    @SuppressWarnings("unchecked")
    @Override
    public List<Node> findNodes() {
        return rootLeaf.findNodes();
    }


    @Override
    public Tree add(Node node) {
        if (!(node instanceof Leaf)) {
            throw new IllegalArgumentException("node must be Tree.Leaf");
        }
        rootLeaf.add(node);
        return this;
    }

    @Override
    public Tree add(int index, Leaf w) {
        rootLeaf.add(index, w);
        return this;
    }


    @Override
    public Tree removeNodeById(String id) {
        rootLeaf.removeNodeById(id);
        return this;
    }

    @Override
    public boolean replaceNodeById(Node w) {
        return rootLeaf.replaceNodeById(w);
    }

    /**
     * 修复叶节点的选中状态
     *
     * @return 本身，这样可以继续设置其他属性
     */
    public Tree fixBoxCheckStatus() {
        // FIXME 根节点必须补box
        rootLeaf.setBox(new TripleBox(null, null, null, null, null));
        rootLeaf.fixBoxCheckStatus();
        return this;
    }

    /**
     * 高亮关键词配置
     *
     * @return Highlight
     * @author lamontYu
     */
    public Highlight getHighlight() {
        return highlight;
    }

    /**
     * 高亮关键词配置
     *
     * @param highlight Highlight
     * @return 本身，这样可以继续设置其他属性
     * @author lamontYu
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
    @Deprecated
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
    @Deprecated
    public Tree setEllipsis(Boolean ellipsis) {
        this.ellipsis = ellipsis;
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

    /**
     * <p>Leaf 树节点 在3.0以前的版本叫TreeItem</p>
     * <p>它允许有0个到多个子节点。</p>
     * <p>默认的属性有</p>
     * <ul>
     * <li><b>id(原2.x叫pk)</b> 逻辑编号，字符串</li>
     * <li><b>t</b> 显示文本</li>
     * <li><b>src</b> 展开时向服务端获取XML的URL</li>
     * <li><b>ic</b> 图标</li>
     * <li><b>oic</b> 展开时的图标</li>
     * <li><b>act</b> 点击动作</li>
     * <li><b>menu</b> 右键时调用的命令</li>
     * </ul>
     *
     * @author DFish Team
     * @version 2.0
     * @since XMLTMPL 1.0
     */
    public static class Leaf extends AbstractNodeContainer<Leaf> implements MultiNodeContainer<Leaf>, Statusful<Leaf>, HtmlContentHolder<Leaf>, LazyLoad<Leaf>, HasText<Leaf> {
        private static final long serialVersionUID = -6246121270694425393L;
        private Boolean focus;
        private Boolean focusable;
        private String icon;
        private String expandedIcon;
        private Boolean expanded;
        private String text;
        private Object tip;
        private String src;
        private Boolean sync;
        private Boolean noToggle;
        private AbstractBox<?> box;
        private Boolean ellipsis;
        private Boolean line;
        private String format;
        private String status;
        private Boolean folder;
        private Boolean escape;
        private String success;
        private String error;
        private String complete;
        private String filter;
        private Object badge;

        /**
         * 默认构造函数,因为树经常没有下级,但一般有属性,所默认创建属性的list
         */
        public Leaf() {
            super(null);
        }

        /**
         * 构造函数,
         *
         * @param id   String
         * @param text 显示文本
         */
        public Leaf(String id, String text) {
            super(id);
            this.setText(text);
        }


        /**
         * 在这棵树下找到第一个id为指定id的节点
         * (注意:这里的id就是属性里面的id)
         *
         * @param id String
         * @return Tree
         */
        public List<Leaf> findPathById(String id) {
            LinkedList<Leaf> result = new LinkedList<Leaf>();
            findPathById(result, this, id);
            return result;
        }

        private static boolean findPathById(LinkedList<Leaf> path, Leaf tree, String pkid) {
            path.add(tree);
            if (pkid == null && tree.getId() == null) {
                return true;
            }
            if (pkid != null && pkid.equals(tree.getId())) {
                return true;
            }
            if (tree.nodes != null) {
                for (Object obj : tree.nodes) {
                    Leaf elem = (Leaf) obj;
                    if (findPathById(path, elem, pkid)) {
                        return true;
                    }
                }
            }
            path.removeLast();
            return false;
        }

        /**
         * 输出本节点的路径，主要用于调试
         *
         * @param path 路径
         * @return String
         */
        public static String toString(List<Leaf> path) {
            StringBuilder sb = new StringBuilder();
            sb.append("path:[");
            for (Iterator<Leaf> iter = path.iterator(); iter.hasNext(); ) {
                Leaf element = iter.next();
                sb.append(element.getText())
                        .append('(')
                        .append(element.getId())
                        .append(')');
                if (iter.hasNext()) {
                    sb.append(" - ");
                }
            }
            sb.append(']');
            return sb.toString();
        }

        /**
         * 添加另一颗树的子树
         *
         * @param otherTree Tree
         * @return Tree
         */
        public Leaf addAllSubLeaf(Leaf otherTree) {
            if (nodes == null) {
                nodes = new ArrayList<>();
            }
            if (otherTree.nodes != null) {
                nodes.addAll(otherTree.nodes);
            }
            return this;
        }

        /**
         * 删除一颗树的子树
         *
         * @return Tree
         */
        public Leaf clearAllSubLeaf() {
            nodes.clear();//help gc
            nodes = null;
            return this;
        }

        /**
         * 复制另一颗树的内容,包括属性和子树.
         * 注意是浅拷贝,改动这个树的内容将会影响原先的树.
         *
         * @param otherLeaf Tree
         */
        public void copyFromAnotherLeaf(Leaf otherLeaf) {
//        this.dataPart = otherLeaf.dataPart;
            this.data = otherLeaf.data;
            this.nodes = otherLeaf.nodes;
        }

        /**
         * 复制另一颗树的内容,包括属性和子树.
         * 注意是深拷贝,速度比较慢.
         *
         * @param otherLeaf Tree
         */
        public void copyFromAnotherLeafSafe(Leaf otherLeaf) {
            this.data = new LinkedHashMap<String, Object>(otherLeaf.data);
            if (otherLeaf.nodes != null) {
                for (Object obj : otherLeaf.nodes) {
                    Leaf elem = (Leaf) obj;
                    Leaf t = new Leaf();
                    t.copyFromAnotherLeafSafe(elem);
                    add(t);
                }
            }
        }


        /**
         * 添加一个或多个节点
         *
         * @param subTree 子节点
         * @return 自身
         * @deprecated 2.x的写法已经不再支持
         */
        @Deprecated
        public Leaf addTreeItem(Leaf... subTree) {
            if (subTree != null && subTree.length > 0) {
                for (int i = 0; i < subTree.length; i++) {
                    add(subTree[i]);
                }
            }
            return this;
        }

        /**
         * 选项表单，类型是 checkbox 或 radio。取消或勾选这个box，将同步fieldset内部所有表单的状态。
         *
         * @return AbstractBox
         */
        public AbstractBox<?> getBox() {
            return box;
        }


        /**
         * 选项表单，类型是 checkbox 或 radio。取消或勾选这个box，将同步fieldset内部所有表单的状态。
         *
         * @param box 选项表单
         * @return 本身，这样可以继续设置其他属性
         */
        public Leaf setBox(AbstractBox<?> box) {
            this.box = box;
            return this;
        }

        /**
         * 是否焦点状态。
         *
         * @return focus
         */
        public Boolean getFocus() {
            return focus;
        }

        /**
         * 是否焦点状态。
         *
         * @param focus Boolean
         * @return 本身，这样可以继续设置其他属性
         */
        public Leaf setFocus(Boolean focus) {
            this.focus = focus;
            return this;
        }

        /**
         * 是否可聚焦
         *
         * @return Boolean
         */
        public Boolean getFocusable() {
            return focusable;
        }

        /**
         * 是否可聚焦
         *
         * @param focusable 是否可聚焦
         * @return 本身，这样可以继续设置其他属性
         */
        public Leaf setFocusable(Boolean focusable) {
            this.focusable = focusable;
            return this;
        }

        /**
         * 图标。可使用图片url地址，或以 "." 开头的样式名。
         *
         * @return icon
         */
        public String getIcon() {
            return icon;
        }

        /**
         * 图标。可使用图片url地址，或以 "." 开头的样式名。
         *
         * @param icon 图标 闭合时的图标
         * @return 本身，这样可以继续设置其他属性
         */
        public Leaf setIcon(String icon) {
            this.icon = icon;
            return this;
        }

        /**
         * 展开图标
         *
         * @return openicon
         */
        public String getExpandedIcon() {
            return expandedIcon;
        }

        /**
         * 展开图标
         *
         * @param expandedIcon 展开图标
         * @return 本身，这样可以继续设置其他属性
         */
        public Leaf setExpandedIcon(String expandedIcon) {
            this.expandedIcon = expandedIcon;
            return this;
        }


        /**
         * 是否展开状态。
         *
         * @return open
         */
        public Boolean getExpanded() {
            return expanded;
        }

        /**
         * 是否展开状态。
         *
         * @param expanded Boolean
         * @return 本身，这样可以继续设置其他属性
         */
        public Leaf setExpanded(Boolean expanded) {
            this.expanded = expanded;
            return this;
        }

        /**
         * 显示文本。
         *
         * @return text
         */
        @Override
        public String getText() {
            return text;
        }

        /**
         * 显示文本。
         *
         * @param text 显示文本
         * @return 本身，这样可以继续设置其他属性
         */
        @Override
        public Leaf setText(String text) {
            this.text = text;
            return this;
        }


        /**
         * 提示信息。设为true，提示信息将使用text参数的值。
         *
         * @return tip
         */
        public Object getTip() {
            return tip;
        }

        /**
         * 提示信息。设为true，提示信息将使用text参数的值。
         *
         * @param tip Boolean
         * @return 本身，这样可以继续设置其他属性
         */
        public Leaf setTip(Boolean tip) {
            this.tip = tip;
            return this;
        }

        /**
         * 提示信息。设为true，提示信息将使用text参数的值。
         *
         * @param tip 提示信息
         * @return 本身，这样可以继续设置其他属性
         */
        public Leaf setTip(String tip) {
            this.tip = tip;
            return this;
        }

        /**
         * 获取子节点的URL地址。
         *
         * @return src
         */
        @Override
        public String getSrc() {
            return src;
        }

        /**
         * 获取子节点的URL地址。
         *
         * @param src URL地址
         * @return 本身，这样可以继续设置其他属性
         */
        @Override
        public Leaf setSrc(String src) {
            this.src = src;
            return this;
        }

        /**
         * 是否隐藏 toggle 图标。
         *
         * @return Boolean
         */
        public Boolean getNoToggle() {
            return noToggle;
        }

        /**
         * 是否隐藏 toggle 图标。
         *
         * @param noToggle Boolean
         * @return 本身，这样可以继续设置其他属性
         */
        public Leaf setNoToggle(Boolean noToggle) {
            this.noToggle = noToggle;
            return this;
        }

        /**
         * 文本超出可视范围部分以省略号显示，默认为true
         *
         * @return Boolean
         * @deprecated 在TreePanel中支持该参数，Leaf中移除
         */
        @Deprecated
        public Boolean getEllipsis() {
            return ellipsis;
        }

        /**
         * 文本超出可视范围部分以省略号显示，默认为true
         *
         * @param ellipsis Boolean
         * @return 本身，这样可以继续设置其他属性
         * @deprecated 在TreePanel中支持该参数，Leaf中移除
         */
        @Deprecated
        public Leaf setEllipsis(Boolean ellipsis) {
            this.ellipsis = ellipsis;
            return this;
        }

        /**
         * 是否显示树结构的辅助线
         *
         * @return Boolean
         */
        public Boolean getLine() {
            return line;
        }

        /**
         * 是否显示树结构的辅助线
         *
         * @param line Boolean
         * @return 本身，这样可以继续设置其他属性
         */
        public Leaf setLine(Boolean line) {
            this.line = line;
            return this;
        }

        @Override
        public List<Node> getNodes() {
            return (List) nodes;
        }

        private static String[] TEXT_PROP_NAMES = {"text", "value"};

        /**
         * 取得标签的文本
         *
         * @return String
         */
        @Deprecated
        public String getTextAsString() {
            return getText();
        }

        /**
         * 级联修复选择框的状态，仅支持Triplebox的状态修复，且根据{@link TripleBox#getChecked()} 来判断，true代表该节点被选中
         *
         * @return 本身，这样可以继续设置其他属性
         * @deprecated 该方法无需调用, 前端引擎已实现自动计算
         */
        @Deprecated
        public Leaf fixBoxCheckStatus() {
            if (this.getBox() == null || !(this.getBox() instanceof TripleBox)) {
                // 无选择框或者选择框不是Triplebox 直接返回无需修复
                return this;
            }

//        fixBoxCheckStatusCascade();

            return this;
        }

//    /**
//     * 级联修复下级选择框的选中状态
//     *
//     * @return this
//     */
//    private int fixBoxCheckStatusCascade() {
//
//        Triplebox triplebox = (Triplebox) this.getBox();
//
//        List<Leaf> sugList = getNodes();
//        // 默认未选中
//        int status = Triplebox.CHECKSTATE_UNCHECKED;
//        if (Utils.notEmpty(sugList)) { // 下级节点不为空的情况
//            // 子节点的选中状态
//            Set<Integer> subStatusSet = new HashSet<Integer>(3);
//            for (Leaf sub : sugList) {
//                int subStatus = sub.fixBoxCheckStatusCascade();
//                subStatusSet.add(subStatus);
//            }
//            if (subStatusSet.size() > 1) {
//                // 至少2种状态说明肯定是,当前级肯定是半选状态
//                status = Triplebox.CHECKSTATE_PARTIALCHECKED;
//            } else {
//                // 理论上不可能出现为空的情况,故不进行判断
//                for (Integer subStatus : subStatusSet) {
//                    // 其他情况下级存什么状态,他的上级也是什么状态
//                    status = subStatus;
//                }
//            }
//        } else { // 没有下级节点
//            // 使用这个方法必须设置checked
//            Integer boxStatus = triplebox.getCheckstate();
//            if (boxStatus != null && boxStatus == Triplebox.CHECKSTATE_CHECKED) {
//                // 选中
//                status = Triplebox.CHECKSTATE_CHECKED;
//            }
//        }
//        // 设置选中状态
//        triplebox.setCheckstate(status);
//
//        return status;
//    }

        /**
         * 格式化内容。支持替换 "$field" 和 "${field.prop}" 形式的变量。支持"javascript:"开头的js语句(需return返回值)。
         * 如果列表有多行，并且这个字段显示的时候，需要一个复杂HTML，而每行中需要的变化的仅仅是少量的数据，可以使用format来减少传输量。
         * 典型的有两种写法
         * <pre>
         * javascript:var d= this.x.data.s;if('1'==d){return \"&lt;span style='color:gray'&gt;唯一&lt;/span&gt;\"};return '';
         * </pre>或<pre>
         * [&lt;a href='javascript:;' onclick=\"demo.enterView(this,'$vId');\"&gt;查看&lt;/a&gt;]&amp;nbsp;
         * </pre>
         *
         * @return String
         */
        @Override
        public String getFormat() {
            return format;
        }

        /**
         * 格式化内容。支持替换 "$field" 和 "${field.prop}" 形式的变量。支持"javascript:"开头的js语句(需return返回值)。
         * 如果列表有多行，并且这个字段显示的时候，需要一个复杂HTML，而每行中需要的变化的仅仅是少量的数据，可以使用format来减少传输量。
         * 典型的有两种写法
         * <pre>
         * javascript:var d= this.x.data.s;if('1'==d){return \"&lt;span style='color:gray'&gt;唯一&lt;/span&gt;\"};return '';
         * </pre>或<pre>
         * [&lt;a href='javascript:;' onclick=\"demo.enterView(this,'$vId');\"&gt;查看&lt;/a&gt;]&amp;nbsp;
         * </pre>
         *
         * @param format String
         * @return 本身，这样可以继续设置其他属性
         */
        @Override
        public Leaf setFormat(String format) {
            this.format = format;
            return this;
        }

        @Override
        public String getStatus() {
            return status;
        }

        @Override
        public Leaf setStatus(String status) {
            this.status = status;
            return this;
        }

        /**
         * 是否为一个可展开的目录，如果不设置本参数，那么引擎将根据是否有src属性或leaf子节点来自动判断
         *
         * @return Boolean
         * @since 3.2.0
         */
        public Boolean getFolder() {
            return folder;
        }

        /**
         * 是否为一个可展开的目录，如果不设置本参数，那么引擎将根据是否有src属性或leaf子节点来自动判断
         *
         * @param folder Boolean
         * @return 本身，这样可以继续设置其他属性
         */
        public Leaf setFolder(Boolean folder) {
            this.folder = folder;
            return this;
        }

        /**
         * 在指定的位置添加子面板
         *
         * @param index 位置
         * @param w     N
         * @return 本身，这样可以继续设置其他属性
         */
        public Leaf add(int index, Leaf w) {
            if (w == null) {
                return this;
            }
            if (w == this) {
                throw new IllegalArgumentException("can not add widget itself as a sub widget");
            }
            if (index < 0) {
                nodes.add(w);
            } else {
                nodes.add(index, w);
            }
            return this;
        }

        @Override
        public Boolean getEscape() {
            return escape;
        }

        @Override
        public Leaf setEscape(Boolean escape) {
            this.escape = escape;
            return this;
        }

        @Override
        public String getSuccess() {
            return success;
        }

        @Override
        public Leaf setSuccess(String success) {
            this.success = success;
            return this;
        }

        @Override
        public String getError() {
            return error;
        }

        @Override
        public Leaf setError(String error) {
            this.error = error;
            return this;
        }

        @Override
        public String getComplete() {
            return complete;
        }

        @Override
        public Leaf setComplete(String complete) {
            this.complete = complete;
            return this;
        }

        @Override
        public String getFilter() {
            return filter;
        }

        @Override
        public Leaf setFilter(String filter) {
            this.filter = filter;
            return this;
        }

        @Override
        public Boolean getSync() {
            return sync;
        }

        @Override
        public Leaf setSync(Boolean sync) {
            this.sync = sync;
            return this;
        }

        /**
         * 显示徽标
         *
         * @return Object
         */
        public Object getBadge() {
            return badge;
        }

        /**
         * 显示徽标
         *
         * @param badge 为true时显示圆点
         * @return 本身，这样可以继续设置其他属性
         */
        public Leaf setBadge(Boolean badge) {
            this.badge = badge;
            return this;
        }

        /**
         * 显示徽标
         *
         * @param badge 要显示的徽标对象
         * @return 本身，这样可以继续设置其他属性
         */
        public Leaf setBadge(Badge badge) {
            this.badge = badge;
            return this;
        }
    }
}
