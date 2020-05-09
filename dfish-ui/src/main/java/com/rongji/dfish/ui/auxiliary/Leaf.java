package com.rongji.dfish.ui.auxiliary;

import com.rongji.dfish.ui.*;
import com.rongji.dfish.ui.form.AbstractBox;
import com.rongji.dfish.ui.form.BoxHolder;
import com.rongji.dfish.ui.form.TripleBox;

import java.util.*;

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
 * @since DFish1.0
 */
public class Leaf extends AbstractLeaf<Leaf> implements MultiNodeContainer<Leaf, Leaf> {
    private static final long serialVersionUID = -6246121270694425393L;

    /**
     * 节点列表
     */
    protected List<Leaf> nodes;

    @Override
    public List<Leaf> getNodes() {
        return nodes;
    }

    @Override
    public Leaf setNodes(List<Leaf> nodes) {
        this.nodes = nodes;
        return this;
    }

    /**
     * 构造函数,
     *
     * @param text 显示文本
     */
    public Leaf(String text) {
        super(text);
    }


    /**
     * 在这棵树下找到第一个id为指定id的节点
     * (注意:这里的id就是属性里面的id)
     *
     * @param id String
     * @return Tree
     */
    public List<Leaf> findPathById(String id) {
        LinkedList<Leaf> result = new LinkedList<>();
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
     * 获得装饰器
     * @see com.rongji.dfish.ui.NodeContainerDecorator
     * @return NodeContainerDecorator
     */
    protected NodeContainerDecorator getNodeContainerDecorator() {
        return new NodeContainerDecorator() {
            @Override
            protected List<Node> nodes() {
                return (List) nodes;
            }

            @Override
            protected void setNode(int i, Node node) {
                if (node == null) {
                    nodes.remove(i);
                } else {
                    nodes.set(i, (Leaf) node);
                }
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
