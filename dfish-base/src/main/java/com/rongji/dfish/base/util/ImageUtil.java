package com.rongji.dfish.base.util;

import com.rongji.dfish.base.img.ImageProcessor;

import java.awt.*;
import java.awt.image.ImageObserver;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * 图片处理工具类
 * @author lamontYu
 * @date 2019-05-07
 * @since 3.2
 */
public class ImageUtil {

    /**
     * 根据图片按比例缩放,图片的大小比例保持不变,系统计算最合理的大小进行输出
     * 图片按照输出流进行输出,输出完成后将自动关闭输入流和输出流
     *
     * @param input       图片文件输入流
     * @param output      图片文件输出流
     * @param width       输出图片的宽,<=0时采用原始图片宽度
     * @param height      输出图片的高,<=0时采用原始图片高度
     * @throws Exception 当输入流非图片或者图片绘制过程中可能有异常
     */
    public static void zoom(InputStream input, OutputStream output, int width, int height) throws Exception {
        ImageProcessor.of(input).zoom(width, height).output(output);
    }

    /**
     * 根据图片按照原始尺寸缩放
     * 图片按照输出流进行输出,输出完成后将自动关闭输入流和输出流
     *
     * @param input       图片文件输入流
     * @param output      图片文件输出流
     * @throws Exception 当输入流非图片或者图片绘制过程中可能有异常
     */
    public static void zoom(InputStream input, OutputStream output) throws Exception {
        zoom(input, output, -1, -1);
    }

    /**
     * 根据图片按指定宽高强制缩放
     * 图片按照输出流进行输出,输出完成后将自动关闭输入流和输出流
     *
     * @param input       图片文件输入流
     * @param output      图片文件输出流
     * @param width       输出图片的宽
     * @param height      输出图片的高
     * @throws Exception 当输入流非图片或者图片绘制过程中可能有异常
     */
    public static void resize(InputStream input, OutputStream output, int width, int height) throws Exception {
        ImageProcessor.of(input).resize(width, height).output(output);
    }

    /**
     * 根据图片按指定宽高切割(图片最中间部分),并按照输出流进行输出,输出完成后将自动关闭输入流和输出流
     *
     * @param input  图片文件输入流
     * @param output 图片文件输出流
     * @param width  缩放图片的宽
     * @param height 缩放图片的高
     * @throws Exception 当输入流非图片或者图片绘制过程中可能有异常
     */
    public static void cut(InputStream input, OutputStream output, int width, int height) throws Exception {
        ImageProcessor.of(input).cut(width, height).output(output);
    }

    /**
     * 根据图片按指定宽高切割(图片最中间部分),并按照输出流进行输出,输出完成后将自动关闭输入流和输出流
     *
     * @param input  图片文件输入流
     * @param output 图片文件输出流
     * @param x  图片左上角横坐标位置{@link Graphics#drawImage(Image, int, int, int, int, ImageObserver)}
     * @param y  图片左上角纵坐标位置{@link Graphics#drawImage(Image, int, int, int, int, ImageObserver)}
     * @param width  缩放图片的宽
     * @param height 缩放图片的高
     * @throws Exception 当输入流非图片或者图片绘制过程中可能有异常
     */
    public static void cut(InputStream input, OutputStream output, int x, int y, int width, int height) throws Exception {
        ImageProcessor.of(input).cut(x, y, width, height).output(output);
    }
}
