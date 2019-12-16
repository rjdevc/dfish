package com.rongji.dfish.base;

import com.rongji.dfish.base.util.img.ImageInfo;
import com.rongji.dfish.base.util.img.ImageOperation;
import com.rongji.dfish.base.util.img.JpegInfo;

import java.io.FileInputStream;
import java.io.FileOutputStream;

public class ImageOperationTest {
    public static void main(String[] args) throws Exception {
        ImageOperation narked= ImageOperation.of(new FileInputStream(""))
                .zoom(800,800)
                .mark();
//        ZoomCallBack zoomCallBack=new ZoomCallBack();
//        zoomCallBack.setMaxWidth(200);
//        zoomCallBack.setMaxHeight(200);
//        oper.schedule(zoomCallBack);
//        oper.output("");

        narked.watermark("",16,null,0,0).output(null);

        narked.zoom(400,400)
                .watermark("",16,null,0,0)
                .output(null);


//        long begin=System.currentTimeMillis();
//        BufferedImage rawImage = ImageIO.read(new FileInputStream("D:\\3_项目\\公司ITASK\\新闻附件\\000000000271.jpg"));
//        showTime("after readImage",begin);
//        Graphics g=rawImage.getGraphics();
//        showTime("after getGraphics",begin);
//        ZoomCallBack zoomCallBack=new ZoomCallBack();
//        zoomCallBack.setMaxWidth(200);
//        zoomCallBack.setMaxHeight(200);
//        oper.schedule(zoomCallBack);
//        oper.zoom(200,200);
//        ImageProcessor oper2 =oper.mark();
//
//        oper2.zoom(100,100)
//                .saveAs(new FileOutputStream("C:\\Users\\LinLW\\Desktop\\test_100.jpg"));
//
//        oper2.saveAs(new FileOutputStream("C:\\Users\\LinLW\\Desktop\\test_200.jpg"));

//        ImageProcessor oper=ImageProcessor.of(new FileInputStream("D:\\3_项目\\公司ITASK\\新闻附件\\000000000271.jpg"));
//        oper.reuse();
//        ImageProcessor.ImageInfo ii=oper.getImageInfo();
//        if(ii.getWidth()>400||ii.getHeight()>400){
//            oper.zoom(400,400)
//                    .saveAs(new FileOutputStream("C:\\Users\\LinLW\\Desktop\\test_400.jpg"));
//        }else {
//            //FileUtil.copyFile()
//        }
        ImageInfo ii=ImageInfo.of(new FileInputStream("D:\\3_项目\\公司ITASK\\新闻附件\\000000000271.jpg"));
        if(ii instanceof JpegInfo){
            JpegInfo cast=(JpegInfo)ii;
            if(cast.getThumbnail()!=null){
                ImageOperation jpegThumb=ImageOperation.of(cast.getThumbnail());
                jpegThumb.output(new FileOutputStream("C:\\Users\\Administrator\\Desktop\\originalThumbnail.jpg"));
            }
        }

    }

    private static void showTime(String msg,long begin) {
        long cost=System.currentTimeMillis()-begin;
        System.out.println(cost+"ms: "+msg);
    }
}
