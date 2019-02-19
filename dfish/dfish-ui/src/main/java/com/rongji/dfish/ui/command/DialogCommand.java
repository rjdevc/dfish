package com.rongji.dfish.ui.command;

import com.rongji.dfish.ui.AbstractDialog;
import com.rongji.dfish.ui.Command;
import com.rongji.dfish.ui.LazyLoad;

/**
 * 该命令用于打开一个对话框(Dialog)
 *
 * @author DFish Team
 * @version 1.0
 * @since XMLTMPL 2.0
 */
public class DialogCommand extends AbstractDialog<DialogCommand> implements Command<DialogCommand>,LazyLoad<DialogCommand> {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3055223672741088528L;
    private String template;
    private String preload;
    private String src;
    private Boolean prong;
	public String getType() {
		return "dialog";
	}
    /**
     * 打开对话框命令
     * @param id String 编号
     * @param preload 预加载模板
     * @param title String 标题栏内容
     * @param width String 窗口宽度
     * @param height String 窗口高度
     * @param pos DialogPosition 窗口在屏幕位置
     * @param src String 窗口数据的URL
     */
    @Deprecated
    public DialogCommand(String id, String preload, String title,
            int width, int height, Integer pos,
            String src) {
		this.id=id;
		this.preload = preload;
		this.title = title;
		this.setWidth(width);
		this.setHeight(height);
		this.position = pos;
		this.src = src;
	}
    
    /**
     * 打开对话框命令
     * @param id String 编号
     * @param preload 预加载模板
     * @param title String 标题栏内容
     * @param width String 窗口宽度
     * @param height String 窗口高度
     * @param pos DialogPosition 窗口在屏幕位置
     * @param src String 窗口数据的URL
     */
    public DialogCommand(String id, String preload, String title,
            String width, String height, Integer pos,
            String src) {
		this.id=id;
		this.preload = preload;
		this.title = title;
		this.setWidth(width);
		this.setHeight(height);
		this.position = pos;
		this.src = src;
	}
    /**
     * 打开对话框命令
     * @param id String 编号
     * @param title String 标题栏内容
     * @param src String 窗口数据的URL
     */
    public DialogCommand(String id, String title, String src) {
    	this.id=id;
		this.title = title;
		this.src = src;
    }
    
    /**
     * 打开对话框命令
     */
    public DialogCommand() {
    	
    }
    
    /**
     * 设置窗口数据的URL
     * @param src String
     * @return 本身，这样可以继续设置其他属性
     */
    public DialogCommand setSrc(String src) {
        this.src = src;
        return this;
    }

    /**
     * 设置窗口使用的模板编号
     * @param template 模板
     * @return  this
     */
    public DialogCommand setTemplate(String template) {
        this.template = template;
        return this;
    }
	/**
	 * 加载 view 的 url。访问这个url 时应当返回一个 view 的 json 字串。
	 * @return src
	 */
	public String getSrc() {
		return src;
	}
	/**
	 * 模板ID。
	 * @return template
	 */
	public String getTemplate() {
		return template;
	}
	/**
	 * 如果设置为true，显示一个箭头，指向snap 参数对象
	 * @return Boolean
	 */
    public Boolean getProng() {
		return prong;
	}
    /**
     * 如果设置为true，显示一个箭头，指向snap 参数对象
     * @param prong Boolean
     * @return this
     */
	public DialogCommand setProng(Boolean prong) {
		this.prong = prong;
		 return this;
	}

	public String getPreload() {
		return preload;
	}

	public DialogCommand setPreload(String preload) {
		this.preload = preload;
		return this;
	}
	
}
