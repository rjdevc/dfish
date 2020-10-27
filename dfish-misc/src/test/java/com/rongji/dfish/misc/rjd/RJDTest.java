package com.rongji.dfish.misc.rjd;

import com.rongji.dfish.base.util.JsonUtil;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class RJDTest {
    public static void main(String[] args) throws IOException {
        RJDWriter rjdw = new RJDWriter(new FileOutputStream("d:/myfile.rjd"), "ThisIsPassword");
        Map<String,String> map=new HashMap<>();
        for(int i=0;i<1000;i++){
            map.put("属性名"+i,"测试值"+i);
        }
        String fileContent=JsonUtil.toJson(map);
        rjdw.write("myfolder/data/D01.json", fileContent);
        rjdw.write("myfolder/data/D02.json", "{\"prop\":\"test value\",\"属性2\":\"测试值\",\"属性3\":\"测试值3\"}");
        rjdw.close();



        //一般文件，用InputStream返回
        RJDCallback fileCallback=(path,  inputStream) -> {
            System.out.println("=== file " + path + " ===");
            //FIXME 自行读取 InputStream
            System.out.println(inputStream);
        };
        //文本文件可以直接返回String
        RJDStringCallback stringFileCallback=(path,  str) -> {
            System.out.println("=== file " + path + " ===");
            System.out.println(str);
        };

        RJDReader rjdr2 = new RJDReader(new FileInputStream("d:/myfile.rjd"), "ThisIsPassword");
        rjdr2.registerCallback("*D02*",fileCallback);
        rjdr2.registerCallback("*.json",stringFileCallback);
        rjdr2.doRead();

        //如果全部都用一种方式读取，可以简化为以下写法。
//        RJDReader rjdr = new RJDReader(new FileInputStream("d:/myfile.rjd"), "ThisIsPassword");
//        rjdr.readStringEntries((path,  str) -> {
//                System.out.println("=== file " + path + " ===");
//                System.out.println(str);
//        });

        System.out.println( RJDReader.match("myfolder/data/D01.json","*.json"));
        System.out.println( RJDReader.match("myfolder/data/D01.json","*"));
        System.out.println( RJDReader.match("myfolder/data/D01.json","*D01*"));
        System.out.println( RJDReader.match("myfolder/data/D01.json","*/data/*.json"));
        System.out.println( RJDReader.match("myfolder/data/D01.json","*.js"));
        System.out.println( RJDReader.match("myfolder/data/D01.json","*D02*"));
        System.out.println( RJDReader.match("myfolder/data/D01.json","*data/*.js"));
    }
}
