package com.rongji.dfish.framework.plugin.file.service;

import com.rongji.dfish.base.util.FileUtil;
import com.rongji.dfish.base.util.JsonUtil;
import com.rongji.dfish.base.util.LogUtil;
import com.rongji.dfish.base.util.Utils;
import com.rongji.dfish.framework.FrameworkHelper;
import com.rongji.dfish.framework.plugin.file.dto.UploadItem;
import com.rongji.dfish.framework.plugin.file.entity.PubFileRecord;
import com.rongji.dfish.framework.service.FrameworkService;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 附件数据服务层接口定义
 *
 * @author lamontYu
 * @since DFish3.0
 */
public interface FileService extends FrameworkService<PubFileRecord, PubFileRecord, String> {

    /**
     * 附件状态-初始状态
     */
    String STATUS_NORMAL = "0";
    /**
     * 附件状态-已关联
     */
    String STATUS_LINKED = "1";
    /**
     * 附件状态-已删除
     */
    String STATUS_DELETE = "9";

    /**
     * 附件配置-上传目录
     */
    String CONFIG_UPLOAD_DIR = "file.uploadDir";
    /**
     * 附件配置-大小限制
     */
    String CONFIG_SIZE_LIMIT = "file.sizeLimit";
    /**
     * 附件配置-文件类型
     */
    String CONFIG_TYPES_PRE = "file.types.";
    /**
     * 附件配置-文件类型
     */
    String CONFIG_TYPE_FILE = "file";
    /**
     * 附件配置-图片类型
     */
    String CONFIG_TYPE_IMAGE = "image";
    /**
     * 附件配置-視頻类型
     */
    String CONFIG_TYPE_VIDEO = "video";

    /**
     * 附件关联
     */
    String LINK_FILE = "FILE";

    /**
     * 附件存储路径
     */
    DateFormat DF_DIR = new SimpleDateFormat("yyyy/MM/dd");

    /**
     * 路径分隔符
     *
     * @return
     */
    default String getDirSeparator() {
        return "/";
    }

    /**
     * 保存文件以及文件记录
     *
     * @param input      文件输入流
     * @param fileRecord PubFileRecord保存的文件记录
     * @return UploadItem 上传数据项
     * @throws Exception 文件记录保存过程可能出现的业务异常
     */
    UploadItem saveFile(InputStream input, PubFileRecord fileRecord) throws Exception;

    /**
     * 文件存放目录
     *
     * @return String
     */
    String getUploadDir();

    /**
     * 文件限制大小
     *
     * @return String
     */
    String getSizeLimit();

    /**
     * 文件类型格式限制
     * @param type 文件类型
     * @param defaultTypes 默认文件格式
     * @return String
     */
    default String getFileTypes(String type, String defaultTypes) {
        type = Utils.isEmpty(type) ? CONFIG_TYPE_FILE : type;
        defaultTypes = Utils.isEmpty(defaultTypes) ? "*.doc;*.docx;*.xls;*.xlsx;*.ppt;*.pptx;*.jpg;*.gif;*.png;*.vsd;*.txt;*.rtf;*.pdf;*.wps;*.rjd;*.rjb;" : defaultTypes;
        // 默认文件格式*.doc;*.docx;*.xls;*.xlsx;*.ppt;*.pptx;*.zip;*.rar;*.jpg;*.gif;*.png;*.vsd;*.pot;*.pps;*.txt;*.rtf;*.pdf;*.epub;*.wps;*.et;*.dps
        return FrameworkHelper.getSystemConfig(CONFIG_TYPES_PRE + type, defaultTypes);
    }

    /**
     * 文件上传支持的类型
     *
     * @return String
     */
    @Deprecated
    default String getFileTypes() {
        return getFileTypes(CONFIG_TYPE_FILE, null);
    }

