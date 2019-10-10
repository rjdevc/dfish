package com.rongji.dfish.ui.json;

import java.util.Stack;

/**
 * ArrayJsonBuilder 为数组的转化器
 * @author DFish Team
 *
 */
public  class ArrayJsonBuilder extends AbstractJsonBuilder {
	public void buildJson(Object o, StringBuilder sb,Stack<PathInfo> path) {
		Object[] cast=(Object[])o;
		boolean appended=false;
		sb.append('[');
		int i=0;
		for(Object item:cast){
			if(appended){
				sb.append(',');
			}else {
				appended=true;
			}
			path.push(new PathInfo("["+(i++)+"]",item));
			J.buildJson(item, sb,path);
			path.pop();
		}
		sb.append(']');
	}

	/**
	 * ByteArrayJsonBuilder 是为基本数据类型 byte[]而准备的转化器
	 * byte[]无法转化成Object[]
	 */
	public  static class ByteArrayJsonBuilder extends AbstractJsonBuilder{
		public void buildJson(Object o, StringBuilder sb,Stack<PathInfo> path) {
			byte[] cast=(byte[])o;
			boolean appended=false;
			sb.append('[');
			for(byte item:cast){
				if(appended)sb.append(',');else appended=true;
				sb.append(item);
			}
			sb.append(']');
		}
	}
	/**
	 * CharArrayJsonBuilder 是为基本数据类型 char[]而准备的转化器
	 * char[]无法转化成Object[]
	 */
	public  static class CharArrayJsonBuilder extends AbstractJsonBuilder{
		public void buildJson(Object o, StringBuilder sb,Stack<PathInfo> path) {
			char[] cast=(char[])o;
			boolean appended=false;
			sb.append('[');
			for(char item:cast){
				if(appended)sb.append(',');else appended=true;
				sb.append(item);
			}
			sb.append(']');
		}
	}
	/**
	 * DoubleArrayJsonBuilder 是为基本数据类型 double[]而准备的转化器
	 * double[]无法转化成Object[]
	 */	
	public  static class DoubleArrayJsonBuilder extends AbstractJsonBuilder{
		public void buildJson(Object o, StringBuilder sb,Stack<PathInfo> path) {
			double[] cast=(double[])o;
			boolean appended=false;
			sb.append('[');
			for(double item:cast){
				if(appended)sb.append(',');else appended=true;
				sb.append(item);
			}
			sb.append(']');
		}
	}
	/**
	 * FloatArrayJsonBuilder 是为基本数据类型 float[]而准备的转化器
	 * float[]无法转化成Object[]
	 */	
	public  static class FloatArrayJsonBuilder extends AbstractJsonBuilder{
		public void buildJson(Object o, StringBuilder sb,Stack<PathInfo> path) {
			float[] cast=(float[])o;
			boolean appended=false;
			sb.append('[');
			for(float item:cast){
				if(appended)sb.append(',');else appended=true;
				sb.append(item);
			}
			sb.append(']');
		}
	}
	/**
	 * IntArrayJsonBuilder 是为基本数据类型 int[]而准备的转化器
	 * int[]无法转化成Object[]
	 */		
	public  static class IntArrayJsonBuilder extends AbstractJsonBuilder{
		public void buildJson(Object o, StringBuilder sb,Stack<PathInfo> path) {
			int[] cast=(int[])o;
			boolean appended=false;
			sb.append('[');
			for(int item:cast){
				if(appended)sb.append(',');else appended=true;
				sb.append(item);
			}
			sb.append(']');
		}
	}
	/**
	 * LongArrayJsonBuilder 是为基本数据类型 long[]而准备的转化器
	 * long[]无法转化成Object[]
	 */	
	public  static class LongArrayJsonBuilder extends AbstractJsonBuilder{
		public void buildJson(Object o, StringBuilder sb,Stack<PathInfo> path) {
			long[] cast=(long[])o;
			boolean appended=false;
			sb.append('[');
			for(long item:cast){
				if(appended)sb.append(',');else appended=true;
				sb.append(item);
			}
			sb.append(']');
		}
	}
	/**
	 * ShortArrayJsonBuilder 是为基本数据类型 short[]而准备的转化器
	 * short[]无法转化成Object[]
	 */	
	public  static class ShortArrayJsonBuilder extends AbstractJsonBuilder{
		public void buildJson(Object o, StringBuilder sb,Stack<PathInfo> path) {
			short[] cast=(short[])o;
			boolean appended=false;
			sb.append('[');
			for(short item:cast){
				if(appended)sb.append(',');else appended=true;
				sb.append(item);
			}
			sb.append(']');
		}
	}
	/**
	 * BooleanArrayJsonBuilder 是为基本数据类型 boolean[]而准备的转化器
	 * boolean[]无法转化成Object[]
	 */	
	public  static class BooleanArrayJsonBuilder extends AbstractJsonBuilder{
		public void buildJson(Object o, StringBuilder sb,Stack<PathInfo> path) {
			boolean[] cast=(boolean[])o;
			boolean appended=false;
			sb.append('[');
			for(boolean item:cast){
				if(appended)sb.append(',');else appended=true;
				sb.append(item);
			}
			sb.append(']');
		}
	}
}
