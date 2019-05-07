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
 * @deprecated 3.2以后 该方法无效了，可用Dialog代替
 *
 */
@Deprecated
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

	public String getStyle() {
		return style;
	}

	public DialogTemplate setStyle(String style) {
		this.style = style;
		return this;
	}

	public String getCls() {
		return cls;
	}

	public DialogTemplate setCls(String cls) {
		this.cls = cls;
		return this;
	}

	public String getBeforecontent() {
		return beforecontent;
	}

	public DialogTemplate setBeforecontent(String beforecontent) {
		this.beforecontent = beforecontent;
		return this;
	}

	public String getAftercontent() {
		return aftercontent;
	}

	public DialogTemplate setAftercontent(String aftercontent) {
		this.aftercontent = aftercontent;
		return this;
	}


	public String getGid() {
		return gid;
	}

	public DialogTemplate setGid(String gid) {
		this.gid = gid;
		return this;
	}

	public Map<String, String> getOn() {
		return events;
	}

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

	public Integer getWmin() {
		return wmin;
	}

	public DialogTemplate setWmin(Integer wmin) {
		this.wmin = wmin;
		return this;
	}

	public Integer getHmin() {
		return hmin;
	}

	public DialogTemplate setHmin(Integer hmin) {
		this.hmin = hmin;
		return this;
	}

	public Object getData(String key) {
		if (key == null || key.equals("")) {
			return null;
		}
		if (data == null) {
			return null;
		}
		return data.get(key);
	}

	public Object removeData(String key) {
		if (key == null || key.equals("")) {
			return null;
		}
		if (data == null) {
			return null;
		}
		return data.remove(key);
	}

	public DialogTemplate setData(String key, Object value) {
		if (data == null) {
			data = new LinkedHashMap<String, Object>();
		}
		data.put(key, value);
		return this;
	}

	public Map<String, Object> getData() {
		return data;
	}

}
