package com.rongji.dfish.ui;

import com.rongji.dfish.base.util.Utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public abstract class AbstractNodeContainerPart implements NodeContainer{

    protected abstract <W extends Node> List<W> nodes();
    protected abstract void setNode(int i,Node node);


    @Override
    public Node findNode(Filter filter) {
        List<Node> nodes=nodes();
        if(nodes==null){
            return null;
        }
		for (Iterator<Node> iter = nodes.iterator(); iter.hasNext(); ) {
            Node item = iter.next();
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
