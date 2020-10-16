package com.rongji.dfish.framework.plugin.exception;

import java.util.Date;

public class PubExceptionRecordDto {
    private String recId;
    private Date eventTime;
    private long typeId;
    private String typeName;
    private String exptMsg;
    private long exptRepetitions;
    private String stack;


    public String getStack() {
        return stack;
    }

    public void setStack(String stack) {
        this.stack = stack;
    }

    public String getRecId() {
        return recId;
    }

    public void setRecId(String recId) {
        this.recId = recId;
    }

    public Date getEventTime() {
        return eventTime;
    }

    public void setEventTime(Date eventTime) {
        this.eventTime = eventTime;
    }

    public long getTypeId() {
        return typeId;
    }

    public void setTypeId(long typeId) {
        this.typeId = typeId;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getExptMsg() {
        return exptMsg;
    }

    public void setExptMsg(String exptMsg) {
        this.exptMsg = exptMsg;
    }

    public long getExptRepetitions() {
        return exptRepetitions;
    }

    public void setExptRepetitions(long exptRepetitions) {
        this.exptRepetitions = exptRepetitions;
    }


}
