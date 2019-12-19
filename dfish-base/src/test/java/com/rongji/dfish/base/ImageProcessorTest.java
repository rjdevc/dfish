package com.rongji.dfish.base;

import com.rongji.dfish.base.img.ImageInfo;
import com.rongji.dfish.base.img.ImageProcessor;
import com.rongji.dfish.base.img.JpegInfo;
import org.junit.Test;

import java.awt.*;
import java.io.*;

public class ImageProcessorTest {
    public static void main(String[] args) throws Exception {
//        ImageOperation narked= ImageOperation.of(new FileInputStream(""))
//                .zoom(800,800)
//                .mark();
//        ZoomCallBack zoomCallBack=new ZoomCallBack();
//        zoomCallBack.setMaxWidth(200);
//        zoomCallBack.setMaxHeight(200);
//        oper.schedule(zoomCallBack);
//        oper.output("");
//
//        narked.watermark("",16,null,0,0).output(null);
//
//        narked.zoom(400,400)
//                .watermark("",16,null,0,0)
//                .output(null);


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


    }
    private static final String SOURCE_FILE="D:\\3_项目\\公司ITASK\\新闻附件\\000000000271.jpg";
    private static final String OUTPUT_PATH="C:\\Users\\LinLW\\Desktop\\imgtest\\";
    @Test
    public void testMark()throws Exception{
        ImageProcessor marked= ImageProcessor.of(new FileInputStream(SOURCE_FILE))
                .zoom(800,800)
                .mark();

        marked.watermark("榕基软件",16,new Color(255,255,255,255),-84,-36)
            .output(new FileOutputStream(OUTPUT_PATH + "f_800.jpg"));
        marked.reset();
        marked.zoom(400,400)
                .watermark("榕基软件",12,new Color(153,204,255,255),18,-27)
                .output(new FileOutputStream(OUTPUT_PATH + "f_400.jpg"));
    }
    @Test
    public void testAnotherPhoto()throws Exception{
        String file="D:\\7_app\\tencent\\qq\\receive_files\\MobileFile\\IMG_20191217_100441.jpg";
        ImageInfo ii=ImageInfo.of(new FileInputStream(file));
        if(ii instanceof JpegInfo){
            JpegInfo cast=(JpegInfo)ii;
            if(cast.getThumbnail()!=null){
                ImageProcessor jpegThumb= ImageProcessor.of(cast.getThumbnail());
                jpegThumb.output(new FileOutputStream(OUTPUT_PATH+"huaweiThumb.jpg"));
            }
        }
    }

    @Test
    public void testAdvZoom()throws Exception{
//        ImageProcessor marked= ImageProcessor.of(new FileInputStream(SOURCE_FILE))
//                .mark();
//        ImageProcessor.AdvancedZoomCallback zoom1=new ImageProcessor.AdvancedZoomCallback();
//        zoom1.setMaxWidth(800);
//        zoom1.setMaxHeight(800);
//        marked.schedule(zoom1)
//                .output(new FileOutputStream(OUTPUT_PATH + "f_adv800.jpg"));
//
//        marked.reset();
//        marked.zoom(400,400)
//                .mark();
//
//        ImageProcessor.AdvancedZoomCallback zoom2=new ImageProcessor.AdvancedZoomCallback();
//        zoom2.setMaxWidth(600);
//        zoom2.setMaxHeight(600);
//        marked.schedule(zoom2)
//                .output(new FileOutputStream(OUTPUT_PATH + "f_nomore_600.jpg"));
//
//        ImageProcessor.AdvancedZoomCallback zoom3=new ImageProcessor.AdvancedZoomCallback();
//        zoom3.setMaxWidth(600);
//        zoom3.setMaxHeight(600);
//        zoom3.setSuitForScope(true);
//        marked.reset();
//        marked.schedule(zoom3)
//                .output(new FileOutputStream(OUTPUT_PATH + "f_suit_for_600.jpg"));
//
//        ImageProcessor.AdvancedZoomCallback zoom4=new ImageProcessor.AdvancedZoomCallback();
//        zoom4.setMaxWidth(400);
//        zoom4.setMaxHeight(400);
//        zoom4.setPaddingColor(new Color(153,204,255,255));
//        zoom4.setSuitForScope(true);
//        marked.reset();
//        marked.schedule(zoom4)
//                .output(new FileOutputStream(OUTPUT_PATH + "f_paddingcolor.jpg"));
//
//
//        ImageProcessor.AdvancedZoomCallback zoom5=new ImageProcessor.AdvancedZoomCallback();
//        zoom5.setMaxWidth(400);
//        zoom5.setMaxHeight(400);
//        zoom5.setMaxAspectRatio(1.0);
//        marked.reset().schedule(zoom5)
//                .output(new FileOutputStream(OUTPUT_PATH + "f_aspect1.0.jpg"));


    }

    @Test
    public void testZoom()throws Exception{
        ImageProcessor.of(
                new FileInputStream("C:\\Users\\LinLW\\Pictures\\u3411.png"))
                .zoom(400,400)
                .output(new FileOutputStream(OUTPUT_PATH + "f_shouZoomOut400.png") );
    }

    @Test
    public void testCut()throws Exception{
        ImageProcessor.of(new FileInputStream(SOURCE_FILE))
                .zoom(800,800)
                .cut(600,600)
                .output(new FileOutputStream(OUTPUT_PATH + "f_CUT600.jpg"));
    }


    @Test
    public void testOriginalThumb() throws Exception {
        ImageInfo ii=ImageInfo.of(new FileInputStream(SOURCE_FILE));
        if(ii instanceof JpegInfo){
            JpegInfo cast=(JpegInfo)ii;
            if(cast.getThumbnail()!=null){
                ImageProcessor jpegThumb= ImageProcessor.of(cast.getThumbnail());
                jpegThumb.output(new FileOutputStream(OUTPUT_PATH+"originalThumbnail.jpg"));
            }
        }
    }


}
