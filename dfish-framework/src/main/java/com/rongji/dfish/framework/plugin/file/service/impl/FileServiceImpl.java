package com.rongji.dfish.framework.plugin.file.service.impl;

import com.rongji.dfish.base.Utils;
import com.rongji.dfish.base.util.FileUtil;
import com.rongji.dfish.framework.FrameworkHelper;
import com.rongji.dfish.framework.dao.FrameworkDao;
import com.rongji.dfish.framework.plugin.file.dao.FileDao;
import com.rongji.dfish.framework.plugin.file.entity.PubFileRecord;
import com.rongji.dfish.framework.plugin.file.service.FileService;
import com.rongji.dfish.framework.service.impl.AbstractFrameworkService4Simple;
import com.rongji.dfish.ui.form.UploadItem;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.util.*;
import java.util.Map.Entry;

public class FileServiceImpl extends AbstractFrameworkService4Simple<PubFileRecord, String> implements FileService {
    @Resource(name = "fileDao")
    private FileDao dao;

    @Override
    public FileDao getDao() {
        return dao;
    }

    public void setDao(FileDao dao) {
        this.dao = dao;
    }

    /**
     * 路径分隔符
     *
     * @return
     */
    public String getDirSeparator() {
        return "/";
    }

    /**
     * 文件存放目录
     *
     * @return
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
     * @return
     */
    @Override
    public String getSizeLimit() {
        return FrameworkHelper.getSystemConfig(CONFIG_SIZE_LIMIT, "10M");
    }

    /**
     * 文件上传支持的类型
     *
     * @return
     */
    @Override
    public String getFileTypes() {
        // 默认文件格式*.doc;*.docx;*.xls;*.xlsx;*.ppt;*.pptx;*.zip;*.rar;*.jpg;*.gif;*.png;*.vsd;*.pot;*.pps;*.txt;*.rtf;*.pdf;*.epub;*.wps;*.et;*.dps
        return FrameworkHelper.getSystemConfig(CONFIG_TYPES_FILE, "*.doc;*.docx;*.xls;*.xlsx;*.ppt;*.pptx;*.jpg;*.gif;*.png;*.vsd;*.txt;*.rtf;*.pdf;*.wps;");

    }

    /**
     * 图片上传支持的类型
     *
     * @return
     */
    @Override
    public String getImageTypes() {
        return FrameworkHelper.getSystemConfig(CONFIG_TYPES_IMAGE, "*.jpg;*.gif;*.png;");
    }

    /**
     * 保存文件以及文件记录
     *
     * @param fileData
     * @param loginUserId
     * @throws Exception
     */
    @Override
    public UploadItem saveFile(MultipartFile fileData, String loginUserId) throws Exception {
        if (fileData == null) {
            return null;
        }

        String extName = FileUtil.getFileExtName(fileData.getOriginalFilename());

        if (".JSP".equalsIgnoreCase(extName)) {
            extName = ".jsp.txt";
        }//安全加固，正规途径下载仍旧是.jsp 落盘是.jsp.txt

        String fileName = fileData.getOriginalFilename().replace(" ", "");
        long fileSize = fileData.getSize();
        String fileId = getNewId();

        // 直接用文件编号作为文件名
        String saveFileName = fileId + extName;

//		String fileType = null;
        Date now = new Date();

        String dirSeparator = getDirSeparator();
        String fileDir = DF_DIR.format(now);

        PubFileRecord fileRecord = new PubFileRecord();
        fileRecord.setFileId(fileId);
        fileRecord.setFileName(fileName);
        fileRecord.setFileType(Utils.notEmpty(extName) ? extName.substring(1).toLowerCase() : null);
        fileRecord.setFileSize(fileSize);
        fileRecord.setFileCreator(loginUserId);
        fileRecord.setCreateTime(now);
        fileRecord.setUpdateTime(now);
        fileRecord.setFileLink(LINK_FILE);
        fileRecord.setFileKey(fileId);
        fileRecord.setFileUrl(fileDir + dirSeparator + saveFileName);
        fileRecord.setFileStatus(STATUS_NORMAL);
        save(fileRecord);

        doSaveFile(fileData.getInputStream(), fileRecord);

        return parseUploadItem(fileRecord);
    }

