package com.rongji.dfish.base.util;

import java.io.*;
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
    public static final String ENCODING = "UTF-8";
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
        if (Utils.isEmpty(fileName) || fileName.indexOf(".") < 0) {
            return "";
        }
        int i = fileName.lastIndexOf(".");
        return fileName.substring(i);
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
     * @param stream       InputStream
     * @param folderPath   String
     * @param realFileName String
     */
    public static void saveFile(InputStream stream, String folderPath,
                                String realFileName) {
//		InputStream stream = null;
        OutputStream bos = null;
        try {
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
            // 将数据流写入文件
            bos = new FileOutputStream(targetFile);
            int bytesRead = 0;
            byte[] buffer = new byte[8192];

            while ((bytesRead = stream.read(buffer, 0, 8192)) != -1) {
                bos.write(buffer, 0, bytesRead);
            }

        } catch (Exception e) {
            LogUtil.error("FileUtil.saveFile error", e);
        } finally {
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException ex) {
                }
            }
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException ex) {
                }
            }
        }
    }

    public static void readAndWrite(InputStream input, OutputStream output) throws IOException {
        try {
            if (input != null && output != null) {
                byte[] buff = new byte[8192];
                int bytesRead;
                while (-1 != (bytesRead = input.read(buff, 0, buff.length))) {
                    output.write(buff, 0, bytesRead);
                }
            }
        } catch (final IOException e) {
            LogUtil.error("FileUtil.readAndWrite error", e);
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
    public static boolean writeFile(String content, String fileName, String charset) {
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
    public static boolean writeFile(String content, File file, String charset, boolean append) {
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
        } catch (Exception e) {
            LogUtil.error("FileUtil.writeFile error", e);
            return false;
        } finally {
            try {
                if (fos != null) {
                    if (writer != null) {
                        writer.close();
                    }
                    fos.close();
                }

            } catch (Exception ex) {
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
    public static boolean writeFile(String content, String fileName) {
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
    public static void addDirectoyToZip(File path, ZipOutputStream zos, String pathInZip, FileFilter fileNameFilter) throws IOException {
        if (pathInZip == null || "/".equals(pathInZip) || "\\".equals(pathInZip)) {
            pathInZip = "";
        }
        if (fileNameFilter != null && !fileNameFilter.accept(path)) {
            return;
        }
        if (path.isDirectory()) {
            File[] subs = path.listFiles();
            for (int i = 0; i < subs.length; i++) {
                if ("".equals(pathInZip)) {
                    addDirectoyToZip(subs[i], zos, subs[i].getName(), fileNameFilter);
                } else {
                    addDirectoyToZip(subs[i], zos, pathInZip + "/" + subs[i].getName(), fileNameFilter);
                }
            }
        } else if (path.isFile()) {
            InputStream data = null;
            try {
                data = new FileInputStream(path);
                addDataToZipASFile(zos, pathInZip, data); // 如果是文件则添加
            } catch (IOException ex) {
                if (data != null) {
                    data.close(); // 保证流关闭
                }
                throw ex;
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

    public static boolean copyFile(String fromFileFullName, String toFileFolder, String toFileName) {
        boolean isExist = true;
        OutputStream os = null;
        InputStream ins = null;
        try {

            File attachFile = new File(fromFileFullName.replace('/',
                    File.separatorChar));
            if (attachFile.exists() && attachFile.isFile()) { // 原附件存在
                File file = new File(toFileFolder);
                if (!file.exists()) {
                    file.mkdirs();
                }
                os = new FileOutputStream((toFileFolder + "/" + toFileName).replace('/', File.separatorChar));
                ins = new FileInputStream(attachFile);
                int r = 0;
                byte[] buffer = new byte[8192];
                while ((r = ins.read(buffer, 0, 8192)) != -1) {
                    os.write(buffer, 0, r);
                }
            } else {
                isExist = false;
            }
        } catch (Exception e) {
            LogUtil.error("FileUtil.copyFile error", e);
        } finally {
            if (os != null) {
                try {
                    os.close();
                } catch (IOException ex) {
                }
            }
            if (ins != null) {
                try {
                    ins.close();
                } catch (IOException ex1) {
                }
            }
        }
        return isExist;
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
