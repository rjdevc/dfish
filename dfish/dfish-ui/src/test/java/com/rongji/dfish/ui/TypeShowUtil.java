package com.rongji.dfish.ui;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.rongji.dfish.ui.json.J;

public class TypeShowUtil{
	
	private static Map<String,Map<String,Set<String>>> typeMap=new HashMap<String,Map<String,Set<String>>>(); 
	private static final String ANY_TYPE="*";
	private static final String ANY_TYPE_PATH="/"+ANY_TYPE;
	

	static {
		generateTypeMap();
	}
	
	
	public static void main(String[] args) {
		boolean isShowType=TypeShowUtil.showType(Arrays.asList(new String[] {"node","vert","nodes","buttonbar","nodes"}), "button");
		System.out.println(isShowType);
	    isShowType=TypeShowUtil.showType(Arrays.asList(new String[] {"node","vert","nodes","button","pub"}), "button");
		System.out.println(isShowType);
	}

	
	public static boolean showType(List<String> path,String curType) {
		
		if(path == null || path.size() <= 0) {
			return true;
		}
		
		Map<String,Set<String>> treeMap=typeMap.get(curType);
		if(treeMap==null) {
			return showType4AnyType(path);
		}else {
			boolean isShowType=!matchNextSubType(treeMap,"/"+curType,0,path);
			if(isShowType) {
				return showType4AnyType(path);
			}
		}
		return false;
		
	}

	private static boolean showType4AnyType(List<String> path) {
		Map<String,Set<String>> treeMap=typeMap.get(ANY_TYPE);
		if(treeMap==null) {
			return true;
		}
		return !matchNextSubType(treeMap,ANY_TYPE_PATH,0,path);
	}
	
	private static boolean matchNextSubType(Map<String, Set<String>> treeMap, String parentTypePath, int level,List<String> path) {
		
		Set<String> subList=treeMap.get(parentTypePath);
		if(subList==null) {
			return true;
		}
		
		int pathLen=path.size();
		for(String curType:subList) {
			level++;
			if(pathLen-level<=0) {
				break;
			}
			if(curType.equals(path.get(pathLen-level))||ANY_TYPE.equals(curType)) {
				if(matchNextSubType(treeMap,parentTypePath+'/'+curType,level,path)) {
					return true;
				}
			} 
			level--;
		}
		return false;
	}


	private static void generateTypeMap() {
		
		String[][] hidTypeDefs=getHidTypeDefDatas();
		
		Map<String, Set<String>> treeMap = null;
		Set<String> subList = null;
		StringBuilder sb=null;
		int rowLen;
		for (String[] row : hidTypeDefs) {
			rowLen = row.length;
			treeMap = typeMap.get(row[rowLen - 1]);
			if (treeMap == null) {
				treeMap = new HashMap<String, Set<String>>();
				typeMap.put(row[rowLen - 1], treeMap);
			}
			sb=new StringBuilder();
			for (int i = rowLen - 1; i > 0; i--) {
				sb.append('/').append(row[i]);
				subList = treeMap.get(sb.toString());
				if (subList == null) {
					subList = new HashSet<String>();
					treeMap.put(sb.toString(), subList);
				}
				if (i > 0) {
					subList.add(row[i - 1]);
				}
			}
		}
		System.out.println(J.formatJson( typeMap));
	}
	
	private static String[][] getHidTypeDefDatas(){
		return new String[][]{
				{"pub","button"},
				{"hiddens","*"},
				{"options","*"},
				{"buttonbar","nodes","button"},
				{"button","*","button"},
				{"button","*","leaf","*","button"},
				{"album","nodes","img"},
				{"tree","nodes","leaf"},
				{"leaf","nodes","leaf"}};
	}
}
