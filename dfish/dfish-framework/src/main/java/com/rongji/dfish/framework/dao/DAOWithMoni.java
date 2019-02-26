package com.rongji.dfish.framework.dao;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.rongji.dfish.framework.SystemData;

public class DAOWithMoni extends  PubCommonDAOImpl{
	

	protected  void log(String sql,long last){
		record(sql, last);
		super.log(sql, last);
	}

	public void record(String sql,long last){
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

	public static class Record{
		String sql;
		int times;
		long max;
		long min;
		long total;
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
