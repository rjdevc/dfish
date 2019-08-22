package com.rongji.dfish.misc.docpreview.data;

public class CharacterRun implements ParagraphElement{
    public static final int STRIKE_UNDERLINE=1;
    public static final int STRIKE_LINE_THROUGH=2;
    public static final int STRIKE_DOUBLE_THROUGH=3;//HTML不支持
    public static final int STRIKE_OVERLINE=4;


    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Integer getFontSize() {
        return fontSize;
    }

    public void setFontSize(Integer fontSize) {
        this.fontSize = fontSize;
    }

    public String getFontFamily() {
        return fontFamily;
    }

    public void setFontFamily(String fontFamily) {
        this.fontFamily = fontFamily;
    }

    public Integer getStrikeType() {
        return strikeType;
    }

    public void setStrikeType(Integer strikeType) {
        this.strikeType = strikeType;
    }

    public boolean isBold() {
        return bold;
    }

    public void setBold(boolean bold) {
        this.bold = bold;
    }

    public boolean isItalic() {
        return italic;
    }

    public void setItalic(boolean italic) {
        this.italic = italic;
    }

    // 文本
    private String text;

    // 颜色 #RRGGBB
    private String color;

    // 字号
    private Integer fontSize;

    // 字体
    private String fontFamily;

    //划线类型 ，中划线，下划线，双中划线(HTML不支持)
    private Integer strikeType;

    // 是否粗体
    private boolean bold;

    // 是否斜体
    private boolean italic;


    @Override
    public String getType() {
        return TYPE_CHARACTER_RUN;
    }
}
