package com.rongji.dfish.framework;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.rongji.dfish.ui.json.J;
public class OvertimeFilter implements Filter {
	private Log LOG=LogFactory.getLog(OvertimeFilter.class);
	private long time=1500;
	@Override
	public void destroy() {
		
	}

	@Override
	public void doFilter(ServletRequest srequest, ServletResponse sresponse, FilterChain fc) throws IOException, ServletException {
		long begin=System.currentTimeMillis();
		try{
			fc.doFilter(srequest, sresponse);
			long end=System.currentTimeMillis();
			if(end-begin>time){
				LOG.warn("request cost "+(end-begin)+"ms. request=\r\n"+convert2JSON((HttpServletRequest)srequest));
			}
		}catch(IOException t){
			long end=System.currentTimeMillis();
			LOG.error("request cost "+(end-begin)+"ms and it cause "+t.getClass().getName()+". message="+t.getMessage()+" request=\r\n"+convert2JSON((HttpServletRequest)srequest));
			throw t;
		}catch(ServletException t){
			long end=System.currentTimeMillis();
			LOG.error("request cost "+(end-begin)+"ms and it cause "+t.getClass().getName()+". message="+t.getMessage()+" request=\r\n"+convert2JSON((HttpServletRequest)srequest));
			throw t;
		}catch(RuntimeException t){
			long end=System.currentTimeMillis();
			LOG.error("request cost "+(end-begin)+"ms and it cause "+t.getClass().getName()+". message="+t.getMessage()+" request=\r\n"+convert2JSON((HttpServletRequest)srequest));
			throw t;
		}catch(Throwable t){
			long end=System.currentTimeMillis();
			LOG.error("request cost "+(end-begin)+"ms and it cause "+t.getClass().getName()+". message="+t.getMessage()+" request=\r\n"+convert2JSON((HttpServletRequest)srequest));
			throw new ServletException(t);
		}
	}

	@Override
	public void init(FilterConfig fc) throws ServletException {
		String timeStr =fc.getInitParameter("time");
		if(timeStr!=null){
			try{
				time=Long.parseLong(timeStr);
			}catch(Exception ex){}
		}
	}
	
	private static String convert2JSON(HttpServletRequest request){
		Map<String,Object> json=new LinkedHashMap<String,Object>();
		Map<String,Object> headMap=new LinkedHashMap<String,Object>();
		Map<String,String[]> paramMap=request.getParameterMap();
		Map<String,Object> sessionMap=new LinkedHashMap<String,Object>();
		json.put("requestURI", request.getRequestURI());
		json.put("head", headMap);
		json.put("parameter", paramMap);
		json.put("session", sessionMap);
		sessionMap.put(FrameworkHelper.LOGIN_USER_KEY, FrameworkHelper.getLoginUser(request));
		if(request.getHeaderNames()!=null){
			java.util.Enumeration<String> headNames=request.getHeaderNames();
			while(headNames.hasMoreElements()){
				String n=headNames.nextElement();
				List<String> headers=new ArrayList<String>();
				if(request.getHeaders(n)!=null){
					headMap.put(n, headers);
					java.util.Enumeration<String> headContent=request.getHeaders(n);
					while(headContent.hasMoreElements()){
						String v=headContent.nextElement();
						headers.add(v);
					}
				}
			}
		}
		
		return J.toJson(json);
	}
}
