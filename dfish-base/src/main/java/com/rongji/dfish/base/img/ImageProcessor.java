package com.rongji.dfish.base.img;

import com.rongji.dfish.base.util.LogUtil;
import com.rongji.dfish.base.util.ThreadUtil;
import com.rongji.dfish.base.util.Utils;

import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 图片处理工具类
 * @author lamontYu
 * @create 2019-12-16
 * @since
 */
public class ImageProcessor {

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
    private List<ImageHandler> handlers;

    /**
     * 构造函数
     * @param file 原始文件
     */
    public ImageProcessor(File file) {
        if (file == null) {
            throw new IllegalArgumentException("file == null");
        }
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
        this.handlers = new ArrayList<>();
    }


    /**
     *
     * @param file 原始文件
     * @return this
     */
    public static ImageProcessor of(File file) {
        return new ImageProcessor(file);
    }

    public String getDest() {
        return dest;
    }

    public ImageProcessor setDest(String dest) {
        this.dest = dest;
        return this;
    }

    public String getAliasPattern() {
        return aliasPattern;
    }

    public ImageProcessor setAliasPattern(String aliasPattern) {
        this.aliasPattern = aliasPattern;
        return this;
    }

    /**
     * 处理方案
     * @param width 宽度
     * @param height 高度
     * @param way 方式
     * @param alias 别名
     * @return this
     */
    public ImageProcessor handle(int width, int height, ImageHandleWay way, String alias) {
        return handle(new ImageHandler(width, height, way, alias));
    }

    /**
     * 处理方案
     * @param width 宽度
     * @param height 高度
     * @param way 方式
     * @param alias 别名
     * @param lazy 延迟处理
     * @return this
     */
    public ImageProcessor handle(int width, int height, ImageHandleWay way, String alias, boolean lazy) {
        return handle(new ImageHandler(width, height, way, alias, lazy));
    }

    /**
     * 处理方案
     * @param handler 处理方案定义
     * @return this
     */
    public ImageProcessor handle(ImageHandler handler) {
        if (handler == null) {
            throw new IllegalArgumentException("handler == null");
        }
        if (handler.getWay() == null) {
            throw new IllegalArgumentException("handler.way == null");
        }
        handlers.add(handler);
        return this;
    }

    private static ExecutorService EXECUTORS = ThreadUtil.getCachedThreadPool();

    /**
     * 开始执行处理方案
     * @return int 已处理完成数量
     * @throws Exception 处理过程可能抛出异常
     */
    public int execute() throws Exception {
        if (handlers.isEmpty()) {
            throw new UnsupportedOperationException("The method of handler must be called at least once.");
        }
        if (Utils.isEmpty(dest)) {
            throw new UnsupportedOperationException("The dest could not be null.");
        }
        Set<String> names = new HashSet<>(handlers.size());
        for (ImageHandler handler : handlers) {
            if (Utils.isEmpty(handler.getName())) {
                if (Utils.isEmpty(handler.getAlias()) || Utils.isEmpty(aliasPattern)) {
                    throw new UnsupportedOperationException("needs alias and the aliasPattern.");
                }
                String name = aliasPattern;
                name = name.replaceAll("\\{FILE_NAME\\}", fileName);
                name = name.replaceAll("\\{ALIAS\\}", handler.getAlias());
                name = name.replaceAll("\\{EXTENSION\\}", extension);
                handler.setName(name);
            }
            boolean add = names.add(handler.getName());
            if (!add) {
                throw new UnsupportedOperationException("The handler name has repeat.[" + handler.getName() + "]");
            }
        }
        AtomicInteger handleResult = new AtomicInteger(0);
        ImageOperation imageOperation = ImageOperation.of(new FileInputStream(file)).zoom(1.0).mark();
        List<Runnable> queue = new ArrayList<>(handlers.size());
        for (ImageHandler handler : handlers) {
            if (handler.isLazy()) {
                EXECUTORS.execute(() -> {
                    try {
                        execute(imageOperation.clone(), handler, handleResult);
                    } catch (Exception e) {
                        LogUtil.error(null, e);
                    }
                });
            } else {
                queue.add(()->{
                    try {
                        execute(imageOperation.clone(), handler, handleResult);
                    } catch (Exception e) {
                        LogUtil.error(null, e);
                    }
                });
            }
        }
        // 执行需要结果的线程
        ThreadUtil.execute(queue);
        return handleResult.get();
    }

    /**
     * 单个方案处理
     * @param imageOperation 处理工具类
     * @param handler 处理方案
     * @param handleResult 处理结果
     * @throws Exception
     */
    private void execute(ImageOperation imageOperation, ImageHandler handler, AtomicInteger handleResult) throws Exception {
        String filePath = this.dest + (this.dest.endsWith("/") ? "" : "/") + handler.getAlias();
        try(OutputStream output = new FileOutputStream(new File(filePath))) {
            switch (handler.getWay()){
                case CUT:
                    imageOperation.cut(handler.getWidth(), handler.getHeight());
                    break;
                case RESIZE:
                    imageOperation.resize(handler.getWidth(), handler.getHeight());
                    break;
                case ZOOM:
                    imageOperation.zoom(handler.getWidth(), handler.getHeight());
                    break;
                default:
            }
            imageOperation.output(output);
            handleResult.incrementAndGet();
        }
        imageOperation.reset();
    }

}
