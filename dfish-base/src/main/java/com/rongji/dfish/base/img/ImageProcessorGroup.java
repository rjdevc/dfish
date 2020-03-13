package com.rongji.dfish.base.img;

import com.rongji.dfish.base.util.LogUtil;
import com.rongji.dfish.base.util.ThreadUtil;
import com.rongji.dfish.base.util.Utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 图片处理工具类
 *
 * @author lamontYu
 * @date 2019-12-16
 * @since
 */
public class ImageProcessorGroup {

    /**
     * 原始文件
     */
    private File file;
    /**
     * 输出目标目录
     */
    private String dest;
    /**
     * 文件输出别名pattern
     * 目前支持别名规则有
     * {FILE_NAME}:原始文件名
     * {ALIAS}:输出文件别名
     * {EXTENSION}:文件扩展名
     */
    private String aliasPattern;
    /**
     * 原始文件名(不含扩展名)
     */
    private String fileName;
    /**
     * 原始文件扩展名
     */
    private String extension;
    /**
     * 图片处理
     */
    private List<ImageProcessConfig> processConfigs;

    /**
     * 构造函数
     *
     * @param file 原始文件
     */
    public ImageProcessorGroup(File file) {
        this.file = file;
        String originalFileName = file.getName();
        if (Utils.notEmpty(originalFileName)) {
            int dotIndex = originalFileName.indexOf(".");
            if (dotIndex >= 0) {
                this.fileName = originalFileName.substring(0, dotIndex);
                this.extension = originalFileName.substring(dotIndex + 1);
            } else {
                this.fileName = originalFileName;
                this.extension = "";
            }
        }
        this.processConfigs = new ArrayList<>();
    }


    /**
     * @param file 原始文件
     * @return this
     */
    public static ImageProcessorGroup of(File file) {
        return new ImageProcessorGroup(file);
    }

    /**
     * 输出目标目录
     * @return String
     */
    public String getDest() {
        return dest;
    }

    /**
     * 输出目标目录
     * @param dest
     * @return this
     */
    public ImageProcessorGroup setDest(String dest) {
        this.dest = dest;
        return this;
    }

    /**
     * 文件输出别名
     *  目前支持别名规则有
     * {FILE_NAME}:原始文件名
     * {ALIAS}:输出文件别名
     * {EXTENSION}:文件扩展名
     * @return String
     */
    public String getAliasPattern() {
        return aliasPattern;
    }

    /**
     * 文件输出别名
     * @param aliasPattern
     * @return this
     */
    public ImageProcessorGroup setAliasPattern(String aliasPattern) {
        this.aliasPattern = aliasPattern;
        return this;
    }

    /**
     * 处理方案
     *
     * @param width  宽度
     * @param height 高度
     * @param way    方式
     * @param alias  别名
     * @return this
     */
    public ImageProcessorGroup process(int width, int height, String way, String alias) {
        return process(new ImageProcessConfig(width, height, way, alias));
    }

    /**
     * 处理方案
     *
     * @param width  宽度
     * @param height 高度
     * @param way    方式
     * @param alias  别名
     * @param lazy   延迟处理
     * @return this
     */
    public ImageProcessorGroup process(int width, int height, String way, String alias, boolean lazy) {
        return process(new ImageProcessConfig(width, height, way, alias, lazy));
    }

    /**
     * 处理方案
     *
     * @param config 处理方案定义
     * @return this
     */
    public ImageProcessorGroup process(ImageProcessConfig config) {
        if (config == null) {
            throw new IllegalArgumentException("config == null");
        }
        if (config.getWay() == null) {
            throw new IllegalArgumentException("config.way == null");
        }
        processConfigs.add(config);
        return this;
    }

    private static ExecutorService LAZY_EXECUTOR = ThreadUtil.getCachedThreadPool();

