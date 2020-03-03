package com.rongji.dfish.ui.auxiliary;

import com.rongji.dfish.ui.AbstractWidget;
import com.rongji.dfish.ui.HasText;
import com.rongji.dfish.ui.LazyLoad;
import com.rongji.dfish.ui.layout.Table;

/**
 * TableTreeItem 是可折叠表格中的折叠项
 * <p>在TableRow中添加了下级可折叠的行的时候，TableTreeItem作为一个视觉标志出现在当前行({@link Table.TR})上。
 * 它前方有一个+号(或-号)点击有展开或折叠效果。</p>
 * <p>多级别的TableTreeItem自动产生缩进效果</p>
 *
 * @author DFish Team
 * @see Table.TR
 */
public class AbstractLeaf<T extends AbstractLeaf<T>> extends AbstractWidget<T> implements LazyLoad<T>, HasText<T> {

    private static final long serialVersionUID = -4828468487351572089L;
    protected String text;
    protected Boolean escape;
    protected String src;
    protected Boolean sync;
    protected String success;
    protected String error;
    protected String complete;
    protected String filter;
    protected String format;
    protected Boolean line;
    protected Object tip;

    public AbstractLeaf(String text) {
        super();
        this.text = text;
    }

    /**
     * 标题
     *
     * @return String
     */
    @Override
    public String getText() {
        return text;
    }

    /**
     * 标题
     *
     * @param text String
     * @return 本身，这样可以继续设置其他属性
     */
    @Override
    public T setText(String text) {
        this.text = text;
        return (T) this;
    }

    /**
     * 如果展开的内容是延迟加载的。将在这个URL所指定的资源中获取内容
     *
     * @return String
     */
    @Override
    public String getSrc() {
        return src;
    }

    /**
     * 如果展开的内容是延迟加载的。将在这个URL所指定的资源中获取内容
     *
     * @param src String
     * @return 本身，这样可以继续设置其他属性
     */
    @Override
    public T setSrc(String src) {
        this.src = src;
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
    public T setEscape(Boolean escape) {
        this.escape = escape;
        return (T) this;
    }

    @Override
    public Boolean getEscape() {
        return escape;
    }

    @Override
    public T setSync(Boolean sync) {
        this.sync = sync;
        return (T) this;
    }

    @Override
    public Boolean getSync() {
        return sync;
    }
}
