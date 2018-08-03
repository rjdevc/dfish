package com.rongji.dfish.framework.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;

import com.rongji.dfish.base.DfishException;
import com.rongji.dfish.base.Page;
import com.rongji.dfish.framework.SystemData;

public class DAOWithMoni implements PubCommonDAO{
	PubCommonDAOImpl core;
	
	public void setHibernateTemplate(HibernateTemplate hibernateTemplate){
		core=new PubCommonDAOImpl();
		core.setHibernateTemplate(hibernateTemplate);
	}
	
	@Override
	public List<?> getQueryList(final String strSql, final Object... object) {
		return (List<?>) execute(strSql,new Work(){
			public Object run() {
				return core.getQueryList(strSql, object);
			}
		});
	}

	private Object execute(String sql,Work work) {
			long begin=System.currentTimeMillis();
			Object o=work.run();
			long last=System.currentTimeMillis()-begin;
			record(sql,last);
			return o;
	}
	public static void record(String sql,long last){
		Record r=records.get(sql);
		if(r==null){
			r=new Record();
			records.put(sql, r);
			r.sql=sql;
			r.max=last;
			r.min=last;
			r.times=1;
			r.total=last;
		}else{
			r.times++;
			r.total+=last;
			if(r.min>last){r.min=last;}
			if(r.max<last){r.max=last;}
		}
	}
	static long recordBegin=System.currentTimeMillis();
	static Map<String,Record> records=Collections.synchronizedMap(new HashMap<String,Record>());
	/**
	 * 记录下一次访问
	 */
	
	public static interface Work{
		public Object run();
	}
	public static class Record{
		String sql;
		int times;
		long max;
		long min;
		long total;
	}

	@Override
	public Object queryAsAnObject(final String strSql, final Object... object) {
		return  execute(strSql,new Work(){
			public Object run() {
				return core.queryAsAnObject(strSql, object);
			}
		});
	}

	@Override
	public List<?> getQueryList(final String strSql, final Page page, final Object... object) {
		 if (page == null) {
		        return getQueryList(strSql, object);
		    } else {
		        if (page.getAutoRowCount() == null) {
		            page.setAutoRowCount(true);
		        }
		    }
		    return getQueryList(strSql, page, page.getAutoRowCount(), object);
	}

