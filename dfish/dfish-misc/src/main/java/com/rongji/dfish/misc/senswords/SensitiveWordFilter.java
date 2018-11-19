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
		String s="avjava你是逗比吗？fuck，avFuCk！ｆｕｃｋ全角半角，口，交换，f!!!u&c ###k 停顿词ff fuuuucccckkk 重复词，法@#轮！@#功over16口交换机";
//		String s="java,路口交通，8口交换机";
		System.out.println("原语句："+s);
		System.out.println("长度："+s.length());
		String re=null;
		long nano = System.nanoTime();
		boolean		b=SensitiveWordFilter.getInstance().match(s);
		boolean w=true;
		while(w){
			re=SensitiveWordFilter.getInstance().replace(s);
		}
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
		char[] chs = source.toCharArray();
		int length = chs.length;
		char currc; // 当前检查的字符
		char cpcurrc=0; // 当前检查字符的备份
		int filled=length;
		TrieTree.Node<Boolean> node=core.getRoot();
		TrieTree.Node<Boolean> find=null;
		int wordbegin=length;
		int wordend=length;
		for (int i = length-1; i >=0; i--) {
			currc=chs[i];
			if(SensitiveTrieTree.stopChar.contains(currc)){
				continue;
			}
			currc = toLowerCase(currc);
			if(currc==cpcurrc){
				continue;
			}else{
				cpcurrc=currc;
			}
			node=core.getRoot().get(currc);
			
			if(node==null){
				continue;
			}
			find=null;
			for(int k=i-1;k>=0;k--){
				currc=chs[k];
				if(SensitiveTrieTree.stopChar.contains(currc)){
					continue;
				}
				currc = toLowerCase(currc);
				if(currc==cpcurrc){
					continue;
				}else{
					cpcurrc=currc;
				}
				node=node.get(currc);
				if(node==null){
					if(find!=null){
						if(find.getValue()){
							//把未压入的内容压入到字符串
							fillWhite(chs,wordend+1,filled,result);
							//把找到的结果压入成*
							fillBlack(wordbegin,wordend+1,result);
							filled=wordbegin;
							find=null;
						}
						i=wordbegin;
						cpcurrc=0;//重置否则退出
					}
					break;
				}else if(node.isEnd()){
					find=node;
					wordbegin=k;
					wordend=i;
				}
			}
		}
		if(find!=null){//如果最后一个是敏感词要补充处理
			if(find.getValue()){
				//把未压入的内容压入到字符串
				fillWhite(chs,wordend+1,filled,result);
				//把找到的结果压入成*
				fillBlack(wordbegin,wordend+1,result);
				filled=wordbegin;
			}
		}
		
		fillWhite(chs,0,filled,result);
		return result.reverse().toString();
	}
	private void fillBlack(int begin, int end, StringBuilder result) {

		for(int i=end-1;i>=begin;i--){
			result.append('*');
		}
	}
	private void fillWhite(char[] chs, int begin, int end, StringBuilder result) {
		
		for(int i=end-1;i>=begin;i--){
			result.append(chs[i]);
		}
	}
	static char toLowerCase(char c){
		return Character.toLowerCase(CharUtil.sbc2dbc(c));
	}
	
	
	
	static class SensitiveTrieTree extends TrieTree<Boolean>{
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
		public SensitiveTrieTree(){
			super(true);
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
						if(stopChar.contains(chs[i])){
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
	}
}