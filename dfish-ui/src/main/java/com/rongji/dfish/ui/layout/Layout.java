package com.rongji.dfish.ui.layout;

import com.rongji.dfish.ui.Container;
import com.rongji.dfish.ui.Widget;
import com.rongji.dfish.ui.form.FormElement;

import java.util.List;



/**
 * Layout 布局类
 * @author DFish Team
 *
 * @param <T>  当前对象类型
 */
public interface Layout<T extends Layout<T>> extends Container<T>,Widget<T>{

}