	@Override
	public List<?> getQueryList(final String strSql, final Page page, final boolean autoGetRowCount, final Object... object) {
		final HibernateTemplate template = getHibernateTemplate();
		template.setCacheQueries(true);
		HibernateCallback<List<?>> action = new HibernateCallback<List<?>>() {
			public List<?> doInHibernate(Session session)
					throws HibernateException, SQLException {
				Query query = session.createQuery(strSql);
				if (object != null) {
					for (int i = 0; i < object.length; i++) {
						PubCommonDAOImpl.setArgument(query, i, object[i]);
					}
				}
				
				// ??????????
				if (page.getCurrentPage() != 0) {
					int pageno = page.getCurrentPage();
					query.setFirstResult((pageno - 1) * (page.getPageSize()));
					query.setMaxResults(page.getPageSize());
				}
				long begin=System.currentTimeMillis();
				List<?> resultList = query.list();
				record(strSql,System.currentTimeMillis()-begin);
				page.setCurrentCount(resultList.size());
				
				if (autoGetRowCount) {
					if(page.getPageSize()>resultList.size()&&(resultList.size()>0||page.getCurrentPage()==1)){
						// 2017-12-22 LinLW
						//如果查询结果一页都没有满，明显无需count
						//如果翻到某一页后突然没数据了，还是要去count实际大小。
						page.setRowCount(resultList.size()+(page.getCurrentPage()-1)*page.getPageSize());
					}else{
						// 2017-12-22 LinLW
						// 更新比较精确的截取的方式，应该用正则表达是而不是用 FROM前面加空格的做法。
						String upperSQL = strSql.toUpperCase();
						int firstForm=-1;
						Matcher m=Pattern.compile("\\bFROM\\b").matcher(upperSQL);
						if(m.find()){
							firstForm=m.start();
						}
						
						if(firstForm<0){
							throw new RuntimeException(new DfishException("无法对没有FROM关键字的HQL进行autoRowCount，建议设置page.setAutoRowCount(false);并自行计算数据行数","DFISH-01000"));
						}
						m=Pattern.compile("\\bDISTINCT\\b").matcher(upperSQL);
						if(m.find()){
							if(m.start()<firstForm){
								throw new RuntimeException(new DfishException("无法保证有DISTINCT关键字的HQL进行autoRowCount的结果正确性，建议设置page.setAutoRowCount(false);并自行计算数据行数","DFISH-01000"));
							}
						}
						int cur=0,lastOrderBy=upperSQL.length();
						m=Pattern.compile("\\bORDER +BY\\b").matcher(upperSQL);
						while(m.find(cur)){
							cur=m.end();
							lastOrderBy=m.start();
						}
						//如果EXISTS子语句有ORDER BY 但主语句没有的时候，可能会发生异常。
						if(lastOrderBy<upperSQL.length()){
							char[] orderSeq=strSql.substring(lastOrderBy).toCharArray();
							//如果 )比(多，我们则怀疑 这个ORDER BY 隶属于子查询。那么忽略掉这个ORDER BY 以提高正确率。
							int left=0,right=0;
							for(char c:orderSeq){
								if(c=='('){
									left++;
								}else if(c==')'){
									right++;
								}
							}
							if(left!=right){
								lastOrderBy=upperSQL.length();
							}
						}
						
						String strHql4cout = "select count(*) "+strSql.substring(firstForm,lastOrderBy);
						try{
							Query query1 = session.createQuery(strHql4cout);
							if (object != null) {
								for (int k = 0; k < object.length; k++) {
									PubCommonDAOImpl.setArgument(query1, k, object[k]);
								}
							}
							begin=System.currentTimeMillis();
							List<?> arrayList = query1.list();
							record("[DFishDAO]"+strHql4cout,System.currentTimeMillis()-begin);
							Integer inte = ((Number) arrayList.get(0)).intValue();
							page.setRowCount(inte.intValue());
						}catch(Exception ex){
							ex.printStackTrace();
							throw new RuntimeException(new DfishException("自动计算数据行数时发生未知错误，建议设置page.setAutoRowCount(false);并自行计算数据行数。\r\n"+strHql4cout,"DFISH-01000"));
						}
					}
				}
				
				//LinLW 2017-07-13 如果当前页号大于1 查询结果数为0，并且autoRowcount为true。很可能是最后一页被删了。要回头显示前面的页。
				if(resultList.size()==0 && page.getAutoRowCount()!=null && page.getAutoRowCount() && page.getCurrentPage()>1){
					//根据page的rowCount计算curpage的数量
					if(page.getRowCount()==0){
						page.setCurrentPage(1);
					}else{
						int pageno=(page.getRowCount()-1)/page.getPageSize()+1;
						page.setCurrentPage(pageno);
						query.setFirstResult((pageno - 1) * (page.getPageSize()));
						query.setMaxResults(page.getPageSize());
						resultList = query.list();
						page.setCurrentCount(resultList.size());
					}
				}
				
				return resultList;
			}
		};
		List<?> result = (List<?>) template.execute(action);
		// 当前记录数
		if (result != null) {
			page.setCurrentCount(result.size());
		}
		return result;
	}

	@Override
	public int deleteSQL(final String strSql, final Object... object) {
		return  (Integer) execute(strSql,new Work(){
			public Object run() {
				return core.deleteSQL(strSql, object);
			}
		});
	}

	@Override
	public void delete(final Object obj) {
		if(obj==null){
			return ;
		}
		execute("DELETE "+obj.getClass().getSimpleName(),new Work(){
			public Object run() {
				core.delete(obj);
				return null;
			}
		});
	}

	@Override
	public void save(final Object object) {
		if(object==null){
			return ;
		}
		execute("SAVE "+object.getClass().getSimpleName(),new Work(){
			public Object run() {
				core.save(object);
				return null;
			}
		});
	}

	@Override
	public void evictObject(Object object) {
		core.evictObject(object);
	}

	@Override
	public void update(final Object object) {
		if(object==null){
			return ;
		}
		execute("UPDATE "+object.getClass().getSimpleName(),new Work(){
			public Object run() {
				core.update(object);
				return null;
			}
		});
	}

	@Override
	public HibernateTemplate getHibernateTemplate() {
		// FIXME 这个时间统计不出来
		return core.getHibernateTemplate();
	}

	@Override
	public int bulkUpdate(final String queryString, final Object... values) {
		return  (Integer) execute(queryString,new Work(){
			public Object run() {
				return core.bulkUpdate(queryString, values);
			}
		});
	}

	@Override
	public int[] batchUpdate(final String[] hql) {
		if(hql==null||hql.length==0){
			return new int[0];
		}
		return  (int[]) execute(hql[0],new Work(){
			public Object run() {
				return core.batchUpdate(hql);
			}
		});
	}

	@Override
	public int[] batchUpdate(final String hql, final List<Object[]> args) {
		return  (int[]) execute(hql,new Work(){
			public Object run() {
				return core.batchUpdate(hql,args);
			}
		});
	}