    /**
     * 图片上传支持的类型
     *
     * @return String
     */
    @Deprecated
    default String getImageTypes() {
        return getFileTypes(CONFIG_TYPE_IMAGE, "*.jpg;*.gif;*.png;");
    }

//    /**
//     * 更新文件链接
//     *
//     * @param fileId   附件编号
//     * @param fileLink 链接名
//     * @param fileKey  关联数据
//     * @return int 更新数量
//     * @deprecated 该方法不建议使用, 仅仅修改链接而不修改旧记录状态, 会让已作废的附件记录无法正确清理
//     *
//     */
//    default int updateFileLink(String fileId, String fileLink, String fileKey){
//        updateFileLink(fileId,fileLink,fileKey,null );
//    }

    /**
     * 更新文件链接
     *
     * @param itemJson 附件项json
     * @param fileLink 链接名
     * @param fileKey  链接关键字
     * @return int 更新数量
     */
    default int updateFileLinks(String itemJson, String fileLink, String fileKey, String fileCreator) {
        if (Utils.isEmpty(fileLink) || Utils.isEmpty(fileKey)) {
            return 0;
        }
        List<PubFileRecord> oldList = listRecords(fileLink, fileKey);
        List<String> oldIds = new ArrayList<>(oldList.size());
        for (PubFileRecord data : oldList) {
            oldIds.add(data.getFileId());
        }
        if (Utils.isEmpty(itemJson)) {
            // 为空时,将旧的关联记录删除
            return updateFileStatus(oldIds, STATUS_DELETE);
        } else {
            List<UploadItem> itemList = parseUploadItems(itemJson);
            List<String> newIds = new ArrayList<>(itemList.size());
            for (UploadItem item : itemList) {
                newIds.add(decrypt(item.getId()));
            }

            List<String> insertList = new ArrayList<>(newIds);
            insertList.removeAll(oldIds);
            List<String> deleteList = new ArrayList<>(oldIds);
            deleteList.removeAll(newIds);
            int count = updateFileLinks(insertList, fileLink, fileKey, fileCreator);
            count += updateFileStatus(deleteList, STATUS_DELETE);
            return count + deleteList.size();
        }
    }

    /**
     * 更新附件链接;fileId为空时,将fileLink和fileKey关联的数据标记删除
     *
     * @param fileId      附件编号(必要)
     * @param fileLink    附件链接(必要)
     * @param fileKey     附件关键字(必要)
     * @param fileCreator 附件创建人(为空时不更新)
     */
    default int updateFileLink(String fileId, String fileLink, String fileKey, String fileCreator) {
        if (Utils.isEmpty(fileLink) || Utils.isEmpty(fileKey)) {
//            throw new IllegalArgumentException("必要参数(fileId,fileLink,fileKey)不可为空");
            return 0;
        }
        if (Utils.isEmpty(fileId)) {
            // fileId为空,说明将旧关联数据删除
            List<PubFileRecord> fileRecords = listRecords(fileLink, fileKey);
            List<String> fileIds = new ArrayList<>(fileRecords.size());
            for (PubFileRecord record : fileRecords) {
                fileIds.add(record.getFileId());
            }
            return updateFileStatus(fileIds, STATUS_DELETE);
        } else {
            return updateFileLinks(Arrays.asList(fileId), fileLink, fileKey, fileCreator);
        }
    }

    /**
     * 更新附件链接
     *
     * @param fileIds     附件编号(必要)
     * @param fileLink    附件链接(必要)
     * @param fileKey     附件关键字(必要)
     * @param fileCreator 附件创建人(为空时不更新)
     */
    int updateFileLinks(List<String> fileIds, String fileLink, String fileKey, String fileCreator);

//    /**
//     * 更新文件链接
//     *
//     * @param fileIds  附件编号集合
//     * @param fileLink 附件链接名
//     * @param fileKey  链接关键字
//     * @return int 更新记录数
//     * @deprecated 该方法不建议使用, 仅仅修改链接而不修改旧记录状态, 会让已作废的附件记录无法正确清理
//     *
//     */
//    int updateFileLinks(List<String> fileIds, String fileLink, String fileKey);

    /**
     * 更新文件记录状态
     *
     * @param fileId     附件编号
     * @param fileStatus 附件状态
     * @return int 更新记录数
     */
    default int updateFileStatus(String fileId, String fileStatus) {
        if (Utils.isEmpty(fileId)) {
            return 0;
        }
        return updateFileStatus(Arrays.asList(fileId), fileStatus);
    }

