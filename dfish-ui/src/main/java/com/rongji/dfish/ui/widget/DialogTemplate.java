package com.rongji.dfish.ui.widget;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;

import com.rongji.dfish.ui.AbstractDialog;
import com.rongji.dfish.ui.Widget;

/**
 * 弹出窗口模板
 * 
 * @author DFish Team
 * 
 */
public class DialogTemplate extends AbstractDialog<DialogTemplate> implements Widget<DialogTemplate> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5977282394362577367L;

	@Override
	public String getType() {
		return null;
	}

	protected Integer wmin;
	protected Integer hmin;
	protected String style;
	protected String cls;
	protected String beforecontent;
	protected String aftercontent;
	protected String gid;
	protected Map<String, String> events;

	protected Map<String, Object> data;

	@Override
    public String getStyle() {
		return style;
	}

	@Override
    public DialogTemplate setStyle(String style) {
		this.style = style;
		return this;
	}

	@Override
    public String getCls() {
		return cls;
	}

	@Override
    public DialogTemplate setCls(String cls) {
		this.cls = cls;
		return this;
	}

	@Override
    public String getBeforecontent() {
		return beforecontent;
	}

	@Override
    public DialogTemplate setBeforecontent(String beforecontent) {
		this.beforecontent = beforecontent;
		return this;
	}

	@Override
    public String getAftercontent() {
		return aftercontent;
	}

	@Override
    public DialogTemplate setAftercontent(String aftercontent) {
		this.aftercontent = aftercontent;
		return this;
	}


	@Override
    public String getGid() {
		return gid;
	}

	@Override
    public DialogTemplate setGid(String gid) {
		this.gid = gid;
		return this;
	}

	@Override
    public Map<String, String> getOn() {
		return events;
	}

	@Override
    public DialogTemplate setOn(String eventName, String script) {
		if (eventName == null) {
			return this;
		}
		if (events == null) {
			events = new TreeMap<String, String>();
		}

		if (script == null || script.equals("")) {
			events.remove(eventName);
		} else {
			events.put(eventName, script);
		}
		return this;
	}

	@Override
    public Integer getWmin() {
		return wmin;
	}

	@Override
    public DialogTemplate setWmin(Integer wmin) {
		this.wmin = wmin;
		return this;
	}

	@Override
    public Integer getHmin() {
		return hmin;
	}

	@Override
    public DialogTemplate setHmin(Integer hmin) {
		this.hmin = hmin;
		return this;
	}

	@Override
    public Object getData(String key) {
		if (key == null || key.equals("")) {
			return null;
		}
		if (data == null) {
			return null;
		}
		return data.get(key);
	}

	@Override
    public Object removeData(String key) {
		if (key == null || key.equals("")) {
			return null;
		}
		if (data == null) {
			return null;
		}
		return data.remove(key);
	}

	@Override
    public DialogTemplate setData(String key, Object value) {
		if (data == null) {
			data = new LinkedHashMap<String, Object>();
		}
		data.put(key, value);
		return this;
	}

	@Override
    public Map<String, Object> getData() {
		return data;
	}

}
