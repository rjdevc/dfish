package com.rongji.dfish.framework.plugin.file.service;

import java.io.Serializable;

public class FileRecordParam implements Serializable {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String fileLink;
	private String fileKey;
		
	public FileRecordParam() {
	}
	
	public FileRecordParam(String fileLink, String fileKey) {
		this.fileLink = fileLink;
		this.fileKey = fileKey;
	}

	public String getFileLink() {
		return fileLink;
	}
	public void setFileLink(String fileLink) {
		this.fileLink = fileLink;
	}
	public String getFileKey() {
		return fileKey;
	}
	public void setFileKey(String fileKey) {
		this.fileKey = fileKey;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((fileKey == null) ? 0 : fileKey.hashCode());
		result = prime * result
				+ ((fileLink == null) ? 0 : fileLink.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
            return true;
        }
		if (obj == null) {
            return false;
        }
		if (getClass() != obj.getClass()) {
            return false;
        }
		FileRecordParam other = (FileRecordParam) obj;
		if (fileKey == null) {
			if (other.fileKey != null) {
                return false;
            }
		} else if (!fileKey.equals(other.fileKey)) {
            return false;
        }
		if (fileLink == null) {
			if (other.fileLink != null) {
                return false;
            }
		} else if (!fileLink.equals(other.fileLink)) {
            return false;
        }
		return true;
	}
	
	

}
