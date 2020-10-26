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


        RJDReader rjdr = new RJDReader(new FileInputStream("d:/myfile.rjd"), "ThisIsPassword");
        rjdr.readStringEntries((path,  str) -> {
                System.out.println("=== file " + path + " ===");
                System.out.println(str);
        });

    }
}
