package com.rongji.dfish.ui.layout;

import java.util.List;

import com.rongji.dfish.ui.*;
import com.rongji.dfish.ui.form.Hidden;

/**
 * 通过把当前面板，简单的划分为上下或左右的方式进行布局。是最基础的布局类型。
 * 划分该类布局的时候一般支持 数字(单位像素) 百分比 * 和 -1
 *
 * @param <T> 当前类型
 * @author DFish team
 */
@SuppressWarnings("unchecked")
public abstract class LinearLayout<T extends LinearLayout<T>> extends AbstractContainer<T>
        implements Scrollable<T>, Alignable<T>, VAlignable<T>, MultiContainer<T, Widget<?>>, HiddenContainer<T> {

	private static final long serialVersionUID = -7555807071265375322L;
    private Boolean scroll;
    private String align;
    private String vAlign;

    /**
     * 默认构造函数
     *
     * @param id String
     */
    public LinearLayout(String id) {
        super(id);
    }


    @Override
    public String getAlign() {
        return align;
    }

    @Override
    public T setAlign(String align) {
        this.align = align;
        return (T) this;
    }

    @Override
    public String getVAlign() {
        return vAlign;
    }

    @Override
    public T setVAlign(String vAlign) {
        this.vAlign = vAlign;
        return (T) this;
    }

    @Override
    public Boolean getScroll() {
        return scroll;
    }

    @Override
    public T setScroll(Boolean scroll) {
        this.scroll = scroll;
        return (T) this;
    }

    @Override
    public List<Widget<?>> getNodes() {
        return (List) nodes;
    }

    /**
     * 添加子面板 一般在布局面板下只能添加可见的元素， 如果添加hidden那么该size将被忽略
     *
     * @param index 位置
     * @param w     Widget
     * @param size  String width或者height
     * @return 本身，这样可以继续设置其他属性
     */
    public abstract T add(int index, Widget w, String size);


    /**
     * 在指定的位置添加子面板
     *
     * @param index 位置
     * @param w     N
     * @return 本身，这样可以继续设置其他属性
     */
    public T add(int index, Widget w) {
        return add(index, w, null);
    }

    @Override
    public T add(HasId w) {
        if (w instanceof Hidden) {
            Hidden hidden = (Hidden) w;
            hiddens.addHidden(hidden.getName(), hidden.getValue());
            return (T) this;
        }
        return add(-1, (Widget) w, null);
    }

    @Override
    public T add(Hidden w) {
        hiddens.addHidden(w.getName(), w.getValue());
        return (T) this;
    }

    /**
     * 添加子面板 一般在布局面板下只能添加可见的元素， 如果添加hidden那么该size将被忽略
     *
     * @param w    Widget
     * @param size String width或者height
     * @return 本身，这样可以继续设置其他属性
     */
    public T add(Widget<?> w, String size) {
        return add(-1, w, size);
    }

    /**
     * 隐藏表单组
     */
    private HiddenPart hiddens = new HiddenPart();

    @Override
    public T addHidden(String name, String value) {
        hiddens.addHidden(name, value);
        return (T) this;
    }

    @Override
    public T removeHidden(String name) {
        hiddens.removeHidden(name);
        return (T) this;
    }
//	public T addHidden(String name,AtExpression value) {
//		hiddens.addHidden(name, value);
//		return (T)this;
//	}
//


    @Override
    public List<Hidden> getHiddens() {
        return hiddens.getHiddens();
    }

    @Override
    public List<String> getHiddenValue(String name) {
        return hiddens.getHiddenValue(name);
    }


}
