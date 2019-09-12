package com.rongji.dfish.ui.template;

import com.rongji.dfish.base.Utils;
import com.rongji.dfish.ui.AbstractNode;
import com.rongji.dfish.ui.JsonObject;

/**
 * PreloadDefine是模范 nodeJS 定义一个模板
 * <p>define("t/cmd/indx",{"type":"text","name":"xxx","@value":"$data.xxx" });</p>
 * 严格意义上来说它并不是一个有效的JSON格式。这里仅仅定义了他输出格式。
 * @author LinLW
 *
 */
public class PreloadDefine implements DFishTemplate {

	private static final long serialVersionUID = -860884319838299839L;
	private String  uri;
	private JsonObject jo;
	public PreloadDefine(String uri,JsonObject jo){
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

		sb.append("define.preload(");
		if(Utils.notEmpty(uri)){
			sb.append("\"").append(uri).append("\",");
		}
		if(jo instanceof AbstractNode){
			sb.append(((AbstractNode<?>) jo).formatString());
		}else{
			sb.append(jo.toString());
		}
		sb.append(");");
		return sb.toString();
	}

}
