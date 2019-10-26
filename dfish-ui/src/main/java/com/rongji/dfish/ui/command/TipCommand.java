package com.rongji.dfish.ui.command;

import com.rongji.dfish.ui.AbstractNode;
import com.rongji.dfish.ui.Command;
import com.rongji.dfish.ui.HasText;
import com.rongji.dfish.ui.Snapable;

/**
 * 提示信息
 * @author DFish Team
 *
 */
public class TipCommand extends AbstractNode<TipCommand> implements Command<TipCommand>,Snapable<TipCommand>,HasText<TipCommand> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3534531697064109684L;
	private String text;
	private String  snap;
	private String  snaptype;
	private Integer timeout;
	private Boolean hoverdrop;
	
	/**
	 * 构造函数
	 * @param text
	 */
	public TipCommand(String text) {
		this.text = text;
	}
	
	@Override
	public String getType() {
		return "tip";
	}
	

	@Override
    public String getSnaptype() {
		return snaptype;
	}

	@Override
    public TipCommand setSnaptype(String snaptype) {
		this.snaptype = snaptype;
		return this;
	}

	@Override
    public String getSnap() {
		return snap;
	}

	@Override
    public TipCommand setSnap(String snap) {
		this.snap = snap;
		return this;
	}
	
    /**
     * 定时关闭，单位:秒。
     * @return Integer
     */
	public Integer getTimeout() {
		return timeout;
	}
    /**
     * 定时关闭，单位:秒。
     * @param timeout Integer
     * @return 本身，这样可以继续设置其他属性
     */
    public TipCommand setTimeout(Integer timeout) {
        this.timeout = timeout;
        return this;
    }
    
    /**
     * 内容
     * @return String
     */
	@Override
    public String getText() {
		return text;
	}
	/**
	 * 设置内容
	 * @param text 内容
	 * @return 本身，这样可以继续设置其他属性
	 */
	@Override
    public TipCommand setText(String text) {
		this.text = text;
		return this;
	}

	/**
	 * 鼠标移开时tip自动关闭
	 * @return Boolean
	 */
	public Boolean getHoverdrop() {
		return hoverdrop;
	}

	/**
	 * 设置鼠标移开时tip自动关闭
	 * @param hoverdrop Boolean
	 */
	public TipCommand setHoverdrop(Boolean hoverdrop) {
		this.hoverdrop = hoverdrop;
		return this;
	}
	
}
