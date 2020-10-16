package com.rongji.dfish.framework.plugin.exception.service.impl;

import com.rongji.dfish.base.Pagination;
import com.rongji.dfish.framework.dao.FrameworkDao;
import com.rongji.dfish.framework.plugin.exception.dao.PubExceptionDao;
import com.rongji.dfish.framework.plugin.exception.entity.PubExceptionConstant;
import com.rongji.dfish.framework.plugin.exception.entity.PubExceptionRecord;
import com.rongji.dfish.framework.plugin.exception.entity.PubExceptionStack;
import com.rongji.dfish.framework.plugin.exception.entity.PubExceptionType;
import com.rongji.dfish.framework.plugin.exception.service.PubExceptionService;
import com.rongji.dfish.framework.service.impl.AbstractFrameworkService4Simple;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;

public class PubExceptionServiceImpl extends AbstractFrameworkService4Simple<PubExceptionRecord, String> implements PubExceptionService {
    @Resource(name = "pubExceptionDao")
    private PubExceptionDao pubExceptionDao;
    @Override
    public void createTables() {
        pubExceptionDao.createTables();
    }

    @Override
    public List<PubExceptionConstant> listAllConstants() {
        return pubExceptionDao.listAllConstants();
    }

    @Override
    public List<PubExceptionType> findExceptionTypesByName(int name, long cause) {
        return pubExceptionDao.findExceptionTypesByName(name,cause);
    }

    @Override
    public PubExceptionType getExceptionType(long id) {
        return pubExceptionDao.getExceptionType(id);
    }

    @Override
    public List<PubExceptionStack> findExceptionStacksByName(int name, long cause) {
        return pubExceptionDao.findExceptionStacksByName(name,cause);
    }

    @Override
    public List<PubExceptionStack> findExceptionStacks(long typeId) {
        return pubExceptionDao.findExceptionStacks(typeId);
    }

    @Override
    public Integer createOrFindConstByName(String name) {
        return pubExceptionDao.createOrFindConstByName(name);
    }

    @Override
    public String findConst(int id) {
        return pubExceptionDao.findConst(id);
    }

    @Override
    public List<PubExceptionRecord> findRecords(Long typeId, Long beginTime, Long endTime, Pagination page) {
        return pubExceptionDao.findRecords(typeId,beginTime,endTime,page);
    }

    @Override
    public long saveType(PubExceptionType type, List<PubExceptionStack> stacks) {
        return pubExceptionDao.saveType(type, stacks);
    }

    @Override
    public FrameworkDao<PubExceptionRecord, String> getDao() {
        return pubExceptionDao;
    }
}
