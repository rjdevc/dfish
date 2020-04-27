package com.rongji.dfish.ui.command;

import com.rongji.dfish.ui.Widget;

/**
 * 插入命令。在某个 widget 内部前置一个或多个 widget。
 *
 * @author DFish Team
 * @version 1.1 lamontYu 所有属性和type按照驼峰命名方式调整
 *
 * @since DFish3.0
 */
public class Prepend extends AddCommand<Prepend> {

    private static final long serialVersionUID = -6112878724451958092L;

    /**
     * 构造函数
     * @param target 目标
     */
    public Prepend(String target) {
        super(target);
    }
}
