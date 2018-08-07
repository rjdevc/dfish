package com.rongji.dfish.misc.chinese;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
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
		String[] pinyins;
		LinkedHashMap<String,LinkedHashMap<String,Integer>> words;
		public String[] getPinyins(){
			return pinyins;
		}
		public LinkedHashMap<String,LinkedHashMap<String,Integer>> getWords(){
			return words;
		}
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
		skip(0x1540);//细胞词库从0x1540开始
		int pinyingCount=readInt4();
		ScelFormat ret=new ScelFormat();
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