	public void reset(){
		records.clear();
		recordBegin=System.currentTimeMillis();
	}
	public String trace(){
		List<Record> result=new ArrayList<Record>(records.values()); 
		if(result.size()==0){
			return "no result to show";
		}
		StringBuilder sb=new StringBuilder();
		sb.append("<html><head><title>DFish DAO Trace</title><style>" +
				"td{font-size:10px;}\r\n" +
				".tt td{font-size:12pt;font-weight:bold;color:#369;}\r\n" +
				".th td{background-color:#06C;color:#CCC;font-weight:bold;}\r\n" +
				".tr0 td{background-color:#FFC;}\r\n" +
				".tr1 td{background-color:#FFF;}\r\n" +
				"</style></head>\r\n<body>");
		long totalElapsed=0;
		int executions=0;
		for(Record r:result){
			totalElapsed+=r.total;
			executions+=r.times;
		}
		sb.append("<table><tr class='tr0'><td>DB URL</td><td>");
		sb.append(SystemData.getInstance().getDataBaseInfo().getDatabaseUrl());
		sb.append("</td></tr><tr class='tr1'><td>DB USER</td><td>");
		sb.append(SystemData.getInstance().getDataBaseInfo().getDatabaseUsername());
		sb.append("</td></tr><tr class='tr0'><td>Begin Monitor</td><td>");
		sb.append(new Date(recordBegin));
		sb.append("</td></tr><tr class='tr1'><td>End Monitor</td><td>");
		sb.append(new Date());
		sb.append("</td></tr><tr class='tr0'><td>Total Executions</td><td>");
		sb.append(executions);
		sb.append("</td></tr><tr class='tr1'><td>Total Elapsed Time(ms)</td><td>");
		sb.append(totalElapsed);
		sb.append("</td></tr></table>\r\n");
		
		Collections.sort(result,new Comparator<Record>(){
			public int compare(Record r0, Record r1) {
				long value=r1.total-r0.total;
				return value>0?1:(value<0?-1:0);
			}
		});
		sb.append("<table><tr class='tt'><td colspan='7'>SQL ordered by Elapsed Time</td></tr>");
		sb.append("<tr class='th'><td>&nbsp;</td><td>Elapsed Time(ms)</td><td>Executions</td><td>avg.(ms)</td><td>max(ms)</td><td>min(ms)</td><td>SQL Text</td></tr>\r\n");
		for(int i=0;i<20&&i<result.size();i++){
			Record r=result.get(i);
			if(i%2==0){
				sb.append("<tr class='tr0'><td>");
			}else{
				sb.append("<tr class='tr1'><td>");
			}
			sb.append(i+1);
			sb.append("</td><td>");
			sb.append(r.total);
			sb.append("</td><td>");
			sb.append(r.times);
			sb.append("</td><td>");
			sb.append(r.total/r.times);
			sb.append("</td><td>");
			sb.append(r.max);
			sb.append("</td><td>");
			sb.append(r.min);
			sb.append("</td><td>");
			sb.append(r.sql);
			sb.append("</td></tr>\r\n");
		}
		sb.append("</table>\r\n");
		Collections.sort(result,new Comparator<Record>(){
			public int compare(Record r0, Record r1) {
				return r1.times-r0.times;
			}
		});
		sb.append("<table><tr class='tt'><td colspan='3'>SQL ordered by Executions</td></tr>");
		sb.append("<tr class='th'><td>&nbsp;</td><td>Executions</td><td>SQL Text</td></tr>\r\n");
		for(int i=0;i<20&&i<result.size();i++){
			Record r=result.get(i);
			if(i%2==0){
				sb.append("<tr class='tr0'><td>");
			}else{
				sb.append("<tr class='tr1'><td>");
			}
			sb.append(i+1);
			sb.append("</td><td>");
			sb.append(r.times);
			sb.append("</td><td>");
			sb.append(r.sql);
			sb.append("</td></tr>\r\n");
		}
		sb.append("</table>\r\n");
		
		Collections.sort(result,new Comparator<Record>(){
			public int compare(Record r0, Record r1) {
				return (int)(r1.max-r0.max);
			}
		});
		sb.append("<table><tr class='tt'><td colspan='3'>SQL ordered by per Exec</td></tr>");
		sb.append("<tr class='th'><td>&nbsp;</td><td>max(ms)</td><td>SQL text</td></tr>\r\n");
		for(int i=0;i<20&&i<result.size();i++){
			Record r=result.get(i);
			if(i%2==0){
				sb.append("<tr class='tr0'><td>");
			}else{
				sb.append("<tr class='tr1'><td>");
			}
			sb.append(i+1);
			sb.append("</td><td>");
			sb.append(r.max);
			sb.append("</td><td>");
			sb.append(r.sql);
			sb.append("</td></tr>\r\n");
		}
		sb.append("</table>\r\n");
		sb.append("</body></html>");
		
		return sb.toString();
	}

}
