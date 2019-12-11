package com.rongji.dfish.misc.origin;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.rongji.dfish.base.text.TrieTree;

import com.rongji.dfish.misc.origin.entity.Detail;
import com.rongji.dfish.misc.origin.entity.Region;
import com.rongji.dfish.misc.origin.entity.Result;

/**
 * 行政区划代码匹配类
 * <p>根据输入的地区名称，返回相对应的行政区划代码。在确保输入准确关键词的前提下，没有输入格式的要求，可智能识别。</p>
 * <p>若通过getInstance()获得的实例,匹配范围为县级及县级以上行政区划,匹配目标为6位行政区划代码。</p>
 * <p>若通过createInstance()获得的实例,可通过行政区划代码前缀参数自定义匹配范围，匹配目标为12位行政区划代码。</p>
 *
 * @author DFish Team v0.8曲戈 v0.9林利炜 v1.1曾锦容 葛泽浩
 * @version 1.1.20180815
 */
public class OriginMatcher {
    private OriginMatcher(String codePrefix) {
        OriginMatcher.codePrefix = codePrefix;
        loadLib("/com/rongji/dfish/misc/origin/origin_lib.json");
    }

    /**
     * 静态的 行政区划代码匹配类对象
     */
    private static OriginMatcher instance;

    /**
     * 静态的 行政区划代码前缀，用于限定行政区划码查找范围.
     * <p>只在前缀匹配的范围内进行匹配，如值为"35"表示只匹配以"35"为开头的行政区划代码，匹配目标为12位行政区划代码。</p>
     * <p>若为null表示不采用行政区划代码前缀，匹配范围为县级及县级以上行政区划,匹配目标为6位行政区划代码。<p/>
     */
    private static String codePrefix;

    /**
     * 静态的 行政区划名称后缀集合
     */
    private static List<String> tailList;

    /**
     * 静态的 行政区划名称前缀集合
     */
    private static List<String> prefixList;

    /**
     * 字典树
     */
    protected TrieTree<List<String>> trieTree = new TrieTree<List<String>>();

    /**
     * @return 行政区划代码匹配类的对象，匹配范围为县级及县级以上行政区划,匹配目标为6位行政区划代码
     */
    public static OriginMatcher getInstance() {
        if (instance == null) {
            instance = new OriginMatcher(null);
        }
        return instance;
    }

    /**
     * @param codePrefix 行政区划代码前缀，用于自定义匹配范围，若大于12位则截取前12位作为前缀
     *                   <p>若不为null则作为行政区划前缀，匹配目标为以前缀开头的12位行政区划代码。<br/>
     *                   若为null则表示匹配范围为县级及县级以上行政区划,匹配目标为6位行政区划代码。<p/>
     * @return 行政区划代码匹配类的对象, 根据行政区划代码前缀自定义匹配范围，匹配目标为12位行政区划代码
     */
    public static OriginMatcher createInstance(String codePrefix) {
        if (codePrefix == null) {
            return getInstance();
        }
        codePrefix = codePrefix.length() > 12 ? codePrefix.substring(0, 12) : codePrefix;
        instance = new OriginMatcher(codePrefix);
        return instance;
    }

    /**
     * @return 行政区划代码匹配类的对象, 匹配范围为全国，匹配目标为12位行政区划代码
     */
    public static OriginMatcher createInstance() {
        return createInstance("");
    }

    /**
     * @param fileName 写有行政区划信息的json文件路径及文件名
     */
    private void loadLib(String fileName) {
        try {
            InputStream is = OriginMatcher.class.getResourceAsStream(fileName);
            BufferedReader bis = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = bis.readLine()) != null) {
                stringBuilder.append(line);
            }
            insertDataFromJson(stringBuilder.toString());
            bis.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 从json中读取行政区划数据，并根据需求在字典树中插入地区名称及对应的区划代码
     *
     * @param jsonStr 带有行政区划数据的json字符串
     */
    private void insertDataFromJson(String jsonStr) {
        Result result = JSONObject.parseObject(jsonStr, Result.class);
        tailList = result.getTails();
        prefixList = result.getPres();
        List<Region> topList = result.getTops();
        List<Detail> detailList = result.getDetails();

        if (codePrefix == null) {
            // 匹配范围为县级及县级以上行政区划,匹配目标为6位行政区划代码

            for (Region top : topList) {
                insertRegion(top, top.getCode());
            }

        } else if (codePrefix.length() > 6) {
            // 匹配范围仅包含县级以下行政区划,目标行政区划代码为12位

            String topCode = codePrefix.substring(0, 6);
            for (Detail detail : detailList) {
                if (detail.getTopCode().equals(topCode)) {
                    String subCodePrefix = codePrefix.substring(6);
                    for (Region sub : detail.getSubs()) {
                        String subCode = sub.getCode();
                        if (subCode.startsWith(subCodePrefix)) {
                            insertRegion(sub, topCode + subCode);
                        }
                    }
                }
            }
        } else {
            // 匹配范围同时包含上层行政区划和下层行政区划，匹配目标为12位行政区划代码

            for (Region top : topList) {
                String topCode = top.getCode();
                if (topCode.startsWith(codePrefix)) {
                    insertRegion(top, topCode + "000000");
                }
            }

            for (Detail detail : detailList) {
                String topCode = detail.getTopCode();
                if (topCode.startsWith(codePrefix)) {
                    for (Region sub : detail.getSubs()) {
                        insertRegion(sub, topCode + sub.getCode());
                    }
                }
            }
        }
    }

