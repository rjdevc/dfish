package com.rongji.dfish.ui.command;


import com.rongji.dfish.ui.AbstractNodeContainerPart;
import com.rongji.dfish.ui.Node;
import com.rongji.dfish.ui.MultiNodeContainer;
import com.rongji.dfish.ui.Widget;
import com.rongji.dfish.ui.widget.AbstractButton;
import com.rongji.dfish.ui.widget.Split;

import java.util.ArrayList;
import java.util.List;

/**
 * 显示一个菜单。menu 既是命令，也是 widget。
 *
 * @author DFish Team
 * @version 2.1 lamontYu 所有属性和type按照驼峰命名方式调整
 * @date 2018-08-03 before
 * @since 2.0
 */
public class Menu extends AbstractDialog<Menu> implements Command<Menu>, MultiNodeContainer<Menu,Widget> {

    private static final long serialVersionUID = 7565861352403693874L;

    /**
     * 默认构造函数
     */
    public Menu() {
    }

    private List<Widget> nodes;
//    private Boolean prong;

    /**
     * 支持增加子按钮
     *
     * @param btn 按钮
     * @return 本身，这样可以继续设置其他属性
     */
    public Menu add(AbstractButton btn) {
        add((Widget) btn);
        return this;
    }

    /**
     * 支持增加分隔符
     *
     * @param split Split
     * @return 本身，这样可以继续设置其他属性
     */
    public Menu add(Split split) {
        add((Widget) split);
        return this;
    }

    @Override
    @Deprecated
    public Menu setNode(Widget node) {
        throw new UnsupportedOperationException("deprecated. see addNode");
    }

    @Override
    public List<Node> getNodes() {
        return (List)nodes;
    }
    @Override
    public Menu setNodes(List<Widget> nodes) {
        this.nodes=nodes;
        return this;
    }




//    /**
//     * 设为 true，显示一个箭头，指向 snap 参数对象。
//     *
//     * @return Boolean
//     */
//    public Boolean getProng() {
//        return prong;
//    }
//
//    /**
//     * 设为 true，显示一个箭头，指向 snap 参数对象。
//     *
//     * @param prong Boolean
//     * @return 本身，这样可以继续设置其他属性
//     */
//    public Menu setProng(Boolean prong) {
//        this.prong = prong;
//        return this;
//    }

    protected AbstractNodeContainerPart containerPart=new AbstractNodeContainerPart() {
        @Override
        protected  List<Widget> nodes() {
            return Menu.this.nodes;
        }

        @Override
        protected void setNode(int i, Node node) {
            if(node==null){
                nodes.remove(i);
            }else{
                nodes.set(i,(Widget)node);
            }
        }
    };
    @Override
    public Node findNode(Filter filter) {
        return containerPart.findNode(filter);
    }

    @Override
    public List<Node> findAllNodes(Filter filter) {
        return containerPart.findAllNodes(filter);
    }

    @Override
    public Node replaceNode(Filter filter, Node node) {
        return containerPart.replaceNode(filter,node);
    }

    @Override
    public int replaceAllNodes(Filter filter, Node node) {
        return containerPart.replaceAllNodes(filter,node);
    }
}
