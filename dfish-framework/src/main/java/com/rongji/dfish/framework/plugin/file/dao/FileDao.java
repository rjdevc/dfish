package com.rongji.dfish.framework.plugin.file.dao;

import com.rongji.dfish.framework.dao.FrameworkDao;
import com.rongji.dfish.framework.plugin.file.dto.FileQueryParam;
import com.rongji.dfish.framework.plugin.file.entity.PubFileRecord;
import com.rongji.dfish.ui.form.UploadItem;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 附件数据访问层
 * @author lamontYu
 * @create 2019-12-05
 */
public interface FileDao extends FrameworkDao<PubFileRecord, String> {


    /**
     * 更新文件记录状态
     *
     * @param fileId   附件编号
     * @param fileStatus 附件状态
     * @return int 更新记录数
     */
    int updateFileStatus(String fileId, String fileStatus);

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
     * 更新文件链接
     *
     * @param fileId 附件编号
     * @param fileLink 链接名
     * @return int 更新数量
     */
    int updateFileLink(String fileId, String fileLink);

    /**
     * 更新文件链接
     *
     * @param fileIds 附件编号集合
     * @param fileLink 附件链接名
     * @param fileKey  链接关键字
     * @return int 更新记录数
     */
    int updateFileLink(List<String> fileIds, String fileLink, String fileKey);

    /**
     * 获取同一个模块下多个数据的附件
     *
     * @param fileLink 附件链接名
     * @param fileKeys 链接关键字集合
     * @param fileStatus 附件状态
     * @return Map&lt;String, List&lt;PubFileRecord&gt;&gt; 附件记录集合
     */
    Map<String, List<PubFileRecord>> getRecords(String fileLink, Collection<String> fileKeys, String[] fileStatus);

}
