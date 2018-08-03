package com.rongji.dfish.ui.plugin.ueditor;



import com.rongji.dfish.ui.form.AbstractInput;

/**
 * @author LinLW
 *
 */
public class Ueditor extends AbstractInput<Ueditor,String>{

	private static final long serialVersionUID = 3578548092631912403L;
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
		this.value=toString(value);
		return this;
	}

}
