package com.rongji.dfish.framework.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.rongji.dfish.base.DfishException;
import com.rongji.dfish.base.Pagination;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.ConnectionCallback;
import org.springframework.jdbc.core.JdbcTemplate;

import com.rongji.dfish.base.Page;
import com.rongji.dfish.base.Utils;
import com.rongji.dfish.base.info.DataBaseInfo;
import com.rongji.dfish.base.util.LogUtil;
import com.rongji.dfish.framework.FrameworkHelper;
import com.rongji.dfish.framework.SystemData;
import org.springframework.jdbc.core.RowMapper;

/**
 * Description: JdbcTemplate常用方法工具类
 * FIXME 临时先放在这个工程,待工程稳定后将此工具类重新梳理封装并吸收到DFish框架中去
 * Copyright:   Copyright © 2018
 * Company:     rongji
 * @author		YuLM
 * @version		1.0
 *
 * Modification History:
 * Date						Author			Version			Description
 * ------------------------------------------------------------------
 * 2018年5月13日 下午11:08:54		YuLM			1.0				1.0 Version
 */
public class JdbcTemplateHelper {
	
	public static JdbcTemplate getJdbcTemplate() {
		return FrameworkHelper.getBean(JdbcTemplate.class);
	}
	
	protected static JdbcTemplate getJdbcTemplate(JdbcTemplate jdbcTemplate) {
		// FIXME 这样封装似乎也不到位
		if (jdbcTemplate != null) {
			return jdbcTemplate;
		}
		return getJdbcTemplate();
	}

	/**
	 * 数据库执行方法 注意;为分隔符号，SQL里面常量如果有;将会出错。
	 * 
	 * @param sql
	 */
	public static void execute(JdbcTemplate jdbcTemplate, String sql) {
		jdbcTemplate = getJdbcTemplate(jdbcTemplate);
		try {
			if (Utils.notEmpty(sql)) {
				for (String item : sql.split(";")) {
					if (Utils.notEmpty(item)) {
						jdbcTemplate.execute(item);
					}
				}
			}
		} catch (Exception e) {
			LogUtil.error("", e);
		}
	}

	/**
	 * 数据库查询方法
	 *
	 * @param sql
	 * @return
	 */
	public static List<?> queryForList(JdbcTemplate jdbcTemplate, String sql) {
		return queryForList(jdbcTemplate, sql, null);
	}

	public static List<?> queryForList(JdbcTemplate jdbcTemplate, String sql, Object[] args) {
		return queryForList(jdbcTemplate, sql, null, args);
	}

	public static List<?> queryForList(JdbcTemplate jdbcTemplate, String sql, Page page, Object[] args) {
		Pagination pagination = Pagination.fromPage(page);
		QueryPreparation preparation = getQueryPreparation(sql, pagination, args);
		List<?> dataList = jdbcTemplate.queryForList(preparation.getQuerySql(), preparation.getQueryArgs());
		if (page != null && page.isAutoRowCount()) {
			fillPageInfo(jdbcTemplate, preparation, page);
			if (Utils.isEmpty(dataList) && page.getCurrentPage() > page.getPageCount()) {
				// 查出来数据为空且当前页过大时重新尝试获取
				page.setCurrentPage(page.getPageCount());
				pagination = Pagination.fromPage(page);
				preparation = getQueryPreparation(sql, pagination, args);
				dataList = jdbcTemplate.queryForList(preparation.getQuerySql(), preparation.getQueryArgs());
			}
		}
		return dataList;
	}

	/**
	 * 数据库查询片段截取方法
	 *
	 * @param sql
	 * @param startIndex
	 * @param endIndex
	 * @return
	 */
	public static List<?> queryForList(JdbcTemplate jdbcTemplate, String sql, int startIndex, int endIndex) {
		jdbcTemplate = getJdbcTemplate(jdbcTemplate);
		try {
			StringBuilder paginationSQL = new StringBuilder(" SELECT * FROM ( ");
			paginationSQL.append(" SELECT temp.* ,ROWNUM num FROM ( ");
			paginationSQL.append(sql);
			paginationSQL.append("  ) temp where ROWNUM <= ").append(endIndex);
			paginationSQL.append(" ) WHERE  num >= ").append(startIndex);
			return jdbcTemplate.queryForList(paginationSQL.toString());
		} catch (Exception e) {
			if (FrameworkHelper.LOG.isErrorEnabled()) {
				FrameworkHelper.LOG.error("", e);
			}
		}
		return Collections.emptyList();
	}
	
