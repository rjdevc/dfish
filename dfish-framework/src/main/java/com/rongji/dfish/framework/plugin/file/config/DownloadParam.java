package com.rongji.dfish.framework.plugin.file.config;

/**
 * @author lamontYu
 * @since
 */
public class DownloadParam {
    boolean inline;
    String fileName;
    String extension;
    long fileSize;
    long lastModified;
    String alias;
    String encryptedFileId;


    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public DownloadParam() {
    }

    public DownloadParam(String fileName, long fileSize, long lastModified) {
        this.fileName = fileName;
        this.fileSize = fileSize;
        this.lastModified = lastModified;
    }

    public DownloadParam(String fileName, long fileSize, long lastModified, String alias, boolean inline) {
        this.fileName = fileName;
        this.fileSize = fileSize;
        this.lastModified = lastModified;
        this.alias = alias;
        this.inline = inline;
    }

    public boolean isInline() {
        return inline;
    }

    public DownloadParam setInline(boolean inline) {
        this.inline = inline;
        return this;
    }

    public String getFileName() {
        return fileName;
    }

    public DownloadParam setFileName(String fileName) {
        this.fileName = fileName;
        return this;
    }

    public long getFileSize() {
        return fileSize;
    }

    public DownloadParam setFileSize(long fileSize) {
        this.fileSize = fileSize;
        return this;
    }

    public long getLastModified() {
        return lastModified;
    }

    public DownloadParam setLastModified(long lastModified) {
        this.lastModified = lastModified;
        return this;
    }

    public String getAlias() {
        return alias;
    }

    public DownloadParam setAlias(String alias) {
        this.alias = alias;
        return this;
    }

    public String getEncryptedFileId() {
        return encryptedFileId;
    }

    public DownloadParam setEncryptedFileId(String encryptedFileId) {
        this.encryptedFileId = encryptedFileId;
        return this;
    }
}
