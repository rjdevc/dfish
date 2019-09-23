package com.rongji.dfish.framework.plugin.lob.service;

import com.rongji.dfish.base.Utils;
import com.rongji.dfish.framework.plugin.lob.dao.LobDao;
import com.rongji.dfish.framework.plugin.lob.entity.PubLob;
import com.rongji.dfish.framework.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

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
		getDao().save(pubLob);
		return lobId;
	}
	
	public int updateLob(String lobId, String lobContent) {
		return getDao().updateLob(lobId, lobContent);
	}
	
//	public int[] updateLobBatch(List<PubLob> lobs) {
//		// FIXME 批量方法需要验证完善
//		if (Utils.isEmpty(lobs)) {
//			return new int[0];
//		}
//		
//		List<Object[]> batchArgs = new ArrayList<Object[]>();
//		for (PubLob lob : lobs) {
//			// 这里还需要非空判断
//			batchArgs.add(new Object[]{ lob.getLobContent(), lob.getLobId() });
//		}
//		return pubCommonDAO.batchUpdate("UPDATE PubLob t SET t.lobContent=? WHERE t.lobId=?", batchArgs);
//	}
	
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
