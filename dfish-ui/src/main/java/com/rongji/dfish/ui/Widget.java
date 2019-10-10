package com.rongji.dfish.ui;

/**
 * Widget 是指可见的视图部件。
 * @author DFish Team
 *
 * @param <T> 当前对象类型
 */
public interface Widget<T extends Widget<T>> extends JsonNode<T>,EventTarget<T> {


	/**
	 * 高度取决于父布局高度，一般来说是是三种情况
	 * <ol>
	 * <li>占满全屏</li>
	 * <li>占满除了其他几个指定高度的元素外的所有高度</li>
	 * <li>如果同时几个元素高度都是*，那么他们平分这个高度</li>
	 * </ol>
	 * @deprecated DEPENDS命名有歧义所以改为REMAIN和 AUTO(-1)对应
	 */
	@Deprecated
	public static final String WIDTH_DEPENDS="*";
	/**
	 * 宽度取决于父布局宽度，一般来说是是三种情况
	 * <ol>
	 * <li>占满全屏</li>
	 * <li>占满除了其他几个指定宽度的元素外的所有宽度</li>
	 * <li>如果同时几个元素宽度都是*，那么他们平分这个宽度</li>
	 * </ol>
	 * @deprecated DEPENDS命名有歧义所以改为REMAIN和 AUTO(-1)对应
	 */
	@Deprecated
	public static final String HEIGHT_DEPENDS="*";
	/**
	 * 高度取决于父布局高度，一般来说是是三种情况
	 * <ol>
	 * <li>占满全屏</li>
	 * <li>占满除了其他几个指定高度的元素外的所有高度</li>
	 * <li>如果同时几个元素高度都是*，那么他们平分这个高度</li>
	 * </ol>
	 */
	public static final String WIDTH_REMAIN="*";
	/**
	 * 宽度取决于父布局宽度，一般来说是是三种情况
	 * <ol>
	 * <li>占满全屏</li>
	 * <li>占满除了其他几个指定宽度的元素外的所有宽度</li>
	 * <li>如果同时几个元素宽度都是*，那么他们平分这个宽度</li>
	 * </ol>
	 */
	public static final String HEIGHT_REMAIN="*";
	
	/**
	 * 高度取决于自己内容的高度
	 */
	public static final String WIDTH_AUTO="-1";
	/**
	 * 宽度取决于自己内容的宽度
	 */
	public static final String HEIGHT_AUTO="-1";
	/**
     * 设置该面板所用的CSS类型 多个用空格隔开
     * @param cls String
     * @return 本身，这样可以继续设置其他属性
     */
    T setCls(String cls);
    /**
     * 取得该面板所用的CSS类型 多个用空格隔开
     * @return String
     */
    String getCls();
    
    /**
	 * 添加样式
	 * @param cls 样式
	 * @return 本身，这样可以继续设置其他属性
	 */
    T addCls(String cls);
    
    /**
   	 * 移除样式
   	 * @param cls 样式
   	 * @return 本身，这样可以继续设置其他属性
   	 */
    T removeCls(String cls);
    /**
     * 设置水平方向可用像素(宽度)减少值。
     * 由于边框的因素，面板可用的空间和面板本身可能不一致，所以需要这个差值。
     * 如：面板边框为1像素，扣掉左右各1像素，该面板的hmin=2
     * @param hmin int
     * @return 本身，这样可以继续设置其他属性
     */
    T setHmin(Integer hmin);

    /**
     * 取得水平方向可用像素(高度)减少值
     * @return String
     */
    Integer getHmin();
    /**
     * 设置竖直方向可用像素(高度)减少值
     * 由于边框的因素，面板可用的空间和面板本身可能不一致，所以需要这个差值。
     * 如：面板边框为1像素，扣掉上下各1像素，该面板的wmin=2
     * @param wmin Integer
     * @return 本身，这样可以继续设置其他属性
     */
    T setWmin(Integer wmin);

    /**
     * 取得竖直方向可用像素(高度)减少值
     * @return Integer
     */
    Integer getWmin();

