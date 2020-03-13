package com.rongji.dfish.ui;

import com.rongji.dfish.base.util.Utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * NodeContainerDecorator 为NodeContainer的装饰器。
 * <p>因为java为单根继承。一些类如果想同时有多个父类的特性，只能继承其中一个父类。而其他的父类的特性。
 * 通常要用模式集成进来。这里使用装饰（Decorator）模式。将NodeContainer的行为集成进来</p>
 */
public abstract class NodeContainerDecorator implements NodeContainer{
    /**
     * 该装饰器应该从宿主 类中获取的下一级节点的信息。
     * @param <W> 返回的类型可以是Node 也可以是更精细的类型
     * @return nodes列表
     */
    protected abstract <W extends Node> List<W> nodes();

    /**
     * 如果替换或删除的时候，通知宿主类，更改值。
     * 删除模式时，node=null
     * 这个i 和nodes()里面的位置是对应的。
     * 如果node 为空，可能根据宿主本身的特性，考虑动作，如果宿主不支持空对象。
     * 可能需要移除节点。
     * 移除的时候需要注意，后续setNode位置是和nodes()，可能和移除完的node并不对应。
     * @param i 位置，和nodes方法对应
     * @param node 需要替代的内容，如果是删除，这里是null
     */
    protected abstract void setNode(int i,Node node);
    @Override
    public Node findNode(Filter filter) {
        List<Node> nodes=nodes();
        if(nodes==null){
            return null;
        }
		for (Iterator<Node> iter = nodes.iterator(); iter.hasNext(); ) {
            Node item = iter.next();
            if(item==null){
                continue;
            }
			if (filter.accept(item)) {
				return item;
			} else if (item instanceof NodeContainer) {
				NodeContainer cast = (NodeContainer) item;
				Node c = cast.findNode(filter);
				if (c != null) {
					return c;
				}
			}
		}
		return null;
    }

    @Override
    public List<Node> findAllNodes(Filter filter) {
        List<Node> nodes=nodes();
        List<Node> ret=new ArrayList<>();
        if(nodes==null){
            return ret;
        }
        for (Iterator<Node> iter = nodes().iterator(); iter.hasNext(); ) {
            Node item = iter.next();
            if(item==null){
                continue;
            }
            if (filter.accept(item)) {
                ret.add(item);
            }
            if (item instanceof NodeContainer) {
                NodeContainer cast = (NodeContainer) item;
                ret.addAll(cast.findAllNodes(filter)) ;
            }
        }
        return ret;
    }


    @Override
    public Node replaceNode(Filter filter, Node node) {
        List<Node> nodes=nodes();
        if(nodes==null){
            return null;
        }
        for (int i=0;i<nodes.size();i++) {
            Node item =nodes.get(i);
            if(item==null){
                continue;
            }
            if (filter.accept(item)) {
                if(onReplace(item,node)) {
                    setNode(i, node);
                    return item;
                }
            } else if (item instanceof NodeContainer) {
                NodeContainer cast = (NodeContainer) item;
                Node c = cast.replaceNode(filter,node);
                if (c != null) {
                    return c;
                }
            }
        }
        return null;
    }

    @Override
    public int replaceAllNodes(Filter filter, Node node) {
        int replaced=0;
        List<Node> nodes=nodes();
        if(nodes==null){
            return replaced;
        }
        for (int i=0;i<nodes.size();i++) {
            Node item =nodes.get(i);
            if(item==null){
                continue;
            }
            if (filter.accept(item)) {
                if(onReplace(item,node)) {
                    replaced++;
                    setNode(i,node);
                }
            }
            if (item instanceof NodeContainer) {
                NodeContainer cast = (NodeContainer) item;
                replaced+=cast.replaceAllNodes(filter,node);
            }
        }
        return replaced;
    }


    /**
     * 替换节点的时候。默认继承被替换内容的高宽。
     * 这里利用 onReplace动作来补偿。如果不需要，可以重写此方法。
     * @param oldNode 被替换的节点
     * @param newNode 节点
     * @return 是否允许替换
     */
    protected boolean onReplace(Node oldNode, Node newNode) {
        if(oldNode instanceof Widget && newNode instanceof  Widget){
            Widget oldWidget = (Widget) oldNode;
            Widget newWidget = (Widget) newNode;
            if (!Utils.isEmpty(oldWidget.getWidth()) && Utils.isEmpty(newWidget.getWidth())) {
                newWidget.setWidth(oldWidget.getWidth());
            }
            if (!Utils.isEmpty(oldWidget.getHeight()) && Utils.isEmpty(newWidget.getHeight())) {
                newWidget.setHeight(oldWidget.getHeight());
            }
        }
        return true;
    }
}
