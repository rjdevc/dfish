package com.rongji.dfish.framework.plugin.file.controller;

import com.rongji.dfish.base.Utils;
import com.rongji.dfish.base.util.FileUtil;
import com.rongji.dfish.base.util.LogUtil;
import com.rongji.dfish.framework.FrameworkHelper;
import com.rongji.dfish.framework.controller.BaseController;
import com.rongji.dfish.framework.plugin.file.controller.img.ImageZoomDefine;
import com.rongji.dfish.framework.plugin.file.controller.img.ImageZoomScheme;
import com.rongji.dfish.framework.plugin.file.controller.plugin.FileUploadPlugin;
import com.rongji.dfish.framework.plugin.file.entity.PubFileRecord;
import com.rongji.dfish.framework.plugin.file.service.FileService;
import com.rongji.dfish.misc.util.ImageUtil;
import com.rongji.dfish.ui.form.UploadItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

@RequestMapping("/file")
@Controller
public class FileController extends BaseController {

	@Autowired
	private FileService fileService;
	@Autowired(required = false)
	private List<FileUploadPlugin> uploadPlugins;
	@Autowired(required = false)
	private List<ImageZoomDefine> imageZoomDefines;
	@Autowired(required = false)
	private List<ImageZoomScheme> imageZoomSchemes;

	private Map<String, FileUploadPlugin> uploadPluginMap = new HashMap<>();
	private Map<String, ImageZoomDefine> imageZoomDefineMap = new HashMap<>();
	private Map<String, ImageZoomScheme> imageZoomSchemeMap = new HashMap<>();


	@PostConstruct
	private void init() {
        if (Utils.notEmpty(uploadPlugins)) {
            for (FileUploadPlugin uploadPlugin : uploadPlugins) {
                registerUploadPlugin(uploadPlugin);
            }
        }
        if (Utils.notEmpty(imageZoomDefines)) {
			for (ImageZoomDefine imageZoomDefine : imageZoomDefines) {
				registerImageZoomDefine(imageZoomDefine);
			}
		}
        if (Utils.notEmpty(imageZoomSchemes)) {
			for (ImageZoomScheme imageZoomScheme : imageZoomSchemes) {
				registerImageZoomScheme(imageZoomScheme);
			}
		}
    }

    private void registerUploadPlugin(FileUploadPlugin uploadPlugin) {
		if (uploadPlugin == null) {
			return;
		}
		if (Utils.isEmpty(uploadPlugin.name())) {
			LogUtil.warn("The name is empty.[" + uploadPlugin.getClass().getName() + "]");
		}
		FileUploadPlugin old = uploadPluginMap.put(uploadPlugin.name(), uploadPlugin);
		if (old != null) {
			LogUtil.warn("The FileUploadPlugin[" + old.getClass().getName() + "] is replaced by [" + uploadPlugin.getClass().getName() + "]");
		}
	}

	private void registerImageZoomDefine(ImageZoomDefine imageZoomDefine) {
		if (imageZoomDefine == null) {
			return;
		}
		if (Utils.isEmpty(imageZoomDefine.getAlias())) {
			LogUtil.warn("The alias is empty.[" + imageZoomDefine.getClass().getName() + "]");
		}
		ImageZoomDefine old = imageZoomDefineMap.put(imageZoomDefine.getAlias(), imageZoomDefine);
		if (old != null) {
			LogUtil.warn("The system exists same name of the ImageZoomDefine.[" + imageZoomDefine.getAlias() + "]");
		}
	}

	private void registerImageZoomScheme(ImageZoomScheme imageZoomScheme) {
		if (imageZoomScheme == null) {
			return;
		}
		if (Utils.isEmpty(imageZoomScheme.getName())) {
			LogUtil.warn("The name is empty.[" + imageZoomScheme.getClass().getName() + "]");
		}
		ImageZoomScheme old = imageZoomSchemeMap.put(imageZoomScheme.getName(), imageZoomScheme);
		if (old != null) {
			LogUtil.warn("The system exists same name of the ImageZoomScheme.[" + imageZoomScheme.getName() + "]");
		}
	}

	public static UploadItem saveFile(HttpServletRequest request, FileService fileService, String acceptTypes) {
        UploadItem uploadItem = null;

        boolean accept = false;
        try {
            if (request instanceof MultipartHttpServletRequest) {
                MultipartHttpServletRequest mRequest = (MultipartHttpServletRequest) request;
                MultipartFile fileData = mRequest.getFile("Filedata");

                String extName=FileUtil.getFileExtName(fileData.getOriginalFilename());
                accept = accept(extName, acceptTypes);
                if (accept) {
                    String loginUserId = FrameworkHelper.getLoginUser(mRequest);
                    uploadItem = fileService.saveFile(fileData, loginUserId);
                }
            }
        } catch (Exception e) {
            long currMs = System.currentTimeMillis();
            LogUtil.error("上传失败,系统内部异常@" + currMs, e);
            uploadItem = new UploadItem();
            uploadItem.setError(true);
            uploadItem.setText("上传失败,系统内部异常@" + currMs);
        }

        if (uploadItem == null) {
            uploadItem = new UploadItem();
            uploadItem.setError(true);
            uploadItem.setText("上传文件失败" + (accept ? "，当前文件类型不符合系统规范" : ""));
        }
        return uploadItem;
    }

