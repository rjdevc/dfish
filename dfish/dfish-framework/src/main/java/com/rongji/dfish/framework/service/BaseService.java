package com.rongji.dfish.framework.service;

import com.rongji.dfish.framework.dao.BaseDao;

import java.io.Serializable;

/**
 * 
 * @author DFish Team
 *
 * @param <T> 实体对象类型Entity
 * @param <ID> ID对象类型通常是String
 */
public abstract class BaseService<T, ID extends Serializable> extends BaseDao<T, ID> {

}
