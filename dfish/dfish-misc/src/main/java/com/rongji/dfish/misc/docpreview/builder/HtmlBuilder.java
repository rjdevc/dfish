package com.rongji.dfish.misc.docpreview.builder;

import com.rongji.dfish.base.Utils;
import com.rongji.dfish.misc.docpreview.BuilderConfig;
import com.rongji.dfish.misc.docpreview.data.*;

public class HtmlBuilder {
    BuilderConfig config;
    public HtmlBuilder(BuilderConfig config){
        this.config=config;
    }

    public void build(Document doc, StringBuilder sb) {
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
    public void build(Paragraph p, StringBuilder sb) {
        if(p.getAlignment()!=null&&!p.getAlignment().equals("LEFT")){
            sb.append("<p style='text-align:")
                    .append(p.getAlignment())
                    .append("'>");
        }else{
            sb.append("<p>");
        }
        //FIMXE indent
        for(ParagraphElement pe:p.getBody()){
            if(pe instanceof CharacterRun){
                build((CharacterRun)pe, sb);
            }else if(pe instanceof Drawing){
                build((Drawing)pe, sb);
            }
        }
        sb.append("</p>\r\n");
    }
    public void build(CharacterRun cr, StringBuilder sb) {
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
                sb.append("px;");
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
    public void build(Drawing cr, StringBuilder sb) {
        //FIXME 相对路径 config  业务可以扩展次方法改写路径
        sb.append("<img");
        if(cr.getPicHeight()!=null){
            sb.append(" height='").append(cr.getPicHeight()).append("'");
        }
        if(cr.getPicWidth()!=null){
            sb.append(" width='").append(cr.getPicHeight()).append("'");
        }
        sb.append(" src='").append(cr.getPicPath()).append("'/>");
    }

    public void build(Table table, StringBuilder sb) {
        //FIXME 各列的宽度
        sb.append("<table>");
        for(TableRow row:table.getRows()){
            build(row,sb);
        }
        sb.append("</table>\r\n");
    }
    public void build(TableRow row, StringBuilder sb) {
        sb.append("<tr>");
        for(TableCell cell:row.getCells()){
            build(cell,sb);
        }
        sb.append("</tr>");
    }
    public void build(TableCell td, StringBuilder sb) {
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
