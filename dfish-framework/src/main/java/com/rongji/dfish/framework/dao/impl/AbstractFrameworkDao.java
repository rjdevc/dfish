package com.rongji.dfish.framework.dao.impl;

import com.rongji.dfish.base.Pagination;
import com.rongji.dfish.base.util.Utils;
import com.rongji.dfish.base.context.SystemContext;
import com.rongji.dfish.base.info.DataBaseInfo;
import com.rongji.dfish.framework.dao.FrameworkDao;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.*;

/**
 * 抽象类，定义对数据库表的基本操作方法
 * @author lamontYu
 * @date 2019-12-04 17:50
 */
public abstract class AbstractFrameworkDao<P, ID extends Serializable> implements FrameworkDao<P, ID> {
    @Resource(name = "jdbcTemplate")
    private JdbcTemplate jdbcTemplate;

    public JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }

    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
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

        public static QueryPreparation of(String sql, Pagination pagination, Object[] args) {
            List<Object> originalArgs = args == null ? new ArrayList<>(0) : Arrays.asList(args);
            List<Object> queryArgs = new ArrayList<>(originalArgs);
            QueryPreparation preparation = new QueryPreparation();
            String querySql = sql;
            if (pagination != null) {
                DataBaseInfo dbInfo = SystemContext.getInstance().get(DataBaseInfo.class);
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
                    preparation.setCountSql(FrameworkDao.getCountSql(sql));
                    preparation.setCountArgs(originalArgs.toArray());
                }
            }

            preparation.setQuerySql(querySql);
            preparation.setQueryArgs(queryArgs.toArray());
            return preparation;
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

    }

    /**
     * 执行某个针对数据库的查询
     * @param sql
     * @param rowMapper
     * @param <T>
     * @return List
     */
    public <T> List<T> query(String sql, RowMapper<T> rowMapper) {
        return query(sql, null, rowMapper);
    }

    /**
     * 执行某个针对数据库的查询,带查询参数
     * @param sql
     * @param args
     * @param rowMapper
     * @param <T> List
     * @return
     */
    public <T> List<T> query(String sql, Object[] args, RowMapper<T> rowMapper) {
        return query(sql, null, args, rowMapper);
    }

    /**
     * 执行某个针对数据库的查询,带查询参数，分页信息
     * @param sql
     * @param pagination
     * @param args
     * @param rowMapper
     * @param <T>
     * @return List
     */
    public <T> List<T> query(String sql, Pagination pagination, Object[] args, RowMapper<T> rowMapper) {
        QueryPreparation preparation = QueryPreparation.of(sql, pagination, args);
        List<T> dataList = jdbcTemplate.query(preparation.getQuerySql(), preparation.getQueryArgs(), rowMapper);
        if (pagination != null && pagination.isAutoRowCount()) {
            pagination.setSize(getRowCount(preparation));
            if (Utils.isEmpty(dataList) && pagination.getOffset() >= pagination.getSize()) {
                pagination.setOffset((pagination.getSize() - 1) / pagination.getLimit() * pagination.getLimit());
                dataList = jdbcTemplate.query(preparation.getQuerySql(), preparation.getQueryArgs(), rowMapper);
            }
        }
        return dataList;
    }

    private int getRowCount(QueryPreparation preparation) {
        Number rowCount = jdbcTemplate.queryForObject(preparation.getCountSql(), preparation.getCountArgs(), Number.class);
        return rowCount.intValue();
    }

    public <T> T queryForObject(String sql, Object[] args, RowMapper<T> rowMapper) {
        // 这样的写法更好安全,查不出数据不会报错
        List<T> dataList = query(sql, args, rowMapper);
        if (Utils.notEmpty(dataList)) {
            return dataList.get(0);
        }
        return null;
    }

}
