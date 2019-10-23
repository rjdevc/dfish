package com.rongji.dfish.misc;

import java.util.HashMap;
import java.util.Map;


@Deprecated
public class CaseInsensitiveTextReplacer {
	/**
	 * <p>TextReplacer 为字符串替换工具，用于高效地替换多个关键词。</p>
	 * <p>一般可用于敏感词替换，为关键词增加超链接等地方</p>
	 * <p>ps:这只是个替换工具，如果要增加超链接，需要自行增加完整文本 比如</p>
	 * <table>
	 * <tr>
	 * <td><b>关键词</b></td>
	 * <td>福州市</td>
	 * </tr>
	 * <tr>
	 * <td><b>替换为</b></td>
	 * <td>&lt;a href="http://www.fuzhou.gov.cn"&gt;福州市&lt;/a&gt;</td>
	 * </tr>
	 * </table>
	 * <p>本工具只提供替换功能，如果已经增加过超链接的文本，再次替换的话，可能会出现问题。又比如说HTML中可能有一些关键字并不适合替换如
	 * &lt;img alt="福州市夜景"/ src="xxx"&gt;</p>
	 * <p>其工作原理为加载关键词的时候，对关键字进行排列。并在替换的时候，根据关键字的顺序快速定位关键字，而非循环判定。</p>
	 * <p>如果两个关键字有包含关系 如 <b>福州</b> 与 <b>福州市</b> 那么优先替换长的关键字。</p>
	 */
	public CaseInsensitiveTextReplacer(){
		
	}

	private KeyValuePair root=new KeyValuePair((char)0);
	public void addKeyWord(String keyWord,String replaceTo){
		root.setKeyValue(keyWord,replaceTo);
	}
	public String replaceText(String src){
		StringBuilder result=new StringBuilder();
		char[] content=src.toCharArray();
		for(int i=0;i<content.length;i++){
			FindResult r=root.match(content,i,content.length);
			if(r.state==FindResult.STATE_MATCH){
				result.append(r.replaceTo);
				i+=(r.end-r.begin-1);
//				System.out.println("找到关键字["+src.substring(r.begin,r.end)+"]位置为["+r.begin+","+r.end+"]");
			}else{
				result.append(content[i]);
			}
		}
		
		return result.toString();
	}
	static class KeyValuePair{
		KeyValuePair(char c){
			key=c;
		}
		public FindResult match(char[] content, int begin, int end) {
		
			char c=content[begin];
			FindResult result=new FindResult(FindResult.STATE_UNMATCH,begin,begin);
			if(children==null){
				return result;
			}
			KeyValuePair p=children.get(c);
			if(p==null){
				return result;
			}else{
				if(p.wordFinish){
					result.state=FindResult.STATE_MATCH;
					result.begin=begin;
					result.end=begin+1;
					result.replaceTo=p.replaceTo;
					//找到一个匹配项，如果没有更长的将使用这个匹配项。
					//不能直接return;
				}
				if(p.children!=null){
					if(begin+1<end){
						FindResult subMatch=p.match(content, begin+1, end);
						if(subMatch.state==FindResult.STATE_MATCH){
							result.state=FindResult.STATE_MATCH;
							result.begin=begin;
							result.end=subMatch.end;
							result.replaceTo=subMatch.replaceTo;
							return result;
						}
					}
				}
			}
			
			return result;
		}
		public void setKeyValue(String keyWord, String replaceTo) {
			setKeyValue(keyWord.toCharArray(),0,keyWord.length(),replaceTo);
		}
		void setKeyValue(char[] charArray,int begin,int end, String replaceTo) {
			if(end-begin<=0) {
                return;
            }
			if(children==null){
				children=new CaseInsensitiveMap();
			}
			char c=charArray[begin];
			KeyValuePair sub=children.get(c);
			if(sub==null){
				sub=new KeyValuePair(c);
				children.put(c, sub);
			}
			if( end-begin ==1){
				sub.wordFinish=true;
				sub.replaceTo=replaceTo;
			}else{
				sub.setKeyValue(charArray, begin+1,end,replaceTo);
			}
		}

//		public String toString(){
//			StringBuilder sb=new StringBuilder();
//			show(sb,0,this);
//			return sb.toString();
//		}
//		void show(StringBuilder sb,int level,KeyValuePair p){
//			for(int i=0;i<level;i++){
//				sb.append('-');
//			}
//			sb.append(p.key);
//			if(p.wordFinish){
//				sb.append(" replace=").append(p.replaceTo);
//			}
//			sb.append("\r\n");
//			if(p.children!=null){
//				for(KeyValuePair sub:p.children.values()){
//					show(sb,level+1,sub);
//				}
//			}
//		}
		char key;
		boolean wordFinish;
		String replaceTo;
		Map<Character , KeyValuePair> children;
	}
	static class FindResult{
		public FindResult(int state, int begin, int end) {
			this.state=state;
			this.begin=begin;
			this.end=end;
		}

		int begin;
		int end;
		int state;
		String replaceTo;
		static final int STATE_MATCH=1;
		static final int STATE_UNMATCH=0;
		static final int STATE_PREFIX=2;
	}
	
	static class CaseInsensitiveMap extends HashMap<Character , KeyValuePair>{

		private static final long serialVersionUID = 1496966445805110348L;

		@Override
		public KeyValuePair get(Object key) {
			if(key instanceof Character){
				char realKey=Character.toLowerCase((Character) key);
				return super.get(realKey);
			}
			return super.get(key);
		}

		@Override
		public KeyValuePair put(Character key, KeyValuePair value) {
			char realKey=Character.toLowerCase(key);
			return super.put(realKey, value);
		}
		
	}
}
