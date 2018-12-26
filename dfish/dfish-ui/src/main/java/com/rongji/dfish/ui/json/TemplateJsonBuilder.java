package com.rongji.dfish.ui.json;

import java.util.Map;
import java.util.Stack;

import com.rongji.dfish.ui.TemplateSupport;

public class TemplateJsonBuilder extends ClassJsonBuilder {
	public TemplateJsonBuilder(Class<?> clz) {
		super(clz);
	}

	@Override
	public void buildJson(Object o, StringBuilder sb,Stack<PathInfo> path) {
		sb.append('{');
		boolean begin =true;
		for(JsonPropAppender entry:methods){
			try {
				begin=entry.appendProperty(o,sb,path,begin);
			} catch (Exception e) {
				J.LOG.debug(null,e);
			}
		}
		//如果o是TemplateSupport的话，采用独立的构造器
		if(o instanceof TemplateSupport){
			Map<String,String>ats=((TemplateSupport) o).ats();
			if(ats!=null){
				for(Map.Entry<String,String>entry:ats.entrySet()){
					String propName=entry.getKey();
					String v=entry.getValue();
					if(v!=null&&!v.equals("")){
						if(begin){begin=false;}else{sb.append(',');}
						sb.append('"');
						sb.append('@');
						sb.append(propName);
						sb.append("\":\"");
						escapeJson(v, sb);//可能有转义字符
						sb.append('"');
					}
				}
			}
		}
		sb.append('}');
	}
}
