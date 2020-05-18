package com.rongji.dfish.ui.layout;

import com.rongji.dfish.base.util.Utils;
import com.rongji.dfish.ui.*;
import com.rongji.dfish.ui.command.Command;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

/**
 * 抽象视图，用于方便构建视图。
 * @author DFish team
 *
 * @param <T> 类型
 */
public abstract class AbstractSrc<T extends AbstractSrc<T>> extends AbstractWidget<T>
		implements SingleNodeContainer<T,Widget>,LazyLoad<T> {
	protected String preload;
	protected String src;
	protected Boolean sync;
	protected String success;
	protected String error;
	protected String complete;
	protected String filter;
	protected Widget node;
	/**
	 * 默认构造函数
	 */
	public AbstractSrc() {}
	
	/**
	 * 构造函数
	 * @param id String
	 */
	public AbstractSrc(String id) {
		setId(id);
	}

	private static final long serialVersionUID = 8070987310944365757L;
	
	/**
	 * 视图中的所有命令
	 */
	protected Map<String,Command> commands=new HashMap<>();
	
	/**
	 * 增加一个命令，如果id重复会被覆盖
	 * @param id String
	 * @param command 命令
	 * @return 本身，这样可以继续设置其他属性
	 */
	@SuppressWarnings("unchecked")
	public T addCommand(String id,Command command) {
		commands.put(id, command);
	     return (T) this;
	}
	/**
	 * 取得命令
	 * @param id String
	 * @return Command
	 */
	public Command getCommandById(String id) {
		return commands.get(id);
	}
	
	/**
	 * 取得所有命令
	 * @return commands Map
	 */
	public Map<String,Command> getCommands() {
		return commands;
	}

	/**
	 * 取得可以展示的根widget
	 * @return Widget
	 */
	@Override
	public Widget getNode() {
		return node;
	}

	/**
	 * 同setNode;
	 * @param rootWidget Widget
	 * @return 本身，这样可以继续设置其他属性
	 * @deprecated 使用setNode
	 */
	@Deprecated
	public T setRootWidget(Widget rootWidget) {
		return setNode(rootWidget);
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public T setNode(Widget rootWidget) {
		node = rootWidget;
		if(node != null){
			if(Utils.isEmpty(node.getHeight())){
				node.setHeight("*");
			}
			if(Utils.isEmpty(node.getWidth())){
				node.setWidth("*");
			}
		}
		return (T) this;
	}


	/**
	 * 加载 具体内容 的 url。访问这个url 时应当返回一个 json 字串。
	 * 如果没有template 这个字符串应该是dfish的格式。
	 * 如果有template 那么template 讲把这个字符串解析成dfish需要的格式。
	 * @return String
	 */
	public String getPreload() {
		return preload;
	}

	/**
	 * 指定用这个编号所对应的预加载模板 将src返回的内容解析成dfish的格式。
	 * @param preload String
	 * @return this
	 */
	public T setPreload(String preload) {
		this.preload = preload;
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

	/**
	 * 获得装饰器
	 * @see com.rongji.dfish.ui.NodeContainerDecorator
	 * @return NodeContainerDecorator
	 */
	protected NodeContainerDecorator getNodeContainerDecorator(){
		return new NodeContainerDecorator() {
			@Override
			protected  List<Node> nodes() {
				return Arrays.asList(node) ;
			}

			@Override
			protected void setNode(int i, Node node) {
				assert(i==0);
				AbstractSrc.this.setNode((Widget) node);
			}
		};
	}
	@Override
	public Node findNode(Predicate<Node> filter) {
		return getNodeContainerDecorator().findNode(filter);
	}

	@Override
	public List<Node> findAllNodes(Predicate<Node> filter) {
		return getNodeContainerDecorator().findAllNodes(filter);
	}

	@Override
	public Node replaceNode(Predicate<Node> filter, Node node) {
		return getNodeContainerDecorator().replaceNode(filter,node);
	}

	@Override
	public int replaceAllNodes(Predicate<Node> filter, Node node) {
		return getNodeContainerDecorator().replaceAllNodes(filter,node);
	}
}
