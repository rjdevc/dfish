package com.rongji.dfish.misc.docpreview;

import com.rongji.dfish.base.util.Utils;

/**
 * 构建器配置
 */
public class BuilderConfig {
    private String fileRootPath="./";
    private int summaryScore = 6000;

    private ImageURLConverter imageURLConverter=NONE ;


    /**
     * 文件夹根路径，这里的根路径，是指，某个大的文件夹。
     * 下面可能会自动根据日期创建子文件夹。然后在文件夹内放置文件。
     * 以提高文件系统是使用性能。 只有这个大文件夹是不变的。就可以记录下来
     * @return String
     */
    public String getFileRootPath() {
        return fileRootPath;
    }

    /**
     * 文件夹根路径，这里的根路径，是指，某个大的文件夹。
     * 下面可能会自动根据日期创建子文件夹。然后在文件夹内放置文件。
     * 以提高文件系统是使用性能。 只有这个大文件夹是不变的。就可以记录下来
     * @param fileRootPath String
     */
    public void setFileRootPath(String fileRootPath) {
        this.fileRootPath = fileRootPath;
    }

    /**
     * 预览分数。预览分数越高保留的预览的文字就越多
     * 通常分数在6000左右，可以有200个行字左右
     * @return int
     */
    public int getSummaryScore() {
        return summaryScore;
    }

    /**
     * 预览分数。预览分数越高保留的预览的文字就越多
     * 通常分数在6000左右，可以有200个行字左右
     * @param summaryScore int
     */
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

    /**
     * 可以注入一个URL转化器。这样可以吧文档预览里面的本地文件
     * 转化成一个外部可用的，安全的下载地址。
     * @see ImageURLConverter
     * @return ImageURLConverter
     */
    public ImageURLConverter getImageURLConverter() {
        return imageURLConverter;
    }

    /**
     * 可以注入一个URL转化器。这样可以吧文档预览里面的本地文件
     * 转化成一个外部可用的，安全的下载地址。
     * see ImageURLConverter
     * @param imageURLConverter ImageURLConverter
     */
    public void setImageURLConverter(ImageURLConverter imageURLConverter) {
        this.imageURLConverter = imageURLConverter;
    }

    /**
     * 设置下载目录。
     * 比如说fileRootPath 写的是D:/tomcat/webapp/myproject/docpreview_img
     * 这里的URL可以写docpreview_img 表示，如果允许直接下载的话。使用这个前缀 加上文件相对路径。
     * @param url
     */
    public void setDownloadURLFolder(String url) {
        this.imageURLConverter = (relativeURL) -> url+"/"+relativeURL;
    }

    private static ImageURLConverter NONE=( relativeURL)->  relativeURL;

}
