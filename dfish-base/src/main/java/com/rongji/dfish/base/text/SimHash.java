package com.rongji.dfish.base.text;
import java.math.BigInteger;

/**
 * SimHash 用于判断两个文章的相似程度
 * <p> 我们把一个长的文本。计算SimHash后将会得到一个 短的hash值。 我们通过 getDist(hash1,hash2)
 * 来判断两个文章相似程度。这样性能将会大大提高。一般来说distance 为32或以上的时候，表名两个文章基本上没有相似度。
 * 我们通常使用3-5来标识比较接近的文章。
 * 因为和语义有关，如果有比较准确的切词器。这个SimHash将会更加准确。注意换切词器通常意味着SimHash的值变化了。</p>
 * SimHash simHash=new SimHash();
 * String hash =simHash.getSimHash();
 * //获得hash
 * int dis=SimHash.getDistance(hash1,hash2)
 * //判定差别大小。
 * //hash1和hash2 要同一个SimHash 计算方法产生的。换了切词器则无法比较。
 */
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
    private static final int[] HEX_DE_INT = { // 用于加速解密的cache
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, // 0
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, // 16
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, // 32
            0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 0, 0, 0, 0, 0, 0, // 48
            0, 10, 11, 12, 13, 14, 15, 0, 0, 0, 0, 0, 0, 0, 0, 0, //64
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, // 80
            0, 10, 11, 12, 13, 14, 15, 0, 0, 0, 0, 0, 0, 0, 0, 0 // 96
    };

    public String getSimHash(String tokens, int hashbits) throws Exception {
        // 定义特征向量/数组
        int[] v = new int[hashbits];
        for(String word:splitter.split(tokens)){
            // 注意停用词会被干掉
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
                int v1=HEX_DE_INT[hash1.charAt(i)];
                int v2=HEX_DE_INT[hash2.charAt(i)];
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

}