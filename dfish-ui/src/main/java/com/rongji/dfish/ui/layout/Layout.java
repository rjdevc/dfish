package com.rongji.dfish.ui.layout;

import com.rongji.dfish.ui.NodeContainer;
import com.rongji.dfish.ui.Widget;


/**
 * Layout 布局类
 * @author DFish Team
 *
 * @param <T>  当前对象类型
 */
public interface Layout<T extends Layout<T>> extends NodeContainer<T>,Widget<T>{

}
