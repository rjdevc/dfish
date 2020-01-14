package com.rongji.dfish.ui.command;


/**
 * 删除命令。删除某个 widget。
 *
 * @author DFish Team
 * @version 1.1 lamontYu 所有属性和type按照驼峰命名方式调整
 * @date 2018-08-03 before
 * @since 3.0
 */
public class Remove extends NodeControlCommand<Remove> {

    private static final long serialVersionUID = -5276859092129055953L;

    /**
     * 构造函数
     *
     * @param target String目标的ID
     */
    public Remove(String target) {
        setTarget(target);
    }

}
