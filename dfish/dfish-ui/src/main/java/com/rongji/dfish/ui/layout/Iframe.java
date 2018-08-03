package com.rongji.dfish.ui.layout;
/**
 * 内嵌窗口
 * @author DFish team
 * @deprecated 改名为 EmbedWindow
 */
@Deprecated
public class Iframe extends EmbedWindow{
	/**
	 * 
	 */
	private static final long serialVersionUID = 8301928882443523671L;

	/**
	 * 构造函数
	 * @param src 路径URL
	 */
	public Iframe(String src) {
		super(src);
	}

}
