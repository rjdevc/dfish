package com.rongji.dfish.ui.widget;

/**
 * 默认提交按钮。在 text 等表单上按回车，将触发此按钮的点击事件。
 * 
 * @author DFish Team
 * 
 */
public class Submitbutton extends AbstractButton<Submitbutton> {
	 /**
	 * 
	 */
	private static final long serialVersionUID = 5355751716469309761L;

	/**
     * 构造函数
     * @param icon String 图标
     * @param text String 标题
     * @param onclick String 所触发的动作(JS)
     */
	@Deprecated
    public Submitbutton(String icon, String text, String onclick) {
    	this.setIcon(icon);
    	this.setText(text);
    	this.setOn(Button.EVENT_CLICK, onclick);
    }
    
    /**
     * 构造函数
     * @param icon String 图标
     * @param text String 标题
     */
    public Submitbutton(String icon, String text) {
    	this.setIcon(icon);
    	this.setText(text);
    }
    
    /**
     * 构造函数
     * @param text String 标题
     */
    public Submitbutton(String text) {
    	this.setText(text);
    }

	@Override
	public String getType() {
		return "submitbutton";
	}

}
