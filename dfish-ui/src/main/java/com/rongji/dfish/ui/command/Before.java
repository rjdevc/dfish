package com.rongji.dfish.ui.command;

/**
 * 插入命令。在某个 widget 之前插入一个或多个 widget。
 *
 * @author DFish Team
 * @version 1.1 lamontYu 所有属性和type按照驼峰命名方式调整
 * @date 2018-08-03 before
 * @since 3.0
 */
public class Before extends AddCommand<Before> {

    private static final long serialVersionUID = -7957942021832245204L;

    @Override
    public String getType() {
        return "Before";
    }

}
