package com.rongji.dfish.ui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.rongji.dfish.base.Utils;
import com.rongji.dfish.ui.form.Hidden;


/**
 * HiddenPart 是用来处理隐藏表单字段属性的Part
 * 所有的Part 都是辅助类，利用桥接模式减少代码的编写
 * @author DFish Team
 *
 */
public class HiddenPart implements HiddenContainer<HiddenPart>{
	private static final long serialVersionUID = 683550505780264834L;
	Map<String, List<String>> map=null;
	public HiddenPart addHidden(String name,String value) {
		if(Utils.isEmpty(name)) {
			return this;
		}
		if (map == null) {
			map = new TreeMap<String, List<String>>();
		}
		List<String> values = map.get(name);
		if (values == null) {
			values = new ArrayList<String>();
			map.put(name, values);
		}
		values.add(value);
		return this;
	}

	public List<Hidden> getHiddens() {
		if (Utils.isEmpty(map)) {
			return Collections.emptyList();
		}
		List<Hidden> result = new ArrayList<Hidden>();
		for (Map.Entry<String, List<String>> entry : map.entrySet()) {
			for (String value : entry.getValue()) {
				result.add(new Hidden(entry.getKey(), value));
			}
		}
		return result;
	}

    public List<String> getHiddenValue(String name) {
    	if (Utils.isEmpty(name)) {
    		return Collections.emptyList();
    	}
    	if (map == null) {
    		return Collections.emptyList();
    	}
    	List<String> result = map.get(name);
    	if (result == null) {
    		return Collections.emptyList();
    	}
	    return result;
    }

    public HiddenPart removeHidden(String name) {
    	if (Utils.notEmpty(name) && map != null) {
    		map.remove(name);
    	}
	    return this;
    }

}
