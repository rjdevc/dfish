package com.rongji.dfish.misc.docpreview.builder;

import com.rongji.dfish.base.util.BeanUtil;
import com.rongji.dfish.misc.docpreview.data.*;
import sun.plugin.com.Utils;

import java.util.ArrayList;
import java.util.List;

public class SummaryBuilder {
    private static final int SUMMARY_SCORE=6000;
    private static final int TYPE_DOCUMENT=0;
    private static final int TYPE_CHARACTER=1;
    private static final int TYPE_PARAGRAPH=2;
    private static final int TYPE_CHARACTER_RUN=3;
    private static final int TYPE_DRAWING=4;
    private static final int TYPE_TABLE=5;
    private static final int TYPE_ROW=6;
    private static final int TYPE_CELL=7;
    private static final int TYPE_COLUMN=8;
    private static final int[] SCORES=new int[]{0,10,200,0,500,0,0,0,0};

    /**
     * 根据分数截取内容。总共SUMMARY_SCORE(6000)分
     * 碰到具体的控件扣除一定的分数，扣到没有分数了，则得到summary 注意，如果有table
     * 它里面又有内容，一般来说，有可能会产生大量扣分。
     * @param doc
     * @return
     */
    public Document build(Document doc) {
        Document d=new Document();
        int left = SUMMARY_SCORE - SCORES[TYPE_DOCUMENT];
        build(doc,d,left);
        return d;
    }
    private int build(Document from,Document to,int left){
       List<DocumentElement> des=new ArrayList<>();
       to.setBody(des);
       for(DocumentElement de: from.getBody()){
           if(left<=0)break;
           if(de instanceof Paragraph){
               left -= SCORES[TYPE_PARAGRAPH];
               Paragraph toP=new Paragraph();
               BeanUtil.copyPropertiesExact(toP,de);
               des.add(toP);
               left=build((Paragraph)de,toP,left);
           }else if(de instanceof Table){
               left -= SCORES[TYPE_TABLE];
               Table toT=new Table();
               BeanUtil.copyPropertiesExact(toT,de);
               des.add(toT);
               left=build((Table)de,toT,left);
           }
       }
       return left;
    }
    private int build(Paragraph from,Paragraph to,int left){
        List<ParagraphElement> pes=new ArrayList<>();
        to.setBody(pes);
        for(ParagraphElement pe: from.getBody()){
            if(left<=0)break;
            if(pe instanceof CharacterRun){
                left -= SCORES[TYPE_CHARACTER_RUN];
                CharacterRun toP=new CharacterRun();
                BeanUtil.copyPropertiesExact(toP,pe);
                pes.add(toP);
                left=build((CharacterRun)pe,toP,left);
            }else if(pe instanceof Drawing){
                left -= SCORES[TYPE_DRAWING];
                Drawing toT=new Drawing();
                BeanUtil.copyPropertiesExact(toT,pe);
                pes.add(toT);
                left=build((Drawing)pe,toT,left);
            }
        }

        return left;
    }

    private int build(Table from,Table to,int left){
        List<TableRow> subs=new ArrayList<>();
        to.setRows(subs);
        for(TableRow pe: from.getRows()){
            if(left<=0)break;
            left -= SCORES[TYPE_ROW];
            TableRow toP=new TableRow();
            BeanUtil.copyPropertiesExact(toP,pe);
            subs.add(toP);
            left=build((TableRow)pe,toP,left);
        }
        return left;
    }
    private int build(TableRow from,TableRow to,int left){
        List<TableCell> subs=new ArrayList<>();
        to.setCells(subs);
        for(TableCell pe: from.getCells()){
            if(left<=0)break;
            left -= SCORES[TYPE_CELL];
            TableCell toP=new TableCell();
            BeanUtil.copyPropertiesExact(toP,pe);
            subs.add(toP);
            left=build((TableCell)pe,toP,left);
        }
        return left;
    }
    private int build(TableCell from,TableCell to,int left){
        List<DocumentElement> des=new ArrayList<>();
        to.setBody(des);
        for(DocumentElement de: from.getBody()){
            if(left<=0)break;
            if(de instanceof Paragraph){
                left -= SCORES[TYPE_PARAGRAPH];
                Paragraph toP=new Paragraph();
                BeanUtil.copyPropertiesExact(toP,de);
                des.add(toP);
                left=build((Paragraph)de,toP,left);
            }else if(de instanceof Table){
                left -= SCORES[TYPE_TABLE];
                Table toT=new Table();
                BeanUtil.copyPropertiesExact(toT,de);
                des.add(toT);
                left=build((Table)de,toT,left);
            }
        }
        return left;
    }
    private int build(CharacterRun from,CharacterRun to,int left){
        //额外扣扣除每个字的分数
        if(from.getText()!=null){
            left -= from.getText().length() * SCORES[TYPE_CHARACTER];
        }
        return left;
    }
    private int build(Drawing from,Drawing to,int left){
        return left;
    }


}
