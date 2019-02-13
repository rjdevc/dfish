package com.rongji.dfish.base.util;

import java.util.Arrays;

/**
 * SequenceUtil 用于做有序的序列化文本
 *
 */
public abstract class SequenceUtil {
	private SequenceUtil(){}
	/**
	 * 纯数字， 09的下一个 是10  99或999的下一个溢出。
	 */
	public static final SequenceUtil DECIMAL=new DecimalSeq();
	/**
	 * 16进制数，0F的下一个是10， FF或FFF的下一个溢出
	 */
	public static final SequenceUtil HEX=new HexSeq();
	/**
	 * 32进制数，RFC2938规范是JAVAInteger默认转化规划，或者认为是extend HEX。
	 * 使用0-9 a-v这些字母
	 */
	public static final SequenceUtil BASE32_RFC2938=new B32R2938Seq();
	/**
	 * 32进制数，RFC4648规范
	 * 使用A-Z 2-7这些字母
	 */
	public static final SequenceUtil BASE32_RFC4648=new B32R4648Seq();
	/**
	 * 比较推荐的一个32进制数规范
	 * 使用0-9 A-Z 但是 oO被视作0，iIlL被视作1
	 * 不使用U字符，防止随机的文本可能会出现一些敏感词。
	 */
	public static final SequenceUtil BASE32_CROCKFORDS=new B32CrockfordsSeq();
	/**
	 * GEOhash中用32进制规范。注意，正常情况下，GeoHash值加1没有逻辑上的意义。
	 * 并不推荐使用。
	 */
	public static final SequenceUtil BASE32_GEOHASH=new B32GeohashSeq();
	/**
	 * 所有字母和数字
	 * 使用0-9 A-Z a-z ，使用62进制 只适合在sequence中使用，如果用于值转化性能比较低。
	 */
	public static final SequenceUtil ALPHABET_AND_NUMBER=new AnSeq();
	/**
	 * 使用标准base64 的64进制
	 * 使用A-Z a-z 0-9 以及+ / 字符。
	 * 如果考虑到主键的实用性，应该避免使用+ / 等字符。所以该方式，适用范围可能需要斟酌。
	 */
	public static final SequenceUtil BASE64=new B64Seq();

	
	
	
	protected int radix=10;

	protected char[] ALPHABET;
	protected int[] DECODE_TABLE;
	/**
	 * 将值增加一，逻辑上相当于plus(sequence,1)。
	 * 但一般该方法的性能比较高
	 * @param sequence 原始值
	 * @return 下一个值
	 * @throws ArithmeticException 如果数字越界 IllegalArgumentException 如果sequence里面包含当前进制不支持的字符
	 */
	public String plus(String sequence){
		if(sequence==null||sequence.equals("")){
			throw new IllegalArgumentException(sequence);
		}
		char[] cs=sequence.toCharArray();
		int pos=cs.length-1;
		while(plus(cs,pos--,1,sequence));
		return new String(cs);
	}
	/**
	 * 增加
	 * @param cs 字节串位置
	 * @param index 位置 0到cs.length-1
	 * @param step 不允许<0 不允许大等于 radix 
	 *  对于0没有性能优化，外部判断后，尽量不要是0
	 *  @param src 原始字符串，用于抛出异常时的详细信息
	 * @return 是否产生进位
	 */
	protected boolean plus(char[] cs,int index,int step,String src){
		if(index<0){
			throw new ArithmeticException(src);
		}
		char c=cs[index];
		if(c>=DECODE_TABLE.length){
			throw new IllegalArgumentException(src);
		}
		int v=DECODE_TABLE[c];
		if(v<0){
			throw new IllegalArgumentException(src);
		}
		v+=step;
		if(v>=radix){
			cs[index]=ALPHABET[v-radix];
			return true;
		}else{
			cs[index]=ALPHABET[v];
			return false;
		}
	}
	
	/**
	 * 将值增加一定的量。
	 *  一般为了集群环境下不会冲突，通常单机每次会申请一定批量的序列号，等到用完继续申请。
	 * 如果宕机，自会漏过一定的空号，并不会有重大影响，但非常明显的减少了序列号争夺的情况。
	 * 为了支持这种情况，故提供一次性将值提供一个数值的方法。
	 * @param sequence 原始值
	 * @param value 增加数量
	 * @return 下value个值。
	 * @throws ArithmeticException 如果数字越界 IllegalArgumentException 如果sequence里面包含当前进制不支持的字符
	 */
	public String plus(String sequence,long value){
		if(sequence==null||sequence.equals("")){
			throw new IllegalArgumentException(sequence);
		}
		if(value<=0){
			throw new IllegalArgumentException(String.valueOf(value));
		}
		char[] cs=sequence.toCharArray();
		int pos=cs.length-1;
		while(value>0){
			int mod =(int)(value % radix);
			value=value/radix;
			if(mod==0){
				pos--;
				continue;
			}
			if(plus(cs,pos--,mod,sequence)){
				value++;
			}
		}
		return new String(cs);
	}

