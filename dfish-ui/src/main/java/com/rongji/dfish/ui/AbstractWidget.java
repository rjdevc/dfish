package com.rongji.dfish.ui;


import com.rongji.dfish.base.Utils;

import java.util.*;

/**
 * AbstractWidget 为抽象widget类，为方便widget构建而创立
 *
 * @param <T> 当前对象类型
 * @author DFish Team
 */
@SuppressWarnings("unchecked")
public abstract class AbstractWidget<T extends AbstractWidget<T>> extends AbstractNode<T> implements Widget<T>, EventTarget<T> {

    /**
     *
     */
    private static final long serialVersionUID = 6752586392648341685L;
    protected String gid;

    protected String width;
    protected String height;
    protected String maxwidth;
    protected String maxheight;
    protected String minwidth;
    protected String minheight;
    protected Integer wmin;
    protected Integer hmin;
    protected String cls;
    protected String style;

    protected String beforecontent;
    protected String prependcontent;
    protected String appendcontent;
    protected String aftercontent;

    protected Map<String, String> events;

    /**
     * 取得自定义的全局ID。可通过 $.globals[ gid ] 方法来获取 widget。
     *
     * @return String
     */
    @Override
    public String getGid() {
        return gid;
    }

    /**
     * 设置自定义的全局ID。可通过 $.globals[ gid ] 方法来获取 widget。
     *
     * @param gid String
     * @return 本身，这样可以继续设置其他属性
     */
    @Override
    public T setGid(String gid) {
        this.gid = gid;
        return (T) this;
    }

    /**
     * 部件在加入布局的时候一般会有大小限制
     *
     * @return String
     */
    @Override
    public String getWidth() {
        return width;
    }

    /**
     * 部件在加入布局的时候一般会有大小限制
     *
     * @param width String
     * @return 本身，这样可以继续设置其他属性
     */
    @Override
    public T setWidth(String width) {
        this.width = width;
        return (T) this;
    }

    /**
     * 部件在加入布局的时候一般会有大小限制
     *
     * @return String
     */
    @Override
    public String getHeight() {
        return height;
    }

    /**
     * 部件在加入布局的时候一般会有大小限制
     *
     * @param height String
     * @return 本身，这样可以继续设置其他属性
     */
    @Override
    public T setHeight(String height) {
        this.height = height;
        return (T) this;
    }

    /**
     * 部件在加入布局的时候一般会有大小限制
     *
     * @param width String
     * @return 本身，这样可以继续设置其他属性
     */
    @Override
    public T setWidth(int width) {
        this.width = String.valueOf(width);
        return (T) this;
    }

    /**
     * 部件在加入布局的时候一般会有大小限制
     *
     * @param height String
     * @return 本身，这样可以继续设置其他属性
     */
    @Override
    public T setHeight(int height) {
        this.height = String.valueOf(height);
        return (T) this;
    }

    /**
     * 取得竖直方向可用像素(高度)减少值
     *
     * @return Integer
     */
    @Override
    public Integer getWmin() {
        return wmin;
    }

    /**
     * 设置竖直方向可用像素(高度)减少值
     * 由于边框的因素，面板可用的空间和面板本身可能不一致，所以需要这个差值。
     * 如：面板边框为1像素，扣掉上下各1像素，该面板的wmin=2
     *
     * @param wmin Integer
     * @return 本身，这样可以继续设置其他属性
     */
    @Override
    public T setWmin(Integer wmin) {
        this.wmin = wmin;
        return (T) this;
    }

    /**
     * 取得水平方向可用像素(高度)减少值
     *
     * @return String
     */
    @Override
    public Integer getHmin() {
        return hmin;
    }

    /**
     * 设置水平方向可用像素(宽度)减少值。
     * 由于边框的因素，面板可用的空间和面板本身可能不一致，所以需要这个差值。
     * 如：面板边框为1像素，扣掉左右各1像素，该面板的hmin=2
     *
     * @param hmin int
     * @return 本身，这样可以继续设置其他属性
     */
    @Override
    public T setHmin(Integer hmin) {
        this.hmin = hmin;
        return (T) this;
    }

    @Override
    public Object getData(String key) {
        if (key == null || key.equals("")) {
            return null;
        }
        if (data == null) {
            return null;
        }
        return data.get(key);
    }

    @Override
    public Object removeData(String key) {
        if (key == null || key.equals("")) {
            return null;
        }
        if (data == null) {
            return null;
        }
        return data.remove(key);
    }

    @Override
    public T setData(String key, Object value) {
        if (data == null) {
            data = new LinkedHashMap<String, Object>();
        }
        data.put(key, value);
        return (T) this;
    }

    @Override
    public Map<String, Object> getData() {
        return data;
    }