    /**
     * 更新文件记录状态
     *
     * @param fileIds    附件编号集合
     * @param fileStatus 附件状态
     * @return int 更新记录数
     */
    int updateFileStatus(Collection<String> fileIds, String fileStatus);

    /**
     * 更新文件记录状态
     *
     * @param fileLink   附件链接名
     * @param fileKey    链接关键字
     * @param fileStatus 附件状态
     * @return int 更新记录数
     */
    int updateFileStatus(String fileLink, String fileKey, String fileStatus);

    /**
     * 获取文件流
     *
     * @param fileId 附件编号
     * @return InputStream 输入流
     * @throws Exception 文件流异常
     */
    default InputStream getFileInputStream(String fileId) throws Exception {
        return getFileInputStream(get(fileId));
    }

    /**
     * 获取文件流
     *
     * @param fileRecord 附件记录
     * @return InputStream 输入流
     * @throws Exception 文件流异常
     */
    default InputStream getFileInputStream(PubFileRecord fileRecord) throws Exception {
        return getFileInputStream(fileRecord, null);
    }


    /**
     * 获取文件流
     *
     * @param fileRecord 附件记录
     * @param alias      附件别名
     * @return InputStream 输入流
     * @throws Exception 文件流异常
     */
    default InputStream getFileInputStream(PubFileRecord fileRecord, String alias) throws Exception {
        return getFileInputStream(fileRecord, alias, null);
    }

    /**
     * 根据文件记录获取文件流
     *
     * @param fileRecord
     * @param alias
     * @param extension
     * @return
     * @throws IOException
     */
    InputStream getFileInputStream(PubFileRecord fileRecord, String alias, String extension) throws IOException;

    /**
     * 根据文件记录获取文件
     *
     * @param fileRecord 附件记录
     * @return File 附件
     */
    default File getFile(PubFileRecord fileRecord) {
        return getFile(fileRecord, null);
    }

    default String getFileDir(PubFileRecord fileRecord) {
        String fileUrl = fileRecord.getFileUrl();
        int splitIndex = fileUrl.lastIndexOf(getDirSeparator());
        String fileDir = fileUrl.substring(0, splitIndex);
        return getUploadDir() + fileDir + getDirSeparator();
    }

    /**
     * 根据文件记录获取文件
     *
     * @param fileRecord 附件记录
     * @param alias      附件别名
     * @return File 附件
     */
    default File getFile(PubFileRecord fileRecord, String alias) {
        return getFile(fileRecord, alias, null);
    }

    /**
     * 根据文件记录获取文件
     *
     * @param fileRecord 附件记录
     * @param alias      附件别名
     * @param extension  扩展名
     * @return File 附件
     */
    default File getFile(PubFileRecord fileRecord, String alias, String extension) {
        return getFile(fileRecord, alias, extension, true);
    }

    /**
     * 根据文件记录获取文件
     *
     * @param fileRecord   文件记录
     * @param alias        文件别名
     * @param fix2Original 文件找不到时使用原始文件
     * @return File 附件
     */
    File getFile(PubFileRecord fileRecord, String alias, String extension, boolean fix2Original);

    /**
     * 获取附件大小
     *
     * @param fileRecord 附件记录
     * @return long 附件大小
     */
    default long getFileSize(PubFileRecord fileRecord) {
        return getFileSize(fileRecord, null);
    }

    /**
     * 获取附件大小
     *
     * @param fileRecord 附件记录
     * @param alias      附件别名
     * @return long 附件大小
     */
    default long getFileSize(PubFileRecord fileRecord, String alias) {
        return getFileSize(fileRecord, alias, null);
    }

    /**
     * 获取文件大小
     *
     * @param fileRecord
     * @return
     */
    default long getFileSize(PubFileRecord fileRecord, String fileAlias, String extension) {
        if (fileRecord == null) {
            return 0L;
        }
        if (Utils.isEmpty(fileAlias)) {
            return fileRecord.getFileSize();
        }
        File file = getFile(fileRecord, fileAlias, extension);
        if (file == null || !file.exists()) {
            return 0L;
        }
        return file.length();
    }

