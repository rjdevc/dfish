package com.rongji.dfish.framework.mybatis.plugin.lob.dao.impl;

import com.rongji.dfish.framework.mybatis.dao.impl.FrameworkDao4Mybatis;
import com.rongji.dfish.framework.plugin.lob.dao.LobDao;
import com.rongji.dfish.framework.plugin.lob.entity.PubLob;

/**
 * @author lamontYu
 * @create 2019-12-05
 */
public interface LobDao4Mybatis extends FrameworkDao4Mybatis<PubLob, String>, LobDao {
}
