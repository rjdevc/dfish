package com.rongji.dfish.misc.senswords;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import com.rongji.dfish.base.TrieTree;
import com.rongji.dfish.base.TrieTree.SearchResult;

/**
 * 敏感词过滤器
 * @author LinLW
 *
 */
public class SensitiveWords {
	private static SensitiveWords instance=new SensitiveWords();
	/**
	 * 取得实例
	 * @return SensitiveWords
	 */
	public static SensitiveWords getInstance(){
		return instance;
	}
	TrieTree<String> core=new TrieTree<String>();
	private SensitiveWords(){
		super();
		loadDic();
	}
	private void loadDic() {
		try{
			InputStream is=getClass().getResourceAsStream("/com/rongji/dfish/misc/senswords/main_dic.txt");
			BufferedReader bis=new BufferedReader(new InputStreamReader(is,"UTF-8"));
			String line="";
			while ((line=bis.readLine())!=null){
				core.put(line, "");
			}
			if(bis!=null){
				bis.close();
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	/**
	 * 是否包含敏感词 
	 * @param source String
	 * @return boolean
	 */
	public boolean match(String source){
		if(source==null)return false;
		List<SearchResult<String>> matches=  core.search(source);
		return matches!=null&&matches.size()>0;
	}
	/**
	 * 将句子中的所有敏感词替换成指定词
	 * @param source String
	 * @param replaceTo String 默认为**
	 * @return String
	 */
	public String replace (String source,String replaceTo){
		if(source==null)return null;
		if(replaceTo==null)replaceTo="**";
		StringBuilder result=new StringBuilder();
		List<SearchResult<String>> matches=  core.search(source);
		int lastMatchEnd=0;
		for(SearchResult<String> sr:matches){
			if(sr.getBegin()>lastMatchEnd){
				result.append(source.substring(lastMatchEnd, sr.getBegin()));
			}
			result.append(replaceTo);
			lastMatchEnd=sr.getEnd();
		}
		if(source.length()>lastMatchEnd){
			result.append(source.substring(lastMatchEnd, source.length()));
		}
		return result.toString();
	}
//	public static void main(String[] main){
//		SensitiveWords sw=SensitiveWords.getInstance();
//		String source1="新射雕英雄传";
//		System.out.println(sw.match(source1));
//		System.out.println(sw.replace(source1, "**"));
//		String source="台独分子捏造天安门事件来污蔑我党。";
//		System.out.println(sw.match(source));
//		System.out.println(sw.replace(source, "**"));
//	}
}
