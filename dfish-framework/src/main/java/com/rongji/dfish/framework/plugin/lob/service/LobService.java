package com.rongji.dfish.framework.plugin.lob.service;

import com.rongji.dfish.base.Utils;
import com.rongji.dfish.framework.plugin.lob.dao.LobDao;
import com.rongji.dfish.framework.plugin.lob.entity.PubLob;
import com.rongji.dfish.framework.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class LobService extends BaseService<PubLob, PubLob, String> {
	@Autowired
	private LobDao dao;

	@Override
	public LobDao getDao() {
		return dao;
	}

	public void setDao(LobDao dao) {
		this.dao = dao;
	}

	public String saveLob(String lobContent) throws Exception {
		String lobId = getNewId();
		PubLob pubLob = new PubLob();
		pubLob.setLobId(lobId);
		pubLob.setLobContent(lobContent);
		pubLob.setOperTime(new Date());
		pubLob.setArchiveFlag("0");
		getDao().save(pubLob);
		return lobId;
	}
	
	public int updateLob(String lobId, String lobContent) {
		return getDao().updateLob(lobId, lobContent);
	}

	public int archiveLob(String lobId) {
		return getDao().archiveLob(lobId);
	}
	
	public String getLobContent(String lobId) {
		Map<String, String> lobMap = findLobContentBatch(lobId);
		return lobMap.get(lobId);
	}
	
	public Map<String, String> findLobContentBatch(String... lobIds) {
		if (Utils.isEmpty(lobIds)) {
			return Collections.emptyMap();
		}
		return findLobContentBatch(Arrays.asList(lobIds));
	}
	
	public Map<String, String> findLobContentBatch(Collection<String> lobIds) {
		return getDao().findLobContentBatch(lobIds);
	}
	
}
