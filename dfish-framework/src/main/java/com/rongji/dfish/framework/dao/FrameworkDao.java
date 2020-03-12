package com.rongji.dfish.framework.dao;

import com.rongji.dfish.base.Pagination;
import com.rongji.dfish.base.exception.MarkedRuntimeException;
import com.rongji.dfish.base.util.Utils;
import com.rongji.dfish.framework.dto.QueryParam;

import java.io.Serializable;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 接口，数据访问层，定义对数据库表的操作
 * @author lamontYu
 * @date 2019-12-04 15:40
 */
public interface FrameworkDao<P, ID extends Serializable> {

    /**
     * 批量大小
     */
    int BATCH_SIZE = 512;

    /**
     * 根据编号获取实体对象
     * @param id 编号
     * @return P 实体对象
     */
    P get(ID id);

    /**
     * 根据编号集合批量获取实体对象
     * @param ids 编号集合
     * @return Map&lt;ID, P&gt; 实体对象集合
     */
    default Map<ID, P> gets(Collection<ID> ids) {
        if (Utils.isEmpty(ids)) {
            return Collections.emptyMap();
        }
        List<P> dataList = listByIds(ids);
        Map<ID, P> dataMap = new HashMap<>(dataList.size());
        for (P data : dataList) {
            if (data != null) {
                dataMap.put(getEntityId(data), data);
            }
        }
        return dataMap;
    }

    /**
     * 根据实体对象获取主键编号
     * @param entity 实体对象
     * @return 主键编号
     */
    ID getEntityId(P entity);

    /**
     * 根据编号集合批量获取实体对象
     * @param ids 编号集合
     * @return List&lt;P&gt; 实体对象集合
     */
    List<P> listByIds(Collection<ID> ids);

    /**
     * 根据分页信息和参数获取列表
     * @param pagination 分页信息
     * @param queryParam 请求参数
     * @return List&lt;P&gt; 实体对象集合
     */
    List<P> list(Pagination pagination, QueryParam<?> queryParam);

    /**
     * 保存
     * @param entity 实体对象
     * @return int 更新记录数
     */
    int save(P entity);

    /**
     * 修改
     * @param entity 实体对象
     * @return int 更新记录数
     */
    int update(P entity);

    /**
     * 保存或更新
     * @param entity 实体对象
     * @return int 更新记录数
     */
    default int saveOrUpdate(P entity) {
        if (entity == null) {
            return 0;
        }
        int count = update(entity);
        if (count < 1) {
            count = save(entity);
        }
        return count;
    }

    /**
     * 删除
     * @param entity 实体对象
     * @return int 更新记录数
     */
    int delete(P entity);

    /**
     * 删除
     * @param id 编号
     * @return int 更新记录数
     */
    int delete(ID id);

    /**
     * 批量删除
     * @param entities 实体对象集合
     * @return int 更新记录数
     */
    int deleteAll(Collection<P> entities);

    /**
     * 预编译多参数拼接语句
     * @param sql 拼接的sql
     * @param paramCount 参数个数
     */
    default void appendParamStr(StringBuilder sql, int paramCount) {
        if (sql == null) {
            return;
        }
        boolean isFirst = true;
        for (int i = 0; i < paramCount; ++i) {
            if (isFirst) {
                isFirst = false;
            } else {
                sql.append(',');
            }
            sql.append('?');
        }
    }

    /**
     * 预编译多参数拼接语句
     * @param paramCount 参数个数
     * @return 拼接的sql
     */
    default String getParamStr(int paramCount) {
        StringBuilder sql = new StringBuilder();
        appendParamStr(sql, paramCount);
        return sql.toString();
    }

    /**
     * 带有FROM
     */
    Pattern PATTERN_FROM = Pattern.compile("\\bFROM\\b");
    /**
     * 带有DISTINCT
     */
    Pattern PATTERN_DISTINCT = Pattern.compile("\\bDISTINCT\\b");
    /**
     * 带有GROUP BY
     */
    Pattern PATTERN_GROUP_BY = Pattern.compile("\\bGROUP +BY\\b");
    /**
     * 带有ORDER BY
     */
    Pattern PATTERN_ORDER_BY = Pattern.compile("\\bORDER +BY\\b");

    /**
     * 获取计数统计语句
     * @param sql 原始查询语句
     * @return String 统计sql
     */
    static String getCountSql(String sql) {
        // 更新比较精确的截取的方式，应该用正则表达是而不是用 FROM前面加空格的做法。
        String upperSQL = sql.toUpperCase();
        int firstFrom = -1;
        Matcher m = PATTERN_FROM.matcher(upperSQL);
        if (m.find()) {
            firstFrom = m.start();
        }

        if (firstFrom < 0) {
            throw new MarkedRuntimeException("无法对没有FROM关键字的SQL进行autoRowCount，建议设置page.setAutoRowCount(false);并自行计算数据行数");
        }
        m = PATTERN_DISTINCT.matcher(upperSQL);
        if (m.find()) {
            if (m.start() < firstFrom) {
                throw new MarkedRuntimeException("无法保证有DISTINCT关键字的SQL进行autoRowCount的结果正确性，建议设置page.setAutoRowCount(false);并自行计算数据行数");
            }
        }
        m = PATTERN_GROUP_BY.matcher(upperSQL);
        if (m.find()) {
            if (m.start() < firstFrom) {
                throw new MarkedRuntimeException("无法保证有GROUP BY关键字的SQL进行autoRowCount的结果正确性，建议设置page.setAutoRowCount(false);并自行计算数据行数");
            }
        }
        int cur = 0, lastOrderBy = upperSQL.length();
        m = PATTERN_ORDER_BY.matcher(upperSQL);
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
        String countSql = "SELECT COUNT(*) " + sql.substring(firstFrom, lastOrderBy);
        return countSql;
    }

    /**
     * 从缓存中驱逐
     * @param entity 实体类
     */
    void evict(Object entity);

}
