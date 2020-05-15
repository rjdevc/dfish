package com.rongji.dfish.base.text;

import java.util.ArrayList;
import java.util.List;


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
public class PinyinWordSplitter implements WordSplitter{
	private SimpleWordSplitter sws;
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
	private static PinyinWordSplitter instance=new PinyinWordSplitter();
	private PinyinWordSplitter(){
		sws=SimpleWordSplitter.getInstance();
		init();
	}
	/**
	 * 因为默认装载了词库，所以只需要一个实例就够了，这里使用单例模式
	 * @return PinyinWordSplitter
	 */
	public static PinyinWordSplitter getInstance(){
		return instance;
	}

	private TrieTree<Void> core= new TrieTree<>();
	private void init() {
		//装载matcher
		for(String pinyin:PIN_YIN){
			core.put(pinyin,null);
		}
	}
	
	/**
	 * 这里的切词是简单切词
	 * 如果是 拼音 比如是 longyanweiyuanhui 将会切成 [long],[yan],[wei],[yuan],[hui]
	 * 拼音将会按最大切词，如果是西安 拼音xian将不会切词，如果需要切词的话，text请用 单引号分开 如 xi'an 拼音最大切词以右边匹配优先
	 * 比如sangang 会匹配成san'gang而不是sang'ang. 相同的如果需要改变切词结果text中请传递单引号。
	 * @param text 文本
	 * @return 切词集合
	 */
	@Override
	public List<String> split(String text) {
		if(text==null||text.isEmpty()){
			return new ArrayList<>(0);
		}
		List<String> result= new ArrayList<>();
		List<TrieTree.SearchResult<Void>> sr=core.search(text);
		if(sr==null||sr.isEmpty()){
			return result;
		}
		int lastEnd=0;
		for(TrieTree.SearchResult<Void> item:sr){
			if(item.getBegin()>lastEnd){
				String noMatch=text.substring(lastEnd,item.getBegin());
				result.addAll(sws.split(noMatch));
			}
			result.add(text.substring(item.getBegin(),item.getEnd()));
			lastEnd=item.getEnd();
		}
		if(lastEnd<text.length()){
			String noMatch=text.substring(lastEnd);
			result.addAll(sws.split(noMatch));
		}
		return result;
	}
	

}
