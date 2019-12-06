package com.rongji.dfish.framework.mybatis.plugin.lob.dao.impl;

import com.rongji.dfish.framework.dto.RequestParam;
import com.rongji.dfish.framework.mybatis.dao.impl.FrameworkDao4Mybatis;
import com.rongji.dfish.framework.plugin.lob.dao.LobDao;
import com.rongji.dfish.framework.plugin.lob.entity.PubLob;
import org.apache.ibatis.session.RowBounds;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @author lamontYu
 * @create 2019-12-05
 */
public class LobDao4Mybatis implements FrameworkDao4Mybatis<PubLob, String>, LobDao {
    @Override
    public List<PubLob> list(RowBounds rowBounds, RequestParam requestParam) {
        return null;
    }

    @Override
    public PubLob get(String s) {
        return null;
    }

    @Override
    public Map<String, PubLob> gets(Collection<String> strings) {
        return null;
    }

    @Override
    public int save(PubLob entity) {
        return 0;
    }

    @Override
    public int update(PubLob entity) {
        return 0;
    }

    @Override
    public int delete(PubLob entity) {
        return 0;
    }

    @Override
    public int delete(String s) {
        return 0;
    }

    @Override
    public int deleteAll(Collection<PubLob> entities) {
        return 0;
    }

    @Override
    public int updateContent(String lobId, String lobContent) {
        return 0;
    }

    @Override
    public int archive(String lobId) {
        return 0;
    }

    @Override
    public Map<String, String> getContents(Collection<String> lobIds) {
        return null;
    }
}
