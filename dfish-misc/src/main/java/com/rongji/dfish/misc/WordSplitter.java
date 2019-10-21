package com.rongji.dfish.misc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



/**
 * WordSplitter 为输入建议用的切词器
 * 
 * 这里的切词是简单切词
 * 比如说 中共龙岩市委员会 切词将会包含 [中],[共],[龙],[岩],[市],[委],[员],[会] 
 * 如果是 拼音 比如是 longyanweiyuanhui 将会切成 [long],[yan],[wei],[yuan],[hui] 
 * 拼音将会按最大切词，如果是西安 拼音xian将不会切词，如果需要切词的话，text请用 单引号分开 如 xi'an 拼音最大切词以右边匹配优先
 * 比如sangang 会匹配成san'gang而不是sang'ang. 相同的如果需要改变切词结果text中请传递单引号。
 * 
 * 该切词器主要服务于当前基于数据库的快速搜索方案。
 * 该方案见
 * @author LinLW
 *
 */
public class WordSplitter {
	/**
	 * 所有的拼音，去除了a o e m 因为他们本来就是单字，不需要特殊处理。
	 */
	public static final String[] PIN_YIN={
		"ai","an","ang","ao",
		"ba","bai","ban","bang","bao","bei","ben","beng","bi","bian","biao","bie","bin","bing","bo","bu",
		"ca","cai","can","cang","cao","ce","cen","ceng","cha","chai","chan","chang","chao","che","chen","cheng","chi","chong","chou","chu","chua","chuai","chuan","chuang","chui","chun","chuo","ci","cong","cou","cu","cuan","cui","cun","cuo",
		"da","dai","dan","dang","dao","de","den","dei","deng","di","dia","dian","diao","die","ding","diu","dong","dou","du","duan","dui","dun","duo",
		"ei","en","eng","er",
		"fa","fan","fang","fei","fen","feng","fo","fou","fu",
		"ga","gai","gan","gang","gao","ge","gei","gen","geng","gong","gou","gu","gua","guai","guan","guang","gui","gun","guo",
		"ha","hai","han","hang","hao","he","hei","hen","heng","hong","hou","hu","hua","huai","huan","huang","hui","hun","huo",
		"ji","jia","jian","jiang","jiao","jie","jin","jing","jiong","jiu","ju","juan","jue","jun",
		"ka","kai","kan","kang","kao","ke","ken","keng","kong","kou","ku","kua","kuai","kuan","kuang","kui","kun","kuo",
		"la","lai","lan","lang","lao","le","lei","leng","li","lia","lian","liang","liao","lie","lin","ling","liu","long","lou","lu","lv","luan","lue","lve","lun","luo",
		"ma","mai","man","mang","mao","me","mei","men","meng","mi","mian","miao","mie","min","ming","miu","mo","mou","mu",
		"na","nai","nan","nang","nao","ne","nei","nen","neng","ni","nian","niang","niao","nie","nin","ning","niu","nong","nou","nu","nv","nuan","nve","nuo","nun",
		"ou",
		"pa","pai","pan","pang","pao","pei","pen","peng","pi","pian","piao","pie","pin","ping","po","pou","pu",
		"qi","qia","qian","qiang","qiao","qie","qin","qing","qiong","qiu","qu","quan","que","qun",
		"ran","rang","rao","re","ren","reng","ri","rong","rou","ru","ruan","rui","run","ruo",
		"sa","sai","san","sang","sao","se","sen","seng","sha","shai","shan","shang","shao","she","shei","shen","sheng","shi","shou","shu","shua","shuai","shuan","shuang","shui","shun","shuo","si","song","sou","su","suan","sui","sun","suo",
		"ta","tai","tan","tang","tao","te","teng","ti","tian","tiao","tie","ting","tong","tou","tu","tuan","tui","tun","tuo",
		"wa","wai","wan","wang","wei","wen","weng","wo","wu",
		"xi","xia","xian","xiang","xiao","xie","xin","xing","xiong","xiu","xu","xuan","xue","xun",
		"ya","yan","yang","yao","ye","yi","yin","ying","yo","yong","you","yu","yuan","yue","yun",
		"za","zai","zan","zang","zao","ze","zei","zen","zeng","zha","zhai","zhan","zhang","zhao","zhe","zhei","zhen","zheng","zhi","zhong","zhou","zhu","zhua","zhuai","zhuan","zhuang","zhui","zhun","zhuo","zi","zong","zou","zu","zuan","zui","zun","zuo"
	};
	/**
	 * 用户分隔的字符 单引号
	 */
	public static final char SPLIT_CHAR='\'';
	private static WordSplitter instance=new WordSplitter();
	private WordSplitter(){
		init();
	}
	/**
	 * 因为默认装载了词库，所以只需要一个实例就够了，这里使用单例模式
	 * @return
	 */
	public static WordSplitter getInstance(){
		return instance;
	}

