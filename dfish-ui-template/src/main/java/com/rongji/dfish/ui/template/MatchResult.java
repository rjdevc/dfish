package com.rongji.dfish.ui.template;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * 用于对模板元素进行查找和操作的工具类
 * 
 *
 */
public class MatchResult  implements Iterable<DFishTemplate>{
	public static final int REL_AND=1;
	public static final int REL_OR=2;
	public static final int OPER_EQUALS=1;
	public static final int OPER_NOT_EQUAL=0;
	public static final int OPER_GREATER_THAN=2;
	public static final int OPER_LESS_THAN=4;
	
	public MatchResult(){}
	
	List<Object> curResult;
	/**
	 * 初始结果 一般 dt为WidgetTemplate 或 TemplateArray
	 * @param dt DFishTemplate
	 */
	public MatchResult(DFishTemplate dt){
		if(dt instanceof AbstractTemplate){
			curResult=new ArrayList<Object>();
			AbstractTemplate cast=(AbstractTemplate)dt;
			curResult.add(cast.json);
		}
	}
	/**
	 * 为了方便 批量设置值，而制作的方法。只对结果中WidgetTemplate生效。
	 * @param prop 属性
	 * @param value 值 一般 为 String / Integer / Double / Boolean
	 */
	public void setProp(String prop,Object value){
		for(DFishTemplate dt:this){
			if(dt instanceof WidgetTemplate ){
				((WidgetTemplate) dt).setProp(prop, value);
			}
		}
	}
	
	/**
	 * 为了方便 批量设置值，而制作的方法。只对结果中WidgetTemplate生效。
	 * @param prop 属性
	 * @param expr 表达式
	 */
	public void at(String prop,String expr){
		for(DFishTemplate dt:this){
			if(dt instanceof WidgetTemplate ){
				((WidgetTemplate) dt).at(prop, expr);
			}
		}
	}
	/**
	 * 为了方便 批量设置值，而制作的方法。只对结果中WidgetTemplate生效。
	 * @param prop 属性
	 * @param temp 模板
	 */
	public void at(String prop,DFishTemplate temp){
		for(DFishTemplate dt:this){
			if(dt instanceof WidgetTemplate ){
				((WidgetTemplate) dt).at(prop, temp);
			}
		}
	}
	/**
	 * 找出 属性等于指定值 的节点
	 * @param prop 属性名
	 * @param value 值
	 * @return MatchResult
	 */
	public MatchResult match(String prop,Object value){
		return search(new Expression[]{
			new Expression(prop,OPER_EQUALS,value),
		},REL_AND);
	}
	
	/**
	 * 找出 id 属性等于指定值的节点
	 * 如果widget遵循 dfish的原则，这里只应该返回一个结果。
	 * @param id String
	 * @return MatchResult
	 */
	public MatchResult findById(String id){
		return search(new Expression[]{
				new Expression("id",OPER_EQUALS,id),
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
		return search(new Expression[]{
			new Expression("type",OPER_EQUALS,type),
			new Expression("name",OPER_EQUALS,name),
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
	
	/**
	 * 找出所有子节点中符合条件的节点。
	 * @param exprs 表达式
	 * @param realtion 关系 AND 还是OR
	 * @return
	 */
	public MatchResult search(Expression[] exprs, int realtion){
		List<Object> ret=new ArrayList<Object>();
		if(curResult!=null){
			for(Object o:curResult){
				searchDown(exprs,realtion,o, ret);
			}
		}
		MatchResult mr=new MatchResult();
		mr.curResult=ret;
		return mr;
	}

	//FIXME 暂时没有完整路径的的做法，只能先search 再search，而且无法根据上级属性名路径来搜索
	protected void searchDown(Expression[] conditions, int relation,Object source,  List<Object> resultHolder) {
		if (source instanceof JSONObject){
			JSONObject cast=(JSONObject)source;
			//进行判定
			boolean check=relation==REL_AND;
			if(check){
				for(Expression con:conditions){
					boolean step=judge(cast,con.prop,con.oper,con.value);
					if(!step){
						check=false;
						break;
					}
				}
			}else{
				for(Expression con:conditions){
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
				@SuppressWarnings({ "rawtypes", "unchecked" })
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
	/**
	 * 表达式	
	 *
	 */
	public static class Expression{
		private String prop;
		private int oper;
		private Object value;
		public Expression(){}
		/**
		 * 构造函数
		 * @param prop 属性名
		 * @param oper 操作 EQUALS NOT_EQUAL LESS_THAN GREATER_THAN
		 * @param value 值
		 */
		public Expression(String prop,int oper,Object value){
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
			if(curResult==null){
				real=new Iterator<Object>() {
					public boolean hasNext() {
						return false;
					}
					public Object next() {
						return null;
					}
					@Override
					public void remove() {
						throw new UnsupportedOperationException("remove");
					}
				};
			}else{
				real=curResult.iterator();
			}
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
		@Override
		public void remove() {
			throw new UnsupportedOperationException("remove");
		}
	}
}
