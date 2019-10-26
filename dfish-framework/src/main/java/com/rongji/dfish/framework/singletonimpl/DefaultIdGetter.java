package com.rongji.dfish.framework.singletonimpl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.rongji.dfish.framework.NewIdGetter;
import com.rongji.dfish.framework.SystemData;
import com.rongji.dfish.framework.dao.PubCommonDAO;

public class DefaultIdGetter implements NewIdGetter{
	/** 数据库编号缓存 */
	private static Map<String, String> cachedIds=new HashMap<String, String>();

	@Override
    public String getNewId(String clzName, String idName, String initId) {
		// FIXME 这个是本地的取法，不适合集群使用
		clzName = clzName.intern();
		synchronized (clzName) {
			// 如果内存中已经存在则不需要从数据库中读取
			String storedId = cachedIds.get(clzName);
			if (storedId != null) {
				String newId=getNextId(storedId,initId,10);// getNextId(storedId, 10);//ZHL
				cachedIds.put(clzName, newId);
				return newId;
			}
			String dataBaseMaxId = getMaxIdFromDataBase(clzName, idName);
			if (dataBaseMaxId != null && !dataBaseMaxId.equals("")) {
				String newId=getNextId(dataBaseMaxId,initId,10);// getNextId(dataBaseMaxId, 10);//ZHL
				cachedIds.put(clzName, newId);
				return newId;
			}
			cachedIds.put(clzName, initId);
			return initId;
		}

	}

	private static String getMaxIdFromDataBase(String clzName,String idName){
		PubCommonDAO dao = (PubCommonDAO) SystemData.getInstance().getBeanFactory().getBean(
				"PubCommonDAO");
		List<?> list = dao.getQueryList("select max(t." + idName + ") from " + clzName + " t");
		if (list != null && list.size() > 0 && list.get(0) != null) {
			return (String) list.get(0);
		}
		return null;
	}

	/**
	 * 当前的数字加1后以定长的字符串数据.
	 * 增加对预留Id的处理,如果Id>=初始值就在Id的基础上+1;反之就在初始值的基础上+1;//ZHL
	 * 比如初始值是10(即系统需要预留1~9的Id),那么如果此时数据库或缓存中的Id=5,那调用本方法之后nextId=10,跳过10以前的Id;
	 * 
	 * @param id
	 * @param initId
	 * @param radix
	 * @return
	 */
	private static String getNextId(String id,String initId,int radix){
		String nextId=initId;
		long _id=Long.parseLong(id);
		long _initId=Long.parseLong(initId);
		if(_id>=_initId){
			nextId=getNextId(id,radix);
		}else{
			nextId=getNextId(initId,radix);
		}
		
		return nextId;
	}

	/**
	 * 当前的数字加1后以定长的字符串数据 比如说输入0022 得到0023 输入00999得到01000 输入999 得到000
	 * 
	 * @param id
	 * @param radix
	 *            多少进制，最常用的是2/8/10/16/32/36
	 * @return
	 */
	private static String getNextId(String id,int radix){
		long l = Long.parseLong(id, radix);
		l++;
		String targetValue = Long.toString(l, radix);
		int idLen = id.length();
		int nowLen = targetValue.length();
		if (idLen == nowLen) {
			return targetValue;
		} else if (idLen < nowLen) {
			throw new RuntimeException("can not get next id for [" + id + "].");
		}
		StringBuilder sb = new StringBuilder();
		for (int i = nowLen; i < idLen; i++) {
			sb.append('0');
		}
		sb.append(targetValue);
		return sb.toString();
	}
	
}
