package com.rongji.dfish.framework.mybatis.dao.impl;

import com.rongji.dfish.base.Pagination;
import com.rongji.dfish.base.util.Utils;
import com.rongji.dfish.framework.dao.FrameworkDao;
import com.rongji.dfish.framework.dto.QueryParam;
import com.rongji.dfish.framework.dto.RequestParam;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import java.io.Serializable;
import java.util.*;

/**
 * @author lamontYu
 * @create 2019-12-04 17:54
 */
public interface FrameworkDao4Mybatis<P, ID extends Serializable> extends FrameworkDao<P, ID> {

    @Override
    default List<P> list(Pagination pagination, QueryParam<?> queryParam) {
        return listByRowBounds(new RowBounds(pagination.getOffset(), pagination.getLimit()), queryParam != null ? queryParam.toRequestParam() : null);
    }

    /**
     * 根据分页信息和条件查询列表
     * @param rowBounds
     * @return
     */
    List<P> listByRowBounds(RowBounds rowBounds, RequestParam requestParam);

    @Override
    List<P> listByIds(@Param("ids") Collection<ID> ids);

    @Override
    default int delete(P entity) {
        if (entity == null) {
            return 0;
        }
        return delete(getEntityId(entity));
    }

    @Override
    default int delete(ID id) {
        if (id == null || "".equals(id)) {
            return 0;
        }
        return deleteByIds(Arrays.asList(id));
    }

    @Override
    default int deleteAll(Collection<P> entities) {
        if (Utils.isEmpty(entities)) {
            return 0;
        }
        Set<ID> ids = new HashSet<>(entities.size());
        for (P entity : entities) {
            ids.add(getEntityId(entity));
        }
        return deleteByIds(ids);
    }

    /**
     * 通过编号批量删除
     *
     * @param ids
     * @return
     */
    int deleteByIds(@Param("ids") Collection<ID> ids);

}