    /**
     * 查询可用的文件记录
     *
     * @param fileLink 附件链接名
     * @param fileKey  链接关键字
     * @return List&lt;PubFileRecord&gt; 附件记录集合
     */
    List<PubFileRecord> listRecords(String fileLink, String fileKey);

    /**
     * 查询可用的文件记录
     *
     * @param fileLink 附件链接名
     * @param fileKey  链接关键字
     * @return List&lt;PubFileRecord&gt; 附件记录集合
     * @see #listRecords(String, String)
     */
    @Deprecated
    default List<PubFileRecord> findFileRecords(String fileLink, String fileKey) {
        return listRecords(fileLink, fileKey);
    }

    /**
     * 获取同一个模块下多个数据的附件
     *
     * @param fileLink 附件链接名
     * @param fileKeys 链接关键字集合
     * @return Map&lt;String, List&lt;PubFileRecord&gt;&gt; 附件记录集合
     */
    Map<String, List<PubFileRecord>> getRecords(String fileLink, Collection<String> fileKeys);

    /**
     * 获取同一个模块下多个数据的附件
     *
     * @param fileLink 附件链接名
     * @param fileKeys 链接关键字集合
     * @return Map&lt;String, List&lt;PubFileRecord&gt;&gt; 附件记录集合
     * @see #getRecords(String, Collection)
     */
    @Deprecated
    default Map<String, List<PubFileRecord>> findFileRecords(String fileLink, Collection<String> fileKeys) {
        return getRecords(fileLink, fileKeys);
    }

    /**
     * 根据链接查询文件数据项
     *
     * @param fileLink 附件链接名
     * @param fileKey  链接关键字
     * @return List&lt;UploadItem&gt; 附件项集合
     */
    default List<UploadItem> listUploadItems(String fileLink, String fileKey) {
        if (Utils.isEmpty(fileLink) || Utils.isEmpty(fileKey)) {
            return Collections.emptyList();
        }
        Map<String, List<UploadItem>> items = getUploadItems(fileLink, Arrays.asList(fileKey));
        List<UploadItem> list = items.get(fileLink);
        return list == null ? new ArrayList<>(0) : list;
    }

    /**
     * 根据链接查询文件数据项
     *
     * @param fileLink 附件链接名
     * @param fileKey  链接关键字
     * @return List&lt;UploadItem&gt; 附件项集合
     * @see #listUploadItems(String, String)
     */
    @Deprecated
    default List<UploadItem> findUploadItems(String fileLink, String fileKey) {
        return listUploadItems(fileLink, fileKey);
    }

    /**
     * 根据链接查询文件数据项
     *
     * @param fileLink 附件链接名
     * @param fileKeys 链接关键字集合
     * @return Map&lt;String, List&lt;UploadItem&gt;&gt; 附件项集合
     */
    Map<String, List<UploadItem>> getUploadItems(String fileLink, Collection<String> fileKeys);

    /**
     * 根据链接查询文件数据项
     *
     * @param fileLink 附件链接名
     * @param fileKeys 链接关键字集合
     * @return Map&lt;String, List&lt;UploadItem&gt;&gt; 附件项集合
     * @see #getUploadItems(String, Collection)
     */
    @Deprecated
    default Map<String, List<UploadItem>> findUploadItems(String fileLink, Collection<String> fileKeys) {
        return getUploadItems(fileLink, fileKeys);
    }

    /**
     * 批量获取文件数据项
     *
     * @param fileIds 附件编号集合
     * @return List&lt;UploadItem&gt;
     */
    List<UploadItem> listUploadItems(String... fileIds);

    /**
     * 批量获取文件数据项
     *
     * @param fileIds 附件编号集合
     * @return List&lt;UploadItem&gt;
     * @see #listUploadItems(String...)
     */
    @Deprecated
    default List<UploadItem> findUploadItems(String... fileIds) {
        return listUploadItems(fileIds);
    }

    /**
     * 根据附件编号获取上传项
     *
     * @param fileId 附件编号
     * @return UploadItem
     */
    default UploadItem getUploadItem(String fileId) {
        return parseUploadItem(get(fileId));
    }

