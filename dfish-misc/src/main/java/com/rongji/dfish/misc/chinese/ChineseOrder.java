package com.rongji.dfish.misc.chinese;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

//import com.rongji.dfish.misc.origin.OriginMatcher;

/**
 * 字符串的排序器，和java.text.Collator不同。
 * 该排序方式对全角半角不影响排序结果。并且和windows一样，会对数字进行分析。
 * 新建文本文档 (9).txt 和 新建文本文档 (10).txt 会以自然语言的方式进行排序。
 * 并且一般来说，符号会在文本的前面，而不会像ASCII那样分散在字符中间。
 * 字库合并了简体与繁体中文。一些非常用汉字如 噩 字也按正确的顺序排列
 *
 * @author DFish Team
 */
public class ChineseOrder implements java.util.Comparator<Object> {
    /**
     * 默认-音序
     */
    public static final int MODE_COMMON = 0;
    /**
     * 音序-按人员姓名优化
     */
    public static final int MODE_PERSON_NAME = 1;
    /**
     * 笔划数顺序
     */
    public static final int MODE_STROKE = 2;

    /**
     * 默认构造函数
     */
    public ChineseOrder() {
        this(MODE_COMMON);
    }

    private static final HashMap<Integer, ChineseOrder> LOADED_MODES = new HashMap<Integer, ChineseOrder>();

    /**
     * 构造函数
     *
     * @param mode 模式
     * @see #MODE_COMMON
     * @see #MODE_PERSON_NAME
     * @see #MODE_STROKE
     */
    public ChineseOrder(int mode) {
        //LinLW 2018-02-05 防止多次new而加载多次
        ChineseOrder loadedInstance = LOADED_MODES.get(mode);
        if (loadedInstance != null) {
            this.CHAR_POS = loadedInstance.CHAR_POS;
            this.LOAD_INDEX = loadedInstance.LOAD_INDEX;
            this.NINE_POSTION = loadedInstance.NINE_POSTION;
            this.ZERO_POSTION = loadedInstance.ZERO_POSTION;
            return;
        }
        loadLib("/com/rongji/dfish/misc/chinese/sign_lib.txt");
        loadLib(LIB_SIGN_KEYBOAD);
        loadLibInversion(LIB_SIGN_KEYBOAD2);
        loadLibInversion(LIB_NUMBER);
        loadLibInversion(LIB_CHARACTER);
        loadLib("/com/rongji/dfish/misc/chinese/char_other_lib.txt");
        switch (mode) {
            case MODE_PERSON_NAME:
                loadLib("/com/rongji/dfish/misc/chinese/char_chinese_name_lib.txt");
                break;
            case MODE_STROKE:
                loadLib("/com/rongji/dfish/misc/chinese/char_chinese_stroke_lib.txt");
                break;
            default:
                loadLib("/com/rongji/dfish/misc/chinese/char_chinese_lib.txt");
        }
        ZERO_POSTION = CHAR_POS.get('0');
        NINE_POSTION = CHAR_POS.get('9');
        LOADED_MODES.put(mode, this);
    }

    /**
     * 对字符进行排序比较
     *
     * @param o1 字符1
     * @param o2 字符2
     * @return 对比结果
     */
    @Override
    public int compare(Object o1, Object o2) {
        if (o1 == null) {
            return o2 == null ? 0 : -1;
        } else if (o2 == null) {
            return 1;
        }
        int i = 0;
        char a[] = ((String) o1).toCharArray();
        char b[] = ((String) o2).toCharArray();

        int length = Math.min(a.length, b.length);
        while (i < length) {
            Integer posA = CHAR_POS.get(a[i]);
            Integer posB = CHAR_POS.get(b[i]);
            if (posA != null && posB == null) {
                //B在不可识别的字符中，排最前面
                return 1;
            } else if (posA == null && posB != null) {
                //A在不可识别的字符中，排最前面
                return -1;
            }
            if (posA != null) {
                //落在需要控制的字符范例里
                if (posA >= ZERO_POSTION && posA <= NINE_POSTION &&
                        posB >= ZERO_POSTION && posB <= NINE_POSTION) {
                    //按数字比较
                    NumberResult iva = getNumberValue(a, i);
                    NumberResult ivb = getNumberValue(b, i);
                    if (iva.result < ivb.result) {
                        return -1;
                    } else if (iva.result > ivb.result) {
                        return 1;
                    } else {
                        i += Math.min(iva.length, ivb.length);
                        continue;
                    }
                }
                int v = posA - posB;
                if (v != 0) {
                    return v;
                }
            } else if (a[i] != b[i]) {
                return a[i] - b[i];
            }
            i++;
        }
        //如果前面的字符都一样，那么比对完，谁的还有更多的字符，它排在后面。
        return a.length - b.length;
    }

