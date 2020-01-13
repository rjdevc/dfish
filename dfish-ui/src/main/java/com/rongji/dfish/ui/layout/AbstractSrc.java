package com.rongji.dfish.ui.layout;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.rongji.dfish.base.util.Utils;
import com.rongji.dfish.ui.HasId;
import com.rongji.dfish.ui.command.Command;
import com.rongji.dfish.ui.LazyLoad;
import com.rongji.dfish.ui.SingleContainer;
import com.rongji.dfish.ui.Widget;

/**
 * 抽象视图，用于方便构建视图。
 * @author DFish team
 *
 * @param <T> 类型
 */
public abstract class AbstractSrc<T extends AbstractSrc<T>> extends AbstractLayout<T> implements SingleContainer<T>,LazyLoad<T> {
	private String preload;
	private String template;
	private String src;
	private Boolean sync;
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
	@Override
	public Widget getNode() {
		if(nodes!=null&&nodes.size()>0){
			return (Widget)nodes.get(0);
		}
		return null;
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<HasId<? extends HasId<?>>> findNodes() {
		return (List)nodes;
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
	
	@Override
	@SuppressWarnings("unchecked")
	public T setNode(HasId rootWidget) {
		if(!(rootWidget instanceof Widget)){
			throw new IllegalArgumentException("Widget only");
		}
		Widget widget=(Widget)rootWidget;
		if(widget!=null){
			if(nodes.size()>0){
				nodes.set(0, widget);
			}else{
				nodes.add(widget);
			}
			if(Utils.isEmpty(widget.getHeight())){
				widget.setHeight("*");
			}
			if(Utils.isEmpty(widget.getWidth())){
				widget.setWidth("*");
			}
		}else{
			nodes=null;
		}
		return (T) this;
	}
	
	@Override
	public T add(HasId widget) {
		return setNode(widget);
	}

	@Override
	public String getPreload() {
		return preload;
	}

	@Override
	public T setPreload(String preload) {
		this.preload = preload;
		return (T) this;
	}
	@Override
	public String getTemplate() {
		return template;
	}

	@Override
	public T setTemplate(String template) {
		this.template = template;
		return (T) this;
	}

	@Override
	public String getSrc() {
		return src;
	}

	@Override
	public T setSrc(String src) {
		this.src = src;
		return (T) this;
	}
	@Override
	public String getSuccess() {
		return success;
	}

	@Override
	public T setSuccess(String success) {
		this.success = success;
		return (T) this;
	}
	@Override
	public String getError() {
		return error;
	}

	@Override
	public T setError(String error) {
		this.error = error;
		return (T) this;
	}
	@Override
	public String getComplete() {
		return complete;
	}

	@Override
	public T setComplete(String complete) {
		this.complete = complete;
		return (T) this;
	}
	@Override
	public String getFilter() {
		return filter;
	}

	@Override
	public T setFilter(String filter) {
		this.filter = filter;
		return (T) this;
	}
	@Override
	public Boolean getSync() {
		return sync;
	}

	@Override
	public T setSync(Boolean sync) {
		this.sync = sync;
		return (T) this;
	}
}
