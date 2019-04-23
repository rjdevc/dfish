package com.rongji.dfish.ui;

import java.util.ArrayList;
import java.util.List;


/**
 * Dialog是打开一个对话框。dialog 既是命令，也是 widget。
 * 而这个 AbstractDialog 是他们的抽象类，其中DialogCommand更多体现命令部分 DialogTemplate更多体现widget部分
 *
 * @param <T> 当前对象类型
 * @author DFish Team
 */
@SuppressWarnings("unchecked")
public abstract class AbstractDialog<T extends AbstractDialog<T>> extends AbstractNode<T>
        implements SingleContainer<T, Widget<?>>, DialogWidth<T>, DialogHeight<T>, HasId<T>,
        Positionable<T>, Snapable<T> {
    /**
     *
     */
    private static final long serialVersionUID = -6765281771952118355L;

    /**
     * 默认构造函数
     */
    public AbstractDialog() {
    }

    protected Integer position;
    protected Boolean pophide;
    protected Boolean cover;
    protected String title;
    protected Boolean cache;
    protected String snap;
    protected String snaptype;
    protected Integer indent;
    protected Integer timeout;
    protected Widget<?> node;
    protected Boolean moveable;
    protected Boolean fullscreen;
    protected Boolean resizable;

    /**
     * 弹出窗口是否附带一个蒙版，让主窗口的元素不能被点击
     *
     * @return 是否覆盖
     */
    public Boolean getCover() {
        return cover;
    }

    /**
     * 弹出窗口是否附带一个蒙版，让主窗口的元素不能被点击
     *
     * @param cover 是否覆盖
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
    public T setTitle(String title) {
        this.title = title;
        return (T) this;
    }

    /**
     * 如果设为 true, 鼠标点击 Dialog 以外的地方将关闭 Dialog。
     *
     * @return Boolean
     */
    public Boolean getPophide() {
        return pophide;
    }

    /**
     * 如果设为 true, 鼠标点击 Dialog 以外的地方将关闭 Dialog。
     *
     * @param pophide Boolean
     * @return 本身，这样可以继续设置其他属性
     */
    public T setPophide(Boolean pophide) {
        this.pophide = pophide;
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

    /**
     * 对话框弹出位置，可选值: 0(默认) 1 2 3 4 5 6 7 8。其中 0 为页面中心点，1-8是页面八个角落方位。
     * @return Integer
     */
    public Integer getPosition() {
        return position;
    }

    /**
     * 对话框弹出位置，可选值: 0(默认) 1 2 3 4 5 6 7 8。其中 0 为页面中心点，1-8是页面八个角落方位。
     * @param position 位置
     * @return 本身，这样可以继续设置其他属性
     */
    public T setPosition(Integer position) {
        this.position = position;
        return (T) this;
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
    public T setTimeout(Integer timeout) {
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

    /**
     * 指定 snap 的位置。
     * 可选值: 11,12,14,21,22,23,32,33,34,41,43,44,bb,bt,tb,tt,ll,lr,rl,rr,cc。
     * 其中 1、2、3、4、t、r、b、l、c 分别代表左上角、右上角、右下角、左下角、上中、右中，下中、左中、中心。
     * 例如 "41" 表示 snap 对象的左下角和 Dialog 对象的左上角吸附在一起。
     * @return String
     */
    public String getSnaptype() {
        return snaptype;
    }

    /**
     * 指定 snap 的位置。
     * 可选值: 11,12,14,21,22,23,32,33,34,41,43,44,bb,bt,tb,tt,ll,lr,rl,rr,cc。
     * 其中 1、2、3、4、t、r、b、l、c 分别代表左上角、右上角、右下角、左下角、上中、右中，下中、左中、中心。
     * 例如 "41" 表示 snap 对象的左下角和 Dialog 对象的左上角吸附在一起。
     * @param snaptype String
     * @return 本身，这样可以继续设置其他属性
     */
    public T setSnaptype(String snaptype) {
        this.snaptype = snaptype;
        return (T) this;
    }

    /**
     * 吸附的对象。可以是 html 元素或 widget ID。
     * @return String
     */
    public String getSnap() {
        return snap;
    }

    /**
     * 吸附的对象。可以是 html 元素或 widget ID。
     * @param snap 吸附的对象
     * @return 本身，这样可以继续设置其他属性
     */
    public T setSnap(String snap) {
        this.snap = snap;
        return (T) this;
    }

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
     * 取得可以展示的根widget
     *
     * @return Widget
     */
    public Widget<?> getNode() {
        return node;
    }

    @Override
    public List<? extends Widget<?>> findNodes() {
        ArrayList<Widget<?>> result = new ArrayList<Widget<?>>();
        result.add(node);
        return result;
    }

    /**
     * 它只能包含唯一的节点
     *
     * @param node Widget
     * @return 本身，这样可以继续设置其他属性
     */
    public T setNode(Widget<?> node) {
        this.node = node;
        return (T) this;
    }

    /**
     * 窗口是否可用鼠标拖动
     *
     * @return Boolean
     */
    public Boolean getMoveable() {
        return moveable;
    }

    /**
     * 窗口是否可用鼠标拖动
     *
     * @param moveable 是否可用鼠标拖动
     * @return 本身，这样可以继续设置其他属性
     */
    public T setMoveable(Boolean moveable) {
        this.moveable = moveable;
        return (T) this;
    }

    /**
     * 窗口在初始化时是否最大化
     *
     * @return Boolean
     */
    public Boolean getFullscreen() {
        return fullscreen;
    }

    /**
     * 窗口在初始化时是否最大化
     *
     * @param fullscreen Boolean
     * @return 本身，这样可以继续设置其他属性
     */
    public T setFullscreen(Boolean fullscreen) {
        this.fullscreen = fullscreen;
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
}
