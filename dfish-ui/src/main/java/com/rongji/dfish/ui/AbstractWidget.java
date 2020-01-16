package com.rongji.dfish.ui;


import com.rongji.dfish.base.util.Utils;
import com.rongji.dfish.base.util.StringUtil;

import java.util.*;

/**
 * AbstractWidget 为抽象widget类，为方便widget构建而创立
 *
 * @param <T> 当前对象类型
 * @author DFish Team
 */
@SuppressWarnings("unchecked")
public abstract class AbstractWidget<T extends AbstractWidget<T>> extends AbstractNode<T> implements Widget<T>, EventTarget<T> {

    private static final long serialVersionUID = 6752586392648341685L;
    protected String gid;

    protected String width;
    protected String height;
    protected String maxWidth;
    protected String maxHeight;
    protected String minWidth;
    protected String minHeight;
    protected Integer widthMinus;
    protected Integer heightMinus;
    protected String cls;
    protected String style;

    protected String beforeContent;
    protected String prependContent;
    protected String appendContent;
    protected String afterContent;

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
    public Integer getWidthMinus() {
        return widthMinus;
    }

    /**
     * 设置竖直方向可用像素(高度)减少值
     * 由于边框的因素，面板可用的空间和面板本身可能不一致，所以需要这个差值。
     * 如：面板边框为1像素，扣掉上下各1像素，该面板的wmin=2
     *
     * @param widthMinus Integer
     * @return 本身，这样可以继续设置其他属性
     */
    @Override
    public T setWidthMinus(Integer widthMinus) {
        this.widthMinus = widthMinus;
        return (T) this;
    }

    /**
     * 取得水平方向可用像素(高度)减少值
     *
     * @return String
     */
    @Override
    public Integer getHeightMinus() {
        return heightMinus;
    }

    /**
     * 设置水平方向可用像素(宽度)减少值。
     * 由于边框的因素，面板可用的空间和面板本身可能不一致，所以需要这个差值。
     * 如：面板边框为1像素，扣掉左右各1像素，该面板的hmin=2
     *
     * @param heightMinus int
     * @return 本身，这样可以继续设置其他属性
     */
    @Override
    public T setHeightMinus(Integer heightMinus) {
        this.heightMinus = heightMinus;
        return (T) this;
    }

    @Override
    public Object getData(String key) {
        if (key == null || "".equals(key)) {
            return null;
        }
        if (data == null) {
            return null;
        }
        return data.get(key);
    }

