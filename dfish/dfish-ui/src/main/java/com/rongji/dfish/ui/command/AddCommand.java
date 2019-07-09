package com.rongji.dfish.ui.command;

import java.util.ArrayList;
import java.util.List;

import com.rongji.dfish.ui.Container;
import com.rongji.dfish.ui.JsonObject;
import com.rongji.dfish.ui.MultiContainer;
/**
 * AddCommand为增加一个元素的命令的基础格式，一般增加元素分为
 * {@link AppendCommand},{@link PrependCommand},{@link AfterCommand}和{@link BeforeCommand}
 * @author DFish Team
 * @version 1.0
 * @param <T> 当前对象类型
 * @since DFish 2.0 当时为InsertComman通过where属性控制增加在哪里。
 */
@SuppressWarnings("unchecked")
public abstract class AddCommand<T extends AddCommand<T>> extends NodeControlCommand<T> implements Container<T>,
MultiContainer<T, JsonObject>{
	/**
	 * 
	 */
	private static final long serialVersionUID = -2417775749900268295L;
	protected List<JsonObject> nodes = new ArrayList<>();

	/**
	 * 添加需要插入元素
	 * 一般是增加Widget。也允许增加Command等其他部件
	 * 
	 * @param w JsonObject
	 * @return 本身，这样可以继续设置其他属性
	 */
	public T add(JsonObject w) {
		if (w == null) {
			return (T) this;
		}
		if (w == this) {
			throw new IllegalArgumentException("can not add widget itself as a sub widget");
		}
		nodes.add(w);
		return (T) this;
	}

	@Override
	public List<JsonObject> findNodes() {
		return nodes;
	}

	@Override
	public List<JsonObject> getNodes() {
		return nodes;
	}
}
