package com.rongji.dfish.framework.hibernate.plugin.file.dao.impl;

import com.rongji.dfish.base.Utils;
import com.rongji.dfish.framework.hibernate.dao.impl.FrameworkDao4Hibernate;
import com.rongji.dfish.framework.plugin.file.dao.FileDao;
import com.rongji.dfish.framework.plugin.file.entity.PubFileRecord;

import java.util.*;

/**
 * @author lamontYu
 * @create 2019-12-05
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
        return bulkUpdate("UPDATE PubFileRecord t SET t.fileStatus=?,t.updateTime=? WHERE t.fileId=?", args.toArray());
    }

    @Override
    public int updateFileStatusByLink(String fileLink, String fileKey, String fileStatus, Date updateTime) {
        if (Utils.isEmpty(fileLink) || Utils.isEmpty(fileKey) || Utils.isEmpty(fileStatus)) {
            return 0;
        }
        return bulkUpdate("UPDATE PubFileRecord t SET t.fileStatus=?,t.updateTime=? WHERE t.fileLink=? AND t.fileKey=?", new Object[]{fileStatus, updateTime, fileLink, fileKey});
    }

    @Override
    public int updateFileLink(String fileId, String fileLink, Date updateTime) {
        if (Utils.isEmpty(fileId) || Utils.isEmpty(fileLink)) {
            return 0;
        }
        return bulkUpdate("UPDATE PubFileRecord t SET t.fileLink=?,t.updateTime=? WHERE t.fileId=?", new Object[]{fileLink, updateTime, fileId});
    }

    @Override
    public int updateFileLinks(List<String> fileIds, String fileLink, String fileKey, Date updateTime) {
        if (Utils.isEmpty(fileIds) || Utils.isEmpty(fileLink) || Utils.isEmpty(fileKey)) {
            return 0;
        }
        List<Object> args = new ArrayList<>();
        args.add(fileLink);
        args.add(fileKey);
        args.add(updateTime);
        args.addAll(fileIds);
        return bulkUpdate("UPDATE PubFileRecord t SET t.fileLink=?,t.fileKey=?,t.updateTime=? WHERE t.fileId IN(" + getParamStr(fileIds.size()) + ")", args.toArray());
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
        List<PubFileRecord> dataList = (List<PubFileRecord>) list(hql.toString(), args.toArray());
        return dataList;
    }

}
