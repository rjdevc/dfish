package com.rongji.dfish.framework.plugin.lob.dao;

import com.rongji.dfish.framework.dao.FrameworkDao;
import com.rongji.dfish.framework.plugin.lob.entity.PubLob;

import java.util.Collection;
import java.util.Map;

/**
 * lob的数据访问层
 *
 * @author lamontYu
 * @version 1.1 改造成接口实现模式 lamontYu 2019-12-05
 * @create 2019-09-23
 * @since 3.2
 */
public interface LobDao extends FrameworkDao<PubLob, String> {

    /**
     * 更新lob内容
     *
     * @param lobId      编号
     * @param lobContent 内容
     * @return int 更新条数,0代表未更新
     */
    int updateContent(String lobId, String lobContent);

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
     * @return int 更新条数,0代表未更新
     */
    int archive(String lobId);

    /**
     * 批量获取Lob内容
     *
     * @param lobIds 编号
     * @return Map&lt;String, String&gt;
     */
    Map<String, String> getContents(Collection<String> lobIds);

}
