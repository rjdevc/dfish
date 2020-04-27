package com.rongji.dfish.ui.command;

import com.rongji.dfish.ui.NodeContainerDecorator;
import com.rongji.dfish.ui.Node;
import com.rongji.dfish.ui.SingleNodeContainer;

import java.util.Arrays;
import java.util.List;

/**
 * 替换命令。替换某个 widget。
 *
 * @author DFish Team
 * @version 1.1 lamontYu 所有属性和type按照驼峰命名方式调整
 * @since DFish3.0
 */
public class Replace extends NodeControlCommand<Replace>
        implements SingleNodeContainer<Replace, Node> {

    private static final long serialVersionUID = -5997424469287615043L;

    /**
     * 构造函数
     *
     * @param node 需要替换的内容
     */
    public Replace(Node<?> node) {
        setNode(node);
    }

    /**
     * 构造函数
     *
     * @param node   需要替换的内容
     * @param target 目标
     */
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
        } else {
            target = node.getId();
        }
        return this;
    }

    /**
     * 获得装饰器
     *
     * @return NodeContainerDecorator
     * @see com.rongji.dfish.ui.NodeContainerDecorator
     */
    protected NodeContainerDecorator getNodeContainerDecorator() {
        return new NodeContainerDecorator() {
            @Override
            protected List<Node> nodes() {
                return Arrays.asList(node);
            }

            @Override
            protected void setNode(int i, Node node) {
                assert (i == 0);
                Replace.this.setNode(node);
            }
        };
    }

    @Override
    public Node findNode(Filter filter) {
        return getNodeContainerDecorator().findNode(filter);
    }

    @Override
    public List<Node> findAllNodes(Filter filter) {
        return getNodeContainerDecorator().findAllNodes(filter);
    }

    @Override
    public Node replaceNode(Filter filter, Node node) {
        return getNodeContainerDecorator().replaceNode(filter, node);
    }

    @Override
    public int replaceAllNodes(Filter filter, Node node) {
        return getNodeContainerDecorator().replaceAllNodes(filter, node);
    }
}
