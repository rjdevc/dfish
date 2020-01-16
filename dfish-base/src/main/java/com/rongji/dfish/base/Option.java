package com.rongji.dfish.base;


import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 选项类
 * @author DFish Team
 *
 */
public class Option {
	private String text;
	private Object value;
	private String icon;
	private Boolean checked;
	private Boolean checkAll;
	private String status;
	protected Map<String, Object> data;
	
	/**
	 * 默认构造函数
	 * @deprecated 不建议使用无参构造方法
	 */
	public Option(){
		
	}
	
	/**
	 * 构造函数 
	 * @param value 值
	 * @param text 显示文本
	 */
	public Option(Object value, String text) {
		this.value = value;
		this.text = text;
	}
	
	/**
	 * 构造函数
	 * @param value 值
	 * @param text 显示文本
	 * @param icon 图标
	 */
	public Option(Object value, String text, String icon) {
		this.value = value;
		this.text = text;
		this.icon = icon;
	}
	
	/**
	 * 显示文本
	 * @return text
	 */
	public String getText() {
		return text;
	}
	/**
	 * 显示文本
	 * @param text 显示文本
	 * @return this
	 */
	public Option setText(String text) {
		this.text = text;
		return this;
	}
	/**
	 * 值
	 * @return value
	 */
	public Object getValue() {
		return value;
	}
	/**
	 * 值
	 * @param value 值
	 * @return this
	 */
	public Option setValue(Object value) {
		this.value = value;
		return this;
	}
	/**
	 * 是否选中
	 * @return check
	 */
	public Boolean getChecked() {
		return checked;
	}
	/**
	 * 是否选中
	 * @param checked Boolean
	 * @return this
	 */
	public Option setChecked(Boolean checked) {
		this.checked = checked;
		return this;
	}
	/**
	 * 是否全选
	 * @return checkall
	 */
	public Boolean getCheckAll() {
		return checkAll;
	}
	/**
	 * 是否全选
	 * @param checkAll Boolean
	 * @return this
	 */
	public Option setCheckAll(Boolean checkAll) {
		this.checkAll = checkAll;
		return this;
	}
	/**
	 * 收拢图标。图片地址url，或是以点 "." 开头的样式名。
	 * @return icon
	 */
	public String getIcon() {
		return icon;
	}
	/**
	 * 收拢图标。图片地址url，或是以点 "." 开头的样式名。
	 * @param icon String
	 * @return this
	 */
	public Option setIcon(String icon) {
		this.icon = icon;
		return this;
	}

	public String getStatus() {
		return status;
	}

	public Option setStatus(String status) {
		this.status = status;
		return this;
	}

	/**
	 * 获取某个属性的值
	 * @param key 数值属性的关键字
	 * @return Object
	 */
	public Object getData(String key) {
		if (key == null || "".equals(key)) {
			return null;
		}
		if(data == null) {
			return null;
		}
		return data.get(key);
	}

	/**
	 * 移除某个属性的值
	 * @param key 数值属性的关键字
	 * @return Object
	 */
	public Object removeData(String key) {
		if (key == null || "".equals(key)) {
			return null;
		}
		if(data == null) {
			return null;
		}
		return data.remove(key);
	}

	/**
	 * 设置某个属性的值
	 * @param key 数值属性的关键字
	 * @param value 数值属性的值
	 * @return 本身，这样可以继续设置其他属性
	 */
	public Option setData(String key, Object value) {
		if(data == null){
			data = new LinkedHashMap<>();
		}
		data.put(key, value);
		return this;
	}

}
