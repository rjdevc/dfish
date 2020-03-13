package com.rongji.dfish.misc.docpreview;

import com.rongji.dfish.misc.docpreview.data.Document;
import com.rongji.dfish.misc.docpreview.data.Drawing;

import java.io.InputStream;

/**
 * 抽象文档解析器
 */
public abstract class DocumentParser {
    protected Builder builder;
    public void setBuilder(Builder builder){
        this.builder=builder;
    }

    /**
     * 解析文档
     * @param is
     * @return
     */
    public abstract Document parse(InputStream is);
    /**
     * 保存图片，并把产生的路径信息写入到drawing中。
     * @param data
     * @param ext
     * @param drawing
     */
    protected void savePic(byte[] data, String ext, Drawing drawing) {
        //这里只是转接，由builder做这件事情访问，该类与builder同包
        builder.savePic(data,ext,drawing);
    }
}