	public static String getPreSqlWithPageOracle(String sql) {
		if (Utils.isEmpty(sql)) {
			return "";
		}
		StringBuilder preSql = new StringBuilder("SELECT * FROM ( ");
		preSql.append(" SELECT temp.* ,ROWNUM num FROM ( ");
		preSql.append(sql);
		preSql.append("  ) temp where ROWNUM <= ?");
		preSql.append(" ) WHERE  num >= ?");
		return preSql.toString();
	}
	
	public static String getPreSqlWithPageMysql(String sql) {
		if (Utils.isEmpty(sql)) {
			return "";
		}
		StringBuilder preSql = new StringBuilder();
		preSql.append(sql);
		preSql.append("  LIMIT ?,?");
		return preSql.toString();
	}

	public static <T> List<T> query(JdbcTemplate jdbcTemplate, String sql, RowMapper<T> rowMapper) {
		return query(jdbcTemplate, sql, null, rowMapper);
	}

	public static <T> List<T> query(JdbcTemplate jdbcTemplate, String sql, Object[] args, RowMapper<T> rowMapper) {
		return query(jdbcTemplate, sql, null, args, rowMapper);
	}

	public static <T> List<T> query(JdbcTemplate jdbcTemplate, String sql, Page page, Object[] args, RowMapper<T> rowMapper) {
		jdbcTemplate = getJdbcTemplate(jdbcTemplate);

		Pagination pagination = Pagination.fromPage(page);
		QueryPreparation preparation = getQueryPreparation(sql, pagination, args);
		List<T> dataList = jdbcTemplate.query(preparation.getQuerySql(), preparation.getQueryArgs(), rowMapper);
		if (page != null && page.isAutoRowCount()) {
			fillPageInfo(jdbcTemplate, preparation, page);
			if (Utils.isEmpty(dataList) && page.getCurrentPage() > page.getPageCount()) {
				// 查出来数据为空且当前页过大时重新尝试获取
				page.setCurrentPage(page.getPageCount());
				pagination = Pagination.fromPage(page);
				preparation = getQueryPreparation(sql, pagination, args);
				dataList = jdbcTemplate.query(preparation.getQuerySql(), preparation.getQueryArgs(), rowMapper);
			}
			page.setCurrentCount(dataList.size());
		}
		return dataList;
	}

	public static <T> T queryForObject(JdbcTemplate jdbcTemplate, String sql, Object[] args, RowMapper<T> rowMapper) {
		jdbcTemplate = getJdbcTemplate(jdbcTemplate);
		// 这样的写法更好安全,查不出数据不会报错
		List<T> dataList = query(jdbcTemplate, sql, args, rowMapper);
		if (Utils.notEmpty(dataList)) {
			return dataList.get(0);
		}
		return null;
	}

	public static <T> T queryForObject(JdbcTemplate jdbcTemplate, String sql, Object[] args, Class<T> clz) {
		jdbcTemplate = getJdbcTemplate(jdbcTemplate);
		return jdbcTemplate.queryForObject(sql, args, clz);
	}

	private static void fillPageInfo(JdbcTemplate jdbcTemplate, QueryPreparation preparation, Page page) {
		if (page == null || !page.isAutoRowCount()) {
			return;
		}
		Number rowCount = jdbcTemplate.queryForObject(preparation.getCountSql(), preparation.getCountArgs(), Number.class);
		page.setRowCount(rowCount.intValue());
	}

	private static class QueryPreparation {
		String querySql;
		String countSql;
		Object[] queryArgs;
		Object[] countArgs;

		public String getQuerySql() {
			return querySql;
		}

		public void setQuerySql(String querySql) {
			this.querySql = querySql;
		}

		public String getCountSql() {
			return countSql;
		}

		public void setCountSql(String countSql) {
			this.countSql = countSql;
		}

		public Object[] getQueryArgs() {
			return queryArgs;
		}

		public void setQueryArgs(Object[] queryArgs) {
			this.queryArgs = queryArgs;
		}

		public Object[] getCountArgs() {
			return countArgs;
		}

		public void setCountArgs(Object[] countArgs) {
			this.countArgs = countArgs;
		}

	}

