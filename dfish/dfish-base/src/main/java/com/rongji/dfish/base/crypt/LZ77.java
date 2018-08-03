package com.rongji.dfish.base.crypt;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;


/**
 * LZ77算法是采用字典做数据压缩的算法，由以色列的两位大神Abraham Lempel与Jacob Ziv在1977年发表的论文《A Universal Algorithm for Sequential Data Compression》中提出。
 * 基于统计的数据压缩编码，比如Huffman编码，需要得到先验知识——信源的字符频率，然后进行压缩。
 * 但是在大多数情况下，这种先验知识是很难预先获得。因此，设计一种更为通用的数据压缩编码显得尤为重要。
 * LZ77数据压缩算法应运而生，其核心思想：利用数据的重复结构信息来进行数据压缩。
 * @author DFish team
 * @deprecated 使用GZIP 的压缩率大大高于本类。
 */
public class LZ77 {
	final static int DICT_SIZE=0x1000;
	final static int END = 0xFFF;
	final static int RESET = 0xFFE;
	/**
	 * 字典
	 * 在压缩和解压过程中需要记录下来重复的信息。形成一个字典
	 * 这个字典最大长度为4096(0x1000)最前面256位固定是byte 0-255 最后两位分别是RESET 和 END
	 * 因为这个字典的数据长度是0x1000所以两个数可以写入到3个字节内。
	 * 字典END表示编解码结束，RESET表示字典满了，需要重置并进行下一个分组的压缩。
	 */
	public static class Dictionary{
		ArrayList<ByteArray> bytes=new ArrayList<ByteArray>(DICT_SIZE);
		HashMap<ByteArray,Integer> map=new HashMap<ByteArray,Integer>();
		/**
		 * 构造函数
		 */
		public Dictionary() {
			reset();
		}
		/**
		 * 指定的byte数组在字典中的位置
		 * @param ba ByteArray
		 * @return int
		 */
		public int indexOf(ByteArray ba) {
			Integer i=map.get(ba);
			return i==null?-1:i;
		}
		/**
		 * 增加一个byte数组
		 * @param ba
		 */
		public void add(ByteArray ba) {
			int size=size();
			map.put(ba, size);
			bytes.add(ba);
		}
		/**
		 * 大小，字典大小是4096 但最前的256位和最后两位已经被固定。
		 * @return int
		 */
		public int size() {
			return bytes.size();
		}
		/**
		 * 取得某个位置的内容，多在解压的时候使用
		 * @param i 位置
		 * @return ByteArray
		 */
		public ByteArray get(int i) {
			return bytes.get(i);
		}
		/**
		 * 重置字典
		 */
		public void reset(){
			//构建并压入0-255 的单byte的所有情况
			bytes.clear();
			map.clear();
			for(int i=0;i<256;i++){
				add(new ByteArray( new byte[]{(byte)i}));
			}
		}
	}
	/**
	 * 数组的封装了，用于方便调用，并保障hash性能。
	 *
	 */
	public static class ByteArray{
		private byte[] ba;
		private int length;
		private int hashcode=0;
		/**
		 * 构造函数。
		 * 已知内容情况下调用，一般内容不会变化才使用这个方式。
		 * @param ba
		 */
		public ByteArray(byte[] ba){
			this.ba=ba;
			length=ba.length;
		}
		/**
		 * 默认构造函数
		 * 和ArrayList一样，支持增长。
		 */
		public ByteArray(){
			this.ba=new byte[10];
			length=0;
		}
		/**
		 * 拷贝构造函数
		 * @param other ByteArray
		 */
		public ByteArray(ByteArray other) {
			this.ba=new byte[other.ba.length];
			System.arraycopy(other.ba, 0, ba, 0, ba.length);
			this.length=other.length;
			this.hashcode=other.hashcode;
		}
		/**
		 * 取得内核值
		 * @return byte[] 
		 */
		public byte[] getBytes(){
			if(ba.length==length){
				return ba;
			}else{
				byte[] result =new byte[length];
				System.arraycopy(ba, 0, result, 0, length);
				return result;
			}
		}
		/**
		 * 增加一个字节
		 * @param b 字节
		 */
		public void addByte(byte b){
			if(length>=ba.length){
				byte[] newba=new byte[ba.length<<1];
				System.arraycopy(ba, 0, newba, 0, ba.length);
				ba=newba;
			}
			ba[length]=b;
			length++;
			hashcode=0;//重算hashcode
		}
		public String toString(){
			// 这个只能用英文字符串调试，中文的话，多为乱码。
			return new String(ba,0,length);
		}

		@Override
		public boolean equals(Object o){
			if(o==null)return false;
			if(o==this)return true;
			ByteArray other=(ByteArray)o;
			if(other.length!=length){
				return false;
			}
			for(int i=0;i<length;i++){
				if(ba[i]!=other.ba[i]){
					return false;
				}
			}
			return true;
		}

