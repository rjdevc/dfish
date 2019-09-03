package com.rongji.dfish.ui.plugin.ueditor;


import com.rongji.dfish.ui.form.AbstractInput;

import java.util.HashMap;
import java.util.Map;

/**
 * @author LinLW
 */
public class Ueditor extends AbstractInput<Ueditor, String> {

    private static final long serialVersionUID = 3578548092631912403L;

    private Map<String, Object> option;

    @Override
    public String getType() {
        return "ueditor";
    }

    public Ueditor(String name, String label, String value) {
        this.setName(name);
        this.setValue(value);
        this.setLabel(label);
    }

    @Override
    public Ueditor setValue(Object value) {
        this.value = toString(value);
        return this;
    }

    public Map<String, Object> getOption() {
        return option;
    }

    public Ueditor setOption(Map<String, Object> option) {
        this.option = option;
        return this;
    }

    public Ueditor setOption(String key, Object value) {
        if (this.option == null) {
            this.option = new HashMap<>();
        }
        this.option.put(key, value);
        return this;
    }

    public Ueditor removeOption(String key) {
        if (this.option != null) {
            this.option.remove(key);
        }
        return this;
    }

}