    /**
     * 在树中插入地区名称及对应的区划代码
     *
     * @param region 行政区划
     * @param code   行政区划代码
     */
    private void insertRegion(Region region, String code) {
        String name = region.getName();
        insert(name, code);

        Integer tailCode = region.getTail();
        Integer prefixCode = region.getPrefix();
        String fullName = "";
        if (prefixCode != null) {
            fullName += prefixList.get(prefixCode);
        }
        fullName += name;
        if ((tailCode) != null) {
            fullName += tailList.get(tailCode);
        }
        insert(fullName, code);
    }

	/**
	 * 在树中插入地区名称及对应的区划代码
	 * @param word 地区名称
	 * @param code 区划代码
	 */
	private void insert(String word, String code) {
		List<String> codes = trieTree.get(word);
		if (codes == null) {
			codes = new ArrayList<String>();
			trieTree.put(word, codes);
		}
		codes.add(code);
	}
	
	/**
	 * 该类存储计算过程中的中间结果，每个候选项有他们自己命中的code
	 * @author LinLW
	 *
	 */
	static class Candidate{
		String code;
		int level;
		int position;
		int confidence;
		Candidate(String code,int position,int confidence){
			this.code = code;
			this.position = position;
			this.confidence = confidence;
            this.level = switchLevel();
		}

        /**
         * 根据行政区划末尾的“0”字符串长度判断其所代表的行政区划等级，数值越大级别越高
         */
        private int switchLevel() {
            if (code.endsWith("0000000000")) {
                return 5;
            } else if (code.endsWith("00000000")) {
                return 4;
            } else if (code.endsWith("000000")) {
                return 3;
            } else if (code.endsWith("000")) {
                return 2;
            } else {
                return 1;
            }
        }


        public String getCode() {
			return code;
		}
		public int getLevel() {
			return level;
		}
		public int getPosition() {
			return position;
		}
		public int getConfidence() {
			return confidence;
		}
		public void setConfidence(int confidence) {
			this. confidence=confidence;
		}
	}	
	/**
	 * 将输入内容的关键词逐个对比，看是否存在匹配关系，若关键词A对应的区号集合中的a与关键词B对应的b匹配,
	 * 则使b在关键词C对应的区号集合中寻找匹配关系，如此寻找到最小的匹配关系为止，并返回。
	 * @param text 输入的地区
	 * @return 
	 */
	public MatchResult match(String text) {
//		System.out.print(text+":   ");
		List<Candidate> codeList = new ArrayList<Candidate>();
		List<TrieTree.SearchResult<List<String>>> result = trieTree.search(text);
		if (result.size() == 0) {
			return new MatchResult(null,MatchResult.CONFIDENCE_UNMATCH);
		}else {
//			int total = result.size();
			 //存放与某一行政码匹配的全部行政码，用于求连续匹配次数
			//福建省            福州市           鼓楼区		
			//350000  350100     320106、320302、350102、410204
			
			//430000    430100、430121     430104
			//湖南                                长沙                                     岳麓
			
			//江苏鼓楼
			//320000      320106、320302、350102、410204
			for (int i=0; i < result.size(); i++) {
				for (int j = 0; j < result.get(i).getValue().size(); j++){
					String m = result.get(i).getValue().get(j);					
					Candidate code = new Candidate(m,i,0);				
					int matchingTimes = 0;//匹配次数
					int comboMatchingTimes = 0;//连续匹配次数
					int confidence = 0;//信息熵
					//存放匹到的区号，以便计算连续匹配次数
					ArrayList<String> matchingCodeList = new ArrayList<String>(); //存放匹到的区号
					for (int k=0; k < result.size(); k++) {
						for (int l = 0; l < result.get(k).getValue().size(); l++){
							if(code.position==k){
								//补上本身
								matchingTimes++;
								matchingCodeList.add(code.getCode());
								break;
							}
							String n = result.get(k).getValue().get(l);
							String n1 = trimZero(n);
							//匹配成功需要和自己order不相同，之后需要把本身补上    
							if (code.getCode().startsWith(n1)) {
								matchingTimes ++;
								matchingCodeList.add(n);
								break;
							}	
						}
					}
					comboMatchingTimes = getComboMatchingtimes(matchingCodeList);
					confidence = getConfidence(matchingTimes,comboMatchingTimes,result.size());
					code.setConfidence(confidence);
					codeList.add(code);
				}
			}	
			
			//关键步骤，numberList按照confidence值从大到小排好，同confidence值根据level大小排好
			Collections.sort(codeList,new Comparator<Candidate>(){
				@Override
                public int compare(Candidate arg0, Candidate arg1){
					if(arg0.getConfidence()==arg1.getConfidence()){
						return arg1.getLevel()-arg0.getLevel();
					}
					return arg1.getConfidence()-arg0.getConfidence();
				}
			});
			
			//只对应出一个区号、只有一个最大值、输入了多个相同关键词情况
			if(codeList.size()==1||codeList.get(0).confidence>codeList.get(1).confidence
					||codeList.get(0).getCode()==codeList.get(1).getCode()||
					belong(codeList.get(0),codeList.get(1))){
				return new MatchResult(codeList.get(0).code,codeList.get(0).confidence); 
			}else{
				codeList.get(0).setConfidence(0);
				return new MatchResult(codeList.get(0).code,codeList.get(0).confidence); 
			}	
		}		
	}
	
