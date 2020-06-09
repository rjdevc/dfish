package com.rongji.dfish.ui.auxiliary;

import com.rongji.dfish.ui.AbstractWidget;
import com.rongji.dfish.ui.Alignable;
import com.rongji.dfish.ui.widget.Img;

/**
 * 时间轴条目
 *
 * @author lamontYu
 * @since DFish5.0
 */
public class TimelineItem extends AbstractWidget<TimelineItem> implements Alignable<TimelineItem> {

    private String align;
    private Boolean escape;
    private String format;
//    private String icon;
    private String text;
    private Img img;
    private Integer space;

    /**
     * 构造函数
     *
     * @param id
     */
    public TimelineItem(String id) {
        this.setId(id);
    }

    /**
     * 构造函数
     */
    public TimelineItem() {
        this.setId(null);
    }

    @Override
    public String getAlign() {
        return align;
    }

    @Override
    public TimelineItem setAlign(String align) {
        this.align = align;
        return this;
    }

    /**
     * 是否对html内容转义。默认值为true。
     *
     * @return Boolean
     */
    public Boolean getEscape() {
        return escape;
    }

    /**
     * 是否对html内容转义。默认值为true。
     *
     * @param escape 为true时对html内容转义
     * @return 本身，这样可以继续设置其他属性
     */
    public TimelineItem setEscape(Boolean escape) {
        this.escape = escape;
        return this;
    }

    /**
     * 格式化文本内容。"$字段名"形式的变量将被解析替换。支持"javascript:"开头的js语句(需return返回值)。
     *
     * @return String
     */
    public String getFormat() {
        return format;
    }

    /**
     * 格式化文本内容。"$字段名"形式的变量将被解析替换。支持"javascript:"开头的js语句(需return返回值)。
     *
     * @param format 内容格式
     * @return 本身，这样可以继续设置其他属性
     */
    public TimelineItem setFormat(String format) {
        this.format = format;
        return this;
    }

//    /**
//     * 图标。可用 "." 开头的样式名，或图片路径。
//     *
//     * @return String
//     */
//    public String getIcon() {
//        return icon;
//    }
//
//    /**
//     * 图标。可用 "." 开头的样式名，或图片路径。
//     *
//     * @param icon 图标
//     * @return 本身，这样可以继续设置其他属性
//     */
//    public TimelineItem setIcon(String icon) {
//        this.icon = icon;
//        return this;
//    }

    /**
     * 显示文本。
     *
     * @return String
     */
    public String getText() {
        return text;
    }

    /**
     * 显示文本。
     *
     * @param text 显示文本。
     * @return 本身，这样可以继续设置其他属性
     */
    public TimelineItem setText(String text) {
        this.text = text;
        return this;
    }

    /**
     * 图标。
     * @return Img
     */
    public Img getImg() {
        return img;
    }

    /**
     * 图标。
     * @param img Img
     * @return 本身，这样可以继续设置其他属性
     */
    public TimelineItem setImg(Img img) {
        this.img = img;
        return this;
    }

    /**
     * 和下一个节点之间的距离。
     * @return Integer
     */
    public Integer getSpace() {
        return space;
    }

    /**
     * 和下一个节点之间的距离。
     * @param space Integer
     * @return 本身，这样可以继续设置其他属性
     */
    public TimelineItem setSpace(Integer space) {
        this.space = space;
        return this;
    }
}