    /**
     * 部件在加入布局的时候一般会有大小限制
     *
     * @return Integer
     */
    @Override
    public String getMaxwidth() {
        return maxwidth;
    }

    /**
     * 部件在加入布局的时候一般会有大小限制
     *
     * @param maxwidth Integer
     * @return 本身，这样可以继续设置其他属性
     */
    @Override
    public T setMaxwidth(int maxwidth) {
        this.maxwidth = String.valueOf(maxwidth);
        return (T) this;
    }

    /**
     * 部件在加入布局的时候一般会有大小限制
     *
     * @param maxwidth Integer
     * @return 本身，这样可以继续设置其他属性
     */
    @Override
    public T setMaxwidth(String maxwidth) {
        this.maxwidth = maxwidth;
        return (T) this;
    }

    /**
     * 部件在加入布局的时候一般会有大小限制
     *
     * @return Integer
     */
    @Override
    public String getMaxheight() {
        return maxheight;
    }

    /**
     * 部件在加入布局的时候一般会有大小限制
     *
     * @param maxheight Integer
     * @return 本身，这样可以继续设置其他属性
     */
    @Override
    public T setMaxheight(String maxheight) {
        this.maxheight = maxheight;
        return (T) this;
    }

    /**
     * 部件在加入布局的时候一般会有大小限制
     *
     * @param maxheight Integer
     * @return 本身，这样可以继续设置其他属性
     */
    @Override
    public T setMaxheight(int maxheight) {
        this.maxheight = String.valueOf(maxheight);
        return (T) this;
    }

    /**
     * 部件在加入布局的时候一般会有大小限制
     *
     * @return Integer
     */
    @Override
    public String getMinwidth() {
        return minwidth;
    }

    /**
     * 部件在加入布局的时候一般会有大小限制
     *
     * @param minwidth Integer
     * @return 本身，这样可以继续设置其他属性
     */
    @Override
    public T setMinwidth(int minwidth) {
        this.minwidth = String.valueOf(minwidth);
        return (T) this;
    }

    /**
     * 部件在加入布局的时候一般会有大小限制
     *
     * @param minwidth Integer
     * @return 本身，这样可以继续设置其他属性
     */
    @Override
    public T setMinwidth(String minwidth) {
        this.minwidth = minwidth;
        return (T) this;
    }

    /**
     * 部件在加入布局的时候一般会有大小限制
     *
     * @return Integer
     */
    @Override
    public String getMinheight() {
        return minheight;
    }

    /**
     * 部件在加入布局的时候一般会有大小限制
     *
     * @param minheight Integer
     * @return 本身，这样可以继续设置其他属性
     */
    @Override
    public T setMinheight(String minheight) {
        this.minheight = minheight;
        return (T) this;
    }

    /**
     * 部件在加入布局的时候一般会有大小限制
     *
     * @param minheight Integer
     * @return 本身，这样可以继续设置其他属性
     */
    @Override
    public T setMinheight(int minheight) {
        this.minheight = String.valueOf(minheight);
        return (T) this;
    }

    /**
     * 取得该面板所用的CSS样式 多个用分号(半角)隔开
     *
     * @return String
     */
    @Override
    public String getStyle() {
        return style;
    }

    /**
     * 设置该面板所用的CSS样式 多个用分号(半角)隔开
     *
     * @param style String
     * @return 本身，这样可以继续设置其他属性
     */
    @Override
    public T setStyle(String style) {
        this.style = style;
        return (T) this;
    }

    /**
     * 取得该面板所用的CSS类型 多个用空格隔开
     *
     * @return String
     */
    @Override
    public String getCls() {
        return cls;
    }

    /**
     * 设置该面板所用的CSS类型 多个用空格隔开
     *
     * @param cls String
     * @return 本身，这样可以继续设置其他属性
     */
    @Override
    public T setCls(String cls) {
        this.cls = cls;
        return (T) this;
    }

    /**
     * 添加样式
     *
     * @param cls 样式
     * @return this
     */
    @Override
    public T addCls(String cls) {
        // FIXME 这里性能可能有问题
        if (Utils.notEmpty(cls)) {
            cls = cls.trim();
            if (Utils.isEmpty(this.cls)) {
                this.setCls(cls);
            } else {
                Set<String> clsSet = parseClsSet(this.cls);
                if (clsSet.add(cls)) {
                    this.setCls(Utils.toString(clsSet, ' '));
                }
            }
        }
        return (T) this;
    }

    /**
     * 移除样式
     *
     * @param cls 样式
     * @return this
     */
    @Override
    public T removeCls(String cls) {
        if (Utils.notEmpty(cls) && Utils.notEmpty(this.cls)) {
            cls = cls.trim();
            Set<String> clsSet = parseClsSet(this.cls);
            if (clsSet.remove(cls)) {
                this.setCls(Utils.toString(clsSet, ' '));
            }
        }
        return (T) this;
    }

