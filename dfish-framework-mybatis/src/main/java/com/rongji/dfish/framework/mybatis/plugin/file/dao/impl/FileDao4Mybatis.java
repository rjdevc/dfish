package com.rongji.dfish.framework.mybatis.plugin.file.dao.impl;

import com.rongji.dfish.base.Utils;
import com.rongji.dfish.framework.mybatis.dao.impl.FrameworkDao4Mybatis;
import com.rongji.dfish.framework.plugin.file.dao.FileDao;
import com.rongji.dfish.framework.plugin.file.entity.PubFileRecord;
import org.apache.ibatis.annotations.Param;

import java.util.*;

/**
 * @author lamontYu
 * @create 2019-12-05
 */
public interface FileDao4Mybatis extends FrameworkDao4Mybatis<PubFileRecord, String>, FileDao {

    @Override
    int updateFileLink(@Param("fileId") String fileId, @Param("fileLink") String fileLink, @Param("fileKey") String fileKey,
                       @Param("fileStatus") String fileStatus, @Param("updateTime") Date updateTime);

    @Override
    int updateFileLinks(@Param("fileIds") List<String> fileIds, @Param("fileLink") String fileLink, @Param("fileKey") String fileKey,
                        @Param("fileStatus") String fileStatus, @Param("updateTime") Date updateTime);

    @Override
    int updateFileStatus(@Param("fileIds") Collection<String> fileIds, @Param("fileStatus") String fileStatus, @Param("updateTime") Date updateTime);

    @Override
    int updateFileStatusByLink(@Param("fileLink") String fileLink, @Param("fileKey") String fileKey, @Param("fileStatus") String fileStatus, @Param("updateTime") Date updateTime);

    @Override
    List<PubFileRecord> listByLink(@Param("fileLink") String fileLink, @Param("fileKeys") Collection<String> fileKeys, @Param("fileStatus") String[] fileStatus);

    @Override
    default String getEntityId(PubFileRecord entity) {
        if (entity == null) {
            return null;
        }
        return entity.getFileId();
    }
}