	private TrieNodeReverse root=new TrieNodeReverse();
	private void init() {
		//装载matcher
		for(String pinyin:PIN_YIN){
			root.setKey(pinyin);
		}
	}
	
	/**
	 * 这里的切词是简单切词
	 * 比如说 中共龙岩市委员会 切词将会包含 [中],[共],[龙],[岩],[市],[委],[员],[会] 
	 * 如果是 拼音 比如是 longyanweiyuanhui 将会切成 [long],[yan],[wei],[yuan],[hui] 
	 * 拼音将会按最大切词，如果是西安 拼音xian将不会切词，如果需要切词的话，text请用 单引号分开 如 xi'an 拼音最大切词以右边匹配优先
	 * 比如sangang 会匹配成san'gang而不是sang'ang. 相同的如果需要改变切词结果text中请传递单引号。
	 * @param text
	 * @return
	 */
	public List<String> splitWords(String text) {
		if(text==null||text.isEmpty()){
			return new ArrayList<String>(0);
		}
		List<String> result=new ArrayList<String>();
		char[] content=text.toCharArray();
		for(int i=content.length;i>0;i--){
			FindResult r=root.match(content,0,i);
			if(r.state==FindResult.STATE_MATCH){
				i-=(r.end-r.begin-1);
				result.add(new String(content,i-1,r.end-r.begin));
			}else{
				char c=content[i-1];
				if(c!=SPLIT_CHAR){
					result.add(new String(new char[]{c}));
				}
			}
		}
		Collections.reverse(result);
		return result;
	}
	
	/**
	 * TrieNode 为字典树的一个节点。
	 * 字典树又称单词查找树，Trie树，是一种树形结构，是一种哈希树的变种。
	 * 典型应用是用于统计，排序和保存大量的字符串（但不仅限于字符串），所以经常被搜索引擎系统用于文本词频统计。
	 * 它的优点是：利用字符串的公共前缀来减少查询时间，最大限度地减少无谓的字符串比较，查询效率比哈希树高。
	 * 
	 * 这里，这个节点是倒序匹配的，sangang 这个拼音按右边的词最大化来切词。所以切词的时候是从右向左切，是倒序的。
	 * 所以这个类被加以Reverse标识
	 * @author LinLW
	 *
	 */
	static class TrieNodeReverse{
		/**
		 * 这个节点的词是否结束，如果结束，那有可能算是找到一个完整的词，返回MATCH
		 * 这里说的有可能的意思是，如果匹配到一个更长的词，这里返回的是更长的词，不一定是当前词。
		 * 比如说 gang 后面包含ang 找到ang(昂的拼音)并不一定马上返回，而是继续尝试匹配。
		 * 匹配不到更长的词的话，才返回短的，并且wordFinish 为true的部分。
		 */
		boolean wordFinish;
		/**
		 * 这个字典树的子节点
		 */
		HashMap<Character , TrieNodeReverse> children;
		
