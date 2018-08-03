package com.rongji.dfish.framework.plugin.file.controller;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.rongji.dfish.base.DfishException;
import com.rongji.dfish.base.util.FileUtil;
import com.rongji.dfish.framework.FrameworkHelper;
import com.rongji.dfish.framework.controller.BaseController;
import com.rongji.dfish.framework.plugin.file.entity.PubFileRecord;
import com.rongji.dfish.framework.plugin.file.service.FileService;
import com.rongji.dfish.ui.form.UploadItem;

@RequestMapping("/file")
@Controller
public class FileController extends BaseController {

	@Autowired
	private FileService fileService;
	
	public FileService getFileService() {
		return fileService;
	}

	public void setFileService(FileService fileService) {
		this.fileService = fileService;
	}
	
	@RequestMapping("/uploadFile")
	@ResponseBody
	public UploadItem uploadFile(HttpServletRequest request, HttpServletResponse response) throws Exception {
		UploadItem uploadItem = null;
		long currMs = System.currentTimeMillis();
		boolean error = false;
		try {
			if (request instanceof MultipartHttpServletRequest) {
				MultipartHttpServletRequest mRequest = (MultipartHttpServletRequest) request;
				MultipartFile fileData = mRequest.getFile("Filedata");
				
				String loginUserId = FrameworkHelper.getLoginUser(mRequest);
				uploadItem = fileService.saveFile(fileData, loginUserId);
			}
        } catch (Exception e) {
	        FrameworkHelper.LOG.error("上传过程出现异常@" + currMs, e);
	        error = true;
        }
		
		if (uploadItem == null) {
			uploadItem = new UploadItem();
			uploadItem.setError(true);
			String text = null;
			if (error) {
				text = "上传文件过程出现异常@" + currMs;
			} else {
				text = "上传文件失败,可能当前文件类型不符合系统规范";
			}
			uploadItem.setText(text);
		}
		return uploadItem;
	}
	
	@RequestMapping("/upload4Ueditor")
	@ResponseBody
	public Object upload4Ueditor(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String action=request.getParameter("action");
		//百度ueditor 
		if("config".equals(action)){
			InputStream is=FileController.class.getClassLoader().getResourceAsStream("com/rongji/dfish/framework/plugin/file/controller/ueditor_config.json");
			int readed=-1;
			byte[] buff=new byte[8192];
			String readedJson="";
			try {
				ByteArrayOutputStream baos=new ByteArrayOutputStream();
				while((readed=is.read(buff))>0){
					baos.write(buff, 0, readed);
				}
				readedJson=new String(baos.toByteArray(),"UTF-8");
			} catch (IOException e1) {
				FrameworkHelper.LOG.error("上传过程出现异常", e1);
			}finally{
				try {
					is.close();
				} catch (IOException e) {
					FrameworkHelper.LOG.error("文件流关闭异常", e);
				}
			}
			return readedJson;
		}
		
		UploadItem uploadItem = uploadFile(request, response);
		
		String resultJson = "";
		if (uploadItem == null || (uploadItem.getError() != null && uploadItem.getError())) {
			// FIXME 异常的具体JSON格式还需研究下百度编辑器
			resultJson = "{\"state\":\"FAIL\",\"text\":\"" + uploadItem.getText() + "\"}";
		} else {
			resultJson = "{\"state\":\"SUCCESS\"," +
					"\"url\":\"file/thumbnail?fileId="+uploadItem.getId()+"\"," +
					"\"title\":\""+uploadItem.getName()+"\"," +
					"\"original\":\""+uploadItem.getName()+"\"," +
					"\"type\":\""+FileUtil.getFileExtName(uploadItem.getName())+"\"," +
					"\"size\":"+uploadItem.getSize()+"" +
					"}";
		}
		//百度ueditor 
		return resultJson;
	}
	
