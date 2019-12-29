package com.rongji.dfish.framework.plugin.code;

public class CaptchaGeneratorTest {
    public static void main (String[] args){
        CaptchaGenerator ccg=new CaptchaGenerator();
        boolean b=ccg.checkCodeEquals("1KAN","ikan");
        System.out.print(b);
    }
}
