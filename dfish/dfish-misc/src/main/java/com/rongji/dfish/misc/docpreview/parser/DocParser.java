package com.rongji.dfish.misc.docpreview.parser;

import com.rongji.dfish.misc.docpreview.DocumentParser;
import com.rongji.dfish.misc.docpreview.data.*;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.model.PicturesTable;
import org.apache.poi.hwpf.usermodel.Picture;
import org.apache.poi.hwpf.usermodel.Range;
import org.apache.poi.hwpf.usermodel.Section;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class DocParser extends DocumentParser {
    @Override
    public Document parse(InputStream is) {
        HWPFDocument hwpfDocument = null;
        List<DocumentElement> contents = new ArrayList<>();
        try {
            hwpfDocument = new HWPFDocument(is);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Range range = hwpfDocument.getRange();

        //FIXME 这里忽略了table//FIXME 这里忽略了table

        PicturesTable pics=hwpfDocument.getPicturesTable();
        for (int i = 0; i < range.numParagraphs(); i++) {

            org.apache.poi.hwpf.usermodel.Paragraph paragraph = range.getParagraph(i);
            // 段元素
            Paragraph p = new Paragraph();
//            p.setAlignment("center");//FIXME
            p.setIndentation(paragraph.getFirstLineIndent()==-1?null:paragraph.getFirstLineIndent() );

            // 行内元素
            int characterNum = paragraph.numCharacterRuns();
            //FIXME 这里忽略了Drawing
            if (characterNum>0){
                p.setBody(new ArrayList<ParagraphElement>());
            }
            for (int j = 0; j < characterNum; j++) {
                CharacterRun cr = new CharacterRun();
                org.apache.poi.hwpf.usermodel.CharacterRun characterRun = paragraph.getCharacterRun(j);
                cr.setText(characterRun.text());
                cr.setBold(characterRun.isBold());
                cr.setItalic(characterRun.isItalic());
                cr.setFontSize(characterRun.getFontSize()/2);
                cr.setFontFamily(characterRun.getFontName());
                cr.setColor(getHexColor(characterRun.getIco24()));
                if(  characterRun.getUnderlineCode()==1){// 下划线代号
                    cr.setStrikeType(CharacterRun.STRIKE_UNDERLINE);
                }else if(characterRun.isDoubleStrikeThrough()){
                    cr.setStrikeType(CharacterRun.STRIKE_DOUBLE_THROUGH);
                }else if(characterRun.isStrikeThrough()){
                    cr.setStrikeType(CharacterRun.STRIKE_LINE_THROUGH);
                }

                if(pics.hasPicture(characterRun)){
                    Picture pic=pics.extractPicture(characterRun, false);
                    String ext=pic.suggestFileExtension();
                    byte[] data= pic.getRawContent();
                    Drawing drawing=new Drawing();
                    savePic(data,ext,drawing);
                    p.getBody().add(drawing);
                }
                if(cr.getText()!=null&&!cr.getText().trim().equals("")){
                    p.getBody().add(cr);
                }
            }
            contents.add(p);
        }
        Document doc=new Document();
        doc.setBody(contents);
        return doc;
    }

    private static char[] HEX_CHARS = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

    protected static String getHexColor(int color) {
        if(color==-1){
            return null;
        }
        char[] cs = new char[7];
        cs[0] = '#';
        cs[1] = HEX_CHARS[color >> 4 & 0xF];
        cs[2] = HEX_CHARS[color & 0xF];
        cs[3] = HEX_CHARS[color >> 12 & 0xF];
        cs[4] = HEX_CHARS[color >> 8 & 0xF];
        cs[5] = HEX_CHARS[color >> 20 & 0xF];
        cs[6] = HEX_CHARS[color >> 16 & 0xF];
        return new String(cs);
    }
}
