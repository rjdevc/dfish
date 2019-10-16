package com.rongji.dfish.ui.layout;

import com.rongji.dfish.ui.*;
import com.rongji.dfish.ui.form.Hidden;
import com.rongji.dfish.ui.widget.Overflow;
import com.rongji.dfish.ui.widget.Split;

import java.util.List;

/**
 *  可切换标签容器
 */
public class Tabs extends AbstractLayout<Tabs,Tab> implements MultiContainer<Tabs,Tab>,PubHolder<Tabs,Tab>,
        Alignable<Tabs>,Valignable<Tabs> ,HiddenContainer<Tabs>{
    private String align;
    private String valign;
    private  Integer space;
    private Split split;
    private Overflow overflow;
    private Tab pub;
    /**
     * 构造函数
     *
     * @param id String
     */
    public Tabs(String id) {
        super(id);
    }

    @Override
    public String getType() {
        return "tabs";
    }

    @Override
    public List<Tab> getNodes() {
        return nodes;
    }

    @Override
    public Tab getPub() {
        return pub;
    }

    @Override
    public Tabs setPub(Tab pub) {
        this.pub=pub;
        return this;
    }


    /**
     * 在按钮之间默认插入的split;
     * 设置了该属性,无需调用{@link #add(Widget)}来添加split,按钮间自动添加1个split
     * @param split Split
     * @return this
     */
    public Tabs setSplit(Split split) {
        this.split = split;
        return this;
    }
    /**
     * 在按钮之间默认插入的split
     * @return Split
     */
    public Split getSplit() {
        if (split == null) {
            split = new Split();
        }
        return split;
    }


    /**
     * 按钮之间的间隔。
     *
     * @return space
     */
    public Integer getSpace() {
        return space;
    }

    /**
     * 按钮之间的间隔。
     *
     * @param space 间距 (像素)
     * @return 本身，这样可以继续设置其他属性
     */
    public Tabs setSpace(Integer space) {
        this.space = space;
        return this;
    }
    /**
     * 当按钮过多，放不下的时候的效果
     * @return Overflow
     */
    public Overflow getOverflow() {
        return overflow;
    }
    /**
     * 当按钮过多，放不下的时候，的效果
     * @param overflow 设置当内容太多的时候不换行
     * @return this
     */
    public Tabs setOverflow(Overflow overflow) {
        this.overflow = overflow;
        return this;
    }
    @Override
    public String getValign() {
        return valign;
    }

    @Override
    public Tabs setValign(String valign) {
        this.valign = valign;
        return this;
    }

    @Override
    public String getAlign() {
        return align;
    }

    @Override
    public Tabs setAlign(String align) {
        this.align = align;
        return this;
    }
    /**
     * 隐藏表单组
     */
    private HiddenPart hiddens = new HiddenPart();

    @Override
    public Tabs addHidden(String name, String value) {
        hiddens.addHidden(name, value);
        return this;
    }
    //	public T addHidden(String name,AtExpression value) {
//		hiddens.addHidden(name, value);
//		return (T)this;
//	}
//
    @Override
    public Tabs add(Hidden hidden) {
        hiddens.add(hidden);
        return this;
    }

    @Override
    public List<Hidden> getHiddens() {
        return hiddens.getHiddens();
    }

    @Override
    public List<String> getHiddenValue(String name) {
        return hiddens.getHiddenValue(name);
    }

    @Override
    public Tabs removeHidden(String name) {
        hiddens.removeHidden(name);
        return this;
    }
}
