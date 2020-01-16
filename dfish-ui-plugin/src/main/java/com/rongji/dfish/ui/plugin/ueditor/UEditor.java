package com.rongji.dfish.ui.plugin.ueditor;


import com.rongji.dfish.ui.form.AbstractInput;

import java.util.HashMap;
import java.util.Map;

/**
 * @author LinLW
 */
public class UEditor extends AbstractInput<UEditor, String> {

    private static final long serialVersionUID = 3578548092631912403L;

    private Map<String, Object> option;

    public UEditor(String name, String label, String value) {
        this.setName(name);
        this.setValue(value);
        this.setLabel(label);
    }

    @Override
    public UEditor setValue(Object value) {
        this.value = toString(value);
        return this;
    }

    public Map<String, Object> getOption() {
        return option;
    }

    public UEditor setOption(Map<String, Object> option) {
        this.option = option;
        return this;
    }

    public UEditor setOption(String key, Object value) {
        if (this.option == null) {
            this.option = new HashMap<>();
        }
        this.option.put(key, value);
        return this;
    }

    public UEditor removeOption(String key) {
        if (this.option != null) {
            this.option.remove(key);
        }
        return this;
    }

}
