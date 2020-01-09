package com.rongji.dfish.ui.helper;

import com.rongji.dfish.ui.HasText;
import com.rongji.dfish.ui.layout.AbstractTd;


/**
 * 和javascript端是对应的TD模型。
 * json中如果td没有cls等额外属性，可能会简化显示它的node
 * 如果这个node还是文本，可能会进一步简化显示成文本。
 * 所以Td默认不能显示按封装类格式。这时候json中的原型将有可能还是这个JsonTd的格式
 * 也有可能是Widget格式或者是text格式。
 * @author DfishTeam
 *
 */
public class JsonTd extends AbstractTd<JsonTd> implements HasText<JsonTd>{
	/**
	 * 
	 */
	private static final long serialVersionUID = -5125782398657967546L;
	private String text;
	/**
	 * 文本模式时， 取得单元格内部文本的值
	 * @return String
	 */
	@Override
    public String getText() {
		return text;
	}
	/**
	 * 文本模式时， 设置单元格内部文本的值
	 * @param text String
	 * @return 本身，这样可以继续设置其他属性
	 */
	@Override
    public JsonTd setText(String text) {
		this.text = text;
		return this;
	}
}
