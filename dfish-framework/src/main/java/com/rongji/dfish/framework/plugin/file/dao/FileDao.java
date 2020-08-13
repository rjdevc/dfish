package com.rongji.dfish.framework.plugin.file.dao;

import com.rongji.dfish.base.util.Utils;
import com.rongji.dfish.framework.dao.FrameworkDao;
import com.rongji.dfish.framework.plugin.file.entity.PubFileRecord;

import java.util.*;

/**
 * 附件数据访问层
 * @author lamontYu
 * @since DFish5.0
 */
public interface FileDao extends FrameworkDao<PubFileRecord, String> {


    /**
     * 更新文件记录状态
     *
     * @param fileIds   附件编号集合
     * @param fileStatus 附件状态
     * @param updateTime 更新时间
     * @return int 更新记录数
     */
    int updateFileStatus(Collection<String> fileIds, String fileStatus, Date updateTime);

    /**
     * 更新文件记录状态
     *
     * @param fileLink   附件链接名
     * @param fileKey    链接关键字
     * @param fileStatus 附件状态
     * @param updateTime 更新时间
     * @return int 更新记录数
     */
    int updateFileStatusByLink(String fileLink, String fileKey, String fileStatus, Date updateTime);

//    /**
//     * 更新文件链接
//     *
//     * @param fileId 附件编号
//     * @param fileLink 链接名
//     * @param fileKey    链接关键字
//     * @param fileStatus 附件状态
//     * @param updateTime 更新时间
//     * @return int 更新数量
//     */
//    int updateFileLink(String fileId, String fileLink, String fileKey, String fileCreator,String fileStatus, Date updateTime);

    /**
     * 更新文件链接
     *
     * @param fileIds 附件编号集合
     * @param fileLink 附件链接名
     * @param fileKey  链接关键字
     * @param fileStatus 附件状态
     * @param updateTime 更新时间
     * @return int 更新记录数
     */
    int updateFileLinks(List<String> fileIds, String fileLink, String fileKey, String fileCreator, String fileStatus, Date updateTime);

    /**
     * 获取同一个模块下多个数据的附件
     *
     * @param fileLink 附件链接名
     * @param fileKeys 链接关键字集合
     * @param fileStatus 附件状态
     * @return Map&lt;String, List&lt;PubFileRecord&gt;&gt; 附件记录集合
     */
    default Map<String, List<PubFileRecord>> getRecords(String fileLink, Collection<String> fileKeys, String[] fileStatus) {
        if (Utils.isEmpty(fileLink) || Utils.isEmpty(fileKeys)) {
            return Collections.emptyMap();
        }
        List<PubFileRecord> dataList = listByLink(fileLink, fileKeys, fileStatus);
        Map<String, List<PubFileRecord>> dataMap = new HashMap<>(fileKeys.size());
        for (PubFileRecord data : dataList) {
//            List<PubFileRecord> subList = dataMap.get(data.getFileKey());
//            if (subList == null) {
//                subList = new ArrayList<>();
//                dataMap.put(data.getFileKey(), subList);
//            }
//            subList.add(data);
            dataMap.computeIfAbsent(data.getFileKey(),k->new ArrayList<>()).add(data);
        }
        return dataMap;
    }

    /**
     * 获取同一个模块下多个数据的附件集合
     *
     * @param fileLink 附件链接名
     * @param fileKeys 链接关键字集合
     * @param fileStatus 附件状态
     * @return List&lt;PubFileRecord&gt; 附件记录集合
     */
    List<PubFileRecord> listByLink(String fileLink, Collection<String> fileKeys, String[] fileStatus);

}