    /**
     * 开始执行处理方案
     *
     * @return int 已处理完成数量
     * @throws Exception 处理过程可能抛出异常
     */
    public int execute() throws Exception {
        if (processConfigs.isEmpty()) {
            throw new UnsupportedOperationException("The method of config must be called at least once.");
        }
        if (Utils.isEmpty(dest)) {
            throw new UnsupportedOperationException("The dest could not be null.");
        }
        Set<String> names = new HashSet<>(processConfigs.size());
        for (ImageProcessConfig processConfig : processConfigs) {
            if (Utils.isEmpty(processConfig.getName())) {
                if (Utils.isEmpty(processConfig.getAlias()) || Utils.isEmpty(aliasPattern)) {
                    throw new UnsupportedOperationException("needs alias and the aliasPattern.");
                }
                String name = aliasPattern;
                name = name.replaceAll("\\{FILE_NAME\\}", fileName);
                name = name.replaceAll("\\{ALIAS\\}", processConfig.getAlias());
                name = name.replaceAll("\\{EXTENSION\\}", extension);
                processConfig.setName(name);
            }
            boolean add = names.add(processConfig.getName());
            if (!add) {
                throw new UnsupportedOperationException("The config name has repeat.[" + processConfig.getName() + "]");
            }
        }
        AtomicInteger processResult = new AtomicInteger(0);
        ImageProcessor imageProcessor = ImageProcessor.of(new FileInputStream(file)).mark();
        List<Runnable> queue = new ArrayList<>(processConfigs.size());
        List<Runnable> lazyQueue = new ArrayList<>(processConfigs.size());
        for (ImageProcessConfig processConfig : processConfigs) {
            if (processConfig.isLazy() && !processConfig.isMarkPoint()) {
                lazyQueue.add(() -> execute(imageProcessor, processConfig, processResult));
            } else {
                queue.add(() -> execute(imageProcessor, processConfig, processResult));
            }
        }
        // 执行需要结果的线程
        // FIXME 标记点一般设置在最前面,否则将影响其他图片的处理
        ThreadUtil.execute(queue);
        // 后台线程执行
        for (Runnable runnable : lazyQueue) {
            LAZY_EXECUTOR.execute(runnable);
        }
        return processResult.get();
    }

    /**
     * 单个方案处理
     *
     * @param imageProcessor 处理工具类
     * @param processConfig  处理配置
     * @param processResult  处理结果
     * @throws Exception
     */
    private void execute(ImageProcessor imageProcessor, ImageProcessConfig processConfig, AtomicInteger processResult) {
        if (!processConfig.isMarkPoint()) {
            imageProcessor = imageProcessor.clone();
        }
        String filePath = this.dest + (this.dest.endsWith("/") ? "" : "/") + processConfig.getName();
        try (OutputStream output = new FileOutputStream(new File(filePath))) {
            switch (processConfig.getWay()) {
                case ImageProcessConfig.WAY_CUT:
                    imageProcessor.zoomAndCut(processConfig.getWidth(), processConfig.getHeight());
                    break;
                case ImageProcessConfig.WAY_ZOOM:
                    imageProcessor.zoom(processConfig.getWidth(), processConfig.getHeight());
                    break;
                case ImageProcessConfig.WAY_RESIZE:
                    imageProcessor.resize(processConfig.getWidth(), processConfig.getHeight());
                    break;
                default:
            }
            ImageWatermarkConfig watermark = processConfig.getWatermark();
            // 处理水印
            if (watermark != null) {
                if (watermark.getImageFile() != null) {
                    // 优先使用图片
                    imageProcessor.watermark(ImageProcessor.of(new FileInputStream(watermark.getImageFile())), watermark.getImageAlpha(), watermark.getX(), watermark.getY());
                } else if (Utils.notEmpty(watermark.getText())) {
                    // 如果没有图片设置使用文字
                    imageProcessor.watermark(watermark.getText(), watermark.getTextFont(), watermark.getTextColor(), watermark.getX(), watermark.getY());
                }
            }
            if (processConfig.isMarkPoint()) {
                imageProcessor.mark();
            }
            imageProcessor.output(output);
            processResult.incrementAndGet();
        } catch (Exception e) {
            LogUtil.error("图片处理异常", e);
        }
    }

}
