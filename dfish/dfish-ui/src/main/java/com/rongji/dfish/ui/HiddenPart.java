package com.rongji.dfish.ui;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.rongji.dfish.ui.form.Hidden;


/**
 * HiddenPart 是用来处理隐藏表单字段属性的Part
 * 所有的Part 都是辅助类，利用桥接模式减少代码的编写
 * @author DFish Team
 *
 */
public class HiddenPart implements HiddenContainer<HiddenPart>{
//	Map<String, List<String>> map=null;
	private List<Hidden> hiddens;
	public HiddenPart addHidden(String name,String value) {
		add(new Hidden(name,value));
		return this;
	}
	public HiddenPart addHidden(String name,AtExpression value) {
		add(new Hidden(name,value));
		return this;
	}
	public HiddenPart add(Hidden hidden) {
		if(hiddens==null){
			hiddens=new ArrayList<Hidden>();
		}
		hiddens.add(hidden);
		return this;
	}

	public List<Hidden> getHiddens() {
		return hiddens;
	}

    public List<String> getHiddenValue(String name) {
    	if(hiddens==null){
    		return null;
    	}
    	List<String> result = new ArrayList<String>();
    	for(Hidden h:hiddens){
    		if(h.getName().equals(name)){
    			result.add(h.getValue());
    		}
    	}
	    return result;
    }

    public HiddenPart removeHidden(String name) {
    	if(hiddens==null){
    		return this;
    	}
    	for(Iterator<Hidden>iter=hiddens.iterator();iter.hasNext();){
    		Hidden h=iter.next();
    		if(h.getName().equals(name)){
    			iter.remove();
    		}
    	}
	    return this;
    }

}
