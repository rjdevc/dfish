package com.rongji.dfish.ui.command;

import com.rongji.dfish.ui.Widget;

/**
 * 插入命令。在某个 widget 内部后置一个或多个 widget。
 *
 * @author DFish Team
 * @version 1.1 lamontYu 所有属性和type按照驼峰命名方式调整
 *
 * @since DFish3.0
 */
public class Append extends AddCommand<Append> {

    private static final long serialVersionUID = -2369976181411608729L;

    public Append(String target) {
        super(target);
    }
}
