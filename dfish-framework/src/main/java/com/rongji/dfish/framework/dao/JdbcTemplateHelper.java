package com.rongji.dfish.framework.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collections;
import java.util.Date;
import java.util.List;

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
	public static List<?> query(JdbcTemplate jdbcTemplate, String sql) {
		jdbcTemplate = getJdbcTemplate(jdbcTemplate);
		try {
			return jdbcTemplate.queryForList(sql);
		} catch (Exception e) {
			LogUtil.error("", e);
		}

		return null;
	}
	
	public static List<?> query(JdbcTemplate jdbcTemplate, String sql, Object... params) {
		if (params == null) {
			return Collections.emptyList();
		}
		jdbcTemplate = getJdbcTemplate(jdbcTemplate);
		List<?> dataList = jdbcTemplate.queryForList(sql, params);
		return dataList;
	}
	
	public static List<?> query(JdbcTemplate jdbcTemplate, String sql, Page page, Object... params) {
		if (params == null) {
			return Collections.emptyList();
		}
		if (page == null) {
			return query(jdbcTemplate, sql, params);
		}
		
		if (page.getCurrentPage() < 1) {
			page.setCurrentPage(1);
		}
		if (page.getPageSize() < 1) {
			// 当无分页数时默认每页20条记录
			page.setPageSize(20);
		}
		jdbcTemplate = getJdbcTemplate(jdbcTemplate);
		int endIndex = page.getCurrentPage() * page.getPageSize();
		int beginIndex = endIndex - page.getPageSize() + 1;
		
		DataBaseInfo dbInfo = SystemData.getInstance().getDataBaseInfo();
		String sqlWithPage = "";
		
		Object[] args = new Object[params.length + 2];
		int index = 0;
		for (Object param : params) {
			args[index++] = param;
		}
		
		// FIXME 这种做法不合理,需要重新封装
		if (DataBaseInfo.DATABASE_ORACLE == dbInfo.getDatabaseType()) {
			sqlWithPage = getPreSqlWithPageOracle(sql);
			
			args[index++] = endIndex;
			args[index++] = beginIndex;
		} else if (DataBaseInfo.DATABASE_MYSQL == dbInfo.getDatabaseType()) {
			sqlWithPage = getPreSqlWithPageMysql(sql);
			
			args[index++] = beginIndex - 1;
			args[index++] = page.getPageSize();
		}
		
		// FIXME autoRowCount暂未处理
		List<?> dataList = jdbcTemplate.queryForList(sqlWithPage, args);
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
	public static List<?> query(JdbcTemplate jdbcTemplate, String sql, int startIndex, int endIndex) {
		jdbcTemplate = getJdbcTemplate(jdbcTemplate);
		try {
			StringBuilder paginationSQL = new StringBuilder(" SELECT * FROM ( ");
			paginationSQL.append(" SELECT temp.* ,ROWNUM num FROM ( ");
			paginationSQL.append(sql);
			paginationSQL.append("  ) temp where ROWNUM <= ").append(endIndex);
			paginationSQL.append(" ) WHERE  num >= ").append(startIndex);
			return jdbcTemplate.queryForList(paginationSQL.toString());
		} catch (Exception e) {
			LogUtil.error("", e);
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

	/**
	 * 数据库批量更新操作
	 * 
	 * @param sql
	 * @param dataList
	 */
	public static int[] batchUpdate(JdbcTemplate jdbcTemplate, String sql, final List<Object[]> dataList) {
		jdbcTemplate = getJdbcTemplate(jdbcTemplate);
		int[] result = null;
		try {
			result = jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
				@Override
                public void setValues(PreparedStatement ps, int i) throws SQLException {
					Object[] item = dataList.get(i);
					if (Utils.notEmpty(item)) {
						for (int j = 0; j < item.length; j++) {
							Object param = item[j];
							if (param instanceof Date) {
								param = new java.sql.Date(((Date) param).getTime());
							}
							ps.setObject(j + 1, param);
						}
					}
				}

				@Override
                public int getBatchSize() {
					return dataList.size();
				}
			});
		} catch (Exception e) {
			LogUtil.error("jdbcTemplate批量更新出错", e);
		}
		return result;
	}
	
    public static Object executeTransaction(JdbcTemplate jdbcTemplate, final List<String> sqls, final List<Object[]> paramsList) {
		if (Utils.isEmpty(sqls) || Utils.isEmpty(paramsList) || sqls.size() != paramsList.size()) {
			return null;
		}
		jdbcTemplate = getJdbcTemplate(jdbcTemplate);
		Object result = jdbcTemplate.execute(new ConnectionCallback<Object>() {
			@Override
            public Object doInConnection(Connection conn) throws SQLException, DataAccessException {
				boolean autoCommit = conn.getAutoCommit();
				try {
					conn.setAutoCommit(false);
					for (int i=0; i < sqls.size(); i++) {
						String sql = sqls.get(i);
						Object[] params = paramsList.get(i);
						PreparedStatement pstmt = conn.prepareStatement(sql);
						if (Utils.notEmpty(params)) {
							int paramIndex = 1;
							for (Object param : params) {
								if (param instanceof Date) {
									param = new java.sql.Date(((Date) param).getTime());
								}
								pstmt.setObject(paramIndex++, param);
							}
						}
						pstmt.execute();
					}
					conn.commit();
                } catch (Exception e) {
                	LogUtil.error("jdbcTemplate执行事务异常", e);
	                conn.rollback();
                } finally {
                	conn.setAutoCommit(autoCommit);
                	conn.close();
                }
				return null;
			}
		});
		return result;
	}
    
}
