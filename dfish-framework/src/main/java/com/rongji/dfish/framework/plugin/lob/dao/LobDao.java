package com.rongji.dfish.framework.plugin.lob.dao;

import com.rongji.dfish.base.util.Utils;
import com.rongji.dfish.framework.dao.FrameworkDao;
import com.rongji.dfish.framework.plugin.lob.entity.PubLob;

import java.util.*;

/**
 * lob的数据访问层
 *
 * @author lamontYu
 * @version 1.1 改造成接口实现模式 lamontYu 2019-12-05
 * @date 2019-09-23
 * @since 3.2
 */
public interface LobDao extends FrameworkDao<PubLob, String> {

    /**
     * 更新lob内容(字符串)
     *
     * @param lobId      编号
     * @param lobContent 内容
     * @param operTime 操作时间
     * @return int 更新条数,0代表未更新
     */
//    int updateContent(String lobId, String lobContent, Date operTime);

    /**
     * 更新lob内容（文件）
     *
     * @param lobId      编号
     * @param lobData 内容
     * @param operTime 操作时间
     * @return int 更新条数,0代表未更新
     */
    int updateLobData(String lobId, byte[] lobData, Date operTime);

    /**
     if (Utils.isEmpty(lobId)) {
     return 0;
     }
     return pubCommonDAO.bulkUpdate("UPDATE PubLob t SET t.lobContent=?,t.operTime=? WHERE t.lobId=?", new Object[]{lobContent, new Date(), lobId});

     if (Utils.isEmpty(lobId)) {
     return 0;
     }
     return pubCommonDAO.bulkUpdate("UPDATE PubLob t SET t.archiveFlag=?,t.archiveTime=? WHERE t.lobId=?",
     new Object[]{"1", new Date(), lobId});


     if (Utils.isEmpty(lobIds)) {
     return Collections.emptyMap();
     }
     Set<String> idSet = new HashSet<>(lobIds);
     for (Iterator<String> iter = idSet.iterator(); iter.hasNext(); ) {
     String key = iter.next();
     if (Utils.isEmpty(key)) {
     iter.remove();
     }
     }
     if (Utils.isEmpty(idSet)) {
     return Collections.emptyMap();
     }
     @SuppressWarnings("unchecked") List<PubLob> dataList = (List<PubLob>) pubCommonDAO.getQueryList("FROM PubLob t WHERE t.lobId IN(" + getParamStr(idSet.size()) + ")", idSet.toArray());
     Map<String, String> result = new HashMap<>(dataList.size());
     for (PubLob data : dataList) {
     result.put(data.getLobId(), data.getLobContent());
     }
     return result;

     */

    /**
     * 归档Lob数据
     *
     * @param lobId 编号
     * @param archiveFlag 归档状态
     * @param archiveTime 归档时间
     * @return int 更新条数,0代表未更新
     */
    int archive(String lobId, String archiveFlag, Date archiveTime);

    /**
     * 批量获取Lob内容
     *
     * @param lobIds 编号
     * @return Map&lt;String, String&gt;
     */
    default Map<String, byte[]> getLobDatas(Collection<String> lobIds) {
        if (Utils.isEmpty(lobIds)) {
            return Collections.emptyMap();
        }
        Map<String,PubLob> lobs = gets(lobIds);
        Map<String, byte[]> contents = new HashMap<>(lobs.size());
        for (PubLob lob : lobs.values()) {
            contents.put(lob.getLobId(), lob.getLobData());
        }
        return contents;
    }

}
