package com.rongji.dfish.misc.docpreview;

import com.rongji.dfish.base.util.Utils;

public class BuilderConfig {
    private String fileRootPath="./";
    private int summaryScore = 6000;

    private ImageURLConverter imageURLConverter=NONE ;



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

    @Override
    public BuilderConfig clone(){
        BuilderConfig cloned=new BuilderConfig();
        Utils.copyPropertiesExact(cloned,this);
        cloned.setImageURLConverter(this.imageURLConverter);
        return cloned;
    }

    public ImageURLConverter getImageURLConverter() {
        return imageURLConverter;
    }

    public void setImageURLConverter(ImageURLConverter imageURLConverter) {
        this.imageURLConverter = imageURLConverter;
    }

    /**
     * 比如说fileRootPath 写的是D:/tomcat/webapp/myproject/docpreview_img
     * 这里的URL可以写docpreview_img 表示，如果允许直接下载的话。使用这个前缀 加上文件相对路径。
     * @param url
     */
    public void setDownloadURLFolder(final String url) {
        this.imageURLConverter = new ImageURLConverter() {
            @Override
            public String getDownloadURL(String relativeURL) {
                return url+"/"+relativeURL;
            }
        };
    }

    private static ImageURLConverter NONE=new ImageURLConverter(){
        @Override
        public String getDownloadURL(String relativeURL) {
            return relativeURL;
        }
    };

}
