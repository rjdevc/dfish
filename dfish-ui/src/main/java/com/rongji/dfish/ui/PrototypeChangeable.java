package com.rongji.dfish.ui;

import com.rongji.dfish.ui.json.JsonWrapper;

/**
 * 有一些封装类，取得的原型可以被编辑。并且编辑后结果将会被保留。
 * 鉴于此类情况。一旦原型被编辑，需要通知封装类，以便封装类锁定当前的对象一旦再被编辑需要通知不合法。
 * 否则，此类封装类，可能将会面临不可预知的结果。
 * @author LinLW
 *
 * @param <P> 原型
 */
public interface PrototypeChangeable<P> extends JsonWrapper<P>{
	/**
	 * 通知本封装类，原型已经被改变了。
	 */
	void notifyChange();
}
