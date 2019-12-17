package com.rongji.dfish.base.util.img;

import com.rongji.dfish.base.util.LogUtil;
import com.rongji.dfish.base.util.ThreadUtil;
import com.rongji.dfish.base.util.Utils;
import sun.plugin2.util.NativeLibLoader;

import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author lamontYu
 * @create 2019-12-16
 * @since
 */
public class ImageProcessor {

    private File file;
    private String dest;
    private String aliasPattern;
    private String fileName;
    private String extension;
    private List<ImageHandler> handlers;

    public ImageProcessor(File file) {
        if (file == null) {
            throw new IllegalArgumentException("The file could not be null.");
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


    public static ImageProcessor of(File file) throws FileNotFoundException {
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

    public ImageProcessor handle(int width, int height, ImageHandleWay way, String alias) {
        return handle(new ImageHandler(width, height, way, alias));
    }

    public ImageProcessor handle(int width, int height, ImageHandleWay way, String alias, boolean lazy) {
        return handle(new ImageHandler(width, height, way, alias, lazy));
    }

    public ImageProcessor handle(ImageHandler handler) {
        if (handler == null) {
            throw new IllegalArgumentException("handler could not be null");
        }
        handlers.add(handler);
        return this;
    }

    private static ExecutorService EXECUTORS = ThreadUtil.getCachedThreadPool();

    public int execute() throws Exception {
        if (handlers.isEmpty()) {
            throw new UnsupportedOperationException("The processor needs at least one handler.");
        }
        if (Utils.isEmpty(dest)) {
            throw new UnsupportedOperationException("The destination can not be null.");
        }
        Set<String> names = new HashSet<>(handlers.size());
        for (ImageHandler handler : handlers) {
            if (handler.getWay() == null) {
                throw new UnsupportedOperationException("The handler way is null.");
            }

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
        for (ImageHandler handler : handlers) {
//            if (handler.isLazy()) {
//                EXECUTORS.execute(() -> {
//                    try {
//                        execute(imageOperation.clone(), handler, handleResult);
//                    } catch (Exception e) {
//                        LogUtil.error(null, e);
//                    }
//                });
//            } else {
//                execute(imageOperation, handler, handleResult);
//            }
            execute(imageOperation, handler, handleResult);
        }
        return handleResult.get();
    }

    private void execute(ImageOperation imageOperation, ImageHandler handler, AtomicInteger handleResult) throws Exception {
        String filePath = this.dest + (this.dest.endsWith("/") ? "" : "/") + handler.getAlias();
        try(OutputStream output = new FileOutputStream(new File(filePath))) {
            if (ImageHandleWay.ZOOM.equals(handler.getWay())) {
                imageOperation.zoom(handler.getWidth(), handler.getHeight());
            } else if (ImageHandleWay.CUT.equals(handler.getWay())) {
                imageOperation.cut(handler.getWidth(), handler.getHeight());
            } else if (ImageHandleWay.RESIZE.equals(handler.getWay())) {
                imageOperation.resize(handler.getWidth(), handler.getHeight());
            }
            imageOperation.output(output);
            handleResult.incrementAndGet();
        }
        imageOperation.reset();
    }

}
