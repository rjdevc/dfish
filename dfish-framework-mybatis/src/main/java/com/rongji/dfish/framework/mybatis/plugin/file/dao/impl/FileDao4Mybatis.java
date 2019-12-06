package com.rongji.dfish.framework.mybatis.plugin.file.dao.impl;

import com.rongji.dfish.framework.mybatis.dao.impl.FrameworkDao4Mybatis;
import com.rongji.dfish.framework.plugin.file.dao.FileDao;
import com.rongji.dfish.framework.plugin.file.entity.PubFileRecord;

/**
 * @author lamontYu
 * @create 2019-12-05
 */
public interface FileDao4Mybatis extends FrameworkDao4Mybatis<PubFileRecord, String>, FileDao {

}