	protected static class DecimalSeq extends SequenceUtil{
		public DecimalSeq(){
			ALPHABET=new char[]{
		            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9'
		    };
			radix=ALPHABET.length;
			DECODE_TABLE=new int[128];
			Arrays.fill(DECODE_TABLE, -1);
			int i=0;
			for(char c:ALPHABET){
				DECODE_TABLE[c]=i;
				i++;
			}
		}
	}

	
	protected static class HexSeq extends SequenceUtil{
		public HexSeq(){
			ALPHABET=new char[]{
					'0', '1', '2', '3', '4', '5', '6', '7',
		            '8', '9', 'A', 'B', 'C', 'D', 'E', 'F',
		    };
			radix=ALPHABET.length;
			DECODE_TABLE=new int[128];
			Arrays.fill(DECODE_TABLE, -1);
			int i=0;
			for(char c:ALPHABET){
				DECODE_TABLE[c]=i;
				if(c>='A'&&c<='Z'){
					DECODE_TABLE[c+32]=i;
				}
				i++;
			}
		}
	}
	protected static class B32R2938Seq extends SequenceUtil{
		public B32R2938Seq(){
			ALPHABET="0123456789abcdefghijklmnopqrstuv".toCharArray();
			radix=ALPHABET.length;
			DECODE_TABLE=new int[128];
			Arrays.fill(DECODE_TABLE, -1);
			int i=0;
			for(char c:ALPHABET){
				DECODE_TABLE[c]=i;
				if(c>='a'&&c<='z'){
					DECODE_TABLE[c-32]=i;
				}
				i++;
			}
		}
	}
	protected static class B32GeohashSeq extends SequenceUtil{
		public B32GeohashSeq(){
			ALPHABET="0123456789bcdefghjkmnpqrstuvwxyz".toCharArray();
			radix=ALPHABET.length;
			DECODE_TABLE=new int[128];
			Arrays.fill(DECODE_TABLE, -1);
			int i=0;
			for(char c:ALPHABET){
				DECODE_TABLE[c]=i;
				if(c>='a'&&c<='z'){
					DECODE_TABLE[c-32]=i;
				}
				i++;
			}
		}
	}
	protected static class B32CrockfordsSeq extends SequenceUtil{
		public B32CrockfordsSeq(){
			ALPHABET="0123456789ABCDEFGHJKMNPQRSTVWXYZ".toCharArray();
			radix=ALPHABET.length;
			DECODE_TABLE=new int[128];
			Arrays.fill(DECODE_TABLE, -1);
			int i=0;
			for(char c:ALPHABET){
				DECODE_TABLE[c]=i;
				if(c>='A'&&c<='Z'){
					DECODE_TABLE[c+32]=i;
				}
				i++;
			}
			DECODE_TABLE['O']=0;
			DECODE_TABLE['o']=0;
			DECODE_TABLE['I']=1;
			DECODE_TABLE['i']=1;
			DECODE_TABLE['L']=1;
			DECODE_TABLE['l']=1;
		}
	}
	
	protected static class B32R4648Seq extends SequenceUtil{
		public B32R4648Seq(){
			ALPHABET="ABCDEFGHIJKLMNOPQRSTUVWXYZ234567".toCharArray();
			radix=ALPHABET.length;
			DECODE_TABLE=new int[128];
			Arrays.fill(DECODE_TABLE, -1);
			int i=0;
			for(char c:ALPHABET){
				DECODE_TABLE[c]=i;
				if(c>='A'&&c<='Z'){
					DECODE_TABLE[c+32]=i;
				}
				i++;
			}
		}
	}
	protected static class B64Seq extends SequenceUtil{
		public B64Seq(){
			ALPHABET="ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/".toCharArray();
			radix=ALPHABET.length;
			DECODE_TABLE=new int[128];
			Arrays.fill(DECODE_TABLE, -1);
			int i=0;
			for(char c:ALPHABET){
				DECODE_TABLE[c]=i;
				i++;
			}
		}
	}
	protected static class AnSeq extends SequenceUtil{
		public AnSeq(){
			ALPHABET="0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz".toCharArray();
			radix=ALPHABET.length;
			DECODE_TABLE=new int[128];
			Arrays.fill(DECODE_TABLE, -1);
			int i=0;
			for(char c:ALPHABET){
				DECODE_TABLE[c]=i;
				i++;
			}
		}
	}

//	public static void main(String[] args) {
//		SequenceUtil s=SequenceUtil.DECIMAL;
//		System.out.println(s.plus("199"));
//		System.out.println(s.plus("1"));
//		System.out.println(s.plus("109"));
//		System.out.println(s.plus("009"));
//		System.out.println(s.plus("109",91));
////		System.out.println(s.plus("999"));
//		SequenceUtil s2=SequenceUtil.HEX;
//		System.out.println(s2.plus("199"));
//		System.out.println(s2.plus("1FF"));
//		System.out.println(s2.plus("1ff"));
//		System.out.println(s2.plus("1ee"));
//		System.out.println(s2.plus("1ef"));
//		System.out.println(s2.plus("1"));
//		System.out.println(s2.plus("10F"));
//		System.out.println(s2.plus("00F"));
//		System.out.println(s2.plus("10F",0xF1));
//		
//		SequenceUtil s3=SequenceUtil.ALPHABET_AND_NUMBER;
//		System.out.println(s3.plus("1zz"));
////		System.out.println(s3.plus("zzz"));
//	}
	
}