	private static QueryPreparation getQueryPreparation(String sql, Pagination pagination, Object[] args) {
		String querySql = sql;

		List<Object> orginArgs = args == null ? new ArrayList<Object>(0) : Arrays.asList(args);
		List<Object> queryArgs = new ArrayList<>(orginArgs);
		QueryPreparation preparation = new QueryPreparation();
		if (pagination != null) {
			DataBaseInfo dbInfo = SystemData.getInstance().getDataBaseInfo();
			if (DataBaseInfo.DATABASE_ORACLE == dbInfo.getDatabaseType()) {
				querySql = getPreSqlWithPageOracle(sql);
				queryArgs.add(pagination.getOffset() + pagination.getLimit());
				queryArgs.add(pagination.getOffset());
			} else if (DataBaseInfo.DATABASE_MYSQL == dbInfo.getDatabaseType()) {
				querySql = getPreSqlWithPageMysql(sql);
				queryArgs.add(pagination.getOffset());
				queryArgs.add(pagination.getLimit());
			}
			if (pagination.isAutoRowCount()) {
				List<Object> countArgs = new ArrayList<>(orginArgs);

				/******************以下截取count语句来源框架*****************/
				// 更新比较精确的截取的方式，应该用正则表达是而不是用 FROM前面加空格的做法。
				String upperSQL = sql.toUpperCase();
				int firstForm = -1;
				Matcher m = Pattern.compile("\\bFROM\\b").matcher(upperSQL);
				if (m.find()) {
					firstForm = m.start();
				}

				if (firstForm < 0) {
					throw new RuntimeException(new DfishException("无法对没有FROM关键字的SQL进行autoRowCount，建议设置page.setAutoRowCount(false);并自行计算数据行数", "DFISH-01000"));
				}
				m = Pattern.compile("\\bDISTINCT\\b").matcher(upperSQL);
				if (m.find()) {
					if (m.start() < firstForm) {
						throw new RuntimeException(new DfishException("无法保证有DISTINCT关键字的SQL进行autoRowCount的结果正确性，建议设置page.setAutoRowCount(false);并自行计算数据行数", "DFISH-01000"));
					}
				}
				m = Pattern.compile("\\bGROUP +BY\\b").matcher(upperSQL);
				if (m.find()) {
					if (m.start() < firstForm) {
						throw new RuntimeException(new DfishException("无法保证有GROUP BY关键字的SQL进行autoRowCount的结果正确性，建议设置page.setAutoRowCount(false);并自行计算数据行数", "DFISH-01000"));
					}
				}
				int cur = 0, lastOrderBy = upperSQL.length();
				m = Pattern.compile("\\bORDER +BY\\b").matcher(upperSQL);
				while (m.find(cur)) {
					cur = m.end();
					lastOrderBy = m.start();
				}
				//如果EXISTS子语句有ORDER BY 但主语句没有的时候，可能会发生异常。
				if (lastOrderBy < upperSQL.length()) {
					char[] orderSeq = sql.substring(lastOrderBy).toCharArray();
					//如果 )比(多，我们则怀疑 这个ORDER BY 隶属于子查询。那么忽略掉这个ORDER BY 以提高正确率。
					int left = 0, right = 0;
					for (char c : orderSeq) {
						if (c == '(') {
							left++;
						} else if (c == ')') {
							right++;
						}
					}
					if (left != right) {
						lastOrderBy = upperSQL.length();
					}
				}
				/******************以上截取count语句来源框架*****************/

				String countSql = "SELECT COUNT(*) " + sql.substring(firstForm, lastOrderBy);
				preparation.setCountSql(countSql);
				preparation.setCountArgs(countArgs.toArray());
			}
		}

		preparation.setQuerySql(querySql);
		preparation.setQueryArgs(queryArgs.toArray());
		return preparation;
	}

	/**
	 * 数据库批量更新操作
	 *
	 * @param sql
	 * @param args
	 */
	public static int update(JdbcTemplate jdbcTemplate, String sql, Object[] args) {
		List<Object[]> argsList = new ArrayList<>(1);
		if (Utils.notEmpty(args)) {
			argsList.add(args);
		}
		int[] result = batchUpdate(jdbcTemplate, sql, argsList);
		return jdbcTemplate.update(sql, args);
	}

