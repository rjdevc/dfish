package com.rongji.dfish.uitemp;

import com.rongji.dfish.ui.JsonObject;

/**
 * DFishTemplate 为动态的json格式。比如说可以指定GridLayout中tbody的部分是动态，来自参数data
 * 可能会写成
 * <pre>
 * {
 * "type":"grid", "id":"f_grid",
 * "tbody":{ "@rows":"$data" }
 * }
 * </pre>
 * <p>
 * 可知这个rows会带@这个特殊符号，并且它的类型会变成一个String表达式。
 * 所以需要将GridLayout 内容转化成动态的模板。而DFishTemplate 将提供这些操作。
 * </p>
 * Description: 
 * Copyright:   Copyright © 2018
 * Company:     rongji
 * @author		LinLW
 * @version		1.0
 *
 * Modification History:
 * Date						Author			Version			Description
 * ------------------------------------------------------------------
 * 2018年12月18日 下午6:50:07		LinLW			1.0				1.0 Version
 */
public interface DFishTemplate extends JsonObject{

}
