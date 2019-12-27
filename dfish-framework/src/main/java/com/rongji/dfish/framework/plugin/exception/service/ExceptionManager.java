package com.rongji.dfish.framework.plugin.exception.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import org.hibernate.HibernateException;
import org.hibernate.classic.Session;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.orm.hibernate3.HibernateCallback;

import com.rongji.dfish.base.util.StringUtil;
import com.rongji.dfish.framework.FrameworkHelper;
import com.rongji.dfish.framework.IdGenerator;
import com.rongji.dfish.framework.dao.PubCommonDAO;
import com.rongji.dfish.framework.plugin.exception.entity.PubExptConstant;
import com.rongji.dfish.framework.plugin.exception.entity.PubExptRecord;
import com.rongji.dfish.framework.plugin.exception.entity.PubExptStack;
import com.rongji.dfish.framework.plugin.exception.entity.PubExptType;

public class ExceptionManager {
	private static ExceptionManager instance;
	private final static Executor SNGL_EXEC = Executors.newSingleThreadExecutor();
	private PubCommonDAO dao;
	
	public boolean isEnabled() {
		try {
			String lockString = FrameworkHelper.getSystemConfig("CONFIG_EXCEPTION_ENABLE", "false"); 
			return Boolean.parseBoolean(lockString);
        } catch (Exception e) {
        }
		return true;
	}
	public boolean toggleEnable() {
		boolean enable = isEnabled();
		FrameworkHelper.setSystemConfig("CONFIG_EXCEPTION_ENABLE", String.valueOf(!enable));
		return enable;
	}
	private ExceptionManager(){
		dao=FrameworkHelper.getDAO();
		this.init();
	}
	public static ExceptionManager getInstance(){
		if (instance == null) {
			synchronized (ExceptionManager.class) {
	            if (instance == null) {
	            	instance = new ExceptionManager();
	            }
            }
		}
		return instance;
	}
	private void init() {
		if (isEnabled()) {// 默认关闭时不获取数据
			@SuppressWarnings("unchecked")
            List<PubExptConstant> constants=(List<PubExptConstant>) dao.getQueryList("FROM PubExptConstant c ORDER BY c.conId");
			for (PubExptConstant constant : constants) {
				revConstMap.put(constant.getConId(), constant.getConName());
				constMap.put(constant.getConName(), constant.getConId());
			}
		}
	}
	public void log(Throwable t){
		if(!isEnabled()){
			return;
		}
		if(t==null){
			return;
		}
		final long now=System.currentTimeMillis();
		SNGL_EXEC.execute(new LogAction(t,now));
	}
	private class LogAction implements Runnable {
		private Throwable t;
		private long oc;
		private LogAction(Throwable t, long oc) {
			this.t=t;
			this.oc=oc;
		}

		@Override
		public void run() {
			ExceptionManager ep=ExceptionManager.getInstance();
			long excepid=ep.getExceptionId(t);
			ep.log(excepid,oc,t.getMessage());
		}
	}
	/**
	 * 异常类的原型，根据JDK的异常规范，异常应该包含 cause name msg 和stack这里忽略掉msg，并把所有带有重复心智的内容转为简单的映射类型。
	 * @author DFishTeam
	 *
	 */
	private class ExceptionTypeInfoPrototype{
		int _hash;
		long cause;
		int name;
		List<StackInfoPrototype> stack;
		@Override
		public int hashCode(){
			if(_hash==0){
				_hash= (int)(cause ^ (cause >>> 32))^name^stack.hashCode();
			}
			return _hash;
		}
		@Override
		public boolean equals(Object o){
			if(o==this) {
				return true;
			}
			if(o==null) {
				return false;
			}
			if(!(o instanceof ExceptionTypeInfoPrototype)){
				return false;
			}
			ExceptionTypeInfoPrototype cast=(ExceptionTypeInfoPrototype)o;
			return cause==cast.cause&&name==cast.name&&stack.equals(cast.stack);
		}
	}
	/**
	 * 异常堆栈每一条信息的原型 根据JDK的异常规范，栈的每一条包含 class method file 和line 这里把复杂类型都转为简单的映射类型。
	 * @author DFishTeam
	 *
	 */
	private class StackInfoPrototype{
		int line;
		int file;
		int clz;
		int method;
		@Override
		public int hashCode(){
			return line ^ file ^ clz ^method;
		}
		@Override
		public boolean equals(Object o){
			if(o==this) {
				return true;
			}
			if(o==null) {
				return false;
			}
			if(!(o instanceof StackInfoPrototype)){
				return false;
			}
			StackInfoPrototype cast=(StackInfoPrototype)o;
			return line==cast.line&&file==cast.file&&clz==cast.clz&&method==cast.method;
		}
	}
	
