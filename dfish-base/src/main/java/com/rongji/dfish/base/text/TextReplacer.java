package com.rongji.dfish.base.text;

import java.util.List;

import com.rongji.dfish.base.text.TrieTree;

/**
 * <p>TextReplacer 为字符串替换工具，用于高效地替换多个关键词。</p>
 * <p>常可用于敏感词替换，为关键词增加超链接等地方</p>
 * <p>ps:这只是个替换工具，如果要增加超链接，需要自行增加完整文本 比如</p>
 * <table>
 * <tr>
 * <td><b>关键词</b></td>
 * <td><b>替换为</b></td>
 * </tr>
 * <tr>
 * <td>福州市</td>
 * <td>&lt;a href="http://www.fuzhou.gov.cn"&gt;福州市&lt;/a&gt;</td>
 * </tr>
 * <tr>
 * <td>莆田市</td>
 * <td>&lt;a href="http://www.putian.gov.cn"&gt;莆田市&lt;/a&gt;</td>
 * </tr>
 * </table>
 * <p>本工具只提供替换功能，如果已经增加过超链接的文本，再次替换的话，可能会出现问题。又比如说HTML中可能有一些关键字并不适合替换如 &lt;img
 * alt="福州市夜景"/ src="xxx"&gt;</p>
 * <p>V2.0 使用TrieTree重构，性能翻倍</p>
 * <p>如果两个关键字有包含关系 如 <b>福州</b> 与 <b>福州市</b> 那么优先替换长的关键字。</p>
 * 
 * @author DFish team
 * @version 2.0 
 *
 */
public class TextReplacer {
	TrieTree<String> core=new TrieTree<String>(true);
	/**
	 * 增加关键词和对应替换内容
	 * @param keyWord 关键词
	 * @param replaceTo 替换内容
	 */
	public void addKeyWord(String keyWord,String replaceTo){
		core.put(keyWord, replaceTo);
	}
	/**
	 * 对内容进行替换
	 * @param src 内容
	 * @return 替换后内容
	 */
	public String replaceText(String src){
		List<TrieTree.SearchResult<String>> sr=core.search(src);
		if(sr==null||sr.isEmpty()){
			return src;
		}
		char[] chars=src.toCharArray();
		StringBuilder sb=new StringBuilder();
		int lastEnd=0;
		for(TrieTree.SearchResult<String> item:sr){
			if(item.getBegin()>lastEnd){
				sb.append(chars,lastEnd,item.getBegin()-lastEnd);
			}
			sb.append(item.getValue());
			lastEnd=item.getEnd();
		}
		if(lastEnd<chars.length){
			sb.append(chars,lastEnd,chars.length-lastEnd);
		}
		
		return sb.toString();
	}

}
