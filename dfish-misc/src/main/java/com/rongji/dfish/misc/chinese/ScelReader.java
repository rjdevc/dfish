package com.rongji.dfish.misc.chinese;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * ScelReader 用于读取搜狗细胞词库
 * @author DFish Team
 *
 */
public class ScelReader {
	/**
	 * 读取的结果
	 * 一般pinyins 是所有的拼音，一般固定都是413个。
	 * words的格式类似<pre>
	 * {
	 * "wulongjiangdadaokou":{"乌龙江大道口":338},
	 * "wushan":{
	 *  "乌山":1025,
	 *  "吴山":5
	 * },
	 * "wushanlu":{"乌山路":377},
	 * "wushanlukou":{"乌山路口":514},
	 * "wushanqiaoxi":{"乌山桥西":1488}
	 * }
	 * </pre>
	 * 数字疑似词频。
	 * @author DFish Team
	 */
	public static class ScelFormat{
		Date publishTime;
		String name;
		String catelog;
		String readme;
		String readme2;
		String[] pinyins;
		LinkedHashMap<String,LinkedHashMap<String,Integer>> words;

		/**
		 * 发布时间
		 * @return 发布时间
		 */
		public Date getPublishTime() {
			return publishTime;
		}

		/**
		 * 福州本地公交地名库
		 * @return 福州本地公交地名库
		 */
		public String getName() {
			return name;
		}

		/**
		 * 城市
		 * @return 城市
		 */
		public String getCatelog() {
			return catelog;
		}

		/**
		 * 备注
		 * @return 备注
		 */
		public String getReadme() {
			return readme;
		}

		/**
		 * 备注
		 * @return 备注
		 */
		public String getReadme2() {
			return readme2;
		}

		/**
		 * 所有的拼音.
		 * @return 所有的拼音
		 */
		public String[] getPinyins(){
			return pinyins;
		}
		public LinkedHashMap<String,LinkedHashMap<String,Integer>> getWords(){
			return words;
		}
		@Override
        public String toString(){
			StringBuilder sb=new StringBuilder();
			sb.append("{\r\n");
			int wordsSize=words.size();
			int curWords=0;
			for(Map.Entry<String, LinkedHashMap<String,Integer>> entry:words.entrySet()){
				sb.append('"').append(entry.getKey()).append("\":{");
				int same=entry.getValue().size();
				int curChinese=0;
				boolean multi=same>1;
				for(Map.Entry<String,Integer> item:entry.getValue().entrySet()){
					if(multi){
						sb.append("\r\n ");
					}
					sb.append('"').append(item.getKey()).append("\":").append(item.getValue());
					if(++curChinese<same){
						sb.append(',');
					}
				}
				if(multi){
					sb.append("\r\n");
				}
				if(++curWords==wordsSize){
					sb.append("}\r\n");
				}else{
					sb.append("},\r\n");
				}
			}
			sb.append('}');
			return sb.toString();
		}
	}
	
	
	private byte[] src;
	private int pointer=0;
	/**
	 * 读取文件
	 * @param file 文件
	 * @return ScelFormat
	 */
	public ScelFormat readFile(File file){
		try {
			FileInputStream fis=new FileInputStream(file);
			ByteArrayOutputStream baos=new ByteArrayOutputStream();
			byte[] buff=new byte[8192];
			int readed=-1;
			while((readed=fis.read(buff))>0){
				baos.write(buff, 0, readed);
			}
			fis.close();
			src=baos.toByteArray();
			pointer=0;
		} catch (IOException e) {
			e.printStackTrace();
		}
		int pinyinStart=readInt4();//一般固定是0x1540细胞词库从0x1540开始
		//这里面还有一些备注的内容
//		System.out.println("未知的数字1="+readInt4());
//		System.out.println("未知的数字2="+readInt4());
//		System.out.println("未知的数字3="+readInt4());
//		System.out.println("未知的数字4="+readInt4());
//		System.out.println("未知的数字5="+readInt4());
//		System.out.println("未知的数字6="+readInt4());
//		System.out.println("未知的文本8="+readUnicodeString());
		
		ScelFormat ret=new ScelFormat();
		pointer=0x11c;
		ret.publishTime=new java.util.Date(1000L*readInt4());
//		System.out.println("拼音词组数量="+readInt4());
//		System.out.println("汉字词组数量="+readInt4());
//		System.out.println("未知的数字12="+readInt4());
//		System.out.println("未知的数字13="+readInt4());
		pointer=0x130;
		ret.name=readUnicodeString();
		pointer=0x338;
		ret.catelog=readUnicodeString();
		pointer=0x540;
		ret.readme=readUnicodeString();
		pointer=0xd40;
		ret.readme2=readUnicodeString();
		
		pointer=pinyinStart;
		int pinyingCount=readInt4();
		
		ret.pinyins=new String[pinyingCount];
		ret.words=new LinkedHashMap<String, LinkedHashMap<String,Integer>>();
		for(int i=0;i<pinyingCount;i++){
			ret.pinyins[i]=readPinyin(i);
		}
		while(pointer<src.length){
			readWords(ret);
		}
		return ret;
	}


	private void readWords(ScelFormat ret) {
		int same=readInt2();
		int pinyinLen=readInt2()/2;
		StringBuilder sb=new StringBuilder();
		for(int i=0;i<pinyinLen;i++){
			int pinyinIndex=readInt2();
			sb.append(ret.pinyins[pinyinIndex]);
		}
		String wordPinyin=sb.toString();
		LinkedHashMap<String,Integer> wordRate=new LinkedHashMap<String,Integer>();
		ret.words.put(wordPinyin, wordRate);
		for(int i=0;i<same;i++){
			int wordLen=readInt2();
			String word=readUnicodeString(wordLen);
			int extLen=readInt2();
			int rate=readInt2();
			skip(extLen-2);//一般总共10位，前面两位看起来是数字，疑似词频。后8位都是0。不知道干啥用的
			wordRate.put(word, rate);
		}
		
	}


	private String readPinyin(int index) {
		int seq=readInt2();//理论上应该等于index
		int length=readInt2();
		String pinyin=readUnicodeString(length);
		return pinyin;
	}


	private String readUnicodeString(int length) {
		int charCnt=length/2;
		char[] chars=new char[charCnt];
		for(int i=0;i<charCnt;i++){
			chars[i]=(char)readInt2();
		}
		return new String(chars);
	}
	private String readUnicodeString() {
		StringBuilder sb=new StringBuilder();
		while(true){
			char c=(char)readInt2();
			if(c==0){break;}
			sb.append(c);
		}
		return sb.toString();
	}



	private int readInt4() {
		byte b0=src[pointer];
		byte b1=src[pointer+1];
		byte b2=src[pointer+2];
		byte b3=src[pointer+3];
		int result= (b0&0xff)+((b1&0xff)<<8)+((b2&0xff)<<16)+((b3&0xff)<<24);
		pointer+=4;
		return result;
	}
	private int readInt2() {
		byte b0=src[pointer];
		byte b1=src[pointer+1];
		int result= (b0&0xff)+((b1&0xff)<<8);
		pointer+=2;
		return result;
	}


	private void skip(int i) {
		pointer+=i;
	}
}