    private static class NumberResult {
        long result;
        int length;
    }

    private NumberResult getNumberValue(char[] cs, int startIndex) {
        int i = startIndex;
        long result = 0;
        //如果这个数字过长，仅识别前面17位的大小
        int limit = Math.min(cs.length, startIndex + 17);
        while (i < limit) {
            int pos = CHAR_POS.get(cs[i]);
            if (pos < ZERO_POSTION || pos > NINE_POSTION) {
                break;
            }
            int v = pos - ZERO_POSTION;
            result = 10 * result + v;
            i++;
        }
        NumberResult r = new NumberResult();
        r.result = result;
        r.length = i - startIndex;
        return r;
    }


    private HashMap<Character, Integer> CHAR_POS = new HashMap<Character, Integer>();
    private int ZERO_POSTION;
    private int NINE_POSTION;
    private int LOAD_INDEX;

    private static final String[] LIB_SIGN_KEYBOAD2 = {
            "/`^\\",
            "／｀＾＼"
    };
    private static final String[] LIB_SIGN_KEYBOAD = {
            " 　",
            ";；﹔",
            "!！﹗",
            "?？﹖",
            "&＆﹠",
            "#＃﹟",
            "*＊﹡",
            "~～∼",
            "+＋﹢",
            "=＝﹦",
            "%％﹪",
            "@＠﹫",
            "|｜︱",
            "$＄￥฿¢₡₢₫€₣₤₥₦₧£₨₪₩¥￠￡﹩",
            ",，、﹐､",
            ".．。…﹒•",
            ":：∶﹕︰‥",
            "-_－＿­‐‑‒–—―−¯￣﹣",
            "()[]{}（）［］｛｝〔〕〖〗【】︵︶︷︸︹︺︻︼﹙﹚﹛﹜﹝﹞",
            "<>＜＞〈〉《》︽︾︿﹀﹤﹥«»",
            "'\"＇＂‘’“”〝〞‵「」『』﹁﹂﹃﹄",
    };
    private static final String[] LIB_NUMBER = {
            "0123456789",
            "０１２３４５６７８９"
    };
    private static final String[] LIB_CHARACTER = {
            "ABCDEFGHIJKLMNOPQRSTUVWXYZ",
            "abcdefghijklmnopqrstuvwxyz",
            "ＡＢＣＤＥＦＧＨＩＪＫＬＭＮＯＰＱＲＳＴＵＶＷＸＹＺ",
            "ａｂｃｄｅｆｇｈｉｊｋｌｍｎｏｐｑｒｓｔｕｖｗｘｙｚ"
    };


    private void loadLib(String resName) {
        try {
            InputStream is = ChineseOrder.class.getResourceAsStream(resName);
            BufferedReader bis = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            String line = "";
            while ((line = bis.readLine()) != null) {
                if (line.indexOf('=') < 0) {
                    continue;
                }
                String[] pair = line.split("=");
                if (pair.length != 2) {
                    continue;
                }
                String value = pair[1];
                for (char c : value.toCharArray()) {
                    CHAR_POS.put(c, LOAD_INDEX++);//把原始数值放入到位置映射
                }
            }
            bis.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void loadLibInversion(String[] lib) {
        char[][] charArr = new char[lib.length][];
        for (int k = 0; k < lib.length; k++) {
            charArr[k] = lib[k].toCharArray();
        }
        for (int i = 0; i < lib[0].length(); i++) {
            for (int k = 0; k < lib.length; k++) {
                char c = charArr[k][i];
                CHAR_POS.put(c, LOAD_INDEX);
            }
            LOAD_INDEX++;
        }
    }

    private void loadLib(String[] lib) {
        for (int k = 0; k < lib.length; k++) {
            char[] charArr = lib[k].toCharArray();
            for (int i = 0; i < charArr.length; i++) {
                char c = charArr[i];
                CHAR_POS.put(c, LOAD_INDEX);
            }
            LOAD_INDEX++;
        }
    }
}
