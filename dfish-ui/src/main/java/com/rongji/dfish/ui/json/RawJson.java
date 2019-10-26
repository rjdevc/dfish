package com.rongji.dfish.ui.json;
/**
 * 有的时候可以用该类再一些地方强行注入原始的JSON。比如某个属性是一个function或某个属性是
 * new Xxxx() 
 * <p>慎用，如果这个json内容格式有误，会导致整个JSON失效。格式有误包含不完全的花括号，方括号，引号的匹配。
 * 以及不正确的转移字符。<p>
 * @author LinLW
 */
public class RawJson {
	private String text;
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public RawJson(String text){
		this.text=text;
	}
	@Override
    public String toString(){
		return text;
	}
}
