package com.rongji.dfish.framework.plugin.exception.service;

import com.rongji.dfish.base.Pagination;
import com.rongji.dfish.framework.plugin.exception.entity.PubExceptionConstant;
import com.rongji.dfish.framework.plugin.exception.entity.PubExceptionRecord;
import com.rongji.dfish.framework.plugin.exception.entity.PubExceptionStack;
import com.rongji.dfish.framework.plugin.exception.entity.PubExceptionType;
import com.rongji.dfish.framework.service.FrameworkService;

import java.util.List;

public interface PubExceptionService extends FrameworkService<PubExceptionRecord,PubExceptionRecord, String> {
    /**
     * 确认表存在。如果不存在要建表
     */
    void createTables();

    /**
     * 获取所有的常量信息
     * @return
     */
    List<PubExceptionConstant> listAllConstants();

    /**
     * 根据异常名获得所有的ExceptionType
     * @param name
     * @param cause
     * @return
     */
    List<PubExceptionType> findExceptionTypesByName(int name, long cause);

    /**
     * 根据ID获得 PubExceptionType
     * @param id
     * @return
     */
    PubExceptionType getExceptionType(long id);
    /**
     * 根据异常名获得所有相关的 堆栈信息
     * @param name
     * @param cause
     * @return
     */
    List<PubExceptionStack> findExceptionStacksByName(int name, long cause);

    /**
     * 根据ID获得所有相关的 堆栈信息
     * @param typeId
     * @return
     */
    List<PubExceptionStack> findExceptionStacks(long typeId);

    /**
     * 这个name 在缓存中找不到，尝试从数据库(集群的时候)找到，或者新建一个。并返回ID。
     * @param name String
     * @return Integer
     */
    Integer createOrFindConstByName(String name);


    /**
     * 已知常量ID。获取常量
     * @param id int
     * @return
     */
    String findConst(int id);

    /**
     * 获取异常记录列表，如果指定了typeId 则只显示改TYPE ID 的信息。
     * @param typeId Long
     * @param page Pagination
     * @return List
     */
    public List<PubExceptionRecord> findRecords(Long typeId,Long beginTime, Long endTime, Pagination page);


    /**
     * 保存异常类型 并返回ID
     * @param type 异常类型
     * @param stacks 堆栈信息
     * @return long
     */
    long saveType(PubExceptionType type, List<PubExceptionStack> stacks);
}
