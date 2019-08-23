package com.rongji.dfish.misc.docpreview.builder;

import com.rongji.dfish.base.util.BeanUtil;
import com.rongji.dfish.misc.docpreview.data.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class SummaryBuilder {
    public static final int TYPE_CHARACTER=0;
    public static final int TYPE_DOCUMENT=1;
    public static final int TYPE_PARAGRAPH=2;
    public static final int TYPE_CHARACTER_RUN=3;
    public static final int TYPE_DRAWING=4;
    public static final int TYPE_TABLE=5;
    public static final int TYPE_ROW=6;
    public static final int TYPE_CELL=7;
    public static final int TYPE_COLUMN=8;

    private int summaryScore =6000;
    private int[] scores =new int[]{10,0,200,0,500,0,0,0,0};
    public int getSummaryScore(){
        return summaryScore;
    }
    public int getScore(int type){
        return scores[type];
    }
    public void setSummaryScore(int summaryScore){
        this.summaryScore=summaryScore;
    }
    public void setScore(int type,int score){
        scores[type]=score;
    }

    /**
     * 根据分数截取内容。总共SUMMARY_SCORE(6000)分
     * 碰到具体的控件扣除一定的分数，扣到没有分数了，则得到summary 注意，如果有table
     * 它里面又有内容，一般来说，有可能会产生大量扣分。
     * @param doc
     * @return
     */
    public Document build(Document doc) {
        Document to=new Document();
        AtomicInteger score=new AtomicInteger(0);
        build(doc,score,to);
        return to;
    }
    private static final HashMap<Class,Object[]> REF_MAP=new HashMap<>();
    static{
        Object[][] types=new Object[][]{
                {Document.class,TYPE_DOCUMENT,"Body"},
                {Paragraph.class,TYPE_PARAGRAPH,"Body"},
                {Table.class,TYPE_TABLE,"Rows"},
                {TableRow.class,TYPE_ROW,"Cells"},
                {TableCell.class,TYPE_CELL,"Body"},
                {CharacterRun.class,TYPE_CHARACTER_RUN,null},
                {Drawing.class,TYPE_DRAWING,null},
        };
        for(Object[] row :types){
            REF_MAP.put((Class)row[0],row);
        }
    }
    private static final Class[] NO_PARAM=new Class[0];
    private void build(Object from,AtomicInteger score,Object to){

        BeanUtil.copyPropertiesExact(to,from);

        Class<?>clz=from.getClass();
        Object[] ref=REF_MAP.get(clz);
        if(ref!=null){
            int type=(Integer)ref[1];
            String subName=(String)ref[2];

            score.getAndAdd(scores[type]);
            if(subName!=null){
                try {
                    Method getter=clz.getMethod("get"+subName,NO_PARAM);
                    Method setter=clz.getMethod("set"+subName,List.class);
                    List toSubs=new ArrayList();
                    setter.invoke(to,toSubs);
                    List fromSubs=(List)getter.invoke(from);
                    for(Object fromSub: fromSubs) {
                        if (score.get() >= summaryScore) {
                            break;
                        }
                        Class subClz=fromSub.getClass();
                        Object toSub= subClz.newInstance();
                        build(fromSub,score,toSub);
                        toSubs.add(toSub);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        if(clz==CharacterRun.class){
            CharacterRun cr=(CharacterRun)from;
            //越多的字符将额外计算积分。
            score.getAndAdd(scores[TYPE_CHARACTER] * cr.getText().length());
        }
    }
}
