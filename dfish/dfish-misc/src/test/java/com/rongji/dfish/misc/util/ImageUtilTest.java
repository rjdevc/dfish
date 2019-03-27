package com.rongji.dfish.misc.util;

import com.rongji.dfish.base.util.FileUtil;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;

public class ImageUtilTest {

    public static void main(String[] args) throws Exception {

        String rawFileName = "jigsaw001";
        String fileExtName = "png";
        int width = 32;
        int height = 32;

//        test(rawFileName, fileExtName, version, width, height);
        int x = 200;
        int y = 120;
        String zoomedFileName = rawFileName+"-test";
//        File dir = new File("E:\\Pictures\\dev\\others\\test\\jigsaw-files\\");
//        for (File file : dir.listFiles()) {
//            if (file.isFile()) {
//                int dotIndex = file.getName().lastIndexOf(".");
//                String fileName = file.getName().substring(0, dotIndex);
//                resize(fileName, fileName + "-test", fileExtName, 800, 400, null);
//            }
//        }
        alphaComposite(zoomedFileName, rawFileName+"-big", fileExtName, x, y, width, height);
//        subCut(zoomedFileName, rawFileName+"-small", fileExtName, x, y, width, height);

    }
    
    public static final String TEST_DIR = "E:\\Pictures\\dev\\others\\test\\jigsaw-files\\";

    public static void test(String rawFileName, String fileExtName, String version, int width, int height) throws Exception{
        int testCount = 1;

        long begin = System.currentTimeMillis();

        File rawFile = new File(TEST_DIR + rawFileName + "." + fileExtName);

        for (int i = 0; i < testCount; i++) {
            zoom(rawFileName, rawFileName + "-zoom", fileExtName, width, height);
            resize(rawFileName, rawFileName + "-zoom", fileExtName, width, height, null);
            resize(rawFileName, rawFileName + "-zoom", fileExtName, width, height, new Color(0, 0, 0, 0));
            cut(rawFileName, rawFileName + "-zoom", fileExtName, width, height);

        }
        long end = System.currentTimeMillis();
        System.out.println("原始文件大小[" + rawFile.length() / 1024 + "K]运行" + testCount + "次共耗时" + (end - begin) + "ms");
        File destFile = new File(TEST_DIR + rawFileName + "-resize-" + version +"." + fileExtName);
        System.out.println("目标文件[" + destFile.getName() + "]大小[" + destFile.length() / 1024 + "K]");

    }

    public static void zoom(String rawFileName, String destFileName, String fileExtName, int width, int height) throws Exception {
        File rawFile = new File(TEST_DIR + rawFileName + "." + fileExtName);
        InputStream input = new FileInputStream(rawFile);
        OutputStream output = new FileOutputStream(new File(TEST_DIR + destFileName + "." + fileExtName));
        ImageUtil.zoom(input, output, fileExtName, width, height);
    }

    public static void resize(String rawFileName, String destFileName, String fileExtName, int width, int height, Color bgColor) throws Exception {
        File rawFile = new File(TEST_DIR + rawFileName + "." + fileExtName);
        InputStream input = new FileInputStream(rawFile);
        OutputStream output = new FileOutputStream(new File(TEST_DIR + destFileName + "." + fileExtName));
        ImageUtil.resize(input, output, fileExtName, width, height, bgColor);
    }

    public static void cut(String rawFileName, String destFileName, String fileExtName, int width, int height) throws Exception {
        File rawFile = new File(TEST_DIR + rawFileName + "." + fileExtName);
        InputStream input = new FileInputStream(rawFile);
        OutputStream output = new FileOutputStream(new File(TEST_DIR + destFileName + "." + fileExtName));
        ImageUtil.cut(input, output, fileExtName, width, height);
    }

    public static void subCut(String rawFileName, String destFileName, String fileExtName, int x, int y, int width, int height) throws Exception {
        File rawFile = new File(TEST_DIR + rawFileName + "." + fileExtName);
        FileInputStream input = new FileInputStream(rawFile);
        FileOutputStream output = new FileOutputStream(new File(TEST_DIR + destFileName + "." + fileExtName));
        try {
            // 读取原始图片
            BufferedImage rawImage = ImageIO.read(input);

            BufferedImage subImage = rawImage.getSubimage(x, y, width, height);
//            BufferedImage destImage = new BufferedImage(width, rawImage.getHeight(), subImage.getType());
            Graphics2D g = subImage.createGraphics();
            BufferedImage destImage = g.getDeviceConfiguration().createCompatibleImage(width, rawImage.getHeight(), Transparency.TRANSLUCENT);
            g.dispose();

//            Graphics g2 = destImage.createGraphics();
            g = (Graphics2D)destImage.getGraphics();
            g.setColor(new Color(0, 0, 0, 0));
            // 必须调用这个方法将背景色填充到图片去
            g.fillRect(0, 0, destImage.getWidth(), destImage.getHeight());
//            g2.setColor(new Color(0, 0, 0, 0));
//            g2.fillRect(0, 0, destImage.getWidth(), destImage.getHeight());
            g.drawImage(subImage, 0, y, width, height, null);

            g.dispose();
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

    public static void alphaComposite(String rawFileName, String destFileName, String fileExtName, int x, int y, int width, int height) throws Exception {
        File rawFile = new File(TEST_DIR + rawFileName + "." + fileExtName);
        FileInputStream input = new FileInputStream(rawFile);
        FileOutputStream output = new FileOutputStream(new File(TEST_DIR + destFileName + "." + fileExtName));
        // 读取原始图片
        BufferedImage rawImage = ImageIO.read(input);
        Graphics g = rawImage.getGraphics();
//        Graphics2D g = rawImage.createGraphics();
//        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, 1.0f));

        g.setColor(new Color(255, 255, 255, 200));
        // 必须调用这个方法将背景色填充到图片去
        g.fillRect(x, y, width, height);


        ImageIO.write(rawImage, fileExtName, output);
        g.dispose();
    }

}