	/**
	 * 判断扩展名是否支持
	 * @param extName 拓展名(带.)
	 * @param acceptTypes
	 * @return
	 */
    static boolean accept(String extName, String acceptTypes) {
        if(acceptTypes==null||acceptTypes.equals("")){
            return true;
        }
        if(Utils.isEmpty(extName)){
            return false;
        }
        // 这里的extName是包含.

        HashSet<String> accSet=new HashSet<>(Arrays.asList(acceptTypes.split("[,;]")));
//		extName=extName.toLowerCase();
        for(String s:accSet){
            if (Utils.isEmpty(s)) {
                continue;
            }
            int dotIndex = s.lastIndexOf(".");
            if(dotIndex < 0) {
                continue;
            }
            String acc=s.substring(dotIndex);
            if(acc.equalsIgnoreCase(extName)) {
                return true;
            }
        }
        return false;
    }


	@RequestMapping("/uploadFile")
	@ResponseBody
	public UploadItem uploadFile(HttpServletRequest request) {
		return saveFile(request, fileService, fileService.getFileTypes());
	}

	private static final ExecutorService EXECUTOR_IMAGE = Executors.newFixedThreadPool(5);

	@RequestMapping("/uploadImage")
	@ResponseBody
	public Object uploadImage(HttpServletRequest request) {
		final UploadItem uploadItem = saveFile(request, fileService, fileService.getImageTypes());

		final String scheme = request.getParameter("scheme");
		if (uploadItem != null && Utils.notEmpty(uploadItem.getId()) && Utils.notEmpty(scheme)) {
		    final AtomicInteger doneFileCount = new AtomicInteger(0);
			EXECUTOR_IMAGE.execute(new Runnable() {
				@Override
				public void run() {
					ImageZoomScheme zoomScheme = imageZoomSchemeMap.get(scheme);
					if (zoomScheme != null && Utils.notEmpty(zoomScheme.getZooms())) {
						// 本应该先提前判断再进行获取压缩,暂时不考虑这些个别情况(多损耗性能)
						String fileId = fileService.decId(uploadItem.getId());
						PubFileRecord fileRecord = fileService.get(fileId);
						if (fileRecord == null || Utils.isEmpty(fileRecord.getFileUrl())) {
							LogUtil.warn("生成缩略图出现异常:原记录文件存储记录丢失[" + fileId + "]");
							return;
						}
						int dotIndex = fileRecord.getFileUrl().lastIndexOf(".");
						if (dotIndex < 0) {
							LogUtil.warn("生成缩略图出现异常:原记录文件存储记录异常[" + fileId + "]");
							return;
						}
						String fileExtName = fileRecord.getFileUrl().substring(dotIndex + 1);
						File imageFile = fileService.getFile(fileRecord);
						if (imageFile == null || !imageFile.exists()) {
							LogUtil.warn("生成缩略图出现异常:原图丢失[" + fileId + "]");
							return;
						}
						for (String zoomAlias : zoomScheme.getZooms()) {
							ImageZoomDefine zoomDefine = imageZoomDefineMap.get(zoomAlias);
							if (zoomDefine == null) {
								continue;
							}
							InputStream input = null;
							OutputStream output = null;
							try {
								input = new FileInputStream(imageFile);
								File outputFile = fileService.getFile(fileRecord, zoomAlias);
								if (!outputFile.exists()) {
									outputFile.createNewFile();
								}
								output = new FileOutputStream(outputFile);
								if (ImageZoomDefine.WAY_ZOOM.equals(zoomDefine.getWay())) {
									ImageUtil.zoom(input, output, fileExtName, zoomDefine.getWidth(), zoomDefine.getHeight());
								} else if (ImageZoomDefine.WAY_CUT.equals(zoomDefine.getWay())) {
									ImageUtil.cut(input, output, fileExtName, zoomDefine.getWidth(), zoomDefine.getHeight());
								} else if (ImageZoomDefine.WAY_RESIZE.equals(zoomDefine.getWay())) {
									ImageUtil.resize(input, output, fileExtName, zoomDefine.getWidth(), zoomDefine.getHeight(), zoomDefine.getBgColor());
								}
                                doneFileCount.incrementAndGet();
							} catch (Exception e) {
								LogUtil.error("生成缩略图出现异常:详见信息", e);
							} finally {
								if (input != null) {
									try {
										input.close();
									} catch (IOException e) {
										LogUtil.error("生成缩略图出现异常:输入流关闭异常", e);
									}
								}
								if (output != null) {
									try {
										output.close();
									} catch (IOException e) {
										LogUtil.error("生成缩略图出现异常:输出流关闭异常", e);
									}
								}
							}
						}
					}
				}
			});
			int waitCount = 0;
			// 至少保证第1个缩略图生成才返回结果
			while (doneFileCount.get() < 1 && waitCount++ < 30) {
                try {
                    // 缩略图未生成休眠等待
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                }
            }
		}
		return uploadItem;
	}

