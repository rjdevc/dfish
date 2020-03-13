package com.rongji.dfish.ui.json;

import java.util.Map;
import java.util.Stack;

/**
 * 把map中的数据转化为Json格式。
 * 这里map的key 必须是String型或可以直接转化成String的类型
 * value的内容，按一般Object规则转化。
 * <p>(V3.0)这里的key 为了性能没有转义，不要试用可转移的字符作为关键字</p>
 * @author DFish Team
 *
 */
public class MapJsonBuilder extends AbstractJsonBuilder{


	@Override
    public void buildJson(Object o, StringBuilder sb, Stack<PathInfo> path) {
		boolean hasContent =false;
		sb.append('{');
		Map<?,?> cast=(Map<?,?>)o;
		for(Map.Entry<?,?> item:cast.entrySet()){
			String key=(String) item.getKey();
			Object value=item.getValue();
			if(value==null) {
                continue;
            }
			int sbLen=sb.length();
			boolean hasContentOld=hasContent;
			if(hasContent) {
                sb.append(',');
            } else {
                hasContent=true;
            }
			sb.append('"');
			sb.append(key);
			sb.append('"');
			sb.append(':');
			int sbBeginAppend=sb.length();
			path.push(new PathInfo(key,value));
			JsonFormat.buildJson(value, sb,path);
			path.pop();
			if((sb.length()-sbBeginAppend==2&&
					sb.charAt(sbBeginAppend)=='{'&&sb.charAt(sbBeginAppend+1)=='}')){
				//回滚RawJsonBuilder
				sb.delete(sbLen, sb.length());
				hasContent=hasContentOld;
			}
		}
		sb.append('}');
	}
}
