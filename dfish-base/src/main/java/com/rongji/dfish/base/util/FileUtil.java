package com.rongji.dfish.base.util;

import java.io.*;
import java.nio.file.CopyOption;
import java.nio.file.Path;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;


/**
 * <p>
 * Title: 榕基I-TASK执行先锋
 * </p>
 *
 * <p>
 * Description: 专门为提高企业执行力而设计的产品
 * </p>
 *
 * <p>
 * Copyright: Copyright (c) 2006
 * </p>
 *
 * <p>
 * Company: 榕基软件开发有限公司
 * </p>
 * FileOperMethods 用于提炼一些文件公用方法供调用
 *
 * @author I-TASK成员: LinLW
 * @version 2.1
 */
public final class FileUtil {
    /**
     * 服务器端字符集
     */
    public static final String ENCODING = "UTF-8";
    /**
     * 客户端字符集(默认，客户端WINDOWS系统居多)
     */
    public static final String CLIENT_ENCODING = "GBK";

    private FileUtil() {
    }

    /**
     * 删除文件或文件夹及该文件夹子树
     *
     * @param path 文件目录名
     */
    public static void deleteFileTree(String path) {
        File root = new File(path);
        deleteFileTree(root);
    }

    /**
     * 删除文件或文件夹及该文件夹子树
     *
     * @param file File
     */
    public static void deleteFileTree(File file) {
        if (file.exists()) {
            if (file.isDirectory()) {
                File[] sonFiles = file.listFiles();
                if (sonFiles != null && sonFiles.length > 0) {
                    for (int i = 0; i < sonFiles.length; i++) {
                        deleteFileTree(sonFiles[i]);
                    }
                }
            }
            file.delete();
        }
    }

    /**
     * 删除文件
     *
     * @param filePath String
     */
    public static void deleteFile(String filePath) {
        File f = new File(filePath);
        if (f.exists() && f.isFile()) {
            f.delete();
        }
    }

    /**
     * 如果有扩展名,包括点号 如 .txt .doc等.否则返回""
     *
     * @param fileName String
     * @return String
     */
    public static String getFileExtName(String fileName) {
        if (Utils.isEmpty(fileName) || fileName.indexOf('.') < 0) {
            return "";
        }
        int i = fileName.lastIndexOf(".");
        return fileName.substring(i);
    }

    /**
     * 如果有扩展名,包括点号 如 txt doc等(注意这里不带.).否则返回""
     *
     * @param fileName String
     * @return String
     */
    public static String getExtension(String fileName) {
        if (Utils.isEmpty(fileName)) {
            return "";
        }
        int lastIndex = fileName.lastIndexOf(".");
        if (lastIndex < 0) {
            return "";
        }
        return fileName.substring(lastIndex + 1);
    }

    /**
     * 从inputStrean取得内容，注意没有关闭该inputStream
     *
     * @param inputStream InputStream
     * @return byte[]
     * @throws IOException
     */
    public static byte[] getBytes(InputStream inputStream) throws IOException {
        BufferedInputStream bufferedInputStream = new BufferedInputStream(
                inputStream);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(
                1024);
        byte[] block = new byte[8192];
        while (true) {
            int readLength = bufferedInputStream.read(block);
            if (readLength == -1) {
                break; // end of file
            }
            byteArrayOutputStream.write(block, 0, readLength);
        }
        byte[] retValue = byteArrayOutputStream.toByteArray();
        byteArrayOutputStream.close();
        return retValue;
    }


