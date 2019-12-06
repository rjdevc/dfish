package com.rongji.dfish.framework.hibernate.plugin.lob.dao.impl;

import com.rongji.dfish.base.Utils;
import com.rongji.dfish.framework.hibernate.dao.impl.FrameworkDao4Hibernate;
import com.rongji.dfish.framework.plugin.lob.dao.LobDao;
import com.rongji.dfish.framework.plugin.lob.entity.PubLob;

import java.util.*;

/**
 * lob的hibernate实现dao层
 *
 * @author lamontYu
 * @create 2019-12-05
 */
public class LobDao4Hibernate extends FrameworkDao4Hibernate<PubLob, String> implements LobDao {
    @Override
    public int updateContent(String lobId, String lobContent) {
        if (Utils.isEmpty(lobId)) {
            return 0;
        }
        return bulkUpdate("UPDATE PubLob t SET t.lobContent=? WHERE t.lobId=?", new Object[]{lobContent, lobId});
    }

    @Override
    public int archive(String lobId) {
        if (Utils.isEmpty(lobId)) {
            return 0;
        }
        return bulkUpdate("UPDATE PubLob t SET t.archiveFlag=? WHERE t.lobId=?", new Object[]{"1", lobId});
    }

    @Override
    public Map<String, String> getContents(Collection<String> lobIds) {
        if (Utils.isEmpty(lobIds)) {
            return Collections.emptyMap();
        }
        List<Object[]> list = (List<Object[]>) list("SELECT t.lobId,t.lobContent FROM PubLob t WHERE t.lobId IN(" + getParamStr(lobIds.size()) + ")",
                lobIds.toArray());
        Map<String, String> dataMap = new HashMap<>(list.size());
        for (Object[] data : list) {
            dataMap.put((String) data[0], (String) data[1]);
        }
        return dataMap;
    }
}
