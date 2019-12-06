package com.rongji.dfish.framework.mybatis.dao.impl;

import com.rongji.dfish.base.Pagination;
import com.rongji.dfish.framework.dao.FrameworkDao;
import com.rongji.dfish.framework.dto.QueryParam;
import com.rongji.dfish.framework.dto.RequestParam;
import org.apache.ibatis.session.RowBounds;

import java.io.Serializable;
import java.util.List;

/**
 * @author lamontYu
 * @create 2019-12-04 17:54
 */
public interface FrameworkDao4Mybatis<P, ID extends Serializable> extends FrameworkDao<P, ID> {

    @Override
    default List<P> list(Pagination pagination, QueryParam queryParam) {
        return listByRowBounds(new RowBounds(pagination.getOffset(), pagination.getLimit()), queryParam != null ? queryParam.toRequestParam() : null);
    }

    /**
     *
     * @param rowBounds
     * @return
     */
    List<P> listByRowBounds(RowBounds rowBounds, RequestParam requestParam);
}
