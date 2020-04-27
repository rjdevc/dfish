package com.rongji.dfish.ui.auxiliary;

import com.rongji.dfish.ui.*;
import com.rongji.dfish.ui.command.Tip;
import com.rongji.dfish.ui.form.AbstractBox;
import com.rongji.dfish.ui.form.BoxHolder;

/**
 * TableTreeItem 是可折叠表格中的折叠项
 * <p>在TableRow中添加了下级可折叠的行的时候，TableTreeItem作为一个视觉标志出现在当前行({@link TR})上。
 * 它前方有一个+号(或-号)点击有展开或折叠效果。</p>
 * <p>多级别的TableTreeItem自动产生缩进效果</p>
 *
 * @author DFish Team
 */
public abstract class AbstractLeaf<T extends AbstractLeaf<T>> extends AbstractWidget<T> implements Statusful<T>,
        HtmlContentHolder<T>, LazyLoad<T>, HasText<T>, BoxHolder<T>, BadgeHolder<T> {
    private Boolean focus;
    private Boolean focusable;
    private String collapsedIcon;
    private String expandedIcon;
    private Boolean expanded;
    private String text;
    private Object tip;
    private String src;
    private Boolean sync;
    private Boolean noToggle;
    private AbstractBox box;
    private Boolean line;
    private String format;
    private String status;
    private Boolean folder;
    private Boolean escape;
    private String success;
    private String error;
    private String complete;
    private String filter;
    private Object badge;


    /**
     * 构造函数,
     *
     * @param text 显示文本
     */
    public AbstractLeaf(String text) {
        super();
        this.setText(text);
    }

    /**
     * 选项表单，类型是 checkbox 或 radio。取消或勾选这个box，将同步fieldset内部所有表单的状态。
     *
     * @return AbstractBox
     */
    @Override
    public AbstractBox getBox() {
        return box;
    }


    /**
     * 选项表单，类型是 checkbox 或 radio。取消或勾选这个box，将同步fieldset内部所有表单的状态。
     *
     * @param box 选项表单
     * @return 本身，这样可以继续设置其他属性
     */
    @Override
    public T setBox(AbstractBox box) {
        this.box = box;
        return (T) this;
    }

    /**
     * 是否焦点状态。
     *
     * @return focus
     */
    public Boolean getFocus() {
        return focus;
    }

    /**
     * 是否焦点状态。
     *
     * @param focus Boolean
     * @return 本身，这样可以继续设置其他属性
     */
    public T setFocus(Boolean focus) {
        this.focus = focus;
        return (T) this;
    }

    /**
     * 是否可聚焦
     *
     * @return Boolean
     */
    public Boolean getFocusable() {
        return focusable;
    }

    /**
     * 是否可聚焦
     *
     * @param focusable 是否可聚焦
     * @return 本身，这样可以继续设置其他属性
     */
    public T setFocusable(Boolean focusable) {
        this.focusable = focusable;
        return (T) this;
    }

    /**
     * 图标。可使用图片url地址，或以 "." 开头的样式名。
     *
     * @return icon
     */
    public String getCollapsedIcon() {
        return collapsedIcon;
    }

    /**
     * 图标。可使用图片url地址，或以 "." 开头的样式名。
     *
     * @param collapsedIcon 图标 闭合时的图标
     * @return 本身，这样可以继续设置其他属性
     */
    public T setCollapsedIcon(String collapsedIcon) {
        this.collapsedIcon = collapsedIcon;
        return (T) this;
    }

    /**
     * 展开图标
     *
     * @return openicon
     */
    public String getExpandedIcon() {
        return expandedIcon;
    }

    /**
     * 展开图标
     *
     * @param expandedIcon 展开图标
     * @return 本身，这样可以继续设置其他属性
     */
    public T setExpandedIcon(String expandedIcon) {
        this.expandedIcon = expandedIcon;
        return (T) this;
    }


    /**
     * 是否展开状态。
     *
     * @return open
     */
    public Boolean getExpanded() {
        return expanded;
    }

    /**
     * 是否展开状态。
     *
     * @param expanded Boolean
     * @return 本身，这样可以继续设置其他属性
     */
    public T setExpanded(Boolean expanded) {
        this.expanded = expanded;
        return (T) this;
    }

    /**
     * 显示文本。
     *
     * @return text
     */
    @Override
    public String getText() {
        return text;
    }

    /**
     * 显示文本。
     *
     * @param text 显示文本
     * @return 本身，这样可以继续设置其他属性
     */
    @Override
    public T setText(String text) {
        this.text = text;
        return (T) this;
    }


    /**
     * 提示信息。设为true，提示信息将使用text参数的值。
     *
     * @return tip
     */
    public Object getTip() {
        return tip;
    }

    /**
     * 提示信息。设为true，提示信息将使用text参数的值。
     *
     * @param tip Boolean
     * @return 本身，这样可以继续设置其他属性
     */
    public T setTip(Boolean tip) {
        this.tip = tip;
        return (T) this;
    }

    /**
     * 提示信息。设为true，提示信息将使用text参数的值。
     *
     * @param tip 提示信息
     * @return 本身，这样可以继续设置其他属性
     */
    public T setTip(String tip) {
        this.tip = tip;
        return (T) this;
    }

    /**
     * 提示信息。
     *
     * @param tip Tip提示命令
     * @return 本身，这样可以继续设置其他属性
     */
    public T setTip(Tip tip) {
        this.tip = tip;
        return (T) this;
    }

    /**
     * 获取子节点的URL地址。
     *
     * @return src
     */
    @Override
    public String getSrc() {
        return src;
    }

    /**
     * 获取子节点的URL地址。
     *
     * @param src URL地址
     * @return 本身，这样可以继续设置其他属性
     */
    @Override
    public T setSrc(String src) {
        this.src = src;
        return (T) this;
    }

    /**
     * 是否隐藏 toggle 图标。
     *
     * @return Boolean
     */
    public Boolean getNoToggle() {
        return noToggle;
    }

    /**
     * 是否隐藏 toggle 图标。
     *
     * @param noToggle Boolean
     * @return 本身，这样可以继续设置其他属性
     */
    public T setNoToggle(Boolean noToggle) {
        this.noToggle = noToggle;
        return (T) this;
    }

    /**
     * 是否显示树结构的辅助线
     *
     * @return Boolean
     */
    public Boolean getLine() {
        return line;
    }

    /**
     * 是否显示树结构的辅助线
     *
     * @param line Boolean
     * @return 本身，这样可以继续设置其他属性
     */
    public T setLine(Boolean line) {
        this.line = line;
        return (T) this;
    }


    @Override
    public String getFormat() {
        return format;
    }


    @Override
    public T setFormat(String format) {
        this.format = format;
        return (T) this;
    }

    @Override
    public String getStatus() {
        return status;
    }

    @Override
    public T setStatus(String status) {
        this.status = status;
        return (T) this;
    }

    /**
     * 是否为一个可展开的目录，如果不设置本参数，那么引擎将根据是否有src属性或leaf子节点来自动判断
     *
     * @return Boolean
     * @since DFish3.2.0
     */
    public Boolean getFolder() {
        return folder;
    }

    /**
     * 是否为一个可展开的目录，如果不设置本参数，那么引擎将根据是否有src属性或leaf子节点来自动判断
     *
     * @param folder Boolean
     * @return 本身，这样可以继续设置其他属性
     */
    public T setFolder(Boolean folder) {
        this.folder = folder;
        return (T) this;
    }


    @Override
    public Boolean getEscape() {
        return escape;
    }

    @Override
    public T setEscape(Boolean escape) {
        this.escape = escape;
        return (T) this;
    }

    @Override
    public String getSuccess() {
        return success;
    }

    @Override
    public T setSuccess(String success) {
        this.success = success;
        return (T) this;
    }

    @Override
    public String getError() {
        return error;
    }

    @Override
    public T setError(String error) {
        this.error = error;
        return (T) this;
    }

    @Override
    public String getComplete() {
        return complete;
    }

    @Override
    public T setComplete(String complete) {
        this.complete = complete;
        return (T) this;
    }

    @Override
    public String getFilter() {
        return filter;
    }

    @Override
    public T setFilter(String filter) {
        this.filter = filter;
        return (T) this;
    }

    @Override
    public Boolean getSync() {
        return sync;
    }

    @Override
    public T setSync(Boolean sync) {
        this.sync = sync;
        return (T) this;
    }

    /**
     * 显示徽标
     *
     * @return Object
     */
    @Override
    public Object getBadge() {
        return badge;
    }

    @Override
    public T setBadge(Boolean badge) {
        this.badge = badge;
        return (T) this;
    }

    @Override
    public T setBadge(String badge) {
        this.badge = badge;
        return (T) this;
    }

    @Override
    public T setBadge(Badge badge) {
        this.badge = badge;
        return (T) this;
    }
}

