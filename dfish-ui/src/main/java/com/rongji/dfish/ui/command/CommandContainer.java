package com.rongji.dfish.ui.command;

import com.rongji.dfish.ui.NodeContainer;

/**
 * CommandContainer 为命令的容器。
 *
 * @param <T> 当前对象类型
 * @author DFish Team
 * @date 2018-08-03 before
 * @since 2.0
 */
public interface CommandContainer<T extends CommandContainer<T>> extends NodeContainer {
    /**
     * 添加一个命令。
     *
     * @param command Command
     * @return CommandContainer
     */
    T add(Command command);

}