    private Set<String> parseClsSet(String cls) {
        // FIXME 这里性能可能有问题
        Set<String> clsSet = new HashSet<String>();
        if (Utils.notEmpty(cls)) {
            String[] clsArray = cls.split(" ");
            Collections.addAll(clsSet, clsArray);
            clsSet.remove("");
        }
        return clsSet;
    }

    /**
     * 同setCls 用于兼容2.x的使用习惯
     *
     * @param cls cssClass
     * @return 本身，这样可以继续设置其他属性
     * @deprecated 同setCls 用于兼容2.x的使用习惯
     */
    @Deprecated
    public T setStyleClass(String cls) {
        this.cls = cls;
        return (T) this;
    }

    /**
     * 附加到之前的内容(边框外前)
     *
     * @return String
     */
    @Override
    public String getBeforecontent() {
        return beforecontent;
    }

    /**
     * 附加到之前的内容(边框外前)
     *
     * @param beforecontent String
     * @return 本身，这样可以继续设置其他属性
     */
    @Override
    public T setBeforecontent(String beforecontent) {
        this.beforecontent = beforecontent;
        return (T) this;
    }

    /**
     * 附加到开头的内容(边框内前)
     *
     * @return String
     */
    @Override
    public String getPrependcontent() {
        return prependcontent;
    }

    /**
     * 附加到开头的内容(边框内前)
     *
     * @param prependcontent String
     * @return 本身，这样可以继续设置其他属性
     */
    @Override
    public T setPrependcontent(String prependcontent) {
        this.prependcontent = prependcontent;
        return (T) this;
    }

    /**
     * 附加到末尾的内容(边框内后)
     *
     * @return String
     */
    @Override
    public String getAppendcontent() {
        return appendcontent;
    }

    /**
     * 附加到末尾的内容(边框内后)
     *
     * @param appendcontent String
     * @return 本身，这样可以继续设置其他属性
     */
    @Override
    public T setAppendcontent(String appendcontent) {
        this.appendcontent = appendcontent;
        return (T) this;
    }

    /**
     * 附加到之后的内容(边框外后)
     *
     * @return String
     */
    @Override
    public String getAftercontent() {
        return aftercontent;
    }

    /**
     * 附加到之后的内容(边框外后)
     *
     * @param aftercontent String
     * @return 本身，这样可以继续设置其他属性
     */
    @Override
    public T setAftercontent(String aftercontent) {
        this.aftercontent = aftercontent;
        return (T) this;
    }

    /**
     * 设置备注<br/>
     * 有默认样式,更符合经常使用习惯,高级用法直接使用{@link #setAftercontent(String)}
     *
     * @param remark String 为空时,会将aftercontent清空
     * @return 本身，这样可以继续设置其他属性
     * @see #setAftercontent(String)
     */
    public T setRemark(String remark) {
        if (Utils.notEmpty(remark)) {
            remark = "<div class='f-remark'>" + Utils.escapeXMLword(remark) + "</div>";
        }
        return setAftercontent(remark);
    }

    @Override
    public Map<String, String> getOn() {
        return events;
    }

    @Override
    public T setOn(String eventName, String script) {
        if (eventName == null) {
            return (T) this;
        }
        if (events == null) {
            events = new TreeMap<>();
        }

        if (script == null || script.equals("")) {
            events.remove(eventName);
        } else {
            events.put(eventName, script);
        }
        return (T) this;
    }

    /**
     * 计算得到当前节点是否要输出转义的Json
     *
     * @param selfEscape   当前节点转义开关
     * @param parentEscape 上级节点转义开关
     * @return Boolean
     */
    public static Boolean calcRealEscape(Boolean selfEscape, Boolean parentEscape) {
        // 暂时先将方法写在这里
        if (selfEscape == null) { // 为空时默认继承上级的设置
            return null;
        } else if (parentEscape == null) { // 上级为空,默认相当于没有转义
            return Boolean.TRUE.equals(selfEscape) ? true : null;
        } else { // 2个都不为空时
            return !selfEscape.equals(parentEscape) ? selfEscape : null;
        }
    }

    protected void copyProperties(AbstractWidget<?> to, AbstractWidget<?> from) {
        super.copyProperties(to, from);
        to.gid = from.gid;
        to.width = from.width;
        to.height = from.height;
        to.maxwidth = from.maxwidth;
        to.maxheight = from.maxheight;
        to.minwidth = from.minwidth;
        to.minheight = from.minheight;
        to.wmin = from.wmin;
        to.hmin = from.hmin;
        to.cls = from.cls;
        to.style = from.style;

        to.beforecontent = from.beforecontent;
        to.prependcontent = from.prependcontent;
        to.appendcontent = from.appendcontent;
        to.aftercontent = from.aftercontent;

        to.events = from.events;
    }
}
