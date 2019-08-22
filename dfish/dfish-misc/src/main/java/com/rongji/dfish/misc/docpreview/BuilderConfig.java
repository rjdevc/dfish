package com.rongji.dfish.misc.docpreview;

import com.rongji.dfish.base.Utils;

public class BuilderConfig {
    public String getFileRootPath() {
        return fileRootPath;
    }
    public void setFileRootPath(String fileRootPath) {
        this.fileRootPath = fileRootPath;
    }
    private String fileRootPath="./";

    public BuilderConfig clone(){
        BuilderConfig cloned=new BuilderConfig();
        Utils.copyPropertiesExact(cloned,this);
        return cloned;
    }
}
