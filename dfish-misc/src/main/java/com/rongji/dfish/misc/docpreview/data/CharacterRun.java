package com.rongji.dfish.misc.docpreview.data;

/**
 * 一段文本，相当于html的Span
 */
public class CharacterRun implements ParagraphElement{
    /**
     *  划线类别- 下划线
     */
    public static final int STRIKE_UNDERLINE=1;
    /**
     * 划线类别 -中划线
     */
    public static final int STRIKE_LINE_THROUGH=2;
    /**
     * 划线类别 -双划线
     */
    public static final int STRIKE_DOUBLE_THROUGH=3;//HTML不支持
    /**
     * 划线类别- 上方划线
     */
    public static final int STRIKE_OVERLINE=4;

    /**
     * text
     * @return
     */
    public String getText() {
        return text;
    }

    /**
     * text
     * @param text
     */
    public void setText(String text) {
        this.text = text;
    }

    /**
     * color
     * @return
     */
    public String getColor() {
        return color;
    }

    /**
     * color
     * @param color
     */
    public void setColor(String color) {
        this.color = color;
    }

    /**
     * font size
     * @return
     */
    public Integer getFontSize() {
        return fontSize;
    }

    /**
     * font size
     * @param fontSize
     */
    public void setFontSize(Integer fontSize) {
        this.fontSize = fontSize;
    }

    /**
     * font family
     * @return
     */
    public String getFontFamily() {
        return fontFamily;
    }

    /**
     * font family
     * @param fontFamily
     */
    public void setFontFamily(String fontFamily) {
        this.fontFamily = fontFamily;
    }

    /**
     * 划线类型
     * @see #STRIKE_UNDERLINE
     * @see #STRIKE_LINE_THROUGH
     * @see #STRIKE_DOUBLE_THROUGH
     * @see #STRIKE_OVERLINE
     * @return Integer
     */
    public Integer getStrikeType() {
        return strikeType;
    }

    /**
     * 划线类型
     * @see #STRIKE_UNDERLINE
     * @see #STRIKE_LINE_THROUGH
     * @see #STRIKE_DOUBLE_THROUGH
     * @see #STRIKE_OVERLINE
     * @param strikeType Integer
     */
    public void setStrikeType(Integer strikeType) {
        this.strikeType = strikeType;
    }

    /**
     * 黑体
     * @return boolean
     */
    public boolean isBold() {
        return bold;
    }

    /**
     * 黑体
     * @param bold boolean
     */
    public void setBold(boolean bold) {
        this.bold = bold;
    }

    /**
     * 斜体
     * @return boolean
     */
    public boolean isItalic() {
        return italic;
    }

    /**
     * 斜体
     * @param italic
     */
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
