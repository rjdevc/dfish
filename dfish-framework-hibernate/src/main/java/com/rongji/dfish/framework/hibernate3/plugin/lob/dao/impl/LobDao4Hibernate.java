package com.rongji.dfish.framework.hibernate3.plugin.lob.dao.impl;

import com.rongji.dfish.base.util.Utils;
import com.rongji.dfish.framework.hibernate3.dao.impl.FrameworkDao4Hibernate;
import com.rongji.dfish.framework.plugin.lob.dao.LobDao;
import com.rongji.dfish.framework.plugin.lob.entity.PubLob;

import java.util.Date;

/**
 * lob的hibernate实现dao层
 *
 * @author lamontYu
 * @since DFish5.0
 */
public class LobDao4Hibernate extends FrameworkDao4Hibernate<PubLob, String> implements LobDao {
    @Override
    public int updateLobData(String lobId, byte[] lobData, Date operTime) {
        if (Utils.isEmpty(lobId)) {
            return 0;
        }
        return bulkUpdate("UPDATE PubLob t SET t.lobData=?,t.operTime=? WHERE t.lobId=?", new Object[]{lobData, operTime, lobId});
    }

    @Override
    public int archive(String lobId, String archiveFlag, Date archiveTime) {
        if (Utils.isEmpty(lobId)) {
            return 0;
        }
        return bulkUpdate("UPDATE PubLob t SET t.archiveFlag=?,t.archiveTime=? WHERE t.lobId=?", new Object[]{archiveFlag, archiveTime, lobId});
    }

}
