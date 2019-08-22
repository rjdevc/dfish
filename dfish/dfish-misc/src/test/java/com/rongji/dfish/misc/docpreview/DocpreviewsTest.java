package com.rongji.dfish.misc.docpreview;

import java.io.File;

public class DocpreviewsTest {
    public static void main(String[] args){
        Builder.getRootConfig().setFileRootPath("./");
//
//        String fileName="C:\\Users\\LinLW\\Desktop\\文本预览.doc";
//        Builder b=Docpreviews.of(new File(fileName));
//        System.out.println(b.buildJson());



        String fileName2="C:\\Users\\LinLW\\Desktop\\网贷是如何拖垮我们的.docx";
        Builder b2=Docpreviews.of(new File(fileName2));
        System.out.println(b2.buildJson());
        System.out.println(b2.buildHtml());
        System.out.println(b2.summary().buildHtml());

//        String fileName3="src/main/java/com/rongji/dfish/misc/docpreview/test.json";
//        Builder b3=Docpreviews.of(new File(fileName3));
//        System.out.println(b3.buildJson());

    }
}
