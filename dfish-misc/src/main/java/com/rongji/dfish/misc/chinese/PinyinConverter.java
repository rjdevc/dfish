package com.rongji.dfish.misc.chinese;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import com.rongji.dfish.base.text.TrieTree;

import net.sourceforge.pinyin4j.PinyinHelper;

/**
 * 将汉字转为拼音
 * 如果有一整句话。那这个转化工具，将尽量根据词组。正确的转化多音字。
 */
public class PinyinConverter {
    /**
     * 使用数字标识   shou3du1
     */
    public static final int FORMAT_WITH_TONE_NUMBER = 0;
    /**
     * 不使用音调 shoudu
     */
    public static final int FORMAT_WITHOUT_TONE = 1;
    /**
     * 使用音调，shǒudū
     */
    public static final int FORMAT_WITH_TONE_MARK = 2;

    /**
     * 使用数字标识   shou3du1
     *
     * @see #FORMAT_WITH_TONE_NUMBER
     */
    @Deprecated
    public static final int WITH_TONE_NUMBER = FORMAT_WITH_TONE_NUMBER;
    /**
     * 不使用音调 shoudu
     *
     * @see #FORMAT_WITHOUT_TONE
     */
    @Deprecated
    public static final int WITHOUT_TONE = FORMAT_WITHOUT_TONE;
    /**
     * 使用音调，shǒudū
     *
     * @see #FORMAT_WITH_TONE_MARK
     */
    @Deprecated
    public static final int WITH_TONE_MARK = FORMAT_WITH_TONE_MARK;

    /**
     * 默认-音序
     */
    public static final int MODE_COMMON = 0;
    /**
     * 音序-按人员姓名优化
     */
    public static final int MODE_PERSON_NAME = 1;
    private int mode;

    /**
     * 构造函数
     *
     * @param mode 模式
     */
    public PinyinConverter(int mode) {
        this.mode = mode;
    }

    /**
     * 转化成拼音 非汉字部分不转化
     *
     * @param str          含有汉字的字符串
     * @param separator    拼音转化出来后的分隔符，有时需要用空格分隔
     * @param pinyinFormat 格式，纯ASCII字母还是带 阴平 阳平等音调
     * @return String 转化后的拼音结果
     */
    public String convert(String str, String separator, int pinyinFormat) {
        TrieTree<String> trieTree = getMainLib();
        return convert(trieTree, str, separator, pinyinFormat);
    }

    /**
     * 转化成拼音 非汉字部分不转化
     *
     * @param trieTree     字典树
     * @param str          含有汉字的字符串
     * @param separator    拼音转化出来后的分隔符，有时需要用空格分隔
     * @param pinyinFormat 格式，纯ASCII字母还是带 阴平 阳平等音调
     * @return String
     */
    private String convert(TrieTree<String> trieTree, String str, String separator, int pinyinFormat) {
        StringBuilder sb = new StringBuilder();
        List<TrieTree.SearchResult<String>> result = trieTree.search(str);
        char[] chs = str.toCharArray();
        int i = 0;
        for (TrieTree.SearchResult<String> token : result) {
            if (token.getBegin() > i) {
                sb.append(chs, i, token.getBegin() - i);
            }
            switch (pinyinFormat) {
                case FORMAT_WITHOUT_TONE:
                    sb.append(token.getValue().replace("5", "").replace("4", "").replace("3", "")
                            .replace("2", "").replace("1", ""));
                    break;
                case FORMAT_WITH_TONE_MARK:
                    sb.append(convert(getVowel(), token.getValue(), null, FORMAT_WITH_TONE_NUMBER));
                    break;
                default:
                    sb.append(token.getValue());
            }
            i = token.getEnd();
            if (separator != null && !"".equals(separator)) {
                sb.append(separator);
            }
        }
        if (i < chs.length) {
            sb.append(chs, i, chs.length - i);
        }
        return sb.toString();
    }

    private TrieTree<String> MAIN_LIB = null;
    private TrieTree<String> VOWEL = null;

