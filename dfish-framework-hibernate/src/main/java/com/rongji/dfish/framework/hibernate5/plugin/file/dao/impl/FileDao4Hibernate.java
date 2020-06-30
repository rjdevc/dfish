package com.rongji.dfish.framework.hibernate5.plugin.file.dao.impl;

import com.rongji.dfish.base.util.Utils;
import com.rongji.dfish.framework.hibernate5.dao.impl.FrameworkDao4Hibernate;
import com.rongji.dfish.framework.plugin.file.dao.FileDao;
import com.rongji.dfish.framework.plugin.file.entity.PubFileRecord;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.springframework.orm.hibernate5.HibernateCallback;

import java.util.*;

/**
 * 附件file的hibernate实现dao层
 * @author lamontYu
 * @since DFish5.0
 */
public class FileDao4Hibernate extends FrameworkDao4Hibernate<PubFileRecord, String> implements FileDao {
    @Override
    public int updateFileStatus(Collection<String> fileIds, String fileStatus, Date updateTime) {
        if (Utils.isEmpty(fileIds) || Utils.isEmpty(fileStatus)) {
            return 0;
        }
        List<Object> args = new ArrayList<>(fileIds.size() + 2);
        args.add(fileStatus);
        args.add(updateTime);
        args.addAll(fileIds);
        return bulkUpdate("UPDATE PubFileRecord t SET t.fileStatus=?,t.updateTime=? WHERE t.fileId IN (" + getParamStr(fileIds.size()) + ")", args.toArray());
    }

    @Override
    public int updateFileStatusByLink(String fileLink, String fileKey, String fileStatus, Date updateTime) {
        if (Utils.isEmpty(fileLink) || Utils.isEmpty(fileKey) || Utils.isEmpty(fileStatus)) {
            return 0;
        }
        return bulkUpdate("UPDATE PubFileRecord t SET t.fileStatus=?,t.updateTime=? WHERE t.fileLink=? AND t.fileKey=?", new Object[]{fileStatus, updateTime, fileLink, fileKey});
    }

    @Override
    public int updateFileLink(String fileId, String fileLink, String fileKey, String fileStatus, Date updateTime) {
        if (Utils.isEmpty(fileId) || Utils.isEmpty(fileLink)) {
            return 0;
        }
        StringBuilder hql = new StringBuilder();
        List<Object> args = new ArrayList<>(4);
        hql.append("UPDATE PubFileRecord t SET t.fileLink=?,t.updateTime=?");
        args.add(fileLink);
        args.add(updateTime);
        if (Utils.notEmpty(fileKey)) {
            hql.append(",t.fileKey=?");
            args.add(fileKey);
        }
        if (Utils.notEmpty(fileStatus)) {
            hql.append(",t.fileStatus=?");
            args.add(fileStatus);
        }
        hql.append(" WHERE t.fileId=?");
        args.add(fileId);
        return bulkUpdate(hql.toString(), args.toArray());
    }

    @Override
    public int updateFileLinks(List<String> fileIds, String fileLink, String fileKey, String fileStatus, Date updateTime) {
        if (Utils.isEmpty(fileIds) || Utils.isEmpty(fileLink) || Utils.isEmpty(fileKey)) {
            return 0;
        }
        StringBuilder hql = new StringBuilder();
        List<Object> args = new ArrayList<>();

        hql.append("UPDATE PubFileRecord t SET t.fileLink=?,t.fileKey=?,t.updateTime=?");
        args.add(fileLink);
        args.add(fileKey);
        args.add(updateTime);

        if (Utils.notEmpty(fileStatus)) {
            hql.append(",t.fileStatus=?");
            args.add(fileStatus);
        }
        hql.append(" WHERE t.fileId IN(");
        appendParamStr(hql, fileIds.size());
        hql.append(')');
        args.addAll(fileIds);
        return bulkUpdate(hql.toString(), args.toArray());
    }

    @Override
    public List<PubFileRecord> listByLink(String fileLink, Collection<String> fileKeys, String[] fileStatus) {
        if (Utils.isEmpty(fileLink) || Utils.isEmpty(fileKeys)) {
            return Collections.emptyList();
        }
        StringBuilder hql = new StringBuilder();
        List<Object> args = new ArrayList<>(fileKeys.size() + 3);
        hql.append("FROM PubFileRecord t WHERE t.fileLink=? AND t.fileKey IN(");
        appendParamStr(hql, fileKeys.size());
        hql.append(')');
        args.add(fileLink);
        args.addAll(fileKeys);
        if (Utils.notEmpty(fileStatus)) {
            hql.append(" AND t.fileStatus IN (");
            appendParamStr(hql, fileStatus.length);
            hql.append(')');
            args.addAll(Arrays.asList(fileStatus));
        }
        List<PubFileRecord> dataList = queryForList(hql.toString(), args.toArray());
        return dataList;
    }

    @Override
    public int bulkSave(List<PubFileRecord> entities) {
        if (Utils.isEmpty(entities)) {
            throw new IllegalArgumentException("entities can not be null");
        }
        return getHibernateTemplate().execute((session) -> {
            for (PubFileRecord entity : entities) {
                session.save(entity);
            }
            return entities.size();
        });
    }

    @Override
    public int bulkUpdate(List<PubFileRecord> entities) {
        if (Utils.isEmpty(entities)) {
            throw new IllegalArgumentException("entities can not be null");
        }
        return getHibernateTemplate().execute((session) -> {
            for (PubFileRecord entity : entities) {
                session.update(entity);
            }
            return entities.size();
        });
    }

    @Override
    public int bulkDelete(List<PubFileRecord> entities) {
        if (Utils.isEmpty(entities)) {
            throw new IllegalArgumentException("entities can not be null");
        }
        return getHibernateTemplate().execute((session) -> {
            for (PubFileRecord entity : entities) {
                session.delete(entity);
            }
            return entities.size();
        });
    }
}
