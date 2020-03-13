package com.rongji.dfish.misc.docpreview.parser;

import com.rongji.dfish.misc.docpreview.DocumentParser;
import com.rongji.dfish.misc.docpreview.data.*;
import com.rongji.dfish.misc.docpreview.data.Document;
import org.apache.poi.xwpf.usermodel.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * word 03版本解析器 后缀名是.docx
 */
public class DocxParser extends DocumentParser {
    @Override
    public Document parse(InputStream is) {
        XWPFDocument xwpfDocument = null;
        List<DocumentElement> paragraphContents = new ArrayList<>();
        try {
            xwpfDocument = new XWPFDocument(is);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Iterator<IBodyElement> iter= xwpfDocument.getBodyElementsIterator();
        while (iter.hasNext()) {
            IBodyElement ibe=iter.next();
            if(ibe instanceof XWPFParagraph ){
                XWPFParagraph para = (XWPFParagraph)ibe;
                paragraphContents.add(parseParagraph(para));
            }else if(ibe instanceof XWPFTable){
                XWPFTable tbl = (XWPFTable)ibe;
                paragraphContents.add(parseTabel(tbl));
            }

        }

        Document doc=new Document();
        doc.setBody(paragraphContents);
        return doc;
    }

    private Table parseTabel(XWPFTable tbl) {
        Table table=new Table();
        //columns

        table.setRows(new ArrayList<TableRow>() );
        for( XWPFTableRow r:tbl.getRows()){
            TableRow row=new TableRow();
             table.getRows().add(row);
             row.setCells(new ArrayList<TableCell>());
             for(XWPFTableCell tc:r.getTableCells()){
                 TableCell cell=new TableCell();
//                 cell.setColSpan(tc.get);
//                 cell.setRowSpan(tc.get);
                 Iterator<IBodyElement> iter= tc.getBodyElements().iterator();
                 List<DocumentElement> body=new ArrayList<>();
                 cell.setBody(body);
                 while (iter.hasNext()) {
                     IBodyElement ibe=iter.next();
                     if(ibe instanceof XWPFParagraph ){
                         XWPFParagraph para = (XWPFParagraph)ibe;
                         body.add(parseParagraph(para));
                     }else if(ibe instanceof XWPFTable){
                         XWPFTable subTbl = (XWPFTable)ibe;
                         body.add(parseTabel(subTbl));
                     }

                 }

                 row.getCells().add(cell);
             }
        }
        ;

        return table;
    }

    private Paragraph parseParagraph(XWPFParagraph para) {


        List<XWPFRun> runsLists = para.getRuns();

        // 段元素
        Paragraph p = new Paragraph();
        // 一行可以有多个元素
        for (XWPFRun run : runsLists) {
            CharacterRun cr = new CharacterRun();
            cr.setBold(run.isBold());
            cr.setColor(run.getColor()==null?null:("#" + run.getColor()));
            cr.setItalic(run.isItalic());

            cr.setText(run.getText(0));
            cr.setFontSize(run.getFontSize()==-1?null:run.getFontSize());
            cr.setFontFamily(run.getFontFamily());
            if (run.getUnderline() != UnderlinePatterns.NONE) {// 下划线代号
                cr.setStrikeType(CharacterRun.STRIKE_UNDERLINE);
            } else if (run.isStrike()) {
                cr.setStrikeType(CharacterRun.STRIKE_LINE_THROUGH);
            }


            List <XWPFPicture>  pics=run .getEmbeddedPictures();
            if(pics!=null&&pics.size()>0){
                for(XWPFPicture pic:pics){
                    if (pic.getPictureData() != null) {
                        byte[] data=pic.getPictureData().getData();
                        String ext=pic.getPictureData().suggestFileExtension();
                        Drawing drawing=new Drawing();
                        savePic(data,ext,drawing);
                        p.getBody().add(drawing);
                    }
                }
                if(cr.getText() !=null&&!"".equals(cr.getText().trim())){
                    p.getBody().add(cr);
                }
            }else{
                p.getBody().add(cr);
            }
        }
        p.setIndentation(para.getIndentationFirstLine() == -1 ? null : para.getIndentationFirstLine() );
        p.setAlignment(para.getAlignment().name());//FIXME 总是LEFT ?
        return p;
    }


}