	/*
	 * 匹配系数A=匹配次数*1.0+连续匹配次数*0.5;
	 * 最大可能连续匹配数C = 总次数=1?0:(2*总次数-3)
	 * 总系数B=总次数*1.0+最大可能连续匹配数C*0.5;
	 * 最后得分=(100*(Math.log(1+A)/Math.log(1+B))+0.5)取整
	 */
	private int getConfidence (int matchingtimes,int combomatchingtimes,int total){
		double A = matchingtimes*1.0+combomatchingtimes*0.5;
		double C = (total==1?0:(2*total-3));
		double B = total*1.0+C*0.5;
		double result = (100*(Math.log(1+A)/Math.log(1+B)));
		return (int)result;
	}
	

	private int getComboMatchingtimes(ArrayList<String> matchingCodeList ){
		int count = 0;
		for(int i=0;i<matchingCodeList.size();i++){
			for(int j=0;j<i;j++){
				String s = trimZero(matchingCodeList.get(j));
				if(matchingCodeList.get(i).startsWith(s)){
					count++;
				}
			}
		}
//		System.out.println(matchingCodeList+"combo match="+count);
		return count;
	}
	
	//判断是否属于
	private boolean belong(Candidate code1,Candidate code2){
		String str = trimZero(code2.getCode());
		return code1.getCode().startsWith(str);
	}

    /**
     * 精简行政区划代码字符串
     * <p>去掉行政区划代码末尾代表下属级别的“0”字符串，只留下代表当前级别及以上信息的行政区划代码片段。<p/>
     *
     * @param code 需要进行精简处理的行政区划代码字符串
     * @return 经过精简处理后的最简行政区划代码字符串
     */
    private String trimZero(String code) {
        while (code.length() >= 9 && code.endsWith("000")) {
            code = code.substring(0, code.length() - 3);
        }

        while (code.length() <= 6 && code.endsWith("00")) {
            code = code.substring(0, code.length() - 2);
        }
        return code;
    }

	/**
	 * 匹配结果类，便于对结果进行分析
	 */
	public static class MatchResult {
		public static final int CONFIDENCE_NONE = 0;
		public static final int CONFIDENCE_FULL = 100;
		public static final int CONFIDENCE_UNMATCH = -1;
		private String code;
		private int confidence;	
		/**
		 * 构造函数
		 */
		public MatchResult() {
			
		}
		/**
		 * 构造函数
		 * @param code 匹配到的值
		 * @param confidence 匹配情况对应的数值
		 */
		public MatchResult(String code, int confidence) {
			this.code = code;
			this.confidence = confidence;
		}
		/**
		 * 获得匹配情况对应的数值
		 * @return 数值
		 */
		public String getCode() {
			return code;
		}
		/**
		 * 设置匹配情况
		 * @param code 数值
		 */
		public void Code(String code) {
			this.code = code;
		}
		/**
		 * 设置匹配可信度
		 * @return 字符串
		 */
		public int getConfidence() {
			return confidence;
		}
		/**
		 * 设置匹配可信度
		 * @param confidence 字符串
		 */
		public void setConfidence(int confidence) {
			if(confidence>CONFIDENCE_FULL){
				confidence =CONFIDENCE_FULL;
			}
			this.confidence = confidence;
		}
	
	}


	
}


