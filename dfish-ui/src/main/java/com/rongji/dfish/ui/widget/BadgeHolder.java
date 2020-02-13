package com.rongji.dfish.ui.widget;

import com.rongji.dfish.ui.widget.Badge;

/**
 * 带有徽标的组件
 * @since 5.0
 * @date 2020-02-12
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
     * @param badge Badge 徽标
     * @return 本身，这样可以继续设置其他属性
     */
    T setBadge(Badge badge);

}
