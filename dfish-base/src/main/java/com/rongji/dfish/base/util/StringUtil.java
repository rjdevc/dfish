package com.rongji.dfish.base.util;

import com.rongji.dfish.base.text.TrieTree;

import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;

/**
 * 字符串的一些通用方法。
 *
 * @author DFish Team
 */
@SuppressWarnings("unchecked")
public class StringUtil {

    /**
     * 对比汉字字符串 拼音顺序
     *
     * @param s1 String
     * @param s2 String
     * @return int
     */
    public static int chineseCompare(String s1, String s2) {
        return CHINESE_ORDER.compare(s1, s2);
    }

    /**
     * 排序时 不区分大小写的英文对比器
     */
    public static final Comparator<String> CASE_INSENSITIVE_ORDER = String.CASE_INSENSITIVE_ORDER;
    /**
     * 排序时候 中文对比器java.text.Collator
     *
     *  java.text.Collator的中文排序不是很准确
     */
    public static final Comparator<Object> CHINESE_ORDER_JDK = java.text.Collator.getInstance(java.util.Locale.SIMPLIFIED_CHINESE);


    /**
     * 字符串的排序器，和{@link #CHINESE_ORDER_JDK}不同。
     * 该排序方式对全角半角不影响排序结果。对英文字符大小写不敏感。
     * 并且和windows一样，会对数字进行分析。
     * 新建文本文档 (9).txt 和 新建文本文档 (10).txt 会以自然语言的方式进行排序。
     * 并且一般来说，符号会在文本的前面，而不会像ASCII那样分散在字符中间。
     * 字库合并了简体与繁体中文。
     */
    public static java.util.Comparator<Object> CHINESE_ORDER = null;

    public static final char CHAR_COMMA =',';
    public static final String COMMA=",";

    /**
     * 截取字符串content前面size个字节的内容 汉字相当于2个英文,数字相当于1个英文，每个英文字母占一个字节空间 要保证汉字不被错误分割 by
     * YeJL 2006-08
     *
     * @param content String 将要截取的源字符串
     * @param size    int 截取字符数
     * @return String
     */
    @Deprecated
    public static String shortenString(String content, int size) {
        return shortenString(content, size, 2, "...");
    }

    /**
     * 截取字符串content前面size个字节的内容 汉字相当于chineseCharSize个英文,数字相当于1个英文，每个英文字母占一个字节空间
     * 要保证汉字不被错误分割
     *
     * @param content         String 将要截取的源字符串
     * @param limitSize       int 截取字符数
     * @param chineseCharSize 汉字相当于chineseCharSize个字节，一般是GBK为2，IE显示也占2个空间。 UTF8下显示空间还是2
     *                        但字节数为3， GB18030下字节数为4
     * @param replacePostfix  被截取的字符用该字符代替
     * @return String
     * @deprecated 该方法，不能精确的表达字符的大小，比如说UTF8中，大部分非ASCII码字符是3个字节，但是部分如é是两个字节，根据Unicode 6.1规范，一些非常用汉字或日韩文可能是4个字节。所以有可能出现异常。
     * 而在GB18030中，也并非所有汉字都是4个字节，大部分汉字还是2个字节，只有冷僻汉字才是4个字节。建议使用shortenStringUTF8 或shortenStringGB
     */
    public static String shortenString(String content, int limitSize, int chineseCharSize,
                                       String replacePostfix) {
        // TODO 英文单词，如果可能最好也不要被分割。 by YeJL 2006-08
        if (content == null || "".equals(content) || limitSize == 0) {
            return "";
        }
        if (content.getBytes().length <= limitSize) {
            return content;
        }

        long l = 0;
        StringBuilder sb = new StringBuilder();
        char[] c = content.toCharArray();
        for (char ca:c) {
            sb.append(ca);
            if (ca > 255) {
                l += chineseCharSize;
            } else {
                l += 1;
            }
            if (l > (limitSize - replacePostfix.length())) {
                sb.deleteCharAt(sb.length() - 1).append(replacePostfix);
                break;
            }
        }
        return sb.toString();
    }

