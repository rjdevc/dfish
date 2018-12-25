package com.rongji.dfish.uitemp;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class MatchResult  implements Iterable<DFishTemplate>{
	public static final int REL_AND=1;
	public static final int REL_OR=2;
	public static final int OPER_EQUALS=1;
	public static final int OPER_NOT_EQUAL=0;
	public static final int OPER_GREATER_THAN=2;
	public static final int OPER_LESS_THAN=4;
	
	public MatchResult(){
		
	}
	List<Object> curResult;
	public MatchResult(DFishTemplate dt){
		if(dt instanceof AbstractTemplate){
			curResult=new ArrayList<Object>();
			AbstractTemplate cast=(AbstractTemplate)dt;
			curResult.add(cast.json);
		}
	}
	public void setProp(String key,String value){
		for(DFishTemplate dt:this){
			if(dt instanceof WidgetTemplate ){
				((WidgetTemplate) dt).setProp(key, value);
			}
		}
	}
	public void at(String key,String value){
		for(DFishTemplate dt:this){
			if(dt instanceof WidgetTemplate ){
				((WidgetTemplate) dt).at(key, value);
			}
		}
	}
	
	public MatchResult match(String prop,Object value){
		return search(new Condition[]{
			new Condition(prop,OPER_EQUALS,value),
		},REL_AND);
	}
	
	public MatchResult findById(String id){
		return search(new Condition[]{
				new Condition("id",OPER_EQUALS,id),
		},REL_AND);
	}
	/***
	 * 相当于
	 * <pre>
	 * return search(new Condition[]{
	 * 	new Condition("type",OPER_EQUALS,type),
	 * 	new Condition("name",OPER_EQUALS,name),
	 * },REL_AND);
	 * </pre>
	 * @param type
	 * @param name
	 * @return
	 */
	public MatchResult findByTypeAndName(String type, String name){
		return search(new Condition[]{
			new Condition("type",OPER_EQUALS,type),
			new Condition("name",OPER_EQUALS,name),
		},REL_AND);
	}
	
	public MatchResult search(String path){
		List<Object> ret=new ArrayList<Object>();
		if(curResult!=null&&path!=null){
			outter:for(Object o:curResult){
				Object cur=o;
				for(String token:path.split("[/]")){
					if(token==null||token.equals("")){
						continue;
					}
					if(isNumeric(token)){
						if(cur instanceof JSONArray){
							JSONArray arr=(JSONArray)cur;
							cur=arr.get(Integer.parseInt(token));
						}else{
							continue outter;
						}
					}else{
						if(cur instanceof JSONObject){
							JSONObject jo=(JSONObject)cur;
							cur=jo.get(token);
						}else{
							continue outter;
						}
					}
					if(cur==null){
						continue outter;
					}
				}
				ret.add(cur);
			}
		}
		MatchResult mr=new MatchResult();
		mr.curResult=ret;
		return mr;
	}
	private static Pattern pattern = Pattern.compile("[0-9]*");
	private static boolean isNumeric(String str){
        Matcher isNum = pattern.matcher(str);
        return isNum.matches() ;
	}
	
	public MatchResult search(Condition[] conditions, int realtion){
		List<Object> ret=new ArrayList<Object>();
		if(curResult!=null){
			for(Object o:curResult){
				searchDown(conditions,realtion,o, ret);
			}
		}
		MatchResult mr=new MatchResult();
		mr.curResult=ret;
		return mr;
	}

	//FIXME 暂时没有完整路径的的做法，只能先search 再search，而且无法根据上级属性名路径来搜索
	protected void searchDown(Condition[] conditions, int relation,Object source,  List<Object> resultHolder) {
		if (source instanceof JSONObject){
			JSONObject cast=(JSONObject)source;
			//进行判定
			boolean check=relation==REL_AND;
			if(check){
				for(Condition con:conditions){
					boolean step=judge(cast,con.prop,con.oper,con.value);
					if(!step){
						check=false;
						break;
					}
				}
			}else{
				for(Condition con:conditions){
					boolean step=judge(cast,con.prop,con.oper,con.value);
					if(step){
						check=true;
						break;
					}
				}
			}
			if(check){
				resultHolder.add(source);
			}
			//item 的子节点需要继续进行搜索。
			for(Object prop:cast.values()){
				searchDown(conditions, relation,prop,  resultHolder);
			}
		}else if (source instanceof JSONArray){
			JSONArray cast=(JSONArray)source;
			for(Object prop:cast){
				searchDown(conditions, relation,prop,  resultHolder);
			}
		}
	}
	private boolean judge(JSONObject cast, String prop, int oper, Object value) {
		Object o=cast.get(prop);
		switch(oper){
		case OPER_EQUALS: return o!=null&&o.equals(value);
		case OPER_NOT_EQUAL: return o!=null&&!o.equals(value);
		case OPER_GREATER_THAN:
		case OPER_LESS_THAN:{
			if(value==null||o==null){
				return false;
			}
			try{
				int com=((Comparable)o).compareTo((Comparable)value);
				return OPER_LESS_THAN==oper?com<0:com>0;
			}catch(Exception ex){
				throw new RuntimeException("Can NOT compare "+o+" ("+o.getClass().getName()+") with "+value+" ("+value.getClass().getName()+")");
			}
		}
		default :return false;
		}
	}
	@Override
	public Iterator<DFishTemplate> iterator() {
		return new ResultIterator(curResult);
	}
	public List<DFishTemplate> getResult(){
		List<DFishTemplate> ret=new ArrayList<DFishTemplate>();
		for(Object  o: curResult){
			if(o instanceof JSONArray){
				ret.add(new TemplateArray(o));
			}else if(o instanceof JSONObject){
				ret.add(new WidgetTemplate(o));
			}
			throw new RuntimeException("unknown result");
		}
		return ret;
	}
	public static class Condition{
		private String prop;
		private int oper;
		private Object value;
		public Condition(){}
		public Condition(String prop,int oper,Object value){
			this.prop=prop;
			this.oper=oper;
			this.value=value;
		}
		public String getProp() {
			return prop;
		}
		public void setProp(String prop) {
			this.prop = prop;
		}
		public int getOper() {
			return oper;
		}
		public void setOper(int oper) {
			this.oper = oper;
		}
		public Object getValue() {
			return value;
		}
		public void setValue(Object value) {
			this.value = value;
		}
	}
	private static class ResultIterator implements Iterator<DFishTemplate>{
		Iterator<Object> real;
		public ResultIterator(List<Object> curResult){
			real=curResult.iterator();
		}
		@Override
		public boolean hasNext() {
			return real.hasNext();
		}
		@Override
		public DFishTemplate next() {
			Object o=real.next();
			if(o instanceof JSONArray){
				return new TemplateArray(o);
			}else if(o instanceof JSONObject){
				return new WidgetTemplate(o);
			}
			throw new RuntimeException("unknown result");
		}
	}
}
