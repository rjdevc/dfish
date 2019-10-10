package com.rongji.dfish.framework.dao;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Id;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateCallback;

import com.rongji.dfish.framework.FrameworkHelper;

/**
 * 
 * @author DFish Team
 *
 * @param <T> 实体对象类型Entity
 * @param <ID> ID对象类型通常是String
 */
public abstract class BaseDao<T, ID extends Serializable> {
	@Autowired
	protected PubCommonDAO pubCommonDAO;

	public PubCommonDAO getPubCommonDAO() {
		return pubCommonDAO;
	}

	public void setPubCommonDAO(PubCommonDAO pubCommonDAO) {
		this.pubCommonDAO = pubCommonDAO;
	}
	@SuppressWarnings("unchecked")
	@Deprecated
	public T find(ID id) {
		if(id==null){
			return null;
		}
		Class<?> entityClass = getEntiyType(getClass());
		String className=entityClass.getName();
		String idName = getIdName(entityClass);
		return (T) pubCommonDAO.queryAsAnObject("FROM "+className+" t WHERE t."+idName+"=?", id);
	}
	
	protected static String getIdName(Class<?> entityClass) {
		String idName=null;
		for(Method m:entityClass.getMethods()){
			if(m.getAnnotation(Id.class)!=null){
				String methodsName=m.getName();
				if(methodsName.startsWith("get")){
					idName=methodsName.substring(3);
					char c=idName.charAt(0);
					if(c>='A'&&c<='Z'){
						idName=((char)(c+32))+ idName.substring(1);
					}
				}
				break;
			}
		}
		return idName;
	}
	
	/**
	 * 根据给定的ID列表 返回指定该ID指定的列表
	 * 如果其中，ID指定的数据不存在，返回结果List中，相对应的位置，为null
	 * 如果ID 有重复，返回结果也是有重复。但并不会进行数据库的多次查询
	 * 改方法将批量调用数据库根据主键查询的方法，形如
	 * SELECT t.xx FROM Xxx t WHERE t.id=? OR t.id=?;
	 * 每个批次默认不多于50个ID。超过，则进行下一批查询。
	 * @param ids ID
	 * @return List
	 */
	@SuppressWarnings("unchecked")
    public List<T> findAll(List<ID> ids) {
		if(ids==null){
			return null;
		}
		final Class<?>entityClass=getEntiyType(getClass());
		String idName=null;
		Method getterMethod=null;
		for(Method m:entityClass.getMethods()){
			if(m.getAnnotation(Id.class)!=null){
				String methodsName=m.getName();
				if(methodsName.startsWith("get")||methodsName.startsWith("set")){
					idName=methodsName.substring(3);
					char c=idName.charAt(0);
					if(c>='A'&&c<='Z'){
						idName=((char)(c+32))+ idName.substring(1);
					}
					if(methodsName.startsWith("get")){
						getterMethod=m;
					}
				}
				break;
			}
		}
		if(idName==null){
			for(Field f:entityClass.getFields()){
				if(f.getAnnotation(Id.class)!=null){
					idName=f.getName();
					break;
				}
			}
		}
		if(getterMethod==null){
			char c=idName.charAt(0);
			if(c>='a'&&c<='z'){
				String getterName="get"+((char)(c-32))+ idName.substring(1);
				try {
					getterMethod=entityClass.getMethod(getterName, NO_PARAMS);
				} catch (Exception e) {
					FrameworkHelper.LOG.error("", e);
				}
			}
		}
		final String idName2=idName;
		Set<ID> idSet = new HashSet<ID>(ids);
		idSet.remove(null);
		final List<ID> norepeat=new ArrayList<ID>(idSet);
		List<T> dbrs=(List<T>) pubCommonDAO.getHibernateTemplate().execute(new HibernateCallback<List<T>>() {
            @Override
			public List<T> doInHibernate(Session session) throws HibernateException, SQLException {
				List<ID> tofetch=norepeat;
				List<T> result=new ArrayList<T>();
				while(tofetch.size()>0){
					if(tofetch.size()>FETCH_SIZE){
						List<ID> cur=tofetch.subList(0, FETCH_SIZE);
						tofetch=tofetch.subList(FETCH_SIZE, tofetch.size());
						result.addAll(session.createCriteria(entityClass).add(Restrictions.in(idName2, cur)).list());
					}else{
						result.addAll(session.createCriteria(entityClass).add(Restrictions.in(idName2, tofetch)).list());
						tofetch=tofetch.subList(tofetch.size(), tofetch.size());
					}
				}
				return result;
			}
		});
		//重新排序并且,补充没查到的数据
		HashMap<ID,T> map=new HashMap<ID,T>();
		for(T item:dbrs){
			try {
				ID i = (ID) getterMethod.invoke(item, NO_ARGS);
				map.put(i, item);
			} catch (Exception e) {
				FrameworkHelper.LOG.error("", e);
			}
		}
		List<T>result=new ArrayList<T> ();
		for(ID i:ids){
			result.add(map.get(i));
		}
		return result;
	}
	private static final Object[] NO_ARGS=new Object[0];
	private static final Class<?>[] NO_PARAMS=new Class<?>[0];
	
	protected Class<?> getEntiyType(){
		return getEntiyType(getClass());
	}
	protected static Class<?> getEntiyType(Class<?>clz){
//		clz.getSuperclass();
		Class<?> workinClz=clz;
		while(true){
			if(workinClz==Object.class){
				break;
			}
			Type genType = workinClz.getGenericSuperclass();
			if (!(genType instanceof ParameterizedType)) {
				workinClz=workinClz.getSuperclass();
				continue;
			}
			
			Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
			if (params==null||params.length==0||!(params[0] instanceof Class)) {
				workinClz=workinClz.getSuperclass();
				continue;
			}
			Class<?> entityClass= (Class<?>) params[0];
			return entityClass;
		}
		throw new UnsupportedOperationException("can not recognize the entity TYPE of "+clz.getName());
	}
	/**
	 * 利用hibernate默认的get方法获取对象
	 * 注意此方法，取出的对象如果进行了set操作，很可能会影响其他线程
	 * 并且不用显式调用update也可能会被写入到数据库。
	 * @param id
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public T get(ID id) {
		if(id==null){
			return null;
		}
		final Class<?>entityClass=getEntiyType(getClass());
		return (T)pubCommonDAO.getHibernateTemplate().get(entityClass, id);
	}
	public void update(T entity) {
		pubCommonDAO.update(entity);
	}
	public<S extends T> void deleteAll(Collection<S> entities) {
		if(entities==null){
			return ;
		}
		pubCommonDAO.getHibernateTemplate().deleteAll(entities);
	}
	public void delete(T entity) {
		pubCommonDAO.delete(entity);
	}
	public void delete(ID id) {
		pubCommonDAO.delete(get(id));
	}
	public void save(T entity) {
		pubCommonDAO.save(entity);
	}
	
	public static final int BATCH_SIZE = 512;
	public static final int FETCH_SIZE = 50;
	
	public static String getParamStr(int paramCount) {
		StringBuilder paramStr = new StringBuilder();
		boolean isFirst = true;
		for (int i = 0; i < paramCount; ++i) {
			if (isFirst) {
				isFirst = false;
			} else {
				paramStr.append(',');
			}
			paramStr.append('?');
		}
		return paramStr.toString();
	}
}
