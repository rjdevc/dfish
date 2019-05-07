package com.rongji.dfish.framework.plugin.file.service;

import com.rongji.dfish.base.Utils;
import com.rongji.dfish.base.crypt.CryptFactory;
import com.rongji.dfish.base.crypt.StringCryptor;
import com.rongji.dfish.base.util.FileUtil;
import com.rongji.dfish.base.util.LogUtil;
import com.rongji.dfish.framework.FrameworkHelper;
import com.rongji.dfish.framework.plugin.file.entity.PubFileRecord;
import com.rongji.dfish.framework.service.BaseService;
import com.rongji.dfish.misc.util.JsonUtil;
import com.rongji.dfish.ui.form.UploadItem;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Map.Entry;

public class FileService extends BaseService<PubFileRecord, String> {

    public static final String STATUS_NORMAL = "0";
    public static final String STATUS_DELETE = "1";

    public static final String CONFIG_UPLOAD_DIR = "file.uploadDir";
    public static final String CONFIG_SIZE_LIMIT = "file.sizeLimit";
    public static final String CONFIG_TYPES_FILE = "file.types.file";
    public static final String CONFIG_TYPES_IMAGE = "file.types.image";

    public static final String LINK_FILE = "FILE";

    protected static final DateFormat DF = new SimpleDateFormat("yyyy/MM/dd");

    protected static final String SECRET_KEY = "DFISH";

    protected static StringCryptor CRY = CryptFactory.getStringCryptor(
            CryptFactory.BLOWFISH, CryptFactory.UTF8,
            CryptFactory.URL_SAFE_BASE64, SECRET_KEY);

    /**
     * 加密文件编号
     *
     * @param id 文件编号
     * @return 加密的文件编号
     */
    public String encId(String id) {
        return CRY.encrypt(id);
    }

