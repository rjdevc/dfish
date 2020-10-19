package com.rongji.dfish.framework.hibernate5.plugin.exception.dao.impl;

import com.rongji.dfish.base.Pagination;
import com.rongji.dfish.base.context.SystemContext;
import com.rongji.dfish.base.info.DataBaseInfo;
import com.rongji.dfish.base.util.Utils;
import com.rongji.dfish.framework.hibernate5.dao.impl.FrameworkDao4Hibernate;
import com.rongji.dfish.framework.plugin.exception.entity.PubExceptionConstant;
import com.rongji.dfish.framework.plugin.exception.entity.PubExceptionRecord;
import com.rongji.dfish.framework.plugin.exception.entity.PubExceptionStack;
import com.rongji.dfish.framework.plugin.exception.entity.PubExceptionType;
import com.rongji.dfish.framework.plugin.exception.core.ExceptionManager;
import com.rongji.dfish.framework.plugin.exception.dao.PubExceptionDao;
import org.hibernate.exception.ConstraintViolationException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PubExceptionDao4Hibernate  extends FrameworkDao4Hibernate<PubExceptionRecord, String> implements PubExceptionDao {
    @Override
    public void createTables() {
        //FIXME 建表
        boolean[] talbeExist=new boolean[4];
        Arrays.fill(talbeExist,true);
        try{
            getHibernateTemplate().get(PubExceptionConstant.class, 1);
        }catch (Exception ex){
            talbeExist[0]=false;
        }
        try{
            getHibernateTemplate().get(PubExceptionStack.class, 1L);
        }catch (Exception ex){
            talbeExist[1]=false;
        }
        try{
            getHibernateTemplate().get(PubExceptionType.class, 1L);
        }catch (Exception ex){
            talbeExist[2]=false;
        }
        try{
            getHibernateTemplate().get(PubExceptionRecord.class, "FAKE");
        }catch (Exception ex){
            talbeExist[3]=false;
        }
        String[] createTableSQL=new String[4];
        DataBaseInfo db=SystemContext.getInstance().get(DataBaseInfo.class);
        switch (db.getDatabaseType()){
            case DataBaseInfo.DATABASE_ORACLE:
                createTableSQL[0]="CREATE TABLE PUB_EXCEPTION_CONSTANT(" +
                        "CON_ID NUMBER(9,0) NOT NULL," +
                        "CON_NAME VARCHAR2(255) NOT NULL," +
                        "PRIMARY KEY (CON_ID)" +
                        ");CREATE INDEX IDX_P_CONST_01 ON PUB_EXCEPTION_CONSTANT(CON_NAME);";
                createTableSQL[1]="CREATE TABLE PUB_EXCEPTION_STACK(" +
                        "STACK_ID NUMBER(19,0) NOT NULL," +
                        "TYPE_ID NUMBER(19,0) NOT NULL," +
                        "STACK_ORDER NUMBER(9,0)," +
                        "CLASS_NAME NUMBER(9,0)," +
                        "METHOD_NAME NUMBER(9,0)," +
                        "LINE_NUMBER NUMBER(9,0)," +
                        "FILE_NAME NUMBER(9,0)," +
                        "PRIMARY KEY (STACK_ID)" +
                        ");CREATE INDEX IDX_P_STACK_01 ON PUB_EXCEPTION_STACK(TYPE_ID);";
                createTableSQL[2]="CREATE TABLE PUB_EXCEPTION_TYPE(" +
                        "TYPE_ID NUMBER(19,0) NOT NULL," +
                        "CLASS_NAME NUMBER(9,0) NOT NULL," +
                        "CAUSE_ID NUMBER(19,0)," +
                        "PRIMARY KEY (TYPE_ID)" +
                        ");CREATE INDEX IDX_P_TYPE_01 ON PUB_EXCEPTION_TYPE(CLASS_NAME);";
                createTableSQL[3]="CREATE TABLE PUB_EXCEPTION_RECORD(" +
                        "REC_ID CHAR(32) NOT NULL," +
                        "TYPE_ID NUMBER(19,0) NOT NULL," +
                        "EVENT_TIME NUMBER(19,0) NOT NULL," +
                        "EXPT_MSG VARCHAR2(255)," +
                        "EXPT_REPETITIONS NUMBER(19,0)," +
                        "PRIMARY KEY (TYPE_ID)" +
                        ");CREATE INDEX IDX_P_TYPE_01 ON PUB_EXCEPTION_TYPE(CLASS_NAME);";
            break;
            case DataBaseInfo.DATABASE_MYSQL:
                createTableSQL[0]="CREATE TABLE PUB_EXCEPTION_CONSTANT(" +
                        "CON_ID INT(11) NOT NULL," +
                        "CON_NAME VARCHAR(255) NOT NULL," +
                        "PRIMARY KEY (CON_ID)" +
                        ");CREATE INDEX IDX_P_CONST_01 ON PUB_EXCEPTION_CONSTANT(CON_NAME);";
                createTableSQL[1]="CREATE TABLE PUB_EXCEPTION_STACK(" +
                        "STACK_ID BIGINT(20) NOT NULL," +
                        "TYPE_ID BIGINT(20) NOT NULL," +
                        "STACK_ORDER INT(11)," +
                        "CLASS_NAME INT(11)," +
                        "METHOD_NAME INT(11)," +
                        "LINE_NUMBER INT(11)," +
                        "FILE_NAME INT(11)," +
                        "PRIMARY KEY (STACK_ID)" +
                        ");CREATE INDEX IDX_P_STACK_01 ON PUB_EXCEPTION_STACK(TYPE_ID);";
                createTableSQL[2]="CREATE TABLE PUB_EXCEPTION_TYPE(" +
                        "TYPE_ID BIGINT(20) NOT NULL," +
                        "CLASS_NAME INT(11) NOT NULL," +
                        "CAUSE_ID BIGINT(20)," +
                        "PRIMARY KEY (TYPE_ID)" +
                        ");CREATE INDEX IDX_P_TYPE_01 ON PUB_EXCEPTION_TYPE(CLASS_NAME);";
                createTableSQL[3]="CREATE TABLE PUB_EXCEPTION_RECORD(" +
                        "REC_ID CHAR(32) NOT NULL," +
                        "TYPE_ID BIGINT(20) NOT NULL," +
                        "EVENT_TIME BIGINT(20) NOT NULL," +
                        "EXPT_MSG VARCHAR(255)," +
                        "EXPT_REPETITIONS BIGINT(20)," +
                        "PRIMARY KEY (TYPE_ID)" +
                        ");CREATE INDEX IDX_P_TYPE_01 ON PUB_EXCEPTION_TYPE(CLASS_NAME);";
                break;
            default:
                ExceptionManager.getInstance().shutdown();
        }

        getHibernateTemplate().execute(session->{
            for(int i=0;i<4;i++){
                if(!talbeExist[i]){
                    try {
                        String [] sqls=createTableSQL[i].split(";");
                        for(String sql:sqls){
                            if(Utils.notEmpty(sql)) {
                                session.createSQLQuery(sql).executeUpdate();
                            }
                        }
                    }catch (Exception ex){
                        ex.printStackTrace();
//                        ExceptionManager.getInstance().shutdown();
                    }
                }
            }
            return null;
        });
    }

    @Override
    public List<PubExceptionConstant> listAllConstants() {
        return queryForList("FROM PubExceptionConstant c ORDER BY c.conId");
    }

    @Override
    public List<PubExceptionType> findExceptionTypesByName(int name, long cause) {
        return queryForList("FROM PubExceptionType t WHERE t.className=? AND t.causeId=?", name,cause);
    }

    @Override
    public PubExceptionType getExceptionType(long id) {
        return getHibernateTemplate().get(PubExceptionType.class, id);
    }

    @Override
    public List<PubExceptionStack> findExceptionStacksByName(int name, long cause) {
        return queryForList("FROM PubExceptionStack t2 " +
                "WHERE EXISTS (" +
                "SELECT t.causeId FROM PubExceptionType t " +
                "WHERE t.typeId=t2.typeId AND t.className=? AND t.causeId=?" +
                ") ORDER BY t2.stackOrder ASC", name,cause);
    }

    @Override
    public List<PubExceptionStack> findExceptionStacks(long typeId) {
        return queryForList("FROM PubExceptionStack t WHERE t.typeId=? ORDER BY t.stackOrder ASC", typeId);
    }

    @Override
    public String findConst(int id) {
        return queryForObject("SELECT t.conName FROM PubExceptionConstant t WHERE t.conId=?", id);
    }

    @Override
    public List<PubExceptionRecord> findRecords(Long typeId, Long beginTime, Long endTime, Pagination page) {
        StringBuilder sql=new StringBuilder();
        List<Object> args=new ArrayList<>();
        sql.append("FROM PubExceptionRecord t");
        if(typeId!=null){
            sql.append(" AND t.typeId=?");
            args.add(typeId);
        }
        if(beginTime!=null){
            sql.append(" AND t.eventTime>=?");
            args.add(beginTime);
        }
        if(endTime!=null){
            sql.append(" AND t.eventTime<?");
            args.add(endTime);
        }
        sql.append(" ORDER BY t.recId DESC");
        return queryForList(sql.toString().replaceFirst(" AND ", " WHERE "),page,args.toArray());
    }

    private int knownMaxConstId=0;//缓存，已知的最大constID。因为可能有集群在，他也不一定完全可靠。
    @Override
    public Integer createOrFindConstByName(String name) {
        Number o=queryForObject("SELECT t.conId FROM PubExceptionConstant t WHERE t.conName=?", name);
        if(o==null){
            for(;;){
                try{
                    if(knownMaxConstId<=0){
                        o=queryForObject("SELECT MAX(t.conId) FROM PubExceptionConstant t ");
                        knownMaxConstId=o==null?0:o.intValue();
                    }
                    o=++knownMaxConstId;
                    PubExceptionConstant con=new PubExceptionConstant();
                    con.setConId(knownMaxConstId);
                    con.setConName(name);
                    getHibernateTemplate().save(con);
                    break;
                }catch(ConstraintViolationException e){
                    o=queryForObject("SELECT MAX(t.conId) FROM PubExceptionConstant t ");
                    knownMaxConstId=o==null?0:o.intValue();
                }catch(Throwable e){
                    e.printStackTrace();
                    break;
                    //可能有多机执行，引起主键冲突。
                }
            }
        }
        return o.intValue();
    }
    private Long knownMaxTypeId=null;//缓存，已知的最大typeID。因为可能有集群在，他也不一定完全可靠。
    private Long knownMaxStackId=null;//缓存，已知的最大stackID。因为可能有集群在，他也不一定完全可靠。
    @Override
    public long saveType(PubExceptionType type, List<PubExceptionStack> stacks_) {
        Number o=knownMaxTypeId;
        boolean fail=true;
        do{
            try{
                if(o==null){
                    o=queryForObject("SELECT MAX(t.typeId) FROM PubExceptionType t ");
                }
                long i=o==null?0L:o.longValue();
                o=++i;
                type.setTypeId(o.longValue());
                getHibernateTemplate().save(type);
                knownMaxTypeId=o.longValue();
                fail=false;
            }catch(ConstraintViolationException e){
                o=knownMaxTypeId=null;
            }catch(Throwable e){
                e.printStackTrace();
                break;
                //可能有多机执行，引起主键冲突。
            }
        }while(fail);
        //循环加入stack
        fail=true;
        Number stackId=knownMaxStackId;

        do{
            try{
                if(stackId==null){
                    stackId=queryForObject("SELECT MAX(t.stackId) FROM PubExceptionStack t ");
                }
                final long i=stackId==null?0L:stackId.longValue();
                final Long initTypeId=o.longValue();

                // 批量
                getHibernateTemplate().execute(session->{
                        long id=i;
                        for(int order=0;order<stacks_.size();order++){
                            id++;
                            PubExceptionStack t=stacks_.get(order);
                            t.setStackId(id);
                            t.setTypeId(initTypeId);
                            session.save(t);
                        }
                        return null;
                });
                knownMaxStackId=i+stacks_.size();
                fail=false;
            }catch(ConstraintViolationException e){
                stackId=knownMaxStackId=null;
            }catch(Throwable e){
                e.printStackTrace();
                break;
                //可能有多机执行，引起主键冲突。
            }
        }while(fail);

        return o.longValue();
    }
}