		@Override
		public int hashCode(){
			if(hashcode!=0){
				return hashcode;
			}
			int hc =0;
			for(int i=0;i<length;i++){
				if((i&3)==0){
					hc^=ba[i]&0xff;
				}else if((i&3)==1){
					hc^=(ba[i]&0xff)<<8;
				}else if((i&3)==2){
					hc^=(ba[i]&0xff)<<16;
				}else{
					hc^=(ba[i]&0xff)<<24;
				}
			}
			hashcode=hc;
			return hc;
		}
	}
	private LZ77(){}
	/**
	 * 压缩
	 * @param src byte数组
	 * @return byte数组
	 */
	public static byte[] encode(byte[] src){
		ByteArrayInputStream bais=new ByteArrayInputStream(src);
		ByteArrayOutputStream baos=new ByteArrayOutputStream();
		encode(bais,baos);
		return baos.toByteArray();
	}
	/**
	 * 压缩
	 * @param in InputStream 考虑到性能，如果是从磁盘或网络读取内容的时候请使用BufferedInputStream
	 * @param out OutputStream 考虑到性能，如果是写入磁盘或网络的时候请使用BufferedOutputStream
	 */
	public static void encode(InputStream in, OutputStream out){
		byte cur=0;
		Dictionary dic=new Dictionary();
		ByteArray prefix=new ByteArray(),block=new ByteArray();
		int[] indexbuff=new int[2];
		try {
			int i=0;//因为用4096的区块相当于2个位置写3个字节，所以这里控制第一个位置还是第二个位置。i只有0和1
			int temp;
			while((temp=in.read())!=-1){
				cur=(byte)temp;
				block=new ByteArray(prefix);
				block.addByte(cur);
				int pos=dic.indexOf(block);
				if(pos>=0){
					//仅仅是个前缀，尝试找更长字段
					prefix=block;
				}else{
					if(i==0){
						indexbuff[0]=dic.indexOf(prefix);
						i=1;
					}else{
						indexbuff[1]=dic.indexOf(prefix);
						zipOutput(out, indexbuff);//把4个字节压缩成3个字节。
						i=0;
					}
					if(dic.size()>=RESET){
						if(i==0){
							indexbuff[0]=RESET;
							i=1;
						}else{
							indexbuff[1]=RESET;
							zipOutput(out, indexbuff);//把4个字节压缩成3个字节。
							i=0;
						}
						dic.reset();
					}else{
						dic.add(block);
					}
					prefix = new ByteArray();
					prefix.addByte(cur);
				}
			}
			if (i ==1) {// this is the case that the
				indexbuff[1] = dic.indexOf(prefix);
				zipOutput(out, indexbuff);
			}else{
				indexbuff[0] = dic.indexOf(prefix);
				indexbuff[1] = END;// put a special index number
				zipOutput(out, indexbuff);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			if(in!=null){
				try{
					in.close();
				}catch(Exception e){
					e.printStackTrace();
				}
			}
			if(out!=null){
				try{
					out.close();
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		}
	}
	/**
	 * 把两个4096以内数字压缩，并写入3个字节内
	 * @param out OutputStream
	 * @param buff int[]
	 */
	private static void zipOutput(OutputStream out, int[] buff) {
		try {
			byte data[] = new byte[3];
			data[0] =(byte)((buff[0]&0xFFF) >> 4);
			data[1] =(byte)((buff[0]&0xFFF) << 4|(buff[1]&0xFFF) >> 8);
			data[2] =(byte)(buff[1]&0xFFF);
			out.write(data, 0, 3);
		} catch (IOException e) {
			System.err.println(e);
			return;
		}
	}
	/**
	 * 在3个字节读取出两个4096以内数字
	 * @param in InputStream
	 * @param codebuf int[]
	 * @param dic Dictionary
	 * @return 是否还有内容
	 */
	private static boolean zipInput(InputStream in, int[] codebuf,Dictionary dic) {
		byte[] buf = new byte[3];
		try {

			if (in.read(buf, 0, 3) != 3) {
				return false;
			}
			codebuf[0] =(buf[0]&0xFF)<<4|(buf[1]&0xF0)>>4;
			codebuf[1] =(buf[1]&0xF)<<8|(buf[2]&0xFF);
			if (codebuf[0] < -1 || codebuf[1] < -1) {
				System.err.println("erroring while getting the code :" + codebuf[0] + "\t" + codebuf[1]);
				System.err.println(dic);
				return false;
			}
		} catch (IOException e) {
			System.err.println(e);
			return false;
		}
		return true;
	}
	/**
	 * 解压内容
	 * @param src byte数组
	 * @return byte数组
	 */
	public static byte[] decode(byte[] src){
		ByteArrayInputStream bais=new ByteArrayInputStream(src);
		ByteArrayOutputStream baos=new ByteArrayOutputStream();
		decode(bais,baos);
		return baos.toByteArray();
	}
	/**
	 * 解压内容
	 * @param in InputStream
	 * @param out OutputStream
	 */
	public static void decode(InputStream in, OutputStream out){
		Dictionary dic=new Dictionary();
		int precode = 0, curcode = 0;
		ByteArray prefix = null;
		int i = 0;
		int bufcode[] = new int[2];// 2 code read from the code file
		boolean more = true;// indicate the end of the file or some error while

		try {

			more = zipInput(in, bufcode,dic);// first input 2 code
			if (more) {
				curcode = bufcode[0];
				out.write(dic.get(curcode).getBytes());
			} 
			while (more) {
				precode = curcode;
				if (i == 0) {
					curcode = bufcode[1];
					i = 1;
				} else {
					more = zipInput(in, bufcode,dic);
					curcode = bufcode[0];
					if (bufcode[1] == END) {
						out.write( dic.get(bufcode[0]).getBytes());
						break;
					}
					i = 0;
					if(!more){break;}
				}
				if (curcode == RESET) {
					dic.reset();
				}else if (curcode == END) {
					break;
				}else{
					out.write(dic.get(curcode).getBytes());
					if(precode==RESET){
						prefix = new ByteArray();
					}else{
						prefix = new ByteArray(dic.get(precode));
						byte[] ba=dic.get(curcode).getBytes();
						prefix.addByte(ba[0]);
						dic.add(prefix);
					}
				}
			}// while

			in.close();
			out.close();
		} catch (IOException e) {
			System.err.println(e);
			return;
		}
	}

}