    /**
     * 设置该面板所用的CSS样式 多个用分号(半角)隔开
     * @param style String
     * @return 本身，这样可以继续设置其他属性
     */
    T setStyle(String style);
    /**
     * 取得该面板所用的CSS样式 多个用分号(半角)隔开
     * @return String
     */
    String getStyle();
	/**
	 * 部件在加入布局的时候一般会有大小限制
	 * @return String
	 */
	String getHeight();
	/**
	 * 部件在加入布局的时候一般会有大小限制
	 * @param height String
	 * @return 本身，这样可以继续设置其他属性
	 */
	T setHeight(String height);
	/**
	 * 部件在加入布局的时候一般会有大小限制,方法效果如{@link #setHeight(String)}
	 * @param height int
	 * @return 本身，这样可以继续设置其他属性
	 */
	T setHeight(int height);
	/**
	 * 部件在加入布局的时候一般会有大小限制
	 * @return String
	 */
	String getWidth();
	/**
	 * 部件在加入布局的时候一般会有大小限制
	 * @param width String
	 * @return 本身，这样可以继续设置其他属性
	 */
	T setWidth(String width);
	/**
	 * 部件在加入布局的时候一般会有大小限制,方法效果如{@link #setWidth(String)}
	 * @param width int
	 * @return 本身，这样可以继续设置其他属性
	 */
	T setWidth(int width);
	
	/**
	 * 部件在加入布局的时候一般会有大小限制
	 * @return Integer
	 */
	String getMaxheight();
	/**
	 * 部件在加入布局的时候一般会有大小限制
	 * @param maxheight Integer
	 * @return 本身，这样可以继续设置其他属性
	 */
	T setMaxheight(int maxheight);
	/**
	 * 部件在加入布局的时候一般会有大小限制
	 * @param maxheight Integer
	 * @return 本身，这样可以继续设置其他属性
	 */
	T setMaxheight(String maxheight);
	/**
	 * 部件在加入布局的时候一般会有大小限制
	 * @return Integer
	 */
	String getMaxwidth();
	/**
	 * 部件在加入布局的时候一般会有大小限制
	 * @param maxwidth Integer
	 * @return 本身，这样可以继续设置其他属性
	 */
	T setMaxwidth(int maxwidth);
	/**
	 * 部件在加入布局的时候一般会有大小限制
	 * @param maxwidth Integer
	 * @return 本身，这样可以继续设置其他属性
	 */
	T setMaxwidth(String maxwidth);

	/**
	 * 部件在加入布局的时候一般会有大小限制
	 * @return Integer
	 */
	String getMinheight();
	/**
	 * 部件在加入布局的时候一般会有大小限制
	 * @param minheight Integer
	 * @return 本身，这样可以继续设置其他属性
	 */
	T setMinheight(int minheight);
	/**
	 * 部件在加入布局的时候一般会有大小限制
	 * @param minheight Integer
	 * @return 本身，这样可以继续设置其他属性
	 */
	T setMinheight(String minheight);
	/**
	 * 部件在加入布局的时候一般会有大小限制
	 * @return Integer
	 */
	String getMinwidth();
	/**
	 * 部件在加入布局的时候一般会有大小限制
	 * @param minwidth Integer
	 * @return 本身，这样可以继续设置其他属性
	 */
	T setMinwidth(String minwidth);
	/**
	 * 部件在加入布局的时候一般会有大小限制
	 * @param minwidth Integer
	 * @return 本身，这样可以继续设置其他属性
	 */
	T setMinwidth(int minwidth);
	 /**
     * 设置自定义的全局ID。可通过 $.globals[ gid ] 方法来获取 widget。
     * @param gid String
	 * @return 本身，这样可以继续设置其他属性
     */
    T setGid(String gid);

    /**
     * 取得自定义的全局ID。可通过 $.globals[ gid ] 方法来获取 widget。
     * @return String
     */
    String getGid();
    
	/**
	 * 附加到之前的内容(边框外前)
	 * @return String
	 */
	String getBeforecontent() ;
	/**
	 * 附加到之前的内容(边框外前)
	 * @param beforecontent String
	 * @return 本身，这样可以继续设置其他属性
	 */
	T setBeforecontent(String beforecontent);
	/**
	 * 附加到开头的内容(边框内前)
	 * @return String
	 */
	String getPrependcontent();
	/**
	 * 附加到开头的内容(边框内前)
	 * @param prependcontent String
	 * @return 本身，这样可以继续设置其他属性
	 */
	T setPrependcontent(String prependcontent);
	/**
	 * 附加到末尾的内容(边框内后)
	 * @return String
	 */
	String getAppendcontent() ;
	/**
	 * 附加到末尾的内容(边框内后)
	 * @param appendcontent String
	 * @return 本身，这样可以继续设置其他属性
	 */
	T setAppendcontent(String appendcontent);
	/**
	 * 附加到之后的内容(边框外后)
	 * @return String
	 */
	String getAftercontent();
	/**
	 * 附加到之后的内容(边框外后)
	 * @param aftercontent String
	 * @return 本身，这样可以继续设置其他属性
	 */
	T setAftercontent(String aftercontent);
}
