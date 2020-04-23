package com.rongji.dfish.ui.auxiliary;

/**
 * 带有徽标的组件
 * @since DFish5.0
 * @author lamontYu
 */
public interface BadgeHolder<T> {

    /**
     * 获取徽标
     * @return Object 徽标
     */
    Object getBadge();

    /**
     * 设置徽标
     * @param badge Boolean 是否显示徽标
     * @return 本身，这样可以继续设置其他属性
     */
    T setBadge(Boolean badge);

    /**
     * 设置徽标
     * @param badge String 徽标提示文本
     * @return 本身，这样可以继续设置其他属性
     */
    T setBadge(String badge);

    /**
     * 设置徽标
     * @param badge Badge 徽标
     * @return 本身，这样可以继续设置其他属性
     */
    T setBadge(Badge badge);

}
