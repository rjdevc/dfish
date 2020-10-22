package com.rongji.dfish.framework.plugin.file.service.impl;

import com.rongji.dfish.base.util.FileUtil;
import com.rongji.dfish.base.util.Utils;
import com.rongji.dfish.framework.FrameworkHelper;
import com.rongji.dfish.framework.plugin.file.config.FileHandleManager;
import com.rongji.dfish.framework.plugin.file.config.FileHandleScheme;
import com.rongji.dfish.framework.plugin.file.dao.FileDao;
import com.rongji.dfish.framework.plugin.file.dto.UploadItem;
import com.rongji.dfish.framework.plugin.file.entity.PubFileRecord;
import com.rongji.dfish.framework.plugin.file.service.FileService;
import com.rongji.dfish.framework.service.impl.AbstractFrameworkService4Simple;

import javax.annotation.Resource;
import java.io.*;
import java.util.*;
import java.util.Map.Entry;

/**
 * 附件数据服务层定义实现类
 */
public class FileServiceImpl extends AbstractFrameworkService4Simple<PubFileRecord, String> implements FileService {
    @Resource(name = "fileDao")
    private FileDao dao;
    @Resource(name = "fileHandleManager")
    private FileHandleManager fileHandleManager;

    @Override
    public FileDao getDao() {
        return dao;
    }

    public void setDao(FileDao dao) {
        this.dao = dao;
    }

    public FileHandleManager getFileHandleManager() {
        return fileHandleManager;
    }

    public void setFileHandleManager(FileHandleManager fileHandleManager) {
        this.fileHandleManager = fileHandleManager;
    }

    /**
     * 文件存放目录
     *
     * @return String 文件存放目录
     */
    @Override
    public String getUploadDir() {
        String uploadDir = FrameworkHelper.getSystemConfig(CONFIG_UPLOAD_DIR, "../upload/");
        String dirSeparator = getDirSeparator();
        if (Utils.notEmpty(uploadDir) && !uploadDir.endsWith(dirSeparator)) {
            uploadDir += dirSeparator;
        }
        return uploadDir;
    }

    /**
     * 文件限制大小
     *
     * @return String 文件限制大小
     */
    @Override
    public String getSizeLimit() {
        return FrameworkHelper.getSystemConfig(CONFIG_SIZE_LIMIT, "10M");
    }

    /**
     * 保存文件以及文件记录
     *
     * @param input 输入流
     * @param fileRecord 文件记录
     * @throws Exception 记录保存可能触发的业务异常
     */
    @Override
    public UploadItem saveFile(InputStream input, PubFileRecord fileRecord) throws Exception {
        String fileName = fileRecord.getFileName();
        String extName = FileUtil.getFileExtName(fileName);

        if (".JSP".equalsIgnoreCase(extName)) {
            extName = ".jsp.txt";
        }//安全加固，正规途径下载仍旧是.jsp 落盘是.jsp.txt

        String fixedFileName = fileName.replace(" ", "");
        String fileId = getNewId();

        // 直接用文件编号作为文件名
        String saveFileName = fileId + extName;

//		String fileType = null;
        Date now = new Date();

        String dirSeparator = getDirSeparator();
        String fileDir = DF_DIR.format(now);

        fileRecord.setFileId(fileId);
        fileRecord.setFileName(fixedFileName);
        fileRecord.setFileUrl(fileDir + dirSeparator + saveFileName);
        fileRecord.setFileExtension(Utils.notEmpty(extName) ? extName.substring(1).toLowerCase() : null);
        if (Utils.isEmpty(fileRecord.getFileType())) {
            fileRecord.setFileType(CONFIG_TYPE_FILE);
        }
        fileRecord.setCreateTime(now);
        fileRecord.setUpdateTime(now);
        fileRecord.setFileLink(LINK_FILE);
        fileRecord.setFileKey(fileId);
        fileRecord.setFileStatus(STATUS_NORMAL);

        doSaveFile(input, fileRecord);
        save(fileRecord);

        return parseUploadItem(fileRecord, true);
    }

    /**
     * 执行文件保存
     * @param inputStream 输入流
     * @param fileRecord 文件记录
     */
    protected void doSaveFile(InputStream inputStream, PubFileRecord fileRecord) throws IOException {
        // 这边没异常,不管有没保存成功都会增加文件记录
        String fileUrl = fileRecord.getFileUrl();
        int splitIndex = fileUrl.lastIndexOf(getDirSeparator());
        String fileDir = "";
        String saveFileName = fileUrl;
        if (splitIndex > 0) {
            fileDir = fileUrl.substring(0, splitIndex);
            saveFileName = fileUrl.substring(splitIndex + 1);
        }
        FileUtil.saveFile(inputStream, getUploadDir() + fileDir, saveFileName);
    }

