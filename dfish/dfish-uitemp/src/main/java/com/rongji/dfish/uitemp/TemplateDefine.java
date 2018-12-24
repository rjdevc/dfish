package com.rongji.dfish.uitemp;

public class TemplateDefine implements DFishTemplate{

	private static final long serialVersionUID = -860884319838299839L;
	private String  uri;
	private DFishTemplate template;
	public TemplateDefine(String uri,DFishTemplate template){
		this.uri=uri;
		this.template=template;
	}

	@Override
	public String getType() {
		return null;
	}

	@Override
	public String asJson() {
		return toString();
	}
	public String toString(){
		StringBuilder sb=new StringBuilder();
		
		if(uri==null||uri.equals("")){
			sb.append("define(");
		}else{
			sb.append("define(\"").append(uri).append("\",");
		}
		sb.append(template);
		sb.append(");");
		return sb.toString();
	}

}
