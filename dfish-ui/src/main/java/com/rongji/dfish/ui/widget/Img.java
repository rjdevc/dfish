package com.rongji.dfish.ui.widget;

import com.rongji.dfish.ui.*;
import com.rongji.dfish.ui.auxiliary.Badge;
import com.rongji.dfish.ui.auxiliary.BadgeHolder;
import com.rongji.dfish.ui.command.Tip;
import com.rongji.dfish.ui.form.AbstractBox;
import com.rongji.dfish.ui.form.BoxHolder;

/**
 * img 图片
 *
 * @author DFish Team
 */
public class Img extends AbstractWidget<Img> implements Alignable<Img>, VAlignable<Img>, HtmlContentHolder<Img>,
        HasText<Img>, BoxHolder<Img>, BadgeHolder<Img> {
    /**
     *
     */
    private static final long serialVersionUID = 672057364742087182L;
    /**
     * 图片文字排列方向-水平
     */
    public static final String DIR_HORIZONTAL = "h";
    /**
     * 图片文字排列方向-垂直(默认值)
     */
    public static final String DIR_VERTICAL = "v";

    private AbstractBox box;
    private Boolean br;
    private String text;
    private String src;
    private Boolean focus;
    private Boolean focusable;
    private String align;
    private String vAlign;
    private Object tip;
    private String description;
    private Integer textWidth;
    private String dir;
    private String imgWidth;
    private String imgHeight;
    private String format;
    private Boolean escape;
    private Object badge;

    /**
     * 构造函数
     *
     * @param src String 图标
     */
    public Img(String src) {
        this.setSrc(src);
    }

    /**
     * 构造函数
     *
     * @param src  String 图标
     * @param text String 显示文本
     */
    public Img(String src, String text) {
        super();
        this.setSrc(src);
        this.setText(text);
    }

    /**
     * 选项表单，类型是 checkbox 或 radio。取消或勾选这个box，将同步fieldset内部所有表单的状态。
     *
     * @return box
     */
    @Override
    public AbstractBox getBox() {
        return box;
    }

    /**
     * 选项表单，类型是 checkbox 或 radio。取消或勾选这个box，将同步fieldset内部所有表单的状态。
     *
     * @param box 附带的选项表单
     * @return 本身，这样可以继续设置其他属性
     */
    @Override
    public Img setBox(AbstractBox box) {
        this.box = box;
        return this;
    }

    /**
     * 文本是否换行。
     *
     * @return Boolean
     */
    public Boolean getBr() {
        return br;
    }

    /**
     * 文本是否换行。
     *
     * @param br Boolean
     * @return 本身，这样可以继续设置其他属性
     */
    public Img setBr(Boolean br) {
        this.br = br;
        return this;
    }

    /**
     * 是否有滚动条。
     *
     * @return text 显示文本
     */
    @Override
    public String getText() {
        return text;
    }

    /**
     * 是否有滚动条。
     *
     * @param text 显示文本
     * @return 本身，这样可以继续设置其他属性
     */
    @Override
    public Img setText(String text) {
        this.text = text;
        return this;
    }

    /**
     * 图片地址。
     *
     * @return src
     */
    public String getSrc() {
        return src;
    }

    /**
     * 图片地址。
     *
     * @param src String
     * @return 本身，这样可以继续设置其他属性
     */
    public Img setSrc(String src) {
        this.src = src;
        return this;
    }

    /**
     * 是否焦点状态。
     *
     * @return Boolean
     */
    public Boolean getFocus() {
        return focus;
    }

    /**
     * 是否焦点状态。
     *
     * @param focus Boolean
     * @return 本身，这样可以继续设置其他属性
     */
    public Img setFocus(Boolean focus) {
        this.focus = focus;
        return this;
    }

    /**
     * 是否可聚焦
     *
     * @return Boolean
     */
    public Boolean getFocusable() {
        return focusable;
    }

    /**
     * 设置是否可聚焦
     *
     * @param focusable Boolean
     * @return 本身，这样可以继续设置其他属性
     */
    public Img setFocusable(Boolean focusable) {
        this.focusable = focusable;
        return this;
    }

    /**
     * 水平对齐方式
     *
     * @return String
     */
    @Override
    public String getAlign() {
        return align;
    }

    /**
     * 水平对齐方式
     *
     * @param align String
     * @return 本身，这样可以继续设置其他属性
     */
    @Override
    public Img setAlign(String align) {
        this.align = align;
        return this;
    }

    /**
     * 垂直对齐方式
     */
    @Override
    public String getVAlign() {
        return vAlign;
    }

    /**
     * 垂直对齐方式
     */
    @Override
    public Img setVAlign(String vAlign) {
        this.vAlign = vAlign;
        return this;
    }

    /**
     * 提示
     *
     * @return String
     */
    public Object getTip() {
        return tip;
    }

    /**
     * 提示
     *
     * @param tip String
     * @return 本身，这样可以继续设置其他属性
     */
    public Img setTip(Boolean tip) {
        this.tip = tip;
        return this;
    }

    /**
     * 提示
     *
     * @param tip String
     * @return 本身，这样可以继续设置其他属性
     */
    public Img setTip(String tip) {
        this.tip = tip;
        return this;
    }

    /**
     * 提示
     *
     * @param tip Tip 提示命令
     * @return 本身，这样可以继续设置其他属性
     */
    public Img setTip(Tip tip) {
        this.tip = tip;
        return this;
    }

    /**
     * 图片描述
     *
     * @return description
     */
    public String getDescription() {
        return description;
    }

    /**
     * 图片描述
     *
     * @param description 图片描述
     * @return 本身，这样可以继续设置其他属性
     */
    public Img setDescription(String description) {
        this.description = description;
        return this;
    }

    /**
     * 文本宽度
     *
     * @return textwidth
     */
    public Integer getTextWidth() {
        return textWidth;
    }

    /**
     * 文本宽度
     *
     * @param textWidth 文本宽度
     * @return 本身，这样可以继续设置其他属性
     */
    public Img setTextWidth(Integer textWidth) {
        this.textWidth = textWidth;
        return this;
    }

    /**
     * 图片文字排列方向(水平或垂直)
     *
     * @return String
     */
    public String getDir() {
        return dir;
    }

    /**
     * 图片文字排列方向(水平或垂直)
     *
     * @param dir String
     * @return 本身，这样可以继续设置其他属性
     */
    public Img setDir(String dir) {
        this.dir = dir;
        return this;
    }

    /**
     * 图片宽度。
     *
     * @return Integer
     */
    public String getImgWidth() {
        return imgWidth;
    }

    /**
     * 图片宽度。
     *
     * @param imgWidth Integer
     * @return 本身，这样可以继续设置其他属性
     */
    public Img setImgWidth(Integer imgWidth) {
        this.imgWidth = String.valueOf(imgWidth);
        return this;
    }

    /**
     * 图片宽度。
     *
     * @param imgWidth String
     * @return 本身，这样可以继续设置其他属性
     */
    public Img setImgWidth(String imgWidth) {
        this.imgWidth = imgWidth;
        return this;
    }

    /**
     * 图片高度。
     *
     * @return Integer
     */
    public String getImgHeight() {
        return imgHeight;
    }

    /**
     * 图片高度。
     *
     * @param imgHeight Integer
     * @return 本身，这样可以继续设置其他属性
     */
    public Img setImgHeight(Integer imgHeight) {
        this.imgHeight = String.valueOf(imgHeight);
        return this;
    }

    /**
     * 图片高度。
     *
     * @param imgHeight String
     * @return 本身，这样可以继续设置其他属性
     */
    public Img setImgHeight(String imgHeight) {
        this.imgHeight = imgHeight;
        return this;
    }

    @Override
    public String getFormat() {
        return format;
    }

    @Override
    public Img setFormat(String format) {
        this.format = format;
        return this;
    }

    /**
     * 用于显示文本是否需要转义,不设置默认是true
     *
     * @return Boolean
     */
    @Override
    public Boolean getEscape() {
        return escape;
    }

    /**
     * 用于显示文本是否需要转义,不设置默认是true
     *
     * @param escape Boolean
     * @return 本身，这样可以继续设置其他属性
     */
    @Override
    public Img setEscape(Boolean escape) {
        this.escape = escape;
        return this;
    }

    /**
     * 显示徽标
     *
     * @return Object
     */
    @Override
    public Object getBadge() {
        return badge;
    }

    @Override
    public Img setBadge(Boolean badge) {
        this.badge = badge;
        return this;
    }

    @Override
    public Img setBadge(String badge) {
        this.badge = badge;
        return this;
    }

    @Override
    public Img setBadge(Badge badge) {
        this.badge = badge;
        return this;
    }

}