    /**
     * 截取字符串content前面size个字节的内容 数字相当于1个英文，每个英文字母占一个字节空间
     * 要保证多字节字符不被错误分割，因为UTF8是变长字符集，所以不能强制设定非ASCII码字符都是3个字节。部分字符应该2个字节。
     * <table summary="字符截取说明">
     * <tr><td>0XXX XXXX</td><td>ASCII</td></tr>
     * <tr><td><p>110X XXXX</p><p>10XX XXXX</p></td><td>2字节，如全角等字符</td></tr>
     * <tr><td><p>1110 XXXX</p><p>10XX XXXX</p><p>10XX XXXX</p></td><td>3字节，汉字主要集中在这个区域</td></tr>
     * <tr><td><p>1111 0XXX</p><p>10XX XXXX</p><p>10XX XXXX</p><p>10XX XXXX</p></td><td>4字节，UNICODE6.1定义范围</td></tr>
     * <tr><td><p>1111 10XX</p><p>10XX XXXX</p><p>10XX XXXX</p><p>10XX XXXX</p><p>10XX XXXX</p></td><td>5字节，预留</td></tr>
     * <tr><td><p>1111 110X</p><p>10XX XXXX</p><p>10XX XXXX</p><p>10XX XXXX</p><p>10XX XXXX</p><p>10XX XXXX</p></td><td>6字节，预留</td></tr>
     * </table>
     * 本方法内计算UTF8的字节长按照规范来。就是说可能出现1-6个字节。虽然4-6个字节的字符原则上现在还没有。
     *
     * @param content 内容
     * @param limitSize 限制大小
     * @param replacePostfix 替换后缀
     * @return 截取结果
     */
    public static String shortenStringUTF8(String content, int limitSize, String replacePostfix) {
        if (content == null || "".equals(content) || limitSize <= 0) {
            return "";
        }
        byte[] ba;
        try {
            ba = content.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return "";
        }
        if (ba.length <= limitSize) {
            return content;
        }

        int ccur = 0;
        if (replacePostfix == null) {
            replacePostfix = "";
        }
        int postfixLeng = replacePostfix.length();
        try {
            postfixLeng = replacePostfix.getBytes("UTF-8").length;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        StringBuilder sb = new StringBuilder();
        char[] c = content.toCharArray();
        for (int i = 0; i < ba.length; ) {
            byte b = ba[i];
            if (b > 0) {
                i++;
            } else if ((b & 0xE0) == 0xC0) {
                i += 2;
            } else if ((b & 0xF0) == 0xE0) {
                i += 3;
            } else if ((b & 0xF8) == 0xF0) {
                i += 4;
            } else if ((b & 0xFC) == 0xF8) {
                i += 5;
            } else if ((b & 0xFE) == 0xFC) {
                i += 6;
            } else {
                throw new RuntimeException("文本可能包含UTF8不可识别的内容 " + content);
            }
            if (i + postfixLeng > limitSize) {
                sb.append(replacePostfix);
                break;
            } else {
                sb.append(c[ccur++]);
            }
        }
        return sb.toString();
    }

    /**
     * 截取字符串content前面size个字节的内容，数字相当于1个英文，每个英文字母占一个字节空间
     * 要保证多字节字符不被错误分割，因为GB18030是变长字符集(主体是GBK 中文2个字节)，所以不能强制设定非ASCII码字符都是2个字节。部分字符应该4个字节。
     * <table summary="字符截取说明">
     * <tr><td>0x00-0x1F</td><td>1字节 控制字符</td></tr>
     * <tr><td>0x20-0x7F</td><td>1字节 ASCII</td></tr>
     * <tr><td><p>首0x81-0xFE</p><p>次0x40-0xFE</p></td><td>2字节，大部分常用汉字</td></tr>
     * <tr><td><p>首0x81-0xFE</p><p>次0x30-0x39</p><p>三0x81-0xFE</p><p>末0x30-0x39</p></td><td>4字节，增补，主要为冷僻汉字</td></tr>
     * </table>
     * 本方法适用于GBK GB2312 以及GB18030的字符集
     *
     * @param content 内容
     * @param limitSize 限制大小
     * @param replacePostfix 替换后缀
     * @return 截取结果
     */
    public static String shortenStringGB(String content, int limitSize, String replacePostfix) {
        if (content == null || "".equals(content) || limitSize <= 0) {
            return "";
        }
        String curEncode = "GB18030";
        byte[] ba = null;
        try {
            ba = content.getBytes("GB18030");
        } catch (UnsupportedEncodingException e) {
            try {
                ba = content.getBytes(curEncode = "GBK");
            } catch (UnsupportedEncodingException e2) {
                try {
                    ba = content.getBytes(curEncode = "gb2312");
                } catch (UnsupportedEncodingException e3) {
                    e.printStackTrace();
                    return "";
                }
            }
        }
        if (ba.length <= limitSize) {
            return content;
        }

        int ccur = 0;
        if (replacePostfix == null) {
            replacePostfix = "";
        }
        int postfixLeng = replacePostfix.length();
        try {
            postfixLeng = replacePostfix.getBytes("UTF-8").length;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        StringBuilder sb = new StringBuilder();
        char[] c = content.toCharArray();
        for (int i = 0; i < ba.length; i++) {
            byte b = ba[i];
            if (b > 0) {
            } else {
                if (++i >= ba.length) {
                    throw new RuntimeException("文本可能包含" + curEncode + "不可识别的内容 " + content);
                }
                byte next = ba[i];
                if (next >= (byte) 0x30 && next < (byte) 0x40) {
                    i += 2;
                }
            }
            if (i + postfixLeng >= limitSize) {
                sb.append(replacePostfix);
                break;
            } else {
                sb.append(c[ccur++]);
            }
        }
        return sb.toString();
    }

    private static Random r = new Random(20080815L + System.currentTimeMillis());

    /**
     * 生成一个指定长度的随机字符串
     *
     * @param length         int 指定长度
     * @param containNum     boolean 是否包含数字
     * @param containLowcase boolean 是否小写数字
     * @param containUppcase boolean 是否包含大写字母
     * @param containSymbol  boolean 是否包含符号
     * @return String
     */
    public static String getRadomString(int length, boolean containNum, boolean containLowcase,
                                        boolean containUppcase, boolean containSymbol) {
        if (!containNum && !containLowcase && !containUppcase && !containSymbol) {
            return "";
        }
        StringBuilder sb = new StringBuilder(length);
        if (0 < length) {
            while (sb.length() < length) {
                byte[] can = new byte[length * 2];
                r.nextBytes(can);
                for (byte b:can) {
                    if ((containNum && b >= '0' && b <= '9')
                            || (containLowcase && b >= 'a' && b <= 'z')
                            || (containUppcase && b >= 'A' && b <= 'Z')
                            || (containSymbol &&
                            ((b >= '!' && b <= '/')
                                    || (b >= ':' && b <= '@')
                                    || (b >= '[' && b <= '`')
                                    || (b >= '{' && b <= '~'))
                    )) {
                        sb.append((char) b);
                        if (sb.length() >= length) {
                            break;
                        }
                    }
                }
            }
            return sb.toString();
        }
        return "";
    }

    /**
     * 判断两个字符串是否equals
     * 空字符串也可以对比。另外把 null和""当成是一样的。
     *
     * @param str1
     * @param str2
     * @return
     */
    public static boolean nullAbleEquals(String str1, String str2) {
        if (str1 == null || "".equals(str1)) {
            return (str1 == null || "".equals(str1));
        } else {
            return str1.equals(str2);
        }
    }

    /**
     * 将数值金额人民币转化为中文意思
     *
     * @param dblNum
     * @return
     */
    public static String RMBtoCHINESE(String dblNum) {
        try {
            if ("".equals(dblNum)) {
                return null;
            }
            String sNum = "零壹贰叁肆伍陆柒捌玖负";
            String sUni = "整分角元拾佰仟万拾佰仟亿拾佰仟万拾佰仟亿拾佰仟万拾佰仟";
            int L = dblNum.length();
            if (dblNum.lastIndexOf(".") == -1) {//整数转化标准人民币格式
                dblNum = dblNum + ".00";
            } else {
                if (dblNum.lastIndexOf(".") + 1 == L) {//补齐标准人民币格式
                    dblNum = dblNum + "00";
                }
                if (dblNum.lastIndexOf(".") + 2 == L) {//补齐标准人民币格式
                    dblNum = dblNum + "0";
                }
            }
            L = dblNum.length();
            if (L > 27) {
                return "数值溢出"; // 包括小数点和2位小数
            }
            StringBuilder sN = new StringBuilder(); // 转换前的字符串
            sN.append(dblNum);
            sN.deleteCharAt(L - 3); // 删除小数点
            --L;
            StringBuilder sT = new StringBuilder(); // 转换后的字符串
            if (sN.charAt(L - 1) == '0' && sN.charAt(L - 2) == '0') {
                sT.insert(0, '整'); // 分位为零则尾部加'整'
            }
            boolean Ziro = false, a = false, b = false;
            int n = 0;
            for (int i = 1; i <= L; i = i + 1) {
                n = sN.charAt(L - i) - '0'; // 数值
                if (i == 7 || i == 15) {
                    b = n == 0; // 万位、万亿位是零
                }
                if ((i > 7) & (i < 11) || (i > 15) & (i < 19)) {
                    b = (n == 0) & b; // 万-亿位、或亿-亿位之间全是零
                }
                a = (i == 1 || i == 3 || i == 7 || i == 11 || i == 15 || i == 19 || i == 23); // 万亿、亿、万、元、分位为0时不写'零'
                if (n > 0 || (i > 1 & a)) {
                    if ((i == 11 || i == 19) & b) {
                        sT.setCharAt(0, '亿'); // 用'亿'覆盖'万'
                    } else {
                        sT.insert(0, sUni.charAt(i)); // 插入单位
                    }
                }
                if (!(n == 0 & (Ziro || a))) {
                    sT.insert(0, sNum.charAt(n)); // 插入数值
                }
                Ziro = n == 0;
            }
            if (n == 0) {
                sT.insert(0, '零');
            }
            return sT.toString();
        } catch (Exception e) {
            return "格式不正确";
        }
    }

    /**
     * 替换EL表达式的内容
     * 表达式中的内容将通过getter 转成需要的内容
     *
     * @param elStr
     * @param getter
     * @return
     */
    public static String relaceELTag(String elStr, ELcontentGetter getter) {
        if (elStr == null) {
            return elStr;
        }
        char[] cs = elStr.toCharArray();
        //找到所有的${...}然后把内容用getter替换
        List<Integer[]> matchs = new ArrayList<>();
        int start = -1;
        int end = -1;
        for (int i = 0; i < cs.length; i++) {
            if (start < 0) {
                if (cs[i] == '$') {
                    if (i + 1 < cs.length) {
                        if (cs[i + 1] == '{') {
                            start = i;
                        }
                    }
                }
            } else {
                if (cs[i] == '}') {
                    end = i;
                    matchs.add(new Integer[]{start, end});
                    start = -1;
                    end = -1;
                }
            }
        }
        start = 0;
        end = elStr.length();

        StringBuilder sb = new StringBuilder();
        for (Integer[] match : matchs) {
            sb.append(cs, start, match[0] - start);
            sb.append(getter.getCotent(elStr.substring(match[0] + 2, match[1])));
            start = match[1] + 1;
        }
        sb.append(cs, start, end - start);
        return sb.toString();
    }

    /**
     * 如果内容里面有表达式 如 ${xxx}
     * 解析过程当中，会把xxx内容当从参数提供给这个接口回调。
     * 接口返回实际内容，则可完成解析。
     */
    public interface ELcontentGetter {
        /**
         * 将el里面的tag转化成实际内容
         *
         * @param tag 标记
         * @return String
         */
        String getCotent(String tag);
    }

    /**
     * 日期格式化模板
     */
    private static Map<String, Format> datePatterns = new HashMap<>();
    /**
     * 数值格式化模板
     */
    private static Map<String, Format> numberPatterns = new HashMap<>();

    /**
     * 将数据格式化成字符(目前仅提供日期和数值的格式化)
     *
     * @param data    数据对象
     * @param pattern 格式样式
     * @return
     */
    public static String format(Object data, String pattern) {
        if (data == null) {
            return "";
        }
        if (Utils.isEmpty(pattern)) {
            if (data instanceof String) {
                return (String) data;
            } else {
                return String.valueOf(data);
                // 这里是否返回data.toString();
//				throw new IllegalArgumentException(data.getClass().getName() + " can not be formatted by the empty pattern." );
            }
        }
        Format f = null;
        if (data instanceof Date) {
            f = datePatterns.get(pattern);
            if (f == null) {
                f = new SimpleDateFormat(pattern);
                datePatterns.put(pattern, f);
            }
        } else if (data instanceof Number) {
            f = numberPatterns.get(pattern);
            if (f == null) {
                f = new DecimalFormat(pattern);
                numberPatterns.put(pattern, f);
            }
        } else {
            return String.valueOf(data);
//			throw new IllegalArgumentException(data.getClass().getName() + " can not be formatted by the pattern(" + pattern + ")." );
        }
        try {
            synchronized (f) {
                return f.format(data);
            }
        } catch (Exception ex) {
            LogUtil.error(null, ex);
            return String.valueOf(data);
        }

    }

    /**
     * 获取高亮的字符串，默认高亮的关键字以&lt;em&gt;&lt;/em&gt;标签高亮
     *
     * @param source 源字符串
     * @param key    高亮的关键字
     * @return String 高亮的字符串
     */
    public static String highlight(String source, String key) {
        return highlight(source, key, new HighlightParam());
    }

    /**
     * 获取高亮的字符串
     *
     * @param source 源字符串
     * @param key    高亮的关键字
     * @param param  高亮参数
     * @return String 高亮的字符串
     */
    public static String highlight(String source, String key, HighlightParam param) {
        if (source == null || "".equals(source) || key == null || "".equals(key)) {
            return source;
        }
        boolean[] matchs = new boolean[source.length()];
        //找到每个词在字符串中的起始位置，将相应数组位置变为true
        int index = 0;
        for (String word : splitKeyword(key, param.getMinMatch())) {
            while ((index = source.indexOf(word, index)) != -1) {
                for (int i = index; i < index + word.length(); i++) {
                    matchs[i] = true;
                }
                index++;
            }
        }
        StringBuilder lightStr = new StringBuilder();
        char[] srcarr = source.toCharArray();
        boolean isInsert = false;
        for (int i = 0; i < srcarr.length; i++) {
            if (isInsert) {
                if (!matchs[i]) {
                    lightStr.append(param.getPostfix());
                    isInsert = false;
                }
            } else {
                if (matchs[i]) {
                    lightStr.append(param.getPrefix());
                    isInsert = true;
                }
            }
            lightStr.append(srcarr[i]);
        }
        if (isInsert) {
            lightStr.append(param.postfix);
        }
        return lightStr.toString();
    }

    /**
     * 将高亮的关键字拆分
     *
     * @param keyWord     关键字
     * @param matchLength 关键字拆分长度
     * @return List&lt;String&gt;
     */
    private static List<String> splitKeyword(String keyWord, int matchLength) {
        if (Utils.isEmpty(keyWord)) {
            return Collections.emptyList();
        }
        List<String> splitWords = new ArrayList<>();
        if (matchLength <= 0) { // 全字匹配
            splitWords.add(keyWord);
        } else {
            // 按照给出的长度值进行匹配
            for (int i = 0; i < keyWord.length() - matchLength + 1; i++) {
                String word = keyWord.substring(i, i + matchLength);
                splitWords.add(word);
            }
        }
        return splitWords;
    }

    /**
     * 关键字高亮参数
     *
     * @author DFish Team
     */
    public static class HighlightParam {
        /**
         * 高亮标签前缀
         */
        private String prefix = "<em>";
        /**
         * 高亮标签后缀
         */
        private String postfix = "</em>";
        /**
         * 高亮关键字匹配长度;&lt;=0:全字匹配,其他情况按照长度拆分关键字再进行匹配
         */
        private int minMatch;

        /**
         * 获得高亮标签前缀
         *
         * @return String
         */
        public String getPrefix() {
            return prefix;
        }

        /**
         * 设置高亮标签前缀
         *
         * @param prefix
         */
        public void setPrefix(String prefix) {
            this.prefix = prefix;
        }

        /**
         * 获得高亮标签后缀
         *
         * @return String
         */
        public String getPostfix() {
            return postfix;
        }

        /**
         * 设置高亮标签后缀
         *
         * @param postfix
         */
        public void setPostfix(String postfix) {
            this.postfix = postfix;
        }

        /**
         * 获得高亮关键字匹配长度;&lt;=0:全字匹配,其他情况按照长度拆分关键字再进行匹配
         *
         * @return int
         */
        public int getMinMatch() {
            return minMatch;
        }

        /**
         * 设置高亮关键字匹配长度;&lt;=0:全字匹配,其他情况按照长度拆分关键字再进行匹配
         *
         * @param minMatch
         */
        public void setMinMatch(int minMatch) {
            this.minMatch = minMatch;
        }

    }

    /**
     * 和EXCEL 类似 第0列为A 第25列为Z 第26列为AA 第27列为AB 第51列为AZ 第52列为BA .. 第(27*26-1)列为ZZ 第27*26列为AAA 第27*27*26列为AAAA
     *
     * @param size
     * @return String
     */
    public static String toExcelLabel(int size) {
        int x = size;
        StringBuilder sb = new StringBuilder();
        do {
            sb.append((char) ('A' + x % 26));//26个大写字符
        } while ((x = x / 26 - 1) >= 0);
        return sb.reverse().toString();
    }

    /**
     * 和EXCEL 类似 第0列为A 第25列为Z 第26列为AA 第27列为AB 第51列为AZ 第52列为BA .. 第(27*26-1)列为ZZ 第27*26列为AAA 第27*27*26列为AAAA
     *
     * @param label
     * @return
     * @throws ParseException
     */
    public static int fromExcelLabel(String label) throws ParseException {
        if (label == null || "".equals(label)) {
            return 0;
        }
        char[] cs = label.toCharArray();
        int ret = 0;
        int index = 0;
        for (char c : cs) {
            ret = ret * 26;
            int cv = c - 64;
            if (cv >= 32) {
                cv -= 32;
            }
            if (cv < 1 || cv > 26) {
                throw new java.text.ParseException(label, index);
            }
            ret += cv;
            index++;
        }
        return ret - 1;
    }

    /**
     * 字符集编码：UTF-8
     */
    public static final String ENCODING_UTF8 = "UTF-8";
    /**
     * 字符集编码:GBK
     */
    public static final String ENCODING_GBK = "GBK";
    //根据传进来 byte[]

    /**
     * 根据传进来 byte[] 判定字符集。
     * 如果传进来byte[] 能够吻合UTF8的格式。默认返回UTF8的格式。
     * 如果传进来的byte[] 吻合GBK(GB18030)的格式。返回GBK
     * 如果都不满足，UnsupportedEncodingException
     * 如果byte是纯ASCII码。无法判定字符集，但都吻合UTF8/GBK的格式。则返回UTF8
     *
     * @param sample byte[]
     * @return UTF8/GBK
     */
    public static String detCharset(byte[] sample) throws UnsupportedEncodingException {
        if (sample == null || sample.length == 0) {
            return ENCODING_UTF8;
        }
        if (checkUTF8(sample)) {
            return ENCODING_UTF8;
        }
        if (checkGBK(sample)) {
            return ENCODING_GBK;
        }
        throw new UnsupportedEncodingException("unknown");
    }

    /**
     * UTF8是变长字符集，所以不能强制设定非ASCII码字符都是3个字节。部分字符应该2个字节。
     * <table>
     * <tr><td>0XXX XXXX</td><td>ASCII</td></tr>
     * <tr><td><p>110X XXXX</p><p>10XX XXXX</p></td><td>2字节，如全角等字符</td></tr>
     * <tr><td><p>1110XXXX</p><p>10XX XXXX</p><p>10XX XXXX</p></td><td>3字节，汉字主要集中在这个区域</td></tr>
     * <tr><td><p>1111 0XXX</p><p>10XX XXXX</p><p>10XX XXXX</p><p>10XX XXXX</p></td><td>4字节，UNICODE6.1定义范围</td></tr>
     * <tr><td><p>1111 10XX</p><p>10XX XXXX</p><p>10XX XXXX</p><p>10XX XXXX</p><p>10XX XXXX</p></td><td>5字节，预留</td></tr>
     * <tr><td><p>1111 110X</p><p>10XX XXXX</p><p>10XX XXXX</p><p>10XX XXXX</p><p>10XX XXXX</p><p>10XX XXXX</p></td><td>6字节，预留</td></tr>
     * </table>
     *
     * @param sample byte[]
     * @return boolean
     */
    private static boolean checkUTF8(byte[] sample) {
        for (int i = 0; i < sample.length; i++) {
            byte b = sample[i];
            if (b > 0) {
                continue;
            }
            //[-64, -32)110X XXXX 后续应该有 一个[-128,-64)10XX XXXX
            //[-32, -16)1110 XXXX 后续应该有 两个[-128,-64)10XX XXXX
            //[-16, -8)1111 0XXX 后续应该有 三个[-128,-64)10XX XXXX
            //根据unicode6.1暂时不判断更多
            if (b >= -32) {
                if (b >= -8) {
                    //1111 1XXX
                    return false;
                } else if (b >= -16) {
                    //三个[-128,-64) //1111 0XXX
                    if (noFollow10Char(sample, i, 3)) {
                        return false;
                    }
                    i += 3;
                } else {
                    //两个[-128,-64) 1110 XXXX
                    if (noFollow10Char(sample, i, 2)) {
                        return false;
                    }
                    i += 2;
                }
            } else if (b >= -64) {
                //一个[-128,-64) 110X XXXX
                if (noFollow10Char(sample, i, 1)) {
                    return false;
                }
                i++;
            } else { //10XX XXXX
                return false;
            }
        }
        return true;
    }

    private static String byte2Bin(byte b) {
        StringBuilder sb = new StringBuilder();
        for (int i = 7; i >= 0; i--) {
            sb.append((b & 1 << i) > 0 ? '1' : '0');
        }
        return sb.toString();
    }

    private static String byte2hex(byte b) {
        StringBuilder sb = new StringBuilder();
        sb.append(Integer.toHexString(b >>> 4 & 15));
        sb.append(Integer.toHexString(b & 15));
        return sb.toString();
    }


    //判定UTF8的第二个以后的字节码因为 是10XX XXXX 所以值 访问只能是[128,-64)
    private static boolean noFollow10Char(byte[] sample, int i, int j) {
        //因为sample是采样的，防止越界
        int end = Math.min(sample.length, i + j + 1);
        for (int k = i + 1; k < end; k++) {
            byte b = sample[k];
            if (b >= -64) {
                return true;
            }
        }
        return false;
    }

    /**
     * GB18030是变长字符集(主体是GBK 中文2个字节)，所以不能强制设定非ASCII码字符都是2个字节。部分字符应该4个字节。
     * <table>
     * <tr><td>0x00-0x1F</td><td>1字节 控制字符</td></tr>
     * <tr><td>0x20-0x7F</td><td>1字节 ASCII</td></tr>
     * <tr><td><p>首0x81-0xFE</p><p>次0x40-0xFE</p></td><td>2字节，大部分常用汉字</td></tr>
     * <tr><td><p>首0x81-0xFE</p><p>次0x30-0x39</p><p>三0x81-0xFE</p><p>末0x30-0x39</p></td><td>4字节，增补，主要为冷僻汉字</td></tr>
     * </table>
     *
     * @param sample
     * @return
     */
    private static boolean checkGBK(byte[] sample) {
        for (int i = 0; i < sample.length; i++) {
            byte b = sample[i];
//    		System.out.println("checking 1st char "+byte2hex(b));
            if (b > 0) {
                continue;
            }
            if (b == -1 || b == -128) {//首个字节不能是80 FF
                return false;
            }
            if (i + 1 >= sample.length) {
                break;
            }
            byte b2 = sample[i + 1];
//   			System.out.println("checking 2nd char "+byte2hex(b2));
            if (b2 >= -1 && b2 < 48) {
                return false;
            } else if (b2 >= 48 && b2 < 64) {//可能是负数
                //GB18030的判定
                if (i + 2 >= sample.length) {
                    break;
                }
                byte b3 = sample[i + 2];
//   				System.out.println("checking 3rd char "+byte2hex(b3));
                if (b3 >= -1 || b3 == -128) {
                    return false;
                }
                if (i + 3 >= sample.length) {
                    break;
                }
                byte b4 = sample[i + 3];
//   				System.out.println("checking 4rd char "+byte2hex(b4));
                if (b4 < 48 || b2 >= 64) {
                    return false;
                } else {
                    i += 3;
                }
            } else {
                i++;
            }
        }
        return true;
    }

    /**
     * 按提供的字符串分隔字符串(带分隔符转换).
     *
     * @param str
     * @param regex
     * @return
     */
    public static String[] split(String str, String regex) {
        String[] sa = null;
        if (str != null && regex != null && !"".equals(regex)) {
            sa = str.split(rex4Str(regex));
        }

        return sa;
    }

    /**
     * 正则转义字符和特殊字符串处理
     *
     * @param regex
     * @return
     */
    private static String rex4Str(String regex) {
        String rexc = "";

        if (".".equals(regex)) {
            rexc = "\\.";
        } else if ("^".equals(regex)) {
            rexc = "\\^";
        } else if ("$".equals(regex)) {
            rexc = "\\$";
        } else if ("*".equals(regex)) {
            rexc = "\\*";
        } else if ("+".equals(regex)) {
            rexc = "\\+";
        } else if ("|".equals(regex)) {
            rexc = "\\|";
        } else {
            rexc = regex;
        }

        return rexc;
    }


    private static final Pattern PATTERN_DECIMAL = Pattern.compile("\\d*");

    /**
     * 判断字符串是否仅有数字组成,null或""均是非数字
     *
     * @param str
     * @return
     * @deprecated 应该使用
     */
    public static boolean isNumber(String str) {
        if (Utils.isEmpty(str)) {
            return false;
        }
        return PATTERN_DECIMAL.matcher(str).matches();
    }

    /**
     * 把一个集合转化成可显示的字符串
     *
     * @param coll
     * @param split
     * @return
     * @deprecated
     * @see #join(Object[], String)
     */
    @Deprecated
    public static String toString(Collection coll, char split) {
        if (coll == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        boolean begin = true;
        for (Object o : coll) {
            if (o == null || "".equals(o)) {
                continue;
            }
            if (begin) {
                begin = false;
            } else {
                sb.append(split);
            }
            sb.append(o);
        }
        return sb.toString();
    }

    /**
     * 把一个集合转化成用逗号隔开的的字符串
     *
     * @param coll
     * @return
     * @deprecated
     * @see #join(Object[], String)
     */
    @Deprecated
    public static String toString(Collection coll) {
        return toString(coll, CHAR_COMMA);
    }

    /**
     * 把一个数组转化成可显示的字符串
     *
     * @param array
     * @return
     * @deprecated
     * @see #join(Object[], String)
     */
    @Deprecated
    public static <T> String toString(T[] array) {
        if (array == null) {
            return null;
        }
        return toString(array, CHAR_COMMA);
    }

    /**
     * 把一个数组转化成可显示的字符串
     *
     * @param array
     * @param split
     * @return
     * @deprecated
     * @see #join(Object[], String)
     */
    @Deprecated
    public static <T> String toString(T[] array, char split) {
        return toString(Arrays.asList(array), split);
    }

    /**
     * 将XSS内容移除。
     * 这个常量通常作为replace方法的一个常量
     *
     * @see #replace(String, Map)
     */
    public static final Map<String, String> XSS_REMOVE;

    /**
     * ；
     * 将XSS内容替换成空格。
     * 这个常量通常作为replace方法的一个常量
     *
     * @see #replace(String, Map)
     */
    public static final Map<String, String> XSS_TO_BLANK;
    /**
     * 将XSS内容替换成全角。
     * 这个常量通常作为replace方法的一个常量
     *
     * @see #replace(String, Map)
     */
    public static final Map<String, String> XSS_TO_SBC;

    static {
        // FIXME 这里先不让他报错,但是这个调用关系应该是有问题的
        String[] chineseOrderProviders = {"com.rongji.dfish.misc.chinese.ChineseOrder"};
        for (String prov : chineseOrderProviders) {
            try {
                Class c = Class.forName(prov);
                CHINESE_ORDER = (Comparator<Object>) c.newInstance();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        if (CHINESE_ORDER == null) {
            CHINESE_ORDER = java.text.Collator.getInstance(Locale.SIMPLIFIED_CHINESE);
        }
        String[] XSS_KEYS = {"|", "&", ";", "$", "%", "@", "'", "\"", "\\'", "\\\\\"", "<", ">", "(", ")", "+",
                "\r", "\n", ",", "\\"};
        HashMap<String, String> xssRemove = new HashMap<>();
        HashMap<String, String> xssToBlank = new HashMap<>();
        HashMap<String, String> xssToSbc = new HashMap<>();
        for (String x : XSS_KEYS) {
            xssRemove.put(x, "");
            xssToBlank.put(x, " ");
            char[] chs = x.toCharArray();
            char[] sbc = new char[chs.length];
            for (int i = 0; i < chs.length; i++) {
                sbc[i] = CharUtil.dbc2sbc(chs[i]);
            }
            xssToSbc.put(x, new String(sbc));
        }
        XSS_TO_BLANK = Collections.unmodifiableMap(xssToBlank);
        XSS_REMOVE = Collections.unmodifiableMap(xssRemove);
        XSS_TO_SBC = Collections.unmodifiableMap(xssToSbc);
//        CharUtil.dbc2sbcl()
    }

    private static Map<Map<String, String>, TrieTree<String>> tries = Collections.synchronizedMap(new HashMap<>());

    /**
     * 替换字符串，同时提换多个字符串，如果几个字符串有包含关系，更长的优先。
     * 该方法是从右向做匹配。
     *
     * @param src       原文
     * @param replaceTo 替换的文本。如果替换的内容比较多，尽量使用常量
     * @return 结果
     */
    public static String replace(String src, Map<String, String> replaceTo) {
        TrieTree<String> trie = tries.get(replaceTo);
        if (trie == null) {
            trie = new TrieTree<>(true);
            for (Map.Entry<String, String> entry : replaceTo.entrySet()) {
                trie.put(entry.getKey(), entry.getValue());
            }
            tries.put(replaceTo, trie);
        }
        List<TrieTree.SearchResult<String>> sr = trie.search(src);
        if (sr == null || sr.isEmpty()) {
            return src;
        }
        char[] chars = src.toCharArray();
        StringBuilder sb = new StringBuilder();
        int lastEnd = 0;
        for (TrieTree.SearchResult<String> item : sr) {
            if (item.getBegin() > lastEnd) {
                sb.append(chars, lastEnd, item.getBegin() - lastEnd);
            }
            sb.append(item.getValue());
            lastEnd = item.getEnd();
        }
        if (lastEnd < chars.length) {
            sb.append(chars, lastEnd, chars.length - lastEnd);
        }

        return sb.toString();
    }

    /**
     * 把一个数组转化成可显示的字符串
     * @param strs 数组
     * @return String
     */
    public static String join(Object[] strs){
        return join(Arrays.asList(strs),COMMA);
    }

    /**
     * 把一个数组转化成可显示的字符串
     *
     * @param strs 数组
     * @return String
     */
    public static String join(String... strs) {
        return join(strs, ',');
    }

    /**
     * 把一个数组转化成可显示的字符串
     * @param strs 数组
     * @param split 分隔符
     * @return String
     */
    public static String join(Object[] strs, char split){
        return join(Arrays.asList(strs),new String(new char[]{split}));
    }

    /**
     * 把一个数组转化成可显示的字符串
     * @param strs 数组
     * @param split 分隔符
     * @return String
     */
    public static String join(Object[] strs, String split){
        return join(Arrays.asList(strs),split);
    }

    /**
     * 把一个集合转化成可显示的字符串
     * @param strs 集合
     * @return String
     */
    public static String join(Collection strs){
        return join(strs,COMMA);
    }

    /**
     * 把一个集合转化成可显示的字符串
     * @param strs 集合
     * @param split 分隔符
     * @return String
     */
    public static String join(Collection strs, char split){
        return join(strs,new String(new char[]{split}));
    }

    /**
     * 把一个集合转化成可显示的字符串
     * @param strs 集合
     * @param split 分隔符
     * @return String
     */
    public static String join(Collection strs, String split){
        if (strs == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        boolean begin = true;
        for (Object o : strs) {
            if (o == null || "".equals(o)) {
                continue;
            }
            if (begin) {
                begin = false;
            } else {
                sb.append(split);
            }
            sb.append(o);
        }
        return sb.toString();
    }

    /**
     * 在左边加入字符串补齐字符串到指定长度
     * 如果字符串比指定的长度还要长，这里并不会截取
     * @param str
     * @param size
     * @param padStr
     * @return String
     */
    public static String leftPad(String str,int size, String padStr){
        if (str == null) {
            return null;
        } else {
            if (padStr == null || "".equals(padStr)) {
                padStr = " ";
            }

            int padLen = padStr.length();
            int strLen = str.length();
            int pads = size - strLen;
            if (pads <= 0) {
                return str;
            } else if (padLen == 1 && pads <= 8192) {
                return leftPad(str, size, padStr.charAt(0));
            } else if (pads == padLen) {
                return padStr.concat(str);
            } else if (pads < padLen) {
                return padStr.substring(0, pads).concat(str);
            } else {
                char[] padding = new char[pads];
                char[] padChars = padStr.toCharArray();

                for(int i = 0; i < pads; ++i) {
                    padding[i] = padChars[i % padLen];
                }

                return (new String(padding)).concat(str);
            }
        }
    }
    /**
     * 在左边加入字符串补齐字符串到指定长度
     * 如果字符串比指定的长度还要长，这里并不会截取
     * @param str
     * @param size
     * @param padChar
     * @return
     */
    public static String leftPad(String str,int size, char padChar){
        if (str == null) {
            return null;
        } else {
            int pads = size - str.length();
            if (pads <= 0) {
                return str;
            } else {
                char[] ret=new char[size];
                Arrays.fill(ret,0,pads,padChar);
                System.arraycopy(str.toCharArray(),0,ret,pads,str.length());
                return new String(ret);
            }
        }
    }

    /**
     * 在右边加入字符串补齐字符串到指定长度
     * 如果字符串比指定的长度还要长，这里并不会截取
     * @param str
     * @param size
     * @param padStr
     * @return
     */
    public static String rightPad(String str,int size, String padStr){
        if (str == null) {
            return null;
        } else {
            if (padStr == null || "".equals(padStr)) {
                padStr = " ";
            }

            int padLen = padStr.length();
            int strLen = str.length();
            int pads = size - strLen;
            if (pads <= 0) {
                return str;
            } else if (padLen == 1 && pads <= 8192) {
                return rightPad(str, size, padStr.charAt(0));
            } else if (pads == padLen) {
                return str.concat(padStr);
            } else if (pads < padLen) {
                return str.concat(padStr.substring(0, pads));
            } else {
                char[] padding = new char[pads];
                char[] padChars = padStr.toCharArray();

                for(int i = 0; i < pads; ++i) {
                    padding[i] = padChars[i % padLen];
                }

                return str.concat(new String(padding));
            }
        }
    }

    /**
     * 在右边加入字符串补齐字符串到指定长度
     * 如果字符串比指定的长度还要长，这里并不会截取
     * @param str
     * @param size
     * @param padChar
     * @return String
     */
    public static String rightPad(String str,int size, char padChar){
        if (str == null) {
            return null;
        } else {
            int pads = size - str.length();
            if (pads <= 0) {
                return str;
            } else {
                char[] ret=new char[size];
                System.arraycopy(str.toCharArray(),0,ret,0,str.length());
                Arrays.fill(ret,str.length(),pads,padChar);
                return new String(ret);
            }
        }
    }

    /**
     * 把两个用特定符号(常用的是逗号)隔开多个关键字的字符串。合并起来
     * 比如001,002  合并  002,003 得到 001,002,003
     * 集合运算中 UNION
     * @param separatedString1 String
     * @param separatedString2 String
     * @param separator 默认为 半角逗号(,)
     * @return String
     */
    public static String addAll(String separatedString1, String separatedString2, char separator){
        return addAll(separatedString1,separatedString2,separator,false);
    }

    /**
     * 把两个用特定符号(常用的是逗号)隔开多个关键字的字符串。合并起来
     * 比如001,002  合并  002,003 得到 001,002,003两个用特定符号(常用的是逗号)隔开多个关键字的字符串。合并起来
     * 集合运算中 UNION
     * @param separatedString1 String
     * @param separatedString2 String
     * @return String
     */
    public static String addAll(String separatedString1, String separatedString2){
        return addAll(separatedString1,separatedString2, CHAR_COMMA,false);
    }

    /**
     * 两个用特定符号(常用的是逗号)隔开多个关键字的字符串。从第一个里面移除第二个出现的字符串
     * 比如001,002  扣除  002,003 得到 003
     * @param separatedString1 String
     * @param separatedString2 String
     * @return String
     */
    public static String removeAll(String separatedString1, String separatedString2){
        return removeAll(separatedString1,separatedString2, CHAR_COMMA);
    }
    /**
     * 两个用特定符号(常用的是逗号)隔开多个关键字的字符串。从第一个里面保留第二个出现的字符串
     * 比如001,002  保留  002,003 得到 002
     * 集合运算中 INTERSECTION
     * @param separatedString1 String
     * @param separatedString2 String
     * @return String
     */
    public static String retainAll(String separatedString1, String separatedString2){
        return retainAll(separatedString1,separatedString2, CHAR_COMMA);
    }

    /**
     * 两个用特定符号(常用的是逗号)隔开多个关键字的字符串。如果第一个和第二个有相同元素则返回true
     * 比如001,002  是否与交集  002,003 得到 true
     * @param separatedString1 String
     * @param separatedString2 String
     * @return boolean
     */
    public static boolean containsAny(String separatedString1, String separatedString2){
        return containsAny(separatedString1,separatedString1, CHAR_COMMA);
    }

    /**
     * 个用特定符号(常用的是逗号)隔开多个关键字的字符串。如果第一个里包含第二个所有元素则返回true
     * @param separatedString1 String
     * @param separatedString2 String
     * @return boolean
     */
    public static boolean containsAll(String separatedString1, String separatedString2){
        return containsAll(separatedString1,separatedString1, CHAR_COMMA);
    }
    /**
     * 把两个用特定符号(常用的是逗号)隔开多个关键字的字符串。合并起来
     * 比如001,002  合并  002,003 得到 001,002,003
     * 集合运算中 UNION
     * 可以是设置允许重复，一般不常用，允许重复，直接字符串相加即可。
     * @param separatedString1 String
     * @param separatedString2 String
     * @param separator 默认为 半角逗号(,)
     * @param duplicate 是否可以重复
     * @return String
     */
    public static String addAll(String separatedString1, String separatedString2, char separator, boolean duplicate){
        if(duplicate){
            List<String> list = toList(separatedString1, separator);
            list.addAll(toList(separatedString2, separator));
            return join(list, separator);
        }else {
            Set<String> set = toSet(separatedString1, separator);
            set.addAll(toSet(separatedString2, separator));
            return join(set, separator);
        }
    }
    /**
     * 把两个用特定符号(常用的是逗号)隔开多个关键字的字符串。合并起来
     * 比如001,002  合并  002,003 得到 001,002,003两个用特定符号(常用的是逗号)隔开多个关键字的字符串。合并起来
     * 集合运算中 UNION
     * @param separatedString1 String
     * @param separatedString2 String
     * @param separator 默认为 半角逗号(,)
     * @return String
     */
    public static String removeAll(String separatedString1, String separatedString2, char separator){
        List<String> list = toList(separatedString1, separator);
        list.removeAll(toList(separatedString2, separator));
        return join(list, separator);
    }
    /**
     * 两个用特定符号(常用的是逗号)隔开多个关键字的字符串。从第一个里面保留第二个出现的字符串
     * 比如001,002  保留  002,003 得到 002
     * 集合运算中 INTERSECTION
     * @param separatedString1 String
     * @param separatedString2 String
     * @param separator 默认为 半角逗号(,)
     * @return String
     */
    public static String retainAll(String separatedString1, String separatedString2, char separator){
        List<String> list = toList(separatedString1, separator);
        list.retainAll(toList(separatedString2, separator));
        return join(list, separator);
    }
    /**
     * 两个用特定符号(常用的是逗号)隔开多个关键字的字符串。如果第一个和第二个有相同元素则返回true
     * 比如001,002  是否与交集  002,003 得到 true
     * @param separatedString1 String
     * @param separatedString2 String
     * @param separator 默认为 半角逗号(,)
     * @return boolean
     */
    public static boolean containsAny(String separatedString1, String separatedString2, char separator){
        Set<String> set = toSet(separatedString1, separator);
        for(String token:toList(separatedString2, separator)){
            if(set.contains(token)){
                return true;
            }
        }
        return false;
    }

    /**
     * 个用特定符号(常用的是逗号)隔开多个关键字的字符串。如果第一个里包含第二个所有元素则返回true
     * @param separatedString1 String
     * @param separatedString2 String
     * @param separator 默认为 半角逗号(,)
     * @return boolean
     */
    public static boolean containsAll(String separatedString1, String separatedString2, char separator){
        Set<String> set = toSet(separatedString1, separator);
        return set.containsAll(toSet(separatedString2, separator));
    }

    private static LinkedHashSet<String> toSet(String separatedString,char separator){
        if(separatedString==null){
            return new LinkedHashSet<>();
        }
        return new LinkedHashSet<>(Arrays.asList(separatedString.split(new String(new char[]{separator}))));
    }
    private static ArrayList<String> toList(String separatedString,char separator){
        if(separatedString==null){
            return new ArrayList<>();
        }
        return new ArrayList<>(Arrays.asList(separatedString.split(new String(new char[]{separator}))));
    }
}
