package com.rongji.dfish.framework.plugin.code;

import com.rongji.dfish.base.Utils;
import com.rongji.dfish.framework.SystemData;
import com.rongji.dfish.framework.plugin.code.dto.JigsawImgItem;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.awt.*;
import java.io.File;
import java.util.Random;

/**
 * 滑动验证码图片工具抽象类,这里将实现公用方法
 * @param <T>
 */
public abstract class AbstractJigsawGenerator <T extends AbstractJigsawGenerator<T>> implements JigsawGenerator {
    protected static final String KEY_CAPTCHA_COUNT = "com.rongji.dfish.CAPTCHA.COUNT";
    protected static final String KEY_CAPTCHA_LOCK = "com.rongji.dfish.CAPTCHA.LOCK";

    /**
     * 大图宽度
     */
    protected int bigWidth = 400;
    /**
     * 大图高度
     */
    protected int bigHeight = 200;
    /**
     * 小图大小(正方形)
     */
    protected int smallSize = 64;
    /**
     * 验证图片目录
     */
    protected String imageFolder = "x/jigsaw/";
    /**
     * 误差范围
     */
    protected double errorRange = 0.05;
    /**
     * 最大错误次数
     */
    protected int maxErrorCount = 16;
    /**
     * 错误提示语
     */
    protected String errorMsg = "次数过多,请稍后再试";
    /**
     * 锁定时间(单位:毫秒)
     */
    protected long timeout = 60000L;


    /**
     * 大拼图缺口背景色
     */
    protected Color gapColor = new Color(255, 255, 255, 220);

    /**
     * 大拼图宽度
     * @return int
     */
    public int getBigWidth() {
        return bigWidth;
    }

    /**
     * 大拼图宽度
     * @param bigWidth int
     * @return 本身，这样可以继续设置其他属性
     */
    public T setBigWidth(int bigWidth) {
        this.bigWidth = bigWidth;
        return (T)this;
    }

    /**
     * 大拼图高度
     * @return int
     */
    public int getBigHeight() {
        return bigHeight;
    }

    /**
     * 大拼图高度
     * @param bigHeight int
     * @return 本身，这样可以继续设置其他属性
     */
    public T setBigHeight(int bigHeight) {
        this.bigHeight = bigHeight;
        return (T)this;
    }

    /**
     * 小拼图大小(正方形)
     * @return int
     */
    public int getSmallSize() {
        return smallSize;
    }

    /**
     * 小拼图大小(正方形)
     * @param smallSize int
     * @return 本身，这样可以继续设置其他属性
     */
    public T setSmallSize(int smallSize) {
        this.smallSize = smallSize;
        return (T)this;
    }

    /**
     * 图片候选目录
     * @return String
     */
    public String getImageFolder() {
        return imageFolder;
    }

    /**
     * 图片候选目录
     * @param imageFolder String
     * @return 本身，这样可以继续设置其他属性
     */
    public T setImageFolder(String imageFolder) {
        this.imageFolder = imageFolder;
        return (T)this;
    }

    /**
     * 空隙颜色
     * @return Color
     */
    public Color getGapColor() {
        return gapColor;
    }

    /**
     * 空隙颜色
     * @param gapColor Color
     * @return 本身，这样可以继续设置其他属性
     */
    public T setGapColor(Color gapColor) {
        this.gapColor = gapColor;
        return (T)this;
    }

    /**
     * 容错精度范围
     * @return double
     */
    public double getErrorRange() {
        return errorRange;
    }

    /**
     * 容错精度范围
     * @param errorRange double
     * @return 本身，这样可以继续设置其他属性
     */
    public T setErrorRange(double errorRange) {
        this.errorRange = errorRange;
        return (T)this;
    }

    /**
     * 最大错误次数
     * @return int
     */
    public int getMaxErrorCount() {
        return maxErrorCount;
    }

    /**
     * 最大错误次数
     * @param maxErrorCount int
     * @return 本身，这样可以继续设置其他属性
     */
    public T setMaxErrorCount(int maxErrorCount) {
        this.maxErrorCount = maxErrorCount;
        return (T)this;
    }

    /**
     * 错误信息
     * @return String
     */
    public String getErrorMsg() {
        return errorMsg;
    }

    /**
     * 错误信息
     * @param errorMsg String
     * @return 本身，这样可以继续设置其他属性
     */
    public T setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
        return (T)this;
    }

    /**
     * 超限后需要等待的时长(毫秒)
     * @return long
     */
    public long getTimeout() {
        return timeout;
    }

    /**
     * 超限后需要等待的时长(毫秒)
     * @param timeout long
     * @return 本身，这样可以继续设置其他属性
     */
    public T setTimeout(long timeout) {
        this.timeout = timeout;
        return (T)this;
    }

    protected static final Random RANDOM = new Random();


    /**
     * 校验拼图是否正确
     *
     * @param request 请求
     * @param offsetStr 偏移量
     * @return boolean 是否验证通过
     */
    public boolean checkJigsawOffset(HttpServletRequest request, String offsetStr) {
        if (Utils.isEmpty(offsetStr)) {
            return false;
        }
        try {
            Double offset = Double.parseDouble(offsetStr);
            return checkJigsawOffset(request, offset);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 校验拼图是否正确
     *
     * @param request 请求
     * @param offset 偏移量
     * @return boolean 是否验证通过
     */
    public boolean checkJigsawOffset(HttpServletRequest request, Number offset) {
        return checkJigsawOffset(request, offset, false);
    }

    @Override
    public boolean checkJigsawOffset(HttpServletRequest request, Number offset, boolean retainCaptcha) {
        if (offset == null) {
            return false;
        }
        double customOffset = offset.doubleValue() * (bigWidth - smallSize) / bigWidth;
        HttpSession session = request.getSession();
        // KEY_CAPTCHA,需要二次验证
        Integer realOffset = (Integer) session.getAttribute(KEY_CAPTCHA);
        // 小于误差范围内都是校验成功
        boolean match = realOffset != null && Math.abs((customOffset - realOffset) / realOffset) <= errorRange;
        if (match) {
            // 匹配成功,清理错误计数数据
            session.removeAttribute(KEY_CAPTCHA_COUNT);
            session.removeAttribute(KEY_CAPTCHA_LOCK);
        }
        if (!match || !retainCaptcha) {
            // 验证不通过,或不保留验证码,需要将验证码清理,防止暴力破解
            request.getSession().removeAttribute(KEY_CAPTCHA);
        }
        return match;
    }

    /**
     * 获取原始图片目录
     *
     * @return Strnig
     */
    protected String getImageRawDir() {
//        ClassLoader.getSystemResource("").getPath();
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
     * 获取真实的扩展名,不含.
     *
     * @param fileExtName 真实的扩展名
     * @return String
     */
    protected String getRealExtName(String fileExtName) {
        if (fileExtName != null && fileExtName.startsWith(".")) {
            return fileExtName.substring(1);
        }
        return fileExtName;
    }

    /**
     * 临时文件目录
     */
    protected static final String FOLDER_TEMP = "temp";

    /**
     * 获取临时目标文件
     *
     * @param destFileName 临时目标文件名
     * @return 临时目标文件
     * @throws Exception
     */
    protected File getTempDestFile(String destFileName) throws Exception {
        String destDirPath = getImageRawDir() + FOLDER_TEMP + "/";
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

    protected JigsawImgItem parseImg(String destFileName, int width, int height) {
        return new JigsawImgItem(imageFolder + FOLDER_TEMP + "/" + destFileName, width, height);
    }
}
