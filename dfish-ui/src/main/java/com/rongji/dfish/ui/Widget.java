package com.rongji.dfish.ui;

/**
 * Widget 是指可见的视图部件。
 *
 * @param <T> 当前对象类型
 * @author DFish Team
 */
public interface Widget<T extends Widget<T>> extends JsonNode<T>, EventTarget<T>,HasId<T> {


    /**
     * 高度取决于父布局高度，一般来说是是三种情况
     * <ol>
     * <li>占满全屏</li>
     * <li>占满除了其他几个指定高度的元素外的所有高度</li>
     * <li>如果同时几个元素高度都是*，那么他们平分这个高度</li>
     * </ol>
     *
     * @deprecated DEPENDS命名有歧义所以改为REMAIN和 AUTO(-1)对应
     */
    @Deprecated
    String WIDTH_DEPENDS = "*";
    /**
     * 宽度取决于父布局宽度，一般来说是是三种情况
     * <ol>
     * <li>占满全屏</li>
     * <li>占满除了其他几个指定宽度的元素外的所有宽度</li>
     * <li>如果同时几个元素宽度都是*，那么他们平分这个宽度</li>
     * </ol>
     *
     * @deprecated DEPENDS命名有歧义所以改为REMAIN和 AUTO(-1)对应
     */
    @Deprecated
    String HEIGHT_DEPENDS = "*";
    /**
     * 高度取决于父布局高度，一般来说是是三种情况
     * <ol>
     * <li>占满全屏</li>
     * <li>占满除了其他几个指定高度的元素外的所有高度</li>
     * <li>如果同时几个元素高度都是*，那么他们平分这个高度</li>
     * </ol>
     */
    String WIDTH_REMAIN = "*";
    /**
     * 宽度取决于父布局宽度，一般来说是是三种情况
     * <ol>
     * <li>占满全屏</li>
     * <li>占满除了其他几个指定宽度的元素外的所有宽度</li>
     * <li>如果同时几个元素宽度都是*，那么他们平分这个宽度</li>
     * </ol>
     */
    String HEIGHT_REMAIN = "*";

    /**
     * 高度取决于自己内容的高度
     */
    String WIDTH_AUTO = "-1";
    /**
     * 宽度取决于自己内容的宽度
     */
    String HEIGHT_AUTO = "-1";

    /**
     * 设置该面板所用的CSS类型 多个用空格隔开
     *
     * @param cls String
     * @return 本身，这样可以继续设置其他属性
     */
    T setCls(String cls);

    /**
     * 取得该面板所用的CSS类型 多个用空格隔开
     *
     * @return String
     */
    String getCls();

    /**
     * 添加样式
     *
     * @param cls 样式
     * @return 本身，这样可以继续设置其他属性
     */
    T addCls(String cls);

    /**
     * 移除样式
     *
     * @param cls 样式
     * @return 本身，这样可以继续设置其他属性
     */
    T removeCls(String cls);

    /**
     * 设置水平方向可用像素(宽度)减少值。
     * 由于边框的因素，面板可用的空间和面板本身可能不一致，所以需要这个差值。
     * 如：面板边框为1像素，扣掉左右各1像素，该面板的hmin=2
     *
     * @param heightMinus int
     * @return 本身，这样可以继续设置其他属性
     */
    T setHeightMinus(Integer heightMinus);

    /**
     * 取得水平方向可用像素(高度)减少值
     *
     * @return String
     */
    Integer getHeightMinus();

    /**
     * 设置竖直方向可用像素(高度)减少值
     * 由于边框的因素，面板可用的空间和面板本身可能不一致，所以需要这个差值。
     * 如：面板边框为1像素，扣掉上下各1像素，该面板的wmin=2
     *
     * @param widthMinus Integer
     * @return 本身，这样可以继续设置其他属性
     */
    T setWidthMinus(Integer widthMinus);

    /**
     * 取得竖直方向可用像素(高度)减少值
     *
     * @return Integer
     */
    Integer getWidthMinus();

    /**
     * 设置该面板所用的CSS样式 多个用分号(半角)隔开
     *
     * @param style String
     * @return 本身，这样可以继续设置其他属性
     */
    T setStyle(String style);

    /**
     * 取得该面板所用的CSS样式 多个用分号(半角)隔开
     *
     * @return String
     */
    String getStyle();

    /**
     * 部件在加入布局的时候一般会有大小限制
     *
     * @return String
     */
    String getHeight();

    /**
     * 部件在加入布局的时候一般会有大小限制
     *
     * @param height String
     * @return 本身，这样可以继续设置其他属性
     */
    T setHeight(String height);

