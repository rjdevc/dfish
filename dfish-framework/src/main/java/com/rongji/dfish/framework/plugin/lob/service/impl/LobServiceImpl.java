package com.rongji.dfish.framework.plugin.lob.service.impl;

import com.rongji.dfish.base.exception.MarkedException;
import com.rongji.dfish.base.util.Utils;
import com.rongji.dfish.framework.plugin.lob.dao.LobDao;
import com.rongji.dfish.framework.plugin.lob.entity.PubLob;
import com.rongji.dfish.framework.plugin.lob.service.LobService;
import com.rongji.dfish.framework.service.impl.AbstractFrameworkService4Simple;

import javax.annotation.Resource;
import java.util.*;

/**
 * lob数据服务层默认实现
 *
 * @author lamontYu
 * @date 2019-09-23
 * @since 5.0
 */
public class LobServiceImpl extends AbstractFrameworkService4Simple<PubLob, String> implements LobService {
    @Resource(name = "lobDao")
    private LobDao dao;

    @Override
    public LobDao getDao() {
        return dao;
    }

    public void setDao(LobDao dao) {
        this.dao = dao;
    }

    @Override
    public String saveLob(String lobContent) throws Exception {
        if (Utils.isEmpty(lobContent)) {
            throw new MarkedException("文本内容不能为空");
        }
        String lobId = getNewId();
        PubLob pubLob = new PubLob();
        pubLob.setLobId(lobId);
        pubLob.setLobContent(lobContent);
        pubLob.setOperTime(new Date());
        pubLob.setArchiveFlag("0");
        getDao().save(pubLob);
        return lobId;
    }

    @Override
    public int updateContent(String lobId, String lobContent) {
        return getDao().updateContent(lobId, lobContent, new Date());
    }

    @Override
    public int archive(String lobId) {
        return getDao().archive(lobId, "1", new Date());
    }

    @Override
    public String getContent(String lobId) {
        Map<String, String> lobMap = getContents(lobId);
        return lobMap.get(lobId);
    }

    @Override
    public Map<String, String> getContents(String... lobIds) {
        if (Utils.isEmpty(lobIds)) {
            return Collections.emptyMap();
        }
        return getContents(Arrays.asList(lobIds));
    }

    @Override
    public Map<String, String> getContents(Collection<String> lobIds) {
        return getDao().getContents(lobIds);
    }

}
