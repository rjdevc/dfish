package com.rongji.dfish.base.text;


import java.util.List;

/**
 * WordSplitter 是切词器
 * <p>不管是搜索引擎还是其他文本处理工具，通常需要把一长段文本，切成词。
 * 切词的时候，具体的切词器可能，出现重复，或忽略掉一些词。
 * 这些被忽略的词通常叫停止词(stop word) 比如 这，那 一个 等。这些每篇文档可能都出现但结果缺没有实际意义。
 * 重复通常是有多重词义的时候，可能连着几个字都算词组的时候，
 * 如，李长春同志到XX医院进行探望  可能会同时包含 李长春 和 长春，这可能更加贴合特定的业务场景。
 * </p>
 */
public interface WordSplitter {
    /**
     * 将文本切成一个个词语。
     * @param text 文本
     * @return 切词集合
     */
    List<String> split(String text);
}
