package com.rongji.dfish.framework.plugin.exception.core;

import com.rongji.dfish.base.util.LogUtil;
import com.rongji.dfish.base.util.StringUtil;
import com.rongji.dfish.base.util.Utils;
import com.rongji.dfish.framework.FrameworkHelper;
import com.rongji.dfish.framework.plugin.exception.entity.PubExceptionConstant;
import com.rongji.dfish.framework.plugin.exception.entity.PubExceptionRecord;
import com.rongji.dfish.framework.plugin.exception.entity.PubExceptionStack;
import com.rongji.dfish.framework.plugin.exception.entity.PubExceptionType;
import com.rongji.dfish.framework.plugin.exception.service.PubExceptionService;
import com.rongji.dfish.framework.service.IdGenerator;

import java.util.*;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * ExceptionManager 能将异常信息，以压缩的方式放入到数据库中去。
 * 使用该功能需要进行以下配置
 * <p>A) 确保commons-logging 版本较新，能够唤起log4j2 而不是log4j 已知1.1.3版是可用的。如果不是的话，请参考ExceptionAppender自行想办法将log中的throwable转接到ExceptionManager</p>
 * <p>B) log4j2.xml中appnders应该有&lt;ExceptionAppender name="ExceptionAppender" /&gt; 并且loggers中应该配置使用它。例如
 * &lt;Logger name="com.rongji" level="DEBUG"&gt;&lt;AppenderRef ref="ExceptionAppender"/&gt;&lt;/Logger&gt;</p>
 * <p>C) spring环境中应该有配置 &lt;bean id="pubExceptionService" class="com.rongji.dfish.framework.plugin.exception.service.impl.PubExceptionServiceImpl"/&gt;
 * 以及&lt;bean id="pubExceptionDao" class="com.rongji.dfish.framework.hibernate5.plugin.exception.dao.impl.PubExceptionDao4Hibernate"/&gt;
 * 如果工程不是hibernate的，请自行实现数据入库</p>
 * <p>D) 如果工程是spring+hibernate的，检查确认spring环境中txAdvice，事务控制中create开头的方法不是只读的。&lt;tx:method name="create*"/&gt;并且
 * sessionFactory中packagesToScan 应该包含entities路径 &lt;value&gt;com.rongji.dfish.framework.plugin.*&lt;/value&gt;</p>
 * <p>E) 如果要使用默认的查看器，可以用扩展于AbstractExceptionViewerController 写一个Controller。指定谁能查看，如果accpet直接返回true则是所有人都能看。
 * 该Controller 需要自行配置 @Controller和路径。界面端可以参考demo的exception_viewer.html和/t/plugin/exception下的JS</p>
 *
 * <p>至于压缩的原理则是利用JDK字节码自己的特性</p>。
 * <p>我们的一个异常的堆栈不短。
 * 但看堆栈的每一行，可以发现，其实都是 at 类名.方法名 (文件名 行号)
 * 而 类名 方法名 文件名 如果做成常量。(参见JAVA 字节码中的UTF常量)。
 * 那整个堆栈其实只要用几个数字表示。
 * 并且如果同一个异常，多次爆发，其实堆栈都是一样的。异常堆栈本身只要记录一次。
 * 而单独记录下异常发生的时间和当时的消息即可。</p>
 * @see com.rongji.dfish.framework.plugin.exception.AbstractExceptionViewerController
 * @sse com.rongji.dfish.framework.plugin.exception.core.ExceptionAppender
 */
public class ExceptionManager {
	private static ExceptionManager instance;
	private final static Executor SNGL_EXEC = Executors.newSingleThreadExecutor();
	private boolean inited;
	private boolean shutdown;
	private PubExceptionService pubExceptionService;

	private ExceptionManager(){

	}
	public void shutdown(){
		shutdown=true;
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
		pubExceptionService =FrameworkHelper.getBean(PubExceptionService.class);
		if(pubExceptionService ==null){
			this.shutdown=true;
		}else {
			try{
				// 确认建表
                pubExceptionService.createTables();

			}catch (Throwable t){t.printStackTrace();}
			// 读取
			List<PubExceptionConstant> constants = pubExceptionService.listAllConstants();
			for (PubExceptionConstant constant : constants) {
				revConstMap.put(constant.getConId(), constant.getConName());
				constMap.put(constant.getConName(), constant.getConId());
			}
		}
		inited=true;

	}
	public void save(Throwable t){
		if(t==null||shutdown){
			return;
		}
		final long now=System.currentTimeMillis();
		SNGL_EXEC.execute(new LogAction(t,now));
	}

