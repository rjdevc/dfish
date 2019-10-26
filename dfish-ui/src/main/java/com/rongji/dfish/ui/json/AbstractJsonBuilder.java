package com.rongji.dfish.ui.json;

/**
 * AbstractJsonBuilder 抽象转化器，用于方便其他转化器的编写
 * @author DFish Team
 *
 */
public abstract class AbstractJsonBuilder implements JsonBuilder {
	@Override
	public void removeProperty(String propName){
		throw new java.lang.UnsupportedOperationException();
	}
	@Override
	public void replaceProperty(String propName, String newName){
		throw new java.lang.UnsupportedOperationException();
	}

	/**
	 * 转义JSON格式的字符串
	 * <p>这里会忽略掉控制字符 ASCII (0-31)</p>
	 * @param s String
	 * @param sb StringBuilder
	 */
	public static void escapeJson(String s,StringBuilder sb){
		if(s==null) {
            return;
        }
		for(char c:s.toCharArray()){
			switch(c){
			case '\\':sb.append("\\\\");break;
			case '"':sb.append("\\\"");break;
			case '\r':sb.append("\\r");break;
			case '\n':sb.append("\\n");break;
			case '\b':sb.append("\\b");break;
			case '\f':sb.append("\\f");break;
			case '\t':sb.append("\\t");break;
			default :if(c<0||c>31) {
                sb.append(c);
            }
			}
		}
	}

}
