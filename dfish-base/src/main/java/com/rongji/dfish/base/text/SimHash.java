package com.rongji.dfish.base.text;
import java.math.BigInteger;


public class SimHash {
    private static final WordSplitter DEFAULT_SPLITER=SimpleWordSplitter.getInstance();
    public WordSplitter getSplitter() {
        return splitter;
    }

    public void setSplitter(WordSplitter splitter) {
        this.splitter = splitter;
    }

    private WordSplitter splitter;

    public static int hashbits = 64;

    public String getSimHash(String tokens) throws Exception {
        splitter=DEFAULT_SPLITER;
        return getSimHash(tokens, hashbits);
    }
    private static final char[] HEX_CHARS=new char[]{
            '0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'
    };
    public String getSimHash(String tokens, int hashbits) throws Exception {
        // 定义特征向量/数组
        int[] v = new int[hashbits];
        // 英文分词
        // StringTokenizer stringTokens = new StringTokenizer(this.tokens);
        // while (stringTokens.hasMoreTokens()) {
        // String temp = stringTokens.nextToken();
        // }
        // 1、中文分词，分词器采用 IKAnalyzer3.2.8 ，仅供演示使用，新版 API 已变化。

        for(String word:splitter.split(tokens)){
            // 注意停用词会被干掉
            // System.out.println(word);
            // 2、将每一个分词hash为一组固定长度的数列.比如 64bit 的一个整数.
            BigInteger t = hash(word, hashbits);
            for (int i = 0; i < hashbits; i++) {
                BigInteger bitmask = new BigInteger("1").shiftLeft(i);
                // 3、建立一个长度为64的整数数组(假设要生成64位的数字指纹,也可以是其它数字),
                // 对每一个分词hash后的数列进行判断,如果是1000...1,那么数组的第一位和末尾一位加1,
                // 中间的62位减一,也就是说,逢1加1,逢0减1.一直到把所有的分词hash数列全部判断完毕.
                if (t.and(bitmask).signum() != 0) {
                    // 这里是计算整个文档的所有特征的向量和
                    // 这里实际使用中需要 +- 权重，比如词频，而不是简单的 +1/-1，
                    v[i] += 1;
                } else {
                    v[i] -= 1;
                }
            }
        }

//		BigInteger fingerprint = new BigInteger("0");
        StringBuilder simHashBuffer = new StringBuilder(16);
        for (int i = 0; i < hashbits; i+=4) {
            // 4、最后对数组进行判断,大于0的记为1,小于等于0的记为0,得到一个 64bit 的数字指纹/签名.
            int index=0;
            if(v[i]>=0){
                index+=8;
            }
            if(v[i+1]>=0){
                index+=4;
            }
            if(v[i+2]>=0){
                index+=2;
            }
            if(v[i+3]>=0){
                index+=1;
            }
            simHashBuffer.append(HEX_CHARS[index]);
        }
        return simHashBuffer.toString();
    }

    private static BigInteger hash(String source, int hashbits) {
        if (source == null || source.length() == 0) {
            return new BigInteger("0");
        } else {
            char[] sourceArray = source.toCharArray();
            BigInteger x = BigInteger.valueOf(((long) sourceArray[0]) << 7);
            BigInteger m = new BigInteger("1000003");
            BigInteger mask = new BigInteger("2").pow(hashbits).subtract(new BigInteger("1"));
            for (char item : sourceArray) {
                BigInteger temp = BigInteger.valueOf((long) item);
                x = x.multiply(m).xor(temp).and(mask);
            }
            x = x.xor(new BigInteger(String.valueOf(source.length())));
            if (x.equals(new BigInteger("-1"))) {
                x = new BigInteger("-2");
            }
            return x;
        }
    }

    public static int getDistance(String hash1, String hash2) {
        int distance;
        if (hash1.length() != hash2.length()) {
            distance = -1;
        } else {
            distance = 0;
            for (int i = 0; i < hash1.length(); i++) {
                int v1=getHexValue(hash1.charAt(i));
                int v2=getHexValue(hash2.charAt(i));
                int dis=v1 ^ v2;
                switch(dis){
                    case 0:
                        break;
                    case 1:
                    case 2:
                    case 4:
                    case 8:
                        distance++;
                        break;
                    case 3:
                    case 6:
                    case 12:
                    case 5:
                    case 9:
                    case 10:
                        distance+=2;
                        break;
                    case 7:
                    case 11:
                    case 13:
                    case 14:
                        distance+=3;
                        break;
                    case 15:
                        distance+=4;
                        break;
                }
            }
        }
        return distance;
    }

    private static int getHexValue(char c) {
        if(c<='9'){
            return c-'0';
        }
        if(c<='F'){
            return c-'A'+10;
        }
        if(c<='f'){
            return c-'a'+10;
        }
        return 0;
    }

}