    /**
     * 获取附件记录
     *
     * @param fileId 附件编号
     * @return PubFileRecord 文件记录
     * @see #get(Serializable)
     */
    @Deprecated
    public PubFileRecord getFileRecord(String fileId) {
        return get(fileId);
    }

    @Override
    public int updateFileStatus(Collection<String> fileIds, String fileStatus) {
        if (Utils.isEmpty(fileIds)) {
            return 0;
        }
        return getDao().updateFileStatus(fileIds, fileStatus, new Date());
//        if (Utils.isEmpty(fileId) || Utils.isEmpty(fileStatus)) {
//            return;
//        }
//        pubCommonDAO.bulkUpdate("UPDATE PubFileRecord t SET t.fileStatus=? WHERE t.fileId=?", fileStatus, fileId);
    }

    /**
     * 更新文件记录状态
     *
     * @param fileLink 文件链接
     * @param fileKey 文件关键字
     * @param fileStatus 文件状态
     */
    @Override
    public int updateFileStatus(String fileLink, String fileKey, String fileStatus) {
        return getDao().updateFileStatusByLink(fileLink, fileKey, fileStatus, new Date());
//        if (Utils.isEmpty(fileLink) || Utils.isEmpty(fileKey) || Utils.isEmpty(fileStatus)) {
//            return;
//        }
//        pubCommonDAO.bulkUpdate("UPDATE PubFileRecord t SET t.fileStatus=? WHERE t.fileLink=? AND t.fileKey=?", fileStatus, fileLink, fileKey);
    }

    /**
     * 获取文件流
     *
     * @param fileRecord 文件记录
     * @return 文件输入流
     * @throws Exception 文件流相关异常
     */
    @Override
    public InputStream getFileInputStream(PubFileRecord fileRecord, String alias, String extension) throws IOException {
        if (fileRecord == null) {
            throw new IllegalArgumentException("fileRecord is null");
        }
        if (FileServiceImpl.STATUS_DELETE.equals(fileRecord.getFileStatus())) {
            throw new IllegalArgumentException("the record[" + fileRecord.getFileId() + "] is signed deleted.");
        }
        File file = getFile(fileRecord, alias, extension);
        if (file == null) {
            // fileRecord
            throw new FileNotFoundException("not fount the file of the record[" + fileRecord.getFileId() + "]");
        }
        if (!file.exists()) {
            throw new FileNotFoundException(file.getAbsolutePath());
        }
        return new FileInputStream(file);
    }

    /**
     * 根据文件记录获取文件
     *
     * @param fileRecord   文件记录
     * @param alias        文件别名
     * @param fix2Original 文件找不到时使用原始文件
     * @return File 目标文件
     */
    @Override
    public File getFile(PubFileRecord fileRecord, String alias,String extension,  boolean fix2Original) {
        if (fileRecord == null || Utils.isEmpty(fileRecord.getFileUrl())) {
            return null;
        }
        int dotIndex = fileRecord.getFileUrl().lastIndexOf(".");
        String oldFileName;
        String fileExtName;
        if (dotIndex >= 0) {
            oldFileName = fileRecord.getFileUrl().substring(0, dotIndex);
            fileExtName = fileRecord.getFileUrl().substring(dotIndex);
        } else {
            oldFileName = fileRecord.getFileUrl();
            fileExtName = "";
        }

        String aliasExtension = Utils.notEmpty(extension) ? ("." + extension) : fileExtName;
        File file = new File(getUploadDir() + oldFileName + (Utils.notEmpty(alias) ? ("_" + alias) : "") + aliasExtension);
        if (!file.exists() && Utils.notEmpty(alias)) {
            if (fix2Original) {
                // 别名文件不存在时使用原始文件
                file = new File(getUploadDir() + oldFileName + fileExtName);
            }
        }
        return file;
    }

    /**
     * 根据文件记录获取文件
     *
     * @param fileRecords 文件记录
     * @return 文件记录列表
     */
    public List<File> getFiles(List<PubFileRecord> fileRecords) {
        if (Utils.isEmpty(fileRecords)) {
            return Collections.emptyList();
        }
        List<File> fileList = new ArrayList<>();
        for (PubFileRecord fileRecord : fileRecords) {
            File file = getFile(fileRecord);
            if (file != null && file.exists()) {
                fileList.add(file);
            }
        }
        return fileList;
    }

