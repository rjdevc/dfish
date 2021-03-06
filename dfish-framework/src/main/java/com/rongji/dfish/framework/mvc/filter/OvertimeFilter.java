package com.rongji.dfish.framework.mvc.filter;

import com.rongji.dfish.base.util.JsonUtil;
import com.rongji.dfish.base.util.LogUtil;
import com.rongji.dfish.framework.FrameworkHelper;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 对传入的参数进行过滤配置
 */
public class OvertimeFilter implements Filter {
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
				LogUtil.warn(getClass(), "request cost "+(end-begin)+"ms. request=\r\n"+convert2JSON((HttpServletRequest)srequest));
			}
		}catch(IOException|ServletException|RuntimeException t){
			long end=System.currentTimeMillis();
			LogUtil.error(getClass(), "request cost "+(end-begin)+"ms and it cause "+t.getClass().getName()+". message="+t.getMessage()+" request=\r\n"+convert2JSON((HttpServletRequest)srequest), t);
			throw t;
		}catch(Throwable t){
			long end=System.currentTimeMillis();
			LogUtil.error(getClass(), "request cost "+(end-begin)+"ms and it cause "+t.getClass().getName()+". message="+t.getMessage()+" request=\r\n"+convert2JSON((HttpServletRequest)srequest), t);
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
	
	private static String convert2JSON(HttpServletRequest request) {
		Map<String,Object> jsonMap= new LinkedHashMap<>();
		Map<String,Object> headMap= new LinkedHashMap<>();
		Map<String,String[]> paramMap=request.getParameterMap();
		Map<String,Object> sessionMap= new LinkedHashMap<>();
		jsonMap.put("requestURI", request.getRequestURI());
		jsonMap.put("head", headMap);
		jsonMap.put("parameter", paramMap);
		jsonMap.put("session", sessionMap);
		sessionMap.put(FrameworkHelper.LOGIN_USER_KEY, FrameworkHelper.getLoginUser(request));
		if(request.getHeaderNames()!=null){
			java.util.Enumeration<String> headNames=request.getHeaderNames();
			while(headNames.hasMoreElements()){
				String n=headNames.nextElement();
				List<String> headers= new ArrayList<>();
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

		return JsonUtil.toJson(jsonMap);
	}
}
