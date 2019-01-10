package com.rongji.dfish.uischm;

/**
 * TemplateDefine是模范 nodeJS 定义一个模板
 * <p>define("t/cmd/indx",{"type":"text","name":"xxx","@value":"$data.xxx" });</p>
 * 严格意义上来说它并不是一个有效的JSON格式。这里仅仅定义了他输出格式。
 * @author LinLW
 *
 */
public class SchemaDefine implements DFishSchema{

	private static final long serialVersionUID = -860884319838299839L;
	private String  uri;
	private DFishSchema template;
	public SchemaDefine(String uri,DFishSchema template){
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
			sb.append("$.schema(");
		}else{
			sb.append("$.schema(\"").append(uri).append("\",");
		}
		sb.append(template);
		sb.append(");");
		return sb.toString();
	}

}
