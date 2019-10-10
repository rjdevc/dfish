package com.rongji.dfish.misc.docpreview.data;

import java.util.List;

public class Document {


    public List<DocumentElement> getBody() {
        return body;
    }
    public void setBody(List<DocumentElement> body) {
        this.body = body;
    }

    public int getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(int totalScore) {
        this.totalScore = totalScore;
    }

    public int getSummaryScore() {
        return summaryScore;
    }

    public void setSummaryScore(int summaryScore) {
        this.summaryScore = summaryScore;
    }

    private int totalScore;
    private int summaryScore;
    private List<DocumentElement> body;
}
