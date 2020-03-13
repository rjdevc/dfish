package com.rongji.dfish.misc.docpreview;

import com.rongji.dfish.misc.docpreview.data.Document;
import com.rongji.dfish.misc.docpreview.parser.JsonParser;
import com.rongji.dfish.misc.docpreview.parser.TxtParser;
import com.rongji.dfish.misc.docpreview.parser.DocParser;
import com.rongji.dfish.misc.docpreview.parser.DocxParser;

import java.io.*;

/**
 * 文档预览工具的主入口
 * 用法
 * Docpreviews.of(new File(fileName3))
 *   .buildHtml();
 */
public class Docpreviews {
    public static final int JSON=0;
    public static final int TXT=1;
    public static final int DOC=2;
    public static final int WORD2003=DOC;
    public static final int DOCX=3;
    public static final int WORD2007=DOCX;
    private static final int RTF=4;
    private static final int WPS=5;
    private static final int PPT=6;
    private static final int PPTX=7;
    private static final int PDF=8;

    public static Builder of(File file){
        FileInputStream fis= null;
        try {
            fis = new FileInputStream(file);
            String fileName=file.getName().toLowerCase();
            int type=-1;
            if(fileName.endsWith(".doc")){
                type=DOC;
            }else if(fileName.endsWith(".docx")){
                type=DOCX;
            }else if(fileName.endsWith(".json")){
                type=JSON;
            }else if(fileName.endsWith(".txt")){
                type=TXT;
            }
            if(type==-1){
                throw new RuntimeException("CAN NOT parse file "+file.getAbsolutePath());
            }
            return of(fis,type);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            if(fis!=null) {
                fis.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        throw new RuntimeException("CAN NOT read file "+file.getAbsolutePath());
    }
//    private static DocumentParser DOC_PARSER=new DocParser();
//    private static DocumentParser DOCX_PARSER=new DocxParser();
//    private static DocumentParser JSON_PARSER=new JsonParser();
//    private static DocumentParser TXT_PARSER=new TxtParser();

    public static Builder of(InputStream is ,int type) {
        DocumentParser parser;
        switch (type){
            case DOCX:
                parser=new DocxParser();
                break;
            case DOC:
                parser=new DocParser();
                break;
            case TXT:
                parser=new TxtParser();
                break;
            case JSON:
                parser=new JsonParser();
                break;
            default:
                throw new IllegalArgumentException("type="+type);
        }
        Builder builder=new Builder();
        parser.setBuilder(builder);
        Document doc=parser.parse(is);
        builder.setDocument(doc);
        return builder;
    }
}
