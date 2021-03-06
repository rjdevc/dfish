package com.rongji.dfish.misc.docpreview;

import com.alibaba.fastjson.JSON;
import com.rongji.dfish.base.util.StringUtil;
import com.rongji.dfish.misc.docpreview.builder.HtmlBuilder;
import com.rongji.dfish.misc.docpreview.builder.SummaryBuilder;
import com.rongji.dfish.misc.docpreview.builder.TextBuilder;
import com.rongji.dfish.misc.docpreview.data.*;
import com.rongji.dfish.misc.docpreview.parser.ImageInfo;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 * 预览构建器
 */
public class Builder {
    private BuilderConfig config;
    private Document doc;

    /**
     * 构造函数
     */
    public Builder(){
        config=getRootConfig().clone();
    }

    /**
     * 设置文档对象
     * @param doc Document
     */
    public void setDocument(Document doc){
        this.doc=doc;
    }

    /**
     * 取得文档对象
     * @return Document
     */
    public Document getDocument(){
        return doc;
    }
    /**
     * 取得摘要
     * @return this
     */
    public Builder summary(){
        Builder b= new Builder();
        SummaryBuilder sb=new SummaryBuilder();
        sb.setSummaryScore(this.config.getSummaryScore());
        b.setDocument(sb.build(doc));
        return b;
    }

    /**
     * 转化成JSON，只有转化成JSON，才能无损转回。
     * @return String
     */
    public String buildJson(){
        return JSON.toJSONString(doc);
    }

    /**
     * 转化成HTML 显示
     * @return String
     */
    public String buildHtml(){

        HtmlBuilder hmtlBuilder =new HtmlBuilder(getConfig());
        return  hmtlBuilder.build(doc);
    }

    /**
     * 获得文本，有可能比较长
     * @return String
     */
    public String buildText(){
        TextBuilder hmtlBuilder =new TextBuilder();
        return  hmtlBuilder.build(doc);
    }


    private static BuilderConfig rootConfg=new BuilderConfig();

    /**
     * 取得根配置。
     * @return
     */
    public static BuilderConfig getRootConfig(){
        return rootConfg;
    }

    /**
     * 取得当前配置。
     * @return
     */
    public BuilderConfig getConfig(){
        return config;
    }



    /**
     * 保存图片，并把产生的路径信息写入到drawing中。
     * @param data 图片数据
     * @param ext 扩展名
     * @param drawing 将路径回写到drawing中
     */
    void savePic(byte[] data, String ext, Drawing drawing) {
        String rootPath=getConfig().getFileRootPath();
        rootPath=rootPath.replace('\\','/');
        String datePath =null;
        synchronized (SDF){
            datePath=SDF.format(new Date());
        }
        PicContext context=getPicContext();
        String folderPath=rootPath+"/"+datePath+"/"+context.getId();
        String fileName=String.valueOf(context.getPicSeq());
        if(ext!=null&&!"".equals(ext)){
            if(ext.startsWith(".")){
                fileName+=ext;
            }else{
                fileName+="."+ext;
            }
        }

        File folder=new File(folderPath);
        folder.mkdirs();
        FileOutputStream fos= null;
        try {
            fos = new FileOutputStream(folderPath+"/"+fileName);
            fos.write(data);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(fos!=null) {
                try {
                    fos.close();
                } catch (IOException e) {}
            }
        }
        ImageInfo info=ImageInfo.of(data);
        if(info!=null&&info.getType()!=null) {
            drawing.setHintWidth(info.getWidth());
            drawing.setHintHeight(info.getHeight());
        }

        drawing.setPicPath(datePath+"/"+context.getId()+"/"+fileName);
    }
    private static final SimpleDateFormat SDF=new SimpleDateFormat("yyMM");
    private PicContext picContext;
    private synchronized PicContext getPicContext(){
        if(picContext==null){
            picContext=new PicContext();
        }
        return picContext;
    }

    private static class PicContext{
        Random r=new Random();
        String id;

        /**
         * 产生有一定顺序的ID。不完全保证ID不重复
         * 某些月份可能是现有 EF开头的ID 后有0123
         * 1.024秒内的ID。前面5位是一样的。后面3位为随机
         * @return ID String
         */
        public synchronized String getId() {
            if(id==null){
                long now=System.currentTimeMillis();
                char[] cs = new char[8];
                cs[0] = ALPHABET[(int)(now>>30 &0x1F)];
                cs[1] = ALPHABET[(int)(now>>25 &0x1F)];
                cs[2] = ALPHABET[(int)(now>>20 &0x1F)];
                cs[3] = ALPHABET[(int)(now>>15 &0x1F)];
                cs[4] = ALPHABET[(int)(now>>10 &0x1F)];
                cs[5] = ALPHABET[r.nextInt(32)];
                cs[6] = ALPHABET[r.nextInt(32)];
                cs[7] = ALPHABET[r.nextInt(32)];
                id=new String(cs);
            }
            return id;
        }
        private static final char[] ALPHABET = {
                '0', '1', '2', '3', '4', '5', '6', '7',
                '8', '9', 'A', 'B', 'C', 'D', 'E', 'F',
                'G', 'H', 'J', 'K', 'M', 'N', 'P', 'Q',
                'R', 'S', 'T', 'V', 'W', 'X', 'Y', 'Z'
        };
        int picSeq=0;

        /**
         * 取得序号
         * @return int
         */
        public synchronized int getPicSeq() {
            return ++picSeq;
        }
    }

}
