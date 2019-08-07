package com.rongji.dfish.ui.command;

import com.rongji.dfish.ui.*;

/**
 * 显示一个"请稍候"的信息窗
 * @author DFish Team
 *
 */
public class LoadingCommand extends AbstractDialog<LoadingCommand> implements Command<LoadingCommand>,HasText<LoadingCommand> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2229794408494180794L;


	@Override
    public String getType() {
	    return "loading";
    }
	
	private String text;
//	private Integer percent;
	private Boolean hide;
//	private Boolean cover;
//	private Widget<?> node;


	/**
	 * 构造函数
	 */
	public LoadingCommand() {
		
	}
	
	/**
	 * 构造函数
	 * @param text 提示文本
	 */
	public LoadingCommand(String text) {
		this.text = text;
	}
	
	/**
	 * 显示加载的文本
	 * @return String
	 */
	public String getText() {
		return text;
	}

	/**
	 * 显示加载的文本
	 * @param text String
	 * @return this
	 */
	public LoadingCommand setText(String text) {
		this.text = text;
		return this;
	}

//	/**
//	 * 显示一个进度条
//	 * @return Integer
//	 */
//	public Integer getPercent() {
//		return percent;
//	}
//
//	/**
//	 * 显示一个进度条
//	 * @param percent Integer
//	 * @return this
//	 */
//    public LoadingCommand setPercent(Integer percent) {
//		this.percent = percent;
//		return this;
//	}

	/**
	 * 关闭loading提示
	 * @return Boolean
	 */
	public Boolean getHide() {
		return hide;
	}

	/**
	 * 关闭loading提示
	 * @param hide Boolean
	 * @return this
	 */
	public LoadingCommand setHide(Boolean hide) {
		this.hide = hide;
		return  this;
	}
	
//	/**
//     * 弹出窗口是否附带一个蒙版，让主窗口的元素不能被点击
//     * @return 是否覆盖
//     */
//	public Boolean getCover() {
//		return cover;
//	}
//
//	/**
//     * 弹出窗口是否附带一个蒙版，让主窗口的元素不能被点击
//     * @param cover 是否覆盖
//     * @return 本身，这样可以继续设置其他属性
//     */
//	public LoadingCommand setCover(Boolean cover) {
//		this.cover = cover;
//		return this;
//	}
//	/**
//	 * 显示一个进度条。参数是 progress widget 的配置参数。
//	 * @return Progress
//	 */
//	public Progress getProgress() {
//		return progress;
//	}
//	/**
//	 * 显示一个进度条。参数是 progress widget 的配置参数。
//	 * @param progress Progress
//	 * @return this
//	 */
//	public LoadingCommand setProgress(Progress progress) {
//		this.progress = progress;
//		return this;
//	}

//	/**
//	 * widget节点
//	 * @return Widget
//	 */
//	public Widget<?> getNode() {
//		return node;
//	}
//
//	/**
//	 * widget节点
//	 * @param node Widget
//	 * @return this
//	 */
//	public LoadingCommand setNode(Widget<?> node) {
//		this.node = node;
//		return this;
//	}

}
