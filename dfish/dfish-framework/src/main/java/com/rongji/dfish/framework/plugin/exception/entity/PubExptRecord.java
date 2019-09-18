package com.rongji.dfish.framework.plugin.exception.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "PUB_EXPT_RECORD")
public class PubExptRecord implements java.io.Serializable {

    private static final long serialVersionUID = -1239029459275581296L;

    private String recId;
    private Long eventTime;
    private Long typeId;
    private String exptMsg;
    private Long exptRepetitions;

    @Id
    @Column(name = "REC_ID", unique = true, nullable = false, length = 32)
    public String getRecId() {
        return recId;
    }

    public void setRecId(String recId) {
        this.recId = recId;
    }

    @Column(name = "EVENT_TIME", nullable = false, length = 18)
    public Long getEventTime() {
        return eventTime;
    }

    public void setEventTime(Long eventTime) {
        this.eventTime = eventTime;
    }

    @Column(name = "TYPE_ID", nullable = false, length = 18)
    public Long getTypeId() {
        return typeId;
    }

    public void setTypeId(Long typeId) {
        this.typeId = typeId;
    }

    @Column(name = "EXPT_MSG", length = 255)
    public String getExptMsg() {
        return exptMsg;
    }

    public void setExptMsg(String exptMsg) {
        this.exptMsg = exptMsg;
    }

    @Column(name = "EXPT_REPETITIONS", length = 255)
    public Long getExptRepetitions() {
        return exptRepetitions;
    }

    public void setExptRepetitions(Long exptRepetitions) {
        this.exptRepetitions = exptRepetitions;
    }
}
