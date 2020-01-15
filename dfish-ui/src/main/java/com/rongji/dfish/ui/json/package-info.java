/**
 * DFISH UI 中产生JSON的类。不使用Gson 的主要原因有
 * <ol>
 * <li> Gson相对较为庞大，性能相对较差，dfish3只用了Object-json的功能，没有反向。所以，定制一个性能较高可以数倍的提高性能。</li>
 * <li>  dfish UI 的组件相对来说有一些特殊的属性不想显示，并且部分属性为了调试方便，顺序尽量按照固定方式显示。而不想因为类的集成关系造成混乱</li>
 * <li>  dfish3.x 中有一些封装类/帮助类，需要先转化成原型类才能展现。</li>
 * </ol>
 * 因此，独立定制一个工具包
 * @see com.rongji.dfish.ui.json.JsonFormat
 */
package com.rongji.dfish.ui.json;