	private class BatchLogger{
		private Map<Long , Long> logCount = Collections.synchronizedMap(new HashMap<Long, Long>()); 
		private static final long DEFAULT_LOG_TIME = 60*1000;
		private long lastLogTime= System.currentTimeMillis();
		//FIXME 做了并发处理，但是性能上待提升
		private synchronized void log(PubExptRecord rec,long excepid ){
			if ((logCount.get(excepid))==null) {
				logCount.put(excepid, 1L);
				exptRecords.put(excepid, rec);
				saveOrUpdateLog(rec,1);
				//FIXME 如果多机执行的时候，需要做异常处理，以及计数的累加
			}else {
				logCount.put(excepid,logCount.get(excepid)+1);
				//60s一个周期  开始处理本周期的数据
				if ((System.currentTimeMillis()-lastLogTime)>DEFAULT_LOG_TIME) {
					for(Map.Entry<Long, Long> entry : logCount.entrySet()) {
						Long repetitions=entry.getValue();
						Long mapExcepid=entry.getKey();
						if (repetitions>1) {
							saveOrUpdateLog(exptRecords.get(mapExcepid),repetitions);
						}
					}
					logCount.clear();
					lastLogTime=System.currentTimeMillis();
				}
			}
	 }
	}
	
	private void saveOrUpdateLog(PubExptRecord rec,long exptRepetitions){
		rec.setExptRepetitions(exptRepetitions);
		if (exptRepetitions>1) {
			dao.update(rec);
		}else {
			rec.setRecId(IdGenerator.getSortedId32());
			dao.save(rec);
		}
			
	}
	private BatchLogger batchLogger = new BatchLogger();
	//记录excepid 和 对应的record
	private Map<Long,PubExptRecord> exptRecords= Collections.synchronizedMap(new HashMap<Long, PubExptRecord>());
	//记录excepid 和出现的次数
	/**
	 * 将数据写入数据
	 * @param excepid
	 * @param oc
	 * @param message
	 */
	protected void log(long excepid, long oc, String message) {
		PubExptRecord rec=new PubExptRecord();
	 	rec.setEventTime(oc);
		rec.setExptMsg(StringUtil.shortenStringUTF8(message, 255,  ""));
		rec.setTypeId(excepid);
		batchLogger.log(rec, excepid);
	}
	@SuppressWarnings("unchecked")
    protected long findOrCreateException(final ExceptionTypeInfoPrototype ei) {
		// 转载的时候可能会同时装载多个Exception 比如说nullpointerException 并且没cause的情况，可能会通知装载很多信息。
		//填充过程可能需要constas 反向MAP
		List<PubExptType> types=(List<PubExptType>) dao.getQueryList("FROM PubExptType t WHERE t.className=? AND t.causeId=?", ei.name,ei.cause);
		List<PubExptStack> stacks=(List<PubExptStack>) dao.getQueryList("FROM PubExptStack t2 WHERE EXISTS (SELECT t.causeId FROM PubExptType t WHERE t.typeId=t2.typeId AND t.className=? AND t.causeId=?) ORDER BY t2.stackOrder ASC", ei.name,ei.cause);
		for(PubExptType expetionType :types){
			long typeId=expetionType.getTypeId();
			ExceptionTypeInfoPrototype eiItem=new ExceptionTypeInfoPrototype();
			eiItem.cause=expetionType.getCauseId();
			eiItem.name=expetionType.getClassName();
			eiItem.stack=new ArrayList<StackInfoPrototype>();
			for(PubExptStack stack:stacks){
				if(stack.getTypeId()!=typeId){
					continue;
				}
				StackInfoPrototype si=new StackInfoPrototype();
				eiItem.stack.add(si);
				si.clz=stack.getClassName();
				si.file=stack.getFileName();
				si.method=stack.getMethodName();
				si.line=stack.getLineNumber();
			}
			revExceptionInfoMap.put(typeId, eiItem);
			exceptionInfoMap.put(eiItem, typeId);
		}
		if(exceptionInfoMap.get(ei)==null){
			Number o=knownMaxTypeId;
			boolean fail=true;
			do{
				try{
					if(o==null){
						o=(Number)dao.queryAsAnObject("SELECT MAX(t.typeId) FROM PubExptType t ");
					}
					long i=o==null?0L:o.longValue();
					i++;
					o=i;
					PubExptType t=new PubExptType();
					t.setTypeId(o.longValue());
					t.setCauseId(ei.cause);
					t.setClassName(ei.name);
					dao.save(t);
					knownMaxTypeId=o;
					fail=false;
				}catch(ConstraintViolationException e){
					o=knownMaxTypeId=null;
				}catch(Throwable e){
					e.printStackTrace();
					break;
					//可能有多机执行，引起主键冲突。
				}
			}while(fail);
			//循环加入stack
				fail=true;
				Number stackId=knownMaxStackId;
				
				do{
					try{
						if(stackId==null){
							stackId=(Number)dao.queryAsAnObject("SELECT MAX(t.stackId) FROM PubExptStack t ");
						}
						final long i=stackId==null?0L:stackId.longValue();
						final Number initTypeId=o;
						
						// 批量
						dao.getHibernateTemplate().execute(new HibernateCallback<Object>(){
							@Override
							public Object doInHibernate(org.hibernate.Session session)
									throws HibernateException, SQLException {
								long id=i;
								for(int order=0;order<ei.stack.size();order++){
									id++;
									StackInfoPrototype si=ei.stack.get(order);
									
									PubExptStack t=new PubExptStack();
									t.setStackId(id);
									t.setTypeId(initTypeId.longValue());
									t.setStackOrder(order);
									t.setClassName(si.clz);
									t.setFileName(si.file);
									t.setLineNumber(si.line);
									t.setMethodName(si.method);
									session.save(t);
								}
								return null;
							}
						});
						knownMaxStackId=i+ei.stack.size();
						fail=false;
					}catch(ConstraintViolationException e){
						stackId=knownMaxStackId=null;
					}catch(Throwable e){
						e.printStackTrace();
						break;
						//可能有多机执行，引起主键冲突。
					}
				}while(fail);
			revExceptionInfoMap .put(o.longValue(), ei);
			exceptionInfoMap.put(ei, o.longValue());
		}
		return exceptionInfoMap.get(ei);
	}
	private Number knownMaxTypeId=null;
	private Number knownMaxStackId=null;
	
