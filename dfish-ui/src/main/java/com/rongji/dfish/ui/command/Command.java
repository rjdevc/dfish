package com.rongji.dfish.ui.command;


import com.rongji.dfish.ui.JsonNode;

/**
 * Command为操作指令，本身是有编号的。编号相同的命令，就认为它是一样的。
 *
 * @param <T> 当前对象类型
 * @author DFish Team
 * @date 2018-08-03 before
 * @since 1.0
 */
public interface Command<T extends Command<T>> extends JsonNode<T> {

}
