package com.rongji.dfish.framework.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.rongji.dfish.base.DfishException;
import com.rongji.dfish.base.Page;
import com.rongji.dfish.base.Pagination;
import com.rongji.dfish.base.Utils;

public class PubCommonDAOImpl extends HibernateDaoSupport implements
		PubCommonDAO {
	/**
	 * 运行时间阀值
	 */
	private long executeThreshold = 2000L;
	/**
	 * 运行警告开关
	 */
	private boolean executeWarn = false;
	
	private static boolean debug = false;
	
	public long getExecuteThreshold() {
		return executeThreshold;
	}

	public void setExecuteThreshold(long executeThreshold) {
		this.executeThreshold = executeThreshold;
	}
	
	public boolean isExecuteWarn() {
		return executeWarn;
	}

	public void setExecuteWarn(boolean executeWarn) {
		this.executeWarn = executeWarn;
	}

	public boolean isDebug() {
		return debug;
	}

	public void setDebug(boolean debug) {
		PubCommonDAOImpl.debug = debug;
	}
	
	private static final Log LOG=LogFactory.getLog(PubCommonDAOImpl.class);
	public List<?> getQueryList(final String strSql, final Object... object) {
		
		return call(strSql,new Callable<List<?>>(){
			@Override
			public List<?> call() throws Exception {
				HibernateTemplate template = getHibernateTemplate();
				template.setCacheQueries(true);
				HibernateCallback<?> action = new HibernateCallback<Object>() {
					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {
						Query query = session.createQuery(strSql);
						if (object != null) {
							for (int i = 0; i < object.length; i++) {
								setArgument(query, i, object[i]);
							}
						}
						ArrayList<?> arrayList = (ArrayList<?>) query.list();
						return arrayList;
					}
				};

				List<?> list= (List<?>) template.execute(action);
				return list;
			}
		});
	}

//	/**
//	 * 显示执行结果
//	 * @param sql
//	 * @param beginTimeMillis
//	 */
//	private void traceWithBeginTime(String sql, long beginTimeMillis) {
//		long endTimeMillis = System.currentTimeMillis();
//		long costTime = endTimeMillis - beginTimeMillis;
//		trace(sql,costTime);
//	}
	/**
	 * 显示执行结果
	 * @param sql
	 * @param costTime
	 */
	protected void log(String sql, long costTime) {
		if (executeWarn) {
			if (costTime > executeThreshold) {
				LOG.warn("execute sql[" + sql + "] cost: " + costTime + "ms");
			}
		}
	}

	public int deleteSQL(String strSql, Object... object) {
		final List<?> list = getQueryList(strSql, object);
		if (list != null) {
			if (object != null) {
				call(strSql,new Callable<Object>(){
					@Override
					public Object call() throws Exception {
						getHibernateTemplate().deleteAll(list);
						return null;
					}
				});
			}
		}
		return list == null ? 0 : list.size();
	}

	public void delete(final Object object) {
		if (object != null) {
			call("DELETE "+object.getClass().getSimpleName(),new Callable<Object>(){
				@Override
				public Object call() throws Exception {
					getHibernateTemplate().delete(object);
					return null;
				}
			});
		}
	}

	public void save(final Object object) {
		if (object != null) {
			call("SAVE "+object.getClass().getSimpleName(),new Callable<Object>(){
				@Override
				public Object call() throws Exception {
					getHibernateTemplate().save(object);
					return null;
				}
			});
		}
	}

	public void update(final Object object) {
		if (object != null) {
			call("UPDATE "+object.getClass().getSimpleName(),new Callable<Object>(){
				@Override
				public Object call() throws Exception {
					getHibernateTemplate().update(object);
					return null;
				}
			});
		}
	}

	protected static final void setArgument(Query query, int index, Object o) {
		if (o instanceof java.lang.String) {
			query.setString(index, (String) o);
		} else if (o instanceof java.lang.Integer) {
			query.setInteger(index, ((Integer) o).intValue());
		} else if (o instanceof java.util.Date) {
			query.setTimestamp(index, (java.util.Date) o);
		} else if (o instanceof java.lang.Boolean) {
			query.setBoolean(index, ((Boolean) o).booleanValue());
		} else if (o instanceof java.lang.Byte) {
			query.setByte(index, ((Byte) o).byteValue());
		} else if (o instanceof java.lang.Long) {
			query.setLong(index, ((Long) o).longValue());
		} else if (o instanceof java.lang.Double) {
			query.setDouble(index, ((Double) o).doubleValue());
		} else if (o instanceof java.lang.Float) {
			query.setFloat(index, ((Float) o).floatValue());
		} else if (o instanceof java.lang.Number) {
			query.setBigDecimal(index, new java.math.BigDecimal(((Number) o)
					.doubleValue()));
		} else if(o==null){
			query.setString(index, null);
		}else{
			query.setString(index, o.toString());
		}

	}

	public void evictObject(final Object object) {
		if (object != null) {
			getHibernateTemplate().evict(object);
		}
	}

	public List<?> getQueryList(final String strSql, final Page page, final Object... object) {
		Pagination p=Pagination.fromPage(page);
		List<?> ret=this.getQueryList(strSql, p, object);
		page.setRowCount(p.getSize()==null?0:p.getSize());
		page.setCurrentCount(ret.size());
		return ret;
	}
	public List<?> getQueryList(final String strSql, final Pagination page, final Object... object) {
	    if (page == null||page.getLimit()==null) {
	        return getQueryList(strSql, object);
	    }
	    if(page.getOffset()==null){
	    	page.setOffset(0);
	    }
	    
	    final HibernateTemplate template = getHibernateTemplate();
		template.setCacheQueries(true);
		HibernateCallback<List<?>> action = new HibernateCallback<List<?>>() {
			public List<?> doInHibernate(Session session)
					throws HibernateException, SQLException {
				Query query = session.createQuery(strSql);
				if (object != null) {
					for (int i = 0; i < object.length; i++) {
						setArgument(query, i, object[i]);
					}
				}
				
				query.setFirstResult(page.getOffset());
				query.setMaxResults(page.getLimit());


				long beginTimeMillis=System.currentTimeMillis();
				List<?> resultList = query.list();
				log(strSql, System.currentTimeMillis()-beginTimeMillis);
				
				if (page.isAutoRowCount()) {
					if(page.getLimit()>resultList.size()&&(resultList.size()>0||page.getOffset()==0)){
						// 2017-12-22 LinLW
						//如果查询结果一页都没有满，明显无需count
						//如果翻到某一页后突然没数据了，还是要去count实际大小。
						page.setSize(resultList.size()+page.getOffset());
					}else{
						// 2017-12-22 LinLW
						// 更新比较精确的截取的方式，应该用正则表达是而不是用 FROM前面加空格的做法。
						String upperSQL = strSql.toUpperCase();
						int firstForm=-1;
						Matcher m=Pattern.compile("\\bFROM\\b").matcher(upperSQL);
						if(m.find()){
							firstForm=m.start();
						}
						
						if(firstForm<0){
							throw new RuntimeException(new DfishException("无法对没有FROM关键字的HQL进行autoRowCount，建议设置page.setAutoRowCount(false);并自行计算数据行数","DFISH-01000"));
						}
						m=Pattern.compile("\\bDISTINCT\\b").matcher(upperSQL);
						if(m.find()){
							if(m.start()<firstForm){
								throw new RuntimeException(new DfishException("无法保证有DISTINCT关键字的HQL进行autoRowCount的结果正确性，建议设置page.setAutoRowCount(false);并自行计算数据行数","DFISH-01000"));
							}
						}
						m=Pattern.compile("\\bGROUP +BY\\b").matcher(upperSQL);
						if(m.find()){
							if(m.start()<firstForm){
								throw new RuntimeException(new DfishException("无法保证有GROUP BY关键字的HQL进行autoRowCount的结果正确性，建议设置page.setAutoRowCount(false);并自行计算数据行数","DFISH-01000"));
							}
						}
						int cur=0,lastOrderBy=upperSQL.length();
						m=Pattern.compile("\\bORDER +BY\\b").matcher(upperSQL);
						while(m.find(cur)){
							cur=m.end();
							lastOrderBy=m.start();
						}
						//如果EXISTS子语句有ORDER BY 但主语句没有的时候，可能会发生异常。
						if(lastOrderBy<upperSQL.length()){
							char[] orderSeq=strSql.substring(lastOrderBy).toCharArray();
							//如果 )比(多，我们则怀疑 这个ORDER BY 隶属于子查询。那么忽略掉这个ORDER BY 以提高正确率。
							int left=0,right=0;
							for(char c:orderSeq){
								if(c=='('){
									left++;
								}else if(c==')'){
									right++;
								}
							}
							if(left!=right){
								lastOrderBy=upperSQL.length();
							}
						}
						
						String strHql4cout = "SELECT COUNT(*) "+strSql.substring(firstForm,lastOrderBy);
						try{
							Query query1 = session.createQuery(strHql4cout);
							if (object != null) {
								for (int k = 0; k < object.length; k++) {
									setArgument(query1, k, object[k]);
								}
							}
							List<?> arrayList = query1.list();
							beginTimeMillis=System.currentTimeMillis();
							Integer inte = ((Number) arrayList.get(0)).intValue();
							log(strHql4cout, System.currentTimeMillis()-beginTimeMillis);
							page.setSize(inte.intValue());
						}catch(Exception ex){
							ex.printStackTrace();
							throw new RuntimeException(new DfishException("自动计算数据行数时发生未知错误，建议设置page.setAutoRowCount(false);并自行计算数据行数。\r\n"+strHql4cout,"DFISH-01000"));
						}
					}
				}
				
				//LinLW 2017-07-13 如果当前页号大于1 查询结果数为0，并且autoRowcount为true。很可能是最后一页被删了。要回头显示前面的页。
				if(resultList.size()==0 && page.isAutoRowCount() && page.getOffset()>0){
					//根据page的rowCount计算curpage的数量

						int offset=(page.getSize()-1)/page.getLimit()*page.getLimit();
						if(offset<0){
							offset=0;
						}
						page.setOffset(offset);
						query.setFirstResult(offset);
						query.setMaxResults(page.getLimit());
//						query.setFirstResult((pageno - 1) * (page.getPageSize()));
//						query.setMaxResults(page.getPageSize());
						beginTimeMillis=System.currentTimeMillis();
						resultList = query.list();
						log(strSql, System.currentTimeMillis()-beginTimeMillis);
//						traceWithBeginTime(strSql, beginTimeMillis);
				}
				
				return resultList;
			}
		};
		List<?> result = (List<?>) template.execute(action);
//		trace(strSql, beginTimeMillis);
		// 当前记录数
		return result;
//	    return getQueryList(strSql, page, page.getAutoRowCount(), object);
	}
	
	public List<?> getQueryList(final String strSql, final Page page,
			final boolean autoGetRowCount, final Object... object) {
		if (page != null) {
			page.setAutoRowCount(autoGetRowCount);
		}
		return getQueryList(strSql, page, object);
	}


	public Object queryAsAnObject(final String strSql, final Object... object){
		List<?> list=getQueryList(strSql,object);
		if(list.size()==0)return null;
		return list.get(0);
	}

	/**
	 * ????????HSQL????????????????????
	 * 
	 * @param queryString
	 * @param values
	 * @return
	 */
	public int bulkUpdate(final String queryString, final Object... values) {
		 return call("batchUpdate",new Callable<Integer>(){
		    @Override
			public Integer call() throws Exception {
			    return  getHibernateTemplate().bulkUpdate(queryString, values);
		    }
		 });
	}

	@Override
    public int[] batchUpdate(final String[] hql) {
	    if (Utils.isEmpty(hql)) {
	    	return null;
	    }
	    return call("batchUpdate",new Callable<int[]>(){
	    	@Override
			public int[] call() throws Exception {
		    return getHibernateTemplate().execute(new HibernateCallback<int[]>() {
				@Override
	            public int[] doInHibernate(Session session) throws HibernateException, SQLException {
					int[] result = new int[hql.length];
					int resultIndex = 0;
					for (String s : hql) { // 多个语句执行
						Query query = session.createQuery(s);
						// 多次执行的结果
						result[resultIndex++] = query.executeUpdate();
					}
					return result;
	            }
			});
	    }
	    });
    }

	@Override
    public int[] batchUpdate(final String hql, final List<Object[]> args) {
		if (Utils.isEmpty(hql)) {
	    	return null;
	    }
		return call(hql,new Callable<int[]>(){
			@Override
			public int[] call() throws Exception {
				return getHibernateTemplate().execute(new HibernateCallback<int[]>() {
					@Override
		            public int[] doInHibernate(Session session) throws HibernateException, SQLException {
						if (Utils.notEmpty(args)) {
							Query query = session.createQuery(hql);
							int[] result = new int[args.size()];
							int resultIndex = 0;
							for (Object[] arg : args) { // 多次执行参数
								int paramIndex = 0;
								for (Object param : arg) { // 单次执行的参数
									query.setParameter(paramIndex++, param);
								}
								// 多次执行的结果
								result[resultIndex++] = query.executeUpdate();
							}
							return result;
						}
						return null;
		            }
				});
			}

		});

    }

	/**
	 * 所有的库操作都经过call操作。为以后call操作用Executor进行监管创造条件
	 *
	 * @param sql
	 * @param callable
	 * @return
	 */
	protected <T>T call(String sql,Callable<T> callable) {
		long begin=System.currentTimeMillis();
		try{
			T o=callable.call();
			return o;
		}catch(RuntimeException ex){
			throw ex;
		}catch(Exception ex){
			throw new RuntimeException(ex);
		}finally{
			long last=System.currentTimeMillis()-begin;
			log(sql,last);
		}
	}

//	@Override
//    public <T> int batchSave(final List<T> entitys) {
//		if (entitys == null) {
//			return 0;
//		}
//		return getHibernateTemplate().execute(new HibernateCallback<Integer>() {
//			@Override
//            public Integer doInHibernate(Session session) throws HibernateException, SQLException {
//				for (T entity : entitys) {
//					session.save(entity);
//				}
//	            return entitys.size();
//            }
//		});
//    }
//
//	@Override
//    public <T> int batchUpdate(final List<T> entitys) {
//		if (entitys == null) {
//			return 0;
//		}
//		return getHibernateTemplate().execute(new HibernateCallback<Integer>() {
//			@Override
//            public Integer doInHibernate(Session session) throws HibernateException, SQLException {
//				for (T entity : entitys) {
//					session.update(entity);
//				}
//	            return entitys.size();
//            }
//		});
//    }
//
//	@Override
//    public <T> int batchDelete(final List<T> entitys) {
//		if (entitys == null) {
//			return 0;
//		}
//		return getHibernateTemplate().execute(new HibernateCallback<Integer>() {
//			@Override
//            public Integer doInHibernate(Session session) throws HibernateException, SQLException {
//				for (T entity : entitys) {
//					session.delete(entity);
//				}
//	            return entitys.size();
//            }
//		});
//    }
}