    private TrieTree<String> getMainLib() {
        if (MAIN_LIB == null) {
            synchronized (PinyinConverter.class) {
                if (MAIN_LIB == null) {
                    //反序(从右往做匹配) 中文的词，经常后面那个字更有意义，所以反序一般会有更优的匹配度
                    MAIN_LIB = new TrieTree<>(true);
                    switch (mode) {
                        case MODE_PERSON_NAME:
                            loadCharLib(MAIN_LIB, "/com/rongji/dfish/misc/chinese/char_chinese_name_lib.txt");
                            break;
                        default:
                            loadCharLib(MAIN_LIB, "/com/rongji/dfish/misc/chinese/char_chinese_lib.txt");
                    }
                    loadPairLib(MAIN_LIB, "/com/rongji/dfish/misc/chinese/word_lib.txt");//初始化词典
                }
            }
        }
        return MAIN_LIB;
    }

    private TrieTree<String> getVowel() {
        if (VOWEL == null) {
            synchronized (PinyinConverter.class) {
                if (VOWEL == null) {
                    VOWEL = new TrieTree<>();
                    loadPairLib(VOWEL, "/com/rongji/dfish/misc/chinese/tone_lib.txt");//初始化韵母表
                }
            }
        }
        return VOWEL;
    }

    private void loadPairLib(TrieTree<String> tree, String fileName) {
        try {
            InputStream is = getClass().getResourceAsStream(fileName);
            BufferedReader bis = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            String line = "";
            while ((line = bis.readLine()) != null) {
                if (line == null || line.indexOf('=') < 0) {
                    continue;
                }
                String[] pair = line.split("=");
                tree.put(pair[0], pair[1]);
            }

            if (bis != null) {
                bis.close();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void loadCharLib(TrieTree<String> tree, String fileName) {
        try {
            InputStream is = getClass().getResourceAsStream(fileName);
            BufferedReader bis = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            String line = "";
            while ((line = bis.readLine()) != null) {
                if (line == null || line.indexOf('=') < 0) {
                    continue;
                }
                String[] pair = line.split("=");
                for (char c : pair[1].toCharArray()) {
                    String key = new String(new char[]{c});
                    tree.put(key, pair[0]);
                }
            }

            if (bis != null) {
                bis.close();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 构造函数
     *
     * @param str 字符
     * @return 转换后的拼音结果
     */
    public String convert(String str) {
        return convert(str, "", FORMAT_WITHOUT_TONE);
    }

    /**
     * 取得拼音
     *
     * @param str 字符
     * @param hasToneNumber 是否包含音调。音调以0(轻声)1(阴平)2(阳平)3(上声)4(去声)
     * @return String 转换后的拼音结果
     */
    public static String getPinyin(String str, boolean hasToneNumber) {
        if (str == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        for (char c : str.toCharArray()) {
            if (c >= 32 && c <= 127) {
                sb.append(c);
                continue;
            }//英文字不做解析
            String[] pinyins = PinyinHelper.toHanyuPinyinStringArray(c);
            if (pinyins == null || pinyins.length == 0) {
                sb.append(c);
                continue;
            }
            if (hasToneNumber) {
                sb.append(pinyins[0]);
            } else {
                String s = pinyins[0];
                char lastChar = s.charAt(s.length() - 1);
                if (lastChar >= '0' && lastChar <= '4') {
                    sb.append(s,0, s.length() - 1);
                } else {
                    sb.append(pinyins[0]);
                }
            }
        }
        return sb.toString();
    }

    /**
     * 同时取得全拼和简拼的字符串
     *
     * @param str 字符
     * @param hasToneNumber 是否有音标
     * @return String[]
     */
    public static String[] getPinyinFullShortFormat(String str, boolean hasToneNumber) {
        if (str == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        StringBuilder shortSB = new StringBuilder();
        for (char c : str.toCharArray()) {
            if (c >= 32 && c <= 127) {
                sb.append(c);
                shortSB.append(c);
                continue;
            }//英文字不做解析
            String[] pinyins = PinyinHelper.toHanyuPinyinStringArray(c);
            if (pinyins == null || pinyins.length == 0) {
                sb.append(c);
                shortSB.append(c);
                continue;
            }
            shortSB.append(pinyins[0].charAt(0));
            if (hasToneNumber) {
                sb.append(pinyins[0]);
            } else {
                String s = pinyins[0];
                char lastChar = s.charAt(s.length() - 1);
                if (lastChar >= '0' && lastChar <= '4') {
                    sb.append(s,0, s.length() - 1);
                } else {
                    sb.append(pinyins[0]);
                }
            }
        }
        return new String[]{sb.toString(), shortSB.toString()};
    }

}
