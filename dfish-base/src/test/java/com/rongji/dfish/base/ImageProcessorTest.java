package com.rongji.dfish.base;

import com.rongji.dfish.base.util.FileUtil;

import java.io.FileInputStream;
import java.io.FileOutputStream;

public class ImageProcessorTest {
    public static void main(String[] args) throws Exception {
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
        ImageProcessor oper2=ImageProcessor.of(new FileInputStream("E:\\王芳照片\\北大毕业_20191119123107.jpg"));
        ImageProcessor jpegThumb=oper2.oraginalThumbnail();
        if(jpegThumb!=null){
            jpegThumb.saveAs(new FileOutputStream("C:\\Users\\Administrator\\Desktop\\originalThumbnail.jpg"));
        }


    }

    private static void showTime(String msg,long begin) {
        long cost=System.currentTimeMillis()-begin;
        System.out.println(cost+"ms: "+msg);
    }
}
