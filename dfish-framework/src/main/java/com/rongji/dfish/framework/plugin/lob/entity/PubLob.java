package com.rongji.dfish.framework.plugin.lob.entity;

import javax.persistence.Column;
import javax.persistence.Id;
import java.util.Date;

/**
 * lob实体类
 * @author lamontYu
 * @date 2019-09-23
 * @since 3.2
 * @version 1.1 去除注解,框架默认采用配置加载模式 lamontYu 2019-12-05
 */
public class PubLob implements java.io.Serializable {

    private static final long serialVersionUID = -3653424051971393087L;
    private String lobId;
    private String lobContent;
    private Date operTime;
    private String archiveFlag;
    private Date archiveTime;
    private byte[] lobData;

    /**
     * default constructor
     */
    public PubLob() {
    }

    /**
     * minimal constructor
     */
    public PubLob(String lobId) {
        this.lobId = lobId;
    }

    /**
     * 编号
     * @return
     */
    @Id
    @Column(name = "LOB_ID")
    public String getLobId() {
        return this.lobId;
    }

    /**
     * 编号
     * @param lobId
     */
    public void setLobId(String lobId) {
        this.lobId = lobId;
    }

    /**
     * lob内容（字符串）
     * @return
     */
    @Column(name = "LOB_CONTENT")
    public String getLobContent() {
        return this.lobContent;
    }

    /**
     * lob内容（字符串）
     * @param lobContent
     */
    public void setLobContent(String lobContent) {
        this.lobContent = lobContent;
    }

    /**
     * 操作时间
     * @return
     */
    @Column(name = "OPER_TIME")
    public Date getOperTime() {
        return this.operTime;
    }

    /**
     * 操作时间
     * @param operTime
     */
    public void setOperTime(Date operTime) {
        this.operTime = operTime;
    }

    /**
     * 归档标识
     * @return
     */
    @Column(name = "ARCHIVE_FLAG")
    public String getArchiveFlag() {
        return this.archiveFlag;
    }

    /**
     * 归档标识
     * @param archiveFlag
     */
    public void setArchiveFlag(String archiveFlag) {
        this.archiveFlag = archiveFlag;
    }

    /**
     * 归档时间
     * @return
     */
    @Column(name = "ARCHIVE_TIME")
    public Date getArchiveTime() {
        return this.archiveTime;
    }

    /**
     * 归档时间
     * @param archiveTime
     */
    public void setArchiveTime(Date archiveTime) {
        this.archiveTime = archiveTime;
    }

    /**
     * lob内容（二进制）
     * @return
     */
    @Column(name = "LOB_DATA")
    public byte[] getLobData() {
        return lobData;
    }

    /**
     * lob内容（二进制）
     * @param lobData
     */
    public void setLobData(byte[] lobData) {
        this.lobData = lobData;
    }

}