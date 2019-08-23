package com.rongji.dfish.misc.docpreview.builder;

import com.rongji.dfish.base.Utils;
import com.rongji.dfish.misc.docpreview.BuilderConfig;
import com.rongji.dfish.misc.docpreview.data.*;

/**
 * 转成HTML
 */
public class HtmlBuilder {
    BuilderConfig config;
    public HtmlBuilder(BuilderConfig config){
        this.config=config;
    }

    public String build(Document doc) {
        StringBuilder sb=new StringBuilder();
        build(doc,sb);
        return sb.toString();
    }
    protected void build(Document doc, StringBuilder sb) {
        sb.append("<div class='document_prew'>");
        for(DocumentElement de:doc.getBody()){
            if(de instanceof Paragraph){
                build((Paragraph)de, sb);
            }else if(de instanceof Table){
                build((Table)de, sb);
            }
        }
        sb.append("</div>");
    }
    protected void build(Paragraph p, StringBuilder sb) {
        if((p.getAlignment()!=null&&!p.getAlignment().equals("LEFT"))||p.getIndentation()!=null){
            sb.append("<p style=\"");
            if(p.getAlignment()!=null&&!p.getAlignment().equals("LEFT")) {
                sb.append("text-align:")
                        .append(p.getAlignment());
            }
            if(p.getIndentation()!=null) {
                sb.append("text-indent:")
                        .append(p.getIndentation()/15)
                        .append("px");//twenties 转为px
            }
            sb.append("\">");
        }else{
            sb.append("<p>");
        }
        for(ParagraphElement pe:p.getBody()){
            if(pe instanceof CharacterRun){
                build((CharacterRun)pe, sb);
            }else if(pe instanceof Drawing){
                build((Drawing)pe, sb);
            }
        }
        sb.append("</p>\r\n");
    }
    protected void build(CharacterRun cr, StringBuilder sb) {
        sb.append("<span");
        if(cr.getColor() != null||cr.getFontFamily() != null||cr.getFontSize() != null||cr.getStrikeType() != null) {
            sb.append(" style=\"");
            if (cr.getColor() != null) {
                sb.append("color:");
                sb.append(cr.getColor());
                sb.append(';');
            }
            if (cr.getFontFamily() != null) {
                sb.append("font-family:");
                sb.append(cr.getFontFamily());
                sb.append(';');
            }
            if (cr.getFontSize() != null) {
                sb.append("font-size:");
                sb.append(cr.getFontSize());
                sb.append("pt;");
            }
            if (cr.getStrikeType() != null) {
                String stikeCss = "none";
                switch (cr.getStrikeType()) {
                    case CharacterRun.STRIKE_LINE_THROUGH:
                        stikeCss = "line-through";
                        break;
                    case CharacterRun.STRIKE_OVERLINE:
                        stikeCss = "overline";
                        break;
                    case CharacterRun.STRIKE_UNDERLINE:
                        stikeCss = "underline";
                        break;
                }
                sb.append("text-decoration:");
                sb.append(stikeCss);
                sb.append(';');
            }
            sb.append('"');
        }
        sb.append('>');
        Utils.escapeXMLword(cr.getText(), sb );
        sb.append("</span>");
    }
    protected void build(Drawing cr, StringBuilder sb) {
        //FIXME 相对路径 config  业务可以扩展此方法改写路径
        // 如果地址是动态地址如 xxx/download?id=xxx&seq=1
        //FIXME Config中原则上要提供该参数会回调方法。
        // 如果地址是可访问文件地址如 docpic/datefolder/id/seq.jpg
        sb.append("<img");
        if(cr.getPicHeight()!=null){
            sb.append(" height='").append(cr.getPicHeight()).append("'");
        }
        if(cr.getPicWidth()!=null){
            sb.append(" width='").append(cr.getPicHeight()).append("'");
        }
        sb.append(" src='").append(cr.getPicPath()).append("'/>");
    }

    protected void build(Table table, StringBuilder sb) {
        //FIXME 各列的宽度
        sb.append("<table>");
        for(TableRow row:table.getRows()){
            build(row,sb);
        }
        sb.append("</table>\r\n");
    }
    protected void build(TableRow row, StringBuilder sb) {
        sb.append("<tr>");
        for(TableCell cell:row.getCells()){
            build(cell,sb);
        }
        sb.append("</tr>");
    }
    protected void build(TableCell td, StringBuilder sb) {
        sb.append("<td");
        if(td.getColSpan()!=null&&td.getColSpan()>1){
            sb.append(" colspan='");
            sb.append(td.getColSpan());
            sb.append('\'');
        }
        if(td.getRowSpan()!=null&&td.getRowSpan()>1){
            sb.append(" rowspan='");
            sb.append(td.getRowSpan());
            sb.append('\'');
        }
        sb.append(">");
        for(DocumentElement de:td.getBody()){
            if(de instanceof Paragraph){
                build((Paragraph)de, sb);
            }else if(de instanceof Table){
                build((Table)de, sb);
            }
        }
        sb.append("</td>");
    }

}
