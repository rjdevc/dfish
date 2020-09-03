package com.rongji.dfish.framework.util;

public class DownloadStatus {
    private boolean cached;
    /**
     * 是否已经开始发送数据
     */
    private boolean sent;
    private long resourceLength;
    private long rangeBegin;
    private long rangeEnd;
    private long contentLength;
    private long completeLength;

    public boolean isCached() {
        return cached;
    }

    public void setCached(boolean cached) {
        this.cached = cached;
    }

    public boolean isSent() {
        return sent;
    }

    public void setSent(boolean sent) {
        this.sent = sent;
    }

    public long getResourceLength() {
        return resourceLength;
    }

    public void setResourceLength(long resourceLength) {
        this.resourceLength = resourceLength;
    }

    public long getRangeBegin() {
        return rangeBegin;
    }

    public void setRangeBegin(long rangeBegin) {
        this.rangeBegin = rangeBegin;
    }

    public long getRangeEnd() {
        return rangeEnd;
    }

    public void setRangeEnd(long rangeEnd) {
        this.rangeEnd = rangeEnd;
    }

    public long getContentLength() {
        return contentLength;
    }

    public void setContentLength(long contentLength) {
        this.contentLength = contentLength;
    }

    public long getCompleteLength() {
        return completeLength;
    }

    public void setCompleteLength(long completeLength) {
        this.completeLength = completeLength;
    }

}
