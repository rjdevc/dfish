package com.rongji.dfish.ui.json;

public class JsonBuilderParam {
	public JsonBuilderParam(){
		this.dateFormat="yyyy-MM-dd HH:mm:ss";
	}
	private String dateFormat;
	private String numberFormat;
	public String getDateFormat() {
		return dateFormat;
	}
	public JsonBuilderParam setDateFormat(String dateFormat) {
		this.dateFormat = dateFormat;
		return this;
	}
	public String getNumberFormat() {
		return numberFormat;
	}
	public JsonBuilderParam setNumberFormat(String numberFormat) {
		this.numberFormat = numberFormat;
		return this;
	}
	
}
