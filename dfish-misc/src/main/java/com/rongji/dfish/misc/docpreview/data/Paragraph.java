package com.rongji.dfish.misc.docpreview.data;


import java.util.ArrayList;
import java.util.List;

/**
 * 段落
 */
public class Paragraph implements DocumentElement {

    /**
     * 对齐方式
     * @return String
     */
    public String getAlignment() {
        return alignment;
    }

    /**
     * 对齐方式
     * @param alignment String
     */
    public void setAlignment(String alignment) {
        this.alignment = alignment;
    }

    /**
     * 缩进
     * @return Integer
     */
    public Integer getIndentation() {
        return indentation;
    }

    /**
     * 缩进
     * @param indentation Integer
     */
    public void setIndentation(Integer indentation) {
        this.indentation = indentation;
    }

    /**
     * 主体内容
     * @return List
     */
    public List<ParagraphElement> getBody() {
        return body;
    }

    /**
     * 主体内容
     * @param body List
     */
    public void setBody(List<ParagraphElement> body) {
        this.body = body;
    }

    // 对齐方式
    private String alignment;

    // 行前缩进
    private Integer indentation;

    // 行元素
    private List<ParagraphElement> body = new ArrayList<ParagraphElement>();

    @Override
    public String getType() {
        return TYPE_PARAGRAPH;
    }
}
