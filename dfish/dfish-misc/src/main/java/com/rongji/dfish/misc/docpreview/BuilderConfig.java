package com.rongji.dfish.misc.docpreview;

import com.rongji.dfish.base.Utils;

public class BuilderConfig {
    private String fileRootPath="./";
    private int summaryScore = 6000;
    public String getFileRootPath() {
        return fileRootPath;
    }
    public void setFileRootPath(String fileRootPath) {

        this.fileRootPath = fileRootPath;
    }

    public int getSummaryScore() {
        return summaryScore;
    }

    public void setSummaryScore(int summaryScore) {
        this.summaryScore = summaryScore;
    }

    public BuilderConfig clone(){
        BuilderConfig cloned=new BuilderConfig();
        Utils.copyPropertiesExact(cloned,this);
        return cloned;
    }
}
