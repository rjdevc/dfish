package com.rongji.dfish.ui;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.rongji.dfish.base.Utils;
import com.rongji.dfish.ui.layout.AbstractLayout;

/**
 * 抽象视图，用于方便构建视图。
 * @author DFish team
 *
 * @param <T> 类型
 */
public abstract class AbstractSrc<T extends AbstractSrc<T>> extends AbstractLayout<T, Widget<?>> implements SingleContainer<T, Widget<?>> ,LazyLoad<T>{
	private String preload;
	private String template;
	private String src;
	private String success;
	private String error;
	private String complete;
	private String filter;
	/**
	 * 默认构造函数
	 */
	public AbstractSrc() {
		super(null);
	}
	
	/**
	 * 构造函数
	 * @param id String
	 */
	public AbstractSrc(String id) {
		super(id);
	}

	private static final long serialVersionUID = 8070987310944365757L;
	
	/**
	 * 视图中的所有命令
	 */
	protected Map<String,Command<?>> commands=new HashMap<String,Command<?>>();
	
	/**
	 * 增加一个命令，如果id重复会被覆盖
	 * @param id String
	 * @param command 命令
	 * @return 本身，这样可以继续设置其他属性
	 */
	@SuppressWarnings("unchecked")
	public T addCommand(String id,Command<?> command) {
		commands.put(id, command);
	     return (T) this;
	}
	/**
	 * 取得命令
	 * @param id String
	 * @return Command
	 */
	public Command<?> getCommandById(String id) {
		return commands.get(id);
	}
	
	/**
	 * 取得所有命令
	 * @return commands Map
	 */
	public Map<String,Command<?>> getCommands() {
		return commands;
	}

	/**
	 * 取得可以展示的根widget
	 * @return Widget
	 */
	public Widget<?> getNode() {
		if(nodes!=null&&nodes.size()>0){
			return nodes.get(0);
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public List<Widget<?>> findNodes() {
		return nodes;
	}
	/**
	 * 同setNode;
	 * @param rootWidget Widget
	 * @return 本身，这样可以继续设置其他属性
	 * @deprecated 使用setNode
	 */
	@Deprecated
	public T setRootWidget(Widget<?> rootWidget) {
		return setNode(rootWidget);
	}
	
	@SuppressWarnings("unchecked")
	public T setNode(Widget<?> rootWidget) {
		if(rootWidget!=null){
			if(nodes.size()>0){
				nodes.set(0, rootWidget);
			}else{
				nodes.add(rootWidget);
			}
			if(Utils.isEmpty(rootWidget.getHeight())){
				rootWidget.setHeight("*");
			}
			if(Utils.isEmpty(rootWidget.getWidth())){
				rootWidget.setWidth("*");
			}
		}else{
			nodes=null;
		}
		return (T) this;
	}
	
	@Override
	public T add(Widget<?> widget) {
		return setNode(widget);
	}

	public String getPreload() {
		return preload;
	}

	public T setPreload(String preload) {
		this.preload = preload;
		return (T) this;
	}
	public String getTemplate() {
		return template;
	}

	public T setTemplate(String template) {
		this.template = template;
		return (T) this;
	}

	public String getSrc() {
		return src;
	}

	public T setSrc(String src) {
		this.src = src;
		return (T) this;
	}
	public String getSuccess() {
		return success;
	}

	public T setSuccess(String success) {
		this.success = success;
		return (T) this;
	}
	public String getError() {
		return error;
	}

	public T setError(String error) {
		this.error = error;
		return (T) this;
	}
	public String getComplete() {
		return complete;
	}

	public T setComplete(String complete) {
		this.complete = complete;
		return (T) this;
	}
	public String getFilter() {
		return filter;
	}

	public T setFilter(String filter) {
		this.filter = filter;
		return (T) this;
	}
}
