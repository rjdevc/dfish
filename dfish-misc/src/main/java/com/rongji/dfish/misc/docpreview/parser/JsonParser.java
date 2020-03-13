package com.rongji.dfish.misc.docpreview.parser;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.rongji.dfish.misc.docpreview.DocumentParser;
import com.rongji.dfish.misc.docpreview.data.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * 从JSON中解析出对象
 */
public class JsonParser extends DocumentParser {
    @Override
    public Document parse(InputStream is) {


        String content=null;
        try {
            ByteArrayOutputStream baos=new ByteArrayOutputStream();
            byte[]buff =new byte[8192];
            int read=-1;
            while ((read=is.read(buff))>=0){
                baos.write(buff,0,read);
            }
            byte[]ret=baos.toByteArray();
            String charset=com.rongji.dfish.base.util.StringUtil.detCharset(ret);
            content=new String(ret,charset);

        } catch (IOException e) {
            e.printStackTrace();
        }
        Document doc=new Document();
        JSONObject o =(JSONObject)JSON.parse(content);
        JSONArray body =(JSONArray)o.get("body");
        List<DocumentElement> contents = new ArrayList<>();
        parseDocumentBody(body,contents);
        doc.setBody(contents);
        return doc;
    }

    private void parseDocumentBody(JSONArray body, List<DocumentElement> contents) {
        for(Object o:body){
            JSONObject j=(JSONObject)o;
            if(DocumentElement.TYPE_PARAGRAPH.equals(j.get("type"))){
                contents.add(parseParagraph(j));
            }else if(DocumentElement.TYPE_TABLE.equals(j.get("type"))){
                contents.add(parseTable(j));
            }
        }
    }

    private DocumentElement parseTable(JSONObject j) {
        Table table=new Table();
        JSONArray columns =(JSONArray)j.get("columns");
        if(columns!=null) {
            List<TableColumn> tableColumn = new ArrayList<>();
            table.setColumns(tableColumn);
            for (Object o:columns) {
                JSONObject column =(JSONObject)o;
                TableColumn col=new TableColumn();
                col.setWidth(column.getInteger("width"));
                tableColumn.add(col);
            }
        }
        JSONArray rows =(JSONArray)j.get("rows");
        if(rows!=null) {
            List<TableRow> tableRows = new ArrayList<>();
            table.setRows(tableRows);
            for (Object o:rows) {
                JSONObject row =(JSONObject)o;
                TableRow tableRow=new TableRow();
                tableRows.add(tableRow);

                List<TableCell> tableCells = new ArrayList<>();
                tableRow.setCells(tableCells);
                JSONArray cells =(JSONArray)row.get("cells");
                for (Object o2:cells) {
                    JSONObject cell =(JSONObject)o2;
                    TableCell tableCell=new TableCell();
                    tableCells.add(tableCell);
                    tableCell.setColSpan(cell.getInteger("colSpan"));
                    tableCell.setRowSpan(cell.getInteger("rowSpan"));

                    JSONArray body =(JSONArray)cell.get("body");
                    if(body!=null) {
                        List<DocumentElement> contents=new ArrayList<>();
                        tableCell.setBody(contents);
                        for(Object o3:body){
                            JSONObject jo=(JSONObject)o3;
                            if(DocumentElement.TYPE_PARAGRAPH.equals(jo.get("type"))){
                                contents.add(parseParagraph(jo));
                            }else if(DocumentElement.TYPE_TABLE.equals(jo.get("type"))){
                                contents.add(parseTable(jo));
                            }
                        }
                    }
                }
            }
        }

        return table;
    }

    private Paragraph parseParagraph(JSONObject j) {
        Paragraph p=new Paragraph();
        p.setAlignment(j.getString("alignment"));
        p.setIndentation(j.getInteger("Indentation"));
        JSONArray body =(JSONArray)j.get("body");
        if(body!=null){
            List<ParagraphElement> contents = new ArrayList<>();
            p.setBody(contents);
            parseParagrapBody(body,contents);
        }
        return p;
    }

    private void parseParagrapBody(JSONArray body, List<ParagraphElement> contents) {
        for(Object o:body){
            JSONObject j=(JSONObject)o;
            if(ParagraphElement.TYPE_CHARACTER_RUN.equals(j.get("type"))){
                contents.add(parseRun(j));
            }else if(ParagraphElement.TYPE_DRAWING.equals(j.get("type"))){
                contents.add(parseDrawing(j));
            }
        }
    }

    private Drawing parseDrawing(JSONObject j) {
        Drawing d=new Drawing();
        d.setPicPath(j.getString("picPath"));
        d.setPicHeight(j.getInteger("picHeight"));
        d.setPicWidth(j.getInteger("picWidth"));
        return d;
    }

    private CharacterRun parseRun(JSONObject j) {
        CharacterRun cr=new CharacterRun();
        cr.setStrikeType(j.getInteger("StrikeType"));
        cr.setText(j.getString("text"));
        Boolean bold=j.getBoolean("bold");
        cr.setBold(Boolean.TRUE.equals(bold));
        cr.setColor(j.getString("color"));
        cr.setFontFamily(j.getString("fonFamily"));
        cr.setFontSize(j.getInteger("fontSize"));
        Boolean italic=j.getBoolean("italic");
        cr.setItalic(Boolean.TRUE.equals(bold));
        return cr;
    }
}
