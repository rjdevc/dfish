package com.rongji.dfish.misc.senswords;

public class SenswordsTest {
    public static void main(String[] args){
        String s=SensitiveWordFilter.getInstance()
                .replace("棉裤\uD83D\uDE02\uD83D\uDE03\uD83D\uDE04");
        String m="\uD83D\uDE02\uD83D\uDE03\uD83D\uDE04";
        System.out.println(s);
    }
}
