package com.rongji.dfish.framework.plugin.lob.service.impl;

import com.rongji.dfish.base.exception.MarkedException;
import com.rongji.dfish.base.util.LogUtil;
import com.rongji.dfish.base.util.Utils;
import com.rongji.dfish.framework.plugin.lob.dao.LobDao;
import com.rongji.dfish.framework.plugin.lob.entity.PubLob;
import com.rongji.dfish.framework.plugin.lob.service.LobService;
import com.rongji.dfish.framework.service.impl.AbstractFrameworkService4Simple;

import javax.annotation.Resource;
import java.io.*;
import java.util.*;

/**
 * lob数据服务层默认实现
 *
 * @author lamontYu
 * @date 2019-09-23
 * @since 5.0
 */
public class LobServiceImpl extends AbstractFrameworkService4Simple<PubLob, String> implements LobService {
    @Resource(name = "lobDao")
    private LobDao dao;

    @Override
    public LobDao getDao() {
        return dao;
    }

    public void setDao(LobDao dao) {
        this.dao = dao;
    }

    @Override
    public String saveLob(String lobContent) throws Exception {
        if (Utils.isEmpty(lobContent)) {
            throw new MarkedException("文本内容不能为空");
        }
        String lobId = getNewId();
        PubLob pubLob = new PubLob();
        pubLob.setLobId(lobId);
//        pubLob.setLobContent(lobContent);
        pubLob.setOperTime(new Date());
        pubLob.setArchiveFlag("0");
        pubLob.setLobData(lobContent.getBytes("utf-8"));
        getDao().save(pubLob);
        return lobId;
    }


    @Override
    public String saveLobData(InputStream lobData) throws Exception {
        if (lobData == null) {
            throw new MarkedException("存储内容不能为空");
        }
        String lobId = getNewId();
        PubLob pubLob = new PubLob();
        pubLob.setLobId(lobId);
//        pubLob.setLobContent(lobContent);
        pubLob.setOperTime(new Date());
        pubLob.setArchiveFlag("0");
        try {
            byte[] buffer = new byte[lobData.available()];
//            byte[] b = new byte[1024];
//            int num = 0;
//            String res = "";
//            while ((num = lobData.read(b)) != -1)
//            {
//                res += new String(b, 0, num);
//            }
//            byte[] buffer = res.getBytes();
            lobData.read(buffer);
            lobData.close();
            pubLob.setLobData(buffer);
            getDao().save(pubLob);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }// 定义文件读入流
        catch (IOException e) {
            e.printStackTrace();
        }
        return lobId;
    }

    @Override
    public int updateContent(String lobId, String lobContent) {
        return getDao().updateLobData(lobId, lobContent.getBytes(), new Date());
    }

    @Override
    public int updateLobData(String lobId, InputStream lobData) {
        byte[] buffer = null;
        try {
            buffer = new byte[lobData.available()];
            lobData.read(buffer);
            lobData.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }// 定义文件读入流
        catch (IOException e) {
            e.printStackTrace();
        }
        return getDao().updateLobData(lobId, buffer, new Date());
    }

    @Override
    public int archive(String lobId) {
        return getDao().archive(lobId, "1", new Date());
    }


    @Override
    public byte[] getLobData(String lobId) {
        Map<String, byte[]> lobMap = getLobDatas(lobId);
        return lobMap.get(lobId);
    }

    @Override
    public Map<String, byte[]> getLobDatas(String... lobIds) {
        if (Utils.isEmpty(lobIds)) {
            return Collections.emptyMap();
        }
        return getLobDatas(Arrays.asList(lobIds));
    }

    @Override
    public Map<String, byte[]> getLobDatas(Collection<String> lobIds) {
        return getDao().getLobDatas(lobIds);
    }


    @Override
    public String getContent(String lobId) {
        Map<String, String> lobMap = getContents(lobId);
        return lobMap.get(lobId);
    }

    @Override
    public Map<String, String> getContents(String... lobIds) {
        if (Utils.isEmpty(lobIds)) {
            return Collections.emptyMap();
        }
        return getContents(Arrays.asList(lobIds));
    }

    @Override
    public Map<String, String> getContents(Collection<String> lobIds) {
        Map<String, byte[]> lobDatas = getDao().getLobDatas(lobIds);
        Map<String, String> contents = new HashMap<>(lobDatas.size());
        for (Map.Entry<String, byte[]> entry : lobDatas.entrySet()) {
            String value = null;
            if (entry.getValue() != null) {
                try {
                    value = new String(entry.getValue(), "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    LogUtil.error("字符转码异常", e);
                }
            }
            contents.put(entry.getKey(), value);
        }
        return contents;
    }

}