    /**
     * 解密编号
     *
     * @param encId 加密的编号
     * @return 编号
     */
    public String decId(String encId) {
        try {
            return CRY.decrypt(encId);
        } catch (Exception e) {
            FrameworkHelper.LOG.error("解密编号出错", e);
            return "";
        }
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
    public String getSizeLimit() {
        return FrameworkHelper.getSystemConfig(CONFIG_SIZE_LIMIT, "2M");
    }

    /**
     * 文件上传支持的类型
     *
     * @return
     */
    public String getFileTypes() {
        // 默认文件格式*.doc;*.docx;*.xls;*.xlsx;*.ppt;*.pptx;*.zip;*.rar;*.jpg;*.gif;*.png;*.vsd;*.pot;*.pps;*.txt;*.rtf;*.pdf;*.epub;*.wps;*.et;*.dps
        return FrameworkHelper.getSystemConfig(CONFIG_TYPES_FILE, "*.doc;*.docx;*.xls;*.xlsx;*.ppt;*.pptx;*.jpg;*.gif;*.png;*.vsd;*.txt;*.rtf;*.pdf;*.wps;");

    }

    /**
     * 文件上传支持的类型
     *
     * @return
     * @deprecated 使用 {@link #getFileTypes()}
     */
    @Deprecated
    public String getTypesFile() {
        return getFileTypes();
    }

    /**
     * 图片上传支持的类型
     *
     * @return
     */
    public String getImageTypes() {
        return FrameworkHelper.getSystemConfig(CONFIG_TYPES_IMAGE, "*.jpg;*.gif;*.png;");
    }

    /**
     * 图片上传支持的类型
     *
     * @return
     * @deprecated 使用{@link #getImageTypes()}
     */
    @Deprecated
    public String getTypesImage() {
        return getImageTypes();
    }

    /**
     * 保存文件以及文件记录
     *
     * @param fileData
     * @param loginUserId
     * @throws Exception
     */
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
        String fileDir = DF.format(now);

        PubFileRecord fileRecord = new PubFileRecord();
        fileRecord.setFileId(fileId);
        fileRecord.setFileName(fileName);
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

    protected void doSaveFile(InputStream inputStream, PubFileRecord fileRecord) throws Exception {
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

    public PubFileRecord getFileRecord(String fileId) {
        if (Utils.isEmpty(fileId)) {
            return null;
        }
        @SuppressWarnings("unchecked")
        List<PubFileRecord> result = (List<PubFileRecord>) pubCommonDAO.getQueryList("SELECT t FROM PubFileRecord t WHERE t.fileId=?", fileId);
        if (Utils.notEmpty(result)) {
            return result.get(0);
        }
        return null;
    }

    /**
     * 更新文件记录状态
     *
     * @param fileId
     * @param fileStatus
     */
    public void updateFileStatus(String fileId, String fileStatus) {
        if (Utils.isEmpty(fileId) || Utils.isEmpty(fileStatus)) {
            return;
        }
        pubCommonDAO.bulkUpdate("UPDATE PubFileRecord t SET t.fileStatus=? WHERE t.fileId=?", fileStatus, fileId);
    }

    /**
     * 更新文件记录状态
     *
     * @param fileLink
     * @param fileKey
     * @param fileStatus
     */
    public void updateFileStatus(String fileLink, String fileKey, String fileStatus) {
        if (Utils.isEmpty(fileLink) || Utils.isEmpty(fileKey) || Utils.isEmpty(fileStatus)) {
            return;
        }
        pubCommonDAO.bulkUpdate("UPDATE PubFileRecord t SET t.fileStatus=? WHERE t.fileLink=? AND t.fileKey=?", fileStatus, fileLink, fileKey);
    }

    /**
     * 获取文件流
     *
     * @param fileId
     * @return
     * @throws Exception
     */
    public InputStream getFileInputStream(String fileId) throws Exception {
        PubFileRecord fileRecord = getFileRecord(fileId);
        return getFileInputStream(fileRecord);
    }

    /**
     * 获取文件流
     *
     * @param fileRecord
     * @return
     * @throws Exception
     */
    public InputStream getFileInputStream(PubFileRecord fileRecord) throws Exception {
        return getFileInputStream(fileRecord, "");
    }

    /**
     * 获取文件流
     *
     * @param fileRecord
     * @return
     * @throws Exception
     */
    public InputStream getFileInputStream(PubFileRecord fileRecord, String fileAlias) throws Exception {
        if (fileRecord == null || FileService.STATUS_DELETE.equals(fileRecord.getFileStatus())) {
            return null;
        }
        File file = getFile(fileRecord, fileAlias);
        if (file == null || !file.exists()) {
            return null;
        }
        return new FileInputStream(file);
    }

    /**
     * 根据文件记录获取文件
     *
     * @param fileRecord
     * @return
     */
    public File getFile(PubFileRecord fileRecord) {
        return getFile(fileRecord, "");
    }

    /**
     * 根据文件记录获取文件
     *
     * @param fileRecord 文件记录
     * @param fileAlias  文件别名
     * @return
     */
    public File getFile(PubFileRecord fileRecord, String fileAlias) {
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

        File file = new File(getUploadDir() + oldFileName + (Utils.notEmpty(fileAlias) ? ("_" + fileAlias) : "") + fileExtName);
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
            String fileId = decId(item.getId());
            if (Utils.isEmpty(fileId)) {
                continue;
            }
            fileIds.add(fileId);
            if (single) {
                break;
            }
        }
        List<PubFileRecord> fileRecords = findFileRecords(fileIds.toArray(new String[]{}));
        return getFiles(fileRecords);
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
    public long getFileSize(PubFileRecord fileRecord) {
        return getFileSize(fileRecord, null);
    }
    /**
     * 获取文件大小
     *
     * @param fileRecord
     * @return
     */
    public long getFileSize(PubFileRecord fileRecord, String fileAlias) {
        if (fileRecord == null) {
            return 0L;
        }
        if (Utils.isEmpty(fileAlias)) {
            return fileRecord.getFileSize();
        }
        File file = getFile(fileRecord, fileAlias);
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
    public List<PubFileRecord> findFileRecords(String fileLink, String fileKey) {
        if (Utils.isEmpty(fileLink) || Utils.isEmpty(fileKey)) {
            return Collections.emptyList();
        }
        Map<String, List<PubFileRecord>> map = findFileRecords(fileLink, Collections.singletonList(fileKey));
//		@SuppressWarnings("unchecked")
//        List<PubFileRecord> result = (List<PubFileRecord>) pubCommonDAO.getQueryList("FROM PubFileRecord t WHERE t.fileLink=? AND t.fileKey=? AND t.fileStatus=?", fileLink, fileKey, STATUS_NORMAL);
        List<PubFileRecord> result = map.get(fileKey);
        if (result == null) {
            return Collections.emptyList();
        }
        return result;
    }

    /**
     * 查询可用的文件记录
     *
     * @param fileLink
     * @param fileKeys
     * @return
     */
    public Map<String, List<PubFileRecord>> findFileRecords(String fileLink,
                                                            Collection<String> fileKeys) {
        if (Utils.isEmpty(fileLink) || Utils.isEmpty(fileKeys)) {
            return Collections.emptyMap();
        }
        List<Object> params = new ArrayList<>(fileKeys.size() + 2);
        params.add(fileLink);
        params.addAll(fileKeys);
        params.add(STATUS_NORMAL);
        // 不考虑fileKeys过多的情况
        @SuppressWarnings("unchecked")
        List<PubFileRecord> dataList = (List<PubFileRecord>) pubCommonDAO
                .getQueryList(
                        "FROM PubFileRecord t WHERE t.fileLink=? AND t.fileKey IN ("
                                + getParamStr(fileKeys.size())
                                + ") AND t.fileStatus=?", params.toArray());
        Map<String, List<PubFileRecord>> result = new HashMap<>();
        for (PubFileRecord data : dataList) {
            List<PubFileRecord> tempList = result.get(data.getFileKey());
            if (tempList == null) {
                tempList = new ArrayList<PubFileRecord>();
                result.put(data.getFileKey(), tempList);
            }
            tempList.add(data);
        }
        return result;
    }


    /**
     * 查询可用的文件记录
     *
     * @param fileParams
     * @return
     */
    @SuppressWarnings("unchecked")
    public Map<FileRecordParam, List<PubFileRecord>> findFileRecords(List<FileRecordParam> fileParams) {
        if (Utils.isEmpty(fileParams)) {
            return Collections.emptyMap();
        }
        Collection<String> fileLinks = new HashSet<>();
        Collection<String> fileKeys = new HashSet<>();
        String link = null;
        String key = null;
        //参数转换
        for (FileRecordParam fp : fileParams) {
            link = fp.getFileLink();
            key = fp.getFileKey();
            if (Utils.notEmpty(link) && Utils.notEmpty(key)) {
                fileLinks.add(link);
                fileKeys.add(key);
            }
        }
        if (fileLinks.size() > 1000) {
            throw new UnsupportedOperationException("查询参数fileLinks支持的最大长度[1000]");
        }

        List<PubFileRecord> dataList = new ArrayList<>();
        final StringBuilder sb = new StringBuilder("FROM PubFileRecord t WHERE 1=1 ");
        final List<Object> params = new ArrayList<>();

        if (fileKeys.size() <= Math.sqrt(fileParams.size() * 2)) {
            sb.append("AND t.fileLink IN(" + getParamStr(fileLinks.size()) + ") ");
            params.addAll(fileLinks);
        }

        final List<String> fileKeyList = new ArrayList<>(fileKeys);
        List<PubFileRecord> tempDataList = (List<PubFileRecord>) pubCommonDAO.getHibernateTemplate().execute(new HibernateCallback<Object>() {
            @Override
            public Object doInHibernate(Session session) throws HibernateException, SQLException {
                List<String> tofetch = fileKeyList;
                List<PubFileRecord> tempList = new ArrayList<>();
                int paramSize = 0;
                String queryStr = "";
                while (tofetch.size() > 0) {
                    List<Object> param = new ArrayList<>();
                    param.addAll(params);
                    if (tofetch.size() > FETCH_SIZE) {
                        List<String> cur = tofetch.subList(0, FETCH_SIZE);
                        tofetch = tofetch.subList(FETCH_SIZE, tofetch.size());
                        queryStr = "AND t.fileKey IN (" + getParamStr(cur.size()) + ") AND t.fileStatus=? ";
                        param.addAll(cur);
                    } else {
                        queryStr = "AND t.fileKey IN (" + getParamStr(tofetch.size()) + ") AND t.fileStatus=? ";
                        param.addAll(tofetch);
                        tofetch = tofetch.subList(tofetch.size(), tofetch.size());
                    }
                    Query query = session.createQuery(sb.toString() + queryStr);
                    param.add(STATUS_NORMAL);
                    paramSize = param.size();
                    for (int i = 0; i < paramSize; i++) {
                        query.setParameter(i, param.get(i));
                    }
                    tempList.addAll(query.list());
                }
                return tempList;
            }
        });

        if (Utils.isEmpty(tempDataList)) {
            return Collections.emptyMap();
        }
        dataList.addAll(tempDataList);
        Map<FileRecordParam, List<PubFileRecord>> result = new HashMap<>();
        for (PubFileRecord data : dataList) {
            FileRecordParam p = new FileRecordParam();
            p.setFileLink(data.getFileLink());
            p.setFileKey(data.getFileKey());
            List<PubFileRecord> tempList = result.get(p);
            if (tempList == null) {
                tempList = new ArrayList<>();
                result.put(p, tempList);
            }
            tempList.add(data);
        }
        return result;
    }

    /**
     * 根据链接查询文件数据项
     *
     * @param fileLink
     * @param fileKey
     * @return
     */
    public List<UploadItem> findUploadItems(String fileLink, String fileKey) {
        List<PubFileRecord> fileRecords = findFileRecords(fileLink, fileKey);
        return parseUploadItems(fileRecords);
    }

    /**
     * 根据链接查询文件数据项
     *
     * @param fileLink
     * @param fileKeys
     * @return
     */
    public Map<String, List<UploadItem>> findUploadItems(String fileLink, Collection<String> fileKeys) {
        Map<String, List<PubFileRecord>> fileMap = findFileRecords(fileLink, fileKeys);
        Map<String, List<UploadItem>> itemMap = new HashMap<>(fileMap.size());
        for (Entry<String, List<PubFileRecord>> entry : fileMap.entrySet()) {
            itemMap.put(entry.getKey(), parseUploadItems(entry.getValue()));
        }
        return itemMap;
    }


    /**
     * 根据fileId 查询文件记录
     *
     * @param fileId 如果fileId有重复，返回结果也是有重复，如果某个文件id查询结果为空 对应返回null
     * @return
     */
    public List<PubFileRecord> findFileRecords(String[] fileId) {
        return findAll(Arrays.asList(fileId));
    }
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
     * @param fileId
     * @return
     */
    public List<UploadItem> findUploadItems(String... fileId) {
        List<PubFileRecord> fileRecords = findFileRecords(fileId);
        return parseUploadItems(fileRecords);
    }

    /**
     * 更新文件链接
     *
     * @param itemJson
     * @param fileLink
     * @param fileKey
     */
    public void updateFileLink(String itemJson, String fileLink, String fileKey) {
        List<UploadItem> itemList = parseUploadItems(itemJson);
        updateFileLink(itemList, fileLink, fileKey);
    }

    /**
     * 更新文件链接
     *
     * @param itemList
     * @param fileLink
     * @param fileKey
     */
    public void updateFileLink(List<UploadItem> itemList, String fileLink, String fileKey) {
        if (Utils.isEmpty(fileLink) || Utils.isEmpty(fileKey)) {
            return;
        }

        List<String> newFileIds = new ArrayList<>();
        if (Utils.notEmpty(itemList)) {
            for (UploadItem item : itemList) {
                newFileIds.add(decId(item.getId()));
            }
        }
        if (itemList.size() > BATCH_SIZE) {
            // FIXME 待分批处理,理论上应该不会出现这么多的附件
        }
        // 这里需要将旧文件标为删除,否则之前的文件无法删除
        @SuppressWarnings("unchecked")
        List<String> oldFileIds = (List<String>) pubCommonDAO.getQueryList("SELECT t.fileId FROM PubFileRecord t WHERE t.fileLink=? AND t.fileKey=? AND t.fileStatus=?", fileLink, fileKey, STATUS_NORMAL);

        List<String> insertIds = new ArrayList<>(newFileIds);
        List<String> deleteIds = new ArrayList<>(oldFileIds);
        insertIds.removeAll(oldFileIds);
        deleteIds.removeAll(newFileIds);
        if (Utils.notEmpty(insertIds)) { // 这边的insert相当于更新附件链接和状态
            @SuppressWarnings("unchecked")
            List<PubFileRecord> fileList = (List<PubFileRecord>) pubCommonDAO.getQueryList("FROM PubFileRecord t WHERE t.fileId IN(" + getParamStr(insertIds.size()) + ")", insertIds.toArray());
            final List<PubFileRecord> insertList = new ArrayList<>();
            List<String> updateIds = new ArrayList<>(fileList.size());
            for (PubFileRecord file : fileList) {
                if (LINK_FILE.equals(file.getFileLink())) { // 临时文件可直接修改模块
                    updateIds.add(file.getFileId());
                } else { // 非临时文件将产生新的记录
                    file.setFileId(getNewId());
                    file.setFileLink(fileLink);
                    file.setFileKey(fileKey);
                    file.setUpdateTime(new Date());
                    insertList.add(file);
                }
            }

            if (Utils.notEmpty(updateIds)) {
                List<Object> params = new ArrayList<>();
                params.add(fileLink);
                params.add(fileKey);
                params.add(new Date());
                params.addAll(updateIds);
                pubCommonDAO.bulkUpdate("UPDATE PubFileRecord t SET t.fileLink=?,t.fileKey=?,t.updateTime=? WHERE t.fileId IN(" + getParamStr(updateIds.size()) + ")", params.toArray());
            }
            pubCommonDAO.getHibernateTemplate().execute(new HibernateCallback<Object>() {
                @Override
                public Object doInHibernate(Session session) throws HibernateException, SQLException {
                    for (PubFileRecord file : insertList) {
                        session.save(file);
                    }
                    return null;
                }
            });
        }
        if (Utils.notEmpty(deleteIds)) { // 这里的delete 相当于把附件状态标志为删除
            List<Object> params = new ArrayList<Object>();
            params.add(STATUS_DELETE);
            params.add(new Date());
            params.addAll(deleteIds);
            pubCommonDAO.bulkUpdate("UPDATE PubFileRecord t SET t.fileStatus=?,t.updateTime=? WHERE t.fileId IN(" + getParamStr(deleteIds.size()) + ")", params.toArray());
        }
    }

    /**
     * 转换成文件数据项
     *
     * @param itemJson
     * @return
     */
    public static List<UploadItem> parseUploadItems(String itemJson) {
        if (Utils.isEmpty(itemJson)) {
            return Collections.emptyList();
        }
        try {
            List<UploadItem> itemList = JsonUtil.parseArray(itemJson, UploadItem.class);
            return itemList;
        } catch (Exception e) {
            FrameworkHelper.LOG.error("转换成文件数据项异常", e);
        }
        return Collections.emptyList();
    }

    /**
     * 转换成文件数据项
     *
     * @param itemJson
     * @return
     */
    public static UploadItem parseUploadItem(String itemJson) {
        List<UploadItem> itemList = parseUploadItems(itemJson);
        if (Utils.notEmpty(itemList)) {
            return itemList.get(0);
        }
        return null;
    }

    public List<String> parseFileIds(String itemJson) {
        List<UploadItem> itemList = parseUploadItems(itemJson);
        List<String> fileIds = new ArrayList<String>(itemList.size());
        for (UploadItem item : itemList) {
            try {
                String fileId = decId(item.getId());
                if (Utils.isEmpty(fileId)) {
                    continue;
                }
                fileIds.add(fileId);
            } catch (Exception e) {
                FrameworkHelper.LOG.error("文件编号解析异常", e);
            }
        }
        return fileIds;
    }

    public String parseFileId(String itemJson) {
        List<String> fileIds = parseFileIds(itemJson);
        if (Utils.notEmpty(fileIds)) {
            return fileIds.get(0);
        }
        return null;
    }

    /**
     * 转换成文件数据项
     *
     * @param fileRecords
     * @return
     */
    public List<UploadItem> parseUploadItems(List<PubFileRecord> fileRecords) {
        if (Utils.isEmpty(fileRecords)) {
            return null;
        }
        List<UploadItem> uploadItems = new ArrayList<>();
        for (PubFileRecord fileRecord : fileRecords) {
            UploadItem item = parseUploadItem(fileRecord);
            if (item != null) {
                uploadItems.add(item);
            }
        }
        return uploadItems;
    }

    /**
     * 转换成文件数据项
     *
     * @param fileRecord
     * @return
     */
    public UploadItem parseUploadItem(PubFileRecord fileRecord) {
        if (fileRecord == null) {
            return null;
        }
        UploadItem item = new UploadItem();
        String encId = encId(fileRecord.getFileId());
        item.setId(encId);
        item.setName(fileRecord.getFileName());
        item.setSize(fileRecord.getFileSize());
        return item;
    }

}
