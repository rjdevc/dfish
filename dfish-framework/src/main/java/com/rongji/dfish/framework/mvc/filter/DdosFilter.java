package com.rongji.dfish.framework.mvc.filter;

import com.rongji.dfish.framework.FrameworkHelper;
import com.rongji.dfish.base.util.JsonUtil;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 *  基础的用于防止DDOS攻击的过滤器
 *  <p>工作原理是 短时间内（默认为10秒）同一个IP，同一个URI的访问 可以配置一个阈值(比如说30次)。超过这个数量的访问将会被禁止访问。
 *  允许不同URI社会的阈值不一样。</p>
 *  在web.xml一个典型的配置如下
 *  <pre>
 *  &lt;filter&gt;
 *    &lt;filter-name&gt;ddosFilter&lt;/filter-name&gt;
 *    &lt;filter-class&gt;com.rongji.dfish.framework.mvc.filter.DdosFilter&lt;/filter-class&gt;
 *    &lt;init-param&gt;
 *      &lt;param-name&gt;interval&lt;/param-name&gt;
 *      &lt;param-value&gt;10000&lt;/param-value&gt;
 *    &lt;/init-param&gt;
 *    &lt;init-param&gt;
 *      &lt;param-name&gt;uris&lt;/param-name&gt;
 *      &lt;param-value&gt;[{"uri":"/fzwp/webCallDetail/index","times":30},{"uri":"/fzwp/callDetail.jsp","times":30}]&lt;/param-value&gt;
 *    &lt;/init-param&gt;
 *  &lt;/filter&gt;
 *  &lt;filter-mapping&gt;
 *    &lt;filter-name&gt;ddosFilter&lt;/filter-name&gt;
 *    &lt;servlet-name&gt;*&lt;/servlet-name&gt;
 *  &lt;/filter-mapping&gt;
 *  </pre>
 *  uris配置为JSON格式。
 * @author DFishTeam
 *
 */
public class DdosFilter implements Filter{
	private static class UriConfig{
		private String uri;
		private Integer times;
		public String getUri() {
			return uri;
		}
		public void setUri(String uri) {
			this.uri = uri;
		}
		public Integer getTimes() {
			return times;
		}
		public void setTimes(Integer times) {
			this.times = times;
		}
	}
	private static class VisitCount{
		private long visitTime;
		private AtomicInteger times;
	}
	
	private Map<String,Integer> uriConfigs;
	private Map<String,VisitCount> visited=Collections.synchronizedMap(new HashMap<String,VisitCount>());
	private long interval=10000L;
	private long clear=3600L*1000;
	private long lastClear=0L;
	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse rsp, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest request=(HttpServletRequest)req;
		String ip= FrameworkHelper.getIpAddr(request);
		String uri=request.getRequestURI();
		if(uriConfigs.get(uri)!=null){
			long now=System.currentTimeMillis();
			VisitCount vc=visited.get(ip+"|"+uri);
			if(vc==null){
				vc=new VisitCount();
				vc.times=new AtomicInteger(0);
				vc.visitTime=System.currentTimeMillis();
				visited.put(ip+"|"+uri, vc);
			}
			if(now>vc.visitTime+interval){
				vc.times.set(0);
				vc.visitTime=now;
			}
			int i=vc.times.getAndIncrement();
			if(i>=uriConfigs.get(uri)){
				HttpServletResponse response=(HttpServletResponse)rsp;
				response.sendError(HttpServletResponse.SC_FORBIDDEN,"BUSY. TRY IT LATER.");
				return;
			}
			if(now>lastClear+clear){
				lastClear=now;
				HashSet<String> toRemove=new HashSet<String>();
				for(Map.Entry<String, VisitCount> entry:visited.entrySet()){
					if(now>entry.getValue().visitTime+interval){
						toRemove.add(entry.getKey());
					}
				}
				for(String key:toRemove){
					visited.remove(key);
				}
			}
		}
		chain.doFilter(request, rsp);
	}

	@Override
	public void init(FilterConfig config) throws ServletException {
		uriConfigs=new HashMap<String,Integer>();
		try{
			String json = config.getInitParameter("uris");
			List<UriConfig> uriConfigList = JsonUtil.parseArray(json, UriConfig.class);
			for(UriConfig c:uriConfigList){
				uriConfigs.put(c.getUri(), c.getTimes());
			}
		}catch(Exception ex){
			FrameworkHelper.LOG.error("init DdosFilter uris fails", ex);
		}
		try{
			String strInterval=config.getInitParameter("interval");
			interval=Long.parseLong(strInterval);
		}catch(Exception ex){
			FrameworkHelper.LOG.error("init DdosFilter interval fails", ex);
		}
	}

}
