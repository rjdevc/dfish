package com.rongji.dfish.framework.plugin.lob.service;

import com.rongji.dfish.framework.plugin.lob.entity.PubLob;
import com.rongji.dfish.framework.service.FrameworkService;

import java.util.Collection;
import java.util.Map;

/**
 * lob数据服务层接口
 * @author lamontYu
 * @date 2019-12-05
 * @since 3.2
 * @version 1.1 改造成接口模式,并调整方法名 lamontYu 2019-12-05
 */
public interface LobService extends FrameworkService<PubLob, PubLob, String> {

    /**
     * 保存lob内容记录
     * @param lobContent 内容
     * @return String 保存的编号
     */
    String saveLob(String lobContent) throws Exception;

    /**
     * 更新lob内容
     * @param lobId 编号
     * @param lobContent 内容
     * @return int 更新记录数
     */
    int updateContent(String lobId, String lobContent);

    /**
     * 归档lob记录
     * @param lobId 编号
     * @return int 更新记录数
     */
    int archive(String lobId);

    /**
     * 获取lob内容
     * @param lobId 编号
     * @return String lob内容值
     */
    String getContent(String lobId);

    /**
     * 批量获取lob内容
     * @param lobIds 编号数组
     * @return Map&lt;String, String&gt; 编号-内容的键值对集合
     */
    Map<String, String> getContents(String... lobIds);

    /**
     * 批量获取lob内容
     * @param lobIds 编号集合
     * @return Map&lt;String, String&gt; 编号-内容的键值对集合
     */
    Map<String, String> getContents(Collection<String> lobIds);

}