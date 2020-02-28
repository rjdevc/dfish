package com.rongji.dfish.ui.layout;

import com.rongji.dfish.ui.LazyLoad;
import com.rongji.dfish.ui.Node;
import com.rongji.dfish.ui.NodeContainerDecorator;
import com.rongji.dfish.ui.Widget;
import com.rongji.dfish.ui.command.Command;

import java.util.*;


/**
 * 视图对象。
 * @author DFish Team
 *
 */
public class View extends AbstractSrc<View>{

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
	
	private String base;


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
	@Override
	protected NodeContainerDecorator getNodeContainerDecorator(){
		return new NodeContainerDecorator() {
			Map<Integer, String> posMap;

			@Override
			protected List<Node> nodes() {
				List<Node> result = new ArrayList<>();
				result.add(node);
				posMap = new HashMap<>();
				int index = 1;
				if(commands!=null) {
					for (Map.Entry<String, Command> entry : commands.entrySet()) {
						if (entry.getValue() instanceof Node) {
							result.add( entry.getValue());
							posMap.put(index++, entry.getKey());
						}
					}
				}
				return result;
			}

			@Override
			protected void setNode(int i, Node node) {
				if (posMap == null) {
					return; //本不该发生
				} else  if(i==0) {
					View.this.node=(Widget)node;
				}else if (node == null) {
					data.remove(posMap.get(i));
				} else {
					data.put(posMap.get(i), node);
				}
			}
		};
	}

}
