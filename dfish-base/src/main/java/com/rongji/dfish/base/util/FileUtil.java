package com.rongji.dfish.base.util;

import com.rongji.dfish.base.Utils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
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
    public static Log LOG = LogFactory.getLog(FileUtil.class);
    public static final String ENCODING = "UTF-8";
    public static final String DOWNLOAD_ENCODING = "ISO8859-1";
    public static final String CLIENT_ENCODING = "GBK";
    static SimpleDateFormat DF_GMT;
    static {
        DF_GMT = new SimpleDateFormat("EEE MMM dd yyyy hh:mm:ss z", Locale.ENGLISH);
        DF_GMT.setTimeZone(TimeZone.getTimeZone("GMT"));
    }

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
     * @deprecated 这个方法返回值默认带. 在更多的场景是不需要. 所以推荐使用{@link #getExtension(String)}
     */
    @Deprecated
    public static String getFileExtName(String fileName) {
        if (Utils.isEmpty(fileName)) {
            return "";
        }
        int lastIndex = fileName.lastIndexOf(".");
        if (lastIndex < 0) {
            return "";
        }
        return fileName.substring(lastIndex);
    }

    /**
     * 如果有扩展名,包括点号 如 txt doc等(注意这里不带.).否则返回""
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
     * @param stream       InputStream
     * @param folderPath   String
     * @param realFileName String
     */
    public static void saveFile(InputStream stream, String folderPath, String realFileName) {
//		InputStream stream = null;
        OutputStream bos = null;
        try {
            // 接收到的附件数据流


            // 附件保存的相对位置
            String attachUrl = folderPath + "/" + realFileName;

            // 如果绝对目录不存在，新建目录
            File filePath = new File(folderPath.replace('/', File.separatorChar));

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
            logError("FileUtil.saveFile error", e);
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

    /**
     * 下载数据流
     *
     * @param response
     * @param is
     * @throws UnsupportedEncodingException
     * @throws IOException
     * @deprecated 经过实测证明，部分浏览器/应用可能在没有content-length 属性的时候，直接停止下载。
     * 请慎用该方法
     */
    public static void downLoadData(final HttpServletResponse response,
                                    InputStream is) throws UnsupportedEncodingException, IOException {
        OutputStream os = response.getOutputStream();
        try {

            byte[] buff = new byte[8192];
            int bytesRead;

            while (-1 != (bytesRead = is.read(buff, 0, buff.length))) {
                os.write(buff, 0, bytesRead);
            }
        } catch (final IOException e) {
            logError("FileUtil.downLoadData error", e);
        } finally {
            if (is != null) {
                is.close();
            }
            if (os != null) {
                os.close();
            }
        }
    }

    /**
     * 下载方法
     *
     * @param response   应答
     * @param attachFile 文件全路径+全名(服务器端的URL)
     * @param fileName   用户另存为的文件名
     * @throws IOException
     * @throws UnsupportedEncodingException
     */
    public static void downLoadFile(final HttpServletResponse response, String attachFile, String fileName)
            throws UnsupportedEncodingException, IOException {
        response.setStatus(HttpServletResponse.SC_OK);
        File file = new File(attachFile);
        fileName = safeFileName(fileName);
        //String contentType = getContentType(fileName);
        String contentType = "application/octet-stream";
        response.setHeader("Accept-Ranges", "bytes");
        response.setHeader("Accept-Charset", DOWNLOAD_ENCODING);
        response.setHeader("Content-type", contentType);
        response.setHeader("Content-Disposition", "attachment; filename="
                + fileName);
        response.setHeader("Content-Length", String.valueOf(file.length()));

        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        if (!file.exists() || file.length() == 0) {
            response.sendError(404);
            return;
        }
        try {
            bis = new BufferedInputStream(new FileInputStream(file));
            bos = new BufferedOutputStream(response.getOutputStream());

            byte[] buff = new byte[8192];
            int bytesRead;

            while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
                bos.write(buff, 0, bytesRead);
            }
        } catch (final IOException e) {
            logError("FileUtil.downLoadFile error", e);
        } finally {
            if (bis != null) {
                bis.close();
            }

            if (bos != null) {
                bos.close();
            }
        }
    }

    /**
     * 下载方法 支持断点续传
     *
     * @param response   应答
     * @param attachFile 文件全路径+全名(服务器端的URL)
     * @param fileName   用户另存为的文件名
     * @throws IOException
     * @throws UnsupportedEncodingException
     */
    public static void downLoadFile(HttpServletRequest request, HttpServletResponse response, String attachFile,
                                    String fileName) throws UnsupportedEncodingException, IOException {
        File file = new File(attachFile);
        //String contentType = getContentType(fileName);
        String contentType = "application/octet-stream";
        downLoadFile(request, response, file, fileName, contentType);
    }

    /**
     * 把byte当成文件下载的方法
     *
     * @param source   byte数组
     * @param response -
     * @param fileName 文件另存为的名字,现版本可以有中文
     * @throws IOException
     * @throws UnsupportedEncodingException
     */
    public static void downloadByteArrAsFile(byte[] source, final HttpServletResponse response, String fileName)
            throws UnsupportedEncodingException, IOException {
        //String contentType = getContentType(fileName);
        //response.setHeader("Content-type", contentType);
        response.setHeader("Content-type", "application/octet-stream");
        response.setHeader("Accept-Ranges", "bytes");
        response.setHeader("Accept-Charset", DOWNLOAD_ENCODING);

        fileName = safeFileName(fileName);
        response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
        response.setHeader("Content-Length", String.valueOf(source.length));

        BufferedOutputStream bos = null;

        try {
            bos = new BufferedOutputStream(response.getOutputStream());
            // 1 << 13 = 8192
            int blocks = ((source.length - 1) >> 13) + 1;

            for (int i = 0; i < blocks; i++) {
                bos.write(source, i << 13, ((source.length - (i << 13)) > 8192) ? 8192 : (source.length - (i << 13)));
            }
        } catch (Exception e) {
            logError("FileUtil.downloadByteArrAsFile error", e);
        } finally {
            if (bos != null) {
                bos.close();
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
            logError("FileUtil.readFileAsText error", e);
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
            logError("FileUtil.writeFile error", e);
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
    public static String safeFileName(String saveAs) {
        try {
            return new String(saveAs.getBytes(CLIENT_ENCODING), "ISO8859-1");
        } catch (UnsupportedEncodingException e) {
            try {
                return new String(saveAs.getBytes(), "ISO8859-1");
            } catch (UnsupportedEncodingException e1) {
                LOG.error(null, e1);
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
        if (l < 1024) {
            return l + " B";
        }
        if (l < 10240) { // 10K内显示2位有效数字
            return String.valueOf(((double) l / 1024)).substring(0, 3) + " KB";
        }
        if (l < 1048576L) { // 10K上显示KB
            return (l >> 10) + " KB";
        }
        if (l < 10485760L) { // 10M显示2位有效数字
            return String.valueOf(((double) l / 1048576L)).substring(0, 3) + " MB";
        }
        if (l < 1073741824L) { // 10M上显示MB
            return (l >> 20) + " MB";
        }
        if (l < 10737418240L) { // 10M上显示MB
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
        if (pathInZip == null || pathInZip.equals("/") || pathInZip.equals("\\")) {
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
            logError("FileUtil.copyFile error", e);
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

    //	public static String getContentType(String saveAs){
//		SAXReader reader = new SAXReader();
//		String contentType = null;
//		try {
//			File file = new File(SystemData.getInstance().getServletInfo().getServletRealPath()+"WEB-INF/config/contenttype.xml");
//			int l = saveAs.lastIndexOf(".");
//			String extension = saveAs.substring(l+1);
//			Document document = reader.read(file);
//			Node node = document
//					.selectSingleNode("/contentType/mime-mapping[extension='"+extension+"']");
//			Element element = (Element) node;
//			if(element!=null){
//				 contentType = element.elementTextTrim("mime-type");
//			}else{
//				contentType = "application/octet-stream";	
//			}
//		} catch (DocumentException e) {
//			e.printStackTrace();
//			contentType = "application/octet-stream";
//		}
//		
//		return contentType;
//	}
    public static void downLoadFile(HttpServletRequest request, HttpServletResponse response, File file, String fileName,
                                    String contentType) throws IOException {
        downLoadFile(request, response, false, contentType, new FileInputStream(file), file.length(),file.lastModified(), fileName);
    }

    private static void logError(String log, Throwable t) {
        if (log == null) {
            log = "";
        }
        boolean logError = true;
        if (t != null) {
            // FIXME 这里客户端异常不打算依赖包,所以通过字符判断;目前仅考虑tomcat的情况,其他web容器还需再验证测试
            logError = !"ClientAbortException".equals(t.getClass().getSimpleName());
            if (logError) {
                log += "[" + t.getClass().getName() + ":" + t.getMessage() + "]";
            }
        }
        if (logError) {
            LOG.warn(log);
        }
    }

    public static boolean downLoadFile(HttpServletRequest request, HttpServletResponse response, boolean inline, String contentType,
                                       InputStream fileInput, long fileLength,long lastModified, String fileName) throws IOException {

        if (lastModified > 0L) {
            if (!checkIfModifiedSince(request, response, fileLength,lastModified)) {
                fileInput.close();
                return true;
            }
        }
        String range = request.getHeader("Range");
        long from = 0L;
        long to = fileLength-1;
        if (range != null && !"".equals(range)) {
            range = range.toLowerCase();
            if (range.startsWith("bytes")) {
                int equotPos = range.indexOf("=");
                int toPos = range.indexOf("-");
                if (toPos < 0 && equotPos > 0) {
                    toPos = range.length();
                }
                if (toPos > 0) {
                    try {
                        String fromStr;
                        if (equotPos > 0) {
                            fromStr = range.substring(equotPos + 1, toPos);
                        } else {
                            // 从bytes开始截取
                            fromStr = range.substring(5, toPos);
                        }
                        if (Utils.notEmpty(fromStr) && Utils.notEmpty(fromStr.trim())) {
                            from = Long.parseLong(fromStr.trim());
                        }
                    } catch (Exception e) {
                        logError("FileUtil.downLoadFile:getting from value error", e);
                    }
                    try {
                        // range中可能有斜杠,这个格式判断不是标准的,可能需要调整
                        int divide = range.indexOf("/");
                        String toStr;
                        if (divide > to) {
                            toStr = range.substring(toPos + 1, divide);
                        } else {
                            toStr = range.substring(toPos + 1);
                        }
                        if (Utils.notEmpty(toStr) && Utils.notEmpty(toStr.trim())) {
                            to = Long.parseLong(toStr.trim());
                        }
                    } catch (Exception e) {
                        logError("FileUtil.downLoadFile:getting to value error", e);
                    }
                }
            }
        }

//        response.setHeader("Accept-Ranges", "bytes");
//        response.setHeader("Accept-Charset", FileUtil.ENCODING);
        if (Utils.isEmpty(contentType)) {
            contentType = "application/octet-stream";
        }
        response.setHeader("Content-type", contentType);

        long partLength = to - from+1;
//		boolean complete = to >= fileLength;
        if (from == 0 && to >= fileLength-1) {
            response.setStatus(HttpServletResponse.SC_OK);
            response.setHeader("Content-Length", String.valueOf(fileLength));
            if(!inline){
                response.setHeader("Content-Disposition", "attachment; filename="
                    + URLEncoder.encode(fileName, FileUtil.ENCODING));
            }
            if (lastModified > 0L) {
                synchronized(DF_GMT) {
                    response.setHeader("Last-Modified", DF_GMT.format(new Date(lastModified)));
                }

                response.setHeader("ETag", getEtag(fileLength,lastModified));
            }

//            response.setHeader("Content-Disposition", (inline ? "inline" : "attachment") + "; filename="
//                    + URLEncoder.encode(fileName, FileUtil.ENCODING));
        } else {
            response.setStatus(HttpServletResponse.SC_PARTIAL_CONTENT);
            response.setHeader("Content-Length", String.valueOf(partLength));
            String contentRange = "bytes " + from + "-" + to  + "/" + fileLength;
            response.setHeader("Content-Range", contentRange);
        }

        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        if (fileInput == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return false;
        }
        // long totalRead = 0;
        boolean complete = false;
        try {
            bis = new BufferedInputStream(fileInput);
            bos = new BufferedOutputStream(response.getOutputStream());

            int bytesRead;
            if (from > 0) {
                bis.skip(from);
            }
            // 75%节点有被加载到认定为"完成节点"
            long completePointLeft = (long) (fileLength * 0.25);
            long loadedLeft = fileLength - from;
            // 剩余字节大于完成节点的剩余,有机会可被加载到"完成节点"
            boolean canLoadedPoint = loadedLeft >= completePointLeft;

            long currLeftLength = partLength;
            byte[] buff = new byte[(int)(currLeftLength>8192 ? 8192 : currLeftLength)];
            // org.apache.catalina.servlets.DefaultServlet
            while (currLeftLength > 0 && -1 != (bytesRead = bis.read(buff, 0, buff.length))) {
                bos.write(buff, 0, bytesRead);
                long readBytes = Long.parseLong(String.valueOf(bytesRead));
                currLeftLength -= readBytes;
                if (canLoadedPoint) { // 有机会加载到"完成节点"
                    // 剩余加载字节
                    loadedLeft -= readBytes;
                    // 剩余加载字节比需完成的小,说明"完成节点已被加载"
                    if (loadedLeft <= completePointLeft) {
                        // 达到或超过"完成节点"
                        complete = true;
                        // "完成节点"已被加载,无需再计算判断加载
                        canLoadedPoint = false;
                    }
                }
            }
        } catch (IOException e) {
            logError("FileUtil.downLoadFile error", e);
        } finally {
            if (bis != null) {
                bis.close();
            }

            if (bos != null) {
                bos.close();
            }
        }
//        LOG.debug("ResponseRange[" + from + "-" + to + "/" + fileLength + "(" + (((double) from * 100) / fileLength) + "%-)],complete:" + complete);
        return complete;
    }
    protected static boolean checkIfModifiedSince(HttpServletRequest request, HttpServletResponse response, long fileLength,long lastModified) {
        try {
            long headerValue = request.getDateHeader("If-Modified-Since");
//            long lastModified = downloadParam.getLastModified();
            if (headerValue != -1) {

                // If an If-None-Match header has been specified, if modified since
                // is ignored.
                if ((request.getHeader("If-None-Match") == null) && (lastModified < headerValue + 1000L)) {
                    // The entity has not been modified since the date
                    // specified by the client. This is not an error case.
                    response.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
                    response.setHeader("ETag", getEtag(fileLength,lastModified));

                    return false;
                }
            }
        } catch (IllegalArgumentException illegalArgument) {
            return true;
        }
        return true;

    }
    private static String getEtag(long fileSize,long lastModified) {
        String etag = getIntHex(fileSize) + getIntHex(lastModified);
        return etag;
    }

    private static String getIntHex(long l) {
        l = (l & 0xFFFFFFFFL) | 0x100000000L;
        String s = Long.toHexString(l);
        return s.substring(1);
    }
}
