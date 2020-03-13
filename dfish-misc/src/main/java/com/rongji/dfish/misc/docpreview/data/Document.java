package com.rongji.dfish.misc.docpreview.data;

import java.util.List;

/**
 * 文档对象
 */
public class Document {

    /**
     * 对象主体有 element列表构成
     * @return List
     */
    public List<DocumentElement> getBody() {
        return body;
    }

    /**
     * 对象主体有 element列表构成
     * @param body List
     */
    public void setBody(List<DocumentElement> body) {
        this.body = body;
    }

    /**
     * 构建过程中文档总分
     * @return int
     */
    public int getTotalScore() {
        return totalScore;
    }

    /**
     * 构建过程中文档总分
     * @param totalScore int
     */
    public void setTotalScore(int totalScore) {
        this.totalScore = totalScore;
    }

    /**
     * 构建过程中summary分数
     * @return int
     */
    public int getSummaryScore() {
        return summaryScore;
    }

    /**
     * 构建过程中summary分数
     * @return int
     */
    public void setSummaryScore(int summaryScore) {
        this.summaryScore = summaryScore;
    }

    private int totalScore;
    private int summaryScore;
    private List<DocumentElement> body;
}