	@RequestMapping("/upload4Plugin")
	@ResponseBody
	public Object upload4Plugin(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String plugin = request.getParameter("plugin");
		// 取得附件上传插件,根据插件对应的上传方法进行附件上传处理
		FileUploadPlugin uploadPlugin = uploadPluginMap.get(plugin);
		if (uploadPlugin == null) {
		    return null;
        }
        Object result = uploadPlugin.doRequest(request);
		return result;
	}
	

	/**
	 * 下载附件方法
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/download")
	@ResponseBody
	public void download(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String enFileId = request.getParameter("fileId");
		String fileId = fileService.decId(enFileId);
		PubFileRecord fileRecord = fileService.getFileRecord(fileId);
		downloadFileData(response, "application/octet-stream", fileRecord, null);
	}

	/**
	 * 下载附件方法
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
     * @deprecated 下载附件方法统一使用download
	 */
	@RequestMapping("/downloadFile")
	@ResponseBody
    @Deprecated
	public void downloadFile(HttpServletRequest request, HttpServletResponse response) throws Exception {
	    // 下载附件方法统一使用download
		download(request, response);
	}

	/**
	 * 附件下载
	 * @param response
	 * @param fileRecord
	 * @throws Exception
	 */
	private void downloadFileData(HttpServletResponse response, String contentType, PubFileRecord fileRecord, String fileAlias) throws Exception {
		contentType = Utils.isEmpty(contentType) ? "application/octet-stream" : contentType;
		String encoding = "UTF-8";
		InputStream input = null;
		OutputStream output = null;
		try {
			String fileName = fileRecord.getFileName();
			response.setHeader("Accept-Ranges", "bytes");
			response.setHeader("Accept-Charset", encoding);
			response.setHeader("Content-type", contentType);
            input = fileService.getFileInputStream(fileRecord, fileAlias);
            long fileSize = 0L;
            if (input != null) {
                fileSize = fileService.getFileSize(fileRecord, fileAlias);
            } else { // 当别名附件不存在时,使用原附件
                input = fileService.getFileInputStream(fileRecord);
                fileSize = fileService.getFileSize(fileRecord);
            }
            response.setHeader("Content-Length", String.valueOf(fileSize));
            response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(fileName, encoding));
            response.setStatus(HttpServletResponse.SC_OK);
			if (input != null) {
				FileUtil.downLoadData(response, input);
			} else {
				String error = "该附件不存在@" + System.currentTimeMillis();
				LogUtil.error(error + ( fileRecord != null ? ("[" + fileRecord.getFileName() + "]" + (Utils.notEmpty(fileAlias) ? "[" + fileAlias + "]" : "")): ""));
				response.sendError(HttpServletResponse.SC_EXPECTATION_FAILED, error);
			}
		} catch (Exception e) {
			String error = "下载附件异常@" + System.currentTimeMillis();
			LogUtil.error(error + "[" + fileRecord.getFileId() + "]", e);
			response.sendError(HttpServletResponse.SC_EXPECTATION_FAILED, error);
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
	 * 显示缩略图方法
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/thumbnail")
	@ResponseBody
	public void thumbnail(HttpServletRequest request, HttpServletResponse response) throws Exception {
		preview(request, response);
	}

    /**
     * 预览文件方法
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("/preview")
    @ResponseBody
    public void preview(HttpServletRequest request, HttpServletResponse response) throws Exception {
        // FIXME 目前仅图片预览方法,如果是文件预览需做处理,不支持预览可能直接下载
        String enFileId = request.getParameter("fileId");
        String fileId = fileService.decId(enFileId);
        PubFileRecord fileRecord = fileService.getFileRecord(fileId);
        String imgType = null;
        if (fileRecord != null) {
            imgType = FileUtil.getFileExtName(fileRecord.getFileUrl());
            // 因取文件类型方法包含.所以需要截取
            if (Utils.notEmpty(imgType) && imgType.length() > 1) {
                imgType = imgType.substring(1);
            }
        }
        String contentType = "image/" + (imgType == null ? "png" : imgType);

        String fileAlias = request.getParameter("fileAlias");
        if (Utils.isEmpty(fileAlias)) {
            String scheme = request.getParameter("scheme");
            ImageZoomScheme zoomScheme = imageZoomSchemeMap.get(scheme);
            if (zoomScheme != null && Utils.notEmpty(zoomScheme.getZooms())) {
                fileAlias = zoomScheme.getZooms().get(0);
                // 缩略图还没生成需要处理

            }
        }

        downloadFileData(response, contentType, fileRecord, fileAlias);
    }

}
