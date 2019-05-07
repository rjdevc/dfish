package com.rongji.dfish.misc.util;

import com.rongji.dfish.base.Utils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.List;

/**
 * 图片处理助手
 *
 */
public class ImageUtil {

    /**
     * 是否图片扩展名
     * @param fileExtName
     * @return boolean
     */
    public static boolean isImageExtName(String fileExtName) {
        if (Utils.isEmpty(fileExtName)) {
            return false;
        }
        // 非支持格式的文件，不进行处理
        List<String> supportTypes = Arrays.asList(ImageIO.getReaderFormatNames());
        return supportTypes.contains(fileExtName.toLowerCase());
    }

    /**
     * 根据图片按比例缩放,图片的大小比例保持不变,系统计算得到最合理大小的图片
     * @param rawImage  原始图片
     * @param width     缩放后的宽度
     * @param height    缩放后的高度
     * @return BufferedImage
     */
    public static BufferedImage getZoomedImage(BufferedImage rawImage, int width, int height) {
        return getZoomedImage(rawImage, width, height, true);
    }

    /**
     * 根据图片按比例缩放,图片的大小比例保持不变,系统计算得到最合理大小的图片
     * @param rawImage  原始图片
     * @param width     缩放后的宽度,<=0时采用原始图片宽度
     * @param height    缩放后的高度,<=0时采用原始图片高度
     * @param destInside    原图是否在指定宽高范围内
     * @return BufferedImage
     */
    public static BufferedImage getZoomedImage(BufferedImage rawImage, int width, int height, boolean destInside) {
        if (rawImage == null) {
            return null;
        }
        if (width <= 0 && height <= 0) {
            return rawImage;
        } else if (width <= 0) {
            width = rawImage.getWidth();
        } else if (height <= 0) {
            height = rawImage.getHeight();
        }
        // 宽度缩放比例
        double widthScale = new Double(width) / rawImage.getWidth();
        // 高度缩放比例
        double heightScale = new Double(height) / rawImage.getHeight();
        // 如果原图都是在指定宽高内采用比例低的;若生成图片在原图范围内采取比例高的
        boolean basedWidth = destInside ? widthScale < heightScale : widthScale > heightScale;
        int destWidth;
        int destHeight;
        if (basedWidth) {
            destWidth = width;

            destHeight = (int)(widthScale * rawImage.getHeight());
            destHeight = destInside && destHeight > height ? height : destHeight;
        } else {
            destWidth = (int) (heightScale * rawImage.getWidth());
            destWidth = destInside && destWidth > width ? width : destWidth;

            destHeight = height;
        }
        // 获取目标图片
        BufferedImage destImage = new BufferedImage(destWidth, destHeight, rawImage.getType());
        Graphics g = destImage.getGraphics();
        g.drawImage(rawImage, 0, 0, destWidth, destHeight, null);
        // 画笔释放
        g.dispose();
        return destImage;
    }

    /**
     * 根据图片按比例缩放,图片的大小比例保持不变,系统计算最合理的大小进行输出
     * 图片按照输出流进行输出,输出完成后将自动关闭输入流和输出流
     * @param input     图片文件输入流
     * @param output    图片文件输出流
     * @param fileExtName   输出图片类型
     * @param width     输出图片的宽,<=0时采用原始图片宽度
     * @param height    输出图片的高,<=0时采用原始图片高度
     * @throws Exception    当输入流非图片或者图片绘制过程中可能有异常
     */
    public static void zoom(InputStream input, OutputStream output, String fileExtName, int width, int height) throws Exception {
        if (width <= 0 && height <= 0) { // 按照原始尺寸输出
            zoom(input, output, fileExtName);
            return;
        }

        if (input == null || output == null) {
            return;
        }
        if (!isImageExtName(fileExtName)) {
            throw new Exception("输出的非图片文件类型");
        }
        try {
            // 读取原始图片
            BufferedImage rawImage = ImageIO.read(input);
            // 获取缩放的图片
            BufferedImage destImage = getZoomedImage(rawImage, width, height);
            // 输出图片
            ImageIO.write(destImage, fileExtName, output);
        } finally {
            if (input != null) {
                input.close();
            }
            if (output != null) {
                output.close();
            }
        }
    }

