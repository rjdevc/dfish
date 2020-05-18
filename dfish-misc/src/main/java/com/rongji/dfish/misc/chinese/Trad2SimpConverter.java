package com.rongji.dfish.misc.chinese;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import com.rongji.dfish.base.text.TrieTree;

/**
 * 实现繁体与简体之间的相互转化
 *
 * @author Q
 */
public class Trad2SimpConverter {
    /**
     * 转化类型-繁体转化为简体
     */
    public static final int TRADITIONAL_TO_SIMPLIFIED = 1;
    /**
     * 转化类型-简体转化为繁体
     */
    public static final int SIMPLIFIED_TO_TRADITIONAL = 2;
    private TrieTree<String> trieTree;
    private int mod;

    /**
     * 默认繁体转化为简体
     */
    public Trad2SimpConverter() {
        this(1);
    }

    /**
     * 繁体与简体的转化
     *
     * @param mod 用来判断繁体与简体的转化方向；当 TRADITIONAL_TO_SIMPLIFIED 时繁体转化为简体，当 SIMPLIFIED_TO_TRADITIONAL 简体转化为繁体
     */
    public Trad2SimpConverter(int mod) {
        this.mod = mod;
        trieTree = new TrieTree<>(true);
        //字库和词库都是以：简体=繁体的形式存放
        if (TRADITIONAL_TO_SIMPLIFIED == mod) {
            //繁体到简体的时候先把一个繁体对应多个简体的字库压入字典树
            loadCharLib("/com/rongji/dfish/misc/chinese/trad2simp_char.txt");
            //繁体到简体的时候把词库压入字典树
            loadWordLib("/com/rongji/dfish/misc/chinese/trad2simp_word.txt");

        }
        if (SIMPLIFIED_TO_TRADITIONAL == mod) {
            //简体到繁体的时候先把一个简体字对应多个繁体字的字库压入字典树
            loadCharLib("/com/rongji/dfish/misc/chinese/simp2trad_char.txt");
            //简体到繁体的时候把词库压入字典树
            loadWordLib("/com/rongji/dfish/misc/chinese/simp2trad_word.txt");
        }
        //把繁体与简体字一一对应的字库压入字典树
        loadCharLib("/com/rongji/dfish/misc/chinese/the_same_char.txt");
        //把繁体与简体字一一对应的词库压入字典树
        loadWordLib("/com/rongji/dfish/misc/chinese/the_same_word.txt");
    }

    /**
     * 实现词库压入字典树的操作
     *
     * @param resname 词库的文件目录+文件名
     */
    private void loadWordLib(String resname) {
        try {
            InputStream is = getClass().getResourceAsStream(resname);
            BufferedReader bis = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            String line = "";
            while ((line = bis.readLine()) != null) {
                if (line == null || line.indexOf('=') < 0) {
                    continue;
                }
                String[] pair = line.split("=");
                if (mod == TRADITIONAL_TO_SIMPLIFIED) {
                    trieTree.put(pair[1], pair[0]);
                } else {
                    trieTree.put(pair[0], pair[1]);
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
     * 实现字库的压入字典树的操作
     *
     * @param resname 字库的文件目录+文件名
     */
    private void loadCharLib(String resname) {
        try {
            InputStream is = getClass().getResourceAsStream(resname);
            BufferedReader dis = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            String line = "";
            while ((line = dis.readLine()) != null) {
                if (line == null || line.indexOf('=') < 0) {
                    continue;
                }
                String[] pair = line.split("=");
                for (char ch : pair[1].toCharArray()) {
                    String tradValue = new String(new char[]{ch});
                    if (TRADITIONAL_TO_SIMPLIFIED == mod) {
                        //只需要载入左边简体字的第一个字即可
                        trieTree.put(tradValue, String.valueOf(pair[0].charAt(0)));
                    } else {
                        trieTree.put(pair[0], tradValue);
                        break;//只需要载入右边繁体字的第一个字即可
                    }
                }
            }

            if (dis != null) {
                dis.close();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 做简体与繁体进行转化
     *
     * @param source 传递过来需要转化的内容
     * @return String类型转化后的内容
     */
    public String convert(String source) {
        StringBuilder sb = new StringBuilder();
        char[] chs = source.toCharArray();
        List<TrieTree.SearchResult<String>> result = trieTree.search(source);
        int i = 0;
        for (TrieTree.SearchResult<String> token : result) {
            if (token.getBegin() > i) {
                sb.append(chs, i, token.getBegin() - i);
            }
            sb.append(token.getValue());
            i = token.getEnd();
        }
        if (i < chs.length) {
            sb.append(chs, i, chs.length - i);
        }
        return sb.toString();
    }
}
