package com.rongji.dfish.framework.mybatis.plugin.lob.dao.impl;

import com.rongji.dfish.framework.mybatis.dao.impl.FrameworkDao4Mybatis;
import com.rongji.dfish.framework.plugin.lob.dao.LobDao;
import com.rongji.dfish.framework.plugin.lob.entity.PubLob;
import org.apache.ibatis.annotations.Param;

import java.util.Date;

/**
 * Mybatis模式下，存储lob的数据库操作定义
 * @author lamontYu
 * @date 2019-12-05
 */
public interface LobDao4Mybatis extends FrameworkDao4Mybatis<PubLob, String>, LobDao {

    @Override
    default String getEntityId(PubLob entity) {
        if (entity == null) {
            return null;
        }
        return entity.getLobId();
    }

   /* @Override
    int updateContent(@Param("lobId") String lobId, @Param("lobContent") String lobContent, @Param("operTime") Date operTime);*/

    @Override
    int updateLobData(@Param("lobId") String lobId, @Param("lobData") byte[] lobData, @Param("operTime") Date operTime);

    @Override
    int archive(@Param("lobId") String lobId, @Param("archiveFlag") String archiveFlag, @Param("archiveTime") Date archiveTime);
}
