package com.rongji.dfish.ui.plugin.ueditor;


import com.rongji.dfish.ui.form.AbstractInput;
import com.rongji.dfish.ui.form.Label;

import java.util.HashMap;
import java.util.Map;

/**
 * 富文本编辑器插件
 * @author LinLW
 */
public class UEditor extends AbstractInput<UEditor, String> {

    private static final long serialVersionUID = 3578548092631912403L;

    private Map<String, Object> option;

    /**
     * 构造函数
     * @param name
     * @param label
     * @param value
     */
    public UEditor(String name, String label, String value) {
        super(name,label,value);
    }

    /**
     * 构造函数
     * @param name
     * @param label
     * @param value
     */
    public UEditor(String name, Label label, String value) {
        super(name,label,value);
    }

    @Override
    public UEditor setValue(Object value) {
        this.value = toString(value);
        return this;
    }

    public Map<String, Object> getOption() {
        return option;
    }

    /**
     * UEditor的参数设置,参考ueditor.config.js
     * @param option
     * @return UEditor
     */
    public UEditor setOption(Map<String, Object> option) {
        this.option = option;
        return this;
    }

    /**
     * UEditor的参数设置,参考ueditor.config.js
     * @param key
     * @param value
     * @return UEditor
     */
    public UEditor setOption(String key, Object value) {
        if (this.option == null) {
            this.option = new HashMap<>();
        }
        this.option.put(key, value);
        return this;
    }

    /**
     * remove UEditor的某个参数设置
     * @param key
     * @return
     */
    public UEditor removeOption(String key) {
        if (this.option != null) {
            this.option.remove(key);
        }
        return this;
    }

}