    /**
     * 转换成文件数据项
     *
     * @param itemJson 附件项json
     * @return List&lt;UploadItem&gt;
     */
    default List<UploadItem> parseUploadItems(String itemJson) {
        if (Utils.notEmpty(itemJson)) {
            try {
                List<UploadItem> itemList = JsonUtil.parseArray(itemJson, UploadItem.class);
                return itemList;
            } catch (Exception e) {
                LogUtil.error("转换成文件数据项异常", e);
            }
        }
        return Collections.emptyList();
    }

    /**
     * 转换成文件数据项
     *
     * @param itemJson 附件项json
     * @return UploadItem 附件项
     */
    default UploadItem parseUploadItem(String itemJson) {
        List<UploadItem> itemList = parseUploadItems(itemJson);
        if (Utils.notEmpty(itemList)) {
            return itemList.get(0);
        }
        return null;
    }

    /**
     * 附件项json转附件编号
     *
     * @param itemJson 附件项json
     * @return List&lt;String&gt; 附件编号集合
     */
    default List<String> parseFileIds(String itemJson) {
        List<UploadItem> itemList = parseUploadItems(itemJson);
        List<String> fileIds = new ArrayList<>(itemList.size());
        for (UploadItem item : itemList) {
            try {
                String fileId = decrypt(item.getId());
                if (Utils.isEmpty(fileId)) {
                    continue;
                }
                fileIds.add(fileId);
            } catch (Exception e) {
                LogUtil.error("文件编号解析异常", e);
            }
        }
        return fileIds;
    }

    /**
     * 获取第1个附件编号
     *
     * @param itemJson 附件项json
     * @return String 附件编号
     */
    default String parseFileId(String itemJson) {
        List<String> fileIds = parseFileIds(itemJson);
        if (Utils.notEmpty(fileIds)) {
            return fileIds.get(0);
        }
        return null;
    }

    /**
     * 转换成文件数据项
     *
     * @param fileRecords 附件记录
     * @return List&lt;UploadItem&gt; 附件项集合
     */
    default List<UploadItem> parseUploadItems(List<PubFileRecord> fileRecords) {
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
     * @param fileRecord 附件记录
     * @return UploadItem 附件项
     */
    default UploadItem parseUploadItem(PubFileRecord fileRecord) {
        return parseUploadItem(fileRecord, false);
    }

    default UploadItem parseUploadItem(PubFileRecord fileRecord, boolean remainRecord) {
        if (fileRecord == null) {
            return null;
        }
        UploadItem item = new UploadItem();
        String encId = encrypt(fileRecord.getFileId());
        item.setId(encId);
        item.setName(fileRecord.getFileName());
        item.setExtension(FileUtil.getExtension(fileRecord.getFileName()));
        item.setSize(fileRecord.getFileSize());
        if (remainRecord) {
            item.setFileRecord(fileRecord);
        }
        return item;
    }

    /**
     * 拷贝附件记录
     *
     * @param fileItems 附件项
     * @param fileLink  附件关联
     * @param fileKey   关联关键字
     * @return List&lt;PubFileRecord&gt; 拷贝完成附件记录列表
     */
    default List<PubFileRecord> copyRecordsByItems(List<UploadItem> fileItems, String fileLink, String fileKey) {
        if (Utils.isEmpty(fileItems) || Utils.isEmpty(fileLink) || Utils.isEmpty(fileKey)) {
            throw new IllegalArgumentException("every parameter can not be empty.");
        }
        List<String> fileIds = new ArrayList<>(fileItems.size());
        for (UploadItem item : fileItems) {
            fileIds.add(decrypt(item.getId()));
        }
        return copyRecords(fileIds, fileLink, fileKey);
    }

    /**
     * 拷贝附件记录
     *
     * @param fileIds  附件编号
     * @param fileLink 附件关联
     * @param fileKey  关联关键字
     * @return List&lt;PubFileRecord&gt; 拷贝完成附件记录列表
     */
    List<PubFileRecord> copyRecords(List<String> fileIds, String fileLink, String fileKey);

}
