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

/**
 * 转成Summary   summary是Text的一部分。
 */
public class SummaryBuilder {
    /**
     * 组件类型-字符
     */
    public static final int TYPE_CHARACTER=0;
    /**
     * 组件类型-文档
     */
    public static final int TYPE_DOCUMENT=1;
    /**
     * 组件类型-段落
     */
    public static final int TYPE_PARAGRAPH=2;
    /**
     * 组件类型-一段文本，相当于html的Span
     */
    public static final int TYPE_CHARACTER_RUN=3;
    /**
     * 组件类型-图片
     */
    public static final int TYPE_DRAWING=4;
    /**
     * 组件类型-表格
     */
    public static final int TYPE_TABLE=5;
    /**
     * 组件类型-表格行
     */
    public static final int TYPE_ROW=6;
    /**
     * 组件类型-单元格
     */
    public static final int TYPE_CELL=7;
    /**
     * 组件类型-列
     */
    public static final int TYPE_COLUMN=8;

    private int summaryScore = 6000;
    private int[] scores =new int[]{10,0,200,0,500,0,0,0,0};

    /**
     * 总分，满了6000分左右截止
     * @return int
     */
    public int getSummaryScore(){
        return summaryScore;
    }

    /**
     * type类型的元素对应的分数
     * @param type
     * @return int
     */
    public int getScore(int type){
        return scores[type];
    }

    /**
     * 总分
     * @param summaryScore
     */
    public void setSummaryScore(int summaryScore){
        this.summaryScore=summaryScore;
    }

    /**
     * type类型的元素对应的分数
     * @param type
     * @param score
     */
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
        AtomicInteger ignored=new AtomicInteger(0);
        build(doc,score,ignored,to);
        to.setTotalScore(score.get()+ignored.get());
        to.setSummaryScore(score.get());
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
    private void build(Object from,AtomicInteger score,AtomicInteger ignored,Object to){
        if(to!=null) {
            BeanUtil.copyPropertiesExact(to, from);
        }

        Class<?>clz=from.getClass();
        Object[] ref=REF_MAP.get(clz);
        boolean appendToSum=score.get() < summaryScore;
        if(ref!=null){
            int type=(Integer)ref[1];
            String subName=(String)ref[2];

            int sc=scores[type];
            if(appendToSum){score.getAndAdd(sc);}
            else{ignored.getAndAdd(sc);}
            if(subName!=null){
                try {
                    Method getter=clz.getMethod("get"+subName,NO_PARAM);
                    List toSubs = null;
                    if(appendToSum) {
                        Method setter = clz.getMethod("set" + subName, List.class);
                        toSubs = new ArrayList();
                        setter.invoke(to, toSubs);
                    }
                    List fromSubs=(List)getter.invoke(from);
                    for(Object fromSub: fromSubs) {
                        appendToSum=score.get() < summaryScore;
                        Class subClz=fromSub.getClass();
                        Object toSub= appendToSum?subClz.newInstance():null;
                        build(fromSub,score,ignored,toSub);
                        if(appendToSum){
                            toSubs.add(toSub);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        if(clz==CharacterRun.class){
            CharacterRun cr=(CharacterRun)from;
            //越多的字符将额外计算积分。
            int sc=scores[TYPE_CHARACTER] * cr.getText().length();
            if(appendToSum){score.getAndAdd(sc);}
            else{ignored.getAndAdd(sc);}
        }
    }
}
