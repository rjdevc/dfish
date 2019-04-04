package com.rongji.dfish.framework.plugin.file.controller;

import com.rongji.dfish.base.DfishException;
import com.rongji.dfish.base.Utils;
import com.rongji.dfish.base.util.FileUtil;
import com.rongji.dfish.base.util.LogUtil;
import com.rongji.dfish.framework.FilterParam;
import com.rongji.dfish.framework.FrameworkHelper;
import com.rongji.dfish.framework.controller.BaseController;
import com.rongji.dfish.framework.plugin.file.controller.config.FileHandlingDefine;
import com.rongji.dfish.framework.plugin.file.controller.config.FileHandlingScheme;
import com.rongji.dfish.framework.plugin.file.controller.config.ImageHandlingDefine;
import com.rongji.dfish.framework.plugin.file.controller.plugin.FileUploadPlugin;
import com.rongji.dfish.framework.plugin.file.entity.PubFileRecord;
import com.rongji.dfish.framework.plugin.file.service.FileService;
import com.rongji.dfish.misc.util.ImageUtil;
import com.rongji.dfish.ui.command.JSCommand;
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
	private List<FileHandlingDefine> fileHandlingDefines;
	@Autowired(required = false)
	private List<FileHandlingScheme> fileHandlingSchemes;

	private Map<String, FileUploadPlugin> uploadPluginMap = new HashMap<>();
	private Map<String, FileHandlingDefine> fileHandlingDefineMap = new HashMap<>();
	private Map<String, FileHandlingScheme> fileHandlingSchemeMap = new HashMap<>();


	@PostConstruct
	private void init() {
        if (Utils.notEmpty(uploadPlugins)) {
            for (FileUploadPlugin uploadPlugin : uploadPlugins) {
                registerUploadPlugin(uploadPlugin);
            }
        }
        if (Utils.notEmpty(fileHandlingDefines)) {
			for (FileHandlingDefine handlingDefine : fileHandlingDefines) {
				registerFileHandlingDefine(handlingDefine);
			}
		}
        if (Utils.notEmpty(fileHandlingSchemes)) {
			for (FileHandlingScheme handlingScheme : fileHandlingSchemes) {
				registerFileHandlingScheme(handlingScheme);
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

	private void registerFileHandlingDefine(FileHandlingDefine handlingDefine) {
		if (handlingDefine == null) {
			return;
		}
		if (Utils.isEmpty(handlingDefine.getAlias())) {
			LogUtil.warn("The alias is empty.[" + handlingDefine.getClass().getName() + "]");
		}
		FileHandlingDefine old = fileHandlingDefineMap.put(handlingDefine.getAlias(), handlingDefine);
		if (old != null) {
			LogUtil.warn("The system exists same name of the FileHandlingDefine.[" + handlingDefine.getAlias() + "]");
		}
	}

	private void registerFileHandlingScheme(FileHandlingScheme handlingScheme) {
		if (handlingScheme == null) {
			return;
		}
		if (Utils.isEmpty(handlingScheme.getName())) {
			LogUtil.warn("The name is empty.[" + handlingScheme.getClass().getName() + "]");
		}
		FileHandlingScheme old = fileHandlingSchemeMap.put(handlingScheme.getName(), handlingScheme);
		if (old != null) {
			LogUtil.warn("The system exists same name of the FileHandlingScheme.[" + handlingScheme.getName() + "]");
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
                } else {
                	LogUtil.debug("上传文件失败;extName=" + extName + "||acceptTypes=" + acceptTypes);
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
            uploadItem.setText("上传文件失败" + (!accept ? "，当前文件类型不符合系统规范" : ""));
        }
        return uploadItem;
    }

	/**
	 * 判断扩展名是否支持
	 * @param extName 拓展名(不管有没.都支持;即doc和.doc)
	 * @param acceptTypes 可接受的类型;格式如:*.doc;*.png;*.jpg;
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

		String[] accepts = acceptTypes.split("[,;]");
//		extName=extName.toLowerCase();
		// 类型是否含.
		int extDot = extName.lastIndexOf(".");
		// 统一去掉.
		String realExtName = (extDot >= 0) ? extName.substring(extDot + 1) : extName;
        for(String s : accepts){
            if (Utils.isEmpty(s)) {
                continue;
            }
            int dotIndex = s.lastIndexOf(".");
            if(dotIndex < 0) {
                continue;
            }
            String acc=s.substring(dotIndex + 1);
            if(acc.equalsIgnoreCase(realExtName)) {
                return true;
            }
        }
        return false;
    }


	@RequestMapping("/uploadFile")
	@ResponseBody
	public UploadItem uploadFile(HttpServletRequest request) {
    	String scheme = request.getParameter("scheme");
		FileHandlingScheme handlingScheme = getFileHandlingScheme(scheme);
		String acceptTypes = handlingScheme != null && Utils.notEmpty(handlingScheme.getHandlingTypes()) ? handlingScheme.getHandlingTypes() : fileService.getFileTypes();
		return saveFile(request, fileService, acceptTypes);
	}

	private static final ExecutorService EXECUTOR_IMAGE = Executors.newFixedThreadPool(5);

    private FileHandlingScheme getFileHandlingScheme(String scheme) {
    	if (Utils.isEmpty(scheme)) {
    		return null;
		}
		return fileHandlingSchemeMap.get(scheme);
	}

	@RequestMapping("/uploadImage")
	@ResponseBody
	public Object uploadImage(HttpServletRequest request) {
		final UploadItem uploadItem = uploadFile(request);
		if (uploadItem == null || Utils.isEmpty(uploadItem.getId())) {
			// 这样异常结果返回可能导致前端显示异常
			return uploadItem;
		}

		FilterParam param = getFileParam(request);
		// 这里先统一补thumbnail缩略图地址
        uploadItem.setThumbnail("file/thumbnail?fileId=" + uploadItem.getId() + param);

		String scheme = request.getParameter("scheme");
		final FileHandlingScheme handlingScheme = getFileHandlingScheme(scheme);
		if (handlingScheme == null || Utils.isEmpty(handlingScheme.getDefines())) {
			// 无需进行图片压缩
			return uploadItem;
		}
		final AtomicInteger doneFileCount = new AtomicInteger(0);
		EXECUTOR_IMAGE.execute(new Runnable() {
			@Override
			public void run() {
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
				for (String defineAlias : handlingScheme.getDefines()) {
					InputStream input = null;
					OutputStream output = null;
					try {
						FileHandlingDefine handlingDefine = fileHandlingDefineMap.get(defineAlias);
						if (handlingDefine == null || !(handlingDefine instanceof ImageHandlingDefine)) {
							continue;
						}
						ImageHandlingDefine realDefine = (ImageHandlingDefine) handlingDefine;

						input = new FileInputStream(imageFile);
						File outputFile = fileService.getFile(fileRecord, defineAlias);
						if (!outputFile.exists()) {
							outputFile.createNewFile();
						}
						output = new FileOutputStream(outputFile);
						if (ImageHandlingDefine.WAY_ZOOM.equals(realDefine.getWay())) {
							ImageUtil.zoom(input, output, fileExtName, realDefine.getWidth(), realDefine.getHeight());
						} else if (ImageHandlingDefine.WAY_CUT.equals(realDefine.getWay())) {
							ImageUtil.cut(input, output, fileExtName, realDefine.getWidth(), realDefine.getHeight());
						} else if (ImageHandlingDefine.WAY_RESIZE.equals(realDefine.getWay())) {
							ImageUtil.resize(input, output, fileExtName, realDefine.getWidth(), realDefine.getHeight(), realDefine.getBgColor());
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
		});
		int waitCount = 0;
		// 至少保证第1个缩略图生成才返回结果
		while (doneFileCount.get() < 1 && waitCount++ < 30) { // 最多等待3秒
			try {
				// 缩略图未生成休眠等待
				Thread.sleep(100);
			} catch (InterruptedException e) {
				LogUtil.error("生成缩略图等待异常", e);
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
		// 目前文件下载统一默认都是原件下载
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
		String enFileId = request.getParameter("fileId");
		String fileId = fileService.decId(enFileId);
		PubFileRecord fileRecord = fileService.getFileRecord(fileId);

		if (fileRecord != null) {
			String fileType = FileUtil.getFileExtName(fileRecord.getFileUrl());
			// 因取文件类型方法包含.所以需要截取
			if (Utils.notEmpty(fileType) && fileType.length() > 1) {
				fileType = fileType.substring(1);
			}
			String contentType = "image/" + (fileType == null ? "png" : fileType);
			// 获取参数
			FilterParam param = getFileParam(request);
			// 获取附件别名
			String fileAlias = getFileAlias(param);
			downloadFileData(response, contentType, fileRecord, fileAlias);
		} else {
			String error = "附件记录不存在@" + System.currentTimeMillis();
			LogUtil.warn(error + "[" + fileRecord.getFileId() + "]");
			response.sendError(HttpServletResponse.SC_EXPECTATION_FAILED, error);
		}
	}

	private String getFileAlias(FilterParam param) {
		String fileAlias = param.getValueAsString("fileAlias");
		if (Utils.isEmpty(fileAlias)) {
			String scheme = param.getValueAsString("scheme");
			FileHandlingScheme handlingScheme = getFileHandlingScheme(scheme);
			if (handlingScheme != null && Utils.notEmpty(handlingScheme.getDefines())) {
				fileAlias = handlingScheme.getDefines().get(0);
				// 缩略图还没生成需要处理
			}
		}
		return fileAlias;
	}

    /**
     * 预览文件方法
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("/preview")
    @ResponseBody
    public Object preview(HttpServletRequest request) throws Exception {
        // FIXME 目前仅图片预览方法,如果是文件预览需做处理,不支持预览可能直接下载
        String enFileId = request.getParameter("fileId");
        String fileId = fileService.decId(enFileId);
        PubFileRecord fileRecord = fileService.getFileRecord(fileId);

        if (fileRecord != null) {
			String fileType = FileUtil.getFileExtName(fileRecord.getFileUrl());
			boolean isImage = accept(fileType, fileService.getImageTypes());
			FilterParam param = getFileParam(request);
			String fileParamUrl = "?fileId=" + enFileId + param;
			if (isImage) {
				return new JSCommand("$.previewImage('file/thumbnail" + fileParamUrl + "');");
			} else {
				return new JSCommand("$.download('file/download" + fileParamUrl + "');");
			}
		} else {
			String error = "附件获取异常@" + System.currentTimeMillis();
			LogUtil.warn(error + "[" + fileRecord.getFileId() + "]");
			throw new DfishException(error);
		}
    }

    private FilterParam getFileParam(HttpServletRequest request) {
    	FilterParam param = new FilterParam();
    	param.registerKey("fileAlias");
    	param.registerKey("scheme");

    	param.bindRequest(request);
    	return param;
	}
}
