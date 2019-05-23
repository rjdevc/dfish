package com.rongji.dfish.ui.layout;

import java.util.HashMap;
import java.util.Map;

import com.rongji.dfish.ui.AbstractSrc;
import com.rongji.dfish.ui.LazyLoad;
import com.rongji.dfish.ui.widget.DialogTemplate;


/**
 * 视图对象。
 * @author DFish Team
 *
 */
@SuppressWarnings("deprecation")
public class View extends AbstractSrc<View> implements LazyLoad<View>{
	private static final long serialVersionUID = 8815207749140104383L;

	/**
	 * 构造函数
	 * @param id String
	 */
	public View(String id) {
		super(id);
	}
	
	/**
	 * 默认构造函数
	 */
	public View() {
		super(null);
	}
	
//	private Boolean load;
	private String template;
	private String preload;
	private String src;
	private String base;
	protected Map<String,DialogTemplate> templates=new HashMap<String,DialogTemplate>();

	@Override
	public String getType() {
		return "view";
	}

//	/**
//	 * 如果设为 true, 当前窗口调用 .close() 方法关闭后，窗口处于隐藏状态并不删除，再次打开时将恢复为上次打开时的状态。
//	 * @return cache
//	 */
//	public Boolean isCache() {
//		return cache;
//	}
//	/**
//	 * 如果设为 true, 当前窗口调用 .close() 方法关闭后，窗口处于隐藏状态并不删除，再次打开时将恢复为上次打开时的状态。
//	 * @param cache
//	 * @return 本身，这样可以继续设置其他属性
//	 */
//	public View setCache(Boolean cache) {
//		this.cache = cache;
//		return this;
//	}

	public String getTemplate() {
		return template;
	}

	public View setTemplate(String template) {
		this.template = template;
		return this;
	}
	/**
	 * 加载 view 的 url。访问这个url 时应当返回一个 view 的 json 字串。
	 * @return src
	 */
	public String getSrc() {
		return src;
	}
	/**
	 * 加载 view 的 url。访问这个url 时应当返回一个 view 的 json 字串。
	 * @param src URL
	 * @return 本身，这样可以继续设置其他属性
	 */
	public View setSrc(String src) {
		this.src = src;
		return this;
	}
	
	/**
	 * 范围，
	 * 假设 http://192.168.0.1/aaa  http://192.168.0.1/bbb这两个项目同一个域名，不同的部署。
	 * aaa 项目里嵌套了 bbb 的 一个 view，这时需要在这个 view 上设置 scope="/bbb", 让它的前后端数据交换都走 bbb 系统
	 * @deprecated 现在叫base; 而scope有其他含义
	 * @see #getBase()
	 * @return scope String
	 */
	@Deprecated
	public String getScope() {
		return getBase();
	}
	/**
	 * 范围，
	 * 假设 http://192.168.0.1/aaa  http://192.168.0.1/bbb这两个项目同一个域名，不同的部署。
	 * aaa 项目里嵌套了 bbb 的 一个 view，这时需要在这个 view 上设置 scope="/bbb", 让它的前后端数据交换都走 bbb 系统
	 * @param scope String
	 * @deprecated 现在叫base; 而scope有其他含义
	 * @see #setBase(String)
	 * @return 本身，这样可以继续设置其他属性
	 */
	@Deprecated
	public View setScope(String scope) {
		return setBase(scope);
	}

	/**
	 * 给当前view里所有请求指定一个默认地址
	 * 假设 http://192.168.0.1/aaa  http://192.168.0.1/bbb这两个项目同一个域名，不同的部署。
	 * aaa 项目里嵌套了 bbb 的 一个 view，这时需要在这个 view 上设置 scope="/bbb", 让它的前后端数据交换都走 bbb 系统
	 * @return base String
	 */
	public String getBase() {
		return base;
	}
	
	/**
	 * 给当前view里所有请求指定一个默认地址
	 * 假设 http://192.168.0.1/aaa  http://192.168.0.1/bbb这两个项目同一个域名，不同的部署。
	 * aaa 项目里嵌套了 bbb 的 一个 view，这时需要在这个 view 上设置 scope="/bbb", 让它的前后端数据交换都走 bbb 系统
	 * @param base String
	 * @return 本身，这样可以继续设置其他属性
	 */
	public View setBase(String base) {
		this.base = base;
		return this;
	}
	/**
	 * 增加一个弹出窗口的模板。
	 * @param id String
	 * @param template 模板
	 * @return 本身，这样可以继续设置其他属性
	 * @see com.rongji.dfish.ui.widget.DialogTemplate
	 * @deprecated 现在template和preload统一模式不再由首页附带。
	 */
	@Deprecated
	public View addTemplate(String id,DialogTemplate template) {
		templates.put(id, template);
	     return this;
	}
	/**
	 * 取得模板
	 * @param id String 模板ID
	 * @return DialogTemplate
	 * @deprecated 现在template和preload统一模式不再由首页附带。
	 */
	@Deprecated
	public DialogTemplate getTemplateById(String id) {
		return templates.get(id);
	}
	
	/**
	 * 取得所有模板
	 * @return templates
	 * @deprecated 现在template和preload统一模式不再由首页附带。
	 */
	@Deprecated
	public Map<String,DialogTemplate> getTemplates() {
		return templates;
	}

	public String getPreload() {
		return preload;
	}

	public View setPreload(String preload) {
		this.preload = preload;
		return this;
	}
}
