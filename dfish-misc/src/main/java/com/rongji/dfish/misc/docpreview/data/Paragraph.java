package com.rongji.dfish.misc.docpreview.data;


import java.util.ArrayList;
import java.util.List;

public class Paragraph implements DocumentElement {

    public String getAlignment() {
        return alignment;
    }

    public void setAlignment(String alignment) {
        this.alignment = alignment;
    }

    public Integer getIndentation() {
        return indentation;
    }

    public void setIndentation(Integer indentation) {
        this.indentation = indentation;
    }

    public List<ParagraphElement> getBody() {
        return body;
    }

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
