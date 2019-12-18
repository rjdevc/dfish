package com.rongji.dfish.framework.hibernate4.plugin.lob.dao.impl;

import com.rongji.dfish.base.util.Utils;
import com.rongji.dfish.framework.hibernate4.dao.impl.FrameworkDao4Hibernate;
import com.rongji.dfish.framework.plugin.lob.dao.LobDao;
import com.rongji.dfish.framework.plugin.lob.entity.PubLob;

import java.util.Date;

/**
 * lob的hibernate实现dao层
 *
 * @author lamontYu
 * @date 2019-12-05
 * @since 5.0
 */
public class LobDao4Hibernate extends FrameworkDao4Hibernate<PubLob, String> implements LobDao {
    @Override
    public int updateContent(String lobId, String lobContent, Date operTime) {
        if (Utils.isEmpty(lobId)) {
            return 0;
        }
        return bulkUpdate("UPDATE PubLob t SET t.lobContent=?,t.operTime=? WHERE t.lobId=?", new Object[]{lobContent, operTime, lobId});
    }

    @Override
    public int archive(String lobId, String archiveFlag, Date archiveTime) {
        if (Utils.isEmpty(lobId)) {
            return 0;
        }
        return bulkUpdate("UPDATE PubLob t SET t.archiveFlag=?,t.archiveTime=? WHERE t.lobId=?", new Object[]{archiveFlag, archiveTime, lobId});
    }

}