	@RequestMapping("/uploadImage")
	@ResponseBody
	public Object uploadImage(HttpServletRequest request, HttpServletResponse response) throws Exception {
		UploadItem uploadItem = null;
		long currMs = System.currentTimeMillis();
		try {
			if (request instanceof MultipartHttpServletRequest) {
				MultipartHttpServletRequest mRequest = (MultipartHttpServletRequest) request;
				MultipartFile fileData = mRequest.getFile("Filedata");
				
				String loginUserId = FrameworkHelper.getLoginUser(mRequest);
				uploadItem = fileService.saveFile(fileData, loginUserId);
			}
        } catch (Exception e) {
	        FrameworkHelper.LOG.error("上传失败,系统内部异常@" + currMs, e);
        }
		if (uploadItem == null) {
			uploadItem = new UploadItem();
			uploadItem.setError(true);
			uploadItem.setText("上传失败,系统内部异常@" + currMs);
		}
		
		return uploadItem;
	}
	
	
	/**
	 * 下载附件方法
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/downloadFile")
	@ResponseBody
	public void downloadFile(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String enFileId = request.getParameter("fileId");
		String fileId = fileService.decId(enFileId);
		PubFileRecord fileRecord = fileService.getFileRecord(fileId);
		downloadFileData(response, fileRecord);
	}
	
	/**
	 * 附件下载
	 * @param response
	 * @param fileRecord
	 * @throws Exception
	 */
	private void downloadFileData(HttpServletResponse response, PubFileRecord fileRecord) throws Exception {
		String contentType = "application/octet-stream";
		String encoding = "UTF-8";
		InputStream input = null;
		OutputStream output = null;
		try {
//			File file = null;
//			if (fileRecord != null && !FileService.STATUS_DELETE.equals(fileRecord.getFileStatus())) { // 记录找不到或者附件删除
//				file = fileService.getFile(fileRecord);
//			}
//			if (file == null || !file.exists()) {
//				throw new DfishException("该附件不存在" + ( fileRecord != null ? "[" + fileRecord.getFileName() + "]": ""));
//			}
//			FileUtil.downLoadFile(request, response, file, fileRecord.getFileName(), contentType);
			String fileName = fileRecord.getFileName();
			response.setHeader("Accept-Ranges", "bytes");
			response.setHeader("Accept-Charset", encoding);
			response.setHeader("Content-type", contentType);
			response.setHeader("Content-Length", String.valueOf(fileService.getFileSize(fileRecord)));
			response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(fileName, encoding));
			response.setStatus(HttpServletResponse.SC_OK);
			input = fileService.getFileInputStream(fileRecord);
			if (input == null) {
				throw new DfishException("该附件不存在" + ( fileRecord != null ? "[" + fileRecord.getFileName() + "]": ""));
			}
			FileUtil.downLoadData(response, input);
        } catch (Exception e) {
        	FrameworkHelper.LOG.error("=========下载附件异常=========[" + fileRecord.getFileId() + "]", e);
        	String error = "附件不存在[" + fileService.encId(fileRecord.getFileId()) + "]";
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
		downloadFileData(response, fileRecord);
////		String contentType = "application/octet-stream";
//		// FIXME 理论上这边有可能会处理裁剪
//		try {
////			File file = null;
////			if (fileRecord != null && !FileService.STATUS_DELETE.equals(fileRecord.getFileStatus())) { // 记录找不到或者附件删除
////				file = fileService.getFile(fileRecord);
////			}
////			if (file == null || !file.exists()) {
////				String error = "";
////				if (fileRecord == null) {
////					error = "缩略图记录不存在[" + fileId + "]";
////				} else {
////					error = "缩略图记录已找到[" + fileId + "]状态:" + fileRecord.getFileStatus();
////				}
////				throw new DfishException(error);
////			}
////			FileUtil.downLoadFile(request, response, file, fileRecord.getFileName(), contentType);
//			InputStream inputStream = fileService.getFileInputStream(fileRecord);
//			if (inputStream == null) {
//				throw new DfishException("缩略图不存在" + ( fileRecord != null ? "[" + fileRecord.getFileName() + "]": ""));
//			}
//			FileUtil.downLoadData(response, inputStream);
//        } catch (Exception e) {
//        	FrameworkHelper.LOG.error("=========显示缩略图异常=========", e);
//        	String error = "缩略图不存在[" + enFileId + "]";
//        	response.sendError(HttpServletResponse.SC_EXPECTATION_FAILED, error);
//        }
	}
	
	@RequestMapping("/removeFile")
	@ResponseBody
	/**
	 * 删除文件方法
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public void removeFile(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String enFileId = request.getParameter("fileId");
		String fileId = fileService.decId(enFileId);
		fileService.updateFileStatus(fileId, FileService.STATUS_DELETE);
//		try {
//			PubFileRecord fileRecord = fileService.getFileRecord(fileId);
//			File file = fileService.getFile(fileRecord);
//			if (file != null && file.exists()) {
//				file.delete();
//			}
//			if (fileRecord != null) {
//				fileService.delete(fileRecord);
//			}
//        } catch (Exception e) {
//        	FrameworkHelper.LOG.error("=========移除附件异常=========", e);
//        	String error = "附件不存在[" + enFileId + "]";
//        	response.sendError(HttpServletResponse.SC_EXPECTATION_FAILED, error);
//        }
	}
	
	@RequestMapping("/preview")
	@ResponseBody
	/**
	 * 预览文件方法
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public Object preview(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		return null;
	}
	
}
