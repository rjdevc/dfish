package com.rongji.dfish.framework.plugin.code;

public class CheckCodeGeneratorTest {
    public static void main (String[] args){
        CheckCodeGenerator ccg=new CheckCodeGenerator();
        boolean b=ccg.checkCodeEquals("1KAN","ikan");
        System.out.print(b);
    }
}