	/**
	 * 数据库批量更新操作
	 * 
	 * @param sql
	 * @param argsList
	 */
	public static int[] batchUpdate(JdbcTemplate jdbcTemplate, String sql, final List<Object[]> argsList) {
		jdbcTemplate = getJdbcTemplate(jdbcTemplate);
		int[] result = null;
		try {
			result = jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
				public void setValues(PreparedStatement ps, int i) throws SQLException {
					Object[] item = argsList.get(i);
					if (Utils.notEmpty(item)) {
						for (int j = 0; j < item.length; j++) {
							Object param = item[j];
							if (param instanceof Date) {
								param = new java.sql.Timestamp(((Date) param).getTime());
							}
							ps.setObject(j + 1, param);
						}
					}
				}

				public int getBatchSize() {
					return argsList.size();
				}
			});
		} catch (Exception e) {
			LogUtil.error("jdbcTemplate批量更新出错", e);
		}
		return result;
	}
	
    public static Object executeTransaction(JdbcTemplate jdbcTemplate, final LinkedHashMap<String, List<Object[]>> executes) {
		if (Utils.isEmpty(executes)) {
			return null;
		}
		jdbcTemplate = getJdbcTemplate(jdbcTemplate);
		Object result = jdbcTemplate.execute(new ConnectionCallback<Object>() {
			public Object doInConnection(Connection conn) throws SQLException, DataAccessException {
				boolean autoCommit = conn.getAutoCommit();
				try {
					conn.setAutoCommit(false);
                    for (Map.Entry<String, List<Object[]>> entry : executes.entrySet()) {
                        if (Utils.isEmpty(entry.getKey()) || Utils.isEmpty(entry.getValue())) {
                            continue;
                        }

						PreparedStatement pstmt = conn.prepareStatement(entry.getKey());
                        for (Object[] args : entry.getValue()) {
                            if (Utils.notEmpty(args)) {
                                int paramIndex = 1;
                                for (Object param : args) {
                                    if (param instanceof Date) {
                                        param = new Timestamp(((Date) param).getTime());
                                    }
                                    pstmt.setObject(paramIndex++, param);
                                }
                            }
                            pstmt.execute();
                        }
					}
					conn.commit();
				} catch (Exception e) {
					LogUtil.error("jdbcTemplate执行事务异常", e);
					conn.rollback();
				} finally {
					conn.setAutoCommit(autoCommit);
				}
				return null;
			}
		});
		return result;
	}

    public static Object executeTransaction(JdbcTemplate jdbcTemplate, List<String> sqls, List<Object[]> argsList) {
		if (Utils.isEmpty(sqls) || Utils.isEmpty(argsList) || sqls.size() != argsList.size()) {
			return null;
		}
		LinkedHashMap<String, List<Object[]>> executes = new LinkedHashMap<>();
		for (int i = 0; i < sqls.size(); i++) {
		    String sql = sqls.get(i);
            if (Utils.isEmpty(sql)) {
                continue;
            }
            Object[] args = argsList.get(i);
            List<Object[]> list = new ArrayList<>();
            if (args != null) {
                list.add(args);
            }
			executes.put(sql, list);
		}
//		jdbcTemplate = getJdbcTemplate(jdbcTemplate);
//		Object result = jdbcTemplate.execute(new ConnectionCallback<Object>() {
//			public Object doInConnection(Connection conn) throws SQLException, DataAccessException {
//				boolean autoCommit = conn.getAutoCommit();
//				try {
//					conn.setAutoCommit(false);
//					for (int i=0; i < sqls.size(); i++) {
//						String sql = sqls.get(i);
//						Object[] params = paramsList.get(i);
//						PreparedStatement pstmt = conn.prepareStatement(sql);
//						if (Utils.notEmpty(params)) {
//							int paramIndex = 1;
//							for (Object param : params) {
//								if (param instanceof Date) {
//									param = new Date(((Date) param).getTime());
//								}
//								pstmt.setObject(paramIndex++, param);
//							}
//						}
//						pstmt.execute();
//					}
//					conn.commit();
//                } catch (Exception e) {
//                	LogUtil.error("jdbcTemplate执行事务异常", e);
//	                conn.rollback();
//                } finally {
//                	conn.setAutoCommit(autoCommit);
//                	conn.close();
//                }
//				return null;
//			}
//		});
		return executeTransaction(jdbcTemplate, executes);
	}
    
}