    public String getStackAsString(long typeId) {
        StringBuilder sb = new StringBuilder();
//		long typeId = Long.parseLong(request.getParameter("typeId"));
//        pubExceptionService.getExceptionType(typeId);
//		FrameworkHelper.getDAO().queryAsAnObject("FROM PubExptType t WHERE t.typeId=?", typeId);
		ExceptionTypeInfo et = ExceptionManager.getInstance().getExceptionType(typeId);
		ExceptionTypeInfo cur = et;

		while (true) {
			sb.append(cur.getClassName());
			sb.append("\r\n");
			for (StackInfo info : cur.getStackTrace()) {
				sb.append("\tat ");
				sb.append(info.getClassName());
				sb.append('.');
				Utils.escapeXMLword( info.getMethodName(),sb);
				if (info.getFileName() != null) {
					sb.append('(');
					sb.append(info.getFileName());
					if (info.getLineNumber() > 0) {
						sb.append(' ');
						sb.append(info.getLineNumber());
					}
					sb.append(')');
				}
				sb.append("\r\n");
			}
			cur = cur.getCause();
			if (cur == null) {
				break;
			}
			sb.append("Cause by ");
		}
		return sb.toString();
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
//            pubExceptionService =FrameworkHelper.getBean(PubExceptionDao.class);
//            pubExceptionService.ensureEntities();

			if(!inited){
				init();
			}
			ExceptionManager ep=ExceptionManager.getInstance();
			long excepid=ep.getExceptionId(t);
			ep.save(excepid,oc,t.getMessage());
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
		private synchronized void save(PubExceptionRecord rec ){
			try {
			    long excepid=rec.getTypeId();
				if ((logCount.get(excepid)) == null) {
					logCount.put(excepid, 1L);
					exptRecords.put(excepid, rec);
					rec.setExptRepetitions(1L);
					pubExceptionService.save(rec);
					//FIXME 如果多机执行的时候，需要做异常处理，以及计数的累加
				} else {
					logCount.put(excepid, logCount.get(excepid) + 1);
					//60s一个周期  开始处理本周期的数据
					if ((System.currentTimeMillis() - lastLogTime) > DEFAULT_LOG_TIME) {
						for (Map.Entry<Long, Long> entry : logCount.entrySet()) {
							Long repetitions = entry.getValue();
							Long mapExcepid = entry.getKey();
							if (repetitions > 1) {
								PubExceptionRecord rec_ = exptRecords.get(mapExcepid);
								rec_.setExptRepetitions(repetitions);
								pubExceptionService.update(rec_);
							}
						}
						logCount.clear();
						lastLogTime = System.currentTimeMillis();
					}
				}
			}catch (Exception ex){
				LogUtil.info("can not record exception : "+ex.getClass().getName());
				ex.printStackTrace();
			}
	    }
	}
	
//	private void saveOrUpdateLog(PubExceptionRecord rec,long exptRepetitions){
//		rec.setExptRepetitions(exptRepetitions);
//		if (exptRepetitions>1) {
//			dao.update(rec);
//		}else {
//			rec.setRecId(IdGenerator.getSortedId32());
//			dao.save(rec);
//		}
//
//	}
	private BatchLogger batchLogger = new BatchLogger();
	//记录excepid 和 对应的record
	private Map<Long,PubExceptionRecord> exptRecords= Collections.synchronizedMap(new HashMap<Long, PubExceptionRecord>());
	//记录excepid 和出现的次数
	/**
	 * 将数据写入数据
	 * @param excepid
	 * @param oc
	 * @param message
	 */
	protected void save(long excepid, long oc, String message) {
		PubExceptionRecord rec=new PubExceptionRecord();
		rec.setRecId(IdGenerator.getSortedId32());
	 	rec.setEventTime(oc);
		rec.setExptMsg(StringUtil.shortenStringUTF8(message, 255,  ""));
		rec.setTypeId(excepid);
		batchLogger.save(rec);
	}
	@SuppressWarnings("unchecked")
    protected long findOrCreateException(final ExceptionTypeInfoPrototype ei) {
		// 转载的时候可能会同时装载多个Exception 比如说nullpointerException 并且没cause的情况，可能会通知装载很多信息。
		//填充过程可能需要constas 反向MAP
        List<PubExceptionType> types= pubExceptionService.findExceptionTypesByName(ei.name,ei.cause);
        List<PubExceptionStack> stacks= pubExceptionService.findExceptionStacksByName(ei.name,ei.cause);
		for(PubExceptionType expetionType :types){
			long typeId=expetionType.getTypeId();
			ExceptionTypeInfoPrototype eiItem=new ExceptionTypeInfoPrototype();
			eiItem.cause=expetionType.getCauseId();
			eiItem.name=expetionType.getClassName();
			eiItem.stack=new ArrayList<StackInfoPrototype>();
			for(PubExceptionStack stack:stacks){
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
            PubExceptionType type=new PubExceptionType();
            type.setCauseId(ei.cause);
            type.setClassName(ei.name);

            List <PubExceptionStack> stacks_= new ArrayList<PubExceptionStack>();
            for(int order=0;order<ei.stack.size();order++){
                StackInfoPrototype si=ei.stack.get(order);
                PubExceptionStack st=new PubExceptionStack();
                st.setStackOrder(order);
                st.setClassName(si.clz);
                st.setFileName(si.file);
                st.setLineNumber(si.line);
                st.setMethodName(si.method);
                stacks_.add(st);
            }

		    long typeId= pubExceptionService.saveType(type,stacks_);

			revExceptionInfoMap.put(typeId, ei);
			exceptionInfoMap.put(ei, typeId);
		}
		return exceptionInfoMap.get(ei);
	}


	@SuppressWarnings("unchecked")
    protected ExceptionTypeInfoPrototype findException(long id) {
		if(id==0){
			return null;
		}
		if(!inited){
		    init();
        }
		PubExceptionType type= pubExceptionService.getExceptionType(id);
		if(type==null){
			return null;
		}
		ExceptionTypeInfoPrototype  type_=new ExceptionTypeInfoPrototype();
		List<PubExceptionStack> stacks= pubExceptionService.findExceptionStacks(id);
		type_.cause=type.getCauseId();
		type_.name=type.getClassName();
		type_.stack=new ArrayList<StackInfoPrototype>(stacks.size());
		for(PubExceptionStack stack:stacks){
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
			i= pubExceptionService.createOrFindConstByName(name);
            revConstMap.put(i, name);
            constMap.put(name, i);
		}
		return i;
	}
	private String getConst(int id){
		String name=revConstMap.get(id);
		if(name==null){
            name= pubExceptionService.findConst(id);
		}
        if(name!=null) {
            revConstMap.put(id, name);
            constMap.put(name, id);
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

//	@SuppressWarnings("unchecked")
//    public void removerepit() {
//		revExceptionInfoMap.clear();
//		exceptionInfoMap.clear();
//		revConstMap.clear();
//		constMap.clear();
//
//		//判断class和causeId相同的情况
//		List<Object[]> classAndCauseIdList = (List<Object[]>) dao.getQueryList("SELECT className,causeId FROM PubExceptionType");
//		for (Object[] objects : classAndCauseIdList) {
//			List<Long > typeIdList = (List<Long >)dao.getQueryList
//					("SELECT typeId FROM PubExceptionType where className="+objects[0]+" and causeId="+objects[1]);
//			Map<Object, Object> stackId =new HashMap<Object, Object>();
//			//stack 判断
//			for (Long typeId : typeIdList) {
//				List<StackInfoPrototype> stackInfo_s = new ArrayList<StackInfoPrototype>();
//				StackInfoPrototype stackInfo_ = new StackInfoPrototype();
//				List<PubExceptionStack> stacks =new ArrayList<PubExceptionStack>();
//				stacks=(List<PubExceptionStack>) dao.getQueryList(" FROM PubExceptionStack where typeId="+typeId);
//				for (PubExceptionStack pubExptStack : stacks) {
//					stackInfo_.clz=pubExptStack.getClassName();
//					stackInfo_.file=pubExptStack.getFileName();
//					stackInfo_.method=pubExptStack.getMethodName();
//					stackInfo_.line=pubExptStack.getLineNumber();
//					stackInfo_s.add(stackInfo_);
//				}
//				Long keyTypeId=(Long) stackId.get(stackInfo_);
//				if (null==keyTypeId) {
//					stackId.put(stackInfo_, typeId);
//				}else {
//					Session session =FrameworkHelper.getDAO().getHibernateTemplate().getSessionFactory().openSession();
//					session.createSQLQuery("UPDATE pub_Expt_Record SET Type_Id = "+keyTypeId+ " WHERE Type_Id = "+typeId).executeUpdate();
//					session.close();
//					dao.deleteSQL("from PubExceptionStack where typeId = "+typeId);
//					dao.deleteSQL("from PubExceptionType where typeId = "+typeId);
//				}
//			}
//		}
//	}
	
}
