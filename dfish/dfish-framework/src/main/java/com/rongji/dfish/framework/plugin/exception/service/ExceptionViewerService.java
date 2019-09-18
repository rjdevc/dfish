package com.rongji.dfish.framework.plugin.exception.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.rongji.dfish.framework.service.BaseService4Simple;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;

import com.rongji.dfish.base.Page;
import com.rongji.dfish.base.Utils;
import com.rongji.dfish.framework.plugin.exception.entity.PubExptRecord;
import com.rongji.dfish.framework.plugin.exception.entity.PubExptStack;
import com.rongji.dfish.framework.plugin.exception.entity.PubExptType;
import com.rongji.dfish.framework.service.BaseService;

public class ExceptionViewerService extends BaseService4Simple<PubExptRecord> {

    @SuppressWarnings("unchecked")
    public List<PubExptRecord> findRecords(Page page, Long typeId) {
        List<PubExptRecord> recs = null;
        if (typeId == null) {
            recs = (List<PubExptRecord>) pubCommonDAO.getQueryList("FROM PubExptRecord t ORDER BY t.recId DESC", page);
        } else {
            recs = (List<PubExptRecord>) pubCommonDAO.getQueryList("FROM PubExptRecord t WHERE t.typeId=? ORDER BY t.recId DESC", page, new Long(typeId));
        }
        return recs;
    }

    /**
     * 删除所有记录（四张表全部清空）
     */
    public void deleteAllExpt() {
        pubCommonDAO.getHibernateTemplate().execute(new HibernateCallback<Object>() {
            public Object doInHibernate(Session session) throws HibernateException, SQLException {
                session.createSQLQuery("truncate pub_expt_record").executeUpdate();
                session.createSQLQuery("truncate pub_expt_stack").executeUpdate();
                session.createSQLQuery("truncate pub_expt_type").executeUpdate();
                session.createSQLQuery("truncate pub_expt_constant").executeUpdate();
                return null;
            }
        });
    }

    /**
     * 去重
     */
    public void distinct() {
        distinct4Constant();
        distinct4Type();
    }

    /**
     * constant表去重
     */
    @SuppressWarnings("unchecked")
    private void distinct4Constant() {
        class ConstantUpdate {
            private int targetId;
            private List<Integer> needChange;

            ConstantUpdate(int targetId, List<Integer> needChange) {
                this.targetId = targetId;
                this.needChange = needChange;
            }

            public int getTargetId() {
                return targetId;
            }

            public List<Integer> getNeedChange() {
                return needChange;
            }
        }

        Session session = pubCommonDAO.getHibernateTemplate().getSessionFactory().openSession();

        List<Integer> conIdList = (List<Integer>) pubCommonDAO.getQueryList("SELECT c1.conId FROM PubExptConstant c1 WHERE c1.conId IN "
                + "(SELECT min(c2.conId) FROM PubExptConstant c2 group by c2.conName having count(c2.conId)>1)");
        List<ConstantUpdate> updateList = new ArrayList<ConstantUpdate>();
        for (Integer conId : conIdList) {
            List<Integer> needChange = (List<Integer>) pubCommonDAO.getQueryList("SELECT c1.conId FROM PubExptConstant c1 WHERE "
                    + "c1.conId!=" + conId + " and c1.conName=(SELECT c2.conName FROM PubExptConstant c2 WHERE c2.conId=" + conId + ")");
            updateList.add(new ConstantUpdate(conId, needChange));
        }
        for (ConstantUpdate update : updateList) {
            List<Integer> needChange = update.getNeedChange();
            String changId = needChange.toString();
            changId = changId.replace('[', '(');
            changId = changId.replace(']', ')');
            session.createSQLQuery("UPDATE pub_expt_type SET CLASS_NAME = " + update.getTargetId() +
                    " WHERE CLASS_NAME IN " + changId).executeUpdate();
            session.createSQLQuery("UPDATE pub_expt_stack SET CLASS_NAME = " + update.getTargetId() +
                    " WHERE CLASS_NAME IN " + changId).executeUpdate();
            session.createSQLQuery("UPDATE pub_expt_stack SET METHOD_NAME = " + update.getTargetId() +
                    " WHERE METHOD_NAME IN " + changId).executeUpdate();
            session.createSQLQuery("UPDATE pub_expt_stack SET FILE_NAME = " + update.getTargetId() +
                    " WHERE FILE_NAME IN " + changId).executeUpdate();
            session.createSQLQuery("DELETE FROM pub_expt_constant WHERE CON_ID IN " + changId).executeUpdate();
        }

        session.close();
    }

