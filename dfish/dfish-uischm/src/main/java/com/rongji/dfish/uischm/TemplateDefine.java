package com.rongji.dfish.uischm;

import com.rongji.dfish.ui.AbstractJsonObject;
import com.rongji.dfish.ui.JsonObject;

/**
 * TemplateDefine是模范 nodeJS 定义一个模板
 * <p>define("t/cmd/indx",{"type":"text","name":"xxx","@value":"$data.xxx" });</p>
 * 严格意义上来说它并不是一个有效的JSON格式。这里仅仅定义了他输出格式。
 * @author LinLW
 *
 */
public class TemplateDefine implements DFishSchema{

	private static final long serialVersionUID = -860884319838299839L;
	private String  uri;
	private JsonObject jo;
	public TemplateDefine(String uri,JsonObject jo){
		this.uri=uri;
		this.jo=jo;
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
			sb.append("$.template(");
		}else{
			sb.append("$.template(\"").append(uri).append("\",");
		}
		if(jo instanceof AbstractJsonObject){
			((AbstractJsonObject<?>) jo).formatString();
		}else{
			jo.toString();
		}
		sb.append(");");
		return sb.toString();
	}

}