    /**
     * 将上传文件保存成硬盘文件
     *
     * @param input        InputStream
     * @param folderPath   String
     * @param realFileName String
     */
    public static void saveFile(InputStream input, String folderPath, String realFileName) throws IOException {
//		InputStream stream = null;
        // 接收到的附件数据流


        // 附件保存的相对位置

        String attachUrl = folderPath + "/" + realFileName;

        // 如果绝对目录不存在，新建目录
        File filePath = new File(folderPath
                .replace('/', File.separatorChar));

        if (!filePath.exists()) {
            filePath.mkdirs();
        }
        File targetFile = new File(attachUrl.replace('/',
                File.separatorChar));
        if (targetFile.exists()) {
            targetFile.delete(); // 由于有些时候文件大小问题。
        }
        targetFile = new File(attachUrl.replace('/', File.separatorChar));
        try (OutputStream bos = new FileOutputStream(targetFile)) {
            // 将数据流写入文件
            int bytesRead = 0;
            byte[] buffer = new byte[8192];

            while ((bytesRead = input.read(buffer, 0, 8192)) != -1) {
                bos.write(buffer, 0, bytesRead);
            }
        } catch (Exception e) {
            throw e;
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException ex) {
                    LogUtil.error(null, ex);
                }
            }
        }
    }

    /**
     * 进行流的读写操作
     *
     * @param input  InputStream
     * @param output OutputStream
     * @throws IOException
     */
    public static void readAndWrite(InputStream input, OutputStream output) throws IOException {
        try {
            if (input != null && output != null) {
                byte[] buff = new byte[8192];
                int bytesRead;
                while (-1 != (bytesRead = input.read(buff, 0, buff.length))) {
                    output.write(buff, 0, bytesRead);
                }
            }
        } catch (IOException e) {
            throw e;
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
     * 写文件
     *
     * @param content  String
     * @param fileName String
     * @param charset  String
     * @return boolean
     */
    public static boolean writeFile(String content, String fileName, String charset) throws IOException {
        return writeFile(content, ((fileName == null) ? null : new File(fileName)), charset, false);
    }

    /**
     * 按文本格式读取文件
     *
     * @param file    File
     * @param charset String
     * @return String
     */
    public static String readFileAsText(File file, String charset) {
        FileInputStream fis = null;
        BufferedInputStream bis = null;
        byte[] content = null;
        try {
            if (file == null || !file.exists()) {
                return null;
            }
            content = new byte[(int) file.length()];

            fis = new FileInputStream(file);
            bis = new BufferedInputStream(fis);
//            if ((bis.read(content)) != -1) {
//                ;
//            }
            return new String(content, charset);
        } catch (IOException e) {
            LogUtil.error("FileUtil.readFileAsText error", e);
        } finally {
            try {
                if (fis != null) {
                    if (bis != null) {
                        bis.close();
                    }
                    fis.close();
                }
            } catch (Exception ex) {
            }
        }
        return null;
    }

    /**
     * 写文件
     *
     * @param content String
     * @param file    File
     * @param charset String
     * @param append  boolean
     * @return boolean
     */
    public static boolean writeFile(String content, File file, String charset, boolean append) throws IOException {
        Writer writer = null;
        FileOutputStream fos = null;

        try {
            String filep = file.getAbsolutePath();
            filep = filep.substring(0, filep.lastIndexOf(File.separator));
            File filePath = new File(filep);

            if (!filePath.exists()) {
                filePath.mkdirs();
            }

            writer = new OutputStreamWriter(fos = new FileOutputStream(file, append), charset);
            writer.write(content);

            return true;
        } catch (IOException e) {
            throw e;
        } finally {
            try {
                if (fos != null) {
                    if (writer != null) {
                        writer.close();
                    }
                    fos.close();
                }
            } catch (Exception ex) {
                LogUtil.error(null, ex);
            }
        }
    }

    /**
     * 写文件
     *
     * @param content  String
     * @param fileName String
     * @return boolean
     */
    public static boolean writeFile(String content, String fileName) throws IOException {
        return writeFile(content, new File(fileName), ENCODING, false);
    }

    /**
     * 截取文件名称防止乱码出现。
     *
     * @param saveAs String
     * @return String
     */
    public static String getSafeFileName(String saveAs) {
        try {
            return new String(saveAs.getBytes(CLIENT_ENCODING), "ISO8859-1");
        } catch (UnsupportedEncodingException e) {
            try {
                return new String(saveAs.getBytes(), "ISO8859-1");
            } catch (UnsupportedEncodingException e1) {
                LogUtil.error(null, e1);
                return saveAs;
            }
        }
    }

    // /**
    // * 把需要压缩的多个文件URL(完整路径+文件名)形成一个数组，传入
    // * 传入生成的ZIP文件URL(完整路径+文件名)传入。
    // * 如果ZIP 文件名不合法会压缩失败。
    // * 如果某个文件名无效，会在ZIP文件中添加一个空文件。
    // * 压成的 ZIP 中的都在根目录下，没有分层。
    // * @param fileUrls String[]
    // * @param zipUrl String
    // */
    // public static void compress(String[] fileUrls, String zipUrl) {
    // BufferedInputStream bis = null;
    // BufferedOutputStream bos = null;
    // File f = new File(zipUrl);
    //
    // if (!f.exists()) {
    // try {
    // f.createNewFile();
    // }
    // catch (IOException ex) {
    // ex.printStackTrace();
    // }
    // }
    //
    // ZipOutputStream zipos = null;
    //
    // try {
    // zipos = new ZipOutputStream(new FileOutputStream(f));
    // }
    // catch (FileNotFoundException ex) {
    // ex.printStackTrace();
    // }
    //
    // zipos.setMethod(ZipOutputStream.DEFLATED); //设置压缩方法
    //
    // for (int i = 0; i < fileUrls.length; i++) {
    // try {
    // //创建一个文件实例
    // //用文件输出流构建ZIP压缩输出流
    // String fileUrl = fileUrls[i].replace('/', File.separatorChar);
    //
    // //fileUrl = fileUrl.replaceAll("\\",File.pathSeparator);
    // String fileName = fileUrl.substring(fileUrl.lastIndexOf(
    // File.separator) + 1);
    //
    // ZipEntry entry = new ZipEntry(fileName);
    // zipos.putNextEntry(entry);
    // bos = new BufferedOutputStream(zipos);
    // bis = new BufferedInputStream(new FileInputStream(
    // new File(fileUrl)));
    //
    // byte[] buff = new byte[2048];
    // int bytesRead;
    //
    // while ( -1 != (bytesRead = bis.read(buff, 0, buff.length))) { //获取压缩文件的长度
    // bos.write(buff, 0, bytesRead);
    // }
    // }
    // catch (IOException ioe) {
    // ioe.printStackTrace();
    // }
    // finally {
    // try {
    // zipos.closeEntry();
    // bis.close();
    // bos.close();
    // }
    // catch (IOException ex) {
    // ex.printStackTrace();
    // }
    // }
    // }
    // }

    /**
     * 把XXX字节表达成xxxKB xxxMB XXXGB的格式
     *
     * @param l long
     * @return String
     */
    public static String getHumanSize(long l) {
        if (l < 1024L) {
            return l + " B";
        }
        if (l < 10240L) {
            // 10K内显示2位有效数字
            return String.valueOf(((double) l / 1024)).substring(0, 3) + " KB";
        }
        if (l < 1048576L) {
            // 10K上显示KB
            return (l >> 10) + " KB";
        }
        if (l < 10485760L) {
            // 10M内显示2位有效数字
            return String.valueOf(((double) l / 1048576L)).substring(0, 3) + " MB";
        }
        if (l < 1073741824L) {
            // 10M上显示MB
            return (l >> 20) + " MB";
        }
        if (l < 10737418240L) {
            // 10G内显示2位有效数字
            return String.valueOf(((double) l / 1073741824L)).substring(0, 3) + " GB";
        }
        return (l >> 30) + " GB";
    }

    /**
     * 把一个目录压缩到ZIP文件里面zip里的文件结构和目录一致。 注意，由于JDK固有的缺陷。不能包含中文文件名。
     *
     * @param path           File 要压缩的文件或文件名
     * @param zos            ZipOutputStream 要压缩到的zip的输出流
     * @param pathInZip      String 在zip里面的根文件路径。如果为空则文件夹内容直接散在跟路径下(如winRAR选中多个文件夹压缩的方式)
     *                       如果需要把把文件夹做为ZIP里面的根文件夹(如winRAR选中一个文件夹压缩的方式)则需要传入文件夹的名称
     *                       也可以用其他名称。
     * @param fileNameFilter FileFilter 用指定的过滤条件使某些文件不添加到zip包里。参见java.io.FileFilter
     * @throws IOException
     */
    public static void addDirectoryToZip(File path, ZipOutputStream zos, String pathInZip, FileFilter fileNameFilter) throws IOException {
        if (pathInZip == null || "/".equals(pathInZip) || "\\".equals(pathInZip)) {
            pathInZip = "";
        }
        if (fileNameFilter != null && !fileNameFilter.accept(path)) {
            return;
        }
        if (path.isDirectory()) {
            File[] subs = path.listFiles();
            if (subs != null) {
                for (int i = 0; i < subs.length; i++) {
                    if ("".equals(pathInZip)) {
                        addDirectoryToZip(subs[i], zos, subs[i].getName(), fileNameFilter);
                    } else {
                        addDirectoryToZip(subs[i], zos, pathInZip + "/" + subs[i].getName(), fileNameFilter);
                    }
                }
            }
        } else if (path.isFile()) {
            try (InputStream input = new FileInputStream(path)) {
                addDataToZipASFile(zos, pathInZip, input); // 如果是文件则添加
            }
        }
    }

    /**
     * 把某个文件/或数据添加到zip中去
     *
     * @param zos               ZipOutputStream
     * @param fileFullNameInZip String
     * @param data              InputStream
     * @throws IOException
     */
    public static void addDataToZipASFile(ZipOutputStream zos, String fileFullNameInZip, InputStream data) throws IOException {
        ZipEntry entry = new ZipEntry(fileFullNameInZip);
        zos.putNextEntry(entry);
        byte[] buff = new byte[8192];
        int bytesRead;
        while (-1 != (bytesRead = data.read(buff, 0, buff.length))) { // 获取压缩文件的长度
            zos.write(buff, 0, bytesRead);
        }
        zos.closeEntry();
        zos.flush();
    }

    /**
     * 用流方式 深度拷贝文件。
     * 不过,如果环境是 JDK7以上 通常建议用操作系统的拷贝会更快。
     *
     * @param fromFileFullName String
     * @param toFileFolder     String
     * @param toFileName       String
     * @return 是否成功
     * @see java.nio.file.Files#copy(Path, Path, CopyOption...)
     */
    public static boolean copyFile(String fromFileFullName, String toFileFolder, String toFileName) throws IOException {

        File attachFile = new File(fromFileFullName.replace('/', File.separatorChar));
        if (!attachFile.exists() || !attachFile.isFile()) {
            return false;
        }

        File file = new File(toFileFolder);
        if (!file.exists()) {
            file.mkdirs();
        }
        try (InputStream input = new FileInputStream(attachFile);
             OutputStream output = new FileOutputStream((toFileFolder + "/" + toFileName).replace('/', File.separatorChar))) {

            int r = 0;
            byte[] buffer = new byte[8192];
            while ((r = input.read(buffer, 0, 8192)) != -1) {
                output.write(buffer, 0, r);
            }
        }
        return true;
    }

    /**
     * 判断扩展名是否支持
     *
     * @param extName     拓展名(不管有没.都支持;即doc和.doc)
     * @param acceptTypes 可接受的类型;格式如:*.doc;*.png;*.jpg;
     * @return boolean 拓展名是否匹配
     */
    public static boolean accept(String extName, String acceptTypes) {
        if (acceptTypes == null || "".equals(acceptTypes)) {
            return true;
        }
        if (Utils.isEmpty(extName)) {
            return false;
        }
        // 这里的extName是包含.
        String[] accepts = acceptTypes.split("[,;]");
//		extName=extName.toLowerCase();
        // 类型是否含.
        int extDot = extName.lastIndexOf(".");
        // 统一去掉.
        String realExtName = (extDot >= 0) ? extName.substring(extDot + 1) : extName;
        for (String s : accepts) {
            if (Utils.isEmpty(s)) {
                continue;
            }
            int dotIndex = s.lastIndexOf(".");
            if (dotIndex < 0) {
                continue;
            }
            String acc = s.substring(dotIndex + 1);
            if (acc.equalsIgnoreCase(realExtName)) {
                return true;
            }
        }
        return false;
    }

//    private static void LogUtil.error(String log, Throwable t) {
//        if (log == null) {
//            log = "";
//        }
//        boolean LogUtil.error = true;
//        if (t != null) {
//            // FIXME 这里客户端异常不打算依赖包,所以通过字符判断;目前仅考虑tomcat的情况,其他web容器还需再验证测试
//            LogUtil.error = !"ClientAbortException".equals(t.getClass().getSimpleName());
//            if (LogUtil.error) {
//                log += "[" + t.getClass().getName() + ":" + t.getMessage() + "]";
//            }
//        }
//        if (LogUtil.error) {
//            LOG.warn(log);
//        }
//    }
}
