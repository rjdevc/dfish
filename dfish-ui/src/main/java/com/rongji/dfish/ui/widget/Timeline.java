package com.rongji.dfish.ui.widget;


import com.rongji.dfish.ui.AbstractWidget;
import com.rongji.dfish.ui.Alignable;
import com.rongji.dfish.ui.PubHolder;
import com.rongji.dfish.ui.layout.AbstractNodeContainer;

/**
 * 时间轴
 * @author lamontYu
 * @date 2019-11-25 11:52
 */
public class Timeline extends AbstractNodeContainer<Timeline> implements Alignable<Timeline>, PubHolder<Timeline, Timeline.Item> {

    private String align;
    private Item pub;

    /**
     * 构造函数
     *
     * @param id String
     */
    public Timeline(String id) {
        super(id);
    }

    @Override
    public String getAlign() {
        return align;
    }

    @Override
    public Timeline setAlign(String align) {
        this.align = align;
        return this;
    }

    private Item pub() {
        if (this.pub == null) {
            this.pub = new Item();
        }
        return this.pub;
    }

    @Override
    public Item getPub() {
        return pub();
    }

    @Override
    public Timeline setPub(Item pub) {
        this.pub = pub;
        return this;
    }

    /**
     * 时间轴条目
     *
     * @author lamontYu
     * @date 2019-11-25 11:54
     */
    public static class Item extends AbstractWidget<Item> implements Alignable<Item> {

        private String align;
        private Boolean escape;
        private String format;
        private String icon;
        private String text;

        @Override
        public String getType() {
            return "TimelineItem";
        }

        @Override
        public String getAlign() {
            return align;
        }

        @Override
        public Item setAlign(String align) {
            this.align = align;
            return this;
        }

        /**
         * 是否对html内容转义。默认值为true。
         * @return Boolean
         */
        public Boolean getEscape() {
            return escape;
        }

        /**
         * 是否对html内容转义。默认值为true。
         * @param escape 为true时对html内容转义
         * @return 本身，这样可以继续设置其他属性
         */
        public Item setEscape(Boolean escape) {
            this.escape = escape;
            return this;
        }

        /**
         * 格式化文本内容。"$字段名"形式的变量将被解析替换。支持"javascript:"开头的js语句(需return返回值)。
         * @return String
         */
        public String getFormat() {
            return format;
        }

        /**
         * 格式化文本内容。"$字段名"形式的变量将被解析替换。支持"javascript:"开头的js语句(需return返回值)。
         * @param format 内容格式
         * @return 本身，这样可以继续设置其他属性
         */
        public Item setFormat(String format) {
            this.format = format;
            return this;
        }

        /**
         * 图标。可用 "." 开头的样式名，或图片路径。
         * @return String
         */
        public String getIcon() {
            return icon;
        }

        /**
         * 图标。可用 "." 开头的样式名，或图片路径。
         * @param icon 图标
         * @return 本身，这样可以继续设置其他属性
         */
        public Item setIcon(String icon) {
            this.icon = icon;
            return this;
        }

        /**
         * 显示文本。
         * @return String
         */
        public String getText() {
            return text;
        }

        /**
         * 显示文本。
         * @param text 显示文本。
         * @return 本身，这样可以继续设置其他属性
         */
        public Item setText(String text) {
            this.text = text;
            return this;
        }
    }
}
