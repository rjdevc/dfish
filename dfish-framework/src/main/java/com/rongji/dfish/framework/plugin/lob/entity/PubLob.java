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

    @Id
    @Column(name = "LOB_ID")
    public String getLobId() {
        return this.lobId;
    }

    public void setLobId(String lobId) {
        this.lobId = lobId;
    }

    @Column(name = "LOB_CONTENT")
    public String getLobContent() {
        return this.lobContent;
    }

    public void setLobContent(String lobContent) {
        this.lobContent = lobContent;
    }

    @Column(name = "OPER_TIME")
    public Date getOperTime() {
        return this.operTime;
    }

    public void setOperTime(Date operTime) {
        this.operTime = operTime;
    }

    @Column(name = "ARCHIVE_FLAG")
    public String getArchiveFlag() {
        return this.archiveFlag;
    }

    public void setArchiveFlag(String archiveFlag) {
        this.archiveFlag = archiveFlag;
    }

    @Column(name = "ARCHIVE_TIME")
    public Date getArchiveTime() {
        return this.archiveTime;
    }

    public void setArchiveTime(Date archiveTime) {
        this.archiveTime = archiveTime;
    }
    @Column(name = "LOB_DATA")
    public byte[] getLobData() {
        return lobData;
    }

    public void setLobData(byte[] lobData) {
        this.lobData = lobData;
    }

}