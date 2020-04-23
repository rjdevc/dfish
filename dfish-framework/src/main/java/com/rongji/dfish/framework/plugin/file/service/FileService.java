package com.rongji.dfish.framework.plugin.file.service;

import com.rongji.dfish.base.util.JsonUtil;
import com.rongji.dfish.base.util.LogUtil;
import com.rongji.dfish.base.util.Utils;
import com.rongji.dfish.framework.plugin.file.dto.UploadItem;
import com.rongji.dfish.framework.plugin.file.entity.PubFileRecord;
import com.rongji.dfish.framework.service.FrameworkService;

import java.io.File;
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
    String CONFIG_TYPES_FILE = "file.types.file";
    /**
     * 附件配置-图片类型
     */
    String CONFIG_TYPES_IMAGE = "file.types.image";

    /**
     * 附件关联
     */
    String LINK_FILE = "FILE";

    /**
     * 附件存储路径
     */
    DateFormat DF_DIR = new SimpleDateFormat("yyyy/MM/dd");

    /**
     * 保存文件以及文件记录
     *
     * @param input            文件输入流
     * @param originalFileName 原始文件名
     * @param fileSize         文件大小
     * @param loginUserId      登录人员
     * @return UploadItem 上传数据项
     * @throws Exception 文件记录保存过程可能出现的业务异常
     */
    UploadItem saveFile(InputStream input, String originalFileName, long fileSize, String loginUserId) throws Exception;

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
     * 文件上传支持的类型
     *
     * @return String
     */
    String getFileTypes();

    /**
     * 图片上传支持的类型
     *
     * @return String
     */
    String getImageTypes();

    /**
     * 更新文件链接
     *
     * @param fileId   附件编号
     * @param fileLink 链接名
     * @param fileKey  关联数据
     * @return int 更新数量
     */
    int updateFileLink(String fileId, String fileLink, String fileKey);

    /**
     * 更新文件链接
     *
     * @param itemJson 附件项json
     * @param fileLink 链接名
     * @param fileKey  链接关键字
     * @return int 更新数量
     */
    default int updateFileLinks(String itemJson, String fileLink, String fileKey) {
        List<UploadItem> itemList = parseUploadItems(itemJson);
        List<PubFileRecord> oldList = getRecords(fileLink, fileKey);
        List<String> newIds = new ArrayList<>(itemList.size());
        for (UploadItem item : itemList) {
            newIds.add(decrypt(item.getId()));
        }
        List<String> oldIds = new ArrayList<>(oldList.size());
        for (PubFileRecord data : oldList) {
            oldIds.add(data.getFileId());
        }
        List<String> insertList = new ArrayList<>(newIds);
        insertList.removeAll(oldIds);
        List<String> deleteList = new ArrayList<>(oldIds);
        deleteList.removeAll(newIds);
        int count = updateFileLinks(insertList, fileLink, fileKey);
        count += updateFileStatus(deleteList, STATUS_DELETE);
        return count + deleteList.size();
    }

    /**
     * 更新文件链接
     *
     * @param fileIds  附件编号集合
     * @param fileLink 附件链接名
     * @param fileKey  链接关键字
     * @return int 更新记录数
     */
    int updateFileLinks(List<String> fileIds, String fileLink, String fileKey);

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
    InputStream getFileInputStream(PubFileRecord fileRecord, String alias) throws Exception;

    /**
     * 根据文件记录获取文件
     *
     * @param fileRecord 附件记录
     * @return File 附件
     */
    default File getFile(PubFileRecord fileRecord) {
        return getFile(fileRecord, null);
    }

    /**
     * 根据文件记录获取文件
     *
     * @param fileRecord 附件记录
     * @param alias      附件别名
     * @return File 附件
     */
    default File getFile(PubFileRecord fileRecord, String alias) {
        return getFile(fileRecord, alias, true);
    }

    /**
     * 根据文件记录获取文件
     *
     * @param fileRecord   文件记录
     * @param alias        文件别名
     * @param fix2Original 文件找不到时使用原始文件
     * @return File 附件
     */
    File getFile(PubFileRecord fileRecord, String alias, boolean fix2Original);

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
    long getFileSize(PubFileRecord fileRecord, String alias);

    /**
     * 查询可用的文件记录
     *
     * @param fileLink 附件链接名
     * @param fileKey  链接关键字
     * @return List&lt;PubFileRecord&gt; 附件记录集合
     */
    List<PubFileRecord> getRecords(String fileLink, String fileKey);

    /**
     * 查询可用的文件记录
     *
     * @param fileLink 附件链接名
     * @param fileKey  链接关键字
     * @return List&lt;PubFileRecord&gt; 附件记录集合
     * @see #getRecords(String, String)
     */
    @Deprecated
    default List<PubFileRecord> findFileRecords(String fileLink, String fileKey) {
        return getRecords(fileLink, fileKey);
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
        if (fileRecord == null) {
            return null;
        }
        UploadItem item = new UploadItem();
        String encId = encrypt(fileRecord.getFileId());
        item.setId(encId);
        item.setName(fileRecord.getFileName());
        item.setSize(fileRecord.getFileSize());
        return item;
    }

    /**
     * 判断扩展名是否支持
     *
     * @param extName     拓展名(不管有没.都支持;即doc和.doc)
     * @param acceptTypes 可接受的类型;格式如:*.doc;*.png;*.jpg;
     * @return boolean 拓展名是否匹配
     */
    default boolean accept(String extName, String acceptTypes) {
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
}