		/**
		 * 从工作文本的，begin到end的位置中去匹配字典中存在文本。
		 * @param content 工作文本
		 * @param begin 开始位置
		 * @param end 结束位置 主要因为这里是右匹配优先，倒序查询，这里的结束位置，从时间上反而是开始位置。
		 * @return
		 */
		public FindResult match(char[] content, int begin, int end) {
			char c=content[end-1];
			FindResult result=new FindResult(FindResult.STATE_UNMATCH,end,end);
			if(children==null){
				return result;
			}
			TrieNodeReverse p=children.get(c);
			if(p==null){
				return result;
			}else{
				if(p.wordFinish){
					result.state=FindResult.STATE_MATCH;
					result.begin=end-1;
					result.end=end;
					//找到一个匹配项，如果没有更长的将使用这个匹配项。
					//不能直接return;
				}
				if(p.children!=null){
					if(begin<end-1){
						FindResult subMatch=p.match(content, begin, end-1);
						if(subMatch.state==FindResult.STATE_MATCH){
							result.state=FindResult.STATE_MATCH;
							result.begin=subMatch.begin;
							result.end=end;
							return result;
						}
					}
				}
			}
			
			return result;
		}
		/**
		 * 尝试在这个字点树(根节点) 里面增加一个单词。单词将被分割成树
		 * @param keyWord
		 */
		public void setKey(String keyWord) {
			setKey(keyWord.toCharArray(),0,keyWord.length());
		}
		/**
		 * 在当前节点中设置字典树的内容。
		 * 一般当前节点的内容包含上级内容扣掉最后一个单字。
		 * @param charArray
		 * @param begin
		 * @param end
		 */
		void setKey(char[] charArray,int begin,int end) {
			if(end-begin<=0) {
                return;
            }
			if(children==null){
				children=new HashMap<Character , TrieNodeReverse>();
			}
			char c=charArray[end-1];
			TrieNodeReverse sub=children.get(c);
			if(sub==null){
				sub=new TrieNodeReverse();
				children.put(c, sub);
			}
			if( end-begin ==1){
				sub.wordFinish=true;
			}else{
				sub.setKey(charArray, begin,end-1);
			}
		}
		
		/**
		 * 为了调试方便，有的时候需要把该节点转成JSON。来检查是否有误。
		 */
		@Override
		public String toString(){
			StringBuilder sb=new StringBuilder();
			toString(sb);
			return sb.toString();
		}
		private void toString(StringBuilder sb) {
			if(children!=null){
				sb.append('{');
				boolean start=true;
				for(Map.Entry<Character , TrieNodeReverse> entry:children.entrySet()){
					if(start){
						start=false;
					}else{
						sb.append(',');
					}
					TrieNodeReverse k=entry.getValue();
					if(k.wordFinish){
						sb.append('"');
						sb.append(entry.getKey());
						sb.append('"');
					}else{
						sb.append('"');
						sb.append(entry.getKey());
						sb.append('"');
						sb.append(':');
						k.toString(sb);
					}
				}
				sb.append('}');
			}
		}
	}
	/**
	 * FindResult为搜索结果，
	 * 搜索结果一般表达为，找到某个词，它的位置是从begin到end
	 * 或则没找到。
	 * 在复杂搜索过程当中会有 找到改词前缀 状态
	 * @author LinLW
	 *
	 */
	static class FindResult{
		public FindResult(int state, int begin, int end) {
			this.state=state;
			this.begin=begin;
			this.end=end;
		}

		int begin;
		int end;
		int state;
		/**
		 * 找到
		 */
		static final int STATE_MATCH=1;
		/**
		 * 没找到
		 */
		static final int STATE_UNMATCH=0;
		/**
		 * 找到了前缀，需要进一步确认是否是匹配。
		 */
		static final int STATE_PREFIX=2;
	}
	
//	
//	public static void main(String[] args){
//		WordSplitter ws=new WordSplitter();
//		List<String> splited= ws.splitWords("中共龙岩市委员会zhonggonglongyanshiweiyuanhui123sangangxianxi'an");
//		System.out.println(splited);
//	}
}