    /**
     * 根据文件记录获取文件
     *
     * @param itemJson 数据项Json
     * @return 文件列表
     */
    public List<File> getFiles(String itemJson) {
        return getFiles(itemJson, false);
    }

    /**
     * 根据文件记录获取文件
     *
     * @param itemJson 数据项Json
     * @param single 是否只获取一个文件
     * @return 文件列表
     */
    private List<File> getFiles(String itemJson, boolean single) {
        if (Utils.isEmpty(itemJson)) {
            return Collections.emptyList();
        }
        List<UploadItem> itemList = parseUploadItems(itemJson);
        List<String> fileIds = new ArrayList<>();
        for (UploadItem item : itemList) {
            String fileId = decrypt(item.getId());
            if (Utils.isEmpty(fileId)) {
                continue;
            }
            fileIds.add(fileId);
            if (single) {
                break;
            }
        }
        Map<String, PubFileRecord> fileRecords = gets(fileIds);
        List<PubFileRecord> recordList = new ArrayList<>(fileRecords.size());
        for (String fileId : fileIds) {
            PubFileRecord record = fileRecords.get(fileId);
            if (record != null) {
                recordList.add(record);
            }
        }
        return getFiles(recordList);
    }

    /**
     * 根据文件记录获取文件,方便项目中导入文件时候使用
     *
     * @param itemJson 数据项Json
     * @return 文件
     */
    public File getFile(String itemJson) {
        List<File> fileList = getFiles(itemJson, true);
        if (Utils.notEmpty(fileList)) {
            return fileList.get(0);
        }
        return null;
    }


    /**
     * 查询可用的文件记录
     *
     * @param fileLink 文件链接
     * @param fileKey 文件关键字
     * @return 文件记录列表
     */
    @Override
    public List<PubFileRecord> listRecords(String fileLink, String fileKey) {
        if (Utils.isEmpty(fileLink) || Utils.isEmpty(fileKey)) {
            return Collections.emptyList();
        }
        Map<String, List<PubFileRecord>> map = getRecords(fileLink, Collections.singletonList(fileKey));
        List<PubFileRecord> result = map.get(fileKey);
        return result == null ? new ArrayList<>(0) : result;
    }

    /**
     * 查询可用的文件记录
     *
     * @param fileLink 文件链接
     * @param fileKeys 文件关键字
     * @return Map&lt;String, List&lt;PubFileRecord&gt;&gt; 附件记录集合
     */
    @Override
    public Map<String, List<PubFileRecord>> getRecords(String fileLink, Collection<String> fileKeys) {
        return getDao().getRecords(fileLink, fileKeys, new String[]{STATUS_NORMAL, STATUS_LINKED});
//        if (Utils.isEmpty(fileLink) || Utils.isEmpty(fileKeys)) {
//            return Collections.emptyMap();
//        }
//        List<Object> params = new ArrayList<>(fileKeys.size() + 2);
//        params.add(fileLink);
//        params.addAll(fileKeys);
//        params.add(STATUS_NORMAL);
//        // 不考虑fileKeys过多的情况
//        @SuppressWarnings("unchecked")
//        List<PubFileRecord> dataList = (List<PubFileRecord>) pubCommonDAO
//                .getQueryList(
//                        "FROM PubFileRecord t WHERE t.fileLink=? AND t.fileKey IN ("
//                                + getParamStr(fileKeys.size())
//                                + ") AND t.fileStatus=?", params.toArray());
//        Map<String, List<PubFileRecord>> result = new HashMap<>();
//        for (PubFileRecord data : dataList) {
//            List<PubFileRecord> tempList = result.get(data.getFileKey());
//            if (tempList == null) {
//                tempList = new ArrayList<PubFileRecord>();
//                result.put(data.getFileKey(), tempList);
//            }
//            tempList.add(data);
//        }
//        return result;
    }

    /**
     * 根据链接查询文件数据项
     *
     * @param fileLink 文件链接
     * @param fileKey 文件关键字
     * @return 上传项列表
     */
    @Override
    public List<UploadItem> listUploadItems(String fileLink, String fileKey) {
        List<PubFileRecord> fileRecords = listRecords(fileLink, fileKey);
        return parseUploadItems(fileRecords);
    }