	private int maxConstIdHint=-1;
	protected int findOrCreateConst(String name) {
		Number o=(Number)dao.queryAsAnObject("SELECT t.conId FROM PubExptConstant t WHERE t.conName=?", name);
		if(o==null){
			boolean fail=true;
			do{
				try{
					if(maxConstIdHint<0){
						o=(Number)dao.queryAsAnObject("SELECT MAX(t.conId) FROM PubExptConstant t ");
						maxConstIdHint=o==null?0:o.intValue();
					}
					o=++maxConstIdHint;
					PubExptConstant con=new PubExptConstant();
					con.setConId(maxConstIdHint);
					con.setConName(name);
					dao.save(con);
					fail=false;
				}catch(ConstraintViolationException e){
					o=(Number)dao.queryAsAnObject("SELECT MAX(t.conId) FROM PubExptConstant t ");
					maxConstIdHint=o==null?0:o.intValue();
				}catch(Throwable e){
					e.printStackTrace();
					break;
					//可能有多机执行，引起主键冲突。
				}
			}while(fail);
		}
		int i=o.intValue();
		//需要转载MAP
		revConstMap.put(i, name);
		constMap.put(name, i);
		return i;
	}
	
	protected String findConst(int id) {
		if(id==0){
			return null;
		}
		String name=(String)dao.queryAsAnObject("SELECT t.conName FROM PubExptConstant t WHERE t.conId=?", id);
		if(name==null){
			return null;
		}
		//需要转载MAP
		revConstMap.put(id, name);
		constMap.put(name, id);
		return name;
	}
	@SuppressWarnings("unchecked")
    protected ExceptionTypeInfoPrototype findException(long id) {
		if(id==0){
			return null;
		}
		PubExptType type=(PubExptType)dao.queryAsAnObject("FROM PubExptType t WHERE t.typeId=?", id);
		if(type==null){
			return null;
		}
		ExceptionTypeInfoPrototype  type_=new ExceptionTypeInfoPrototype();
		List<PubExptStack> stacks=(List<PubExptStack>)dao.getQueryList("FROM PubExptStack t WHERE t.typeId=? ORDER BY t.stackOrder ASC", id);
		type_.cause=type.getCauseId();
		type_.name=type.getClassName();
		type_.stack=new ArrayList<ExceptionManager.StackInfoPrototype>(stacks.size());
		for(PubExptStack stack:stacks){
			StackInfoPrototype si=new StackInfoPrototype();
			type_.stack.add(si);
			si.clz=stack.getClassName();
			si.file=stack.getFileName();
			si.line=stack.getLineNumber();
			si.method=stack.getMethodName();
		}
		//需要转载MAP
		revExceptionInfoMap.put(id, type_);
		exceptionInfoMap.put(type_, id);
//		revConstMap.put(id, name);
//		constMap.put(name, id);
		return type_;
	}

	
	
