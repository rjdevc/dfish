package com.rongji.dfish.ui.command;

import java.util.ArrayList;
import java.util.List;

import com.rongji.dfish.ui.Command;
import com.rongji.dfish.ui.MultiContainer;


/**
 * CommandGroup 为系列命令
 * <p>有时候需要顺序执行一些列的命令来达到一定的效果。这时候，用CommandGroup把这些命令包含起来</p>
 * <p>CommandGroup里面有path属性，指这个命令在那个窗口(View)执行。
 * 某些时刻如果其他命令需要在默认窗口之外的窗口执行。
 * 那么需要用CommandGroup包括起来</p>
 * @author DFish Team
 * @version 1.0
 * @since XMLTMPL 2.0
 */
public class CommandGroup extends AbstractCommand<CommandGroup> implements CommandContainer<CommandGroup>,
 MultiContainer<CommandGroup, Command<?>> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6990584366431273097L;
	/**
	 * 如果想在当前弹出窗口的父窗口(打开该弹出窗口的窗口)可以设置path为该常量
	 */
	public static final String PATH_OWNER_VIEW="javascript:return $.dialog(this).ownerView.path;";
	/**
	 * 如果想在当前弹出窗口的父窗口(打开该弹出窗口的窗口)可以设置path为该常量
	 * 
	 * @deprecated 改名
	 */
	@Deprecated
	public static final String PATH_FROM_VIEW=PATH_OWNER_VIEW;
	
    protected ArrayList<Command<?>> nodes;
    protected String path;
    protected String target;
	private Long delay;

    /**
     * 执行路径，如果不设置为当前view
     * @return String
     */
    public String getPath() {
		return path;
	}
    /**
     * 指定一个 widget id，当前的命令集由这个 widget 作为执行主体。path 和 target 同时指定时，相当于 VM( path ).find( target ).cmd( args );
     * @param path String
     * @return 本身，这样可以继续设置其他属性
     */
	public CommandGroup setPath(String path) {
		this.path = path;
		return this;
	}
	/**
     * 指定一个 widget id，当前的命令集由这个 widget 作为执行主体。path 和 target 同时指定时，相当于 VM( path ).find( target ).cmd( args );
     * @return String
     */
	public String getTarget() {
		return target;
	}
	/**
     * 执行路径，如果不设置为当前view
     * @param target String
     * @return 本身，这样可以继续设置其他属性
     */
	public CommandGroup setTarget(String target) {
		this.target = target;
		return this;
	}

    /**
     * 默认构造函数
     */
    public CommandGroup() {
    	nodes = new ArrayList<>();
    }
   

    public CommandGroup add(Command<?> cmd) {
    	nodes.add(cmd);
        return this;
    }

	/**
	 * 延迟执行，单位:毫秒
	 * @return delay
	 */
	public Long getDelay() {
		return delay;
	}

    /**
     * 设置该组里面包含的动作在多长时间后执行。
     * 单位秒
     * @param delay 延迟执行时间,单位:毫秒
     * @return 本身，这样可以继续设置其他属性
     */
	public CommandGroup setDelay(Long delay) {
		this.delay = delay;
		return this;
	}
	@SuppressWarnings("unchecked")
	public List<Command<?>> findNodes() {
		return nodes;
	}
	
	public List<Command<?>> getNodes() {
		return nodes;
	}
	public String getType() {
		return "cmd";
	}

}
