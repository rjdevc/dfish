package com.rongji.dfish.ui;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.rongji.dfish.base.Utils;
import com.rongji.dfish.ui.form.Hidden;


/**
 * HiddenPart 是用来处理隐藏表单字段属性的Part
 * 所有的Part 都是辅助类，利用桥接模式减少代码的编写
 *
 * @author DFish Team
 */
public class HiddenPart implements HiddenContainer<HiddenPart> {
    //	Map<String, List<String>> map=null;
    private List<Hidden> hiddens;

    /**
     * 添加隐藏组件,添加时将逐一寻找已存在的隐藏组件,找到第1个相同name将其隐藏值覆盖;不存在时将自动添加
     * 用法类似Map.put
     *
     * @param name  String
     * @param value String
     * @return
     */
    public HiddenPart addHidden(String name, String value) {
        if (Utils.isEmpty(name)) {
            return this;
        }
        if (this.hiddens != null) {
            for (Hidden hidden : this.hiddens) {
                if (name.equals(hidden.getName())) {
                    // 找到第1个名称相同hidden并设值返回
                    hidden.setValue(value);
                    return this;
                }
            }
        }
        add(new Hidden(name, value));
        return this;
    }
//	public HiddenPart addHidden(String name,AtExpression value) {
//		add(new Hidden(name,value));
//		return this;
//	}

    /**
     * 按顺序添加隐藏组件
     *
     * @param hidden Hidden
     * @return
     */
    public HiddenPart add(Hidden hidden) {
        // 名称为空不允许添加,没意义
        if (hidden == null || Utils.isEmpty(hidden.getName())) {
            return this;
        }
        if (hiddens == null) {
            hiddens = new ArrayList<>();
        }
        hiddens.add(hidden);
        return this;
    }

    public List<Hidden> getHiddens() {
        return hiddens;
    }

    public List<String> getHiddenValue(String name) {
        if (hiddens == null) {
            return null;
        }
        List<String> result = new ArrayList<>();
        for (Hidden h : hiddens) {
            if (h.getName().equals(name)) {
                result.add(h.getValue());
            }
        }
        return result;
    }

    public HiddenPart removeHidden(String name) {
        if (hiddens == null || Utils.isEmpty(name)) {
            return this;
        }
        for (Iterator<Hidden> iter = hiddens.iterator(); iter.hasNext(); ) {
            Hidden h = iter.next();
            if (h.getName().equals(name)) {
                iter.remove();
            }
        }
        return this;
    }

}
