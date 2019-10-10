package com.rongji.dfish.ui.command;

import com.rongji.dfish.ui.AbstractNode;
import com.rongji.dfish.ui.Command;

/**
 * AbstractCommand为 抽象命令，提供默认方法简化Command的开发。
 * @param <T> 当前对象类型
 * @author DFish Team
 * @version 1.0
 * @since DFish 3.0
 */
public abstract class AbstractCommand<T extends AbstractCommand<T>> extends AbstractNode<T> implements Command<T>{
	/**
	 * 
	 */
	private static final long serialVersionUID = -7974701457852829654L;
}
