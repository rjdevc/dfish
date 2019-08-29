package com.rongji.dfish.framework.plugin.code;

import com.rongji.dfish.base.DfishException;
import com.rongji.dfish.base.Utils;
import com.rongji.dfish.base.util.FileUtil;
import com.rongji.dfish.framework.SystemData;
import com.rongji.dfish.ui.AbstractNode;
import com.rongji.dfish.ui.widget.Img;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class JigsawGenerator {

    public static final String KEY_CHECKCODE = "com.rongji.dfish.CHECKCODE.JIGSAW";
    public static final String KEY_GENERATOR_COUNT = "com.rongji.dfish.CHECKCODE.COUNT";
    public static final String KEY_LOCK_TIME = "com.rongji.dfish.CHECKCODE.LOCK";

    /**
     * 大图宽度
     */
    private int bigWidth = 400;
    /**
     * 大图高度
     */
    private int bigHeight = 200;
    /**
     * 小图大小(正方形)
     */
    private int smallSize = 48;
    /**
     * 验证图片目录
     */
    private String imageFolder = "m/jigsaw/";
    /**
     * 误差范围
     */
    private double errorRange = 0.05;
    /**
     * 最大错误次数
     */
    private int maxErrorCount = 16;
    /**
     * 错误提示语
     */
    private String errorMsg = "次数过多,请稍后再试";
    /**
     * 锁定时间(单位:秒)
     */
    private int timeout = 60;


    /**
     * 大拼图缺口背景色
     */
    private Color gapsColor;

    public int getBigWidth() {
        return bigWidth;
    }

    public void setBigWidth(int bigWidth) {
        this.bigWidth = bigWidth;
    }

    public int getBigHeight() {
        return bigHeight;
    }

    public void setBigHeight(int bigHeight) {
        this.bigHeight = bigHeight;
    }

    public int getSmallSize() {
        return smallSize;
    }

    public void setSmallSize(int smallSize) {
        this.smallSize = smallSize;
    }

    public String getImageFolder() {
        return imageFolder;
    }

    public void setImageFolder(String imageFolder) {
        this.imageFolder = imageFolder;
    }

    public Color getGapsColor() {
        if (gapsColor == null) {
            gapsColor = new Color(255, 255, 255, 100);
        }
        return gapsColor;
    }

    public void setGapsColor(Color gapsColor) {
        this.gapsColor = gapsColor;
    }

    public double getErrorRange() {
        return errorRange;
    }

    public void setErrorRange(double errorRange) {
        this.errorRange = errorRange;
    }

    public int getMaxErrorCount() {
        return maxErrorCount;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public void setMaxErrorCount(int maxErrorCount) {
        this.maxErrorCount = maxErrorCount;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    private static final Random RANDOM = new Random();

    /**
     * 生成拼图
     * @param request
     * @return
     * @throws Exception
     */
    public JigsawData generatorJigsaw(HttpServletRequest request) throws Exception {
//        // 大图宽高都必须是小图的4倍
//        int bigMinSize = smallSize << 2;
//        if (smallSize <= 0 || bigWidth < bigMinSize || bigHeight < bigMinSize) {
//            throw new UnsupportedOperationException("图片切割大小图宽高设置不符合规范");
//        }
        HttpSession session = request.getSession();
        // 目前反暴力刷图策略暂时以session来判断,以后完善可以增加ip判断
        Integer generatorCount = (Integer) session.getAttribute(KEY_GENERATOR_COUNT);
        JigsawData jigsaw = new JigsawData();

        if (generatorCount == null) {
            generatorCount = 0;
        } else if (generatorCount >= maxErrorCount) {
            // FIXME 暂不做控制,需要和前端配合
            Long lastLockTime = (Long) session.getAttribute(KEY_LOCK_TIME);

            int leftTimeout = 0;
            if (lastLockTime == null) {
                session.setAttribute(KEY_LOCK_TIME, System.currentTimeMillis());
                leftTimeout = timeout;
            } else {
                // 剩余时间
                leftTimeout = (int) (timeout - (System.currentTimeMillis() - lastLockTime) / 1000);
                if (leftTimeout <= 0) {
                    generatorCount = 0;
                    session.removeAttribute(KEY_GENERATOR_COUNT);
                    session.removeAttribute(KEY_LOCK_TIME);
                }
            }
            if (leftTimeout > 0) {
                JigsawData.JigsawError error = new JigsawData.JigsawError();
                jigsaw.setError(error);
                error.setMsg(errorMsg);
                error.setTimeout(leftTimeout);
                return jigsaw;
            }
        }
        session.setAttribute(KEY_GENERATOR_COUNT, ++generatorCount);

        // 图片周边预留1/8的位置
        int minWidthPosition = bigWidth >> 3;
        int minHeightPosition = bigHeight >> 3;

        int maxWidth = bigWidth - (minWidthPosition << 1) - smallSize;
        int maxHeight = bigHeight - (minHeightPosition << 1) - smallSize;
        // 横坐标位置
        int x = minWidthPosition + RANDOM.nextInt(maxWidth);
        int y = minHeightPosition + RANDOM.nextInt(maxHeight);

        File imageDir = new File(getImageRawDir());
        if (!imageDir.exists()) {
            imageDir.mkdirs();
        }

        if (!imageDir.isDirectory()) {
            throw new DfishException("验证码拼图路径非目录");
        }
        File[] subFiles = imageDir.listFiles();
        List<File> imageFiles = new ArrayList<>();
        for (File file : subFiles) {
            if (file.isFile()) {
                imageFiles.add(file);
            }
        }
        if (Utils.isEmpty(imageFiles)) {
            throw new DfishException("验证码拼图缺少范例图片");
        }
        int fileIndex = RANDOM.nextInt(imageFiles.size());
        File rawFile = imageFiles.get(fileIndex);

        String jigsawFileName = request.getSession().getId() + "-" + System.currentTimeMillis();
        Img bigImg = generatorBigImage(jigsawFileName, rawFile, x, y, smallSize, smallSize);
        Img smallImg = generatorSmallImage(jigsawFileName, rawFile, x, y, smallSize, smallSize);
        // 将验证码放到session中
        request.getSession().setAttribute(KEY_CHECKCODE, x);

        jigsaw.setBig(bigImg);
        jigsaw.setSmall(smallImg);
        jigsaw.setMaxvalue(bigWidth);
        return jigsaw;
    }

    /**
     * 校验拼图是否正确
     * @param request
     * @param offset
     * @return boolean
     */
    public boolean checkJigsawOffset(HttpServletRequest request, Number offset) {
        double customOffset = offset.doubleValue() * (bigWidth - smallSize) / bigWidth;
        HttpSession session = request.getSession();
        Integer realOffset = (Integer) session.getAttribute(KEY_CHECKCODE);
        // 小于误差范围内都是校验成功
        boolean match = realOffset != null && Math.abs((customOffset - realOffset) / realOffset) <= errorRange;
        if (match) {
            // 匹配成功,清理数据
            session.removeAttribute(KEY_GENERATOR_COUNT);
            session.removeAttribute(KEY_LOCK_TIME);
            // KEY_CHECKCODE数据不能清理,需要二次验证
        }
        return match;
    }

    /**
     * 获取原始图片目录
     * @return
     */
    private String getImageRawDir() {
        String realServletPath = SystemData.getInstance().getServletInfo().getServletRealPath();
        if (!realServletPath.endsWith("/")) {
            realServletPath += "/";
        }
        if (Utils.notEmpty(imageFolder) && !imageFolder.endsWith("/")) {
            imageFolder += "/";
        }
        return realServletPath + imageFolder;
    }


    /**
     * 生成大拼图
     * @param jigsawFileName 会话编号
     * @param rawFile 原始文件
     * @param x 切块开始的横坐标
     * @param y 切块开始的纵坐标
     * @param width 切块宽度
     * @param height 切块高度
     * @return 大图片组件
     * @throws Exception
     */
    private Img generatorBigImage(String jigsawFileName, File rawFile, int x, int y, int width, int height) throws Exception {
        FileInputStream input = null;
        FileOutputStream output = null;
        try {
            input = new FileInputStream(rawFile);
            String fileExtName = FileUtil.getFileExtName(rawFile.getName());
            String destFileName = jigsawFileName + "-B" + fileExtName;
            File tempDestFile = getTempDestFile(destFileName);
            output = new FileOutputStream(tempDestFile);
            // 读取原始图片
            BufferedImage rawImage = ImageIO.read(input);

            Graphics g = rawImage.getGraphics();
//        Graphics2D g = rawImage.createGraphics();
//        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, 1.0f));

            g.setColor(getGapsColor());
            // 必须调用这个方法将背景色填充到图片去
            g.fillRect(x, y, width, height);

            ImageIO.write(rawImage, getRealExtName(fileExtName), output);
            g.dispose();

            return parseImg(output, destFileName, rawImage.getWidth(), rawImage.getHeight());
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
     * 生成小拼图
     * @param jigsawFileName 会话编号
     * @param rawFile 原始文件
     * @param x 切块开始的横坐标
     * @param y 切块开始的纵坐标
     * @param width 切块宽度
     * @param height 切块高度
     * @return 小图片组件
     * @throws Exception
     */
    private Img generatorSmallImage(String jigsawFileName, File rawFile, int x, int y, int width, int height) throws Exception {
        FileInputStream input = null;
        FileOutputStream output = null;
        try {
            input = new FileInputStream(rawFile);
            String fileExtName = FileUtil.getFileExtName(rawFile.getName());
            String destFileName = jigsawFileName + "-S" + fileExtName;
            File tempDestFile = getTempDestFile(destFileName);
            output = new FileOutputStream(tempDestFile);
            // 读取原始图片
            BufferedImage rawImage = ImageIO.read(input);

            BufferedImage subImage = rawImage.getSubimage(x, y, width, height);
//            BufferedImage destImage = new BufferedImage(width, rawImage.getHeight(), subImage.getType());
            Graphics2D g = subImage.createGraphics();
            BufferedImage destImage = g.getDeviceConfiguration().createCompatibleImage(width, rawImage.getHeight(), Transparency.TRANSLUCENT);
            g.dispose();

//            Graphics g2 = destImage.createGraphics();
            g = (Graphics2D)destImage.getGraphics();
            g.setColor(new Color(255, 255, 255, 0));
            // 必须调用这个方法将背景色填充到图片去
            g.fillRect(0, 0, destImage.getWidth(), destImage.getHeight());
//            g2.setColor(new Color(0, 0, 0, 0));
//            g2.fillRect(0, 0, destImage.getWidth(), destImage.getHeight());
            g.drawImage(subImage, 0, y, width, height, null);
            // 小图片周边虚化
            g.setColor(getGapsColor());
            // 虚化大小
            int blurSize = 1;
            // 上
            g.fillRect(0, y, width, blurSize);
            // 右
            g.fillRect(width-blurSize, y, blurSize, height);
            // 下
            g.fillRect(0, y+width-blurSize, width, blurSize);
            // 左
            g.fillRect(0, y, blurSize, height);

            g.dispose();
            // 输出图片
            ImageIO.write(destImage, getRealExtName(fileExtName), output);

            return parseImg(output, destFileName, width, height);
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
     * 获取真实的扩展名,不含.
     * @param fileExtName 真实的扩展名
     * @return String
     */
    private String getRealExtName(String fileExtName) {
        if (fileExtName != null && fileExtName.startsWith(".")) {
            return fileExtName.substring(1);
        }
        return fileExtName;
    }

    /**
     * 临时文件目录
     */
    private static final String FOLDER_TEMP = "temp";

    /**
     * 获取临时目标文件
     * @param destFileName 临时目标文件名
     * @return 临时目标文件
     * @throws Exception
     */
    private File getTempDestFile(String destFileName) throws Exception {
        String destDirPath = getImageRawDir()  + FOLDER_TEMP + "/";
        File destDir = new File(destDirPath);
        if (!destDir.exists()) {
            destDir.mkdirs();
        }
        File destFile = new File(destDirPath + destFileName);
        if (!destFile.exists()) {
            destFile.createNewFile();
        }
        return destFile;
    }

    private Img parseImg(FileOutputStream output, String destFileName, int width, int height) {
        Img img = new Img(imageFolder + FOLDER_TEMP + "/" + destFileName);
        img.setWidth(width);
        img.setHeight(height);
        return img;
    }

    /**
     * 拼图数据
     */
    public static class JigsawData extends AbstractNode {
        /**
         * 大图片
         */
        private Img big;
        /**
         * 小图片
         */
        private Img small;
        /**
         * 最小值
         */
        private Number minvalue;
        /**
         * 最大值
         */
        private Number maxvalue;

        private JigsawError error;

        public JigsawData() {
        }

        public JigsawData(Img big, Img small, Number minvalue, Number maxvalue) {
            this.big = big;
            this.small = small;
            this.minvalue = minvalue;
            this.maxvalue = maxvalue;
        }

        @Override
        public String getType() {
            return null;
        }

        public Img getBig() {
            return big;
        }

        public void setBig(Img big) {
            this.big = big;
        }

        public Img getSmall() {
            return small;
        }

        public void setSmall(Img small) {
            this.small = small;
        }

        public Number getMinvalue() {
            return minvalue;
        }

        public void setMinvalue(Number minvalue) {
            this.minvalue = minvalue;
        }

        public Number getMaxvalue() {
            return maxvalue;
        }

        public void setMaxvalue(Number maxvalue) {
            this.maxvalue = maxvalue;
        }

        public JigsawError getError() {
            return error;
        }

        public void setError(JigsawError error) {
            this.error = error;
        }

        public static class JigsawError {
            private String msg;
            private int timeout;

            public String getMsg() {
                return msg;
            }

            public JigsawError setMsg(String msg) {
                this.msg = msg;
                return this;
            }

            public int getTimeout() {
                return timeout;
            }

            public JigsawError setTimeout(int timeout) {
                this.timeout = timeout;
                return this;
            }
        }

    }

    /**
     * 拼图校验结果
     */
    public static class JigsawCheckData extends AbstractNode {
        public JigsawCheckData(boolean result) {
            this.result = result;
        }

        public JigsawCheckData(boolean result, String msg) {
            this.result = result;
            this.msg = msg;
        }

        @Override
        public String getType() {
            return null;
        }

        /**
         * 校验结果
         */
        private boolean result;
        /**
         * 校验信息
         */
        private String msg;

        public boolean isResult() {
            return result;
        }

        public JigsawCheckData setResult(boolean result) {
            this.result = result;
            return this;
        }

        public String getMsg() {
            return msg;
        }

        public JigsawCheckData setMsg(String msg) {
            this.msg = msg;
            return this;
        }
    }

}
