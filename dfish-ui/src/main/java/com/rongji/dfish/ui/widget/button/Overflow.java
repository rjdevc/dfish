package com.rongji.dfish.ui.widget.button;

import com.rongji.dfish.ui.AbstractWidget;
import com.rongji.dfish.ui.widget.Button;

/**
 * 按钮栏空间不够现实足够多按钮的时候的效果。
 * @author DFish team
 *
 */
public class Overflow extends AbstractWidget<Overflow>{
	/**
	 * 有个折叠按钮，点击被折叠的按钮，被点击的按钮将会被交换到未被折叠的按钮的最后一个。
	 */
	public static final String EFFECT_SWAP="swap";
	@Override
	public String getType() {
		// TODO Auto-generated method stub
		return "overflow";
	}
	
	private String effect;
	private Button button;
	
	public String getEffect() {
		return effect;
	}
	public Overflow setEffect(String effect) {
		this.effect = effect;
		return this;
	}
	/**
	 * 如果被折叠，将会出现这个展开按钮的【更多】按钮。
	 * 有时他和普通按钮一样大 有时可能只是个小小的右箭头。
	 * @return
	 */
	public Button getButton() {
		return button;
	}
	/**
	 * 如果被折叠，将会出现这个展开按钮的【更多】按钮。
	 * 有时他和普通按钮一样大 有时可能只是个小小的右箭头。
	 * @param button
	 */
	public Overflow setButton(Button button) {
		this.button = button;
		return this;
	}

}
