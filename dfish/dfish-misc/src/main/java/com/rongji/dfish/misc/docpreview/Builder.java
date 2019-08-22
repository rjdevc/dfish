package com.rongji.dfish.misc.docpreview;

import com.alibaba.fastjson.JSON;
import com.rongji.dfish.base.util.StringUtil;
import com.rongji.dfish.misc.docpreview.builder.HtmlBuilder;
import com.rongji.dfish.misc.docpreview.builder.SummaryBuilder;
import com.rongji.dfish.misc.docpreview.data.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class Builder {
    private BuilderConfig config;
    private Document doc;
    public Builder(){
        config=getRootConfig().clone();
    }
    public void setDocument(Document doc){
        this.doc=doc;
    }


    public Builder summary(){
        Builder b= new Builder();
        SummaryBuilder sb=new SummaryBuilder();
        b.setDocument(sb.build(doc));
        return b;
    }

    public String buildJson(){
        return JSON.toJSONString(doc);
    }
    public String buildHtml(){
        StringBuilder sb=new StringBuilder();
        HtmlBuilder hmtlBuilder =new HtmlBuilder(getConfig());
        hmtlBuilder.build(doc,sb);
        return sb.toString();
    }




    private static BuilderConfig rootConfg=new BuilderConfig();
    public static BuilderConfig getRootConfig(){
        return rootConfg;
    }
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
        if(ext!=null&&!ext.equals("")){
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
        public synchronized int getPicSeq() {
            return ++picSeq;
        }
    }

}