    /**
     * 部件在加入布局的时候一般会有大小限制,方法效果如{@link #setHeight(String)}
     *
     * @param height int
     * @return 本身，这样可以继续设置其他属性
     */
    T setHeight(int height);

    /**
     * 部件在加入布局的时候一般会有大小限制
     *
     * @return String
     */
    String getWidth();

    /**
     * 部件在加入布局的时候一般会有大小限制
     *
     * @param width String
     * @return 本身，这样可以继续设置其他属性
     */
    T setWidth(String width);

    /**
     * 部件在加入布局的时候一般会有大小限制,方法效果如{@link #setWidth(String)}
     *
     * @param width int
     * @return 本身，这样可以继续设置其他属性
     */
    T setWidth(int width);

    /**
     * 部件在加入布局的时候一般会有大小限制
     *
     * @return Integer
     */
    String getMaxHeight();

    /**
     * 部件在加入布局的时候一般会有大小限制
     *
     * @param maxHeight Integer
     * @return 本身，这样可以继续设置其他属性
     */
    T setMaxHeight(int maxHeight);

    /**
     * 部件在加入布局的时候一般会有大小限制
     *
     * @param maxHeight Integer
     * @return 本身，这样可以继续设置其他属性
     */
    T setMaxHeight(String maxHeight);

    /**
     * 部件在加入布局的时候一般会有大小限制
     *
     * @return Integer
     */
    String getMaxWidth();

    /**
     * 部件在加入布局的时候一般会有大小限制
     *
     * @param maxWidth Integer
     * @return 本身，这样可以继续设置其他属性
     */
    T setMaxWidth(int maxWidth);

    /**
     * 部件在加入布局的时候一般会有大小限制
     *
     * @param maxWidth Integer
     * @return 本身，这样可以继续设置其他属性
     */
    T setMaxWidth(String maxWidth);

    /**
     * 部件在加入布局的时候一般会有大小限制
     *
     * @return Integer
     */
    String getMinHeight();

    /**
     * 部件在加入布局的时候一般会有大小限制
     *
     * @param minHeight Integer
     * @return 本身，这样可以继续设置其他属性
     */
    T setMinHeight(int minHeight);

    /**
     * 部件在加入布局的时候一般会有大小限制
     *
     * @param minHeight Integer
     * @return 本身，这样可以继续设置其他属性
     */
    T setMinHeight(String minHeight);

    /**
     * 部件在加入布局的时候一般会有大小限制
     *
     * @return Integer
     */
    String getMinWidth();

    /**
     * 部件在加入布局的时候一般会有大小限制
     *
     * @param minWidth Integer
     * @return 本身，这样可以继续设置其他属性
     */
    T setMinWidth(String minWidth);

    /**
     * 部件在加入布局的时候一般会有大小限制
     *
     * @param minWidth Integer
     * @return 本身，这样可以继续设置其他属性
     */
    T setMinWidth(int minWidth);

    /**
     * 设置自定义的全局ID。可通过 $.globals[ gid ] 方法来获取 widget。
     *
     * @param gid String
     * @return 本身，这样可以继续设置其他属性
     */
    T setGid(String gid);

    /**
     * 取得自定义的全局ID。可通过 $.globals[ gid ] 方法来获取 widget。
     *
     * @return String
     */
    String getGid();

    /**
     * 附加到之前的内容(边框外前)
     *
     * @return String
     */
    String getBeforeContent();

    /**
     * 附加到之前的内容(边框外前)
     *
     * @param beforeContent String
     * @return 本身，这样可以继续设置其他属性
     */
    T setBeforeContent(String beforeContent);

    /**
     * 附加到开头的内容(边框内前)
     *
     * @return String
     */
    String getPrependContent();

    /**
     * 附加到开头的内容(边框内前)
     *
     * @param prependContent String
     * @return 本身，这样可以继续设置其他属性
     */
    T setPrependContent(String prependContent);

    /**
     * 附加到末尾的内容(边框内后)
     *
     * @return String
     */
    String getAppendContent();

    /**
     * 附加到末尾的内容(边框内后)
     *
     * @param appendContent String
     * @return 本身，这样可以继续设置其他属性
     */
    T setAppendContent(String appendContent);

    /**
     * 附加到之后的内容(边框外后)
     *
     * @return String
     */
    String getAfterContent();

    /**
     * 附加到之后的内容(边框外后)
     *
     * @param afterContent String
     * @return 本身，这样可以继续设置其他属性
     */
    T setAfterContent(String afterContent);
}
