package com.rongji.dfish.misc.docpreview.data;

/**
 * 段落内容 一般分为两种一种是文本块，一种是图形。
 */
public interface ParagraphElement {
    /**
     * 文本块 相当于HTML的Span
     */
    String TYPE_CHARACTER_RUN="r";
    /**
     * 图形
     */
    String TYPE_DRAWING="d";

    /**
     * 类型
     * @return String
     */
    String getType();
}
