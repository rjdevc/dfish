package com.rongji.dfish.misc.docpreview.data;

/**
 * 文档元素
 */
public interface DocumentElement {
    /**
     * 段落
     */
    String TYPE_PARAGRAPH="p";
    /**
     * 表格
     */
    String TYPE_TABLE="t";

    /**
     * 类型
     * @return String
     */
    String getType();
}