    @Override
    public Object removeData(String key) {
        if (key == null || "".equals(key)) {
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
            data = new LinkedHashMap<>();
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
    public String getMaxWidth() {
        return maxWidth;
    }

    /**
     * 部件在加入布局的时候一般会有大小限制
     *
     * @param maxWidth Integer
     * @return 本身，这样可以继续设置其他属性
     */
    @Override
    public T setMaxWidth(int maxWidth) {
        this.maxWidth = String.valueOf(maxWidth);
        return (T) this;
    }

    /**
     * 部件在加入布局的时候一般会有大小限制
     *
     * @param maxWidth Integer
     * @return 本身，这样可以继续设置其他属性
     */
    @Override
    public T setMaxWidth(String maxWidth) {
        this.maxWidth = maxWidth;
        return (T) this;
    }

    /**
     * 部件在加入布局的时候一般会有大小限制
     *
     * @return Integer
     */
    @Override
    public String getMaxHeight() {
        return maxHeight;
    }

    /**
     * 部件在加入布局的时候一般会有大小限制
     *
     * @param maxHeight Integer
     * @return 本身，这样可以继续设置其他属性
     */
    @Override
    public T setMaxHeight(String maxHeight) {
        this.maxHeight = maxHeight;
        return (T) this;
    }

    /**
     * 部件在加入布局的时候一般会有大小限制
     *
     * @param maxHeight Integer
     * @return 本身，这样可以继续设置其他属性
     */
    @Override
    public T setMaxHeight(int maxHeight) {
        this.maxHeight = String.valueOf(maxHeight);
        return (T) this;
    }

    /**
     * 部件在加入布局的时候一般会有大小限制
     *
     * @return Integer
     */
    @Override
    public String getMinWidth() {
        return minWidth;
    }

    /**
     * 部件在加入布局的时候一般会有大小限制
     *
     * @param minWidth Integer
     * @return 本身，这样可以继续设置其他属性
     */
    @Override
    public T setMinWidth(int minWidth) {
        this.minWidth = String.valueOf(minWidth);
        return (T) this;
    }

    /**
     * 部件在加入布局的时候一般会有大小限制
     *
     * @param minWidth Integer
     * @return 本身，这样可以继续设置其他属性
     */
    @Override
    public T setMinWidth(String minWidth) {
        this.minWidth = minWidth;
        return (T) this;
    }

    /**
     * 部件在加入布局的时候一般会有大小限制
     *
     * @return Integer
     */
    @Override
    public String getMinHeight() {
        return minHeight;
    }

    /**
     * 部件在加入布局的时候一般会有大小限制
     *
     * @param minHeight Integer
     * @return 本身，这样可以继续设置其他属性
     */
    @Override
    public T setMinHeight(String minHeight) {
        this.minHeight = minHeight;
        return (T) this;
    }

    /**
     * 部件在加入布局的时候一般会有大小限制
     *
     * @param minHeight Integer
     * @return 本身，这样可以继续设置其他属性
     */
    @Override
    public T setMinHeight(int minHeight) {
        this.minHeight = String.valueOf(minHeight);
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
    public T addClass(String cls) {
        // FIXME 这里性能可能有问题
        if (Utils.notEmpty(cls)) {
            cls = cls.trim();
            if (Utils.isEmpty(this.cls)) {
                this.setCls(cls);
            } else {
                Set<String> clsSet = parseClsSet(this.cls);
                if (clsSet.add(cls)) {
                    this.setCls(StringUtil.toString(clsSet, ' '));
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
    public T removeClass(String cls) {
        if (Utils.notEmpty(cls) && Utils.notEmpty(this.cls)) {
            cls = cls.trim();
            Set<String> clsSet = parseClsSet(this.cls);
            if (clsSet.remove(cls)) {
                this.setCls(StringUtil.toString(clsSet, ' '));
            }
        }
        return (T) this;
    }

    private Set<String> parseClsSet(String cls) {
        // FIXME 这里性能可能有问题
        Set<String> clsSet = new HashSet<>();
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
    public String getBeforeContent() {
        return beforeContent;
    }

    /**
     * 附加到之前的内容(边框外前)
     *
     * @param beforeContent String
     * @return 本身，这样可以继续设置其他属性
     */
    @Override
    public T setBeforeContent(String beforeContent) {
        this.beforeContent = beforeContent;
        return (T) this;
    }

    /**
     * 附加到开头的内容(边框内前)
     *
     * @return String
     */
    @Override
    public String getPrependContent() {
        return prependContent;
    }

    /**
     * 附加到开头的内容(边框内前)
     *
     * @param prependContent String
     * @return 本身，这样可以继续设置其他属性
     */
    @Override
    public T setPrependContent(String prependContent) {
        this.prependContent = prependContent;
        return (T) this;
    }

    /**
     * 附加到末尾的内容(边框内后)
     *
     * @return String
     */
    @Override
    public String getAppendContent() {
        return appendContent;
    }

    /**
     * 附加到末尾的内容(边框内后)
     *
     * @param appendContent String
     * @return 本身，这样可以继续设置其他属性
     */
    @Override
    public T setAppendContent(String appendContent) {
        this.appendContent = appendContent;
        return (T) this;
    }

    /**
     * 附加到之后的内容(边框外后)
     *
     * @return String
     */
    @Override
    public String getAfterContent() {
        return afterContent;
    }

    /**
     * 附加到之后的内容(边框外后)
     *
     * @param afterContent String
     * @return 本身，这样可以继续设置其他属性
     */
    @Override
    public T setAfterContent(String afterContent) {
        this.afterContent = afterContent;
        return (T) this;
    }

    /**
     * 设置备注<br/>
     * 有默认样式,更符合经常使用习惯,高级用法直接使用{@link #setAfterContent(String)}
     *
     * @param remark String 为空时,会将aftercontent清空
     * @return 本身，这样可以继续设置其他属性
     * @see #setAfterContent(String)
     */
    public T setRemark(String remark) {
        if (Utils.notEmpty(remark)) {
            remark = "<div class='f-remark'>" + Utils.escapeXMLword(remark) + "</div>";
        }
        return setAfterContent(remark);
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

        if (script == null || "".equals(script)) {
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
        to.maxWidth = from.maxWidth;
        to.maxHeight = from.maxHeight;
        to.minWidth = from.minWidth;
        to.minHeight = from.minHeight;
        to.widthMinus = from.widthMinus;
        to.heightMinus = from.heightMinus;
        to.cls = from.cls;
        to.style = from.style;

        to.beforeContent = from.beforeContent;
        to.prependContent = from.prependContent;
        to.appendContent = from.appendContent;
        to.afterContent = from.afterContent;

        to.events = from.events;
    }
}
