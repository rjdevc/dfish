package com.rongji.dfish.misc.senswords;

public class SenswordsTest {
    public static void main(String[] args) {
//		System.out.println(SensitiveTrieTree.isStopChar('，'));

        SensitiveWordFilter.getInstance();
        String s = "avjava你是逗比吗？fuck，avFuCk！ｆｕｃｋ全角半角，口，交换，f!!!u&c ###k 停顿词ff fuuuucccckkk 重复词，法@#轮！@#功over16口交换机";
//		String s="java,路口交通，8口交换机";
        System.out.println("原语句：" + s);
        System.out.println("长度：" + s.length());
        String re = null;
        long nano = System.nanoTime();
        boolean b = SensitiveWordFilter.getInstance().match(s);
        boolean w = true;
        while (w) {
            re = SensitiveWordFilter.getInstance().replace(s);
        }
        nano = (System.nanoTime() - nano);
        System.out.println("是否包含: " + b);
        System.out.println("解析时间 : " + nano + "ns");
        System.out.println("解析时间 : " + nano / 1000000 + "ms");
        System.out.println(re);
        System.out.println(re.length() == s.length());

        String s1=SensitiveWordFilter.getInstance()
                .replace("棉裤\uD83D\uDE02\uD83D\uDE03\uD83D\uDE04");
        System.out.println(s1);
    }
}
