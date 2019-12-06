package com.rongji.dfish.framework.dao;

import com.rongji.dfish.base.DfishException;
import com.rongji.dfish.base.Pagination;
import com.rongji.dfish.framework.dto.QueryParam;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author lamontYu
 * @create 2019-12-04 15:40
 */
public interface FrameworkDao<P, ID extends Serializable> {

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
    Map<ID, P> gets(Collection<ID> ids);

    /**
     * 根据分页信息和参数获取列表
     * @param pagination 分页信息
     * @param queryParam 请求参数
     * @return List&lt;P&gt; 实体对象集合
     */
    List<P> list(Pagination pagination, QueryParam<?> queryParam);

    /**
     * 保存方法
     * @param entity
     * @return
     */
    int save(P entity);

    int update(P entity);

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

    int delete(P entity);

    int delete(ID id);

    int deleteAll(Collection<P> entities);

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

    default String getParamStr(int paramCount) {
        StringBuilder sql = new StringBuilder();
        appendParamStr(sql, paramCount);
        return sql.toString();
    }

    Pattern PATTERN_FROM = Pattern.compile("\\bFROM\\b");
    Pattern PATTERN_DISTINCT = Pattern.compile("\\bDISTINCT\\b");
    Pattern PATTERN_GROUP_BY = Pattern.compile("\\bGROUP +BY\\b");
    Pattern PATTERN_ORDER_BY = Pattern.compile("\\bORDER +BY\\b");

    static String getCountSql(String sql) {
        // 更新比较精确的截取的方式，应该用正则表达是而不是用 FROM前面加空格的做法。
        String upperSQL = sql.toUpperCase();
        int firstForm = -1;
        Matcher m = PATTERN_FROM.matcher(upperSQL);
        if (m.find()) {
            firstForm = m.start();
        }

        if (firstForm < 0) {
            throw new RuntimeException(new DfishException("无法对没有FROM关键字的SQL进行autoRowCount，建议设置page.setAutoRowCount(false);并自行计算数据行数", "DFISH-01000"));
        }
        m = PATTERN_DISTINCT.matcher(upperSQL);
        if (m.find()) {
            if (m.start() < firstForm) {
                throw new RuntimeException(new DfishException("无法保证有DISTINCT关键字的SQL进行autoRowCount的结果正确性，建议设置page.setAutoRowCount(false);并自行计算数据行数", "DFISH-01000"));
            }
        }
        m = PATTERN_GROUP_BY.matcher(upperSQL);
        if (m.find()) {
            if (m.start() < firstForm) {
                throw new RuntimeException(new DfishException("无法保证有GROUP BY关键字的SQL进行autoRowCount的结果正确性，建议设置page.setAutoRowCount(false);并自行计算数据行数", "DFISH-01000"));
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
        String countSql = "SELECT COUNT(*) " + sql.substring(firstForm, lastOrderBy);
        return countSql;
    }

}