    /**
     * 根据图片按照原始尺寸缩放
     * 图片按照输出流进行输出,输出完成后将自动关闭输入流和输出流
     * @param input     图片文件输入流
     * @param output    图片文件输出流
     * @param fileExtName   输出图片类型
     * @throws Exception    当输入流非图片或者图片绘制过程中可能有异常
     */
    public static void zoom(InputStream input, OutputStream output, String fileExtName) throws Exception {
        if (input == null || output == null) {
            return;
        }
        if (!isImageExtName(fileExtName)) {
            throw new Exception("输出的非图片文件类型");
        }
        try {
            // 读取原始图片
            BufferedImage rawImage = ImageIO.read(input);
            // 输出图片
            ImageIO.write(rawImage, fileExtName, output);
        } finally {
            if (input != null) {
                input.close();
            }
            if (output != null) {
                output.close();
            }
        }
    }

    /**
     * 根据图片按指定宽高强制缩放
     * 图片按照输出流进行输出,输出完成后将自动关闭输入流和输出流
     *
     * @param input     图片文件输入流
     * @param output    图片文件输出流
     * @param fileExtName   输出图片类型
     * @param width     输出图片的宽
     * @param height    输出图片的高
     * @param bgColor   输出图片填充背景色(为空时,不填充,按照指定宽高缩放;不为空时,按比例缩放到最合理的大小后填充颜色)
     * @throws Exception    当输入流非图片或者图片绘制过程中可能有异常
     */
    public static void resize(InputStream input, OutputStream output, String fileExtName, int width, int height, Color bgColor) throws Exception {
        if (width <= 0 && height <= 0) { // 按照原始尺寸输出
            zoom(input, output, fileExtName);
            return;
        }
        if (input == null || output == null) {
            return;
        }
        if (!isImageExtName(fileExtName)) {
            throw new Exception("输出的非图片文件类型");
        }

        Graphics g = null;
        try {
            // 读取原始图片
            BufferedImage rawImage = ImageIO.read(input);
            int imageType = rawImage.getType();
            // 目标图片创建
            BufferedImage destImage = new BufferedImage(width, height, imageType);
            // 获取画笔工具
            g = destImage.getGraphics();
            if (bgColor == null) { // 无填充背景色
                // 按指定大小强制缩放
                g.drawImage(rawImage, 0, 0, width, height, null);
            } else {
                // 设置背景色
                g.setColor(bgColor);
                // 必须调用这个方法将背景色填充到图片去
                g.fillRect(0, 0, width, height);
                // 获取按比例缩放的图片
                BufferedImage zoomedImage = getZoomedImage(rawImage, width, height);
                // 横纵坐标偏移量
                int x = (width - zoomedImage.getWidth()) / 2;
                int y = (height - zoomedImage.getHeight()) / 2;
                g.drawImage(zoomedImage, x, y, bgColor,null);
            }
            // 输出图片
            ImageIO.write(destImage, fileExtName, output);
        } finally {
            if (g != null) {
                g.dispose();
            }
            if (input != null) {
                input.close();
            }
            if (output != null) {
                output.close();
            }
        }
    }

    /**
     * 根据图片按指定宽高切割(图片最中间部分),并按照输出流进行输出,输出完成后将自动关闭输入流和输出流
     *
     * @param input     图片文件输入流
     * @param output    图片文件输出流
     * @param width     缩放图片的宽
     * @param height    缩放图片的高
     * @throws Exception    当输入流非图片或者图片绘制过程中可能有异常
     */
    public static void cut(InputStream input, OutputStream output, String fileExtName, int width, int height) throws Exception {
        if (width <= 0 && height <= 0) { // 按照原始尺寸输出
            zoom(input, output, fileExtName);
            return;
        }
        if (input == null || output == null) {
            return;
        }
        if (!isImageExtName(fileExtName)) {
            throw new Exception("输出的非图片文件类型");
        }

        try {
            // 读取原始图片
            BufferedImage rawImage = ImageIO.read(input);
            // 获取缩放的图片
            BufferedImage destImage = getZoomedImage(rawImage, width, height, false);

            int x = (destImage.getWidth() - width) / 2;
            int y = (destImage.getHeight() - height) / 2;
            // 输出图片
            ImageIO.write(destImage.getSubimage(x, y, width, height), fileExtName, output);
        } finally {
            if (input != null) {
                input.close();
            }
            if (output != null) {
                output.close();
            }
        }
    }

}
