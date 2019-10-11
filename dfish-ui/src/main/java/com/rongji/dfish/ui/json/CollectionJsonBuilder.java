package com.rongji.dfish.ui.json;

import java.util.Collection;
import java.util.Stack;

/**
 * CollectionJsonBuilder 为java Collection用的转化器
 * @author DFish Team
 *
 */
public class CollectionJsonBuilder extends AbstractJsonBuilder {

	@Override
    public void buildJson(Object o, StringBuilder sb, Stack<PathInfo> path) {
		Collection<?> cast=(Collection<?>)o;
		boolean begin2=true;
		sb.append('[');
		int i=0;
		for(Object item:cast){
			if(begin2) {
                begin2=false;
            } else {
                sb.append(',');
            }
			path.push(new PathInfo("["+(i++)+"]",item));
			J.buildJson(item, sb,path);
			path.pop();
		}
		sb.append(']');
	}

}