    /**
     * type表去重
     */
    @SuppressWarnings("unchecked")
    private void distinct4Type() {

        List<List<PubExptType>> typeList = new ArrayList<List<PubExptType>>();
        Map<PubExptType, List<PubExptStack>> exceptionInfoMap = new HashMap<>();

        List<PubExptType> groupList = (List<PubExptType>) pubCommonDAO.getQueryList("FROM PubExptType t1 WHERE t1.typeId IN "
                + "(SELECT min(t2.typeId) FROM PubExptType t2 group by t2.className,t2.causeId) ORDER BY t1.typeId");
        for (PubExptType pubExptType : groupList) {
            typeList.add((List<PubExptType>) pubCommonDAO.getQueryList("FROM PubExptType t1 "
                    + "WHERE t1.className= " + pubExptType.getClassName() + " and t1.causeId=" + pubExptType.getCauseId() + " ORDER BY t1.typeId"));
        }

        List<Long> oldTypeList = new ArrayList<Long>();
        List<Long> newTypeList = new ArrayList<Long>();
        for (List<PubExptType> list : typeList) {
            for (int i = 0; i < list.size() - 1; i++) {
                PubExptType type1 = list.get(i);

                for (int j = i + 1; j < list.size(); j++) {
                    PubExptType type2 = list.get(j);
                    if (equals(type1, type2, exceptionInfoMap)) {
                        newTypeList.add(type1.getTypeId());
                        oldTypeList.add(type2.getTypeId());
                        list.remove(j);
                        j--;
                    }
                }
            }
        }
        Session session = pubCommonDAO.getHibernateTemplate().getSessionFactory().openSession();
        for (int i = 0; i < oldTypeList.size(); i++) {
            session.createSQLQuery("DELETE FROM pub_expt_stack WHERE TYPE_ID=" + oldTypeList.get(i)).executeUpdate();
            session.createSQLQuery("UPDATE pub_expt_record SET TYPE_ID=" + newTypeList.get(i) + " WHERE TYPE_ID=" + oldTypeList.get(i)).executeUpdate();
            session.createSQLQuery("DELETE FROM pub_expt_type WHERE TYPE_ID=" + oldTypeList.get(i)).executeUpdate();
        }
        session.close();

    }

    private boolean equals(PubExptType type1, PubExptType type2, Map<PubExptType, List<PubExptStack>> exceptionInfoMap) {
        List<PubExptStack> exceptionInfo1 = findOrCreateException(type1, exceptionInfoMap);
        List<PubExptStack> exceptionInfo2 = findOrCreateException(type2, exceptionInfoMap);
        if (exceptionInfo1 == null && exceptionInfo2 == null) {
            return true;
        } else if (exceptionInfo1 == null || exceptionInfo2 == null) {
            return false;
        }
        if (exceptionInfo1.size() != exceptionInfo2.size()) {
            return false;
        }
        int length = exceptionInfo1.size();
        for (int i = 0; i < length; i++) {
            PubExptStack pubExptStack1 = exceptionInfo1.get(i);
            PubExptStack pubExptStack2 = exceptionInfo2.get(i);
            if (pubExptStack1 != null && !pubExptStack1.equals(pubExptStack2)) {
                return false;
            }
        }
        return true;
    }

    @SuppressWarnings("unchecked")
    private List<PubExptStack> findOrCreateException(PubExptType type, Map<PubExptType, List<PubExptStack>> exceptionInfoMap) {
        List<PubExptStack> stackInfo = exceptionInfoMap.get(type);
        if (stackInfo == null) {
            stackInfo = (List<PubExptStack>) pubCommonDAO.getQueryList("FROM PubExptStack s1 WHERE s1.typeId = "
                    + type.getTypeId() + " ORDER BY s1.stackOrder");
            exceptionInfoMap.put(type, stackInfo);
        }
        return stackInfo;
    }
}