    protected void doSaveFile(InputStream inputStream, PubFileRecord fileRecord) {
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
     * @return PubFileRecord
     * @see #get(Serializable)
     */
    @Deprecated
    public PubFileRecord getFileRecord(String fileId) {
        return get(fileId);
    }

    /**
     * 更新文件记录状态
     *
     * @param fileId
     * @param fileStatus
     */
    @Override
    public int updateFileStatus(String fileId, String fileStatus) {
        return getDao().updateFileStatus(fileId, fileStatus);
//        if (Utils.isEmpty(fileId) || Utils.isEmpty(fileStatus)) {
//            return;
//        }
//        pubCommonDAO.bulkUpdate("UPDATE PubFileRecord t SET t.fileStatus=? WHERE t.fileId=?", fileStatus, fileId);
    }

    /**
     * 更新文件记录状态
     *
     * @param fileLink
     * @param fileKey
     * @param fileStatus
     */
    @Override
    public int updateFileStatus(String fileLink, String fileKey, String fileStatus) {
        return getDao().updateFileStatus(fileLink, fileKey, fileStatus);
//        if (Utils.isEmpty(fileLink) || Utils.isEmpty(fileKey) || Utils.isEmpty(fileStatus)) {
//            return;
//        }
//        pubCommonDAO.bulkUpdate("UPDATE PubFileRecord t SET t.fileStatus=? WHERE t.fileLink=? AND t.fileKey=?", fileStatus, fileLink, fileKey);
    }

    /**
     * 获取文件流
     *
     * @param fileRecord
     * @return
     * @throws Exception
     */
    @Override
    public InputStream getFileInputStream(PubFileRecord fileRecord, String alias) throws Exception {
        if (fileRecord == null || FileServiceImpl.STATUS_DELETE.equals(fileRecord.getFileStatus())) {
            return null;
        }
        File file = getFile(fileRecord, alias);
        if (file == null || !file.exists() || file.length() <= 0) {
            return null;
        }
        return new FileInputStream(file);
    }

    /**
     * 根据文件记录获取文件
     *
     * @param fileRecord   文件记录
     * @param alias        文件别名
     * @param fix2Original 文件找不到时使用原始文件
     * @return
     */
    @Override
    public File getFile(PubFileRecord fileRecord, String alias, boolean fix2Original) {
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

        File file = new File(getUploadDir() + oldFileName + (Utils.notEmpty(alias) ? ("_" + alias) : "") + fileExtName);
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
     * @param fileRecords
     * @return
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
     * @param itemJson
     * @return
     */
    public List<File> getFiles(String itemJson) {
        return getFiles(itemJson, false);
    }

    /**
     * 根据文件记录获取文件
     *
     * @param itemJson
     * @param single
     * @return
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
     * @param itemJson
     * @return
     */
    public File getFile(String itemJson) {
        List<File> fileList = getFiles(itemJson, true);
        if (Utils.notEmpty(fileList)) {
            return fileList.get(0);
        }
        return null;
    }

    /**
     * 获取文件大小
     *
     * @param fileRecord
     * @return
     */
    @Override
    public long getFileSize(PubFileRecord fileRecord, String alias) {
        if (fileRecord == null) {
            return 0L;
        }
        if (Utils.isEmpty(alias)) {
            return fileRecord.getFileSize();
        }
        File file = getFile(fileRecord, alias);
        if (file == null || !file.exists()) {
            return 0L;
        }
        return file.length();
    }

    /**
     * 查询可用的文件记录
     *
     * @param fileLink
     * @param fileKey
     * @return
     */
    @Override
    public List<PubFileRecord> getRecords(String fileLink, String fileKey) {
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
     * @param fileLink
     * @param fileKeys
     * @return
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
     * @param fileLink
     * @param fileKey
     * @return
     */
    @Override
    public List<UploadItem> getUploadItems(String fileLink, String fileKey) {
        List<PubFileRecord> fileRecords = getRecords(fileLink, fileKey);
        return parseUploadItems(fileRecords);
    }

    /**
     * 根据链接查询文件数据项
     *
     * @param fileLink
     * @param fileKeys
     * @return
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
     * @param fileIds
     * @return
     */
    @Override
    public List<UploadItem> getUploadItems(String... fileIds) {
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
    public int updateFileLink(String fileId, String fileLink) {
        return getDao().updateFileLink(fileId, fileLink);
    }

    @Override
    public int updateFileLink(List<String> fileIds, String fileLink, String fileKey) {
        return getDao().updateFileLink(fileIds, fileLink, fileKey);
//        if (Utils.isEmpty(itemList) || Utils.isEmpty(fileLink) || Utils.isEmpty(fileKey)) {
//            return 0;
//        }
//
//        List<String> newFileIds = new ArrayList<>();
//        if (Utils.notEmpty(itemList)) {
//            for (UploadItem item : itemList) {
//                newFileIds.add(decrypt(item.getId()));
//            }
//        }
//        if (itemList.size() > BATCH_SIZE) {
//            // FIXME 待分批处理,理论上应该不会出现这么多的附件
//        }
//        // 这里需要将旧文件标为删除,否则之前的文件无法删除
//        @SuppressWarnings("unchecked")
//        List<String> oldFileIds = (List<String>) pubCommonDAO.getQueryList("SELECT t.fileId FROM PubFileRecord t WHERE t.fileLink=? AND t.fileKey=? AND t.fileStatus=?", fileLink, fileKey, STATUS_NORMAL);
//
//        List<String> insertIds = new ArrayList<>(newFileIds);
//        List<String> deleteIds = new ArrayList<>(oldFileIds);
//        insertIds.removeAll(oldFileIds);
//        deleteIds.removeAll(newFileIds);
//        if (Utils.notEmpty(insertIds)) { // 这边的insert相当于更新附件链接和状态
//            @SuppressWarnings("unchecked")
//            List<PubFileRecord> fileList = (List<PubFileRecord>) pubCommonDAO.getQueryList("FROM PubFileRecord t WHERE t.fileId IN(" + getParamStr(insertIds.size()) + ")", insertIds.toArray());
//            final List<PubFileRecord> insertList = new ArrayList<>();
//            List<String> updateIds = new ArrayList<>(fileList.size());
//            for (PubFileRecord file : fileList) {
//                if (LINK_FILE.equals(file.getFileLink())) { // 临时文件可直接修改模块
//                    updateIds.add(file.getFileId());
//                } else { // 非临时文件将产生新的记录
//                    file.setFileId(getNewId());
//                    file.setFileLink(fileLink);
//                    file.setFileKey(fileKey);
//                    file.setUpdateTime(new Date());
//                    insertList.add(file);
//                }
//            }
//
//            if (Utils.notEmpty(updateIds)) {
//                List<Object> params = new ArrayList<>();
//                params.add(fileLink);
//                params.add(fileKey);
//                params.add(new Date());
//                params.add(STATUS_LINKED);
//                params.addAll(updateIds);
//                pubCommonDAO.bulkUpdate("UPDATE PubFileRecord t SET t.fileLink=?,t.fileKey=?,t.updateTime=?,t.fileStatus=? WHERE t.fileId IN(" + getParamStr(updateIds.size()) + ")", params.toArray());
//            }
//            pubCommonDAO.getHibernateTemplate().execute(new HibernateCallback<Object>() {
//                @Override
//                public Object doInHibernate(Session session) throws HibernateException, SQLException {
//                    for (PubFileRecord file : insertList) {
//                        session.save(file);
//                    }
//                    return null;
//                }
//            });
//        }
//        if (Utils.notEmpty(deleteIds)) {
//            // 这里的delete 相当于把附件状态标志为删除
//            List<Object> params = new ArrayList<>();
//            params.add(STATUS_DELETE);
//            params.add(new Date());
//            params.addAll(deleteIds);
//            pubCommonDAO.bulkUpdate("UPDATE PubFileRecord t SET t.fileStatus=?,t.updateTime=? WHERE t.fileId IN(" + getParamStr(deleteIds.size()) + ")", params.toArray());
//        }
//        return itemList.size();
    }

//    public void updateFileLink(String fileId, String fileLink) {
//        if (Utils.isEmpty(fileId) || Utils.isEmpty(fileLink)) {
//            return;
//        }
//        pubCommonDAO.bulkUpdate("UPDATE PubFileRecord t SET t.fileLink=?,t.updateTime=?,t.fileStatus=? WHERE t.fileId=?", new Object[]{ fileLink, new Date(), STATUS_LINKED, fileId });
//    }

}
