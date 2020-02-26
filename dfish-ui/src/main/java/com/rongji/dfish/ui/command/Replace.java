package com.rongji.dfish.ui.command;

import com.rongji.dfish.ui.AbstractNodeContainerPart;
import com.rongji.dfish.ui.Node;
import com.rongji.dfish.ui.SingleNodeContainer;
import com.rongji.dfish.ui.Widget;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 替换命令。替换某个 widget。
 *
 * @author DFish Team
 * @version 1.1 lamontYu 所有属性和type按照驼峰命名方式调整
 * @date 2018-08-03 before
 * @since 3.0
 */
public class Replace extends NodeControlCommand<Replace>
        implements SingleNodeContainer<Replace,Node> {

    private static final long serialVersionUID = -5997424469287615043L;

    /**
     * 构造函数
     *
     * @param node 需要替换的内容
     */
    public Replace(Node<?> node) {
        setNode(node);
    }
    public Replace(Node<?> node, String target) {
        setNode(node);
        setTarget(target);
    }

    private Node<?> node;

    @Override
    public Node<?> getNode() {
        return node;
    }

    @Override
    public Replace setNode(Node node) {
        this.node = node;
        if (node == null) {
            target = null;
        }else{
            target = node.getId();
        }
        return this;
    }

    protected AbstractNodeContainerPart containerPart=new AbstractNodeContainerPart() {
        @Override
        protected  List<Node> nodes() {
            return Arrays.asList(node) ;
        }

        @Override
        protected void setNode(int i, Node node) {
            assert(i==0);
            Replace.this.setNode(node);
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
