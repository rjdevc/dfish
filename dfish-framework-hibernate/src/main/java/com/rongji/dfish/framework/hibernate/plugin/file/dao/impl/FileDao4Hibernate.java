package com.rongji.dfish.framework.hibernate.plugin.file.dao.impl;

import com.rongji.dfish.base.Utils;
import com.rongji.dfish.framework.hibernate.dao.impl.FrameworkDao4Hibernate;
import com.rongji.dfish.framework.plugin.file.dao.FileDao;
import com.rongji.dfish.framework.plugin.file.dto.FileQueryParam;
import com.rongji.dfish.framework.plugin.file.entity.PubFileRecord;
import com.rongji.dfish.ui.form.UploadItem;

import java.util.*;

/**
 * @author lamontYu
 * @create 2019-12-05
 */
public class FileDao4Hibernate extends FrameworkDao4Hibernate<PubFileRecord, String> implements FileDao {
    @Override
    public int updateFileStatus(String fileId, String fileStatus) {
        if (Utils.isEmpty(fileId) || Utils.isEmpty(fileStatus)) {
            return 0;
        }
        return bulkUpdate("UPDATE PubFileRecord t SET t.fileStatus=? WHERE t.fileId=?", new Object[]{fileStatus, fileId});
    }

    @Override
    public int updateFileStatus(String fileLink, String fileKey, String fileStatus) {
        if (Utils.isEmpty(fileLink) || Utils.isEmpty(fileKey) || Utils.isEmpty(fileStatus)) {
            return 0;
        }
        return bulkUpdate("UPDATE PubFileRecord t SET t.fileStatus=? WHERE t.fileLink=? AND t.fileKey=?", new Object[]{fileStatus, fileLink, fileKey});
    }

    @Override
    public int updateFileLink(String fileId, String fileLink) {
        if (Utils.isEmpty(fileId) || Utils.isEmpty(fileLink)) {
            return 0;
        }
        return bulkUpdate("UPDATE PubFileRecord t SET t.fileLink=? WHERE t.fileId=?", new Object[]{fileLink, fileId});
    }

    @Override
    public int updateFileLink(List<String> fileIds, String fileLink, String fileKey) {
        if (Utils.isEmpty(fileIds) || Utils.isEmpty(fileLink) || Utils.isEmpty(fileKey)) {
            return 0;
        }
        return bulkUpdate("UPDATE PubFileRecord t SET t.fileLink=?,t.fileKey=? WHERE t.fileId IN(" + getParamStr(fileIds.size()) + ")", fileIds.toArray());
    }

    @Override
    public Map<String, List<PubFileRecord>> getRecords(String fileLink, Collection<String> fileKeys, String[] fileStatus) {
        if (Utils.isEmpty(fileLink) || Utils.isEmpty(fileKeys)) {
            return Collections.emptyMap();
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
        Map<String, List<PubFileRecord>> dataMap = new HashMap<>(fileKeys.size());
        for (PubFileRecord data : dataList) {
            List<PubFileRecord> subList = dataMap.get(data.getFileKey());
            if (subList == null) {
                subList = new ArrayList<>();
                dataMap.put(data.getFileKey(), subList);
            }
            subList.add(data);
        }
        return dataMap;
    }
}