    /**
     * 根据链接查询文件数据项
     *
     * @param fileLink 文件链接
     * @param fileKeys 文件关键字列表
     * @return Map&lt;String, List&lt;UploadItem&gt;&gt; 文件数据项
     */
    @Override
    public Map<String, List<UploadItem>> getUploadItems(String fileLink, Collection<String> fileKeys) {
        Map<String, List<PubFileRecord>> fileMap = getRecords(fileLink, fileKeys);
        Map<String, List<UploadItem>> itemMap = new HashMap<>(fileMap.size());
        for (Entry<String, List<PubFileRecord>> entry : fileMap.entrySet()) {
            itemMap.put(entry.getKey(), parseUploadItems(entry.getValue()));
        }
        return itemMap;
    }

//    protected List<PubFileRecord> findDbRecords(Collection<String> fileIds) {
//        if (Utils.isEmpty(fileIds)) {
//            return Collections.emptyList();
//        }
//        List<PubFileRecord> fileList = (List<PubFileRecord>) pubCommonDAO.getQueryList("FROM PubFileRecord t WHERE t.fileId IN(" + getParamStr(fileIds.size()) + ")", fileIds.toArray());
//        return fileList;
//    }

/*	public List<PubFileRecord> findFileRecords(String[] fileId) {
		if (Utils.isEmpty(fileId)) {
			return Collections.emptyList();
		}
		if (fileId.length > BATCH_SIZE) { //
			throw new UnsupportedOperationException("单批查询支持的最大数量[" + BATCH_SIZE + "]");
		}
		List<Object> params = new ArrayList<Object>();
		for (String item : fileId) {
			if (item == null) {
				continue;
			}
			params.add(item);
		}
		if (params.isEmpty()) {
			return Collections.emptyList();
		}
		@SuppressWarnings("unchecked")
        List<PubFileRecord> result = (List<PubFileRecord>) pubCommonDAO.getQueryList("FROM PubFileRecord t WHERE t.fileId IN(" + getParamStr(params.size()) + ")", params.toArray());
		return result;
	}*/

    /**
     * 查询可用文件数据项
     *
     * @param fileIds 文件编号
     * @return  文件数据项列表
     */
    @Override
    public List<UploadItem> listUploadItems(String... fileIds) {
        if (Utils.isEmpty(fileIds)) {
            return Collections.emptyList();
        }
        Map<String, PubFileRecord> records = gets(Arrays.asList(fileIds));
        List<PubFileRecord> list = new ArrayList<>(records.size());
        for (String fileId : fileIds) {
            PubFileRecord record = records.get(fileId);
            if (record != null) {
                list.add(record);
            }
        }
        return parseUploadItems(list);
    }

    @Override
    public List<PubFileRecord> copyRecords(List<String> fileIds, String fileLink, String fileKey) {
        if (Utils.isEmpty(fileIds) || Utils.isEmpty(fileLink) || Utils.isEmpty(fileKey)) {
            return Collections.emptyList();
        }
        List<PubFileRecord> records = listByIds(fileIds);
        Date now = new Date();
        for (PubFileRecord record : records) {
            dao.evict(record);
            record.setFileId(getNewId());
            record.setFileLink(fileLink);
            record.setFileKey(fileKey);
            record.setUpdateTime(now);
        }
        dao.bulkSave(records);
        return records;
    }

//    @Override
//    public int updateFileLink(String fileId, String fileLink, String fileKey,String creator) {
//        return getDao().updateFileLink(fileId, fileLink, fileKey,creator, STATUS_LINKED, new Date());
//    }

    @Override
    public int updateFileLinks(List<String> fileIds, String fileLink, String fileKey,String creator) {
        if (Utils.isEmpty(fileIds)) {
            return 0;
        }
        return getDao().updateFileLinks(fileIds, fileLink, fileKey,creator, STATUS_LINKED, new Date());
    }

    @Override
    public UploadItem parseUploadItem(PubFileRecord fileRecord) {
        UploadItem item = parseUploadItem(fileRecord, false);
        // FIXME 这么写,如果直接调用remainRecord=true的方法不会处理,这是个问题,但不紧急
        if (item != null && fileHandleManager != null) {
            FileHandleScheme handleScheme = fileHandleManager.getScheme(fileRecord.getFileType(), fileRecord.getFileScheme());
            if (handleScheme != null) {
                handleScheme.fixUploadItem(item);
            }
        }
        return item;
    }
}
