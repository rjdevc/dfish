package com.rongji.dfish.ui.command;

import com.rongji.dfish.ui.Container;

/**
 * CommandContainer 为命令的容器。
 *
 * @author DFish Team
 * @version 1.0
 * @param <T>  当前对象类型
 * @since XMLTMPL 2.0
 */
public interface CommandContainer<T extends CommandContainer<T>> extends Container<T> {
    /**
     * 添加一个命令。
     * @param command Command
     * @return CommandContainer
     */
    T add(Command<?> command);

//    /**
//     * 根据ID取得一个命令。
//     * @param id String
//     * @return Command
//     */
//    Command<?> getCommandById(String id);
}