	private Map<Long,ExceptionTypeInfoPrototype> revExceptionInfoMap=new HashMap<Long,ExceptionTypeInfoPrototype>();
	private Map<Integer,String> revConstMap=new HashMap<Integer,String>();
	
	private Map<ExceptionTypeInfoPrototype,Long> exceptionInfoMap=new HashMap<ExceptionTypeInfoPrototype,Long>();
	private Map<String,Integer> constMap=new HashMap<String,Integer>();
	
	/**
	 * 根据异常的类型，cause和exceptionStack获得异常的ID。
	 * @param t
	 * @return
	 */
	private long getExceptionId(Throwable t) {
		if(t==null){
			return 0;
		}
		ExceptionTypeInfoPrototype ei=parseExceptionInfo(t);
		Long l=exceptionInfoMap.get(ei);
		if(l==null){
			l=findOrCreateException(ei);
		}
		return l;
	}
	
	private ExceptionTypeInfoPrototype parseExceptionInfo(Throwable t) {
		long causeId=getExceptionId(t.getCause());
		int name=getConst(t.getClass().getName());
		StackTraceElement[] es= t.getStackTrace();
		List<StackInfoPrototype> ls=new ArrayList<StackInfoPrototype>(es.length);
		if(es!=null){
			for(StackTraceElement e:es){
				StackInfoPrototype si=new StackInfoPrototype();
				ls.add(si);
				si.clz=getConst(e.getClassName());
				if(e.getFileName()!=null){
					si.file=getConst(e.getFileName());
				}
				si.line=e.getLineNumber();
				si.method=getConst(e.getMethodName());
			}
		}
		ExceptionTypeInfoPrototype ei=new ExceptionTypeInfoPrototype();
		ei.cause=causeId;
		ei.name=name;
		ei.stack=ls;
		return ei;
	}
	private int getConst(String name){
		Integer i=constMap.get(name);
		if(i==null){
			i=findOrCreateConst(name);
		}
		return i;
	}
	private String getConst(int id){
		String name=revConstMap.get(id);
		if(name==null){
			name=findConst(id);
		}
		return name;
	}
	public ExceptionTypeInfo getExceptionType(long typeId){
		ExceptionTypeInfo et=new ExceptionTypeInfo();
		ExceptionTypeInfoPrototype et_=getExceptionTypeInner(typeId);
		if(et_==null){
			return null;
		}
		et.setClassName(getConst(et_.name));
		et.setCause(getExceptionType(et_.cause));
		List<StackInfo> stacks=new ArrayList<StackInfo>(et_.stack.size());
		et.setStackTrace(stacks);
		for(StackInfoPrototype si_:et_.stack){
			StackInfo si=new StackInfo();
			stacks.add(si);
			si.setClassName(getConst(si_.clz));
			si.setFileName(getConst(si_.file));
			si.setLineNumber(si_.line);
			si.setMethodName(getConst(si_.method));
		}
		
		return et;
	}
	private ExceptionTypeInfoPrototype getExceptionTypeInner(long typeId) {
		ExceptionTypeInfoPrototype type=revExceptionInfoMap.get(typeId);
		if(type==null){
			type=findException(typeId);
		}
		return type;
	}
	public void clear(boolean clearAll) {
		if(clearAll){
			revConstMap.clear();
			constMap.clear();
		}
		revExceptionInfoMap.clear();
		exceptionInfoMap.clear();
	}
	
