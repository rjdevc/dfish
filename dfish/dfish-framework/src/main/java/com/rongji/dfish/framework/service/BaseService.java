package com.rongji.dfish.framework.service;

import java.io.Serializable;

import com.rongji.dfish.framework.IdGenerator;
import com.rongji.dfish.framework.dao.BaseDao;

/**
 * 
 * @author DFish Team
 *
 * @param <T> 实体对象类型Entity
 * @param <ID> ID对象类型通常是String
 */
public abstract class BaseService<T, ID extends Serializable> extends BaseDao<T, ID> {
	
	public String getNewId() {
		return IdGenerator.getSortedId32();
	}
	
}
