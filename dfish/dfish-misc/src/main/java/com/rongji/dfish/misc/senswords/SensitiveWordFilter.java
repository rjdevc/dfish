package com.rongji.dfish.misc.senswords;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import com.rongji.dfish.base.TrieTree;
import com.rongji.dfish.base.TrieTree.SearchResult;
import com.rongji.dfish.base.util.CharUtil;
//import com.rongji.dfish.misc.senswords.andy.WordFilter;

public class SensitiveWordFilter {
	public static void main(String[] args) {
//		System.out.println(SensitiveTrieTree.isStopChar('，'));
		SensitiveWordFilter.getInstance();
		String s="java你是逗比吗？fuck，FuCk！ｆｕｃｋ全角半角，口，交换，f!!!u&c ###k 停顿词ff fuuuucccckkk 重复词，法@#轮！@#功over16口交换机";
//		String s="java,路口交通，8口交换机";
		System.out.println("原语句："+s);
		System.out.println("长度："+s.length());
		String re;
		long nano = System.nanoTime();
		boolean		b=SensitiveWordFilter.getInstance().match(s);
		re=SensitiveWordFilter.getInstance().replace(s);
		nano = (System.nanoTime()-nano);
		System.out.println("是否包含: " + b);
		System.out.println("解析时间 : " + nano + "ns");
		System.out.println("解析时间 : " + nano/1000000 + "ms");
		System.out.println(re);
		System.out.println(re.length()==s.length());
	}
	
	
	private static SensitiveWordFilter instance=new SensitiveWordFilter();
	/**
	 * 取得实例
	 * @return SensitiveWords
	 */
	public static SensitiveWordFilter getInstance(){
		return instance;
	}
	TrieTree<Boolean> core= new SensitiveTrieTree();
	private SensitiveWordFilter(){
		loadDic(); //读取铭感词和 白名单
	}
	private void loadDic() {
		try{
			InputStream is=getClass().getResourceAsStream("/com/rongji/dfish/misc/senswords/main_dic.txt");
			BufferedReader bis=new BufferedReader(new InputStreamReader(is,"UTF-8"));
			String line="";
			while ((line=bis.readLine())!=null){
				if("口交".equals(line)){
					System.out.println("");
				}
				core.put(line, true);
			}
			if(bis!=null){
				bis.close();
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
		// 白名单用fasle;
		try{
			InputStream is=getClass().getResourceAsStream("/com/rongji/dfish/misc/senswords/white_words.txt");
			BufferedReader bis=new BufferedReader(new InputStreamReader(is,"UTF-8"));
			String line="";
			while ((line=bis.readLine())!=null){
				core.put(line, false);
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
		List<SearchResult<Boolean>> matches=  core.search(source);
		return matches!=null&&matches.size()>0;
	}
	
	/**
	 * 将句子中的所有敏感词替换成指定词
	 * @param source String
	 * @param replaceTo String 默认为*
	 * @return String
	 */
	public String replace (String source){
		if(source==null)return null;

		StringBuilder result=new StringBuilder();
		List<SearchResult<Boolean>> matches=  core.search(source);
		int lastMatchEnd=0;
		for(SearchResult<Boolean> sr:matches){
			if(sr.getBegin()>lastMatchEnd){
				result.append(source.substring(lastMatchEnd, sr.getBegin()));
			}
			if(sr.getValue()){
				for(int i=0;i<sr.getEnd()-sr.getBegin();i++){
					result.append('*');
				}
			}else{
				result.append(source.substring(sr.getBegin(), sr.getEnd()));
			}
			lastMatchEnd=sr.getEnd();
		}
		if(source.length()>lastMatchEnd){
			result.append(source.substring(lastMatchEnd, source.length()));
		}
		return result.toString();
	}
	
	static class SensitiveTrieTree extends TrieTree<Boolean>{
		public SensitiveTrieTree(){
			super(false);
		}
		static char toLowerCase(char c){
			return Character.toLowerCase(CharUtil.sbc2dbc(c));
		}
		
		private static HashSet<Character> stopChar;
		static{
			stopChar=new HashSet<Character>();
			char[] ASCII_STOP_WORDS=" !\"#$%&'()*+,-./:;<=>?@[\\]^_`{|}~".toCharArray();
			for(char c:ASCII_STOP_WORDS){
				stopChar.add(c);
				stopChar.add(CharUtil.dbc2sbcl(c));
			}
			char[] CHINESE_STOP_WORDS="，。￥“”‘’【】—…《》".toCharArray();
			for(char c:CHINESE_STOP_WORDS){
				stopChar.add(c);
			}
		}
		static boolean isStopChar(char c){
			//空格//符号 全角符号和特殊中文符号，当做停止符号
			return stopChar.contains(c);
		}
		
		@Override
		public Boolean put(String key, Boolean value) {
				Node<Boolean> current = root;
					char[] chs=key.toCharArray();
					char lastChar=(char)-1;
					for (int i=chs.length-1;i>=0;i-- ) {
						//char需要转化为半角小写 
						//char不能有连续两个是一样的。
						//如果有符号，则跳过一位继续查找
						if(isStopChar(chs[i])){
							continue;
						}
						char c=toLowerCase(chs[i]);
						if(lastChar==c){
							continue;
						}else{
							lastChar=c;
						}
						Node<Boolean> child = current.get(c);
						if (child != null) {
							current = child;
						} else {
							Node<Boolean> nextNode = new Node<Boolean>();
							current.put(c,nextNode);
							current = nextNode;
						}
					}
				// Set isEnd to indicate end of the word
				Boolean oldValue=current.getValue();
				current.setValue(value);
				current.setEnd(true);
				return oldValue;
		}

		@Override
		public List<SearchResult<Boolean>> search(String text) {
			List<SearchResult<Boolean>> searchResult = new ArrayList<SearchResult<Boolean>>();
			char[] content = text.toCharArray();
				for (int i = content.length; i >0; i--) {
					
					SearchResult<Boolean> r = matchReverse(content, 0, i,root,(char)-1);
					if (r!=null) {
						searchResult.add(r);
						i -= (r.getEnd() - r.getBegin() - 1);
					}
				}
				java.util.Collections.reverse(searchResult);
			return searchResult;
		}
		
		/**
		 * 尝试匹配内容，并且这里反向匹配(从右向左)最长的字符串。
		 * 比如有长沙县和沙县。如果当前节点是【沙】它并不会马上得到结果，而是尝试匹配更长的字符。
		 * 匹配的上，则返回更长的关键字 长沙县；匹配不上则返回当前结果 沙县。
		 * @param content 匹配的内容
		 * @param begin 开始(数字比较小的)匹配的位置-针对于 匹配的内容，一般不限制结束匹配的位置，一般为0；
		 * @param end 结束(数字比较大的)匹配的位置-针对于匹配的内容
		 * @return SearchResult 仅返回最长的一个结果。没有返回所有结果。
		 */
		public SearchResult<Boolean> matchReverse(char[] content, int begin, int end ,Node<Boolean> node, char lastChar) {
			char c = content[end-1];
			// char需要转化为半角小写 
			// char不能有连续两个是一样的。
			// 如果有符号，则跳过一位继续查找
			if(isStopChar(c)){
				return matchReverse(content,begin,end-1,node,lastChar);
			}
			c=toLowerCase(c);
			if(c==lastChar){
				return matchReverse(content,begin,end-1,node,lastChar);
			}else{
				lastChar=c;
			}
			if (node.getSize() ==0) {
				return null;
			}
			SearchResult<Boolean> result = null;
			Node<Boolean> p = node.get(c);
			if (p == null) {
				return null;
			} else {
				if (p.isEnd()) {
					result=new SearchResult<Boolean>(end-1,end,p.getValue());
				}
				if (p.getSize() >0) {
					if (begin + 1 < end) {
						SearchResult<Boolean> subMatch = matchReverse(content, begin , end-1,p,lastChar);
						if (subMatch!=null) {
							return new SearchResult<Boolean>(subMatch.getBegin(),end,subMatch.getValue()) ;
						}
					}
				}
			}
			return result;
		}
	}
}