	@SuppressWarnings("unchecked")
    public void removerepit() {
		revExceptionInfoMap.clear();
		exceptionInfoMap.clear();
		revConstMap.clear();
		constMap.clear();
		
		//判断class和causeId相同的情况
		List<Object[]> classAndCauseIdList = (List<Object[]>) dao.getQueryList("SELECT className,causeId FROM PubExptType");
		for (Object[] objects : classAndCauseIdList) {
			List<Long > typeIdList = (List<Long >)dao.getQueryList
					("SELECT typeId FROM PubExptType where className="+objects[0]+" and causeId="+objects[1]);
			Map<Object, Object> stackId =new HashMap<Object, Object>();
			//stack 判断
			for (Long   typeId : typeIdList) {
				List<StackInfoPrototype> stackInfo_s = new ArrayList<ExceptionManager.StackInfoPrototype>();
				StackInfoPrototype stackInfo_ = new StackInfoPrototype();
				List<PubExptStack> stacks =new ArrayList<PubExptStack>();
				stacks=(List<PubExptStack>) dao.getQueryList(" FROM PubExptStack where typeId="+typeId);
				for (PubExptStack pubExptStack : stacks) {
					stackInfo_.clz=pubExptStack.getClassName();
					stackInfo_.file=pubExptStack.getFileName();
					stackInfo_.method=pubExptStack.getMethodName();
					stackInfo_.line=pubExptStack.getLineNumber();
					stackInfo_s.add(stackInfo_);
				}
				Long  keyTypeId=(Long ) stackId.get(stackInfo_);
				if (null==keyTypeId) {
					stackId.put(stackInfo_, typeId);
				}else {
					Session session =FrameworkHelper.getDAO().getHibernateTemplate().getSessionFactory().openSession();
					session.createSQLQuery("UPDATE pub_Expt_Record SET Type_Id = "+keyTypeId+ " WHERE Type_Id = "+typeId).executeUpdate();
					session.close();
					dao.deleteSQL("from PubExptStack where typeId = "+typeId);
					dao.deleteSQL("from PubExptType where typeId = "+typeId);
				}
			}
		}
	}
	
}
