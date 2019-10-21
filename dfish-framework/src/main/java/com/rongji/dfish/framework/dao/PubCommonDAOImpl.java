package com.rongji.dfish.framework.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
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
	@Override
    public List<?> getQueryList(final String strSql, final Object... object) {
		long beginTimeMillis = getCurrentTimeMillis();
		
		final HibernateTemplate template = getHibernateTemplate();
		template.setCacheQueries(true);
		// return template.find(strSql,object);
		// ?????timeStamp???????????????????find
		HibernateCallback<?> action = new HibernateCallback<Object>() {
			@Override
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
		showExecuteResult(strSql, beginTimeMillis);
		return list;
	}
	
	/**
	 * 获取当前时间
	 * @return
	 */
	private long getCurrentTimeMillis() {
		// 
		if (executeWarn) {
			return System.currentTimeMillis();
		}
		return 0;
	}
	/**
	 * 显示执行结果
	 * @param sql
	 * @param beginTimeMillis
	 */
	private void showExecuteResult(String sql, long beginTimeMillis) {
		if (executeWarn) {
			long endTimeMillis = System.currentTimeMillis();
			long costTime = endTimeMillis - beginTimeMillis;
			if (costTime > executeThreshold) {
				LOG.warn("execute sql[" + sql + "] cost: " + costTime + "ms");
			}
		}
	}

	@Override
    public int deleteSQL(String strSql, Object... object) {
		long beginTimeMillis = getCurrentTimeMillis();
		List<?> list = getQueryList(strSql, object);
		if (list != null) {
			getHibernateTemplate().deleteAll(list);
		}
		showExecuteResult(strSql, beginTimeMillis);
		return list == null ? 0 : list.size();
	}

	@Override
    public void delete(Object obj) {
		if (obj != null) {
			getHibernateTemplate().delete(obj);
		}
	}

	@Override
    public void save(final Object object) {
		if (object != null) {
			getHibernateTemplate().save(object);
		}
	}

	@Override
    public void update(final Object object) {
		if (object != null) {
			getHibernateTemplate().update(object);
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

	@Override
    public void evictObject(final Object object) {
		if (object != null) {
			getHibernateTemplate().evict(object);
		}
	}

	@Override
    public List<?> getQueryList(final String strSql, final Page page, final Object... object) {
	    if (page == null) {
	        return getQueryList(strSql, object);
	    }
	    
	    final boolean autoRowCount = page.getAutoRowCount() == null || page.getAutoRowCount();
	    
	    final HibernateTemplate template = getHibernateTemplate();
		template.setCacheQueries(true);
		long beginTimeMillis = getCurrentTimeMillis();
		HibernateCallback<List<?>> action = new HibernateCallback<List<?>>() {
			@Override
            public List<?> doInHibernate(Session session)
					throws HibernateException, SQLException {
				Query query = session.createQuery(strSql);
				if (object != null) {
					for (int i = 0; i < object.length; i++) {
						setArgument(query, i, object[i]);
					}
				}
				
				// ??????????
				if (page.getCurrentPage() != 0) {
					int pageno = page.getCurrentPage();
					query.setFirstResult((pageno - 1) * (page.getPageSize()));
					query.setMaxResults(page.getPageSize());
				}

				List<?> resultList = query.list();
				page.setCurrentCount(resultList.size());
				
				if (autoRowCount) {
					if(page.getPageSize()>resultList.size()&&(resultList.size()>0||page.getCurrentPage()==1)){
						// 2017-12-22 LinLW
						//如果查询结果一页都没有满，明显无需count
						//如果翻到某一页后突然没数据了，还是要去count实际大小。
						page.setRowCount(resultList.size()+(page.getCurrentPage()-1)*page.getPageSize());
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
							Integer inte = ((Number) arrayList.get(0)).intValue();
							page.setRowCount(inte.intValue());
						}catch(Exception ex){
							ex.printStackTrace();
							throw new RuntimeException(new DfishException("自动计算数据行数时发生未知错误，建议设置page.setAutoRowCount(false);并自行计算数据行数。\r\n"+strHql4cout,"DFISH-01000"));
						}
					}
				}
				
				//LinLW 2017-07-13 如果当前页号大于1 查询结果数为0，并且autoRowcount为true。很可能是最后一页被删了。要回头显示前面的页。
				if(resultList.size()==0 && autoRowCount && page.getCurrentPage()>1){
					//根据page的rowCount计算curpage的数量
					if(page.getRowCount()==0){
						page.setCurrentPage(1);
					}else{
						int pageno=(page.getRowCount()-1)/page.getPageSize()+1;
						page.setCurrentPage(pageno);
						query.setFirstResult((pageno - 1) * (page.getPageSize()));
						query.setMaxResults(page.getPageSize());
						resultList = query.list();
						page.setCurrentCount(resultList.size());
					}
				}
				
				return resultList;
			}
		};
		List<?> result = (List<?>) template.execute(action);
		showExecuteResult(strSql, beginTimeMillis);
		// 当前记录数
		if (result != null) {
			page.setCurrentCount(result.size());
		}
		return result;
//	    return getQueryList(strSql, page, page.getAutoRowCount(), object);
	}
	
	@Override
    public List<?> getQueryList(final String strSql, final Page page,
                                final boolean autoGetRowCount, final Object... object) {
		if (page != null) {
			page.setAutoRowCount(autoGetRowCount);
		}
		return getQueryList(strSql, page, object);
	}


	@Override
    public Object queryAsAnObject(final String strSql, final Object... object){
		List<?> list=getQueryList(strSql,object);
		if(list.size()==0) {
            return null;
        }
		return list.get(0);
	}

	/**
	 * ????????HSQL????????????????????
	 * 
	 * @param queryString
	 * @param values
	 * @return
	 */
	@Override
    public int bulkUpdate(String queryString, Object... values) {
		long beginTimeMillis = getCurrentTimeMillis();
		int result = getHibernateTemplate().bulkUpdate(queryString, values);
		showExecuteResult(queryString, beginTimeMillis);
		return result;
	}

	@Override
    public int[] batchUpdate(final String[] hql) {
	    if (Utils.isEmpty(hql)) {
	    	return null;
	    }
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

	@Override
    public int[] batchUpdate(final String hql, final List<Object[]> args) {
		if (Utils.isEmpty(hql)) {
	    	return null;
	    }
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
