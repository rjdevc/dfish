package com.rongji.dfish.ui.command;

import com.rongji.dfish.ui.AbstractWidget;
import com.rongji.dfish.ui.Node;
import com.rongji.dfish.ui.Positionable;
import com.rongji.dfish.ui.auxiliary.Snap;


/**
 * Dialog是打开一个对话框。dialog 既是命令，也是 widget。
 * 而这个 AbstractDialog 是他们的抽象类，其中DialogCommand更多体现命令部分 DialogTemplate更多体现widget部分
 *
 * @param <T> 当前对象类型
 * @author DFish Team
 */
@SuppressWarnings("unchecked")
public abstract class AbstractPopup<T extends AbstractPopup<T>> extends AbstractWidget<T>
        implements Node<T>, Positionable<T> {

    private static final long serialVersionUID = -6765281771952118355L;

    /**
     * 默认构造函数
     */
    public AbstractPopup() {
    }

    /**
     * 最大宽度
     */
    public static final String WIDTH_MAX = "*";
    /**
     * 最大高度
     */
    public static final String HEIGHT_MAX = "*";

    protected String position;
    protected Boolean autoHide;
    protected Boolean cover;
    protected String title;
    protected Boolean cache;
    protected Integer indent;
    protected Long timeout;
    protected Boolean movable;
    protected Boolean fullScreen;
    protected Boolean resizable;
    protected Boolean independent;
    protected Boolean escape;
    protected String format;
    protected Snap snap;
    protected Boolean prong;

    /**
     * 弹出窗口是否附带一个蒙版，让主窗口的元素不能被点击
     *
     * @return 是否覆盖
     */
    /**
     * 如果设为 true, 页面和对话框之间将覆盖一层半透明蒙版。
     * @return Boolean 是否覆盖
     */
    public Boolean getCover() {
        return cover;
    }

    /**
     * 如果设为 true, 页面和对话框之间将覆盖一层半透明蒙版。
     * @param cover Boolean
     * @return 本身，这样可以继续设置其他属性
     */
    public T setCover(Boolean cover) {
        this.cover = cover;
        return (T) this;
    }


    /**
     * 设置标题栏内容
     *
     * @param title String
     * @return 本身，这样可以继续设置其他属性
     */
    @SuppressWarnings("unchecked")
    public T setTitle(String title) {
        this.title = title;
        return (T) this;
    }

    /**
     * 如果设为 true, 鼠标点击 Dialog 以外的地方将关闭 Dialog。
     *
     * @return Boolean
     */
    public Boolean getAutoHide() {
        return autoHide;
    }

    /**
     * 如果设为 true, 鼠标点击 Dialog 以外的地方将关闭 Dialog。
     *
     * @param autoHide Boolean
     * @return 本身，这样可以继续设置其他属性
     */
    @SuppressWarnings("unchecked")
    public T setAutoHide(Boolean autoHide) {
        this.autoHide = autoHide;
        return (T) this;
    }

    /**
     * 标题。如果有设置 template, 标题将显示在 template/title 中。
     *
     * @return String
     */
    public String getTitle() {
        return title;
    }

    @Override
    public String getPosition() {
        return position;
    }

    @Override
    public T setPosition(String position) {
        this.position = position;
        return (T) this;
    }

    /**
     * 定时关闭，单位:毫秒。-1则不关闭
     * @return Long
     */
    public Long getTimeout() {
        return timeout;
    }

    /**
     * 定时关闭，单位:毫秒。-1则不关闭
     * @param timeout Long
     * @return 本身，这样可以继续设置其他属性
     */
    public T setTimeout(Long timeout) {
        this.timeout = timeout;
        return (T) this;
    }

    /**
     * 如果设为 true, 当前窗口调用 .close() 方法关闭后，窗口处于隐藏状态并不删除，再次打开时将恢复为上次打开时的状态。
     *
     * @return Boolean
     */
    public Boolean getCache() {
        return cache;
    }

    /**
     * 如果设为 true, 当前窗口调用 .close() 方法关闭后，窗口处于隐藏状态并不删除，再次打开时将恢复为上次打开时的状态。
     *
     * @param cache Boolean
     * @return 本身，这样可以继续设置其他属性
     */
    public T setCache(Boolean cache) {
        this.cache = cache;
        return (T) this;
    }

//    @Override
//    public String getSnapType() {
//        return snapType;
//    }
//
//    @Override
//    public T setSnapType(String snapType) {
//        this.snapType = snapType;
//        return (T) this;
//    }
//
//    @Override
//    public String getSnap() {
//        return snap;
//    }
//
//    @Override
//    public T setSnap(String snap) {
//        this.snap = snap;
//        return (T) this;
//    }

    /**
     * 当设置了 snap 时，再设置 indent 指定相对于初始位置缩进微调多少个像素。
     *
     * @return Integer
     */
    public Integer getIndent() {
        return indent;
    }

    /**
     * 当设置了 snap 时，再设置 indent 指定相对于初始位置缩进微调多少个像素。
     *
     * @param indent Integer
     * @return 本身，这样可以继续设置其他属性
     */
    public T setIndent(Integer indent) {
        this.indent = indent;
        return (T) this;
    }


    /**
     * 窗口是否可用鼠标拖动
     *
     * @return Boolean
     */
    public Boolean getMovable() {
        return movable;
    }

    /**
     * 窗口是否可用鼠标拖动
     *
     * @param movable 是否可用鼠标拖动
     * @return 本身，这样可以继续设置其他属性
     */
    public T setMovable(Boolean movable) {
        this.movable = movable;
        return (T) this;
    }

    /**
     * 窗口在初始化时是否最大化
     *
     * @return Boolean
     */
    public Boolean getFullScreen() {
        return fullScreen;
    }

    /**
     * 窗口在初始化时是否最大化
     *
     * @param fullScreen Boolean
     * @return 本身，这样可以继续设置其他属性
     */
    public T setFullScreen(Boolean fullScreen) {
        this.fullScreen = fullScreen;
        return (T) this;
    }

    /**
     * 窗口是否可用鼠标拖动调整大小
     *
     * @return Boolean
     */
    public Boolean getResizable() {
        return resizable;
    }

    /**
     * 窗口是否可用鼠标拖动调整大小
     *
     * @param resizable Boolean
     * @return 本身，这样可以继续设置其他属性
     */
    public T setResizable(Boolean resizable) {
        this.resizable = resizable;
        return (T) this;
    }

    /**
     * 设置为true,取消与父窗口的关联效果
     *
     * @return Boolean
     */
    public Boolean getIndependent() {
        return independent;
    }

    /**
     * 设置为true,取消与父窗口的关联效果
     *
     * @param independent Boolean
     * @return 本身，这样可以继续设置其他属性
     */
    public T setIndependent(Boolean independent) {
        this.independent = independent;
        return (T) this;
    }

    /**
     * 用于显示文本是否需要转义,不设置默认是true
     *
     * @return Boolean
     */
    public Boolean getEscape() {
        return escape;
    }

    /**
     * 用于显示文本是否需要转义,不设置默认是true
     *
     * @param escape Boolean
     * @return 本身，这样可以继续设置其他属性
     */
    public T setEscape(Boolean escape) {
        this.escape = escape;
        return (T) this;
    }

    /**
     * 格式化内容。"$字段名"形式的变量将被解析替换。支持"javascript:"开头的js语句(需return返回值)。
     *
     * @return String 格式化内容
     */
    public String getFormat() {
        return format;
    }

    /**
     * 格式化内容。"$字段名"形式的变量将被解析替换。支持"javascript:"开头的js语句(需return返回值)。
     *
     * @param format String 格式化内容
     * @return 本身，这样可以继续设置其他属性
     */
    public T setFormat(String format) {
        this.format = format;
        return (T) this;
    }

    /**
     * 吸附的对象。
     * @return Snap
     */
    public Snap getSnap() {
        return snap;
    }

    /**
     * 获取吸附的对象。不存在时将新建对象
     * @return Snap
     */
    public Snap snap() {
        if (snap == null) {
            snap = new Snap();
        }
        return snap;
    }

    /**
     * 吸附的对象。
     * @param snap Snap
     * @return 本身，这样可以继续设置其他属性
     */
    public T setSnap(Snap snap) {
        this.snap = snap;
        return (T) this;
    }

    /**
     * 设为 true，显示一个箭头，指向 snap 参数对象
     * @return Boolean
     */
    public Boolean getProng() {
        return prong;
    }

    /**
     * 设为 true，显示一个箭头，指向 snap 参数对象
     * @param prong Boolean
     * @return 本身，这样可以继续设置其他属性
     */
    public T setProng(Boolean prong) {
        this.prong = prong;
        return (T) this;
    }

}
