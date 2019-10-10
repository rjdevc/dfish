package com.rongji.dfish.framework.plugin.lob.dao;

import com.rongji.dfish.base.Utils;
import com.rongji.dfish.framework.dao.BaseDao;
import com.rongji.dfish.framework.plugin.lob.entity.PubLob;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class LobDao extends BaseDao<PubLob, String> {

    public int updateLob(String lobId, String lobContent) {
        if (Utils.isEmpty(lobId)) {
            return 0;
        }
        return pubCommonDAO.bulkUpdate("UPDATE PubLob t SET t.lobContent=? WHERE t.lobId=?", lobContent, lobId);
    }

    public Map<String, String> findLobContentBatch(Collection<String> lobIds) {
        if (Utils.isEmpty(lobIds)) {
            return Collections.emptyMap();
        }
        Set<String> idSet = new HashSet<>(lobIds);
        for (Iterator<String> iter = idSet.iterator(); iter.hasNext();) {
            String key = iter.next();
            if (Utils.isEmpty(key)) {
                iter.remove();
            }
        }
        if (Utils.isEmpty(idSet)) {
            return Collections.emptyMap();
        }
        @SuppressWarnings("unchecked")
        List<PubLob> dataList = (List<PubLob>) pubCommonDAO.getQueryList("FROM PubLob t WHERE t.lobId IN(" + getParamStr(idSet.size()) + ")", idSet.toArray());
        Map<String, String> result = new HashMap<>(dataList.size());
        for (PubLob data : dataList) {
            result.put(data.getLobId(), data.getLobContent());
        }
        return result;
    }

}
