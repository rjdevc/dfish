package com.rongji.dfish.framework.mvc.filter;

/**
 * 对IPV4协议进行处理
 * 
 */
public class IPv4 implements java.io.Serializable,java.lang.Comparable<Object>{
	private static final long serialVersionUID = 8891234771470437121L;
	
	private int v;

	/**
	 * 构造函数
	 * @param s
	 */
	public IPv4(String s){
		String[] p=s.split("[.]");
		v= (Integer.parseInt(p[0])<<24)|
			(Integer.parseInt(p[1])<<16)|
			(Integer.parseInt(p[2])<<8)|
			Integer.parseInt(p[3]);
	}

	/**
	 * 构造函数
	 * @param l
	 */
	public IPv4(long l){
		v= (int)l;
	}
	
	/**
	 * 构造函数
	 * @param b
	 */
	public IPv4(byte[] b){
		v= (b[0]<<24)|(b[1]<<16)|(b[2]<<8)|b[3];
	}

	@Override
	public String toString(){
		StringBuilder sb=new StringBuilder();
		sb.append((v>>24)&0xFF).append('.').append((v>>16)&0xFF)
		.append('.').append((v>>8)&0xFF)
		.append('.').append((v)&0xFF);
		return sb.toString();
	}

	@Override
	public int hashCode(){
		return v;
	}

	public byte[] toByteArray(){
		return null;
	}
	public long toLong(){
		return (long)v&0xFFFFFFFFL;
	}
	
	@Override
	public boolean equals(Object o){
		if(o==null) {
            return false;
        }
		if(o==this) {
            return true;
        }
		if(o instanceof IPv4){
			IPv4 ins=(IPv4)o;
			return ins.v==v;
		}else{
			return false;
		}
	}
	


	@Override
	public int compareTo(Object o) {
		IPv4 ip=(IPv4)o;
		long l= ((long)v&0xFFFFFFFFL) - ((long)ip.v&0xFFFFFFFFL);
		return l>0?1:(l==0?0:-1);
	}
}
